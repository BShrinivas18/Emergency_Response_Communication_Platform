package com.Api_Gateway_Server.Config;

import com.Api_Gateway_Server.FeignClient.ResponderFeignClient;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class ResponderAccess {
    private final ResponderFeignClient responders;
    public ResponderAccess(ResponderFeignClient responders) { this.responders = responders; }
    public boolean assignedTo(Authentication authentication, String id) {
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) return true;
        if (authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_RESPONDER"))) return false;
        try { return Integer.valueOf(id).equals(responders.getResponderByUsername(authentication.getName()).getIncidentId()); }
        catch (RuntimeException e) { return false; }
    }
    public boolean owns(Authentication authentication, String id) {
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) return true;
        if (authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_RESPONDER"))) return false;
        try {
            return responders.getResponderByUsername(authentication.getName()).getResponderId() == Integer.parseInt(id);
        } catch (RuntimeException e) { return false; }
    }
}
