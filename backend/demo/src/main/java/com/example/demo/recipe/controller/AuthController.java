package com.example.demo.recipe.controller;

import com.example.demo.recipe.DTO.LoginRequestDTO;
import com.example.demo.recipe.DTO.LoginResponseDTO;
import com.example.demo.recipe.DTO.RegisterRequestDTO;
import com.example.demo.recipe.DTO.RegisterResponseDTO;
import com.example.demo.recipe.model.User;
import com.example.demo.recipe.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.recipe.security.JwtService;

// controller gets http-requests and calls service methods

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = {
        "http://localhost:3000",
        "http://172.17.222.129:3000"
})
public class AuthController {

  private final UserService userService;
  private final JwtService jwtService;

  @PostMapping("/register")
  public ResponseEntity<RegisterResponseDTO> register(
          @Valid @RequestBody RegisterRequestDTO request
  ) {
    User user = userService.register(request);

    RegisterResponseDTO response = new RegisterResponseDTO(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getCreatedAt()
    );

    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDTO> login(
          @Valid
          @RequestBody
          LoginRequestDTO request
  ) {
    // checks the login and password
    User user = userService.login(request);

    // creates a JWT token for the user
    String token = jwtService.generateToken(user);

    // creates the response with the JWT token
    LoginResponseDTO response =
            new LoginResponseDTO(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    "Login successful",
                    token
            );

    return ResponseEntity.ok(response);
  }
}