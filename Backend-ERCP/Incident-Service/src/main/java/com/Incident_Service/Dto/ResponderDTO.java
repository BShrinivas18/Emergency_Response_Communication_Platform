package com.Incident_Service.Dto;

import com.Incident_Service.Enums.ResponderStatus;
import com.Incident_Service.Enums.ResponderType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponderDTO {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private int responderId;
   private String name;
   private String currentLocation;
   private ResponderStatus status;
   private ResponderType type;
   private LocalDateTime lastUpdate;
//   private Integer incidentId;
}
