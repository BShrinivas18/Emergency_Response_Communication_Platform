package com.Emergency_Response_Management.Controller;

import com.Emergency_Response_Management.DTO.IncidentDTO;
import com.Emergency_Response_Management.Model.Incident;
import com.Emergency_Response_Management.Service.IncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    @Autowired
    private IncidentService incidentService;

    @PostMapping
    public IncidentDTO createIncident(@RequestBody IncidentDTO incident) {
        return incidentService.createIncident(incident);
    }

    @GetMapping
    public List<IncidentDTO> getAllIncidents() {
        return incidentService.getAllIncidents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidentDTO> getIncidentById(@PathVariable Integer id) {
        return incidentService.getIncidentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<IncidentDTO>> getIncidentsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(incidentService.getIncidentsByStatus(status));
    }

    @PutMapping("/{id}/assign/{responderId}")
    public ResponseEntity<IncidentDTO> assignResponder(
            @PathVariable Integer id,
            @PathVariable Integer responderId) {
        return ResponseEntity.ok(incidentService.assignResponder(id, responderId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<IncidentDTO> updateStatus(
            @PathVariable Integer id,
            @RequestParam String status) {
        return ResponseEntity.ok(incidentService.updateStatus(id, status));
    }


    @PutMapping("/{id}")
    public ResponseEntity<IncidentDTO> updateIncident(
            @PathVariable Integer id,
            @RequestBody IncidentDTO incident) {
        return ResponseEntity.ok(incidentService.updateIncident(id, incident));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncident(@PathVariable Integer id) {
        incidentService.deleteIncident(id);
        return ResponseEntity.noContent().build();
    }
}
