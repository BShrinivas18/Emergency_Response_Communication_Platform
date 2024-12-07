package com.Incident_Service.FeignClient;

import com.Incident_Service.Dto.LogDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "LOG-SERVICE",path = "/logs")
public interface LogServiceClient {

    @PostMapping
    public LogDTO createLog( @RequestBody LogDTO log);
}
