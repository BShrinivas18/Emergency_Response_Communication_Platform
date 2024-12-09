package com.Log_Service.Repository;

import com.Log_Service.Model.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface LogRepository extends JpaRepository<Log,Integer> {
    List<Log> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    List<Log> findByIncidentId(Integer incidentId);
}
