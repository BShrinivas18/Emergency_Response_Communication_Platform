package com.Emergency_Response_Management.Controller;

import com.Emergency_Response_Management.DTO.DispatcherDTO;
import com.Emergency_Response_Management.Model.Dispatcher;
import com.Emergency_Response_Management.Service.DispatcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dispatchers")
public class DispatcherController {

    @Autowired
    private DispatcherService dispatcherService;

    @PostMapping
    public DispatcherDTO createDispatcher(@RequestBody DispatcherDTO dispatcher) {
        return dispatcherService.createDispatcher(dispatcher);
    }

    @GetMapping
    public List<DispatcherDTO> getAllDispatchers() {
        return dispatcherService.getAllDispatchers();
    }

    @GetMapping("/{id}")
    public DispatcherDTO getDispatcherById(@PathVariable Integer id) {
        return dispatcherService.getDispatcherById(id);
    }

    @GetMapping("/region/{region}")
    public ResponseEntity<List<Dispatcher>> getDispatchersByRegion(@PathVariable String region) {
        return ResponseEntity.ok(dispatcherService.getDispatchersByRegion(region));
    }

    @PutMapping("/{id}")
    public DispatcherDTO updateDispatcher(@PathVariable Integer id, @RequestBody DispatcherDTO updatedDispatcher) {
        return dispatcherService.updateDispatcher(id, updatedDispatcher);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDispatcher(@PathVariable Integer id) {
        dispatcherService.deleteDispatcher(id);
        return ResponseEntity.noContent().build();
    }
}
