package com.example.demo.authorization.controller;

import com.example.demo.authorization.model.User;
import com.example.demo.authorization.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth/me")
@RequiredArgsConstructor
public class CurrentUserController {

  private final CurrentUserService currentUserService;

  @GetMapping
  public ResponseEntity<Map<String, Object>> getCurrentUser() {
    User user = currentUserService.getCurrentUser();

    return ResponseEntity.ok(
        Map.of(
            "id", user.getId(),
            "username", user.getUsername(),
            "email", user.getEmail()
        )
    );
  }
}