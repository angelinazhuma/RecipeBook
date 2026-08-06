package com.example.demo.authorization.security;

import com.example.demo.authorization.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;

//JwtService handles JWT tokens. It creates a token with the username, user ID, creation time, and expiration time
// after login and signs it with a secret key for security
// JwtAuthenticationFilter then checks this signature and expiration
// on each request to log the user in

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
                .subject(user.getUsername()) // writes the username to the token
                .claim("userId", user.getId()) // adds the user id to the token
                .claim("role", user.getRole().name())
                .issuedAt(issuedAt)    // sets the time when the token was issued
                .expiration(expiration) // sets the time when the token will expire
                .signWith(getSigningKey()) // signs the token with the secret
                .compact(); // gets the representation line of the token
    }

    public String extractRole(String token) {
        return extractAllClaims(token)
            .get("role", String.class);
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
                .parseSignedClaims(token) //Parses the jws argument, expected to be a cryptographically signed Claims JWS
                .getPayload();
    }

    // converts the Base64 secret into a signing key
    private SecretKey getSigningKey() {
        byte[] keyBytes =
                Decoders.BASE64.decode(jwtSecret); // converts the secret into bytes

        return Keys.hmacShaKeyFor(keyBytes); // jwt library uses HMAC SHA-256
    }

    public long extractUserId(String token) {
        Number userId = extractAllClaims(token) // useful data/information/details
                .get("userId", Number.class); // gets the user id from the token
        // number.class is a parent class of long, because json parser returns a number

        return userId.longValue();
    }

}
