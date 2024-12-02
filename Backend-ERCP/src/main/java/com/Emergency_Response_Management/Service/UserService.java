package com.Emergency_Response_Management.Service;
import com.Emergency_Response_Management.DTO.UserDTO;
import com.Emergency_Response_Management.Model.User;

import com.Emergency_Response_Management.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDTO createUser(UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Integer id) {
        User User = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDTO(User);
    }

    public UserDTO updateUser(Integer id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setName(userDTO.getName());
        existingUser.setContactInfo(userDTO.getContactInfo());
        existingUser.setRole(userDTO.getRole());
        User updatedUser = userRepository.save(existingUser);
        return convertToDTO(updatedUser);
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    private UserDTO convertToDTO(User User) {
        UserDTO dto = new UserDTO();
        dto.setUserId(User.getUserId());
        dto.setName(User.getName());
        dto.setContactInfo(User.getContactInfo());
        dto.setRole(User.getRole());
        return dto;
    }

    private User convertToEntity(UserDTO dto) {
        User User = new User();
        User.setUserId(dto.getUserId());
        User.setName(dto.getName());
        User.setContactInfo(dto.getContactInfo());
        User.setRole(dto.getRole());
        return User;
    }
}
