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
        dto.setIncidentId(log.getIncident().getIncidentId()); // Assuming you have a relation set up
        return dto;
    }

    private Log convertToEntity(LogDTO dto) {
        Log log = new Log();
        log.setLogId(dto.getLogId());
        log.setStatusUpdate(dto.getStatusUpdate());
        log.setTimestamp(dto.getTimestamp());
        log.setUpdatedBy(dto.getUpdatedBy());
        return log;
    }

    public LogDTO createLog(LogDTO log, Integer incidentId) {
        Log log1 = convertToEntity(log);
        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(()-> new GeneralException("Log not found"));
        log1.setIncident(incident);
        Log saveLog = logRepository.save(log1);
        return convertToDTO(saveLog);

    }
    public List<LogDTO> getAllLogs() {

        return logRepository.findAll().stream().map(this::convertToDTO).toList();

    }

    public Optional<LogDTO> getLogById(Integer id) {
        return logRepository.findById(id) // Find the log by ID
                .map(this::convertToDTO); // Convert to DTO if found
    }

    public List<LogDTO> getLogsByIncident(Integer incidentId) {
        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident not found"));
        return logRepository.findByIncident(incident).stream().map(this::convertToDTO).toList();
    }

    public List<LogDTO> getLogsByTimeRange(LocalDateTime start, LocalDateTime end) {
        return logRepository.findByTimestampBetween(start, end).stream().map(this::convertToDTO).toList();
    }

    public List<LogDTO> getLogsByUser(Integer userId) {
        return logRepository.findByUpdatedBy(userId).stream().map(this::convertToDTO).toList();
    }

    public LogDTO updateLog(Integer id, LogDTO log) {
        Log existingLog = logRepository.findById(id).orElseThrow(()-> new GeneralException("log not found"));
        existingLog.setStatusUpdate(log.getStatusUpdate());
        existingLog.setUpdatedBy(log.getUpdatedBy());
        return convertToDTO(logRepository.save(existingLog));
    }
    public void deleteLog(Integer id) {
        logRepository.deleteById(id);
    }
}
