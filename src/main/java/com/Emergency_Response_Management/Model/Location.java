package com.Emergency_Response_Management.Model;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer locationId;
    private Float latitude;
    private Float longitude;
    private String address;

    @OneToMany(mappedBy = "location")
    @JsonIgnore
    private List<Incident> incidentList = new ArrayList<>();

    @OneToMany(mappedBy = "location")//One Location can have many Responders
    @JsonIgnore // Helps prevent circular reference during serialization
    private List<Responder> responders = new ArrayList<>();

    @OneToMany(mappedBy = "location")//One Location can have many Victims
    @JsonIgnore
    private List<Victim> victims = new ArrayList<>(); // Added for Victim reference
}
