package com.example.demo.authorization.controller;

import com.example.demo.authorization.DTO.MfaCodeRequestDTO;
import com.example.demo.authorization.DTO.MfaLoginRequestDTO;
import com.example.demo.authorization.DTO.MfaSetupResponseDTO;
import com.example.demo.authorization.model.User;
import com.example.demo.authorization.repository.UserRepository;
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
  private final UserRepository userRepository;
  private final QRCodeService qrCodeService;
  private final JwtService jwtService;
  private final CookieUtils cookieUtils;

  @PostMapping("/setup")
  public MfaSetupResponseDTO setup(
      Authentication authentication
  ) {

    AuthenticatedUser currentUser =
        (AuthenticatedUser)
            authentication.getPrincipal();

    User user = userRepository
        .findById(currentUser.id())
        .orElseThrow(() ->
            new IllegalArgumentException(
                "User not found"
            )
        );

    String secret =
        mfaService.generateSecret();

    user.setMfaSecret(secret);
    userRepository.save(user);

    String issuer = "RecipeBook"; //identifies recipebook

    // structs the url for the qr code
    String otpAuthUrl =
        "otpauth://totp/"
            + issuer //identifies recipebook
            + ":"
            + URLEncoder.encode(
            user.getUsername(),
            StandardCharsets.UTF_8 // takes username and encodes it to url
        )
            + "?secret="
            + secret // secret key
            + "&issuer="
            + issuer;

// otpauthurl to qrcodeservice
    String qrCode =
        qrCodeService.generateQRCode(
            otpAuthUrl // url to qr code
        );

    return new MfaSetupResponseDTO(qrCode);
}
  @PostMapping("/enable")
  public void enableMfa(
      @RequestBody MfaCodeRequestDTO request,
      Authentication authentication
  ) {

    AuthenticatedUser currentUser =
        (AuthenticatedUser)
            authentication.getPrincipal();

    User user = userRepository
        .findById(currentUser.id())
        .orElseThrow(() ->
            new IllegalArgumentException(
                "User not found"
            )
        );

    boolean valid =
        mfaService.verifyCode(
            user.getMfaSecret(),
            request.code()
        );

    if (!valid) {
      throw new IllegalArgumentException(
          "Invalid MFA code"
      );
    }

    user.setMfaEnabled(true);

    userRepository.save(user);
  }

  @PostMapping("/verify-login")
  public ResponseEntity<Void> verifyLogin(
      @RequestBody MfaLoginRequestDTO request
  ) {

    if (!jwtService.isMfaPending(request.mfaToken())) {
      throw new IllegalArgumentException(
          "Invalid MFA token"
      );
    }

    Long userId =
        jwtService.extractUserId(
            request.mfaToken()
        );

    User user =
        userRepository.findById(userId)
            .orElseThrow(() ->
                new IllegalArgumentException(
                    "User not found"
                )
            );

    boolean valid =
        mfaService.verifyCode(
            user.getMfaSecret(),
            request.code()
        );

    if (!valid) {
      throw new IllegalArgumentException(
          "Invalid code"
      );
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