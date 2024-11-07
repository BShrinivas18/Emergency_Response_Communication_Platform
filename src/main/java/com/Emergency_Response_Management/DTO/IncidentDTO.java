package com.Emergency_Response_Management.DTO;

import com.Emergency_Response_Management.Enums.IncidentSeverity;
import com.Emergency_Response_Management.Enums.IncidentStatus;
import com.Emergency_Response_Management.Enums.IncidentType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class IncidentDTO {
    private Integer incidentId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private IncidentSeverity severity;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private IncidentType type;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private IncidentStatus status;

    private LocalDateTime timestamp;

    // Incident location
    private Integer incidentLocationId;

    // Victim information
    private Integer victimId;
    private String victimName;
    private String victimContact;
    private Integer victimLocationId;

    // List of responder IDs assigned to this incident
    private List<Integer> responderIds;

//    private Integer dispatcherId;
//    private List<Integer> logIds; // removed because it can be fetched separately when needed
}
