package com.Api_Gateway_Server.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class HelloController {

    @GetMapping("hello")
    public String greet(HttpServletRequest request){
        return "Hello World"+request.getSession().getId();
    }

    @GetMapping("about")
    public String about(HttpServletRequest request){
        return "Gokul " + request.getSession().getId();
    }
}
