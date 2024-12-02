package com.Emergency_Response_Management.Repository;

//import com.Emergency_Response_Management.Model.Dispatcher;
import com.Emergency_Response_Management.Model.Incident;
import com.Emergency_Response_Management.Model.Responder;
import com.Emergency_Response_Management.Model.Victim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Integer> {
    List<Incident> findByStatus(ResponseStatus status);
//    List<Incident> findByVictim(Victim victim);
//    List<Incident> findByAssignedResponders(Responder responder);
//    List<Incident> findByManagedBy(Dispatcher dispatcher);
}

