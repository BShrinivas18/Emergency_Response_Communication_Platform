package com.Emergency_Response_Management.Service;

import com.Emergency_Response_Management.Model.Location;
import com.Emergency_Response_Management.Model.Victim;
import com.Emergency_Response_Management.Repository.LocationRepository;
import com.Emergency_Response_Management.Repository.VictimRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VictimService {

    @Autowired
    private VictimRepository victimRepository;

    @Autowired
    private LocationRepository locationRepository;

    public Victim createVictim(Victim victim) {
        return victimRepository.save(victim);
    }

    public List<Victim> getAllVictims() {
        return victimRepository.findAll();
    }

    public Victim getVictimById(Integer id) {
        return victimRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Victim not found"));
    }

    public List<Victim> getVictimsByLocation(Integer locationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));
        return victimRepository.findByLocation(location);
    }

    public List<Victim> getVictimsByName(String name) {
        return victimRepository.findByName(name);
    }


    public Victim updateVictim(Integer id, Victim victim) {
        Victim existingVictim = getVictimById(id);
        existingVictim.setName(victim.getName());
        existingVictim.setContactInfo(victim.getContactInfo());
        existingVictim.setLocation(victim.getLocation());
//        existingVictim.setMedicalCondition(victim.getMedicalCondition());
//        existingVictim.setStatus(victim.getStatus());
        return victimRepository.save(existingVictim);
    }

    public void deleteVictim(Integer id) {
        victimRepository.deleteById(id);
    }
}
