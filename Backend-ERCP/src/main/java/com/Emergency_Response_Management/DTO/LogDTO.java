package com.Emergency_Response_Management.DTO;

import jakarta.persistence.Entity;
import lombok.Data;

import java.time.LocalDateTime;

@Data

public class LogDTO {
    private Integer logId;
    private String statusUpdate;
    private LocalDateTime timestamp;
    //private Integer updatedBy;
    private Integer incidentId;
}
