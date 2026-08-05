package com.example.demo.authorization.security;

public record AuthenticatedUser(
    Long id,
    String username
) {
}
