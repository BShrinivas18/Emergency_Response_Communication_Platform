package com.Emergency_Response_Management.DTO;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ResponderDTO {
    private Integer responderId;
    private String name;
    private String currentLocation;
    private String status;
    private String role;
    private LocalDateTime lastUpdate;
    private Integer locationId;
    private List<Integer> incidentIds;
}
