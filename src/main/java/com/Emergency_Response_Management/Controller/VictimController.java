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
    public Victim createVictim(@RequestBody Victim victim) {
        return victimService.createVictim(victim);
    }

    @GetMapping
    public List<Victim> getAllVictims() {
        return victimService.getAllVictims();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Victim> getVictimById(@PathVariable Integer id) {
        return victimService.getVictimById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Victim updateVictim(@PathVariable Integer id, @RequestBody Victim updatedVictim) {
        return victimService.updateVictim(id, updatedVictim);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVictim(@PathVariable Integer id) {
        victimService.deleteVictim(id);
        return ResponseEntity.noContent().build();
    }
}
