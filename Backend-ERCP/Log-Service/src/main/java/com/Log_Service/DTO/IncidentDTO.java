package com.Log_Service.DTO;

import com.Log_Service.Enums.IncidentStatus;
import com.Log_Service.Enums.IncidentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "incidents")
public class IncidentDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int incidentId;
    private int locationId;
    private IncidentType type;
    private LocalDateTime timestamp;
    private IncidentStatus status;
}
