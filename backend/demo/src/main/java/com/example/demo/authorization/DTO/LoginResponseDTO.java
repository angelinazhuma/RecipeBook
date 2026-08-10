package com.example.demo.authorization.DTO;

public record LoginResponseDTO(
    Long id,
    String username,
    String email,
    String message,
    String token,
    boolean mfaRequired,
    String mfaToken
) {
}