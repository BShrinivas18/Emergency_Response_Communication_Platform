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
@Table(name = "incidents")
public class Incident {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer incidentId;
    private String location;
    private String severity;
    private String type;
    private LocalDateTime timestamp;
    private String status;


//    @ManyToOne
//    @JoinColumn(name = "victim_id", nullable = true)
//    private Victim victim;
//
//    @ManyToOne
//    @JoinColumn(name = "responder_id", nullable = true)
//    private Responder assignedResponder;
//
//    @ManyToOne
//    @JoinColumn(name = "dispatcher_id", nullable = true)
//    private Dispatcher managedBy;
//
//    @OneToMany(mappedBy = "incident", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Log> logs = new ArrayList<>();
}
