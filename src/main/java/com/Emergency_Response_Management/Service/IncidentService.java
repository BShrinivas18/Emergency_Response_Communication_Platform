package com.Emergency_Response_Management.Service;

import com.Emergency_Response_Management.Exception.GeneralException;
import com.Emergency_Response_Management.Model.Incident;
import com.Emergency_Response_Management.Model.Responder;
import com.Emergency_Response_Management.Repository.IncidentRepository;
import com.Emergency_Response_Management.Repository.ResponderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IncidentService {

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private ResponderRepository responderRepository;

    public Incident createIncident(Incident incident) {
        return incidentRepository.save(incident);
    }

    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    public Optional<Incident> getIncidentById(Integer id) {
        return incidentRepository.findById(id);
    }


    public List<Incident> getIncidentsByStatus(String status) {
        return incidentRepository.findByStatus(status);
    }

    public Incident assignResponder(Integer incidentId, Integer responderId) {
        Incident incident = getIncidentById(incidentId).orElseThrow(()->new GeneralException("Incident Not Found"));
        Responder responder = responderRepository.findById(responderId)
                .orElseThrow(() -> new RuntimeException("Responder not found"));

        incident.setAssignedResponder(responder);
        return incidentRepository.save(incident);
    }

    public Incident updateStatus(Integer id, String status) {
        Incident incident = getIncidentById(id).orElseThrow(()->new GeneralException("Incident Not Found"));
        incident.setStatus(status);
        return incidentRepository.save(incident);
    }

    public Incident updateIncident(Integer id, Incident incident) {
        Incident existingIncident = getIncidentById(id).orElseThrow(()->new GeneralException("Incident Not Found"));
        existingIncident.setLocation(incident.getLocation());
        existingIncident.setSeverity(incident.getSeverity());
        existingIncident.setType(incident.getType());
        existingIncident.setStatus(incident.getStatus());
        return incidentRepository.save(existingIncident);
    }

    public void deleteIncident(Integer id) {
        incidentRepository.deleteById(id);
    }
}
