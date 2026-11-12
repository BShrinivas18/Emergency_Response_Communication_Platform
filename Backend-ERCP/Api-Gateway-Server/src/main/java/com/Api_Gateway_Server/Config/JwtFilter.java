package com.Api_Gateway_Server.Config;
import com.Api_Gateway_Server.Service.JwtService;
import com.Api_Gateway_Server.Service.MyUserDetailsService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwt;
    private final MyUserDetailsService users;
    public JwtFilter(JwtService jwt, MyUserDetailsService users) { this.jwt = jwt; this.users = users; }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI().substring(request.getContextPath().length());
        return "POST".equals(request.getMethod()) &&
            ("/login".equals(path) || "/incidents/create".equals(path));
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            try {
                String token = header.substring(7);
                var user = users.loadUserByUsername(jwt.extractUserName(token));
                if (!jwt.validateToken(token, user)) throw new IllegalArgumentException();
                // Use current database authorities, never stale client claims.
                var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (JwtException | IllegalArgumentException | UsernameNotFoundException e) {
                SecurityContextHolder.clearContext();
                response.sendError(401, "Invalid or expired credentials");
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
