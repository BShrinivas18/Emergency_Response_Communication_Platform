package com.Emergency_Response_Management.Model;

import com.Emergency_Response_Management.Enums.IncidentSeverity;
import com.Emergency_Response_Management.Enums.IncidentStatus;
import com.Emergency_Response_Management.Enums.IncidentType;
import com.fasterxml.jackson.annotation.*;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer incidentId;

    @ManyToOne
    @JoinColumn(name = "location_id")
    @JsonIgnoreProperties({"victims", "responders"})
    private Location location;
    @Enumerated(value = EnumType.STRING)
    private IncidentSeverity severity;
    @Enumerated(value = EnumType.STRING)
    private IncidentType type;
    private LocalDateTime timestamp;
    private IncidentStatus status;


    @ManyToOne
    @JoinColumn(name = "victim_id", nullable = true)
    @JsonIgnoreProperties({"incidents"}) // breaks the circular reference
    private Victim victim;

//    @ManyToOne
//    @JoinColumn(name = "responder_id", nullable = true)
//    @JsonIgnoreProperties("incidents") // breaks the circular reference
//    private Responder assignedResponder;

    @OneToMany(mappedBy = "incident", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("incident")
    private List<Responder> assignedResponders = new ArrayList<>();

//    @ManyToOne
//    @JoinColumn(name = "dispatcher_id", nullable = true)
//    @JsonIgnoreProperties("managedIncidents")
//    private Dispatcher managedBy;

    @OneToMany(mappedBy = "incident", cascade = CascadeType.ALL, orphanRemoval = true)//One Incident has many Logs
    @JsonIgnoreProperties({"incidents"})
    private List<Log> logs = new ArrayList<>();
}
