package com.example.demo.authorization.controller;

import com.example.demo.authorization.DTO.LoginRequestDTO;
import com.example.demo.authorization.DTO.LoginResponseDTO;
import com.example.demo.authorization.model.User;
import com.example.demo.authorization.security.JwtService;
import com.example.demo.authorization.service.UserService;
import com.example.demo.authorization.utils.CookieUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/login")
@RequiredArgsConstructor
public class LoginController {

  private final UserService userService;
  private final JwtService jwtService;
  private final CookieUtils cookieUtils;

  @PostMapping
  public ResponseEntity<LoginResponseDTO> login(
      @Valid @RequestBody LoginRequestDTO request // gets request
  ) {

    User user = userService.login(request);

    String token = jwtService.generateToken(user);

    ResponseCookie jwtCookie = cookieUtils.createJwtCookie (token);


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