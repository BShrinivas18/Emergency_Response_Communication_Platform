package com.Emergency_Response_Management.Service;

import com.Emergency_Response_Management.Model.Incident;
import com.Emergency_Response_Management.Repository.IncidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IncidentService {

    @Autowired
    private IncidentRepository incidentRepository;

    public Incident createIncident(Incident incident) {
        return incidentRepository.save(incident);
    }

    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    public Optional<Incident> getIncidentById(Integer id) {
        return incidentRepository.findById(id);
    }

    public Incident updateIncident(Integer id, Incident updatedIncident) {
        updatedIncident.setIncidentId(id);
        return incidentRepository.save(updatedIncident);
    }

    public void deleteIncident(Integer id) {
        incidentRepository.deleteById(id);
    }
}
