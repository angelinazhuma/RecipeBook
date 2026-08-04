package com.example.demo.authorization.controller;

import com.example.demo.authorization.DTO.RegisterRequestDTO;
import com.example.demo.authorization.DTO.RegisterResponseDTO;
import com.example.demo.authorization.model.User;
import com.example.demo.authorization.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/register")
@RequiredArgsConstructor
public class RegisterController {

  private final UserService userService;

  @PostMapping
  public ResponseEntity<RegisterResponseDTO> register(
      @Valid @RequestBody RegisterRequestDTO request
  ) {
    User user = userService.register(request);

    RegisterResponseDTO response =
        new RegisterResponseDTO(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getCreatedAt()
        );

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(response);
  }
}