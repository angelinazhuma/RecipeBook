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

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class MfaService {

  private final UserRepository userRepository;
  private final MfaSecretEncryptionService encryptionService;
  private final MlKemService mlKemService;

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

    try {
      MlKemService.MlKemEncapsulation encapsulation =
          mlKemService.encapsulate();

      String encryptedSecret =
          encryptionService.encrypt(
              secret,
              encapsulation.sharedSecret()
          ); // encrypts the secret with the shared secret

      String kemCiphertext =
          Base64.getEncoder()
              .encodeToString(
                  encapsulation.kemCiphertext()
              );

      user.setMfaSecret(
          encryptedSecret
      );

      user.setMfaKemCiphertext(
          kemCiphertext
      );


      userRepository.save(user);

      return new MfaSetupResult(
          user,
          secret
      );

    } catch (Exception e) {
      throw new IllegalStateException(
          "MFA setup failed",
          e
      );
    }
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

    if (
        user.getMfaSecret() == null ||
            user.getMfaKemCiphertext() == null
    ) {
      return false;
    }

    String secret =
        decryptMfaSecret(user);

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

    if (
        user.getMfaSecret() == null ||
            user.getMfaKemCiphertext() == null
    ) {
      return null;
    }

    String secret =
        decryptMfaSecret(user);

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
  } // dara record which is not changable
  // it is for hetting back the user and opened TOTP secret from setupMfa method

  private String decryptMfaSecret(
      User user
  ) {

    byte[] kemCiphertext =
        Base64.getDecoder()
            .decode(
                user.getMfaKemCiphertext()
            );

    byte[] sharedSecret =
        mlKemService.decapsulate(
            kemCiphertext
        );

    return encryptionService.decrypt(
        user.getMfaSecret(),
        sharedSecret
    );
  }

}