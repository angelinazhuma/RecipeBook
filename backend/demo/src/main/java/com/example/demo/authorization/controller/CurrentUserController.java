package com.example.demo.authorization.controller;

import com.example.demo.authorization.model.User;
import com.example.demo.authorization.repository.UserRepository;
import com.example.demo.authorization.security.AuthenticatedUser;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class CurrentUserController {

  private final UserRepository userRepository;

  public CurrentUserController(
      UserRepository userRepository
  ) {
    this.userRepository = userRepository;
  }

  @GetMapping("/me")
  public Map<String, Object> getCurrentUser(
      Authentication authentication
  ) {
    AuthenticatedUser currentUser =
        (AuthenticatedUser)
            authentication.getPrincipal();

    User user = userRepository
        .findById(currentUser.id())
        .orElseThrow(() ->
            new IllegalArgumentException(
                "User not found"
            )
        );

    return Map.of(
        "id", user.getId(),
        "username", user.getUsername(),
        "role", user.getRole(),
        "mfaEnabled", user.isMfaEnabled()
    );
  }
}