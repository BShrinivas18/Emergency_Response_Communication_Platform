package com.Emergency_Response_Management.Controller;

import com.Emergency_Response_Management.DTO.LogDTO;
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

    @GetMapping
    public List<LogDTO> getAllLogs() {
        return logService.getAllLogs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LogDTO> getLogById(@PathVariable Integer id) {
        return logService.getLogById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/incident/{incidentId}")
    public ResponseEntity<List<LogDTO>> getLogsByIncident(@PathVariable Integer incidentId) {
        return ResponseEntity.ok(logService.getLogsByIncident(incidentId));
    }

    @GetMapping("/timerange")
    public ResponseEntity<List<LogDTO>> getLogsByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(logService.getLogsByTimeRange(start, end));
    }

//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<LogDTO>> getLogsByUser(@PathVariable Integer userId) {
//        return ResponseEntity.ok(logService.getLogsByUser(userId));
//    }

    @PutMapping("/{id}")
    public ResponseEntity<LogDTO> updateLog(
            @PathVariable Integer id,
            @RequestBody LogDTO logDTO) {
        return ResponseEntity.ok(logService.updateLog(id, logDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLog(@PathVariable Integer id) {
        logService.deleteLog(id);
        return ResponseEntity.noContent().build();
    }
}
