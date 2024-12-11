package com.Responder_Service.Controller;

import com.Responder_Service.DTO.IncidentDTO;
import com.Responder_Service.DTO.ResponderDTO;
import com.Responder_Service.Enums.ResponderStatus;
import com.Responder_Service.Enums.ResponderType;
import com.Responder_Service.Model.Responder;
import com.Responder_Service.Service.ResponderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/responders")
public class ResponderController {

    @Autowired
    private ResponderService responderService;


//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponderDTO createResponder(@RequestBody ResponderDTO responder) {
        return responderService.createResponder(responder);
    }


//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<ResponderDTO> getAllResponders() {
        return responderService.getAllResponders();
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ResponderDTO> getResponderById(@PathVariable Integer id) {
        return responderService.getResponderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

//    @PreAuthorize("hasRole('ADMIN','RESPONDER')")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ResponderDTO>> getRespondersByStatus(@PathVariable ResponderStatus status) {
        return ResponseEntity.ok(responderService.getRespondersByStatus(status));
    }

//    @PreAuthorize("hasRole('ADMIN','RESPONDER')")
    @GetMapping("/type/{type}")
    public ResponseEntity<List<ResponderDTO>> getRespondersByType(@PathVariable ResponderType type) {
        return ResponseEntity.ok(responderService.getRespondersByType(type));
    }

//    @PreAuthorize("hasRole('ADMIN','RESPONDER')")
    @PutMapping("/{id}")
    public ResponseEntity<ResponderDTO> updateResponder(
            @PathVariable Integer id,
            @RequestBody ResponderDTO responder) {
        return ResponseEntity.ok(responderService.updateResponder(id, responder));
    }

//    @PreAuthorize("hasRole('ADMIN','RESPONDER')")
    @PatchMapping("/{id}/{status}")
    public ResponseEntity<ResponderDTO> updateStatus(
            @PathVariable Integer id,
            @PathVariable ResponderStatus status) {
        return ResponseEntity.ok(responderService.updateStatus(id, status));
    }


    @PostMapping("/assign-responder")
    public ResponseEntity<Void> assignResponder(@RequestBody IncidentDTO incident) {
        responderService.assignAppropriateResponder(incident);
        return ResponseEntity.ok().build();
    }

//    @PreAuthorize("hasRole('ADMIN','RESPONDER')")
    @PutMapping("/request-additional/{id}/{type}")
    public ResponseEntity<Void> requestResponder(
            @PathVariable Integer id,
            @PathVariable ResponderType type) {
        responderService.requestAdditionalResponders(id, type);
        return ResponseEntity.ok().build();
    }

//    @PreAuthorize("hasRole('ADMIN','RESPONDER')")
    @GetMapping("/incident/{incidentId}")
    public List<Responder> getRespondersByIncidentId(@PathVariable Integer incidentId) {
        return responderService.getResponderByIncidentId(incidentId);
    }




}
