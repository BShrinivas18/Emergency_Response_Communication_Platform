package com.Emergency_Response_Management.Service;

import com.Emergency_Response_Management.Exception.GeneralException;
import com.Emergency_Response_Management.Model.Dispatcher;
import com.Emergency_Response_Management.Repository.DispatcherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DispatcherService {

    @Autowired
    private DispatcherRepository dispatcherRepository;

    public Dispatcher createDispatcher(Dispatcher dispatcher) {
        return dispatcherRepository.save(dispatcher);
    }

    public List<Dispatcher> getAllDispatchers() {
        return dispatcherRepository.findAll();
    }

    public Optional<Dispatcher> getDispatcherById(Integer id) {
        return dispatcherRepository.findById(id);
    }

    public Dispatcher updateDispatcher(Integer id, Dispatcher dispatcher) {
        Dispatcher existingDispatcher = getDispatcherById(id).orElseThrow(()->new GeneralException("Dispatcher Not Found"));
        existingDispatcher.setName(dispatcher.getName());
        existingDispatcher.setContactInfo(dispatcher.getContactInfo());
        existingDispatcher.setAssignedRegion(dispatcher.getAssignedRegion());
        return dispatcherRepository.save(existingDispatcher);
    }

    public List<Dispatcher> getDispatchersByRegion(String region) {
        return dispatcherRepository.findByAssignedRegion(region);
    }

    public void deleteDispatcher(Integer id) {
        dispatcherRepository.deleteById(id);
    }
}
