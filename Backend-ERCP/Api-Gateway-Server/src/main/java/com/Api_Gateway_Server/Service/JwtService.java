package com.Api_Gateway_Server.Service;

import com.Api_Gateway_Server.Model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET = "TmV3U2VjcmV0S2V5Rm9ySldUU2lnbmluZ1B1cnBvc2VzMTIzNDU2Nzg="; // Your fixed secret key

    private String secretKey;

    public JwtService() {
        // Use the fixed secret key directly for both signing and verification
        secretKey = SECRET;
    }

    public String generateToken(String username, Role role) {
        Map<String, Object> claims = new HashMap<>();
//        claims.put("authorities", "ROLE_" + role.name());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .claim("role", role.name())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 )) // 60 minutes expiration
                .signWith(getKey(), SignatureAlgorithm.HS256) // Sign with the fixed key
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); // Decode the fixed secret key
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public List<String> extractAuthorities(String token) {
        Claims claims = extractAllClaims(token);
        // Extract the "authorities" claim and split it into a list of roles
        String authorities = claims.get("authorities", String.class); // Get the authorities as a single string
        return authorities != null ? Arrays.asList(authorities.split(",")) : new ArrayList<>(); // Split into list
    }


    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey()) // Use the fixed key for verification
                .build().parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
