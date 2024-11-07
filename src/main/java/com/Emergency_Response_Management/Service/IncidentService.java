package com.Emergency_Response_Management.Service;

import com.Emergency_Response_Management.DTO.IncidentDTO;
import com.Emergency_Response_Management.DTO.VictimDTO;
import com.Emergency_Response_Management.Enums.IncidentStatus;
import com.Emergency_Response_Management.Enums.IncidentType;
import com.Emergency_Response_Management.Enums.ResponderStatus;
import com.Emergency_Response_Management.Enums.ResponderType;
import com.Emergency_Response_Management.Exception.GeneralException;
import com.Emergency_Response_Management.Exception.ResourceNotFoundException;
import com.Emergency_Response_Management.Model.*;
import com.Emergency_Response_Management.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.BooleanUtils.forEach;

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

    public IncidentDTO reportIncident(IncidentDTO dto) {

        // Create a new victim regardless of whether victimId is provided
        // Step 1: Validate and get victim's location
        if (dto.getVictimLocationId() == null) {
            throw new IllegalArgumentException("Victim location is required");
        }
        Location victimLocation = locationRepository.findById(dto.getVictimLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Victim location not found"));

        Victim victim = new Victim();
        victim.setName(dto.getVictimName());
        victim.setContactInfo(dto.getVictimContact());
        victim.setLocation(victimLocation);
        victim = victimRepository.save(victim);


        Location incidentLocation;
        if (dto.getIncidentLocationId() != null) {
            // If incident location is provided, use that
            incidentLocation = locationRepository.findById(dto.getIncidentLocationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Incident location not found"));
        } else {
            // If no incident location provided, use victim's location
            incidentLocation = victimLocation;
        }

        // Step 4: Create and save the incident
        Incident incident = new Incident();
        incident.setType(dto.getType());
        incident.setSeverity(dto.getSeverity());
        incident.setTimestamp(LocalDateTime.now());
        incident.setStatus(IncidentStatus.NEW);
        incident.setVictim(victim);
        incident.setLocation(incidentLocation);

        incident = incidentRepository.save(incident);

        // Step 5: Create log entry
        String locationInfo = incidentLocation.equals(victimLocation)
                ? "at victim's location: " + victimLocation.getAddress()
                : "at location: " + incidentLocation.getAddress() + " (victim located at: " + victimLocation.getAddress() + ")";

        logService.createLog(incident,
                "Emergency incident reported by " + victim.getName() + " " + locationInfo,
                victim.getVictimId());

//        assignAvailableDispatcher(incident);
        responderService.assignAppropriateResponder(incident);
        // Return the incident DTO
        return convertToDTO(incident);
    }


    // Automatically assign an available dispatcher
//    private void assignAvailableDispatcher(Incident incident) {
//        // For simplicity, getting the first available dispatcher
//        // In a real system, we might want to implement more sophisticated load balancing
//        List<Dispatcher> dispatchers = dispatcherRepository.findAll();
//        if (!dispatchers.isEmpty()) {
//            Dispatcher dispatcher = dispatchers.getFirst();
//            incident.setManagedBy(dispatcher);
//            incident.setStatus(IncidentStatus.NEW);
//            incidentRepository.save(incident);
//
//           logService.createLog(incident, "Dispatcher automatically assigned to incident", dispatcher.getDispatcherId());
//
//            // Automatically try to assign appropriate responder
//
//        } else {
//            logService.createLog(incident, "WARNING: No dispatchers available", null);
//        }
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
    public IncidentDTO updateIncidentStatus(Integer incidentId, IncidentStatus status, String statusDetails, Integer updatedBy) {
        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new ResourceNotFoundException("Incident not found"));

        incident.setStatus(status);
        incident = incidentRepository.save(incident);

        logService.createLog(incident, "Status updated to " + status + ": " + statusDetails, updatedBy);

        if (status == IncidentStatus.RESOLVED) {
            incident.getAssignedResponders()
                    .forEach(i -> {
                        i.setStatus(ResponderStatus.AVAILABLE);
                       responderRepository.save(i);
                   }
           );


//            responder.setStatus(ResponderStatus.AVAILABLE);

//            notificationService.notifyVictim(incident.getVictim(), "Your incident has been resolved");
        }

        return convertToDTO(incident);
    }

    public void deleteIncident(Integer id) {
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
        dto.setSeverity(incident.getSeverity());
        dto.setType(incident.getType());
        dto.setTimestamp(incident.getTimestamp());
        dto.setStatus(incident.getStatus());// Set incident location
        if (incident.getLocation() != null) {
            dto.setIncidentLocationId(incident.getLocation().getLocationId());
        }

        if (incident.getAssignedResponders() != null && !incident.getAssignedResponders().isEmpty()) {
            dto.setResponderIds(incident.getAssignedResponders().stream()
                    .map(Responder::getResponderId)
                    .toList());
            System.out.println("Assigned Responder IDs: " + dto.getResponderIds());
        } else {
            System.out.println("No assigned responders found.");
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
