package com.Emergency_Response_Management.Controller;

import com.Emergency_Response_Management.DTO.ResponderDTO;
import com.Emergency_Response_Management.Model.Responder;
import com.Emergency_Response_Management.Service.ResponderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/responders")
public class ResponderController {

    @Autowired
    private ResponderService responderService;

    @PostMapping
    public ResponderDTO createResponder(@RequestBody ResponderDTO responder) {
        return responderService.createResponder(responder);
    }

    @GetMapping
    public List<ResponderDTO> getAllResponders() {
        return responderService.getAllResponders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponderDTO> getResponderById(@PathVariable Integer id) {
        return responderService.getResponderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ResponderDTO>> getRespondersByStatus(@PathVariable String status) {
        return ResponseEntity.ok(responderService.getRespondersByStatus(status));
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<ResponderDTO>> getRespondersByRole(@PathVariable String role) {
        return ResponseEntity.ok(responderService.getRespondersByRole(role));
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<ResponderDTO>> getRespondersByLocation(@PathVariable Integer locationId) {
        return ResponseEntity.ok(responderService.getRespondersByLocation(locationId));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ResponderDTO> updateResponder(
            @PathVariable Integer id,
            @RequestBody ResponderDTO responder) {
        return ResponseEntity.ok(responderService.updateResponder(id, responder));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ResponderDTO> updateStatus(
            @PathVariable Integer id,
            @RequestParam String status) {
        return ResponseEntity.ok(responderService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResponder(@PathVariable Integer id) {
        responderService.deleteResponder(id);
        return ResponseEntity.noContent().build();
    }
}
