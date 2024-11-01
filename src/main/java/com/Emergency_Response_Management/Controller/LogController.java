package com.Emergency_Response_Management.Controller;

import com.Emergency_Response_Management.Model.Log;
import com.Emergency_Response_Management.Service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    @Autowired
    private LogService logService;

    @PostMapping
    public Log createLog(@RequestBody Log log) {
        return logService.createLog(log);
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

    @PutMapping("/{id}")
    public Log updateLog(@PathVariable Integer id, @RequestBody Log updatedLog) {
        return logService.updateLog(id, updatedLog);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLog(@PathVariable Integer id) {
        logService.deleteLog(id);
        return ResponseEntity.noContent().build();
    }
}
