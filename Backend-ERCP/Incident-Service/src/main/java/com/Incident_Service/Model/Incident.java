package com.Incident_Service.Model;

import com.Incident_Service.Dto.LocationDTO;
import com.Incident_Service.Enums.IncidentStatus;
import com.Incident_Service.Enums.IncidentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="incidents")
public class Incident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int incidentId;
    private int locationId;
    private IncidentType type;
    private LocalDateTime timestamp;
    private IncidentStatus status;
    private Integer victimId;


}
