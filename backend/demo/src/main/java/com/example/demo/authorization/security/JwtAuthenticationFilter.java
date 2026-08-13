package com.example.demo.authorization.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

  @Override
  protected void doFilterInternal(

      @NonNull HttpServletRequest request, // HAS URL, HTTP METHOD, HEADERS, COOKIES, BODY
      @NonNull HttpServletResponse response, //response that backends sends to the browser
      @NonNull FilterChain filterChain // chain of filters that are executed after this filter
  ) throws ServletException, IOException {

    // 1. gets JWT token from the cookie
    String token = extractTokenFromCookies(request);

    // continues without authentication if there is no JWT cookie
    if (token == null || token.isBlank()) {
      filterChain.doFilter(request, response); // this filter stopped its job, send request and response to tne next object
      return;
    }

    try {
      // gets username from the JWT token
      String username =
          jwtService.extractUsername(token);

      Long userId = jwtService.extractUserId(token);

      String role = jwtService.extractRole(token);

      boolean authenticationMissing =
          SecurityContextHolder.getContext().getAuthentication() == null;

      if (
          username != null
              && userId != null
              && authenticationMissing
      ) {

        AuthenticatedUser authenticatedUser =
            new AuthenticatedUser(
                userId,
                username,
                role
            );

        // checks if the JWT token is valid for the user
          UsernamePasswordAuthenticationToken authentication =
              new UsernamePasswordAuthenticationToken(
                  authenticatedUser,
                  null,
                  List.of(
                      new SimpleGrantedAuthority("ROLE_" + role)
                  )
              );

          authentication.setDetails(
              new WebAuthenticationDetailsSource()
                  .buildDetails(request) // sets the details of the authentication
          );

          // saves authentication for the current request
          SecurityContextHolder
              .getContext()
              .setAuthentication(authentication);
      }
    } catch (
        JwtException
        | IllegalArgumentException exception
    ) {
      // clears authentication if JWT is invalid or expired
      SecurityContextHolder.clearContext();
    }
    filterChain.doFilter(
        request,
        response
    );
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