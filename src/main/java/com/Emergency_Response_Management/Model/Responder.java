package com.Emergency_Response_Management.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private String currentLocation;
    private String status;
    private String role;
    private LocalDateTime lastUpdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = true)
    private Location location;

    // Other fields, getters, setters, and constructors
}
