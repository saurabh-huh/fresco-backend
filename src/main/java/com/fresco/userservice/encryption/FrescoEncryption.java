package com.fresco.userservice.encryption;

import com.fresco.userservice.exceptionHandler.BusinessException;
import com.fresco.userservice.exceptionHandler.enums.ErrorCodes;
import com.fresco.userservice.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Slf4j
public class FrescoEncryption {

    private static final String ALGORITHM = Constants.ALGORITHM;
    private static final String SECRET_KEY = Constants.SECRET_KEY; // 16-byte key

    public static String encrypt(String data) throws BusinessException {
        try {
            log.info("****[FrescoEncryption] :: encrypt :: Starting encryption for data: {} ****", data);
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            String encryptedData = Base64.getEncoder().encodeToString(encryptedBytes);
            log.info("****[FrescoEncryption] :: encrypt :: Encryption completed. Encrypted data: {} ****", encryptedData);
            return encryptedData;
        } catch (Exception e) {
            log.error("Error during encryption: {}", e.getMessage());
            throw new BusinessException(ErrorCodes.ENCRYPTION_FAILED,HttpStatus.BAD_REQUEST);
        }
    }

    public static String decrypt(String encryptedData) {
        try {
            log.info("****[FrescoEncryption] :: decrypt :: Starting decryption for data: {} ****", encryptedData);
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            String decryptedData = new String(decryptedBytes);
            log.info("****[FrescoEncryption] :: decrypt :: Decryption completed. Decrypted data: {} ****", decryptedData);
            return decryptedData;
        } catch (Exception e) {
            log.error("****[FrescoEncryption] :: decrypt :: Error during decryption: {} ****", e.getMessage());
            throw new BusinessException(ErrorCodes.ENCRYPTION_FAILED,HttpStatus.BAD_REQUEST);
        }
    }
}