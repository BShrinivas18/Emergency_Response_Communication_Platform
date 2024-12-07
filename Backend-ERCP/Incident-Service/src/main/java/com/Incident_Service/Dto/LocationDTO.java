package com.Incident_Service.Dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int locationId;
    private double latitude;
    private double longitude;
    private String address;
}
