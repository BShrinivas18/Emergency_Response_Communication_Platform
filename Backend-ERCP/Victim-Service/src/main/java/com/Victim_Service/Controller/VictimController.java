package com.Victim_Service.Controller;

import com.Victim_Service.DTO.VictimDTO;
import com.Victim_Service.Model.Victim;
import com.Victim_Service.Service.VictimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/victims")
public class VictimController {

    @Autowired
    private VictimService victimService;

//    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<VictimDTO> createVictim(@RequestBody VictimDTO victimDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(victimService.createVictim(victimDTO));
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<VictimDTO>> getAllVictims() {
        return ResponseEntity.ok(victimService.getAllVictims());
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<VictimDTO> getVictimById(@PathVariable Integer id) {
        return ResponseEntity.ok(victimService.getVictimById(id));
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/name/{name}")
    public ResponseEntity<List<VictimDTO>> getVictimsByName(@PathVariable String name) {
        return ResponseEntity.ok(victimService.getVictimsByName(name));
    }


//    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<VictimDTO> updateVictim(
            @PathVariable Integer id,
            @RequestBody VictimDTO victimDTO) {
        return ResponseEntity.ok(victimService.updateVictim(id, victimDTO));
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVictim(@PathVariable Integer id) {
        victimService.deleteVictim(id);
        return ResponseEntity.noContent().build();
    }
}
