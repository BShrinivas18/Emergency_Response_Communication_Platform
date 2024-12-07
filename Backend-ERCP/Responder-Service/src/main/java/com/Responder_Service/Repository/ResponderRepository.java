package com.Responder_Service.Repository;

import com.Responder_Service.DTO.ResponderDTO;
import com.Responder_Service.Enums.ResponderStatus;
import com.Responder_Service.Enums.ResponderType;
import com.Responder_Service.Model.Responder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResponderRepository extends JpaRepository<Responder,Integer> {
    public List<Responder> findByStatus(ResponderStatus status);
    public List<Responder> findByType(ResponderType type);
    public List<Responder> findByStatusAndType(ResponderStatus status, ResponderType type);
    public List<Responder> findByIncidentId(Integer incidentId);

}
