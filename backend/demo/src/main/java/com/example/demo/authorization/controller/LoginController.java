package com.example.demo.authorization.controller;

import com.example.demo.authorization.DTO.LoginRequestDTO;
import com.example.demo.authorization.DTO.LoginResultDTO;
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
  public ResponseEntity<LoginResultDTO> login(
      @Valid @RequestBody LoginRequestDTO request
  ) {

    User user = userService.login(request);

    if (user.isMfaEnabled()) {

      String mfaToken =
          jwtService.generateMfaToken(user);

      return ResponseEntity.ok(
          new LoginResultDTO(
              true,
              mfaToken,
              "MFA required"
          )
      );
    }

    String token =
        jwtService.generateToken(user);

    ResponseCookie jwtCookie =
        cookieUtils.createJwtCookie(token);

    return ResponseEntity.ok()
        .header(
            HttpHeaders.SET_COOKIE,
            jwtCookie.toString()
        )
        .body(
            new LoginResultDTO(
                false,
                null,
                "Login successful"
            )
        );
  }}