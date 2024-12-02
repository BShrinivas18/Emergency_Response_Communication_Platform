package com.Emergency_Response_Management.Repository;

import com.Emergency_Response_Management.Model.Incident;
import com.Emergency_Response_Management.Model.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, Integer> {
    List<Log> findByIncident(Incident incident);
    List<Log> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    List<Log> findByUpdatedBy(Integer userId);
    //List<Log> findByIncidentId(int incidentid);
}

