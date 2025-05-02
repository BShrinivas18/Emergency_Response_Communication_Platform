package com.Api_Gateway_Server.FeignClient;

import com.Api_Gateway_Server.Model.ResponderDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "RESPONDER-SERVICE", path = "/responders")
public interface ResponderFeignClient {

    @GetMapping("/name/{name}")
    ResponderDto getResponderByUsername(@PathVariable("name") String username);
}
