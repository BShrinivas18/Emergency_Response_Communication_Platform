package com.Emergency_Response_Management.DTO;

import com.Emergency_Response_Management.Enums.ResponderStatus;
import com.Emergency_Response_Management.Enums.ResponderType;
import com.fasterxml.jackson.annotation.JsonFormat;
//import jakarta.persistence.EnumType;
//import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;
//import java.util.List;

@Data
public class ResponderDTO {
    private Integer responderId;
    private String name;
    private String currentLocation;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private ResponderStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private ResponderType type;
    private LocalDateTime lastUpdate;
    private Integer locationId;

    private Integer incidentId;
//    private List<Integer> incidentIds;  // can be fetched when required separately
}
