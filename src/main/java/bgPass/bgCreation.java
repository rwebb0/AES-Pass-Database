package bgPass;

import GUI.mainGUI;
import login.newAcc;

import javax.swing.*;
import java.io.*;
import java.security.NoSuchAlgorithmException;

public class bgCreation {
    public static final String PASSWORD_JSON_FILENAME = "password.json";
    public static final String USER_JSON_FILENAME = "user.json";
    public static final String DIRECTORY_PATH = "src/main/resources/imp_files/";

    File passwordFile = new File(DIRECTORY_PATH, PASSWORD_JSON_FILENAME);
    File userFile = new File(DIRECTORY_PATH, USER_JSON_FILENAME);

    private passEncryption encryption;

    public bgCreation() throws NoSuchAlgorithmException, IOException {
        encryption = new passEncryption();
    }


    public void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                encryption.updateEncryptionKey();  // Assuming updateEncryptionKey handles both password.json and user.json
                System.out.println("Encryption keys updated and data re-encrypted.");
            } catch (Exception e) {
                System.err.println("Error re-encrypting data on shutdown: " + e.getMessage());
            }
        }));
    }

    public void emptyFileCheck(){
        if (passwordFile.length() == 0){
            try {
                System.out.println("Wrote to password.json file.");
                String jsonFormat = "[\n" +
                        "  {\n" +
                        "    \"category\": \"\",\n" +
                        "    \"title\": \"\",\n" +
                        "    \"username\": \"\",\n" +
                        "    \"password\": \"\",\n" +
                        "    \"notes\": \"\",\n" +
                        "    \"last_updated\": \"\"\n" +
                        "  }\n" +
                        "]";

                try (FileWriter writer = new FileWriter(passwordFile)) {
                    writer.write(jsonFormat);
                }
            } catch (IOException e) {
                System.err.println("Error writing to password.json file: " + e.getMessage());
            }
        }
    }

    public void createFilesIfNotExist() {
        if (!passwordFile.exists()) {
            try {
                passwordFile.createNewFile();
                System.out.println("Created password.json file.");
                String jsonFormat = "[\n" +
                        "  {\n" +
                        "    \"category\": \"\",\n" +
                        "    \"title\": \"\",\n" +
                        "    \"username\": \"\",\n" +
                        "    \"password\": \"\",\n" +
                        "    \"notes\": \"\",\n" +
                        "    \"last_updated\": \"\"\n" +
                        "  }\n" +
                        "]";

                try (FileWriter writer = new FileWriter(passwordFile)) {
                    writer.write(jsonFormat);
                }
            } catch (IOException e) {
                System.err.println("Error creating password.json file: " + e.getMessage());
            }
        } else {
            System.out.println("password.json file already exists.");
        }

        if (!userFile.exists()) {
            try {
                userFile.createNewFile();
                System.out.println("Created user.json file.");

                // Launch the newAcc class for creating a new account
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        new newAcc(); // Create newAcc instance without arguments
                    }
                });

            } catch (IOException e) {
                System.err.println("Error creating user.json file: " + e.getMessage());
            }
        } else {
            System.out.println("user.json file already exists.");
            launchMainGUI();
        }
    }

    // Method to launch the mainGUI class
    public static void launchMainGUI() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    new mainGUI();
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public static void main(String[] args) {
        try {
            bgCreation bg = new bgCreation();
            bg.addShutdownHook();
            bg.emptyFileCheck();
            bg.createFilesIfNotExist();
        } catch (NoSuchAlgorithmException | IOException e) {
            System.err.println("Error initializing bgCreation: " + e.getMessage());
        }
    }

}

