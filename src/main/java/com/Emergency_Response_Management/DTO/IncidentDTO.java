package com.Emergency_Response_Management.DTO;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class IncidentDTO {
    private Integer incidentId;
    private String location;
    private String severity;
    private String type;
    private LocalDateTime timestamp;
    private String status;
    private Integer victimId;
    private Integer responderId;
    private Integer dispatcherId;
    private List<Integer> logIds;
}
