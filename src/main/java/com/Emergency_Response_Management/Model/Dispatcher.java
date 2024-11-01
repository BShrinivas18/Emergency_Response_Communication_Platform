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
@Table(name = "dispatchers")
public class Dispatcher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dispatcherId;

    private String name;
    private String contactInfo;
    private String assignedRegion;

//    @OneToMany(mappedBy = "managedBy", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Incident> managedIncidents = new ArrayList<>();
}
