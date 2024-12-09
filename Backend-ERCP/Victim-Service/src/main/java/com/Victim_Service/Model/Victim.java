package com.Victim_Service.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="victims")
public class Victim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer victimId;
    private String name;
    private String contactInfo;
    private Integer locationId;
}
