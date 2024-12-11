package com.Api_Gateway_Server.Repository;

import com.Api_Gateway_Server.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    public Optional<User> findByUsername(String username);

    User getIdByUsername(String username);

}
