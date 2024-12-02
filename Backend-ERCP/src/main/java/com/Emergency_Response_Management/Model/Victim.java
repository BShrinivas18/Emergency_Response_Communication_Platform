package com.Emergency_Response_Management.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "victims")
public class Victim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer victimId;
    private String name;
    private String contactInfo;

    @ManyToOne
    @JoinColumn(name = "location_id")// Ensure the column name matches your DB
    @JsonIgnoreProperties({"victims", "responders"})
    private Location location;

    @OneToMany(mappedBy = "victim")//One Victim can be involved in many Incidents
    @JsonIgnore
     private List<Incident> incidents = new ArrayList<>();
}
