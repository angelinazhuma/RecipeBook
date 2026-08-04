package com.example.demo.authorization.security;

import com.example.demo.authorization.model.User;
import com.example.demo.authorization.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
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

// before every http request, this filter checks if there is a JWT cookie
// saved in the securityconteztholder and authenticates the user
//controllers can get information about cuurentuser
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter
    extends OncePerRequestFilter {

  // service for reading and validating JWT tokens, knows how to make token, read token and check it
  private final JwtService jwtService;

  // repository for finding users
  private final UserRepository userRepository;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {

    // gets JWT token from the cookie
    String token = extractTokenFromCookies(request);

    // continues without authentication if there is no JWT cookie
    if (token == null || token.isBlank()) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      // gets username from the JWT token
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

        // checks that the user exists
        // and the JWT token is valid
        if (
            user != null
                && jwtService.isTokenValid(
                token,
                user
            )
        ) {
          // creates an authenticated Spring Security object
          UsernamePasswordAuthenticationToken authentication =
              new UsernamePasswordAuthenticationToken(
                  user.getUsername(),
                  null,
                  List.of()
              );

          authentication.setDetails(
              new WebAuthenticationDetailsSource()
                  .buildDetails(request)
          );

          // saves authentication for the current request
          SecurityContextHolder
              .getContext()
              .setAuthentication(authentication);
        }
      }
    } catch (
        JwtException
        | IllegalArgumentException exception
    ) {
      // clears authentication if JWT is invalid or expired
      SecurityContextHolder.clearContext();
    }

    // continues processing the request
    filterChain.doFilter(request, response);
  }

  // searches for the JWT cookie in the request
  private String extractTokenFromCookies(
      HttpServletRequest request
  ) {
    Cookie[] cookies = request.getCookies();

    if (cookies == null) {
      return null;
    }

    for (Cookie cookie : cookies) {
      if ("jwt".equals(cookie.getName())) {
        return cookie.getValue();
      }
    }

    return null;
  }
}