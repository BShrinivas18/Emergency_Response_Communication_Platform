package com.Emergency_Response_Management.Controller;

import com.Emergency_Response_Management.DTO.DispatcherDTO;
import com.Emergency_Response_Management.Model.Dispatcher;
import com.Emergency_Response_Management.Service.DispatcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dispatchers")
public class DispatcherController {

    @Autowired
    private DispatcherService dispatcherService;

    @PostMapping
    public ResponseEntity<DispatcherDTO> createDispatcher(@RequestBody DispatcherDTO dispatcher) {
        return new ResponseEntity<>(dispatcherService.createDispatcher(dispatcher), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DispatcherDTO>> getAllDispatchers() {
        return new ResponseEntity<>(dispatcherService.getAllDispatchers(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DispatcherDTO> getDispatcherById(@PathVariable Integer id) {
        return new ResponseEntity<>(dispatcherService.getDispatcherById(id),HttpStatus.FOUND);
    }

    @GetMapping("/region/{region}")
    public ResponseEntity<List<Dispatcher>> getDispatchersByRegion(@PathVariable String region) {
        return ResponseEntity.ok(dispatcherService.getDispatchersByRegion(region));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DispatcherDTO> updateDispatcher(@PathVariable Integer id, @RequestBody DispatcherDTO updatedDispatcher) {
        return new ResponseEntity<>(dispatcherService.updateDispatcher(id, updatedDispatcher),HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDispatcher(@PathVariable Integer id) {
        dispatcherService.deleteDispatcher(id);
        return ResponseEntity.noContent().build();
    }
}
