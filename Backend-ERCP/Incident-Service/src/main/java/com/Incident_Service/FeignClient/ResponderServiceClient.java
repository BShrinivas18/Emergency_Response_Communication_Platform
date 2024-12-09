package com.Incident_Service.FeignClient;

import com.Incident_Service.Dto.IncidentDTO;
import com.Incident_Service.Dto.ResponderDTO;
import com.Incident_Service.Model.Incident;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "RESPONDER-SERVICE",path = "/responders")
public interface ResponderServiceClient {

    @PostMapping("/assign-responder")
    public void assignResponder(@RequestBody Incident incident);
//    public void assignResponder(@RequestParam("incidentId") int incidentId);

    @GetMapping("/incident/{incidentId}")
     public List<ResponderDTO> getRespondersByIncident(@PathVariable int incidentId);
}
