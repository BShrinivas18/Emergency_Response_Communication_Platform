package com.Location_Service.FeignClient;

import com.Location_Service.DTO.VictimDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "VICTIM-SERVICE",path = "/victims")
public interface VictimServiceClient {

    @GetMapping("/{id}")
    public VictimDTO getVictimById(@PathVariable int id);
}
