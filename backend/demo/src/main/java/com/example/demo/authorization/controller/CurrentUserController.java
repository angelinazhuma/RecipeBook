package com.example.demo.authorization.controller;

import com.example.demo.authorization.security.AuthenticatedUser;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class CurrentUserController {

  @GetMapping("/me")
  public AuthenticatedUser getCurrentUser(
      Authentication authentication
  ) {
    return (AuthenticatedUser)
        authentication.getPrincipal();
  }
}