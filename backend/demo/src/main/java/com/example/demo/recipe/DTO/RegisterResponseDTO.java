package com.example.demo.recipe.DTO;

import java.time.LocalDateTime;

public record RegisterResponseDTO(
    Long id,
    String username,
    String email,
    LocalDateTime createdAt
) {
}