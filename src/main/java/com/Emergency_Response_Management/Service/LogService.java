package com.Emergency_Response_Management.Service;

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

    public Log createLog(Log log, Integer incidentId) {
        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident not found"));
        log.setIncident(incident);
        return logRepository.save(log);
    }

    public List<Log> getAllLogs() {
        return logRepository.findAll();
    }

    public Optional<Log> getLogById(Integer id) {

        return logRepository.findById(id);
    }

    public List<Log> getLogsByIncident(Integer incidentId) {
        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident not found"));
        return logRepository.findByIncident(incident);
    }

    public List<Log> getLogsByTimeRange(LocalDateTime start, LocalDateTime end) {
        return logRepository.findByTimestampBetween(start, end);
    }

    public List<Log> getLogsByUser(Integer userId) {
        return logRepository.findByUpdatedBy(userId);
    }

    public Log updateLog(Integer id, Log log) {
        Log existingLog = getLogById(id).orElseThrow(()->new GeneralException("Log Not Found"));
        existingLog.setStatusUpdate(log.getStatusUpdate());
        existingLog.setUpdatedBy(log.getUpdatedBy());
        return logRepository.save(existingLog);
    }
    public void deleteLog(Integer id) {
        logRepository.deleteById(id);
    }
}
