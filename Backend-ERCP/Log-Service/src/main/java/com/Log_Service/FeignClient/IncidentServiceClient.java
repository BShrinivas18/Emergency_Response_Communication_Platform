package com.Log_Service.FeignClient;

import com.Log_Service.DTO.IncidentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "INCIDENT-SERVICE",path = "/incidents")
public interface IncidentServiceClient {
    @GetMapping("/{id}")
    public IncidentDTO getIncidentById(@PathVariable int id);
}
