package login;

import bgPass.bgCreation;
import bgPass.passEncryption;
import com.google.gson.Gson;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class loginLogic {
    private static userValue user;
    private passEncryption encryption; // Instance of passEncryption

    public loginLogic() {
        try {
            encryption = new passEncryption();
            Gson gson = new Gson();

            // Read the encrypted and encoded file content
            String filePath = bgCreation.DIRECTORY_PATH + bgCreation.USER_JSON_FILENAME;
            String encryptedContent = new String(Files.readAllBytes(Paths.get(filePath)));

            // Decode from Base64 and then decrypt
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedContent);
            String decryptedJson = encryption.decrypt(decodedBytes);

            // Parse the decrypted JSON using Gson
            user = gson.fromJson(decryptedJson, userValue.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean loginUser(String username, String password) {
        // Check if the user object is null (no user data loaded)
        if (user == null) {
            System.out.println("No user data found. Please set a user first.");
            return false;
        }

        // Check if the entered username and password match the decrypted values
        if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
            System.out.println("Login successful!");
            return true;
        } else {
            System.out.println("Invalid credentials. Login failed.");
            return false;
        }
    }

    public static void saveUserData(String username, String password) {
        // Encrypt and encode the username and password
        try {
            passEncryption encryption = new passEncryption(); // Initialize passEncryption
            // Encrypt then encode to Base64
            String encryptedUsername = Base64.getEncoder().encodeToString(encryption.encrypt(username));
            String encryptedPassword = Base64.getEncoder().encodeToString(encryption.encrypt(password));


            // Create a new userValue object with encrypted values
            user = new userValue(encryptedUsername, encryptedPassword);

            // Serialize and save the user data to the JSON file using Gson
            Gson gson = new Gson();
            try (FileWriter writer = new FileWriter(bgCreation.DIRECTORY_PATH + bgCreation.USER_JSON_FILENAME)) {
                gson.toJson(user, writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
