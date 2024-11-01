package com.Emergency_Response_Management.Controller;

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
    public Dispatcher createDispatcher(@RequestBody Dispatcher dispatcher) {
        return dispatcherService.createDispatcher(dispatcher);
    }

    @GetMapping
    public List<Dispatcher> getAllDispatchers() {
        return dispatcherService.getAllDispatchers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dispatcher> getDispatcherById(@PathVariable Integer id) {
        return dispatcherService.getDispatcherById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Dispatcher updateDispatcher(@PathVariable Integer id, @RequestBody Dispatcher updatedDispatcher) {
        return dispatcherService.updateDispatcher(id, updatedDispatcher);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDispatcher(@PathVariable Integer id) {
        dispatcherService.deleteDispatcher(id);
        return ResponseEntity.noContent().build();
    }
}
