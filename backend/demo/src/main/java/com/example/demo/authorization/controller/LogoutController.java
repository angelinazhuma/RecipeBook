package com.example.demo.authorization.controller;

import com.example.demo.authorization.utils.CookieUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/logout")
@RequiredArgsConstructor
public class LogoutController {

  private final CookieUtils cookieUtils;

  @PostMapping
  public ResponseEntity<Void> logout() {
    ResponseCookie deletedCookie = cookieUtils.deleteJwtCookie();

    return ResponseEntity.noContent()
        .header(
            HttpHeaders.SET_COOKIE,
            deletedCookie.toString()
        )
        .build();
  }
}