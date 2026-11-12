package com.Api_Gateway_Server.Config;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.*;

/** Replaces caller-supplied audit identity at the trusted single-host gateway boundary. */
public class TrustedActorFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        String actor = auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)
                ? auth.getName() : "SYSTEM";
        chain.doFilter(new HttpServletRequestWrapper(request) {
            @Override public String getHeader(String name) {
                return "X-ERCP-Actor".equalsIgnoreCase(name) ? actor : super.getHeader(name);
            }
            @Override public Enumeration<String> getHeaders(String name) {
                return "X-ERCP-Actor".equalsIgnoreCase(name) ? Collections.enumeration(List.of(actor)) : super.getHeaders(name);
            }
            @Override public Enumeration<String> getHeaderNames() {
                Set<String> names = new HashSet<>(Collections.list(super.getHeaderNames()));
                names.removeIf(n -> n.equalsIgnoreCase("X-ERCP-Actor")); names.add("X-ERCP-Actor");
                return Collections.enumeration(names);
            }
        }, response);
    }
}
