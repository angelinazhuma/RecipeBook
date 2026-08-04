package com.example.demo.authorization.service;

import com.example.demo.authorization.model.User;
import com.example.demo.authorization.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

// currentUserService retrieves information about the current user from the SecurityContextHolder
// during each request, JwtAuthenticationFilter verifies the JWT and saves an Authentication object into the SecurityContext
// after that, CurrentUserService gets the username via getAuthentication().getName(),
// looks up this user in the database using UserRepository,
// and returns the full User object. Thanks to this,
// other services can work with the current user
// to show only their recipes or save new recipes directly under their profile

@Service
@RequiredArgsConstructor
public class CurrentUserService {

  private final UserRepository userRepository;
  public User getCurrentUser() {

    String username = SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getName();


    User user = userRepository.findByUsername(username)
        .orElseThrow(() ->
            new RuntimeException("User not found: " + username)
        );

    return user;
  }



}