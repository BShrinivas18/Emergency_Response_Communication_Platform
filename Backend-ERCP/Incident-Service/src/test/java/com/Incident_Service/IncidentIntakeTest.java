package com.Incident_Service;
import com.Incident_Service.Dto.*;
import com.Incident_Service.Enums.*;
import com.Incident_Service.FeignClient.*;
import com.Incident_Service.Model.Incident;
import com.Incident_Service.Repository.IncidentRepository;
import com.Incident_Service.Service.IncidentService;
import com.Incident_Service.Controller.IncidentController;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Optional;

class IncidentIntakeTest {
    IncidentRepository repository = mock(IncidentRepository.class);
    LocationServiceClient locations = mock(LocationServiceClient.class);
    ResponderServiceClient responders = mock(ResponderServiceClient.class);
    VictimServiceClient victims = mock(VictimServiceClient.class);
    LogServiceClient logs = mock(LogServiceClient.class);
    IncidentService service = new IncidentService(repository, locations, responders, victims, logs);
    IncidentDTO request;
    @BeforeEach void setup() {
        request = new IncidentDTO(); request.setType(IncidentType.SOS_REQUEST);
        request.setIncidentLocation(new LocationDTO(0, 12.0, 77.0, null));
        when(locations.findByLatitudeAndLongitude(12.0, 77.0)).thenReturn(Optional.empty());
        when(locations.createLocation(any())).thenReturn(new LocationDTO(7, 12.0, 77.0, null));
        when(repository.saveAndFlush(any())).thenAnswer(call -> { Incident i = call.getArgument(0); i.setIncidentId(42); return i; });
    }
    @Test void anonymousSosWithoutVictimOrAddressReturnsCreatedReference() {
        var result = new IncidentController(service).createIncident(request);
        assertEquals(201, result.getStatusCode().value());
        IncidentDTO receipt = (IncidentDTO) result.getBody();
        assertEquals(42, receipt.getIncidentId()); assertEquals(IncidentStatus.NEW, receipt.getStatus());
        assertTrue(receipt.getResponderIds().isEmpty()); verifyNoInteractions(victims);
    }
    @Test void downstreamFailureAfterCommitDoesNotEraseReceipt() {
        doThrow(new RuntimeException("unavailable")).when(responders).assignResponder(any());
        when(logs.createLog(any())).thenThrow(new RuntimeException("unavailable"));
        assertEquals(42, service.reportIncident(request).getIncidentId());
        verify(locations, never()).getLocationById(anyInt());
        var order = inOrder(repository, responders); order.verify(repository).saveAndFlush(any()); order.verify(responders).assignResponder(any());
    }
    @Test void persistenceFailureNeverAcknowledges() {
        doThrow(new RuntimeException("sensitive internal error")).when(repository).saveAndFlush(any());
        var result = new IncidentController(service).createIncident(request);
        assertEquals(500, result.getStatusCode().value());
        assertFalse(result.getBody().toString().contains("sensitive")); verifyNoInteractions(responders, logs);
    }
    @Test void missingOrOutOfRangeCoordinatesRejectedBeforeSideEffects() {
        request.setIncidentLocation(null);
        assertEquals(400, new IncidentController(service).createIncident(request).getStatusCode().value());
        request.setIncidentLocation(new LocationDTO(0, Double.NaN, 181.0, null));
        assertThrows(IllegalArgumentException.class, () -> service.reportIncident(request));
        verifyNoInteractions(repository, locations, responders, victims, logs);
    }
    @Test void anonymousAccidentKeepsRequestedType() {
        request.setType(IncidentType.TRAFFIC_ACCIDENT); request.setVictimName("Anonymous");
        assertEquals(IncidentType.TRAFFIC_ACCIDENT, service.reportIncident(request).getType());
    }
}
