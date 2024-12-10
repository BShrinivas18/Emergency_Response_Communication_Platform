package com.Victim_Service.FeignClient;

import com.Victim_Service.DTO.IncidentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "INCIDENT-SERVICE",path = "/incidents")
public interface IncidentServiceClient {

    @GetMapping("/victim/{victimId}")
    public List<IncidentDTO> getVictimById(@PathVariable int victimId);
}
