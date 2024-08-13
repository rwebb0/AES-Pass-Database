package bgPass;

import org.junit.jupiter.api.*;
import javax.crypto.SecretKey;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class passEncryptionTest {
    passEncryption encryptionUtil;
    SecretKey originalKey;

    @BeforeEach
    void setUp() throws Exception {
        encryptionUtil = new passEncryption();
        originalKey = encryptionUtil.getKey(); // Assume a method to expose the key for testing purposes
    }

    @Test
    void encryptAndDecrypt_ReturnsOriginalString() throws Exception {
        String originalMessage = "Test message";
        byte[] encrypted = encryptionUtil.encrypt(originalMessage);
        String decrypted = encryptionUtil.decrypt(encrypted);

        assertEquals(originalMessage, decrypted);
    }

    @Test
    void encryptAndDecryptWithModifiedData_ThrowsException() {
        String originalMessage = "Test message";
        Assertions.assertThrows(Exception.class, () -> {
            byte[] encrypted = encryptionUtil.encrypt(originalMessage);
            // Simulate data corruption
            encrypted[5] ^= 1;
            encryptionUtil.decrypt(encrypted);
        });
    }

    @Test
    void decryptWithDifferentKey_ThrowsException() throws Exception {
        Assertions.assertThrows(Exception.class, () -> {
            String originalMessage = "Test message";
            byte[] encrypted = encryptionUtil.encrypt(originalMessage);

            // Generate a new key
            SecretKey newKey = encryptionUtil.generateKey();

            // Try to decrypt the data encrypted with the original key using the new key
            encryptionUtil.decryptWithKey(encrypted, newKey);
        });
    }

    @Test
    void decryptWithInsufficientData_ThrowsIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            encryptionUtil.decrypt(new byte[10]); // Less than 12 bytes for the IV
        });
    }

    @AfterEach
    void tearDown() {
        // Reset any changes that tests may have made
    }
}
