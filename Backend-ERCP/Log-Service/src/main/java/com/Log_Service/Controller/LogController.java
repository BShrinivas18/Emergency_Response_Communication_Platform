package com.Log_Service.Controller;

import com.Log_Service.DTO.LogDTO;
import com.Log_Service.Model.Log;
import com.Log_Service.Service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/logs")
public class LogController {

    @Autowired
    private LogService logService;

//    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<LogDTO> createLog(@RequestBody LogDTO logDTO) {
        LogDTO createdLog = logService.createLog(logDTO);
        return new ResponseEntity<>(createdLog, HttpStatus.CREATED);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<LogDTO>> getAllLogs() {
        List<LogDTO> logs = logService.getAllLogs();
        return ResponseEntity.ok(logs);
    }


//    @PreAuthorize("hasRole('ADMIN','RESPONDER')")
    @GetMapping("/{id}")
    public ResponseEntity<LogDTO> getLogById(@PathVariable Integer id) {
        LogDTO log = logService.getLogById(id);
        return ResponseEntity.ok(log);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/timerange")
    public ResponseEntity<List<LogDTO>> getLogsByTimeRange(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        List<LogDTO> logs = logService.getLogsByTimeRange(start, end);
        return ResponseEntity.ok(logs);
    }

//    @PreAuthorize("hasRole('ADMIN','RESPONDER')")
    @GetMapping("/incident/{incidentId}")
    public ResponseEntity<List<LogDTO>> getLogsByIncident(@PathVariable Integer incidentId) {
        List<LogDTO> logs = logService.getLogsByIncident(incidentId);
        return ResponseEntity.ok(logs);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<LogDTO> updateLog(
            @PathVariable Integer id,
            @RequestBody LogDTO logDTO) {
        LogDTO updatedLog = logService.updateLog(id, logDTO);
        return ResponseEntity.ok(updatedLog);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLog(@PathVariable Integer id) {
        logService.deleteLog(id);
        return ResponseEntity.noContent().build();
    }

}
