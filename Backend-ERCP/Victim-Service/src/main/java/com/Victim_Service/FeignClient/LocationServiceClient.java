package com.Victim_Service.FeignClient;

import com.Victim_Service.DTO.LocationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "LOCATION-SERVICE",path = "/locations")
public interface LocationServiceClient {
    @GetMapping("/{id}")
    public LocationDTO getLocationById(@PathVariable int id);
}