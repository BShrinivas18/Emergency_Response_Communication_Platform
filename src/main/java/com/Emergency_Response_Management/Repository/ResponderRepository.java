package com.Emergency_Response_Management.Repository;

import com.Emergency_Response_Management.Enums.ResponderStatus;
import com.Emergency_Response_Management.Enums.ResponderType;
import com.Emergency_Response_Management.Model.Location;
import com.Emergency_Response_Management.Model.Responder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResponderRepository extends JpaRepository<Responder, Integer> {
    List<Responder> findByStatus(ResponderStatus status);
    List<Responder> findByType(ResponderType type);
    List<Responder> findByLocation(Location location);

}

