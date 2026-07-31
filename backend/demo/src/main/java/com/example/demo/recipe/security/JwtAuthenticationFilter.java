package com.example.demo.recipe.security;

import com.example.demo.recipe.model.User;
import com.example.demo.recipe.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    // service for reading and validating JWT tokens
    private final JwtService jwtService;

    // repository for finding users
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // gets the Authorization header
        String authorizationHeader =
                request.getHeader("Authorization");

        // continues without authentication if there is no token
        if (
                authorizationHeader == null
                        || !authorizationHeader
                        .startsWith("Bearer ")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        // removes "Bearer " and gets only the JWT token
        String token =
                authorizationHeader.substring(7);

        try {
            // gets the username from the token
            String username =
                    jwtService.extractUsername(token);

            // checks that the user is not already authenticated
            boolean authenticationMissing =
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication() == null;

            if (
                    username != null
                            && authenticationMissing
            ) {
                // finds the user in the database
                User user = userRepository
                        .findByUsername(username)
                        .orElse(null);

                // checks that the user exists and the token is valid
                if (
                        user != null
                                && jwtService.isTokenValid(
                                token,
                                user
                        )
                ) {
                    // creates an authentication object
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    user.getUsername(),
                                    null,
                                    List.of()
                            );

                    // adds request information to authentication
                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    // marks the user as authenticated
                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authentication);
                }
            }
        } catch (
                JwtException
                | IllegalArgumentException exception
        ) {
            // removes authentication if the token is invalid
            SecurityContextHolder.clearContext();
        }

        // continues processing the request
        filterChain.doFilter(request, response);
    }
}