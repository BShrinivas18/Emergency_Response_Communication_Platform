package com.Emergency_Response_Management.Controller;

import com.Emergency_Response_Management.Model.Log;
import com.Emergency_Response_Management.Service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    @Autowired
    private LogService logService;

    @PostMapping("/incident/{incidentId}")
    public ResponseEntity<Log> createLog(
            @RequestBody Log log,
            @PathVariable Integer incidentId) {
        return ResponseEntity.ok(logService.createLog(log, incidentId));
    }

    @GetMapping
    public List<Log> getAllLogs() {
        return logService.getAllLogs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Log> getLogById(@PathVariable Integer id) {
        return logService.getLogById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/incident/{incidentId}")
    public ResponseEntity<List<Log>> getLogsByIncident(@PathVariable Integer incidentId) {
        return ResponseEntity.ok(logService.getLogsByIncident(incidentId));
    }

    @GetMapping("/timerange")
    public ResponseEntity<List<Log>> getLogsByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(logService.getLogsByTimeRange(start, end));
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Log>> getLogsByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(logService.getLogsByUser(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Log> updateLog(
            @PathVariable Integer id,
            @RequestBody Log log) {
        return ResponseEntity.ok(logService.updateLog(id, log));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLog(@PathVariable Integer id) {
        logService.deleteLog(id);
        return ResponseEntity.noContent().build();
    }
}
