package com.Location_Service.DTO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "victims")
public class VictimDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int victimId;
    private String name;
    private int locationId;
    private String contactInfo;
}
