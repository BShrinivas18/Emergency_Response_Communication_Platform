package com.Api_Gateway_Server.Controller;

import com.Api_Gateway_Server.FeignClient.ResponderFeignClient;
import com.Api_Gateway_Server.Model.LoginResponse;
import com.Api_Gateway_Server.Model.ResponderDto;
import com.Api_Gateway_Server.Model.Enums.Role;
import com.Api_Gateway_Server.Model.User;
import com.Api_Gateway_Server.Service.JwtService;
import com.Api_Gateway_Server.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    public User register(@RequestBody User user) {
        return userService.saveUser(user);

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody User user) {
        // Authenticate the user using AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        System.out.println("authentication: "+authentication);

        // Check if authentication was successful
        if (authentication.isAuthenticated()) {
            User dbuser = userService.findByUsername(user.getUsername());
            if (dbuser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse("Invalid response", null, null));
            }
            String token = jwtService.generateToken(user.getUsername(), dbuser.getRole());
            System.out.println("token -> " + token);
            if (dbuser.getRole() == Role.ADMIN) {
                LoginResponse loginResponse = new LoginResponse(token, dbuser.getId(), null);
                return ResponseEntity.ok(loginResponse);
            } else if (dbuser.getRole() == Role.RESPONDER) {
                try{
                    System.out.println("dbuser.getUsername() -> " + dbuser.getUsername());
                    System.out.println("inside if else or role responder");
                    ResponderDto responder = ResponderService.getResponderByUsername(dbuser.getUsername());
                    LoginResponse loginResponse = new LoginResponse(token, null, responder.getResponderId());
                    return ResponseEntity.ok(loginResponse);
                }
                catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new LoginResponse(token, null, null));
                }
            }

            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse("Invalid response", null, null));
            }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build() ;
    }

        @GetMapping("/user/{username}")
        public User getUser(@PathVariable String username) {
            return userService.findByUsername(username);
        }



    }
