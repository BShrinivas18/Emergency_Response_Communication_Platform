package com.Responder_Service.Repository;
import com.Responder_Service.Model.ResponderEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ResponderEventRepository extends JpaRepository<ResponderEvent, Long> {
    List<ResponderEvent> findByResponderIdOrderByIdAsc(Integer responderId);
}
