package bgPass;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javax.crypto.SecretKey;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static bgPass.bgCreation.PASSWORD_JSON_FILENAME;

public class passMan {

    public void addAccount(String title, String username, String password, String notes, String lastUpdated, String category) throws Exception {
        try {
            passEncryption encryption = new passEncryption();
            List<passValues> accounts;
            String fileLocation = "src/main/resources/imp_files/password.json";
            accounts = readDataFromFile(fileLocation);

            // Encrypt the individual fields
            title = Base64.getEncoder().encodeToString(encryption.encrypt(title));
            username = Base64.getEncoder().encodeToString(encryption.encrypt(username));
            password = Base64.getEncoder().encodeToString(encryption.encrypt(password));
            notes = Base64.getEncoder().encodeToString(encryption.encrypt(notes));
            lastUpdated = Base64.getEncoder().encodeToString(encryption.encrypt(lastUpdated));
            category = Base64.getEncoder().encodeToString(encryption.encrypt(category));

            passValues newAccount = new passValues(title, username, password, notes, lastUpdated, category);
            accounts.add(newAccount);

            // Directly pass the accounts list to writeDataToFile
            writeDataToFile(accounts, fileLocation);
        } catch (Exception e){
            System.err.print("Error within addAccount" + e.getMessage());
        }
    }

    public static List<passValues> readDataFromFile(String fileLocation) throws IOException, NoSuchAlgorithmException {
        passEncryption encryption = new passEncryption();
        List<passValues> accounts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileLocation))) {
            String fileContent = reader.readLine(); // Read the entire data from the file

            if (fileContent == null || fileContent.isEmpty()) {
                System.out.println("No data in file");
                return new ArrayList<>();
            }

            if (fileContent.trim().startsWith("[")) {
                // Data is plain JSON, parse it directly
                Gson gson = new GsonBuilder().create();
                Type listType = new TypeToken<List<passValues>>() {}.getType();
                accounts = gson.fromJson(fileContent, listType);
            } else {
                // Data is assumed to be encrypted and Base64-encoded
                try {
                    byte[] decodedBytes = Base64.getDecoder().decode(fileContent);
                    String decryptedData = encryption.decrypt(decodedBytes);

                    Gson gson = new GsonBuilder().create();
                    Type listType = new TypeToken<List<passValues>>() {}.getType();
                    accounts = gson.fromJson(decryptedData, listType);
                } catch (IllegalArgumentException e) {
                    System.err.println("Error in Base64 decoding: " + e.getMessage());
                    e.printStackTrace();
                } catch (Exception e) {
                    System.err.println("Error decrypting data: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            if (accounts == null) {
                accounts = new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    // Method to serialize the list of passValues to JSON and write to a file
    public static void writeDataToFile(List<passValues> entries, String filename) throws IOException {
        Gson gson = new Gson();
        try (Writer writer = new FileWriter(filename)) {
            gson.toJson(entries, writer);
        } catch (IOException e) {
            // Handle the exception, possibly rethrowing it or logging it
            throw e;
        }
    }



    // Method to update an existing entry with new encrypted values, including category
    public static void updateEntry(String encryptedTitle, String encryptedUsername, String encryptedPassword, String encryptedNotes, String encryptedCategory) throws Exception {
        passEncryption encryption = new passEncryption();
        List<passValues> entries = readDataFromFile(bgCreation.DIRECTORY_PATH + PASSWORD_JSON_FILENAME);

        for (passValues entry : entries) {
            String decryptedTitle = decryptField(entry.getTitle());
            if (decryptedTitle.equals(new String(encryption.decrypt(Base64.getDecoder().decode(encryptedTitle)).getBytes(), StandardCharsets.UTF_8))) {
                entry.setTitle(encryptedTitle);
                entry.setUsername(encryptedUsername);
                entry.setPassword(encryptedPassword);
                entry.setNotes(encryptedNotes);
                entry.setCategory(encryptedCategory); // Update the category here
                break;
            }
        }

        // Write the updated list back to the file
        writeDataToFile(entries, bgCreation.DIRECTORY_PATH + PASSWORD_JSON_FILENAME);
    }


    // Method to delete an entry by its encrypted title
    public static void deleteEntry(String encryptedTitle) throws Exception {
        passEncryption encryption = new passEncryption();
        List<passValues> entries = readDataFromFile(bgCreation.DIRECTORY_PATH + PASSWORD_JSON_FILENAME);
        entries.removeIf(entry -> {
            String decryptedTitle = null;
            try {
                decryptedTitle = decryptField(entry.getTitle());
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                return decryptedTitle.equals(new String(encryption.decrypt(Base64.getDecoder().decode(encryptedTitle)).getBytes(), StandardCharsets.UTF_8));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        writeDataToFile(entries, bgCreation.DIRECTORY_PATH + PASSWORD_JSON_FILENAME);
    }

    private static String decryptField(String base64EncodedEncryptedData) throws NoSuchAlgorithmException, IOException {
        passEncryption encryption = new passEncryption();
        if (base64EncodedEncryptedData == null || base64EncodedEncryptedData.isEmpty()) {
            return "";
        }
        try {
            byte[] encryptedBytes = Base64.getDecoder().decode(base64EncodedEncryptedData);
            if (encryptedBytes.length < 12) {
                throw new IllegalArgumentException("Insufficient data for IV.");
            }
            return encryption.decrypt(encryptedBytes);
        } catch (Exception e) {
            System.err.println("Error decrypting data: " + e.getMessage());
            return "Decryption Error";
        }
    }

}
