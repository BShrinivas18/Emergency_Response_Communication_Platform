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
@Table(name = "logs")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer logId;
    private String statusUpdate;
    private LocalDateTime timestamp;
    private Integer updatedBy;

//    @ManyToOne
//    @JoinColumn(name = "incident_id", nullable =true)
//    private Incident incident;


}
