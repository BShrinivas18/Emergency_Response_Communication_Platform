package com.Location_Service.Controller;

import com.Location_Service.DTO.IncidentDTO;
import com.Location_Service.DTO.LocationDTO;
import com.Location_Service.Model.Location;
import com.Location_Service.Service.LocationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

//    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<LocationDTO> createLocation(@Valid @RequestBody LocationDTO location) {
        return new ResponseEntity<>(locationService.createLocation(location), HttpStatus.CREATED);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<LocationDTO>> getAllLocations() {
        return new ResponseEntity<>(locationService.getAllLocations(), HttpStatus.OK);
    }

//    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> getLocationById(@PathVariable Integer id) {
        return ResponseEntity.ok(locationService.getLocationById(id));
    }

//    @PreAuthorize("hasRole('ADMIN','RESPONDER')")
    @GetMapping("/address/{address}")
    public ResponseEntity<List<LocationDTO>> findByAddress(@PathVariable String address) {
        return ResponseEntity.ok(locationService.findByAddress(address));
    }

//    @PreAuthorize("hasRole('ADMIN','RESPONDER')")
    @PutMapping("/{id}")
    public ResponseEntity<LocationDTO> updateLocation(
            @PathVariable Integer id,
            @Valid @RequestBody LocationDTO location) {
        return ResponseEntity.ok(locationService.updateLocation(id, location));
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Integer id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/coordinates")
    public ResponseEntity<Optional<LocationDTO>> getByCoordinates(@RequestParam Double latitude, @RequestParam Double longitude){
        System.out.println("trying to find location by latitude and longitude VIA CONTROLLER");
        return ResponseEntity.ok(locationService.findByLatitudeAndLongitude(latitude, longitude));
          }



}
