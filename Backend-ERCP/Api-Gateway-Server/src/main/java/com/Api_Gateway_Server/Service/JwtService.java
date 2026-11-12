package com.Api_Gateway_Server.Service;
import com.Api_Gateway_Server.Model.Enums.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
@Service
public class JwtService {
    private final Key key;
    public JwtService(@Value("${security.jwt.secret}") String secret) {
        try { key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)); }
        catch (RuntimeException e) { throw new IllegalArgumentException("JWT_SECRET must encode at least 32 random bytes in base64"); }
    }
    public String generateToken(String username, Role role) {
        return Jwts.builder().setSubject(username).claim("role", role.name())
            .setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + 3600000))
            .signWith(key, SignatureAlgorithm.HS256).compact();
    }
    private Claims claims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
    public String extractUserName(String token) { return claims(token).getSubject(); }
    public boolean validateToken(String token, UserDetails user) {
        Claims c = claims(token);
        return user.getUsername().equals(c.getSubject()) && c.getExpiration() != null && c.getExpiration().after(new Date());
    }
}
