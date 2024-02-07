package com.odin.orchestrator.appmgmt.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AESHelper {
    private static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA256";

    private final String secret;
    private final String salt;
    private final int keySize;
    private final int iterationCount;
    
    @Value("${encryption.iv}")
    private String iv;

    public AESHelper(@Value("${encryption.secretKey}") String aesSecretKey,
                     @Value("${encryption.salt}") String aesSalt,
                     @Value("${encryption.keySize}") int aesKeySize,
                     @Value("${encryption.iterationCount}") int aesIterationCount) {
        this.secret = aesSecretKey;
        this.salt = aesSalt;
        this.keySize = aesKeySize;
        this.iterationCount = aesIterationCount;
    }

    public String encrypt(String plaintext) {
        try {
            SecretKey secretKey = generateSecretKey();
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            log.info(String.valueOf(Base64.getDecoder().decode(iv).length));
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(Base64.getDecoder().decode(iv)));
            byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            log.error("Error during encryption: {}", e.getMessage());
            return null;
        }
    }

    public String decrypt(String encryptedText) {
        try {
            SecretKey secretKey = generateSecretKey();
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            log.info("Key: " + Base64.getEncoder().encodeToString(secretKey.getEncoded()));
            log.info("IV: " + iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(Base64.getDecoder().decode(iv)));
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes);
        } catch (Exception e) {
            log.error("Error during decryption: {}", e.getMessage());
            return null;
        }
    }

    private SecretKey generateSecretKey() {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM);
            KeySpec spec = new PBEKeySpec(secret.toCharArray(), salt.getBytes(), iterationCount, keySize);
            SecretKey secretKey = factory.generateSecret(spec);
            return new SecretKeySpec(secretKey.getEncoded(), "AES");
        } catch (Exception e) {
            log.error("Error during key generation: {}", e.getMessage());
            return null;
        }
    }
}
