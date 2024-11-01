package com.Emergency_Response_Management.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
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
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer responderId;
    private String name;
    private String currentLocation;
    private String status;
    private String role;
    private LocalDateTime lastUpdate;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = true)
    @JsonIgnoreProperties({"responders","victims"})
    private Location location;

    @OneToMany(mappedBy = "assignedResponder")
    @JsonIgnore
    private List<Incident> incidents = new ArrayList<>();
    // Other fields, getters, setters, and constructors
}
