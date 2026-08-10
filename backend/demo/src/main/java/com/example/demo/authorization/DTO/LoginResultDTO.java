package com.example.demo.authorization.DTO;

public record LoginResultDTO(
    boolean mfaRequired,
    String mfaToken,
    String message
) {
}