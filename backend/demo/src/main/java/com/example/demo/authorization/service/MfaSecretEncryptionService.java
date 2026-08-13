package com.example.demo.authorization.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class MfaSecretEncryptionService {

  private static final String ALGORITHM =
      "AES/GCM/NoPadding"; // AES in Galois/Counter Mode (GCM) with no padding

  // GCM - Galois/Counter Mode,
  // it doesnot only encrypts but also checks it for integrity and authenticity, checking are they fake or not
  // GCM is a symmetric block cipher mode of operation that provides authenticated encryption.

  // No padding - it does not add any padding to the message, because of GCM
  // NOSuchAlghoritmEXception, javaprobider does not support AESGCM
  // AESGCM - dont add extra bytes for aligning, tou dont need them
  //GCm dont need padding for alligning blocks

  private static final int IV_LENGTH = 12; // Recommended IV length for GCM is 12 bytes (96 bits)
   // IV is randomly generated order of bytes used with encryption key
  // with IV, the same message encrypted with the same key will produce different ciphertexts

  private static final int TAG_LENGTH = 128; // Authentication tag length in bits
  // confirms integrity and completeness
  // garanties that the message has not been changed or replaced by an attacker

  private final SecureRandom secureRandom =
      new SecureRandom(); // secure random number generator

  public String encrypt(
      String value, //mfa secret
      byte[] sharedSecret
  ) {
    try {
      SecretKeySpec key = createAesKey(sharedSecret);

      byte[] iv = new byte[IV_LENGTH];

      secureRandom.nextBytes(iv);

      Cipher cipher = Cipher.getInstance(ALGORITHM);

      GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH, iv);

      cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);

      byte[] encrypted = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));

      byte[] combined = ByteBuffer //makes memory for one big array its size is: iv + encrypted
              .allocate(
                  iv.length +
                      encrypted.length
              )
              .put(iv)
              .put(encrypted)
              .array(); //returns this buffer back to classic byte array

      return Base64.getEncoder().encodeToString(combined);
      // because enctypted is binary, we need to convert it to base64 and returns it

    } catch (Exception e) {
      throw new IllegalStateException(
          "MFA secret encryption failed",
          e
      );
    }
  }

  public String decrypt(
      String value, // text which i need to decrypt
      byte[] sharedSecret
  ) {
    try {SecretKeySpec key = createAesKey(sharedSecret);

      byte[] combined = Base64.getDecoder().decode(value); // IV + ENCRYPTES DATA + GCM TAG

      ByteBuffer buffer = ByteBuffer.wrap(
              combined // makes byte array to buffer where he can read step by step data from the array
          );

      byte[] iv = new byte[IV_LENGTH]; // Take out IV as 12 bytes

      buffer.get(iv); // get() method reads a sequence of bytes from this buffer

      byte[] encrypted = new byte[
              buffer.remaining()
              ]; // remaining() returns the number of remaining bytes in this buffer
      //get out the encrypted text

      buffer.get(encrypted);

      Cipher cipher = Cipher.getInstance(ALGORITHM);

      GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH, iv);
      // IV is needed to decrypt the message correctly
      // tag is for checking that data was not changed or replaced by an attacker
      // decrypt this data only if the tag is correct and the data was not changed

      // here is the key and here is IV, which was used for encrypting, now decrypt

      cipher.init(
          Cipher.DECRYPT_MODE, key, parameterSpec
          // gcm parameter spec is used to specify the initialization vector and the authentication tag length
      );

      byte[] decrypted = cipher.doFinal(encrypted);
      //takes authentication tag, checks it and decrypts the message and return back to the array

      return new String(
          decrypted,
          StandardCharsets.UTF_8
      ); // returns decrypted message to readable format

    } catch (Exception e) {
      throw new IllegalStateException(
          "MFA secret decryption failed",
          e
      );
    }
  }


  private SecretKeySpec createAesKey( //
      byte[] sharedSecret // shared secret is the secret which is used to encrypt the secret
                                      // nowits just a array of bytes we want to get it to aes key
  ) {
    try {
      MessageDigest digest =
          MessageDigest.getInstance( // make an algorithm which can read hash with provided alghotritm
              "SHA-256" // SHA-256 is a cryptographic hash function that produces a 256-bit (32-byte) hash value
          );

      byte[] keyBytes =
          digest.digest(// method that produces a hash value from the input data
              // derives 32 bytes (256 bits) from the ML-KEM shared secret using SHA-256
              sharedSecret // SHA256 - keybytes = 32 bytes
          );

      return new SecretKeySpec(
          keyBytes, // creates an AES-256 key from the 32 derived bytes
          "AES" // this 32 bytes now look as the aes key
      );

    } catch (Exception e) {
      throw new IllegalStateException(
          "Could not create AES key",
          e
      );
    }
  }
}