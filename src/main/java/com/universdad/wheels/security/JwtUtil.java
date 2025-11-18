package com.universdad.wheels.security;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "clave_super_secreta_de_prueba_123456789";
    private static final long EXPIRATION_TIME = 86400000; // 1 día
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // Genera token incluyendo username, id y rol (como String)
    public String generateToken(String username, Long id, String rol) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .addClaims(Map.of(
                        "id", id,
                        "rol", rol
                ))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Extrae el username
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // Extrae el id
    public Long extractId(String token) {
        return getClaims(token).get("id", Long.class);
    }

    // Extrae el rol
    public String extractRol(String token) {
        return getClaims(token).get("rol", String.class);
    }

    // Valida el token
    public boolean isTokenValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    // Obtiene los claims del token
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
