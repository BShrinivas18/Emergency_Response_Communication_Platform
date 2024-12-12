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

        LocationDTO incidentLocation = determineLocation(dto.getIncidentLocation());// for incident location
        int incidentLocationId = incidentLocation.getLocationId();
        LocationDTO victimLocation = determineLocation(dto.getVictimLocation());
        int victimLocationId = victimLocation.getLocationId();// for victim location
        // Create incident
        System.out.println("incident location id : "+ incidentLocationId);
        Incident incident = createIncident(dto, incidentLocationId);

        log.info("Incident created ");
        log.info(incident.toString());
        // Handle victim information if available (not for SOS)
        Integer victimId = null;
        if (!"Anonymous".equals(dto.getVictimName()) && dto != null) {
            victimId = handleVictimInformation(dto,victimLocationId);
            incident.setVictimId(victimId);

        }
        System.out.println("Incident: " + incident);

        // Save incident
        incident = incidentRepository.save(incident);

        // Log the incident
        logIncident(incident, incidentLocation);

        // Assign responder
        assignResponder(incident);
        return convertToDTO(incident);
    }

    private Incident createIncident(IncidentDTO dto, int incidentLocation) {
        Incident incident = new Incident();
        incident.setType(dto.getVictimName().equals("Anonymous") ? IncidentType.SOS_REQUEST : dto.getType());
        incident.setTimestamp(LocalDateTime.now());
        incident.setStatus(IncidentStatus.NEW);  // This is where status is set
        System.out.println("incident incidentId : " + incident.getIncidentId());
        System.out.println("incident location : "+ incidentLocation);

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
        try {
            // First, check if location already exists
            Optional<LocationDTO> existingLocation = locationServiceClient.findByLatitudeAndLongitude(
                    location.getLatitude(),
                    location.getLongitude()
            );
            System.out.println("existing location : "+ existingLocation);
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
            System.out.println("saved location : "+ savedLocation);
            newLocation.setLocationId(savedLocation.getLocationId());
            return savedLocation;

        } catch (Exception e) {
            throw new RuntimeException("Location determination failed: " + e.getMessage(), e);
        }
    }

    private Integer handleVictimInformation(IncidentDTO dto,int victimLocation) {
//        System.out.println("handle victim information");
        if (dto.getVictimName() != null) {
//            System.out.println("victim name: "+dto.getVictimName());
            VictimDTO victim = new VictimDTO();
            victim.setName(dto.getVictimName());
            victim.setContactInfo(dto.getVictimContact());
//            victim.setLocationId(dto.getVictimLocationId());
            victim.setLocationId(victimLocation);
            Integer victimId = (victimServiceClient.createVictim(victim)).getVictimId(); // victim id

            return victimId;
        }
        return null;
    }

    private void logIncident(Incident incident, LocationDTO location) {
        LogDTO log = new LogDTO();
        log.setStatusUpdate(String.format("Incident reported at location: %s (Lat: %f, Long: %f)",
                location.getAddress(), location.getLatitude(), location.getLongitude()));
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
        System.out.println("get incident by id in service works");
        return incidentRepository.findById(id).map(this::convertToDTO);
    }
    public List<IncidentDTO> getIncidentsByStatus(IncidentStatus status) {
        return incidentRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void updateIncidentStatus(int incidentId, IncidentStatus status) {
        System.out.println("update incident status in service works");

        Incident incident = incidentRepository.findById(incidentId).orElseThrow(()-> new RuntimeException("Incident not found"));
        incident.setStatus(status);
        System.out.println("incident status: "+incident.getStatus());
        System.out.println("incident: "+incident);
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
                    dto.setIncidentLocation(locationServiceClient.getLocationById(incident.getLocationId()));
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
        dto.setIncidentLocation(locationServiceClient.getLocationById(incident.getLocationId()));

        // Fetch additional details from other services
        if (incident.getVictimId() != null) {
            VictimDTO victim = victimServiceClient.getVictimById(incident.getVictimId());
            dto.setVictimId(victim.getVictimId());
            dto.setVictimName(victim.getName());
            dto.setVictimContact(victim.getContactInfo());
//            System.out.println("victim : "+ victim);
//            System.out.println("victim location id: "+victim.getLocationId());
            dto.setVictimLocation(locationServiceClient.getLocationById(victim.getLocationId()));

        }
        // Fetch responders for this incident
        List<ResponderDTO> responders = responderServiceClient.getRespondersByIncident(incident.getIncidentId());
        dto.setResponderIds(responders.stream()
                .map(ResponderDTO::getResponderId)
                .collect(Collectors.toList()));

        return dto;
    }
}
