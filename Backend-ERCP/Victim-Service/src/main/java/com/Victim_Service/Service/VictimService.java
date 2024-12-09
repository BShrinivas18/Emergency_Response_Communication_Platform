package com.Victim_Service.Service;

import com.Victim_Service.DTO.IncidentDTO;
import com.Victim_Service.DTO.LocationDTO;
import com.Victim_Service.DTO.VictimDTO;
import com.Victim_Service.FeignClient.IncidentServiceClient;
import com.Victim_Service.FeignClient.LocationServiceClient;
import com.Victim_Service.Model.Victim;
import com.Victim_Service.Repository.VictimRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VictimService {

    @Autowired
    private VictimRepository victimRepository;

    @Autowired
    private LocationServiceClient locationServiceClient;

    @Autowired
    private IncidentServiceClient incidentServiceClient;

    @Transactional
    public VictimDTO createVictim(VictimDTO victimDTO) {
//        System.out.println("victim name: "+victimDTO.getName());
        Victim victim = new Victim();
        victim.setName(victimDTO.getName());
        victim.setContactInfo(victimDTO.getContactInfo());
        victim.setLocationId(victimDTO.getLocationId());

        Victim savedVictim = victimRepository.save(victim);
//        System.out.println("victim : " + savedVictim);
        return convertToDTO(savedVictim);
    }

    public List<VictimDTO> getAllVictims() {
        return victimRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public VictimDTO getVictimById(Integer id) {
//        System.out.println("HEy its inside get victim by id method");
//        System.out.println("victim: "+victimRepository.findById(id).map(this::convertToDTO));
        return victimRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Victim not found with id: " + id));
    }

    public List<VictimDTO> getVictimsByName(String name) {
        return victimRepository.findByName(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @Transactional
    public VictimDTO updateVictim(Integer id, VictimDTO victimDTO) {
        Victim existingVictim = victimRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Victim not found with id: " + id));

        existingVictim.setName(victimDTO.getName());
        existingVictim.setContactInfo(victimDTO.getContactInfo());

        Victim updatedVictim = victimRepository.save(existingVictim);
        return convertToDTO(updatedVictim);
    }

    @Transactional
    public void deleteVictim(Integer id) {
        if (!victimRepository.existsById(id)) {
            throw new RuntimeException("Victim not found with id: " + id);
        }
        victimRepository.deleteById(id);
    }

    public VictimDTO convertToDTO(Victim victim) {
        VictimDTO dto = new VictimDTO();
        dto.setVictimId(victim.getVictimId());
        dto.setName(victim.getName());
        dto.setContactInfo(victim.getContactInfo());
        dto.setLocationId(victim.getLocationId());
//        try {
//            // Fetch location details
//            LocationDTO location = locationServiceClient.getLocationById(victim.getVictimId());
//            dto.setLocationId(location.getLocationId());
//        } catch (Exception e) {
//            // Handle case where location might not exist
//            dto.setLocationId(null);
//        }

        try {
             //Fetch incident details
            List<IncidentDTO> incidents = incidentServiceClient.getVictimById(victim.getVictimId());
            dto.setIncidentIds(incidents.stream()
                    .map(IncidentDTO::getIncidentId)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            // Handle case where incidents might not exist
            dto.setIncidentIds(new ArrayList<>());
        }

        return dto;
    }
}
