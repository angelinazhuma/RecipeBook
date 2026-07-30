package com.example.demo.recipe.security;

import com.example.demo.recipe.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.JwtException;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    // secret used to sign and verify JWT tokens
    private final String jwtSecret;

    // token lifetime in milliseconds
    private final long expirationMs;

    public JwtService(
            @Value("${security.jwt.secret}")
            String jwtSecret,

            @Value("${security.jwt.expiration-ms}")
            long expirationMs
    ) {
        this.jwtSecret = jwtSecret;
        this.expirationMs = expirationMs;
    }

    // creates a JWT token for the user
    public String generateToken(User user) {
        Date issuedAt = new Date();

        // calculates when the token will expire
        Date expiration = new Date(
                issuedAt.getTime() + expirationMs
        );

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId", user.getId())
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    // gets the username from the token
    public String extractUsername(String token) {
        return extractAllClaims(token)
                .getSubject();
    }

    // reads all information from the token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // converts the Base64 secret into a signing key
    private SecretKey getSigningKey() {
        byte[] keyBytes =
                Decoders.BASE64.decode(jwtSecret);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    // checks whether the token belongs to the user
    public boolean isTokenValid(
            String token,
            User user
    ) {
        try {
            String username = extractUsername(token);

            return username.equals(user.getUsername());
        } catch (
                JwtException
                | IllegalArgumentException exceptionS
        ) {
            return false;
        }
    }
}