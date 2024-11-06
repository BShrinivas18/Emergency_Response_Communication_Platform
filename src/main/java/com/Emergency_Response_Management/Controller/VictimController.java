package com.Emergency_Response_Management.Controller;

import com.Emergency_Response_Management.DTO.VictimDTO;
import com.Emergency_Response_Management.Model.Victim;
import com.Emergency_Response_Management.Service.VictimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/victims")
public class VictimController {

    @Autowired
    private VictimService victimService;

    @PostMapping
    public ResponseEntity<VictimDTO> createVictim(@RequestBody VictimDTO victim) {
        return ResponseEntity.ok(victimService.createVictim(victim));
    }

    @GetMapping
    public ResponseEntity<List<VictimDTO>> getAllVictims() {
        return ResponseEntity.ok(victimService.getAllVictims());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VictimDTO> getVictimById(@PathVariable Integer id) {
        return ResponseEntity.ok(victimService.getVictimById(id));
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<VictimDTO>> getVictimsByLocation(@PathVariable Integer locationId) {
        return ResponseEntity.ok(victimService.getVictimsByLocation(locationId));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<VictimDTO>> getVictimsByName(@PathVariable String name) {
        return ResponseEntity.ok(victimService.getVictimsByName(name));
    }


    @PutMapping("/{id}")
    public ResponseEntity<VictimDTO> updateVictim(
            @PathVariable Integer id,
            @RequestBody VictimDTO victim) {
        return ResponseEntity.ok(victimService.updateVictim(id, victim));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVictim(@PathVariable Integer id) {
        victimService.deleteVictim(id);
        return ResponseEntity.noContent().build();
    }
}
