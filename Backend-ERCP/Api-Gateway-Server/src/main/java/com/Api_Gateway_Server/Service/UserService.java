package com.Api_Gateway_Server.Service;

import com.Api_Gateway_Server.Model.User;
import com.Api_Gateway_Server.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserCrudService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }
    public User findByUsername(String username){
        return userRepository.findByUsername(username).orElse(null);
    }

    public Long getIdByUsername(String username){
        User user = userRepository.getIdByUsername(username);
        return user.getId();

    }
}
