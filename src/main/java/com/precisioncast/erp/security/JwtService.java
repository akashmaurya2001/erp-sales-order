package com.precisioncast.erp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    private final long normalJwtExpiration = 1000L * 60 * 60 * 24; // 1 day
    private final long rememberMeJwtExpiration = 1000L * 60 * 60 * 24 * 7; // 7 days

    public String generateToken(String email, String role, Boolean rememberMe) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        long expirationTime = Boolean.TRUE.equals(rememberMe)
                ? rememberMeJwtExpiration
                : normalJwtExpiration;

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignInKey())
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public long getNormalJwtExpiration() {
        return normalJwtExpiration;
    }

    public long getRememberMeJwtExpiration() {
        return rememberMeJwtExpiration;
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    public boolean isTokenValid(String token, String email) {
        final String username = extractUsername(token);
        return username.equals(email) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}