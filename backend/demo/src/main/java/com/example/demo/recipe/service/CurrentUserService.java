package com.example.demo.recipe.service;

import com.example.demo.recipe.model.User;
import com.example.demo.recipe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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