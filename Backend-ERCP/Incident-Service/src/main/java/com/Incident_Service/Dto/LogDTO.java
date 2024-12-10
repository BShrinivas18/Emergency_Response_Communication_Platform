package com.Incident_Service.Dto;

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
public class LogDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int logId;
    private String statusUpdate;
    private LocalDateTime timestamp;
    private Integer incidentId;
//    private int updatedBy;
}
