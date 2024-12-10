package com.Responder_Service.DTO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "locations")
public class LocationDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int locationId;
    private double latitude;
    private double longitude;
    private String address;

}
