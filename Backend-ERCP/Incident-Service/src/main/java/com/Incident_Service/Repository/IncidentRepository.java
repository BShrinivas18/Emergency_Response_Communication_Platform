package com.Incident_Service.Repository;

import com.Incident_Service.Enums.IncidentStatus;
import com.Incident_Service.Model.Incident;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidentRepository extends JpaRepository<Incident,Integer> {
    List<Incident> findByStatus(IncidentStatus status);
    List<Incident> findByVictimId(int victimId);
}
