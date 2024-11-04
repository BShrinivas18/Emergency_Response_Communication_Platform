package com.Emergency_Response_Management.DTO;

import com.Emergency_Response_Management.Enums.ResponderStatus;
import com.Emergency_Response_Management.Enums.ResponderType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ResponderDTO {
    private Integer responderId;
    private String name;
    private String currentLocation;
    private ResponderStatus status;
    private ResponderType type;
    private LocalDateTime lastUpdate;
    private Integer locationId;
    private List<Integer> incidentIds;
}
