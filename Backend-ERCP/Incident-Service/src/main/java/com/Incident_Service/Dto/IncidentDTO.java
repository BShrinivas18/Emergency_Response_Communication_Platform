package com.Incident_Service.Dto;

import com.Incident_Service.Enums.IncidentStatus;
import com.Incident_Service.Enums.IncidentType;
import com.Incident_Service.Model.Incident;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncidentDTO {
    private int incidentId;
    @NotNull(message = "Incident type cannot be null")
    private IncidentType type;
    @NotNull(message = "Incident timestamp cannot be null")
    private LocalDateTime timestamp;
    @NotNull(message = "Incident status cannot be null")
    private IncidentStatus status;

    //Location Details
    //This is also victim location ID
    private LocationDTO incidentLocation; // This is the location ID in the Location Service

    // Victim details
    private Integer victimId;

    @Size(max = 100, message = "Victim name cannot be more than 100 characters")
    private String victimName;

    @Pattern(regexp = "^\\[1-9]\\d{1,14}$", message = "Victim contact number must be in the format of [1-9]XXXXXXXXXX")
    private String victimContact;

    private LocationDTO victimLocation;
    // Responder details
    private List<Integer> responderIds = new ArrayList<>();
}
