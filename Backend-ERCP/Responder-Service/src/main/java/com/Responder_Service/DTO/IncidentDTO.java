package com.Responder_Service.DTO;

import com.Responder_Service.Enums.IncidentStatus;
import com.Responder_Service.Enums.IncidentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
