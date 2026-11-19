package com.Api_Gateway_Server;
import com.Api_Gateway_Server.Config.*;
import com.Api_Gateway_Server.Controller.UserController;
import com.Api_Gateway_Server.Service.*;
import com.Api_Gateway_Server.FeignClient.ResponderFeignClient;
import com.Api_Gateway_Server.Model.*;
import com.Api_Gateway_Server.Model.Enums.Role;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.RequestDispatcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.mockito.Mockito.*;
@WebMvcTest({UserController.class, com.Api_Gateway_Server.Controller.ApiInfoController.class})
@Import({SecurityConfig.class, JwtFilter.class, ResponderAccess.class})
class SecurityBoundaryTest {
    @Test void rootExplainsApiWithoutExposingProtectedData() throws Exception {
        mvc.perform(get("/")).andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith("application/json"))
            .andExpect(jsonPath("$.service").value("Emergency Response API"));
        mvc.perform(get("/incidents")).andExpect(status().isUnauthorized());
    }
    @Autowired MockMvc mvc;
    @Test void internalErrorDispatchPreservesServiceFailureWithoutOpeningErrorRoute() throws Exception {
        mvc.perform(get("/error")).andExpect(status().isUnauthorized());
        mvc.perform(get("/error")
                .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 503)
                .with(request -> { request.setDispatcherType(DispatcherType.ERROR); return request; }))
            .andExpect(status().isServiceUnavailable());
    }
    @MockBean UserService users;
    @MockBean MyUserDetailsService details;
    @MockBean JwtService jwt;
    @MockBean ResponderFeignClient responders;
    String body = "{\"username\":\"staff\",\"password\":\"test-password\",\"role\":\"ADMIN\"}";
    @Test void existingAdminLoginUsesStoredRole() throws Exception {
        User account = new User(1L,"staff",new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("test-password"),Role.ADMIN);
        when(details.loadUserByUsername("staff")).thenReturn(new UserPrincipal(account));
        when(users.findByUsername("staff")).thenReturn(account); when(jwt.generateToken("staff",Role.ADMIN)).thenReturn("test-token");
        mvc.perform(post("/login").contentType("application/json").content(body))
            .andExpect(status().isOk()).andExpect(jsonPath("$.userId").value(1)).andExpect(jsonPath("$.jwtToken").value("test-token"));
    }
    @Test void existingResponderLoginIgnoresRequestedAdminRole() throws Exception {
        User account = new User(1L,"staff",new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("test-password"),Role.RESPONDER);
        when(details.loadUserByUsername("staff")).thenReturn(new UserPrincipal(account));
        when(users.findByUsername("staff")).thenReturn(account); when(jwt.generateToken("staff",Role.RESPONDER)).thenReturn("test-token");
        ResponderDto responder = new ResponderDto(); responder.setResponderId(7);
        when(responders.getResponderByUsername("staff")).thenReturn(responder);
        mvc.perform(post("/login").contentType("application/json").content(body))
            .andExpect(status().isOk()).andExpect(jsonPath("$.responderId").value(7));
        verify(jwt,never()).generateToken("staff",Role.ADMIN);
    }
    @Test void anonymousCannotProvisionStaff() throws Exception {
        mvc.perform(post("/register").contentType("application/json").content(body)).andExpect(status().isUnauthorized());
        verifyNoInteractions(users);
    }
    @Test void responderCannotProvisionOrDeleteIncidents() throws Exception {
        mvc.perform(post("/register").with(user("staff").roles("RESPONDER")).contentType("application/json").content(body)).andExpect(status().isForbidden());
        mvc.perform(delete("/incidents/1").with(user("staff").roles("RESPONDER"))).andExpect(status().isForbidden());
        verifyNoInteractions(users);
    }
    @Test void adminRegistrationReturnsSafeResponse() throws Exception {
        when(users.saveUser(any())).thenReturn(new User(1L,"staff","private-hash",Role.ADMIN));
        mvc.perform(post("/register").with(user("admin").roles("ADMIN")).contentType("application/json").content(body))
            .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.password").doesNotExist());
    }
    @Test void responderRegistrationRequiresMatchingProfile() throws Exception {
        String request = body.replace("ADMIN", "RESPONDER");
        mvc.perform(post("/register").with(user("admin").roles("ADMIN")).contentType("application/json").content(request))
            .andExpect(status().isUnprocessableEntity());
        verify(users, never()).saveUser(any());

        ResponderDto profile = new ResponderDto();
        profile.setResponderId(7);
        when(responders.getResponderByUsername("staff")).thenReturn(profile);
        when(users.saveUser(any())).thenReturn(new User(1L, "staff", "private-hash", Role.RESPONDER));
        mvc.perform(post("/register").with(user("admin").roles("ADMIN")).contentType("application/json").content(request))
            .andExpect(status().isOk()).andExpect(jsonPath("$.role").value("RESPONDER"))
            .andExpect(jsonPath("$.password").doesNotExist());
    }
    @Test void duplicateStaffRegistrationDoesNotChangeTheExistingAccount() throws Exception {
        when(users.findByUsername("staff")).thenReturn(new User(1L, "staff", "private-hash", Role.ADMIN));
        mvc.perform(post("/register").with(user("admin").roles("ADMIN")).contentType("application/json").content(body))
            .andExpect(status().isConflict());
        verify(users, never()).saveUser(any());
    }
    @Test void authenticatedResponderWithoutProfileGetsActionableStatus() throws Exception {
        User account = new User(1L, "staff", new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("test-password"), Role.RESPONDER);
        when(details.loadUserByUsername("staff")).thenReturn(new UserPrincipal(account));
        when(users.findByUsername("staff")).thenReturn(account);
        mvc.perform(post("/login").contentType("application/json").content(body))
            .andExpect(status().isUnprocessableEntity());
    }
    @Test void lookupLimitedToSelfOrAdmin() throws Exception {
        mvc.perform(get("/user/other").with(user("staff").roles("RESPONDER"))).andExpect(status().isForbidden());
        when(users.findByUsername("staff")).thenReturn(new User(1L,"staff","private-hash",Role.RESPONDER));
        mvc.perform(get("/user/staff").with(user("staff").roles("RESPONDER"))).andExpect(status().isOk()).andExpect(jsonPath("$.password").doesNotExist());
    }
    @Test void malformedTokenReturns401() throws Exception {
        when(jwt.extractUserName("bad")).thenThrow(new io.jsonwebtoken.MalformedJwtException("bad"));
        mvc.perform(get("/user/staff").header("Authorization","Bearer bad")).andExpect(status().isUnauthorized());
    }
    @Test void publicSosBypassesStaleToken() throws Exception {
        // No gateway route in this MVC slice: 404 proves the security chain let it through.
        mvc.perform(post("/incidents/create").header("Authorization","Bearer stale")).andExpect(status().isNotFound());
        verifyNoInteractions(jwt);
    }
    @Test void corsRejectsUnknownOriginAndAllowsConfiguredOrigin() throws Exception {
        mvc.perform(options("/login").header("Origin","https://untrusted.example").header("Access-Control-Request-Method","POST")).andExpect(status().isForbidden());
        mvc.perform(options("/login").header("Origin","http://localhost:4200").header("Access-Control-Request-Method","POST"))
            .andExpect(status().isOk()).andExpect(header().string("Access-Control-Allow-Origin","http://localhost:4200"))
            .andExpect(header().doesNotExist("Access-Control-Allow-Credentials"));
    }
    @Test void responderCannotReadAnotherRespondersHistory() throws Exception {
        ResponderDto dto = new ResponderDto(); dto.setResponderId(1);
        when(responders.getResponderByUsername("staff")).thenReturn(dto);
        mvc.perform(get("/responders/2/history").with(user("staff").roles("RESPONDER"))).andExpect(status().isForbidden());
        mvc.perform(patch("/responders/2/OFF_DUTY").with(user("staff").roles("RESPONDER"))).andExpect(status().isForbidden());
    }
}
