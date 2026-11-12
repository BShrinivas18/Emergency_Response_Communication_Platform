package com.Log_Service;
import com.Log_Service.Service.LogService;
import com.Log_Service.Repository.LogRepository;
import com.Log_Service.DTO.LogDTO;
import com.Log_Service.Model.Log;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
class AppendOnlyLogTest {
    @Test void callerCannotOverwriteExistingLogOrChangeTimestamp() {
        var repository = mock(LogRepository.class); var service = new LogService();
        ReflectionTestUtils.setField(service,"logRepository",repository);
        when(repository.save(any())).thenAnswer(call -> call.getArgument(0));
        LogDTO dto = new LogDTO(); dto.setLogId(99); dto.setIncidentId(42); dto.setStatusUpdate("test");
        service.createLog(dto);
        verify(repository).save(argThat(log -> log.getLogId() == 0 && log.getTimestamp() != null));
    }
    @Test void updateAndDeleteAreRejected() {
        var service = new LogService();
        assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> service.updateLog(1,new LogDTO()));
        assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> service.deleteLog(1));
    }
}
