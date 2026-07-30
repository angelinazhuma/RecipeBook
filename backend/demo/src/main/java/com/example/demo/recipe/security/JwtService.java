package com.example.demo.recipe.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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


}