package com.Api_Gateway_Server.Controller;

import com.Api_Gateway_Server.Model.LoginResponse;
import com.Api_Gateway_Server.Model.Role;
import com.Api_Gateway_Server.Model.User;
import com.Api_Gateway_Server.Service.JwtService;
import com.Api_Gateway_Server.Service.UserCrudService;
import com.Api_Gateway_Server.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<LoginResponse> login(@RequestBody User user) {
        // Authenticate the user using AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );

        // Check if authentication was successful
        if (authentication.isAuthenticated()) {
            User dbuser = userService.findByUsername(user.getUsername());
            if (dbuser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse("Invalid Credentials"));
            }
            String token = jwtService.generateToken(user.getUsername(), dbuser.getRole());
            LoginResponse loginResponse = new LoginResponse(token);
            return ResponseEntity.ok(loginResponse);


            // Extract authorities (roles) from the authenticated user
//            String authorities = authentication.getAuthorities().stream()
//                    .map(GrantedAuthority::getAuthority)
//                    .collect(Collectors.joining(","));

            // Generate and return the JWT token including roles/authorities
//            Role role = user.getRole();

//            return jwtService.generateToken(user.getUsername(), role);  // Pass roles/authorities to JWT
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse("Invalid Credentials"));
        }
        }

        @GetMapping("/user/{username}")
        public User getUser(@PathVariable String username) {
            return userService.findByUsername(username);
        }



    }
