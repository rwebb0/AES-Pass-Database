package login;

import bgPass.bgCreation;
import com.google.gson.Gson;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;

import bgPass.passEncryption; // Import passEncryption class
import GUI.mainGUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class newAcc extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    public newAcc() {
        // Set FlatLaf Dark Look and Feel
        FlatLaf.install(new FlatDarkLaf());
        // Set up the JFrame for creating a new account
        setTitle("Create New Account");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);

        // Create components
        JPanel panel = new JPanel(new GridLayout(4, 2));
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        JButton createAccountButton = new JButton("Create Account");

        // Add components to the panel with empty borders for spacing
        panel.add(createBorderedPanel(usernameLabel));
        panel.add(createBorderedPanel(usernameField));
        panel.add(createBorderedPanel(passwordLabel));
        panel.add(createBorderedPanel(passwordField));
        panel.add(createBorderedPanel(confirmPasswordLabel));
        panel.add(createBorderedPanel(confirmPasswordField));
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(createBorderedPanel(createAccountButton));

        // Add ActionListener to the create account button
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAccount();
            }
        });

        add(panel);
        setVisible(true);
    }

    private void createAccount() {
        try {
            // Retrieve entered username and password
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            // Check if passwords match
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.");
                return;
            }

            // Save user data in plain text to JSON file first
            userValue user = new userValue(username, password);
            Gson gson = new Gson();
            String json = gson.toJson(user);

            try (FileWriter writer = new FileWriter(bgCreation.DIRECTORY_PATH + bgCreation.USER_JSON_FILENAME)) {
                writer.write(json);
            }

            // Read, encrypt and encode the file content
            encryptAndSaveFile(bgCreation.DIRECTORY_PATH + bgCreation.USER_JSON_FILENAME);

            JOptionPane.showMessageDialog(this, "Account successfully created.");
            dispose();

            openMainGUI();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error creating account.");
        }
    }

    private void openMainGUI() {
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new mainGUI().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void encryptAndSaveFile(String filePath) throws Exception {
        passEncryption encryption = new passEncryption();
        String fileContent = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);

        byte[] encryptedBytes = encryption.encrypt(fileContent);
        String base64Encoded = Base64.getEncoder().encodeToString(encryptedBytes);

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(base64Encoded);
        } catch (IllegalStateException e){
            System.out.println("Error encrypting.");
        }
    }

    // Create a bordered panel to add separation
    private JPanel createBorderedPanel(Component component) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(component, BorderLayout.CENTER);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Adjust padding as needed
        return panel;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new newAcc();
            }
        });
    }
}
