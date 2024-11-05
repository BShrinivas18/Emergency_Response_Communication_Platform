package com.Emergency_Response_Management.Service;

import com.Emergency_Response_Management.DTO.ResponderDTO;
import com.Emergency_Response_Management.Enums.ResponderStatus;
import com.Emergency_Response_Management.Enums.ResponderType;
import com.Emergency_Response_Management.Exception.GeneralException;
import com.Emergency_Response_Management.Model.Location;
import com.Emergency_Response_Management.Model.Responder;
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
}
