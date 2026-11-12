package com.Api_Gateway_Server.Model;

import com.Api_Gateway_Server.Model.Enums.Role;

public record UserResponse(Long id, String username, Role role) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getRole());
    }
}
