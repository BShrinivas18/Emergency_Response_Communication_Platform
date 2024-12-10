package com.Location_Service.DTO;

import com.Location_Service.Enums.IncidentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

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
    private String type;
    private LocalDateTime timestamp;
    private IncidentStatus status;
}
