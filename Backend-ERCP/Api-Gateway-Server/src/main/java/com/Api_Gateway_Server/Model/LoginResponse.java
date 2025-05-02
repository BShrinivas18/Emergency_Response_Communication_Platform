package com.Api_Gateway_Server.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String jwtToken;
    private Long userId;       // Admins
    private Integer responderId;  // Responders
}

