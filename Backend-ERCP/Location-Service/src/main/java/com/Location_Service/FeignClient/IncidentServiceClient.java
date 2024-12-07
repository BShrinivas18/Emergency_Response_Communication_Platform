package com.Location_Service.FeignClient;

import com.Location_Service.DTO.IncidentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "INCIDENT-SERVICE",path = "/incidents")
public interface IncidentServiceClient {

    @GetMapping("/{id}")
    IncidentDTO getIncidentById(@PathVariable int id);

}
