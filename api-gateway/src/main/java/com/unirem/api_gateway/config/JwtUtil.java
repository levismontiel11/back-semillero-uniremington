package com.unirem.api_gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    public void validateToken(String token) {
        Jwts.parserBuilder()
            .setSigningKey(secret.getBytes())
                .build()
            .parseClaimsJws(token);
    }

    public String extractRole(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(secret.getBytes())
                .build()
            .parseClaimsJws(token)
            .getBody()
            .get("role", String.class);
    }
}
