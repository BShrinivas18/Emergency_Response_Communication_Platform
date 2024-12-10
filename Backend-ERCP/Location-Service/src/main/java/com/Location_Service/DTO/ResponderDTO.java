package com.Location_Service.DTO;

import com.Location_Service.Enums.ResponderStatus;
import com.Location_Service.Enums.ResponderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

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
    private int locationId;
    private String currentLocation;
    private ResponderStatus status;
    private ResponderType type;
    private LocalDateTime lastUpdate;
}
