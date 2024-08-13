package GUI;

import bgPass.bgCreation;
import bgPass.passEncryption;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import login.loginLogic;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class mainGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton minimizeButton;

    int posX=0, posY=0;

    public mainGUI() throws NoSuchAlgorithmException, IOException {

        // Set FlatLaf look and feel (dark theme)
        FlatLaf.setup(new FlatDarkLaf());
        // Set up the JFrame
        setTitle("Login");
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(new Color(0,0,0,0));
        setSize(900, 640);
        setLocationRelativeTo(null);

        // Create the login panel
        loginGUID loginPanel = new loginGUID();
        add(loginPanel);

        // Initialize components
        usernameField = loginPanel.getUsernameField();
        passwordField = loginPanel.getPasswordField();
        loginButton = loginPanel.getLoginButton();
        minimizeButton = loginPanel.getMinimizeButton();
        setVisible(true);

        minimizeButton.addActionListener(e -> setState(ICONIFIED));

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            char[] passwordChars = passwordField.getPassword();
            String password = new String(passwordChars);

            if (isValidLogin(username, password)) {
                try {
                    passEncryption encryption = new passEncryption();
                    String fileContent = new String(Files.readAllBytes(Paths.get(bgCreation.DIRECTORY_PATH + bgCreation.PASSWORD_JSON_FILENAME)), StandardCharsets.UTF_8);
                    if (fileContent.startsWith("[")){
                        byte[] encryptedBytes = encryption.encrypt(fileContent);
                        String base64Encoded = Base64.getEncoder().encodeToString(encryptedBytes);
                        try (FileWriter writer = new FileWriter(bgCreation.DIRECTORY_PATH + bgCreation.PASSWORD_JSON_FILENAME)) {
                            writer.write(base64Encoded);
                        } catch (IllegalStateException J){
                            System.out.println("Error encrypting.");
                        }
                        openPasswordManagerGUI();
                    } else {
                        openPasswordManagerGUI();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(mainGUI.this, "Error decrypting data.");
                }
            } else {
                JOptionPane.showMessageDialog(mainGUI.this, "Invalid credentials. Login failed.");
            }
        });

        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                posX = e.getX();
                posY = e.getY();
            }
        });

        this.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent evt) {
                // Sets frame position when mouse dragged
                setLocation(evt.getXOnScreen() - posX, evt.getYOnScreen() - posY);
            }
        });

        // Display the JFrame
        setVisible(true);
    }

    // Create an instance of loginLogic
    login.loginLogic loginLogic = new loginLogic();

    private boolean isValidLogin(String username, String password) {
        return loginLogic.loginUser(username, password);
    }

    // Open the PasswordManagerGUI
    private void openPasswordManagerGUI() {
        dispose(); // Close the login GUI
        SwingUtilities.invokeLater(() -> {
            try {
                new passManGUI();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new mainGUI();
            } catch (NoSuchAlgorithmException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
