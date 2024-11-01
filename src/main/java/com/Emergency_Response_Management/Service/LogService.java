package com.Emergency_Response_Management.Service;

import com.Emergency_Response_Management.Model.Log;
import com.Emergency_Response_Management.Repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    public Log createLog(Log log) {
        return logRepository.save(log);
    }

    public List<Log> getAllLogs() {
        return logRepository.findAll();
    }

    public Optional<Log> getLogById(Integer id) {
        return logRepository.findById(id);
    }

    public Log updateLog(Integer id, Log updatedLog) {
        updatedLog.setLogId(id);
        return logRepository.save(updatedLog);
    }

    public void deleteLog(Integer id) {
        logRepository.deleteById(id);
    }
}
