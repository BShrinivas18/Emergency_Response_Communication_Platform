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
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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


    public IncidentDTO reportIncident(IncidentDTO dto, Double latitude, Double longitude, boolean isSOS) {
        // Determine or create location
        LocationDTO location = determineLocation(dto, latitude, longitude);

        // Create incident
        Incident incident = createIncident(dto, location.getLocationId(), isSOS);

        // Handle victim information if available (not for SOS)
        Integer victimId = null;
        if (!isSOS && dto != null) {
//
//            System.out.println("Its not SOS");
            victimId = handleVictimInformation(dto);
            incident.setVictimId(victimId);

        }

        // Save incident
        incident = incidentRepository.save(incident);

        // Log the incident
        logIncident(incident, location);

        // Assign responder
        assignResponder(incident);

//        incident = incidentRepository.save(incident);

        return convertToDTO(incident);
    }

    private Incident createIncident(IncidentDTO dto, int locationId, boolean isSOS) {

        Incident incident = new Incident();
        incident.setType(isSOS ? IncidentType.SOS_REQUEST : dto.getType());
        incident.setTimestamp(LocalDateTime.now());
        incident.setStatus(IncidentStatus.NEW);
        incident.setLocationId(locationId);
        return incident;
    }

    private LocationDTO determineLocation(IncidentDTO dto, Double latitude, Double longitude) {
//        if(dto.getIncidentLocationId() != null) {
//            return locationServiceClient.getLocationById(dto.getIncidentLocationId());
//        }
        if (latitude != null && longitude != null ) {
            // Try to find existing location or create new
            try {
                System.out.println("trying to find location by latitude and longitude");
//                System.out.println(locationServiceClient.findByLatitudeAndLongitude(latitude, longitude)+"");
                return locationServiceClient.findByLatitudeAndLongitude(latitude, longitude)
                        .orElseGet(() -> {
                            System.out.println("location not found, creating new location");
                            LocationDTO newLocation = new LocationDTO();
                            newLocation.setLatitude(latitude);
                            newLocation.setLongitude(longitude);
                            System.out.println("created new location "+ newLocation);
//                            System.out.println("location: "+ locationServiceClient.createLocation(newLocation));
                            return locationServiceClient.createLocation(newLocation);
                        });
            } catch (Exception e) {
                System.out.println("exception caught");
                // Fallback to location from DTO
                return locationServiceClient.getLocationById(dto.getIncidentLocationId());
            }
        } else {
            // Use location from DTO
            if(dto.getIncidentLocationId() == null) {
                System.out.println("incident location id is null");
                dto.setIncidentLocationId(dto.getVictimLocationId());
            }
            System.out.println("incident location id: "+dto.getIncidentLocationId());
            return locationServiceClient.getLocationById(dto.getIncidentLocationId());
        }
    }

    private Integer handleVictimInformation(IncidentDTO dto) {
//        System.out.println("handle victim information");
        if (dto.getVictimName() != null) {
//            System.out.println("victim name: "+dto.getVictimName());
            VictimDTO victim = new VictimDTO();
            victim.setName(dto.getVictimName());
            victim.setContactInfo(dto.getVictimContact());
            victim.setLocationId(dto.getVictimLocationId());

            Integer victimId = (victimServiceClient.createVictim(victim)).getVictimId();
//            System.out.println("victim name: "+victim.getName());
//            System.out.println("victim id: "+victimId);
//            System.out.println("victim contact: "+victim.getLocationId());
            return victimId;
        }
        return null;
    }

    private void logIncident(Incident incident, LocationDTO location) {
        LogDTO log = new LogDTO();
        log.setStatusUpdate(String.format("Incident reported at location: %s (Lat: %f, Long: %f)",
                location.getAddress(), location.getLatitude(), location.getLongitude()));
        log.setTimestamp(LocalDateTime.now());
//        log.setUpdatedBy(victimId);
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
                    dto.setIncidentLocationId(incident.getLocationId());
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
        dto.setIncidentLocationId(incident.getLocationId());

        // Fetch additional details from other services
        if (incident.getVictimId() != null) {
            VictimDTO victim = victimServiceClient.getVictimById(incident.getVictimId());
            dto.setVictimId(victim.getVictimId());
            dto.setVictimName(victim.getName());
            dto.setVictimContact(victim.getContactInfo());
//            System.out.println("victim : "+ victim);
//            System.out.println("victim location id: "+victim.getLocationId());
            dto.setVictimLocationId(victim.getLocationId());

        }
        // Fetch responders for this incident
        List<ResponderDTO> responders = responderServiceClient.getRespondersByIncident(incident.getIncidentId());
        dto.setResponderIds(responders.stream()
                .map(ResponderDTO::getResponderId)
                .collect(Collectors.toList()));

        return dto;
    }
}
