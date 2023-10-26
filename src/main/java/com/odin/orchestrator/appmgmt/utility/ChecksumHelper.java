package com.odin.orchestrator.appmgmt.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ChecksumHelper {

    private static final String AES_ALGORITHM = "AES";
    private static final String SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA512";

    @Value("${encryption.secretKey}")
    private String secret;

    @Value("${encryption.salt}")
    private String salt;

    @Value("${encryption.keySize}")
    private int keySize;

    @Value("${encryption.iterationCount}")
    private int iterationCount;

    @Value("${encryption.iv}")
    private String iv;

    public ChecksumHelper() {
    }

    public String encrypt(String plaintext) {
        try {
            SecretKey secretKey = generateSecretKey();
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
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
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Error during decryption: {}", e.getMessage());
            return null;
        }
    }

    private SecretKey generateSecretKey() {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM);
            char[] passwordChars = secret.toCharArray();
            byte[] saltBytes = salt.getBytes(StandardCharsets.UTF_8);

            PBEKeySpec spec = new PBEKeySpec(passwordChars, saltBytes, iterationCount, keySize);
            SecretKey secretKey = factory.generateSecret(spec);

            return new SecretKeySpec(secretKey.getEncoded(), "AES");
        } catch (Exception e) {
            log.error("Error during key generation: {}", e.getMessage());
            return null;
        }
    }
}
