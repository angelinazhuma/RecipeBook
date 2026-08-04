package com.example.demo.authorization.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/logout")
public class LogoutController {

  @PostMapping
  public ResponseEntity<Void> logout() {
    ResponseCookie deletedCookie = ResponseCookie
        .from("jwt", "") // empty value, deletes the cookie
        .httpOnly(true) // prevents client-side JavaScript from accessing the cookie
        .secure(false)
        .sameSite("Lax") // stops cookies from being sent to third-party sites
        .path("/") // restricts the cookie to the root path
        .maxAge(0) // tells browser to delete cookie
        .build(); // creates the cookie

    return ResponseEntity.noContent()
        .header(
            HttpHeaders.SET_COOKIE,
            deletedCookie.toString()
        )
        .build();
  }
}