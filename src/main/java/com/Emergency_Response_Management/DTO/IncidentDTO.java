package com.Emergency_Response_Management.DTO;

import com.Emergency_Response_Management.Enums.IncidentSeverity;
import com.Emergency_Response_Management.Enums.IncidentStatus;
import com.Emergency_Response_Management.Enums.IncidentType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class IncidentDTO {
    private Integer incidentId;
    private String location;
    private IncidentSeverity severity;
    private IncidentType type;
    private LocalDateTime timestamp;
    private IncidentStatus status;
    private Integer victimId;
    private Integer responderId;
    private Integer dispatcherId;
    private List<Integer> logIds;
}
