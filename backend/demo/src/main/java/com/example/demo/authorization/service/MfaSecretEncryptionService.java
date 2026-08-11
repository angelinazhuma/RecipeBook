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
      "AES/GCM/NoPadding"; // AES in Galois/Counter Mode (GCM) with no padding

  // GCM - Galois/Counter Mode
  //
  // GCM is a symmetric block cipher mode of operation that provides authenticated encryption.
  // It is designed to be more secure than CBC and CTR modes, and is more efficient than
  // CCM and EAX modes.

  // No

  private static final int IV_LENGTH = 12; // Recommended IV length for GCM is 12 bytes (96 bits)
   // IV is randomly generated order of bytes used with encryption key
  private static final int TAG_LENGTH = 128; // Authentication tag length in bits
  // confirms integrity and completeness

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
    try { // 1. Generate a random IV (Initialization Vector)
      byte[] iv = new byte[IV_LENGTH];

      SecureRandom secureRandom =
          new SecureRandom();

      secureRandom.nextBytes(iv);

// 2. Initialize the cipher in ENCRYPT_MODE
      Cipher cipher =
          Cipher.getInstance(ALGORITHM);

      GCMParameterSpec parameterSpec =
          new GCMParameterSpec(
              TAG_LENGTH,
              iv
          );

      cipher.init(
          Cipher.ENCRYPT_MODE,
          key,
          parameterSpec
      );

      byte[] encrypted =
          cipher.doFinal(
              value.getBytes(
                  StandardCharsets.UTF_8
              )
          );

      byte[] combined =
          ByteBuffer
              .allocate(
                  iv.length +
                      encrypted.length
              )
              .put(iv)
              .put(encrypted)
              .array();

      return Base64
          .getEncoder()
          .encodeToString(combined);

    } catch (Exception e) {
      throw new IllegalStateException(
          "MFA secret encryption failed",
          e
      );
    }
  }

  public String decrypt(String value) {
    try {
      byte[] combined =
          Base64.getDecoder()
              .decode(value);

      ByteBuffer buffer =
          ByteBuffer.wrap(combined);

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

      GCMParameterSpec parameterSpec =
          new GCMParameterSpec(
              TAG_LENGTH,
              iv
          );

      cipher.init(
          Cipher.DECRYPT_MODE,
          key,
          parameterSpec
      );

      byte[] decrypted =
          cipher.doFinal(encrypted);

      return new String(
          decrypted,
          StandardCharsets.UTF_8
      );

    } catch (Exception e) {
      throw new IllegalStateException(
          "MFA secret decryption failed",
          e
      );
    }
  }
}