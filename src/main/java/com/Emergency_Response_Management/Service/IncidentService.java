package com.Emergency_Response_Management.Service;

import com.Emergency_Response_Management.DTO.IncidentDTO;
import com.Emergency_Response_Management.Enums.IncidentStatus;
import com.Emergency_Response_Management.Exception.GeneralException;
import com.Emergency_Response_Management.Model.*;
import com.Emergency_Response_Management.Repository.DispatcherRepository;
import com.Emergency_Response_Management.Repository.IncidentRepository;
import com.Emergency_Response_Management.Repository.ResponderRepository;
import com.Emergency_Response_Management.Repository.VictimRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

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
    private DispatcherRepository dispatcherRepository;

    public IncidentDTO createIncident(IncidentDTO incidentDTO) {
        Incident incident = convertToEntity(incidentDTO);
        Incident savedIncident = incidentRepository.save(incident);
        return convertToDTO(savedIncident);
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

    public IncidentDTO assignResponder(Integer incidentId, Integer responderId) {
        Incident incident = getIncidentByIdEntity(incidentId);
        Responder responder = responderRepository.findById(responderId)
                .orElseThrow(() -> new RuntimeException("Responder not found"));

        incident.setAssignedResponder(responder);
        Incident updatedIncident = incidentRepository.save(incident);
        return convertToDTO(updatedIncident);
    }

    public IncidentDTO updateStatus(Integer id, IncidentStatus status) {
        Incident incident = getIncidentByIdEntity(id);
        incident.setStatus(status);
        Incident updatedIncident = incidentRepository.save(incident);
        return convertToDTO(updatedIncident);
    }

    public IncidentDTO updateIncident(Integer id, IncidentDTO incidentDTO) {
        Incident existingIncident = getIncidentByIdEntity(id);
        existingIncident.setLocation(incidentDTO.getLocation());
        existingIncident.setSeverity(incidentDTO.getSeverity());
        existingIncident.setType(incidentDTO.getType());
        existingIncident.setStatus(incidentDTO.getStatus());
        // Map other fields as necessary from incidentDTO
        Incident updatedIncident = incidentRepository.save(existingIncident);
        return convertToDTO(updatedIncident);
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
        dto.setLocation(incident.getLocation());
        dto.setSeverity(incident.getSeverity());
        dto.setType(incident.getType());
        dto.setTimestamp(incident.getTimestamp());
        dto.setStatus(incident.getStatus());
        dto.setVictimId(incident.getVictim() != null ? incident.getVictim().getVictimId() : null);
        dto.setResponderId(incident.getAssignedResponder() != null ? incident.getAssignedResponder().getResponderId() : null);
        dto.setDispatcherId(incident.getManagedBy() != null ? incident.getManagedBy().getDispatcherId() : null);
        dto.setLogIds(incident.getLogs().stream()
                .map(Log::getLogId)
                .collect(Collectors.toList()));
        return dto;
    }

    private Incident convertToEntity(IncidentDTO dto) {
        Incident incident = new Incident();
        incident.setIncidentId(dto.getIncidentId());
        incident.setLocation(dto.getLocation());
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
