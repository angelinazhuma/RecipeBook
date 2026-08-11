package com.example.demo.authorization.service;

import com.example.demo.authorization.model.User;
import com.example.demo.authorization.repository.UserRepository;
import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MfaService {

  private final UserRepository userRepository;
  private final MfaSecretEncryptionService encryptionService;

  public String generateSecret() {
    SecretGenerator secretGenerator =
        new DefaultSecretGenerator();

    return secretGenerator.generate();
  }

  public boolean verifyCode(
      String secret,
      String code
  ) {
    TimeProvider timeProvider =
        new SystemTimeProvider();

    CodeGenerator codeGenerator =
        new DefaultCodeGenerator();

    CodeVerifier verifier =
        new DefaultCodeVerifier(
            codeGenerator,
            timeProvider
        );

    return verifier.isValidCode(
        secret,
        code
    );
  }

  public MfaSetupResult setupMfa(
      Long userId
  ) {
    User user = userRepository
        .findById(userId)
        .orElse(null);

    if (user == null) {
      return null;
    }

    String secret =
        generateSecret();

    String encryptedSecret =
        encryptionService.encrypt(
            secret
        );

    user.setMfaSecret(
        encryptedSecret
    );

    user.setMfaEnabled(false);

    userRepository.save(user);

    return new MfaSetupResult(
        user,
        secret
    );
  }

  public boolean enableMfa(
      Long userId,
      String code
  ) {
    User user = userRepository
        .findById(userId)
        .orElse(null);

    if (user == null) {
      return false;
    }

    if (user.getMfaSecret() == null) {
      return false;
    }

    String secret =
        encryptionService.decrypt(
            user.getMfaSecret()
        );

    boolean valid =
        verifyCode(
            secret,
            code
        );

    if (!valid) {
      return false;
    }

    user.setMfaEnabled(true);

    userRepository.save(user);

    return true;
  }

  public User verifyLogin(
      Long userId,
      String code
  ) {
    User user = userRepository
        .findById(userId)
        .orElse(null);

    if (user == null) {
      return null;
    }

    if (user.getMfaSecret() == null) {
      return null;
    }

    String secret =
        encryptionService.decrypt(
            user.getMfaSecret()
        );

    boolean valid =
        verifyCode(
            secret,
            code
        );

    if (!valid) {
      return null;
    }

    return user;
  }

  public record MfaSetupResult(
      User user,
      String secret
  ) {
  }
}