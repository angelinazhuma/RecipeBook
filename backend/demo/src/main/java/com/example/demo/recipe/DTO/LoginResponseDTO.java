package com.example.demo.recipe.DTO;

public record LoginResponseDTO(
        Long id,
        String username,
        String email,
        String message,
        String token
) {
}