package com.example.demo.authorization.controller;

import com.example.demo.authorization.DTO.MfaCodeRequestDTO;
import com.example.demo.authorization.DTO.MfaLoginRequestDTO;
import com.example.demo.authorization.DTO.MfaSetupResponseDTO;
import com.example.demo.authorization.model.User;
import com.example.demo.authorization.security.AuthenticatedUser;
import com.example.demo.authorization.security.JwtService;
import com.example.demo.authorization.service.MfaService;
import com.example.demo.authorization.service.QRCodeService;
import com.example.demo.authorization.utils.CookieUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/auth/mfa")
@RequiredArgsConstructor
public class MfaController {

  private final MfaService mfaService;
  private final QRCodeService qrCodeService;
  private final JwtService jwtService;
  private final CookieUtils cookieUtils;

  @PostMapping("/setup")
  public ResponseEntity<?> setup(
      Authentication authentication
  ) {

    AuthenticatedUser currentUser =
        (AuthenticatedUser)
            authentication.getPrincipal();

    MfaService.MfaSetupResult result =
        mfaService.setupMfa(
            currentUser.id()
        );

    if (result == null) {
      return ResponseEntity
          .badRequest()
          .body("Could not setup MFA");
    }

    String issuer = "RecipeBook";

    String otpAuthUrl =
        "otpauth://totp/"
            + issuer
            + ":"
            + URLEncoder.encode(
            result.user().getUsername(),
            StandardCharsets.UTF_8
        )
            + "?secret="
            + result.secret()
            + "&issuer="
            + issuer;

    String qrCode =
        qrCodeService.generateQRCode(
            otpAuthUrl
        );

    return ResponseEntity.ok(
        new MfaSetupResponseDTO(qrCode)
    );
  }
  @PostMapping("/enable")
  public ResponseEntity<?> enableMfa(
      @RequestBody MfaCodeRequestDTO request,
      Authentication authentication
  ) {

    AuthenticatedUser currentUser =
        (AuthenticatedUser)
            authentication.getPrincipal();

    boolean success =
        mfaService.enableMfa(
            currentUser.id(),
            request.code()
        );

    if (!success) {
      return ResponseEntity
          .badRequest()
          .body("Invalid MFA code");
    }

    return ResponseEntity.ok().build();
  }

  @PostMapping("/verify-login")
  public ResponseEntity<?> verifyLogin(
      @RequestBody MfaLoginRequestDTO request
  ) {

    if (!jwtService.isMfaPending(
        request.mfaToken()
    )) {
      return ResponseEntity
          .badRequest()
          .body("Invalid MFA token");
    }

    Long userId =
        jwtService.extractUserId(
            request.mfaToken()
        );

    User user =
        mfaService.verifyLogin(
            userId,
            request.code()
        );

    if (user == null) {
      return ResponseEntity
          .badRequest()
          .body("Invalid MFA code");
    }

    String jwt =
        jwtService.generateToken(user);

    ResponseCookie cookie =
        cookieUtils.createJwtCookie(jwt);

    return ResponseEntity.ok()
        .header(
            HttpHeaders.SET_COOKIE,
            cookie.toString()
        )
        .build();
  }

}