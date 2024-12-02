package com.Emergency_Response_Management.Model;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Value;

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

    @Min(value = -90, message = "Latitude must be between -90 and 90")
    @Max(value = 90, message = "Latitude must be between -90 and 90")
    private Double latitude;

    @Min(value = -180, message = "Longitude must be between -180 and 180")
    @Max(value = 180, message = "Longitude must be between -180 and 180")
    private Double longitude;
    private String address;

    @OneToMany(mappedBy = "location")
    @JsonIgnore
    private List<Incident> incidentList = new ArrayList<>();

    @OneToMany(mappedBy = "responderLocation")//One Location can have many Responders
    @JsonIgnore // Helps prevent circular reference during serialization
    private List<Responder> responders = new ArrayList<>();

    @OneToMany(mappedBy = "location")//One Location can have many Victims
    @JsonIgnore
    private List<Victim> victims = new ArrayList<>(); // Added for Victim reference
}
