package com.Responder_Service.Service;

import com.Responder_Service.DTO.IncidentDTO;
import com.Responder_Service.DTO.LocationDTO;
import com.Responder_Service.DTO.LogDTO;
import com.Responder_Service.DTO.ResponderDTO;
import com.Responder_Service.Enums.IncidentStatus;
import com.Responder_Service.Enums.IncidentType;
import com.Responder_Service.Enums.ResponderStatus;
import com.Responder_Service.Enums.ResponderType;
import com.Responder_Service.FeignClient.IncidentServiceClient;
import com.Responder_Service.FeignClient.LocationServiceClient;
import com.Responder_Service.FeignClient.LogServiceClient;
import com.Responder_Service.Model.Responder;
import com.Responder_Service.Repository.ResponderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResponderService {
    @Autowired
    private com.Responder_Service.Repository.ResponderEventRepository events;
    public java.util.List<com.Responder_Service.Model.ResponderEvent> history(Integer id) {
        return events.findByResponderIdOrderByIdAsc(id);
    }
    private void record(Responder responder, Integer incidentId, String kind, ResponderStatus previous) {
        var attributes = org.springframework.web.context.request.RequestContextHolder.getRequestAttributes();
        String actor = "SYSTEM";
        if (attributes instanceof org.springframework.web.context.request.ServletRequestAttributes servlet) {
            String forwarded = servlet.getRequest().getHeader("X-ERCP-Actor");
            if (forwarded != null && !forwarded.isBlank()) actor = forwarded;
        }
        events.save(new com.Responder_Service.Model.ResponderEvent(responder.getResponderId(), incidentId,
            kind, previous == null ? null : previous.name(), responder.getStatus().name(), actor));
    }


    @Autowired
    private ResponderRepository responderRepository;

    @Autowired
    private LocationServiceClient locationServiceClient;

    @Autowired
    private IncidentServiceClient incidentServiceClient;

    @Autowired
    private LogServiceClient logServiceClient;

    public Responder getByName(String name) {
        return responderRepository.findByName(name);
    }
    @org.springframework.transaction.annotation.Transactional
    public ResponderDTO createResponder(ResponderDTO responderDTO) {
        Responder responder = convertToEntity(responderDTO);
        responder.setResponderId(0);
        responder.setStatus(ResponderStatus.AVAILABLE);
        responder.setIncidentId(null);
        Responder savedResponder = responderRepository.save(responder);
        record(savedResponder, null, "CREATED", null);
        return convertToDTO(savedResponder);
    }

    public Optional<ResponderDTO> getResponderById(Integer id) {
        return responderRepository.findById(id).map(this::convertToDTO);
    }

    public List<ResponderDTO> getAllResponders() {
        return responderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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


    @org.springframework.transaction.annotation.Transactional
    public ResponderDTO updateResponder(Integer id, ResponderDTO responderDTO) {
        Responder existingResponder = responderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Responder Not Found"));

        existingResponder.setName(responderDTO.getName());
        existingResponder.setStationLocation(responderDTO.getStationLocation());
        // Operational status changes must use the audited status endpoint.
        if (responderDTO.getStatus() != existingResponder.getStatus()) throw new IllegalArgumentException("Use status endpoint");
        existingResponder.setType(responderDTO.getType());
        existingResponder.setLastUpdate(LocalDateTime.now());
        existingResponder.setLocationId(responderDTO.getLocationId());
        existingResponder.setStationLocation(responderDTO.getStationLocation());

        Responder updatedResponder = responderRepository.save(existingResponder);
        record(updatedResponder, updatedResponder.getIncidentId(), "PROFILE_UPDATED", updatedResponder.getStatus());
        return convertToDTO(updatedResponder);
    }

    @org.springframework.transaction.annotation.Transactional
    public ResponderDTO updateStatus(Integer id, ResponderStatus status) {
        Responder responder = responderRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Responder not found"));
        if (status == null || status == ResponderStatus.ASSIGNED) throw new IllegalArgumentException("Invalid duty transition");
        ResponderStatus previous = responder.getStatus();
        Integer incidentId = responder.getIncidentId();
        if ((status == ResponderStatus.ON_ROUTE || status == ResponderStatus.ON_SCENE || status == ResponderStatus.REACHED_LOCATION)
                && incidentId == null) throw new IllegalArgumentException("No assigned incident");
        responder.setStatus(status);
        responder.setLastUpdate(LocalDateTime.now());
        if (status == ResponderStatus.AVAILABLE || status == ResponderStatus.NOT_AVAILABLE
                || status == ResponderStatus.OFF_DUTY || status == ResponderStatus.OFFLINE) responder.setIncidentId(null);
        responderRepository.save(responder);
        record(responder, incidentId, "STATUS_CHANGED", previous);
        // Duty changes never imply that the incident is resolved.
        if (incidentId != null) {
            try { logServiceClient.createLog(createLogDTO(incidentId, "Responder " + id + " status changed to " + status)); }
            catch (RuntimeException ignored) { /* Local transactional event remains the authoritative history. */ }
        }
        return convertToDTO(responder);
    }

    @org.springframework.transaction.annotation.Transactional
    public void assignAppropriateResponder(IncidentDTO incident) {
        ResponderType type = getRequiredResponderType(incident.getType());
        List<Responder> candidates = responderRepository.findByStatusAndType(ResponderStatus.AVAILABLE, type);
        if (candidates.isEmpty() && type != ResponderType.POLICE_OFFICER)
            candidates = responderRepository.findByStatusAndType(ResponderStatus.AVAILABLE, ResponderType.POLICE_OFFICER);
        if (candidates.isEmpty()) return;
        Responder responder = candidates.get(0);
        ResponderStatus previous = responder.getStatus();
        responder.setStatus(ResponderStatus.ASSIGNED);
        responder.setIncidentId(incident.getIncidentId());
        responder.setLastUpdate(LocalDateTime.now());
        responderRepository.save(responder);
        record(responder, incident.getIncidentId(), "ASSIGNED", previous);
    }

    @org.springframework.transaction.annotation.Transactional
    public void requestAdditionalResponders(Integer incidentId, ResponderType requiredResponderType) {
        // Fetch incident details
        IncidentDTO incident = incidentServiceClient.getIncidentById(incidentId);

        // Log the request
        LogDTO logDTO = createLogDTO(incidentId,
                "Requesting additional responders of type " + requiredResponderType);
        logDTO.setIncidentId(incidentId);
        logDTO.setTimestamp(LocalDateTime.now());
        logServiceClient.createLog(logDTO);

        // Find additional responders
        List<Responder> additionalResponders = responderRepository.findByStatusAndType(
                ResponderStatus.AVAILABLE, requiredResponderType
        );

        if(!additionalResponders.isEmpty()){

            Responder responder = additionalResponders.get(0);
            responder.setStatus(ResponderStatus.ASSIGNED);
            responder.setIncidentId(incident.getIncidentId());
            responder.setLastUpdate(LocalDateTime.now());
            responderRepository.save(responder);
            record(responder, incidentId, "ADDITIONAL_ASSIGNMENT", ResponderStatus.AVAILABLE);

            // Log successful assignment
            LogDTO successLogDTO = createLogDTO(incidentId,
                    "Additional responders assigned to the incident");
            successLogDTO.setIncidentId(incidentId);
            successLogDTO.setTimestamp(LocalDateTime.now());
            logServiceClient.createLog(successLogDTO);

        }

        else {
            // Log no available responders
            LogDTO noRespondersLogDTO = createLogDTO(incidentId,
                    "No additional responders available for type " + requiredResponderType);
            noRespondersLogDTO.setIncidentId(incidentId);
            noRespondersLogDTO.setTimestamp(LocalDateTime.now());
            logServiceClient.createLog(noRespondersLogDTO);

        }

    }

    private ResponderType getRequiredResponderType(IncidentType incidentType) {
        return switch (incidentType) {
            case MEDICAL_EMERGENCY, TRAFFIC_ACCIDENT -> ResponderType.PARAMEDIC;
            case FIRE -> ResponderType.FIREFIGHTER;
            case NATURAL_DISASTER -> ResponderType.RESCUE_TEAM;
            case HAZMAT -> ResponderType.HAZMAT_SPECIALIST;
            default -> ResponderType.POLICE_OFFICER;
        };
    }


    private IncidentStatus determineIncidentStatus(ResponderStatus responderStatus) {
        return switch (responderStatus) {
            case ON_ROUTE, ON_SCENE -> IncidentStatus.IN_PROGRESS;

            default -> null;
        };
    }

    private LogDTO createLogDTO(Integer incidentId, String message) {
        LogDTO logDTO = new LogDTO();
        logDTO.setStatusUpdate(message);
        logDTO.setTimestamp(LocalDateTime.now());
        logDTO.setIncidentId(incidentId);
        // System update
        return logDTO;
    }

    private ResponderDTO convertToDTO(Responder responder) {
        ResponderDTO dto = new ResponderDTO();
        dto.setResponderId(responder.getResponderId());
        dto.setName(responder.getName());
        dto.setStationLocation(responder.getStationLocation());
        dto.setStatus(responder.getStatus());
        dto.setType(responder.getType());
        dto.setLastUpdate(responder.getLastUpdate());
        if (responder.getLocationId() != null) {
            dto.setLocationId(responder.getLocationId());
        }
        if(responder.getIncidentId() != null){
            dto.setIncidentId(responder.getIncidentId());
        }
//
        return dto;
    }


    private Responder convertToEntity(ResponderDTO dto) {
        Responder responder = new Responder();
        responder.setResponderId(dto.getResponderId());
        responder.setName(dto.getName());
        responder.setStationLocation(dto.getStationLocation());
        responder.setStatus(dto.getStatus());
        responder.setType(dto.getType());
        responder.setLastUpdate(LocalDateTime.now());

        if (dto.getLocationId() != null) {
            LocationDTO location = locationServiceClient.getLocationById(dto.getLocationId());
            responder.setLocationId(location.getLocationId());
        }
        if(dto.getIncidentId() != null){
            responder.setIncidentId(dto.getIncidentId());
        }
        return responder;
    }

    public List<Responder> getResponderByIncidentId(Integer incidentId) {
        return responderRepository.findByIncidentId(incidentId);
    }
}

