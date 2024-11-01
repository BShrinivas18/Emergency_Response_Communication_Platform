package com.Emergency_Response_Management.Controller;

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
    public Responder createResponder(@RequestBody Responder responder) {
        return responderService.createResponder(responder);
    }

    @GetMapping
    public List<Responder> getAllResponders() {
        return responderService.getAllResponders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Responder> getResponderById(@PathVariable Integer id) {
        return responderService.getResponderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Responder updateResponder(@PathVariable Integer id, @RequestBody Responder updatedResponder) {
        return responderService.updateResponder(id, updatedResponder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResponder(@PathVariable Integer id) {
        responderService.deleteResponder(id);
        return ResponseEntity.noContent().build();
    }
}
