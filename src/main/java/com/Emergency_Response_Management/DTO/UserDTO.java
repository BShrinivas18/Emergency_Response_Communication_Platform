package com.Emergency_Response_Management.DTO;

import com.Emergency_Response_Management.Enums.UserRole;
import lombok.Data;

@Data
public class UserDTO {
    private Integer userId;
    private String name;
    private String contactInfo;
    private UserRole role;
}
