package com.Emergency_Response_Management.DTO;

import com.Emergency_Response_Management.Enums.UserRole;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data

public class UserDTO {
    private Integer userId;
    private String name;
    private String contactInfo;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private UserRole role;
}
