package com.Emergency_Response_Management.Service;

import com.Emergency_Response_Management.DTO.LogDTO;
import com.Emergency_Response_Management.DTO.VictimDTO;
import com.Emergency_Response_Management.Exception.GeneralException;
import com.Emergency_Response_Management.Model.Incident;
import com.Emergency_Response_Management.Model.Location;
import com.Emergency_Response_Management.Model.Victim;
import com.Emergency_Response_Management.Repository.IncidentRepository;
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

    @Autowired
    private IncidentRepository incidentRepository;

    private VictimDTO convertToDTO(Victim victim) {
        VictimDTO dto = new VictimDTO();
        dto.setVictimId(victim.getVictimId());
        dto.setName(victim.getName());
        dto.setContactInfo(victim.getContactInfo());

        if (victim.getLocation() != null) {
            dto.setLocationId(victim.getLocation().getLocationId());
        }

        dto.setIncidentIds(victim.getIncidents().stream().map(Incident::getIncidentId).toList());
        return dto;
    }

    private Victim convertToEntity(VictimDTO dto) {
        Victim victim = new Victim();
        victim.setVictimId(dto.getVictimId());
        victim.setName(dto.getName());
        victim.setContactInfo(dto.getContactInfo());

        if (dto.getLocationId() != null) {
            Location location = locationRepository.findById(dto.getLocationId())
                    .orElseThrow(() -> new RuntimeException("Location not found"));
            victim.setLocation(location);
        }

        List<Incident> incidents = incidentRepository.findAllById(dto.getIncidentIds());
        victim.setIncidents(incidents);
        return victim;
    }

    public VictimDTO createVictim(VictimDTO victimDTO) {
        Victim victim = convertToEntity(victimDTO);

        // Fetch location based on locationId in VictimDTO
        if (victimDTO.getLocationId() != null) {
            Location location = locationRepository.findById(victimDTO.getLocationId())
                    .orElseThrow(() -> new RuntimeException("Location not found"));
            victim.setLocation(location);
        }

        Victim savedVictim = victimRepository.save(victim);
        return convertToDTO(savedVictim);
    }

    public List<VictimDTO> getAllVictims() {
        return victimRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    public VictimDTO getVictimById(Integer id) {

        return convertToDTO(victimRepository
                .findById(id)
                .orElseThrow(()-> new GeneralException("Victim not found")));

    }

    public List<VictimDTO> getVictimsByLocation(Integer locationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));
        return victimRepository.findByLocation(location).stream().map(this::convertToDTO).toList();
    }

    public List<VictimDTO> getVictimsByName(String name) {

        return victimRepository.findByName(name).stream().map(this::convertToDTO).toList();
    }


    public VictimDTO updateVictim(Integer id, VictimDTO victim) {
        Victim existingVictim = victimRepository.findById(id).orElseThrow(()-> new GeneralException("victim not found"));
        existingVictim.setName(victim.getName());
        existingVictim.setContactInfo(victim.getContactInfo());
        existingVictim.setLocation(
                locationRepository.findById(victim.getLocationId()).orElseThrow(()-> new GeneralException("Location not found"))
        );


        if (victim.getIncidentIds() != null) {
            List<Incident> incidents = incidentRepository.findAllById(victim.getIncidentIds());
            existingVictim.setIncidents(incidents);
        }
        return convertToDTO(victimRepository.save(existingVictim));
    }

    public void deleteVictim(Integer id) {
        if(!victimRepository.existsById(id)){
            throw new GeneralException("Cannot find victim intended to be deleted");
        }
        victimRepository.deleteById(id);
    }
}
