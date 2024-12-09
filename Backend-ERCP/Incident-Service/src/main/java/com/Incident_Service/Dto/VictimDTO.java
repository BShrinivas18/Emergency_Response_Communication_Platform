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
public class VictimDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int victimId;
    private String name;
    private String contactInfo;
    private int locationId;
}
