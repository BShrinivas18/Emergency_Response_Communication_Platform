package com.Emergency_Response_Management.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "incidents")
public class Incident {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer incidentId;
    private String location;
    private String severity;
    private String type;
    private LocalDateTime timestamp;
    private String status;


    @ManyToOne
    @JoinColumn(name = "victim_id", nullable = true)
    @JsonIgnoreProperties({"incidents"}) // breaks the circular reference
    private Victim victim;

    @ManyToOne
    @JoinColumn(name = "responder_id", nullable = true)
    @JsonIgnoreProperties("incidents") // breaks the circular reference
    private Responder assignedResponder;

    @ManyToOne
    @JoinColumn(name = "dispatcher_id", nullable = true)
    @JsonIgnoreProperties("managedIncidents")
    private Dispatcher managedBy;

    @OneToMany(mappedBy = "incident", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"incidents"})
    private List<Log> logs = new ArrayList<>();
}
