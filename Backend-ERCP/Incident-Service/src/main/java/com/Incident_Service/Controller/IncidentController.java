package com.Incident_Service.Controller;

import com.Incident_Service.Dto.IncidentDTO;
import com.Incident_Service.Enums.IncidentStatus;
import com.Incident_Service.Model.Incident;
import com.Incident_Service.Service.IncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/incidents")
public class IncidentController {


    private final IncidentService incidentService;

    @Autowired
    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

//    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create")
//    public ResponseEntity<?> createIncident(
//            @RequestBody(required = false) IncidentDTO incident,
//            @RequestParam(required = false) Double latitude,
//            @RequestParam(required = false) Double longitude,
//            @RequestParam(required = false, defaultValue = "false") boolean isSOS
//    ) {
//        try {
//            IncidentDTO response = incidentService.reportIncident(incident, latitude, longitude, isSOS);
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return ResponseEntity
//                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Collections.singletonMap("error", e.getMessage()));
//        }
//    }
    public ResponseEntity<?> createIncident(@RequestBody IncidentDTO incident) {
        try {

            System.out.println("Received incident type: " + incident.getType());
            IncidentDTO response = incidentService.reportIncident(incident);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }


    //    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<IncidentDTO> getAllIncidents() {
        return incidentService.getAllIncidents();
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<IncidentDTO> getIncidentById(@PathVariable Integer id) {
        System.out.println("Hurray!!! get incident by id works");
        return incidentService.getIncidentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

//    @PreAuthorize("hasRole('USER')")
    @GetMapping("incident/status/{id}")
    public ResponseEntity<IncidentStatus> getIncidentStatus(@PathVariable Integer id) {
        return ResponseEntity.ok(incidentService.getIncidentStatus(id));
    }

//    @PreAuthorize("hasRole('ADMIN','RESPONDER')")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<IncidentDTO>> getIncidentsByStatus(@PathVariable IncidentStatus status) {
        return ResponseEntity.ok(incidentService.getIncidentsByStatus(status));
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/victim/{victimId}")
    public ResponseEntity<List<IncidentDTO>> getIncidentsByVictimId(@PathVariable Integer victimId) {
        return ResponseEntity.ok(incidentService.getIncidentsByVictimId(victimId));
    }

//    @PreAuthorize("hasRole('ADMIN','RESPONDER')")
    @PutMapping("/{id}/status")
    public void updateIncidentStatus(@PathVariable int id, @RequestParam IncidentStatus status) {
        incidentService.updateIncidentStatus(id, status);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncident(@PathVariable Integer id) {
        incidentService.deleteIncident(id);
        return ResponseEntity.noContent().build();
    }

}
