package com.example.e_learning_application.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTUtil {

    private static final Logger logger = LoggerFactory.getLogger(JWTUtil.class);
    private final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Generate a secure key for HS256

    // Generate JWT token with ID
    public String generateToken(String username, String role, String userId) {
        String token = Jwts.builder()
                .setSubject(username)
                .claim("role", role) // Add the role as a claim
                .claim("id", userId) // Add the user ID as a claim
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour expiration time
                .signWith(SECRET_KEY)
                .compact();
        logger.info("Generated Token: {}", token);
        return token;
    }

    // Extract user ID from the token
    public String extractUserId(String token) {
        return extractClaims(token).get("id", String.class);
    }


    // Extract role from the token
    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }


    // Extract Claims from the token
    public Claims extractClaims(String token) {
        logger.info("Parsing Token: {}", token);
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Extract username from the token
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Check if the token is expired
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // Validate the token with username and role
    public boolean validateToken(String token, String username, String role) {
        return (username.equals(extractUsername(token)) &&
                role.equals(extractRole(token)) &&
                !isTokenExpired(token));
    }

}