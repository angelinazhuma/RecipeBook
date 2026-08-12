package com.example.demo.authorization.service;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.SecretWithEncapsulation;
import org.bouncycastle.crypto.generators.MLKEMKeyPairGenerator;
import org.bouncycastle.crypto.kems.MLKEMExtractor;
import org.bouncycastle.crypto.kems.MLKEMGenerator;
import org.bouncycastle.crypto.params.MLKEMKeyGenerationParameters;
import org.bouncycastle.crypto.params.MLKEMPrivateKeyParameters;
import org.bouncycastle.crypto.params.MLKEMPublicKeyParameters;
import org.bouncycastle.crypto.params.MLKEMParameters;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import javax.security.auth.DestroyFailedException;
import java.security.SecureRandom;
import java.util.Base64;

//MLKEM - KEY ENCAPSULATION MECHANISM

@Service
public class MlKemService {

  private static final MLKEMParameters PARAMETERS =
      MLKEMParameters.ml_kem_768; // algorithm parameters

  private final SecureRandom secureRandom =
      new SecureRandom(); // secure random number generator

  private final MLKEMPublicKeyParameters publicKey;
  private final MLKEMPrivateKeyParameters privateKey;

  public MlKemService(
      @Value("${mlkem.public-key}")
      String publicKeyBase64,

      @Value("${mlkem.private-key}")
      String privateKeyBase64
  ) {

    this.publicKey =
        new MLKEMPublicKeyParameters(
            PARAMETERS,
            Base64.getDecoder()
                .decode(publicKeyBase64)
        );

    this.privateKey =
        new MLKEMPrivateKeyParameters(
            PARAMETERS,
            Base64.getDecoder()
                .decode(privateKeyBase64)
        );
  }

  /*
  public MlKemKeyPair generateKeyPair() { // generates a new key pair

    MLKEMKeyPairGenerator generator =
        new MLKEMKeyPairGenerator();

    generator.init(
        new MLKEMKeyGenerationParameters(
            secureRandom,
            PARAMETERS
        )
    );

    AsymmetricCipherKeyPair keyPair =
        generator.generateKeyPair(); // generates the key pair

    MLKEMPublicKeyParameters publicKey =
        (MLKEMPublicKeyParameters)
            keyPair.getPublic(); // casts the public key parameters

    MLKEMPrivateKeyParameters privateKey =
        (MLKEMPrivateKeyParameters)
            keyPair.getPrivate(); // casts the private key parameters

    return new MlKemKeyPair(
        publicKey,
        privateKey
    ); // returns the key pair
  }

   */



  // this method encapsulates the public key
  // this method makes on the side which wants to send the secret
  // it makes the symmetric key and encrypts it with the public key

  public MlKemEncapsulation encapsulate()
      throws DestroyFailedException {

    MLKEMGenerator generator =
        new MLKEMGenerator(
            secureRandom
        ); // generates a new generator


    // this method generates random public secret and encrypts it with the public key
    SecretWithEncapsulation result =
        generator.generateEncapsulated(
            publicKey
        );

    try {
      return new MlKemEncapsulation(
          result.getSecret(),  // returns the shared secret
          result.getEncapsulation() // kemCiphertext
      );
    } finally { // destroys the encapsulated secret
      result.destroy();
    }
  }


// this method decapsulates the secret
  // this method is made by the side which received the secret
  // with his private key this side gets the original symmetric key
public byte[] decapsulate(
    byte[] kemCiphertext
) {

  MLKEMExtractor extractor =
      new MLKEMExtractor(
          privateKey
      );

  return extractor.extractSecret(
      kemCiphertext
  );
}

  /*
  public record MlKemKeyPair(
      MLKEMPublicKeyParameters publicKey,
      MLKEMPrivateKeyParameters privateKey
  ) {
  }
  */

  public record MlKemEncapsulation(
      byte[] sharedSecret,
      byte[] kemCiphertext
  ) {
  }
}
