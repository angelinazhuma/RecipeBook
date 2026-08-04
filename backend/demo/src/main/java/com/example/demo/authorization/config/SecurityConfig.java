package com.example.demo.authorization.config;

import com.example.demo.authorization.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  } // BCrypt is a strong password hashing algorithm

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http
  ) throws Exception {

    http
        .cors(cors ->
            cors.configurationSource(
                corsConfigurationSource()
            )
        )

        .csrf(csrf -> csrf
            .csrfTokenRepository(
                CookieCsrfTokenRepository.withHttpOnlyFalse()
            )
            .ignoringRequestMatchers(
                "/auth/login",
                "/auth/register",
                "/auth/logout"
            )
        )

        .sessionManagement(session ->
            session.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS
            )
        ) // disables session management

        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                HttpMethod.OPTIONS,
                "/**"
            ).permitAll() // allows OPTIONS requests for CORS

            .requestMatchers(
                "/auth/register",
                "/auth/login", "/auth/logout"
            ).permitAll() // allows registration and login requests

            .requestMatchers(
                "/auth/me", "/recipes/**"
            ).authenticated() // requires authentication for all recipes routes

            .anyRequest().authenticated()
        )

        .exceptionHandling(exception ->
            exception.authenticationEntryPoint(
                new HttpStatusEntryPoint(
                    HttpStatus.UNAUTHORIZED
                )
            )
        ) // handles unauthorized requests with 401 status

        .addFilterBefore(
            jwtAuthenticationFilter,
            UsernamePasswordAuthenticationFilter.class
        ); // adds JWT authentication filter

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration =
        new CorsConfiguration();

    configuration.setAllowedOriginPatterns(List.of(
        "http://localhost:3000",
        "http://172.*.*.*:3000"
    ));

    configuration.setAllowedMethods(List.of(
        "GET",
        "POST",
        "PUT",
        "DELETE",
        "OPTIONS"
    ));

    configuration.setAllowedHeaders(List.of(
        "Content-Type",
        "X-XSRF-TOKEN"
    ));


    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source =
        new UrlBasedCorsConfigurationSource();

    source.registerCorsConfiguration(
        "/**",
        configuration
    ); // applies CORS configuration to all routes

    return source;
  }
}