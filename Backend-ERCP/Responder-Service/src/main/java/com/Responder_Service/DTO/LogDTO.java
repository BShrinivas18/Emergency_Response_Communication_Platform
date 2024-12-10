package com.Responder_Service.DTO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "logs")
public class LogDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int logId;
    private String statusUpdate;
    private LocalDateTime timestamp;
//    private int updatedBy;
    private int incidentId;
}
