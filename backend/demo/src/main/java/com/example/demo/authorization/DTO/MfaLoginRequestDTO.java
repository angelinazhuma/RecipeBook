package com.example.demo.authorization.DTO;

public record MfaLoginRequestDTO(
    String mfaToken,
    String code
) {
}