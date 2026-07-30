package com.example.demo.recipe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {


// tells spring, make object password encoder and save it in spring context
@Bean
public PasswordEncoder passwordEncoder() {
  return new BCryptPasswordEncoder();
}

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)
          throws Exception {

    http
            .csrf(csrf -> csrf.disable()) //turns off crsf safety for rest api without it post request might be forbidden
            .authorizeHttpRequests(auth -> auth
                    //allows registration without signing in
                    .requestMatchers("/auth/register", "/auth/login").permitAll()
                    // allows all other requests too for now!
                    .anyRequest().permitAll()
            );

    return http.build();
  }
}