package com.example.demo.authorization.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class MfaSecretEncryptionService {

  private static final String ALGORITHM =
      "AES/GCM/NoPadding";

  private static final int IV_LENGTH = 12;
  private static final int TAG_LENGTH = 128;

  private final SecretKeySpec key;

  public MfaSecretEncryptionService(
      @Value("${mfa.encryption-key}")
      String encryptionKey
  ) {
    byte[] keyBytes =
        Base64.getDecoder()
            .decode(encryptionKey);

    this.key =
        new SecretKeySpec(
            keyBytes,
            "AES"
        );
  }

  public String encrypt(String value) {
    try {
      byte[] iv = new byte[IV_LENGTH];

      new SecureRandom()
          .nextBytes(iv);

      Cipher cipher =
          Cipher.getInstance(ALGORITHM);

      cipher.init(
          Cipher.ENCRYPT_MODE,
          key,
          new GCMParameterSpec(
              TAG_LENGTH,
              iv
          )
      );

      byte[] encrypted =
          cipher.doFinal(
              value.getBytes(
                  StandardCharsets.UTF_8
              )
          );

      byte[] result =
          ByteBuffer.allocate(
                  iv.length +
                      encrypted.length
              )
              .put(iv)
              .put(encrypted)
              .array();

      return Base64.getEncoder()
          .encodeToString(result);

    } catch (Exception e) {
      throw new IllegalStateException(
          "Could not encrypt MFA secret",
          e
      );
    }
  }

  public String decrypt(String value) {
    try {
      byte[] decoded =
          Base64.getDecoder()
              .decode(value);

      ByteBuffer buffer =
          ByteBuffer.wrap(decoded);

      byte[] iv =
          new byte[IV_LENGTH];

      buffer.get(iv);

      byte[] encrypted =
          new byte[
              buffer.remaining()
              ];

      buffer.get(encrypted);

      Cipher cipher =
          Cipher.getInstance(ALGORITHM);

      cipher.init(
          Cipher.DECRYPT_MODE,
          key,
          new GCMParameterSpec(
              TAG_LENGTH,
              iv
          )
      );

      return new String(
          cipher.doFinal(encrypted),
          StandardCharsets.UTF_8
      );

    } catch (Exception e) {
      throw new IllegalStateException(
          "Could not decrypt MFA secret",
          e
      );
    }
  }
}