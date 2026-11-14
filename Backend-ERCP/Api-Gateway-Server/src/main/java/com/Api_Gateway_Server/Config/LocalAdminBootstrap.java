package com.Api_Gateway_Server.Config;

import com.Api_Gateway_Server.Model.User;
import com.Api_Gateway_Server.Model.Enums.Role;
import com.Api_Gateway_Server.Repository.UserRepository;
import com.Api_Gateway_Server.Service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

/** Explicit, local-only initial account provisioning; never resets existing credentials. */
@Component
@Profile("local")
public class LocalAdminBootstrap implements CommandLineRunner {
    private final UserRepository repository;
    private final UserService users;
    private final String username;
    private final String password;
    public LocalAdminBootstrap(UserRepository repository, UserService users,
            @Value("${ERCP_LOCAL_ADMIN_USERNAME:}") String username,
            @Value("${ERCP_LOCAL_ADMIN_PASSWORD:}") String password) {
        this.repository = repository;
        this.users = users;
        this.username = username;
        this.password = password;
    }
    @Override public void run(String... args) {
        if (username.isBlank() || password.isBlank() || repository.count() != 0) return;
        if (password.length() < 16) throw new IllegalArgumentException("Local bootstrap password must be at least 16 characters");
        users.saveUser(new User(null, username, password, Role.ADMIN));
    }
}
