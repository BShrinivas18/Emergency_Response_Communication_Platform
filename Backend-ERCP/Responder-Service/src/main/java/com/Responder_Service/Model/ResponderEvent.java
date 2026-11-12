package com.Responder_Service.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.Instant;

/** Local append-only history, committed with the responder update. */
@Entity
@Table(name = "responder_events")
@Getter
@NoArgsConstructor
public class ResponderEvent {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, updatable = false) private Integer responderId;
    @Column(updatable = false) private Integer incidentId;
    @Column(nullable = false, updatable = false) private String eventType;
    @Column(nullable = false, updatable = false) private String actor;
    @Column(updatable = false) private String previousStatus;
    @Column(updatable = false) private String newStatus;
    @Column(nullable = false, updatable = false) private Instant occurredAt;
    public ResponderEvent(Integer responderId, Integer incidentId, String eventType, String previousStatus, String newStatus, String actor) {
        this.responderId = responderId; this.incidentId = incidentId; this.eventType = eventType;
        this.previousStatus = previousStatus; this.newStatus = newStatus; this.occurredAt = Instant.now();
        this.actor = actor;
    }
}
