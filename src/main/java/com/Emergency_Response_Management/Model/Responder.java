package com.Emergency_Response_Management.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Integer responderId;
    private String name;
    private String currentLocation;
    private String status;
    private String role;
    private LocalDateTime lastUpdate;
//    @OneToMany(mappedBy = "assignedResponder")
//    private List<Incident> assignedIncidents = new ArrayList<>();
//    @ManyToOne
//    @JoinColumn(name = "location_id")
//    private Location location;

}
