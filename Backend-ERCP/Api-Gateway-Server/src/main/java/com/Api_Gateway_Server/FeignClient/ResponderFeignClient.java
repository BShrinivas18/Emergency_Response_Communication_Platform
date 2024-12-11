package com.Api_Gateway_Server.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "responder-service")
public interface ResponderFeignClient {

}
