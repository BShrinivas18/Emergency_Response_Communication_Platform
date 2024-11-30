package com.Emergency_Response_Management.Controller;

import com.Emergency_Response_Management.DTO.IncidentDTO;
import com.Emergency_Response_Management.Enums.IncidentStatus;
import com.Emergency_Response_Management.Model.Incident;
import com.Emergency_Response_Management.Service.IncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    @Autowired
    private IncidentService incidentService;

//    @PostMapping
//    public ResponseEntity<IncidentDTO> createIncident(@RequestBody IncidentDTO incident) {
//        return ResponseEntity.ok(incidentService.reportIncident(incident));
//    }

    @PostMapping
    public ResponseEntity<?> createIncident(
            @RequestBody(required = false) IncidentDTO incident,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false, defaultValue = "false") boolean isSOS
    ) {
        try {
            IncidentDTO response = incidentService.reportIncident(incident, latitude, longitude, isSOS);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new HashMap<String, String>() {{
                        put("error", e.getMessage());
                    }});
        }
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
    public ResponseEntity<List<IncidentDTO>> getIncidentsByStatus(@RequestParam ResponseStatus status) {
        return ResponseEntity.ok(incidentService.getIncidentsByStatus(status));
    }

//    @PutMapping("/{incidentId}/status")
//    public ResponseEntity<IncidentDTO> updateStatus(
//            @PathVariable Integer incidentId,
//            @RequestParam IncidentStatus status,
//            @RequestParam String statusDetails,
//            @RequestParam Integer updatedBy) {
//        return ResponseEntity.ok(incidentService.updateIncidentStatus(
//                incidentId, status));
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncident(@PathVariable Integer id) {
        incidentService.deleteIncident(id);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping
    public ResponseEntity<Void> deleteAllIncidents() {
        for (IncidentDTO incident : incidentService.getAllIncidents()) {
            incidentService.deleteIncident(incident.getIncidentId());
        }
        return ResponseEntity.noContent().build();
    }
}
