package com.Emergency_Response_Management.Controller;

import com.Emergency_Response_Management.DTO.LocationDTO;
import com.Emergency_Response_Management.Model.Location;
import com.Emergency_Response_Management.Service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @PostMapping
    public ResponseEntity<LocationDTO> createLocation(@RequestBody LocationDTO location) {
        return new ResponseEntity<>(locationService.createLocation(location), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<LocationDTO>> getAllLocations() {
        return new ResponseEntity<>(locationService.getAllLocations(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> getLocationById(@PathVariable Integer id) {
        return ResponseEntity.ok(locationService.getLocationById(id));

    }

    @GetMapping("/search")
    public ResponseEntity<List<LocationDTO>> findByCoordinates(
            @RequestParam Float latitude,
            @RequestParam Float longitude) {
        return ResponseEntity.ok(locationService.findByCoordinates(latitude, longitude));
    }

    @GetMapping("/address/{address}")
    public ResponseEntity<List<LocationDTO>> findByAddress(@PathVariable String address) {
        return ResponseEntity.ok(locationService.findByAddress(address));
    }


    @PutMapping("/{id}")
    public ResponseEntity<LocationDTO> updateLocation(
            @PathVariable Integer id,
            @RequestBody LocationDTO location) {
        return ResponseEntity.ok(locationService.updateLocation(id, location));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Integer id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
}
