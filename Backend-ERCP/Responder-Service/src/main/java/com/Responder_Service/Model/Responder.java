package com.Responder_Service.Model;

import com.Responder_Service.DTO.LocationDTO;
import com.Responder_Service.Enums.ResponderStatus;
import com.Responder_Service.Enums.ResponderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.stream.Location;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "responders")
public class Responder {

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
//    Do it later additional functionality
//    private List<Integer> pastIncidentIds = new ArrayList<>();


}
