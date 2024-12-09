package com.Responder_Service.FeignClient;

import com.Responder_Service.DTO.IncidentDTO;
import com.Responder_Service.Enums.IncidentStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "INCIDENT-SERVICE",path = "/incidents")
public interface IncidentServiceClient {

    @GetMapping("/{id}")
    public IncidentDTO getIncidentById(@PathVariable int id);

    @PutMapping("/{id}/status")
    public void updateIncidentStatus(@PathVariable int id, @RequestParam IncidentStatus status);

}
