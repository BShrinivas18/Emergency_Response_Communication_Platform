package com.Responder_Service.FeignClient;

import com.Responder_Service.DTO.LogDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "LOG-SERVICE",path = "/logs")
public interface LogServiceClient {

    @PostMapping
    public void createLog( @RequestBody LogDTO log);
}
