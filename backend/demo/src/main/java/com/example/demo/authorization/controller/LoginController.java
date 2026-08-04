package com.example.demo.authorization.controller;

import com.example.demo.authorization.DTO.LoginRequestDTO;
import com.example.demo.authorization.DTO.LoginResponseDTO;
import com.example.demo.authorization.model.User;
import com.example.demo.authorization.security.JwtService;
import com.example.demo.authorization.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/auth/login")
@RequiredArgsConstructor
public class LoginController {

  private final UserService userService;
  private final JwtService jwtService;

  @PostMapping
  public ResponseEntity<LoginResponseDTO> login(
      @Valid @RequestBody LoginRequestDTO request // gets request
  ) {

    User user = userService.login(request);

    String token = jwtService.generateToken(user);

    ResponseCookie jwtCookie = ResponseCookie
        .from("jwt", token)
        .httpOnly(true) // prevents client-side JavaScript from accessing the cookie, javascript can not reedthis cookie
        .secure(false) // tells when browser sends cookie by localhhost not in recipebook.com
        .sameSite("Lax") // stops cookies from being sent to third-party sites
        .path("/") // restricts the cookie to the root path
        .maxAge(Duration.ofHours(1)) // cookie expires after 1 hour
        .build(); // creates the cookie


    LoginResponseDTO response =
        new LoginResponseDTO(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            "Login successful",
            null
        );

    return ResponseEntity.ok()
        .header(
            HttpHeaders.SET_COOKIE, // sets the cookie in the response header
            jwtCookie.toString() // converts the cookie to a string
        )
        .body(response); // returns the response
  }}