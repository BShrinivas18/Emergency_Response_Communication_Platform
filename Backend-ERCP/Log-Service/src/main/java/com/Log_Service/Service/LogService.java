package com.Log_Service.Service;

import com.Log_Service.DTO.LogDTO;
import com.Log_Service.Model.Log;
import com.Log_Service.Repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    public LogDTO createLog(LogDTO logDTO) {
        Log log = convertToEntity(logDTO);
        Log savedLog = logRepository.save(log);
        return convertToDTO(savedLog);
    }

    public List<LogDTO> getAllLogs() {
        return logRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public LogDTO getLogById(Integer id) {
        Log log = logRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Log not found with id: " + id));
        return convertToDTO(log);
    }

    public List<LogDTO> getLogsByTimeRange(LocalDateTime start, LocalDateTime end) {
        return logRepository.findByTimestampBetween(start, end).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public List<LogDTO> getLogsByIncident(Integer incidentId) {
        return logRepository.findByIncidentId(incidentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public LogDTO updateLog(Integer id, LogDTO logDTO) {
        Log existingLog = logRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Log not found with id: " + id));

        existingLog.setStatusUpdate(logDTO.getStatusUpdate());
        existingLog.setTimestamp(logDTO.getTimestamp());
//        existingLog.setUpdatedBy(logDTO.getUpdatedBy());
        existingLog.setIncidentId(logDTO.getIncidentId());
        Log updatedLog = logRepository.save(existingLog);
        return convertToDTO(updatedLog);
    }

    public void deleteLog(Integer id) {
        Log log = logRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Log not found with id: " + id));
        logRepository.delete(log);
    }

    private LogDTO convertToDTO(Log log) {
        LogDTO dto = new LogDTO();
        dto.setLogId(log.getLogId());
        dto.setStatusUpdate(log.getStatusUpdate());
        dto.setTimestamp(log.getTimestamp());
        dto.setIncidentId(log.getIncidentId());

//        dto.setUpdatedBy(log.getUpdatedBy());
        return dto;
    }

    private Log convertToEntity(LogDTO dto) {
        Log log = new Log();
        log.setIncidentId(dto.getIncidentId());
        log.setLogId(dto.getLogId());
        log.setStatusUpdate(dto.getStatusUpdate());
        log.setTimestamp(dto.getTimestamp());
        log.setIncidentId(dto.getIncidentId());
//        log.setUpdatedBy(dto.getUpdatedBy());
        return log;
    }
}
