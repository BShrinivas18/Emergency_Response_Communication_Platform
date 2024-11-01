package com.Emergency_Response_Management.Model;

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

    @ManyToOne(fetch = FetchType.LAZY) // Change to ManyToOne to represent relation
    @JoinColumn(name = "location_id", nullable = true) // Ensure the column name matches your DB
    private Location location;

    @OneToMany(mappedBy = "victim", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Incident> incidents = new ArrayList<>();
}
