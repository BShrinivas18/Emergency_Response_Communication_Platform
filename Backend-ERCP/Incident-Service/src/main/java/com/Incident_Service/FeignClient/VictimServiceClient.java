package com.Incident_Service.FeignClient;

import com.Incident_Service.Dto.VictimDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "VICTIM-SERVICE",path = "/victims")
public interface VictimServiceClient {

    @PostMapping
    public VictimDTO createVictim( @RequestBody VictimDTO victim);

    @GetMapping("{id}")
    public VictimDTO getVictimById(@PathVariable int id);
}
