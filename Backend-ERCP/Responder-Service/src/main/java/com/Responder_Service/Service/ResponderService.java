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
    private ResponderRepository responderRepository;

    @Autowired
    private LocationServiceClient locationServiceClient;

    @Autowired
    private IncidentServiceClient incidentServiceClient;

    @Autowired
    private LogServiceClient logServiceClient;

    public ResponderDTO createResponder(ResponderDTO responderDTO) {
        Responder responder = convertToEntity(responderDTO);
        Responder savedResponder = responderRepository.save(responder);
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


    public ResponderDTO updateResponder(Integer id, ResponderDTO responderDTO) {
        Responder existingResponder = responderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Responder Not Found"));

        existingResponder.setName(responderDTO.getName());
        existingResponder.setStationLocation(responderDTO.getStationLocation());
        existingResponder.setStatus(responderDTO.getStatus());
        existingResponder.setType(responderDTO.getType());
        existingResponder.setLastUpdate(LocalDateTime.now());
        existingResponder.setLocationId(responderDTO.getLocationId());
        existingResponder.setStationLocation(responderDTO.getStationLocation());

        Responder updatedResponder = responderRepository.save(existingResponder);
        return convertToDTO(updatedResponder);
    }

    public ResponderDTO updateStatus(Integer id, ResponderStatus status) {
        Responder responder = responderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Responder Not Found"));

        responder.setStatus(status);

        if(status == ResponderStatus.NOT_AVAILABLE || status == ResponderStatus.AVAILABLE){
            responder.setLastUpdate(LocalDateTime.now());
//           Integer incidentId = responder.getIncidentId();
//           System.out.println(incidentId);
//           responder.setPastIncidentIds(List.of(incidentId));
//           System.out.println(responder.getPastIncidentIds());
            responder.setIncidentId(null);
            responderRepository.save(responder);
            return convertToDTO(responder);
        }
//       responder.setLastUpdate(LocalDateTime.now());

        // Fetch incident details

        IncidentDTO incident = incidentServiceClient.getIncidentById(responder.getIncidentId());
//        System.out.println("qwertyuiopasdfghjklZxcvbnm    sdfghjhgfdsaasdfghjjhgfdsasdfgh");
//        System.out.println("incident "+ incident);
        // Update incident status based on responder status
        IncidentStatus incidentStatus = determineIncidentStatus(status);
//        System.out.println("incident status: "+incidentStatus);
        if (incidentStatus != null) {
            incidentServiceClient.updateIncidentStatus(incident.getIncidentId(),incidentStatus);
//            System.out.println("incident status updated");
        }

        LogDTO logDTO = createLogDTO(incident.getIncidentId(), "Status updated to " + status);
        logServiceClient.createLog(logDTO);

        Responder updatedResponder = responderRepository.save(responder);
        System.out.println("updated responder: "+updatedResponder);
        return convertToDTO(updatedResponder);
    }

    public void assignAppropriateResponder(IncidentDTO incident) {
        ResponderType requiredResponderType = getRequiredResponderType(incident.getType());
        System.out.println("requiredResponderType returned: "+ requiredResponderType);
        List<Responder> availableResponders = responderRepository.findByStatusAndType(
                ResponderStatus.AVAILABLE, requiredResponderType
        );

        if (!availableResponders.isEmpty()) {
            Responder responder = availableResponders.get(0);
            responder.setStatus(ResponderStatus.ASSIGNED);
            responder.setIncidentId(incident.getIncidentId());
            responder.setLastUpdate(LocalDateTime.now());
            // Log the assignment
            LogDTO logDTO = createLogDTO(incident.getIncidentId(),
                    "Responder automatically assigned: " + responder.getName() +"\n"
            + "Responder Type : " + responder.getType());
            logDTO.setIncidentId(incident.getIncidentId());
            logDTO.setTimestamp(LocalDateTime.now());
            logServiceClient.createLog(logDTO);

            responderRepository.save(responder);
        } else {
            if(!responderRepository.findByStatusAndType(ResponderStatus.AVAILABLE, ResponderType.POLICE_OFFICER).isEmpty()) {

                Responder responder = responderRepository.findByType(ResponderType.POLICE_OFFICER).get(0);
                responder.setStatus(ResponderStatus.ASSIGNED);
                responder.setIncidentId(incident.getIncidentId());
                responder.setLastUpdate(LocalDateTime.now());
                responderRepository.save(responder);
            }

            // Log warning about no available responders
            LogDTO logDTO = createLogDTO(
                    incident.getIncidentId(),
                    "WARNING: No available responders of type. " + requiredResponderType + "\n" +
                            "Don't Worry we will assign a " + requiredResponderType + " to the incident ASAP." + "\n" +
                            "Default Responder assigned"
            );

            logDTO.setTimestamp(LocalDateTime.now());
            logDTO.setIncidentId(incident.getIncidentId());

            logServiceClient.createLog(logDTO);

        }
    }

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
        System.out.println("additional responders: "+additionalResponders);

        if(!additionalResponders.isEmpty()){

            Responder responder = additionalResponders.get(0);
            responder.setStatus(ResponderStatus.ASSIGNED);
            responder.setIncidentId(incident.getIncidentId());
            responder.setLastUpdate(LocalDateTime.now());
            responderRepository.save(responder);

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
            case OFF_DUTY -> IncidentStatus.RESOLVED;
            default -> null;
        };
    }

    private LogDTO createLogDTO(Integer incidentId, String message) {
        LogDTO logDTO = new LogDTO();
        logDTO.setStatusUpdate(message);
        logDTO.setTimestamp(LocalDateTime.now());
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

