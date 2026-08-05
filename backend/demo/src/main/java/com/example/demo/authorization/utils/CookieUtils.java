package com.example.demo.authorization.utils;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import java.time.Duration;

@Component
public class CookieUtils {

  private static final String COOKIE_NAME = "jwt";

  public ResponseCookie createJwtCookie(String token) {
    return ResponseCookie
        .from("jwt", token)
        .httpOnly(true) // prevents client-side JavaScript from accessing the cookie, javascript can not reedthis cookie
        .secure(false) // tells when browser sends cookie by localhhost not in recipebook.com
        .sameSite("Lax") // stops cookies from being sent to third-party sites
        .path("/") // restricts the cookie to the root path
        .maxAge(Duration.ofHours(1)) // cookie expires after 1 hour
        .build(); // creates the cookie

  }

  public ResponseCookie deleteJwtCookie() {
    return ResponseCookie
        .from("jwt", "") // empty value, deletes the cookie
        .httpOnly(true) // prevents client-side JavaScript from accessing the cookie
        .secure(false)
        .sameSite("Lax") // stops cookies from being sent to third-party sites
        .path("/") // restricts the cookie to the root path
        .maxAge(0) // tells browser to delete cookie
        .build(); // creates the cookie
}}