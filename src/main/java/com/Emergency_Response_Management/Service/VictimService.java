package com.Emergency_Response_Management.Service;

import com.Emergency_Response_Management.Model.Victim;
import com.Emergency_Response_Management.Repository.VictimRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VictimService {

    @Autowired
    private VictimRepository victimRepository;

    public Victim createVictim(Victim victim) {
        return victimRepository.save(victim);
    }

    public List<Victim> getAllVictims() {
        return victimRepository.findAll();
    }

    public Optional<Victim> getVictimById(Integer id) {
        return victimRepository.findById(id);
    }

    public Victim updateVictim(Integer id, Victim updatedVictim) {
        updatedVictim.setVictimId(id);
        return victimRepository.save(updatedVictim);
    }

    public void deleteVictim(Integer id) {
        victimRepository.deleteById(id);
    }
}
