package com.Emergency_Response_Management.Service;

import com.Emergency_Response_Management.Model.Responder;
import com.Emergency_Response_Management.Repository.ResponderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResponderService {

    @Autowired
    private ResponderRepository responderRepository;

    public Responder createResponder(Responder responder) {
        return responderRepository.save(responder);
    }

    public List<Responder> getAllResponders() {
        return responderRepository.findAll();
    }

    public Optional<Responder> getResponderById(Integer id) {
        return responderRepository.findById(id);
    }

    public Responder updateResponder(Integer id, Responder updatedResponder) {
        updatedResponder.setResponderId(id);
        return responderRepository.save(updatedResponder);
    }

    public void deleteResponder(Integer id) {
        responderRepository.deleteById(id);
    }
}
