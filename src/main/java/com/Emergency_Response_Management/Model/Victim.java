package com.Emergency_Response_Management.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer victimId;
    private String name;
    private String contactInfo;

    @ManyToOne // Change to ManyToOne to represent relation
    @JoinColumn(name = "location_id")// Ensure the column name matches your DB
    @JsonIgnoreProperties({"victims", "responders"})
    private Location location;

    @OneToMany(mappedBy = "victim")
    @JsonIgnore
     private List<Incident> incidents = new ArrayList<>();
}
