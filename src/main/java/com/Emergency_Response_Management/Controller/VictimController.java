package com.Emergency_Response_Management.Controller;

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
    public ResponseEntity<Victim> createVictim(@RequestBody Victim victim) {
        return ResponseEntity.ok(victimService.createVictim(victim));
    }

    @GetMapping
    public ResponseEntity<List<Victim>> getAllVictims() {
        return ResponseEntity.ok(victimService.getAllVictims());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Victim> getVictimById(@PathVariable Integer id) {
        return ResponseEntity.ok(victimService.getVictimById(id));
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<Victim>> getVictimsByLocation(@PathVariable Integer locationId) {
        return ResponseEntity.ok(victimService.getVictimsByLocation(locationId));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Victim>> getVictimsByName(@PathVariable String name) {
        return ResponseEntity.ok(victimService.getVictimsByName(name));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Victim> updateVictim(
            @PathVariable Integer id,
            @RequestBody Victim victim) {
        return ResponseEntity.ok(victimService.updateVictim(id, victim));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVictim(@PathVariable Integer id) {
        victimService.deleteVictim(id);
        return ResponseEntity.noContent().build();
    }
}
