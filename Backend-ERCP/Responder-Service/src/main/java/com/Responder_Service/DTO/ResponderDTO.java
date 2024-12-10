package com.Responder_Service.DTO;

import com.Responder_Service.Enums.ResponderStatus;
import com.Responder_Service.Enums.ResponderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "responders")
public class ResponderDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int responderId;
    private String name;
    private String stationLocation;
    private ResponderStatus status;
    private ResponderType type;
    private LocalDateTime lastUpdate;
    private Integer locationId;
    private Integer incidentId;
//    Do irt later additional functionality
//    private List<Integer> pastIncidentIds ;
}
