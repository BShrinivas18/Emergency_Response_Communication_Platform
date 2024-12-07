package com.Api_Gateway_Server.Controller;

import com.Api_Gateway_Server.Model.Role;
import com.Api_Gateway_Server.Model.User;
import com.Api_Gateway_Server.Service.JwtService;
import com.Api_Gateway_Server.Service.UserCrudService;
import com.Api_Gateway_Server.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping()
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        // Authenticate the user using AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );

        // Check if authentication was successful
        if (authentication.isAuthenticated()) {
            // Extract authorities (roles) from the authenticated user
            String authorities = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            // Generate and return the JWT token including roles/authorities
            Role role = user.getRole();
            return jwtService.generateToken(user.getUsername(), role);  // Pass roles/authorities to JWT
        } else {
            return "Authentication failed";
        }
    }
}