package com.odin.orchestrator.appmgmt.utility;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
    private final SecureRandom random = new SecureRandom();
    private final IvParameterSpec ivParameterSpec;

    public AESHelper(@Value("${encryption.secretKey}") String aesSecretKey,
                     @Value("${encryption.salt}") String aesSalt,
                     @Value("${encryption.keySize}") int aesKeySize,
                     @Value("${encryption.iterationCount}") int aesIterationCount) {
        this.secret = aesSecretKey;
        this.salt = aesSalt;
        this.keySize = aesKeySize;
        this.iterationCount = aesIterationCount;

        // Generate the IV once and reuse it
        byte[] iv = new byte[16];
        random.nextBytes(iv);
        ivParameterSpec = new IvParameterSpec(iv);
    }

    public String encrypt(String plaintext) {
        try {
            SecretKey secretKey = generateSecretKey();
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
            byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());

            byte[] combined = new byte[ivParameterSpec.getIV().length + encryptedBytes.length];
            System.arraycopy(ivParameterSpec.getIV(), 0, combined, 0, ivParameterSpec.getIV().length);
            System.arraycopy(encryptedBytes, 0, combined, ivParameterSpec.getIV().length, encryptedBytes.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            log.error("{}", e.getMessage());
            return null;
        }
    }

    public String decrypt(String encryptedText) throws Exception {
        byte[] combined = Base64.getDecoder().decode(encryptedText);
        byte[] iv = new byte[16];
        byte[] encryptedBytes = new byte[combined.length - 16];

        System.arraycopy(combined, 0, iv, 0, 16);
        System.arraycopy(combined, 16, encryptedBytes, 0, encryptedBytes.length);

        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        SecretKey secretKey = generateSecretKey();
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }

    private SecretKey generateSecretKey() {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM);
            KeySpec spec = new PBEKeySpec(secret.toCharArray(), salt.getBytes(), iterationCount, keySize);
            SecretKey secretKey = factory.generateSecret(spec);
            return new SecretKeySpec(secretKey.getEncoded(), "AES");
        } catch (Exception e) {
            log.error("{}", e.getMessage());
            return null;
        }
    }
}
