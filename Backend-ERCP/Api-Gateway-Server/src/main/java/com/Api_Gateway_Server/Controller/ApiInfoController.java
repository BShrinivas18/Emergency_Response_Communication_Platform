package com.Api_Gateway_Server.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class ApiInfoController {
    @GetMapping("/")
    public Map<String, String> index() {
        return Map.of("service", "Emergency Response API", "frontend", "http://127.0.0.1:4200/",
            "message", "Open the frontend to use the application. Protected API endpoints require staff authentication.");
    }
}
