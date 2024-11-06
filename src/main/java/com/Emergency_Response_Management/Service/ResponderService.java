package com.Emergency_Response_Management.Service;

import com.Emergency_Response_Management.DTO.ResponderDTO;
import com.Emergency_Response_Management.Enums.IncidentStatus;
import com.Emergency_Response_Management.Enums.IncidentType;
import com.Emergency_Response_Management.Enums.ResponderStatus;
import com.Emergency_Response_Management.Enums.ResponderType;
import com.Emergency_Response_Management.Exception.GeneralException;
import com.Emergency_Response_Management.Model.Incident;
import com.Emergency_Response_Management.Model.Location;
import com.Emergency_Response_Management.Model.Responder;
import com.Emergency_Response_Management.Repository.IncidentRepository;
import com.Emergency_Response_Management.Repository.LocationRepository;
import com.Emergency_Response_Management.Repository.ResponderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResponderService {

    @Autowired
    private ResponderRepository responderRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private LogService logService;

    public ResponderDTO createResponder(ResponderDTO responderDTO) {
        Responder responder = convertToEntity(responderDTO);
        Responder savedResponder = responderRepository.save(responder);
        return convertToDTO(savedResponder);
    }

    public List<ResponderDTO> getAllResponders() {
        return responderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ResponderDTO> getResponderById(Integer id) {
        return responderRepository.findById(id).map(this::convertToDTO);
    }

    public List<ResponderDTO> getRespondersByStatus(ResponderStatus status) {
        return responderRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    public List<ResponderDTO> getRespondersByType(ResponderType type) {
        return responderRepository.findByType(type).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ResponderDTO> getRespondersByLocation(Integer locationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));
        return responderRepository.findByLocation(location).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ResponderDTO updateResponder(Integer id, ResponderDTO responderDTO) {
        Responder existingResponder = responderRepository.findById(id)
                .orElseThrow(() -> new GeneralException("Responder Not Found"));

        existingResponder.setName(responderDTO.getName());
        existingResponder.setCurrentLocation(responderDTO.getCurrentLocation());
        existingResponder.setStatus(responderDTO.getStatus());
        existingResponder.setType(responderDTO.getType());
        existingResponder.setLastUpdate(LocalDateTime.now());

        if (responderDTO.getLocationId() != null) {
            Location location = locationRepository.findById(responderDTO.getLocationId())
                    .orElseThrow(() -> new GeneralException("Location Not Found"));
            existingResponder.setLocation(location);
        }

        Responder updatedResponder = responderRepository.save(existingResponder);
        return convertToDTO(updatedResponder);
    }

    public ResponderDTO updateStatus(Integer id, ResponderStatus status) {
        Responder responder = responderRepository.findById(id)
                .orElseThrow(() -> new GeneralException("Responder Not Found"));

        responder.setStatus(status);
        responder.setLastUpdate(LocalDateTime.now());

        Responder updatedResponder = responderRepository.save(responder);
        return convertToDTO(updatedResponder);
    }


    public void deleteResponder(Integer id) {
        responderRepository.deleteById(id);
    }

    private ResponderDTO convertToDTO(Responder responder) {
        ResponderDTO dto = new ResponderDTO();
        dto.setResponderId(responder.getResponderId());
        dto.setName(responder.getName());
        dto.setCurrentLocation(responder.getCurrentLocation());
        dto.setStatus(responder.getStatus());
        dto.setType(responder.getType());
        dto.setLastUpdate(responder.getLastUpdate());

        if (responder.getLocation() != null) {
            dto.setLocationId(responder.getLocation().getLocationId());
        }

        // Assuming there's a method to retrieve incident IDs for a responder
//        dto.setIncidentIds(responder.getIncidents().stream()
//                .map(Incident::getIncidentId)
//                .collect(Collectors.toList()));

        return dto;
    }

    private Responder convertToEntity(ResponderDTO dto) {
        Responder responder = new Responder();
        responder.setResponderId(dto.getResponderId());
        responder.setName(dto.getName());
        responder.setCurrentLocation(dto.getCurrentLocation());
        responder.setStatus(dto.getStatus());
        responder.setType(dto.getType());
        responder.setLastUpdate(dto.getLastUpdate());

        if (dto.getLocationId() != null) {
            Location location = locationRepository.findById(dto.getLocationId())
                    .orElseThrow(() -> new GeneralException("Location Not Found"));
            responder.setLocation(location);
        }
        return responder;
    }

    // Automatically assign appropriate responder based on incident type (needs to be done for availability too)
    public void assignAppropriateResponder(Incident incident) {
        ResponderType requiredResponderType = getRequiredResponderType(incident.getType());

        // Find available responders of the required type
        List<Responder> availableResponders = responderRepository.findByStatusAndType(
                ResponderStatus.AVAILABLE,
                requiredResponderType
        );

        if (!availableResponders.isEmpty()) {
            // For now, simply get the first available responder
            // In a real system, we might want to consider location, workload, etc.

            Responder responder = availableResponders.stream().filter(i->(i.getStatus().toString()).equals("AVAILABLE")).findFirst().orElse(null);
            if(responder==null){
                responder = responderRepository.findByStatusAndType(ResponderStatus.AVAILABLE,ResponderType.POLICE_OFFICER).getFirst();
//                responder = responderService.findByStatusAndType(ResponderStatus.AVAILABLE,ResponderType.POLICE_OFFICER).getFirst();
            }
            // Assign responder

            incident.setAssignedResponder(responder);
//            incident.setStatus(IncidentStatus.IN_PROGRESS);

            // Update responder status
            responder.setStatus(ResponderStatus.ASSIGNED);
            responderRepository.save(responder);

            // Save incident
            incidentRepository.save(incident);

            logService.createLog(incident, "Responder automatically assigned: " + responder.getName(),responder.getResponderId()
            );

            // Notify responder
//            notificationService.notifyResponder(responder, incident);
        } else {
            logService.createLog(incident, "WARNING: No available responders of type " + requiredResponderType,null);
        }
    }

    // Helper method to determine required responder type based on incident type
    private ResponderType getRequiredResponderType(IncidentType incidentType) {

        return switch (incidentType) {
            case MEDICAL_EMERGENCY -> ResponderType.PARAMEDIC;
            case FIRE -> ResponderType.FIREFIGHTER;
            case NATURAL_DISASTER -> ResponderType.RESCUE_TEAM;
            case HAZMAT -> ResponderType.HAZMAT_SPECIALIST;
            default -> ResponderType.POLICE_OFFICER;
        };

//        return switch (incidentType) {
//            case "MEDICAL_EMERGENCY", "TRAFFIC_ACCIDENT" -> ResponderType.PARAMEDIC;
//            case "FIRE" -> ResponderType.FIREFIGHTER;
//            case "NATURAL_DISASTER" -> ResponderType.RESCUE_TEAM;
//            case "HAZMAT" -> ResponderType.HAZMAT_SPECIALIST;
//            default -> ResponderType.POLICE_OFFICER;
//        };


    }
//    public List<Responder> findByStatusAndType(ResponderStatus status, ResponderType type){
//        return responderRepository.findAll().stream().filter(i-> (i.getStatus().equals(status) && i.getType().equals(type))).toList();
//    }
}
