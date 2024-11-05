package com.Emergency_Response_Management.Service;

import com.Emergency_Response_Management.DTO.IncidentDTO;
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

@Service
public class IncidentService {

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private VictimService victimService;

    @Autowired
    private ResponderRepository responderRepository;

    @Autowired
    private VictimRepository victimRepository;

    @Autowired
    private DispatcherRepository dispatcherRepository;

    @Autowired
    private DispatcherService dispatcherService;


    @Autowired
    private LogService logService;

    @Autowired
    private LocationRepository locationRepository;

    public IncidentDTO reportIncident(IncidentDTO dto) {
        // Create a new victim regardless of whether victimId is provided
        Location victimLocation = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Victim location not found"));

        Victim victim = new Victim();
        victimService.createVictim(victim);
        // Save the newly created victim
        victim = victimRepository.save(victim);

        // Determine incident location
        Location incidentLocation;
        if (dto.getLocationId() != null) {
            // If the user provides a locationId, use that as the incident location
            incidentLocation = locationRepository.findById(dto.getLocationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Incident location not found"));
        } else {
            // If the user does not provide a locationId, use the victim's location
            incidentLocation = victim.getLocation();
        }

        // Create the incident
        Incident incident = new Incident();
        incident.setType(dto.getType());
        incident.setSeverity(dto.getSeverity());
        incident.setTimestamp(LocalDateTime.now());
        incident.setStatus(IncidentStatus.NEW);
        incident.setVictim(victim);
        incident.setLocation(incidentLocation);

        // Save the incident
        incident = incidentRepository.save(incident);

        // Create initial log for the incident
        logService.createLog(incident, "Emergency incident reported by " + victim.getName() +
                " at location: " + incidentLocation.getAddress(), victim.getVictimId());

        assignAvailableDispatcher(incident);

        // Return the incident DTO
        return convertToDTO(incident);
    }


    // Automatically assign an available dispatcher
    private void assignAvailableDispatcher(Incident incident) {
        // For simplicity, getting the first available dispatcher
        // In a real system, we might want to implement more sophisticated load balancing
        List<Dispatcher> dispatchers = dispatcherRepository.findAll();
        if (!dispatchers.isEmpty()) {
            Dispatcher dispatcher = dispatchers.getFirst();
            incident.setManagedBy(dispatcher);
            incident.setStatus(IncidentStatus.NEW);
            incidentRepository.save(incident);

           logService.createLog(incident, "Dispatcher automatically assigned to incident", dispatcher.getDispatcherId());

            // Automatically try to assign appropriate responder
            dispatcherService.assignAppropriateResponder(incident);
        } else {
            logService.createLog(incident, "WARNING: No dispatchers available", null);
        }
    }



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

//    public IncidentDTO assignResponder(Integer incidentId, Integer responderId) {
//        Incident incident = getIncidentByIdEntity(incidentId);
//        Responder responder = responderRepository.findById(responderId)
//                .orElseThrow(() -> new RuntimeException("Responder not found"));
//
//        incident.setAssignedResponder(responder);
//        Incident updatedIncident = incidentRepository.save(incident);
//        return convertToDTO(updatedIncident);
//    }

    // Update incident status during response
    public IncidentDTO updateIncidentStatus(Integer incidentId, IncidentStatus status, String statusDetails, Integer updatedBy) {
        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new ResourceNotFoundException("Incident not found"));

        incident.setStatus(status);
        incident = incidentRepository.save(incident);

        logService.createLog(incident, "Status updated to " + status + ": " + statusDetails, updatedBy);

        if (status == IncidentStatus.RESOLVED) {
            Responder responder = incident.getAssignedResponder();
            responder.setStatus(ResponderStatus.AVAILABLE);
            responderRepository.save(responder);
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
        dto.setLocationId(incident.getLocation()!=null ? incident.getLocation().getLocationId():null);
        dto.setSeverity(incident.getSeverity());
        dto.setType(incident.getType());
        dto.setTimestamp(incident.getTimestamp());
        dto.setStatus(incident.getStatus());
        dto.setVictimId(incident.getVictim() != null ? incident.getVictim().getVictimId() : null);
        dto.setResponderId(incident.getAssignedResponder() != null ? incident.getAssignedResponder().getResponderId() : null);
        dto.setDispatcherId(incident.getManagedBy() != null ? incident.getManagedBy().getDispatcherId() : null);
//        dto.setLogIds(incident.getLogs().stream()
//                .map(Log::getLogId)
//                .collect(Collectors.toList()));
        return dto;
    }

    private Incident convertToEntity(IncidentDTO dto) {
        Incident incident = new Incident();
        incident.setIncidentId(dto.getIncidentId());
        if(dto.getLocationId()!=null){
            Location location = locationRepository.findById(dto.getLocationId())
                    .orElseThrow(()->new ResourceNotFoundException("Location not found"));
            incident.setLocation(location);
        }
        incident.setSeverity(dto.getSeverity());
        incident.setType(dto.getType());
        incident.setTimestamp(dto.getTimestamp());
        incident.setStatus(dto.getStatus());
        if (dto.getResponderId() != null) {
            Responder responder = responderRepository.findById(dto.getResponderId())
                    .orElseThrow(() -> new RuntimeException("Responder not found"));
            incident.setAssignedResponder(responder);
        }
        if (dto.getVictimId()!=null){
            Victim victim = victimRepository.findById(dto.getVictimId())
                    .orElseThrow(()-> new GeneralException("Victim not found"));
           incident.setVictim(victim);
        }

        if(dto.getDispatcherId()!= null){
            Dispatcher dispatcher = dispatcherRepository.findById(dto.getDispatcherId())
                    .orElseThrow(() -> new GeneralException("Dispatcher not found"));
            incident.setManagedBy(dispatcher);
        }
        // Map other fields if necessary, like victim, dispatcher, and logs.
        return incident;
    }

}
