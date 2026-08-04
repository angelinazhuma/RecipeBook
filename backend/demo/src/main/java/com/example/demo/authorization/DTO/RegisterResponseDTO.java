package com.example.demo.authorization.DTO;

import java.time.LocalDateTime;

public record RegisterResponseDTO(
    Long id,
    String username,
    String email,
    LocalDateTime createdAt
) {
}