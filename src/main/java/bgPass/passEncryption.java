package bgPass;

import login.userMan;
import login.userValue;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

import static bgPass.bgCreation.*;

public class passEncryption {
    private SecretKey key;
    private final int KEY_SIZE = 256; // Key size for AES encryption
    private final int T_LEN = 128; // Tag length for GCM mode
    private Cipher encryptionCipher;
    private static final String KEY_FILE_PATH = "src/main/resources/imp_files/encryption_0.key"; // Path for storing the encryption key

    // Constructor for passEncryption class
    public passEncryption() throws NoSuchAlgorithmException, IOException {
        this.key = getKey();
    }

    // Method to get the encryption key, either from file or generate a new one
    SecretKey getKey() throws NoSuchAlgorithmException, IOException {
        File keyFile = new File(KEY_FILE_PATH);
        if (keyFile.exists()) {
            // If key file exists, read the key from file
            return readKeyFromFile(KEY_FILE_PATH);
        } else {
            // If key file doesn't exist, generate a new key and save it to file
            SecretKey newKey = generateKey();
            saveKeyToFile(newKey, KEY_FILE_PATH);
            return newKey;
        }
    }

    // Method to generate a new AES encryption key
    SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(KEY_SIZE);
        return keyGen.generateKey();
    }

    // Method to save the encryption key to a file
    private void saveKeyToFile(SecretKey key, String filePath) throws IOException {
        byte[] encoded = key.getEncoded();
        String encodedKey = Base64.getEncoder().encodeToString(encoded);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(encodedKey);
        }
    }

    // Method to read the encryption key from a file
    private SecretKey readKeyFromFile(String filePath) throws IOException {
        byte[] encoded;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String encodedKey = reader.readLine();
            encoded = Base64.getDecoder().decode(encodedKey);
        }
        return new SecretKeySpec(encoded, 0, encoded.length, "AES");
    }

    // Method to encrypt a message
    public byte[] encrypt(String message) throws Exception {
        Cipher encryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");

        // Generate a random IV
        SecureRandom random = new SecureRandom();
        byte[] iv = new byte[12]; // 12 bytes IV for GCM
        random.nextBytes(iv);

        // Initialize the encryptionCipher with the generated IV
        encryptionCipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(T_LEN, iv));

        byte[] messageInByte = message.getBytes(StandardCharsets.UTF_8);

        byte[] encryptedData = encryptionCipher.doFinal(messageInByte);

        // Combine IV and encrypted data into a single byte array
        ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + encryptedData.length);
        byteBuffer.put(iv);
        byteBuffer.put(encryptedData);

        System.out.println("Encrypted Data: " + Base64.getEncoder().encodeToString(byteBuffer.array()) + "\n");

        return byteBuffer.array();
    }


    // Method to decrypt a message
    public String decrypt(byte[] encryptedBytes) throws Exception {
        if (encryptedBytes == null || encryptedBytes.length < 12) {
            throw new IllegalArgumentException("Insufficient data for IV.");
        }

        try {
            Cipher decryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");

            ByteBuffer buffer = ByteBuffer.wrap(encryptedBytes);
            byte[] iv = new byte[12]; // Assuming 12-byte IV
            buffer.get(iv);
            byte[] actualEncryptedData = new byte[buffer.remaining()];
            buffer.get(actualEncryptedData);

            GCMParameterSpec spec = new GCMParameterSpec(T_LEN, iv);
            decryptionCipher.init(Cipher.DECRYPT_MODE, key, spec);

            try {
                byte[] decryptedBytes = decryptionCipher.doFinal(actualEncryptedData);
                return new String(decryptedBytes, StandardCharsets.UTF_8);
            } catch (AEADBadTagException e) {
                throw e; // Re-throw the exception to maintain the original error flow
            }
        } catch (BufferUnderflowException | IllegalArgumentException e) {
            // This might indicate that the data is not encrypted or in an unexpected format
            System.err.println("Error in decrypting data: " + e.getMessage());
            throw e;
        }
    }


    public void updateEncryptionKey() throws Exception {
        SecretKey newKey = generateKey();
        int newKeyVersion = getCurrentKeyVersion() + 1;

        // Save the new key temporarily
        saveKeyToFile(newKey, getKeyFilePath(newKeyVersion));

        // Re-encrypt password entries
        List<passValues> passEntries = passMan.readDataFromFile(DIRECTORY_PATH + PASSWORD_JSON_FILENAME);
        reEncryptEntries(passEntries, newKey, newKeyVersion);

        // Re-encrypt user entries
        List<userValue> userEntries = userMan.readUserDataFromFile(DIRECTORY_PATH + USER_JSON_FILENAME);
        reEncryptUserEntries(userEntries, newKey, newKeyVersion);

        // After successful re-encryption, update the system to use the new key
        this.key = newKey;
        saveKeyToFile(newKey, KEY_FILE_PATH);
        saveCurrentKeyVersion(newKeyVersion);
    }

    private void reEncryptEntries(List<passValues> entries, SecretKey newKey, int newKeyVersion) throws Exception {
        for (passValues entry : entries) {
            String decryptedData = entry.getEncryptedData() != null ? decryptWithKey(Base64.getDecoder().decode(entry.getEncryptedData()), this.key) : "";
            if (!decryptedData.isEmpty()) {
                byte[] reEncryptedData = encryptWithKey(newKey, decryptedData);
                entry.setEncryptedData(Base64.getEncoder().encodeToString(reEncryptedData).getBytes());
                entry.setKeyVersion(newKeyVersion);
            }

        }
        passMan.writeDataToFile(entries, DIRECTORY_PATH + PASSWORD_JSON_FILENAME);
    }

    private void reEncryptUserEntries(List<userValue> entries, SecretKey newKey, int newKeyVersion) throws Exception {
        for (userValue user : entries) {
            String decryptedData = decryptWithKey(Base64.getDecoder().decode(user.getEncryptedData()), this.key);
            byte[] reEncryptedData = encryptWithKey(newKey, decryptedData);
            user.setEncryptedData(Base64.getEncoder().encodeToString(reEncryptedData));
            user.setKeyVersion(newKeyVersion);
        }
        userMan.writeUserDataToFile(entries, DIRECTORY_PATH + USER_JSON_FILENAME);
    }



    public String decryptWithKey(byte[] encryptedBytes, SecretKey key) throws Exception {
        if (encryptedBytes == null || encryptedBytes.length == 0) {
            System.out.println("Attempted to decrypt null or empty encrypted data");
            return "";  // Return an empty string if the input is null or empty
        }
        Cipher decryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");

        ByteBuffer byteBuffer = ByteBuffer.wrap(encryptedBytes);
        byte[] iv = new byte[12]; // GCM nonce is 12 bytes
        byteBuffer.get(iv);
        byte[] encryptedData = new byte[byteBuffer.remaining()];
        byteBuffer.get(encryptedData);

        GCMParameterSpec spec = new GCMParameterSpec(T_LEN, iv);
        decryptionCipher.init(Cipher.DECRYPT_MODE, key, spec);

        byte[] decryptedData = decryptionCipher.doFinal(encryptedData);
        return new String(decryptedData, StandardCharsets.UTF_8);
    }


    private byte[] encryptWithKey(SecretKey key, String data) throws Exception {
        if (data == null) {
            System.out.println("Attempted to encrypt null data");
            return null; // or handle it in another way that suits your application
        }
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        SecureRandom random = new SecureRandom();
        byte[] iv = new byte[12];
        random.nextBytes(iv);
        GCMParameterSpec spec = new GCMParameterSpec(T_LEN, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        // Log the new encrypted data for debugging
        System.out.println("Re-encrypted Data: " + Base64.getEncoder().encodeToString(encryptedData));

        ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + encryptedData.length);
        byteBuffer.put(iv);
        byteBuffer.put(encryptedData);
        return byteBuffer.array();
    }


    private int getCurrentKeyVersion() throws IOException {
        File versionFile = new File(DIRECTORY_PATH, "key_version");
        if (!versionFile.exists()) {
            return 0; // If the version file doesn't exist, assume it's the initial version
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(versionFile))) {
            String versionString = reader.readLine();
            return Integer.parseInt(versionString);
        } catch (NumberFormatException e) {
            throw new IOException("Invalid key version", e);
        }
    }

    private void saveCurrentKeyVersion(int version) throws IOException {
        File versionFile = new File(DIRECTORY_PATH, "key_version");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(versionFile))) {
            writer.write(Integer.toString(version));
        }
    }

    private String getKeyFilePath(int version) {
        return DIRECTORY_PATH + "encryption_" + version + ".key";
    }

}
