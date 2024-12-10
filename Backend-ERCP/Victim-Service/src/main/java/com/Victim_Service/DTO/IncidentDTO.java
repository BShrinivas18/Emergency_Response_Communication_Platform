package com.Victim_Service.DTO;

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
public class IncidentDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int incidentId;
    private int locationId;
    private String type;
    private LocalDateTime timestamp;
    private String status;
}
