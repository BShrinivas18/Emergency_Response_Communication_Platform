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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer incidentId;
    private String location;
    private String severity;
    private String type;
    private LocalDateTime timestamp;
    private String status;

//    @ManyToOne
//    @JoinColumn(name = "victim_id")
//    private Victim victim;
//    @ManyToOne
//    @JoinColumn(name = "responder_id")
//    private Responder assignedResponder;
//    @ManyToOne
//    @JoinColumn(name = "dispatcher_id")
//    private Dispatcher managedBy;
//    @OneToMany(mappedBy = "incident")
//    private List<Log> logs = new ArrayList<>();
}
