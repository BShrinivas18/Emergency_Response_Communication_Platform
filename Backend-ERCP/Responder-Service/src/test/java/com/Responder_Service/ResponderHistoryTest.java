package com.Responder_Service;
import com.Responder_Service.Service.ResponderService;
import com.Responder_Service.Repository.*;
import com.Responder_Service.Model.*;
import com.Responder_Service.DTO.*;
import com.Responder_Service.Enums.*;
import com.Responder_Service.FeignClient.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
@ExtendWith(MockitoExtension.class)
class ResponderHistoryTest {
    @Mock ResponderRepository repository;
    @Mock ResponderEventRepository events;
    @Mock IncidentServiceClient incidents;
    @Mock LocationServiceClient locations;
    @Mock LogServiceClient logs;
    @InjectMocks ResponderService service;
    Responder responder;
    @BeforeEach void setup() {
        responder = new Responder(); responder.setResponderId(1); responder.setStatus(ResponderStatus.ASSIGNED); responder.setIncidentId(42);
    }
    @Test void offDutyKeepsIncidentOpenAndPreservesHistory() {
        when(repository.findById(1)).thenReturn(Optional.of(responder));
        service.updateStatus(1, ResponderStatus.OFF_DUTY);
        assertNull(responder.getIncidentId()); assertNotNull(responder.getLastUpdate());
        verifyNoInteractions(incidents);
        ArgumentCaptor<ResponderEvent> event = ArgumentCaptor.forClass(ResponderEvent.class);
        verify(events).save(event.capture());
        assertEquals(42,event.getValue().getIncidentId()); assertEquals("ASSIGNED",event.getValue().getPreviousStatus());
        assertEquals("OFF_DUTY",event.getValue().getNewStatus()); assertNotNull(event.getValue().getOccurredAt());
        assertEquals("SYSTEM",event.getValue().getActor());
        verify(logs).createLog(argThat(log -> log.getIncidentId() == 42));
    }
    @Test void unavailableLogServiceDoesNotLoseLocalHistory() {
        when(repository.findById(1)).thenReturn(Optional.of(responder));
        doThrow(new RuntimeException()).when(logs).createLog(any());
        service.updateStatus(1, ResponderStatus.AVAILABLE);
        verify(events).save(any()); verify(repository).save(responder);
    }
    @Test void cannotGoOnRouteWithoutAssignment() {
        responder.setIncidentId(null); when(repository.findById(1)).thenReturn(Optional.of(responder));
        assertThrows(IllegalArgumentException.class, () -> service.updateStatus(1,ResponderStatus.ON_ROUTE));
        verifyNoInteractions(events,logs); verify(repository,never()).save(any());
    }
    @Test void policeFallbackUsesAvailableCandidatesOnly() {
        IncidentDTO incident = new IncidentDTO(); incident.setIncidentId(42); incident.setType(IncidentType.FIRE);
        when(repository.findByStatusAndType(ResponderStatus.AVAILABLE,ResponderType.FIREFIGHTER)).thenReturn(List.of());
        responder.setStatus(ResponderStatus.AVAILABLE);
        when(repository.findByStatusAndType(ResponderStatus.AVAILABLE,ResponderType.POLICE_OFFICER)).thenReturn(List.of(responder));
        service.assignAppropriateResponder(incident);
        verify(repository,never()).findByType(any()); verify(events).save(any()); assertEquals(ResponderStatus.ASSIGNED,responder.getStatus());
    }
    @Test void historyUsesRequestedResponder() { service.history(1); verify(events).findByResponderIdOrderByIdAsc(1); }
    @Test void onRouteStillAdvancesNewIncident() {
        when(repository.findById(1)).thenReturn(Optional.of(responder));
        IncidentDTO incident = new IncidentDTO(); incident.setIncidentId(42); incident.setStatus(IncidentStatus.NEW);
        when(incidents.getIncidentById(42)).thenReturn(incident);
        service.updateStatus(1,ResponderStatus.ON_ROUTE);
        verify(incidents).updateIncidentStatus(42,IncidentStatus.IN_PROGRESS); verify(events).save(any());
    }
    @Test void operationalProgressDoesNotReopenTerminalIncident() {
        when(repository.findById(1)).thenReturn(Optional.of(responder));
        IncidentDTO incident = new IncidentDTO(); incident.setIncidentId(42); incident.setStatus(IncidentStatus.RESOLVED);
        when(incidents.getIncidentById(42)).thenReturn(incident);
        service.updateStatus(1,ResponderStatus.ON_SCENE);
        verify(incidents,never()).updateIncidentStatus(anyInt(),any()); verify(events).save(any());
    }
    @Test void creationCannotOverwriteOrForgeAssignmentAndIsAudited() {
        ResponderDTO dto = new ResponderDTO(); dto.setResponderId(99); dto.setIncidentId(42); dto.setStatus(ResponderStatus.ASSIGNED);
        when(repository.save(any())).thenAnswer(call -> { Responder r = call.getArgument(0); assertEquals(0,r.getResponderId()); r.setResponderId(1); return r; });
        var result = service.createResponder(dto);
        assertEquals(ResponderStatus.AVAILABLE,result.getStatus()); assertNull(result.getIncidentId());
        verify(events).save(argThat(event -> event.getEventType().equals("CREATED") && event.getResponderId() == 1));
    }
}
