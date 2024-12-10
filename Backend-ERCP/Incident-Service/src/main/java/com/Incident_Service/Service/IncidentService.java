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

import javax.xml.stream.Location;
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


    public IncidentDTO reportIncident(IncidentDTO dto) {
        // Determine or create location
//        LocationDTO location = determineLocation(dto, latitude, longitude);

            LocationDTO incidentLocation = dto.getIncidentLocation();
            LocationDTO victimLocation = dto.getVictimLocation();
            int incidentLocationId = determineLocation(incidentLocation, dto).getLocationId();
            int victimLocationId = determineLocation(victimLocation, dto).getLocationId();
            // Create incident
        Incident incident = createIncident(dto, incidentLocation);

        // Handle victim information if available (not for SOS)
        Integer victimId = null;
        if (dto.getVictimName() != "Anonymous" && dto != null) {
//
//            System.out.println("Its not SOS");
            victimId = handleVictimInformation(dto);
            incident.setVictimId(victimId);

        }

        // Save incident
        incident = incidentRepository.save(incident);

        // Log the incident
        logIncident(incident, incidentLocation);

        // Assign responder
        assignResponder(incident);

//        incident = incidentRepository.save(incident);

        return convertToDTO(incident);
    }

    private Incident createIncident(IncidentDTO dto, LocationDTO incidentLocation) {

        Incident incident = new Incident();
        incident.setType(dto.getVictimName().equals("Anonymous") ? IncidentType.SOS_REQUEST : dto.getType());
        incident.setTimestamp(LocalDateTime.now());
        incident.setStatus(IncidentStatus.NEW);
        incident.setLocationId(incidentLocation.getLocationId());
        return incident;
    }

    private LocationDTO determineLocation(LocationDTO location, IncidentDTO dto) {
//        if(dto.getIncidentLocationId() != null) {
//            return locationServiceClient.getLocationById(dto.getIncidentLocationId());
//        }
//        if (location.getLatitude().isNaN() && location.getLongitude().isNaN() ) {
        // Try to find existing location or create new
        try {
            System.out.println("trying to find location by latitude and longitude");
//                System.out.println(locationServiceClient.findByLatitudeAndLongitude(latitude, longitude)+"");
            LocationDTO foundLocation = locationServiceClient.findByLatitudeAndLongitude(location.getLatitude(), location.getLongitude())
                    .orElse(null);
            if (foundLocation != null) {
                return foundLocation;
            }

            System.out.println("location not found, creating new location");
            LocationDTO newLocation = new LocationDTO();
            newLocation.setLatitude(location.getLatitude());
            newLocation.setLongitude(location.getLongitude());
            newLocation.setAddress(location.getAddress());

            LocationDTO savedLocation = locationServiceClient.createLocation(newLocation);
            if (savedLocation == null || savedLocation.getLocationId() < 0) {
                throw new RuntimeException("Failed to save location. Please check the location service.");
            }

            System.out.println("Saved Location ID: " + savedLocation.getLocationId());
            System.out.println();
            newLocation.setLocationId(savedLocation.getLocationId());
            System.out.println("Saved Location Id: " + savedLocation.getLocationId());
            return savedLocation;


            } catch (Exception e) {
               e.printStackTrace();
               throw new RuntimeException(e);
                // Fallback to location from DTO
//                return locationServiceClient.getLocationById(dto.getVictimLocation().getLocationId());
            }
//        }
//        else {
//            // Use location from DTO
//            if(location == null) {
//                System.out.println("incident location id is null");
//                dto.setIncidentLocation(dto.getVictimLocation());
//
//            }
//            System.out.println("incident location id: "+dto.getIncidentLocationId());
//            return locationServiceClient.getLocationById(dto.getIncidentLocationId());
//        }
    }

    private Integer handleVictimInformation(IncidentDTO dto) {
//        System.out.println("handle victim information");
        if (dto.getVictimName() != null) {
//            System.out.println("victim name: "+dto.getVictimName());
            VictimDTO victim = new VictimDTO();
            victim.setName(dto.getVictimName());
            victim.setContactInfo(dto.getVictimContact());
//            victim.setLocationId(dto.getVictimLocationId());
            victim.setLocationId(dto.getVictimLocation().getLocationId());

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
