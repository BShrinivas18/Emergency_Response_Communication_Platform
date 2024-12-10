package com.Victim_Service.DTO;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VictimDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int victimId;
    private String name;
    private String contactInfo;
    private Integer locationId;
    private List<Integer> incidentIds;
}
