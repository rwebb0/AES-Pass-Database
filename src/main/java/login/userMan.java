package login;

import bgPass.passEncryption;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class userMan {
    private static final String USER_JSON_FILE_PATH = "src/main/resources/imp_files/user.json";
    private static passEncryption encryption;

    static {
        try {
            encryption = new passEncryption();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to read user data from the JSON file
    public static List<userValue> readUserDataFromFile(String fileLocation) throws Exception {
        Gson gson = new Gson();
        Type userListType = new TypeToken<ArrayList<userValue>>(){}.getType();
        List<userValue> userValues;

        try (FileReader reader = new FileReader(fileLocation)) {
            userValues = gson.fromJson(reader, userListType);
            if (userValues == null) {
                userValues = new ArrayList<>();
            }

            // Decrypt the user data
            for (userValue user : userValues) {
                String decryptedUsername = encryption.decrypt(user.getUsername().getBytes());
                String decryptedPassword = encryption.decrypt(user.getPassword().getBytes());
                user.setUsername(decryptedUsername);
                user.setPassword(decryptedPassword);
            }
        }

        return userValues;
    }

    // Method to write user data to the JSON file
    public static void writeUserDataToFile(List<userValue> userValues, String fileLocation) throws Exception {
        Gson gson = new Gson();

        // Encrypt the user data
        for (userValue user : userValues) {
            String encryptedUsername = new String(encryption.encrypt(user.getUsername()));
            String encryptedPassword = new String(encryption.encrypt(user.getPassword()));
            user.setUsername(encryptedUsername);
            user.setPassword(encryptedPassword);
        }

        try (FileWriter writer = new FileWriter(fileLocation)) {
            gson.toJson(userValues, writer);
        }
    }
}
