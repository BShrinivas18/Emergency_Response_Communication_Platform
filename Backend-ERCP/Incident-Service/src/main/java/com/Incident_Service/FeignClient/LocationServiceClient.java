package com.Incident_Service.FeignClient;

import com.Incident_Service.Dto.LocationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@FeignClient(name = "LOCATION-SERVICE",path = "/locations")
public interface LocationServiceClient {
    @GetMapping("/{id}")
     public LocationDTO getLocationById(@PathVariable int id);

    @PostMapping
    public LocationDTO createLocation(@RequestBody LocationDTO location);

    @GetMapping("/coordinates")
    public Optional<LocationDTO> findByLatitudeAndLongitude(@RequestParam Double latitude, @RequestParam Double longitude);
}
