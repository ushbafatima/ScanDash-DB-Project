package CardManagement;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class BlowfishCipher {
    // Ensure the key length is exactly 8 bytes for Blowfish
    private static final String key = "46721251"; // 8 characters = 8 bytes

    /**
     * Encrypts the provided data using the Blowfish algorithm.
     *
     * @param data the data to encrypt
     * @return the encrypted data as a Base64-encoded string
     * @throws Exception if an encryption error occurs
     */
    public static String encrypt(String data) throws Exception {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "Blowfish");

            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encryptedData = cipher.doFinal(data.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            throw new Exception("Error encrypting data: " + e.getMessage(), e);
        }
    }

    /**
     * Decrypts the provided Base64-encoded encrypted data using the Blowfish algorithm.
     *
     * @param encryptedData the Base64-encoded encrypted data
     * @return the decrypted data as a string
     * @throws Exception if a decryption error occurs
     */
    public static String decrypt(String encryptedData) throws Exception {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "Blowfish");

            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] decodedData = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedData = cipher.doFinal(decodedData);
            return new String(decryptedData, "UTF-8");
        } catch (Exception e) {
            throw new Exception("Error decrypting data: " + e.getMessage(), e);
        }
    }
}
