package com.Emergency_Response_Management.Service;

import com.Emergency_Response_Management.DTO.DispatcherDTO;
import com.Emergency_Response_Management.Exception.GeneralException;
import com.Emergency_Response_Management.Model.Dispatcher;
import com.Emergency_Response_Management.Model.Incident;
import com.Emergency_Response_Management.Repository.DispatcherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DispatcherService {

    @Autowired
    private DispatcherRepository dispatcherRepository;

    public DispatcherDTO createDispatcher(DispatcherDTO dispatcherDTO) {
        Dispatcher dispatcher = convertToEntity(dispatcherDTO);
        Dispatcher savedDispatcher = dispatcherRepository.save(dispatcher);
        return convertToDTO(savedDispatcher);
    }


    public List<DispatcherDTO> getAllDispatchers() {
        return dispatcherRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DispatcherDTO getDispatcherById(Integer id) {
        Dispatcher dispatcher = dispatcherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dispatcher not found"));
        return convertToDTO(dispatcher);
    }

    public DispatcherDTO updateDispatcher(Integer id, DispatcherDTO dispatcherDTO) {
        Dispatcher existingDispatcher = dispatcherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dispatcher not found"));
        existingDispatcher.setName(dispatcherDTO.getName());
        existingDispatcher.setContactInfo(dispatcherDTO.getContactInfo());
        existingDispatcher.setAssignedRegion(dispatcherDTO.getAssignedRegion());
        Dispatcher updatedDispatcher = dispatcherRepository.save(existingDispatcher);
        return convertToDTO(updatedDispatcher);
    }

    public List<Dispatcher> getDispatchersByRegion(String region) {
        return dispatcherRepository.findByAssignedRegion(region);
    }

    public void deleteDispatcher(Integer id) {
        dispatcherRepository.deleteById(id);
    }

    private DispatcherDTO convertToDTO(Dispatcher dispatcher) {
        DispatcherDTO dto = new DispatcherDTO();
        dto.setDispatcherId(dispatcher.getDispatcherId());
        dto.setName(dispatcher.getName());
        dto.setContactInfo(dispatcher.getContactInfo());
        dto.setAssignedRegion(dispatcher.getAssignedRegion());
        dto.setManagedIncidentIds(dispatcher.getManagedIncidents().stream()
                .map(Incident::getIncidentId)
                .collect(Collectors.toList()));
        return dto;
    }

    private Dispatcher convertToEntity(DispatcherDTO dto) {
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setDispatcherId(dto.getDispatcherId());
        dispatcher.setName(dto.getName());
        dispatcher.setContactInfo(dto.getContactInfo());
        dispatcher.setAssignedRegion(dto.getAssignedRegion());
        // Set managed incidents based on their IDs if necessary.
        return dispatcher;
    }


}
