package com.Incident_Service.Service;

import com.Incident_Service.Dto.*;
import com.Incident_Service.Enums.IncidentStatus;
import com.Incident_Service.Enums.IncidentType;
import com.Incident_Service.FeignClient.LocationServiceClient;
import com.Incident_Service.FeignClient.LogServiceClient;
import com.Incident_Service.FeignClient.ResponderServiceClient;
import com.Incident_Service.FeignClient.VictimServiceClient;
import com.Incident_Service.Model.Incident;
import com.Incident_Service.Repository.IncidentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import javax.xml.stream.Location;
import javax.xml.stream.Location;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final LocationServiceClient locationServiceClient;
    private final ResponderServiceClient responderServiceClient;
    private final VictimServiceClient victimServiceClient;
    private final LogServiceClient logServiceClient;


    @Autowired
    public IncidentService(
            IncidentRepository incidentRepository,
            LocationServiceClient locationServiceClient,
            ResponderServiceClient responderServiceClient,
            VictimServiceClient victimServiceClient,
            LogServiceClient logServiceClient
    )
    {
        this.incidentRepository = incidentRepository;
        this.locationServiceClient = locationServiceClient;
        this.responderServiceClient = responderServiceClient;
        this.victimServiceClient = victimServiceClient;
        this.logServiceClient = logServiceClient;
    }


    public IncidentDTO reportIncident(IncidentDTO dto) {
        if (dto == null || dto.getType() == null) throw new IllegalArgumentException("Incident type required");
        validateLocation(dto.getIncidentLocation());
        String address = dto.getIncidentLocation().getAddress();
        if (address != null && address.length() > 255) throw new IllegalArgumentException("Incident address too long");
        LocationDTO location = determineLocation(dto.getIncidentLocation());
        Incident incident = createIncident(dto, location.getLocationId());
        incident.setReportedAddress(address == null || address.isBlank() ? null : address.strip());
        boolean hasName = dto.getVictimName() != null && !dto.getVictimName().isBlank()
                && !"Anonymous".equals(dto.getVictimName());
        boolean hasContact = dto.getVictimContact() != null && !dto.getVictimContact().isBlank();
        if (dto.getType() != IncidentType.SOS_REQUEST && (hasName || hasContact)) {
            LocationDTO victimLocation = dto.getVictimLocation() == null ? location : determineLocation(dto.getVictimLocation());
            incident.setVictimId(handleVictimInformation(dto, victimLocation.getLocationId()));
        }
        // Repository transaction commits before any downstream dispatch/logging call.
        incident = incidentRepository.saveAndFlush(incident);
        IncidentDTO receipt = new IncidentDTO();
        receipt.setIncidentId(incident.getIncidentId());
        receipt.setType(incident.getType());
        receipt.setTimestamp(incident.getTimestamp());
        receipt.setStatus(incident.getStatus());
        receipt.setIncidentLocation(withReportedAddress(location, incident));
        // Best effort only. A durable retry/outbox is a separately reviewed follow-up.
        try { logIncident(incident, location); }
        catch (RuntimeException e) { log.warn("Incident stored; audit delivery not confirmed"); }
        try { assignResponder(incident); }
        catch (RuntimeException e) { log.warn("Incident stored; dispatch not confirmed"); }
        return receipt;
    }

    private void validateLocation(LocationDTO location) {
        if (location == null || location.getLatitude() == null || location.getLongitude() == null
                || !Double.isFinite(location.getLatitude()) || !Double.isFinite(location.getLongitude())
                || Math.abs(location.getLatitude()) > 90 || Math.abs(location.getLongitude()) > 180) {
            throw new IllegalArgumentException("Valid incident coordinates required");
        }
    }

    private Incident createIncident(IncidentDTO dto, int incidentLocation) {
        Incident incident = new Incident();
        incident.setType(dto.getType());
        incident.setTimestamp(LocalDateTime.now());
        incident.setStatus(IncidentStatus.NEW);  // This is where status is set


        // Ensure the location ID is set from the saved/found location
        if(incidentLocation > 0) {
            incident.setLocationId(incidentLocation);
        }
        else {
            throw new RuntimeException("Invalid location for incident");
        }
        return incident;
    }

    private LocationDTO determineLocation(LocationDTO location) {
        validateLocation(location);
        try {
            // First, check if location already exists
            Optional<LocationDTO> existingLocation = locationServiceClient.findByLatitudeAndLongitude(
                    location.getLatitude(),
                    location.getLongitude()
            );

            // If location exists, return the existing location
            if (existingLocation.isPresent()) {
                return existingLocation.get();
            }
            // If no existing location, create a new one
            LocationDTO newLocation = new LocationDTO();
            newLocation.setLatitude(location.getLatitude());
            newLocation.setLongitude(location.getLongitude());
            newLocation.setAddress(location.getAddress());

            LocationDTO savedLocation = locationServiceClient.createLocation(newLocation);

            newLocation.setLocationId(savedLocation.getLocationId());
            return savedLocation;

        } catch (Exception e) {
            throw new RuntimeException("Location determination failed: " + e.getMessage(), e);
        }
    }

    private Integer handleVictimInformation(IncidentDTO dto, int victimLocation) {
        VictimDTO victim = new VictimDTO();
        victim.setName(dto.getVictimName());
        victim.setContactInfo(dto.getVictimContact());
        victim.setLocationId(victimLocation);
        return victimServiceClient.createVictim(victim).getVictimId();
    }

    private void logIncident(Incident incident, LocationDTO location) {
        LogDTO log = new LogDTO();
        log.setStatusUpdate("Incident stored; responder assignment unconfirmed");
        log.setTimestamp(LocalDateTime.now());
        log.setIncidentId(incident.getIncidentId());
        logServiceClient.createLog(log);
    }

    private void assignResponder(Incident incident) {
        // Call responder service to assign an appropriate responder
        responderServiceClient.assignResponder(incident);
    }

    public IncidentStatus getIncidentStatus(Integer id) {
        return incidentRepository.findById(id).map(Incident::getStatus).orElse(null);
    }

    public List<IncidentDTO> getAllIncidents() {
        return incidentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<IncidentDTO> getIncidentById(Integer id) {

        return incidentRepository.findById(id).map(this::convertToDTO);
    }
    public List<IncidentDTO> getIncidentsByStatus(IncidentStatus status) {
        return incidentRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void updateIncidentStatus(int incidentId, IncidentStatus status) {

        Incident incident = incidentRepository.findById(incidentId).orElseThrow(()-> new RuntimeException("Incident not found"));
        incident.setStatus(status);


        incidentRepository.save(incident);
    }

    public void deleteIncident(Integer id) {
        incidentRepository.deleteById(id);
    }

    public List<IncidentDTO> getIncidentsByVictimId(Integer victimId) {
        return incidentRepository.findByVictimId(victimId).stream()
                .map(incident -> {
                    IncidentDTO dto = new IncidentDTO();
                    dto.setIncidentId(incident.getIncidentId());
                    dto.setType(incident.getType());
                    dto.setTimestamp(incident.getTimestamp());
                    dto.setStatus(incident.getStatus());
                    dto.setIncidentLocation(withReportedAddress(locationServiceClient.getLocationById(incident.getLocationId()), incident));
//                    dto.setIncidentLocationId(incident.getLocationId());
                    dto.setVictimId(incident.getVictimId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private IncidentDTO convertToDTO(Incident incident) {
        IncidentDTO dto = new IncidentDTO();
        dto.setIncidentId(incident.getIncidentId());
        dto.setType(incident.getType());
        dto.setTimestamp(incident.getTimestamp());
        dto.setStatus(incident.getStatus());
        dto.setIncidentLocation(withReportedAddress(locationServiceClient.getLocationById(incident.getLocationId()), incident));

        // Fetch additional details from other services
        if (incident.getVictimId() != null) {
            VictimDTO victim = victimServiceClient.getVictimById(incident.getVictimId());
            dto.setVictimId(victim.getVictimId());
            dto.setVictimName(victim.getName());
            dto.setVictimContact(victim.getContactInfo());


            dto.setVictimLocation(locationServiceClient.getLocationById(victim.getLocationId()));

        }
        // Fetch responders for this incident
        List<ResponderDTO> responders = responderServiceClient.getRespondersByIncident(incident.getIncidentId());
        dto.setResponderIds(responders.stream()
                .map(ResponderDTO::getResponderId)
                .collect(Collectors.toList()));

        return dto;
    }

    private LocationDTO withReportedAddress(LocationDTO location, Incident incident) {
        if (incident.getReportedAddress() == null) return location;
        return new LocationDTO(location.getLocationId(), location.getLatitude(), location.getLongitude(),
                incident.getReportedAddress());
    }
}
