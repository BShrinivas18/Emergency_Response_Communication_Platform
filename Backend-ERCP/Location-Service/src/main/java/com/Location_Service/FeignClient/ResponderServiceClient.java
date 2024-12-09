package com.Location_Service.FeignClient;

import com.Location_Service.DTO.ResponderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "RESPONDER-SERVICE",path = "/responders")
public interface ResponderServiceClient {
    @GetMapping("/{id}")
    public ResponderDTO getResponderById(@PathVariable int id);
}
