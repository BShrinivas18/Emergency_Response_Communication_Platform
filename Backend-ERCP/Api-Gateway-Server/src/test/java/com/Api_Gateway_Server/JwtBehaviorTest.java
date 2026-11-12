package com.Api_Gateway_Server;
import com.Api_Gateway_Server.Model.*;
import com.Api_Gateway_Server.Model.Enums.Role;
import com.Api_Gateway_Server.Service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class JwtBehaviorTest {
    @Test void gatewayReplacesSpoofedActor() throws Exception {
        var context = org.springframework.security.core.context.SecurityContextHolder.getContext();
        context.setAuthentication(new org.springframework.security.authentication.UsernamePasswordAuthenticationToken("staff", null, List.of()));
        var request = new org.springframework.mock.web.MockHttpServletRequest(); request.addHeader("X-ERCP-Actor", "forged");
        try {
            new com.Api_Gateway_Server.Config.TrustedActorFilter().doFilter(request,
                new org.springframework.mock.web.MockHttpServletResponse(), (req, res) ->
                    assertEquals("staff", ((jakarta.servlet.http.HttpServletRequest)req).getHeader("X-ERCP-Actor")));
        } finally { org.springframework.security.core.context.SecurityContextHolder.clearContext(); }
    }
    byte[] bytes = new byte[32];
    JwtBehaviorTest() { new java.security.SecureRandom().nextBytes(bytes); }
    JwtService jwt() { return new JwtService(Base64.getEncoder().encodeToString(bytes)); }
    @Test void validTokenAndDatabaseRoles() {
        User user = new User(1L, "staff", "hash", Role.RESPONDER);
        var principal = new UserPrincipal(user);
        String token = jwt().generateToken("staff", Role.RESPONDER);
        assertTrue(jwt().validateToken(token, principal));
        assertEquals("ROLE_RESPONDER", principal.getAuthorities().iterator().next().getAuthority());
        user.setRole(Role.ADMIN); assertEquals("ROLE_ADMIN", principal.getAuthorities().iterator().next().getAuthority());
    }
    @Test void expiredAndWronglySignedTokensRejected() {
        String expired = Jwts.builder().setSubject("staff").setExpiration(new Date(0))
            .signWith(Keys.hmacShaKeyFor(bytes)).compact();
        assertThrows(ExpiredJwtException.class, () -> jwt().extractUserName(expired));
        byte[] other = bytes.clone(); other[0] ^= 1;
        String forged = new JwtService(Base64.getEncoder().encodeToString(other)).generateToken("staff", Role.ADMIN);
        assertThrows(JwtException.class, () -> jwt().extractUserName(forged));
        assertThrows(JwtException.class, () -> jwt().extractUserName("not.a.token"));
    }
    @Test void weakKeyFailsAtStartup() { assertThrows(IllegalArgumentException.class, () -> new JwtService("c2hvcnQ=")); }
    @Test void safeResponseContainsNoPassword() throws Exception {
        String json = new ObjectMapper().writeValueAsString(UserResponse.from(new User(1L,"staff","secret-hash",Role.ADMIN)));
        assertFalse(json.contains("password")); assertFalse(json.contains("secret-hash"));
    }
}
