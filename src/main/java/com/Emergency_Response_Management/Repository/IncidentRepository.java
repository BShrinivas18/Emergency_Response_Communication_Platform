package com.Emergency_Response_Management.Repository;

import com.Emergency_Response_Management.Model.Incident;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidentRepository extends JpaRepository<Incident,Integer> {
}
