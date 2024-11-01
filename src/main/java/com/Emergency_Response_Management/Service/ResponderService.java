package com.Emergency_Response_Management.Service;

import com.Emergency_Response_Management.Exception.GeneralException;
import com.Emergency_Response_Management.Model.Location;
import com.Emergency_Response_Management.Model.Responder;
import com.Emergency_Response_Management.Repository.LocationRepository;
import com.Emergency_Response_Management.Repository.ResponderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ResponderService {

    @Autowired
    private ResponderRepository responderRepository;

    @Autowired
    private LocationRepository locationRepository;

    public Responder createResponder(Responder responder) {
        return responderRepository.save(responder);
    }

    public List<Responder> getAllResponders() {
        return responderRepository.findAll();
    }

    public Optional<Responder> getResponderById(Integer id) {
        return responderRepository.findById(id);
    }

    public List<Responder> getRespondersByStatus(String status) {
        return responderRepository.findByStatus(status);
    }


    public List<Responder> getRespondersByRole(String role) {
        return responderRepository.findByRole(role);
    }

    public List<Responder> getRespondersByLocation(Integer locationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));
        return responderRepository.findByLocation(location);
    }

    public Responder updateResponder(Integer id, Responder responder) {
        Responder existingResponder = getResponderById(id).orElseThrow(()->new GeneralException("Responder Not Found"));
        existingResponder.setName(responder.getName());
        existingResponder.setCurrentLocation(responder.getCurrentLocation());
        existingResponder.setStatus(responder.getStatus());
        existingResponder.setRole(responder.getRole());
        existingResponder.setLastUpdate(LocalDateTime.now());
        return responderRepository.save(existingResponder);
    }

    public Responder updateStatus(Integer id, String status) {
        Responder responder = getResponderById(id).orElseThrow(()->new GeneralException("Responder Not Found"));
        responder.setStatus(status);
        responder.setLastUpdate(LocalDateTime.now());
        return responderRepository.save(responder);
    }

    public void deleteResponder(Integer id) {
        responderRepository.deleteById(id);
    }
}
