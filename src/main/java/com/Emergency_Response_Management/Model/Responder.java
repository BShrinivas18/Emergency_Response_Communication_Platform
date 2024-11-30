package com.Emergency_Response_Management.Model;

import com.Emergency_Response_Management.Enums.ResponderStatus;
import com.Emergency_Response_Management.Enums.ResponderType;
//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "responders")
public class Responder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer responderId;
    private String name;
    private String StationAddress;
    private ResponderStatus status;

    private ResponderType type;
    private LocalDateTime lastUpdate;

    @ManyToOne
    @JoinColumn(name = "responderLocation_id", nullable = true)
    @JsonIgnoreProperties({"responders","victims"})
    private Location responderLocation;

    @ManyToOne
    @JoinColumn(name = "incident_id")
    @JsonIgnoreProperties("assignedResponders")
    private Incident incident;

//    @OneToMany(mappedBy = "assignedResponder")//One Responder handles many Incidents
//    @JsonIgnore
//    private List<Incident> incidents = new ArrayList<>();
    // Other fields, getters, setters, and constructors
}
