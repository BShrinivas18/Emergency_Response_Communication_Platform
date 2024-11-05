package com.Emergency_Response_Management.Service;

import com.Emergency_Response_Management.DTO.DispatcherDTO;
import com.Emergency_Response_Management.Enums.IncidentStatus;
import com.Emergency_Response_Management.Enums.IncidentType;
import com.Emergency_Response_Management.Enums.ResponderStatus;
import com.Emergency_Response_Management.Enums.ResponderType;
import com.Emergency_Response_Management.Exception.GeneralException;
import com.Emergency_Response_Management.Model.Dispatcher;
import com.Emergency_Response_Management.Model.Incident;
import com.Emergency_Response_Management.Model.Responder;
import com.Emergency_Response_Management.Repository.DispatcherRepository;
import com.Emergency_Response_Management.Repository.IncidentRepository;
import com.Emergency_Response_Management.Repository.ResponderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DispatcherService {

    @Autowired
    private DispatcherRepository dispatcherRepository;

    @Autowired
    private ResponderRepository responderRepository;

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private LogService logService;

    public DispatcherDTO createDispatcher(DispatcherDTO dispatcherDTO) {
        Dispatcher dispatcher = convertToEntity(dispatcherDTO);
        Dispatcher savedDispatcher = dispatcherRepository.save(dispatcher);
        return convertToDTO(savedDispatcher);
    }


    public List<DispatcherDTO> getAllDispatchers() {
        return dispatcherRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DispatcherDTO getDispatcherById(Integer id) {
        Dispatcher dispatcher = dispatcherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dispatcher not found"));
        return convertToDTO(dispatcher);
    }

    public DispatcherDTO updateDispatcher(Integer id, DispatcherDTO dispatcherDTO) {
        Dispatcher existingDispatcher = dispatcherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dispatcher not found"));
        existingDispatcher.setName(dispatcherDTO.getName());
        existingDispatcher.setContactInfo(dispatcherDTO.getContactInfo());
//        existingDispatcher.setAssignedRegion(dispatcherDTO.getAssignedRegion());
        Dispatcher updatedDispatcher = dispatcherRepository.save(existingDispatcher);
        return convertToDTO(updatedDispatcher);
    }

//    public List<Dispatcher> getDispatchersByRegion(String region) {
//        return dispatcherRepository.findByAssignedRegion(region);
//    }

    public void deleteDispatcher(Integer id) {
        dispatcherRepository.deleteById(id);
    }

    private DispatcherDTO convertToDTO(Dispatcher dispatcher) {
        DispatcherDTO dto = new DispatcherDTO();
        dto.setDispatcherId(dispatcher.getDispatcherId());
        dto.setName(dispatcher.getName());
        dto.setContactInfo(dispatcher.getContactInfo());
//        dto.setAssignedRegion(dispatcher.getAssignedRegion());
//        dto.setManagedIncidentIds(dispatcher.getManagedIncidents().stream()
//                .map(Incident::getIncidentId)
//                .collect(Collectors.toList()));
        return dto;
    }

    private Dispatcher convertToEntity(DispatcherDTO dto) {
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setDispatcherId(dto.getDispatcherId());
        dispatcher.setName(dto.getName());
        dispatcher.setContactInfo(dto.getContactInfo());
//        dispatcher.setAssignedRegion(dto.getAssignedRegion());
        // Set managed incidents based on their IDs if necessary.
        return dispatcher;
    }

    // Automatically assign appropriate responder based on incident type (needs to be done for availability too)
   public void assignAppropriateResponder(Incident incident) {
        ResponderType requiredResponderType = getRequiredResponderType(incident.getType().toString());

        // Find available responders of the required type
        List<Responder> availableResponders = responderRepository.findByStatusAndType(
                ResponderStatus.AVAILABLE,
                requiredResponderType
        );

        if (!availableResponders.isEmpty()) {
            // For now, simply get the first available responder
            // In a real system, we might want to consider location, workload, etc.
            Responder responder = availableResponders.getFirst();

            // Assign responder
            incident.setAssignedResponder(responder);
            incident.setStatus(IncidentStatus.IN_PROGRESS);

            // Update responder status
            responder.setStatus(ResponderStatus.ASSIGNED);
            responderRepository.save(responder);

            // Save incident
            incidentRepository.save(incident);

            logService.createLog(incident, "Responder automatically assigned: " + responder.getName(),
                    incident.getManagedBy().getDispatcherId());

            // Notify responder
//            notificationService.notifyResponder(responder, incident);
        } else {
            logService.createLog(incident, "WARNING: No available responders of type " + requiredResponderType,
                    incident.getManagedBy().getDispatcherId());
        }
    }

    // Helper method to determine required responder type based on incident type
    private ResponderType getRequiredResponderType(String incidentType) {
        return switch (incidentType) {
            case "MEDICAL_EMERGENCY", "TRAFFIC_ACCIDENT" -> ResponderType.PARAMEDIC;
            case "FIRE" -> ResponderType.FIREFIGHTER;
            case "NATURAL_DISASTER" -> ResponderType.RESCUE_TEAM;
            case "HAZMAT" -> ResponderType.HAZMAT_SPECIALIST;
            default -> ResponderType.POLICE_OFFICER;
        };
    }



}
