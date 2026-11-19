package com.Api_Gateway_Server.Controller;

import com.Api_Gateway_Server.FeignClient.ResponderFeignClient;
import com.Api_Gateway_Server.Model.LoginResponse;
import com.Api_Gateway_Server.Model.ResponderDto;
import com.Api_Gateway_Server.Model.Enums.Role;
import com.Api_Gateway_Server.Model.User;
import com.Api_Gateway_Server.Model.UserResponse;
import feign.FeignException;
import java.nio.charset.StandardCharsets;
import org.springframework.security.access.prepost.PreAuthorize;
import com.Api_Gateway_Server.Service.JwtService;
import com.Api_Gateway_Server.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping()
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ResponderFeignClient ResponderService;

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse register(@RequestBody User user) {
        if (user.getUsername() == null || user.getUsername().isBlank() || user.getRole() == null
                || user.getPassword() == null || user.getPassword().length() < 12
                || user.getPassword().getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid account details");
        }
        user.setUsername(user.getUsername().strip());
        if (userService.findByUsername(user.getUsername()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Account already exists");
        }
        if (user.getRole() == Role.RESPONDER) {
            requireResponderProfile(user.getUsername());
        }
        user.setId(null);
        return UserResponse.from(userService.saveUser(user));

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
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse("Invalid response", null, null));
            }
            String token = jwtService.generateToken(user.getUsername(), dbuser.getRole());
            if (dbuser.getRole() == Role.ADMIN) {
                LoginResponse loginResponse = new LoginResponse(token, dbuser.getId(), null);
                return ResponseEntity.ok(loginResponse);
            } else if (dbuser.getRole() == Role.RESPONDER) {
                ResponderDto responder = requireResponderProfile(dbuser.getUsername());
                return ResponseEntity.ok(new LoginResponse(token, null, responder.getResponderId()));
            }

            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse("Invalid response", null, null));
            }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build() ;
    }

    private ResponderDto requireResponderProfile(String username) {
        try {
            ResponderDto responder = ResponderService.getResponderByUsername(username);
            if (responder != null && responder.getResponderId() > 0) return responder;
        } catch (FeignException.NotFound ignored) {
            // A staff account needs a responder profile before it can sign in.
        } catch (FeignException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Responder lookup unavailable");
        }
        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Responder profile required");
    }

        @GetMapping("/user/{username}")
        @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
        public ResponseEntity<UserResponse> getUser(@PathVariable String username) {
            User user = userService.findByUsername(username);
            return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(UserResponse.from(user));
        }



    }
