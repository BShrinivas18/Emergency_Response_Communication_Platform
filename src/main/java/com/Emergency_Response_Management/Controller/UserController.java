package com.Emergency_Response_Management.Controller;

import com.Emergency_Response_Management.DTO.UserDTO;
import com.Emergency_Response_Management.Exception.GeneralException;
import com.Emergency_Response_Management.Model.User;
import com.Emergency_Response_Management.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Users")
public class UserController {

    @Autowired
    private UserService UserService;

    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO User) {
        return UserService.createUser(User);
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return UserService.getAllUsers();
    }

    @GetMapping("/{id}")
    @ResponseStatus
    public UserDTO getUserById(@PathVariable Integer id) {
        if(UserService.getUserById(id)!=null)
        return UserService.getUserById(id);
        else
            throw new GeneralException("User Not Found");
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable Integer id, @RequestBody UserDTO updatedUser) {
        return UserService.updateUser(id, updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        UserService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
