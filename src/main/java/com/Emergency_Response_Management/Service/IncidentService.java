package com.Emergency_Response_Management.Service;

import com.Emergency_Response_Management.DTO.IncidentDTO;
import com.Emergency_Response_Management.Enums.IncidentStatus;
import com.Emergency_Response_Management.Enums.IncidentType;
import com.Emergency_Response_Management.Enums.ResponderStatus;
import com.Emergency_Response_Management.Exception.GeneralException;
import com.Emergency_Response_Management.Exception.ResourceNotFoundException;
import com.Emergency_Response_Management.Model.*;
import com.Emergency_Response_Management.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class IncidentService {

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private ResponderRepository responderRepository;

    @Autowired
    private VictimRepository victimRepository;

    @Autowired
    private ResponderService responderService;

    @Autowired
    private LogService logService;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private GeocodingService geocodingService; // This service is responsible for retrieving the address from coordinates


    public IncidentDTO reportIncident(IncidentDTO dto, Double latitude, Double longitude, boolean isSOS) {
        // Handle location
        Location incidentLocation = determineLocation(dto, latitude, longitude);

        // Create incident
        Incident incident = createIncident(dto, incidentLocation, isSOS);

        // Handle victim information if available (not for SOS)
        if (!isSOS && dto != null) {
            handleVictimInformation(incident, dto);
        }

        // Save incident
        incident = incidentRepository.save(incident);

        // Log the incident
        logIncident(incident);

        responderService.assignAppropriateResponder(incident);

        return convertToDTO(incident);
    }

    private Incident createIncident(IncidentDTO dto, Location location, boolean isSOS) {
        Incident incident = new Incident();
        incident.setType(isSOS ? IncidentType.SOS_REQUEST: dto.getType());
//        System.out.println("incident-type: "+incident.getType());
        incident.setTimestamp(LocalDateTime.now());
        incident.setStatus(IncidentStatus.NEW);
        incident.setLocation(location);
        return incident;
    }

    private void handleVictimInformation(Incident incident, IncidentDTO dto) {
        if (dto.getVictimName() != null) {
            Victim victim = new Victim();
            victim.setName(dto.getVictimName());
            victim.setContactInfo(dto.getVictimContact());
            victim.setLocation(incident.getLocation());
            victim = victimRepository.save(victim);
            incident.setVictim(victim);
        }
    }

    private void logIncident(Incident incident) {
        String locationInfo = String.format("at location: %s (Lat: %f, Long: %f)",
                incident.getLocation().getAddress(),
                incident.getLocation().getLatitude(),
                incident.getLocation().getLongitude());

        logService.createLog(incident,
                "Incident reported " + locationInfo,
                incident.getVictim() != null ? incident.getVictim().getVictimId() : null);
    }

    private Location determineLocation(IncidentDTO dto, Double latitude, Double longitude) {
        if (latitude != null && longitude != null) {
            return locationRepository.findByLatitudeAndLongitude(latitude, longitude)
                    .orElseGet(() -> {
                        Location newLocation = new Location();
                        newLocation.setLatitude(latitude);
                        newLocation.setLongitude(longitude);
                        String address = geocodingService.getAddressFromCoordinates(latitude, longitude);

//                        System.out.println("address: "+address);
                        newLocation.setAddress(address);
                        return locationRepository.save(newLocation);
                    });
        } else {
            return locationRepository.findById(dto.getVictimLocationId())
                    .orElseThrow(() -> new RuntimeException("Location not found"));
        }
    }




//    public IncidentDTO reportIncident(IncidentDTO dto) {
//
//        // Create a new victim regardless of whether victimId is provided
//        // Step 1: Validate and get victim's location
//        if (dto.getVictimLocationId() == null) {
//            throw new IllegalArgumentException("Victim location is required");
//        }
//        Location victimLocation = locationRepository.findById(dto.getVictimLocationId())
//                .orElseThrow(() -> new ResourceNotFoundException("Victim location not found"));
//
//        Victim victim = new Victim();
//        victim.setName(dto.getVictimName());
//        victim.setContactInfo(dto.getVictimContact());
//        victim.setLocation(victimLocation);
//        victim = victimRepository.save(victim);
//
//
//        Location incidentLocation;
//        if (dto.getIncidentLocationId() != null) {
//            // If incident location is provided, use that
//            incidentLocation = locationRepository.findById(dto.getIncidentLocationId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Incident location not found"));
//        } else {
//            // If no incident location provided, use victim's location
//            incidentLocation = victimLocation;
//        }
//
//        // Step 4: Create and save the incident
//        Incident incident = new Incident();
//        incident.setType(dto.getType());
////        incident.setSeverity(dto.getSeverity());
//        incident.setTimestamp(LocalDateTime.now());
//        incident.setStatus(IncidentStatus.NEW);
//        incident.setVictim(victim);
//        incident.setLocation(incidentLocation);
//
//        incident = incidentRepository.save(incident);
//
//        // Step 5: Create log entry
//        String locationInfo = incidentLocation.equals(victimLocation)
//                ? "at victim's location: " + victimLocation.getAddress()
//                : "at location: " + incidentLocation.getAddress() + " (victim located at: " + victimLocation.getAddress() + ")";
//
//        logService.createLog(incident,
//                "Emergency incident reported by " + victim.getName() + " " + locationInfo,
//                victim.getVictimId());
//
//        responderService.assignAppropriateResponder(incident);
//        // Return the incident DTO
//        return convertToDTO(incident);
//    }


    public List<IncidentDTO> getAllIncidents() {
        return incidentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<IncidentDTO> getIncidentById(Integer id) {
        return incidentRepository.findById(id).map(this::convertToDTO);
    }



    public List<IncidentDTO> getIncidentsByStatus(ResponseStatus status) {
        return incidentRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Update incident status during response
//    public void updateIncidentStatus(Integer incidentId, ResponderStatus status) {
//        Incident incident = incidentRepository.findById(incidentId)
//                .orElseThrow(() -> new ResourceNotFoundException("Incident not found"));
//        switch (status){
//            case ON_ROUTE ,ON_SCENE-> incident.setStatus(IncidentStatus.IN_PROGRESS);
//            default -> incident.setStatus(IncidentStatus.RESOLVED);
//        }
////        incident.setStatus(status);
//        incident = incidentRepository.save(incident);
//
//        logService.createLog(incident, "Status updated to " + status + ": ", null);
//
//        if (incident.getStatus() == IncidentStatus.RESOLVED) {
//            incident.getAssignedResponders()
//                    .forEach(i -> {
//                        i.setStatus(ResponderStatus.AVAILABLE);
//                       responderRepository.save(i);
//                   }
//           );
//  }
//        convertToDTO(incident);
//    }

    public void deleteIncident(Integer id) {
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident not found"));

        // Disassociate responders before deleting the incident
        for (Responder responder : incident.getAssignedResponders()) {
            responder.setIncident(null);
            responder.setStatus(ResponderStatus.AVAILABLE);
            responder.setLastUpdate(LocalDateTime.now());
            // Clear the reference to the incident
        }

        // Now you can delete the incident safely
        incidentRepository.deleteById(id);
    }


    // Helper method to retrieve Incident as an entity for internal use
    private Incident getIncidentByIdEntity(Integer id) {
        return incidentRepository.findById(id)
                .orElseThrow(() -> new GeneralException("Incident Not Found"));
    }

    private IncidentDTO convertToDTO(Incident incident) {
        IncidentDTO dto = new IncidentDTO();
        dto.setIncidentId(incident.getIncidentId());
//        dto.setSeverity(incident.getSeverity());
        dto.setType(incident.getType());
        dto.setTimestamp(incident.getTimestamp());
        dto.setStatus(incident.getStatus());

        // Set incident location
        if (incident.getLocation() != null) {
            dto.setIncidentLocationId(incident.getLocation().getLocationId());
        }

        if (incident.getAssignedResponders() != null) {
            List<Integer> responderIds = incident.getAssignedResponders().stream()
                    .map(Responder::getResponderId)
                    .collect(Collectors.toList());
            dto.setResponderIds(responderIds);
        } else {
            dto.setResponderIds(new ArrayList<>());
        }

        // Set victim information
        if (incident.getVictim() != null) {
            dto.setVictimId(incident.getVictim().getVictimId());
            dto.setVictimName(incident.getVictim().getName());
            dto.setVictimContact(incident.getVictim().getContactInfo());
            if (incident.getVictim().getLocation() != null) {
                dto.setVictimLocationId(incident.getVictim().getLocation().getLocationId());
            }
        }
        return dto;
    }



}
