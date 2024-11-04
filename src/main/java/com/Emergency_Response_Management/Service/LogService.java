package com.Emergency_Response_Management.Service;

import com.Emergency_Response_Management.DTO.LogDTO;
import com.Emergency_Response_Management.Exception.GeneralException;
import com.Emergency_Response_Management.Model.Incident;
import com.Emergency_Response_Management.Model.Log;
import com.Emergency_Response_Management.Repository.IncidentRepository;
import com.Emergency_Response_Management.Repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private IncidentRepository incidentRepository;

    private LogDTO convertToDTO(Log log) {
        LogDTO dto = new LogDTO();
        dto.setLogId(log.getLogId());
        dto.setStatusUpdate(log.getStatusUpdate());
        dto.setTimestamp(log.getTimestamp());
        dto.setUpdatedBy(log.getUpdatedBy());
        dto.setIncidentId(log.getIncident().getIncidentId());
        return dto;
    }

    private Log convertToEntity(LogDTO dto) {
        Log log = new Log();
        log.setLogId(dto.getLogId());
        log.setStatusUpdate(dto.getStatusUpdate());
        log.setTimestamp(dto.getTimestamp());
        log.setUpdatedBy(dto.getUpdatedBy());
        log.setIncident(incidentRepository.findById(dto.getIncidentId())
                .orElseThrow(() -> new RuntimeException("Incident not found")));
        return log;
    }

    public LogDTO createLog(LogDTO logDTO, Integer incidentId) {
        Log log = convertToEntity(logDTO);
        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident not found"));
        log.setIncident(incident);
        return convertToDTO(logRepository.save(log));
    }

    public List<LogDTO> getAllLogs() {
        return logRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<LogDTO> getLogById(Integer id) {
        return logRepository.findById(id)
                .map(this::convertToDTO);
    }

    public List<LogDTO> getLogsByIncident(Integer incidentId) {
        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident not found"));
        return logRepository.findByIncident(incident).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<LogDTO> getLogsByTimeRange(LocalDateTime start, LocalDateTime end) {
        return logRepository.findByTimestampBetween(start, end).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<LogDTO> getLogsByUser(Integer userId) {
        return logRepository.findByUpdatedBy(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public LogDTO updateLog(Integer id, LogDTO logDTO) {
        Log existingLog = logRepository.findById(id)
                .orElseThrow(() -> new GeneralException("Log not found"));
        existingLog.setStatusUpdate(logDTO.getStatusUpdate());
        existingLog.setUpdatedBy(logDTO.getUpdatedBy());
        return convertToDTO(logRepository.save(existingLog));
    }

    public void deleteLog(Integer id) {
        logRepository.deleteById(id);
    }
}
