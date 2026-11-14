package com.Api_Gateway_Server;
import com.Api_Gateway_Server.Config.LocalAdminBootstrap;
import com.Api_Gateway_Server.Repository.UserRepository;
import com.Api_Gateway_Server.Service.UserService;
import com.Api_Gateway_Server.Model.Enums.Role;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
class LocalAdminBootstrapTest {
    UserRepository repository = mock(UserRepository.class);
    UserService users = mock(UserService.class);
    @Test void explicitlyConfiguredEmptyDatabaseGetsAdmin() {
        new LocalAdminBootstrap(repository, users, "local-admin", "a-test-only-long-password").run();
        verify(users).saveUser(argThat(u -> u.getRole() == Role.ADMIN && u.getUsername().equals("local-admin")));
    }
    @Test void existingDatabaseIsNeverModified() {
        when(repository.count()).thenReturn(1L);
        new LocalAdminBootstrap(repository, users, "local-admin", "a-test-only-long-password").run();
        verifyNoInteractions(users);
    }
    @Test void missingCredentialsDoNotCreateDefaultAccount() {
        new LocalAdminBootstrap(repository, users, "", "").run();
        verifyNoInteractions(users);
    }
    @Test void weakPasswordIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new LocalAdminBootstrap(repository, users, "admin", "short").run());
        verifyNoInteractions(users);
    }
}
