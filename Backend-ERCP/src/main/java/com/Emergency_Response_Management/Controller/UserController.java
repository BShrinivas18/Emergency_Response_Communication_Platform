package com.Emergency_Response_Management.Controller;

import com.Emergency_Response_Management.DTO.UserDTO;
import com.Emergency_Response_Management.Exception.GeneralException;
import com.Emergency_Response_Management.Model.User;
import com.Emergency_Response_Management.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Users")
public class UserController {

    @Autowired
    private UserService UserService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO User) {
        return new ResponseEntity<>(UserService.createUser(User), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return new ResponseEntity<>(UserService.getAllUsers(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
//    @ResponseStatus
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
//        if(UserService.getUserById(id)!=null)
        return new ResponseEntity<>(UserService.getUserById(id),HttpStatus.FOUND);
//        else
//            throw new GeneralException("User Not Found");
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Integer id, @RequestBody UserDTO updatedUser) {
        return new ResponseEntity<>(UserService.updateUser(id, updatedUser),HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        UserService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
