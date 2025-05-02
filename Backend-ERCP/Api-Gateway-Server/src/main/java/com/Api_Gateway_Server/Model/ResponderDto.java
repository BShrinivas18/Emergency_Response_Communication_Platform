package com.Api_Gateway_Server.Model;

import com.Api_Gateway_Server.Model.Enums.ResponderStatus;
import com.Api_Gateway_Server.Model.Enums.ResponderType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponderDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int responderId;
    private String name;
    private String currentLocation;
    private ResponderStatus status;
    private ResponderType type;
    private LocalDateTime lastUpdate;
}

