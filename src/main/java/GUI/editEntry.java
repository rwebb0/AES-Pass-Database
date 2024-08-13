package GUI;

import bgPass.bgCreation;
import bgPass.passEncryption;
import bgPass.passMan;
import bgPass.passValues;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

public class editEntry extends javax.swing.JFrame implements PasswordReceiver {
    private EntryUpdateListener updateListener;
    int posX=0,posY=0;
    passEncryption encryption = new passEncryption();
    boolean isPassRevealed = false;

    public editEntry(String title, String username) throws Exception {
        initComponents(title, username);
    }

    public void setUpdateListener(EntryUpdateListener updateListener) {
        this.updateListener = updateListener;
    }

    // Call this method when an entry is updated
    private void notifyEntryUpdated() {
        if (updateListener != null) {
            updateListener.onEntryUpdated();
        }
    }

    // Call this method when an entry is deleted
    private void notifyEntryDeleted() {
        if (updateListener != null) {
            updateListener.onEntryDeleted();
        }
    }

    public void initComponents(String title, String username) throws Exception {
        // Get and set notes for the given title
        String notes = getNotesForTitle(title);
        JButton openPasswordGen = new JButton();
        JButton deleteEntryButton = new JButton();
        JButton saveEntryButton = new JButton();
        revealPassword = new javax.swing.JButton();
        JLabel programTitle = new JLabel();
        JLabel titleLabel = new JLabel();
        titleField = new javax.swing.JTextField();
        JLabel usernameLabel = new JLabel();
        usernameField = new javax.swing.JTextField();
        JLabel passwordLabel = new JLabel();
        passwordField = new javax.swing.JTextField();
        JLabel repeatLabel = new JLabel();
        repeatedPasswordField = new javax.swing.JTextField();
        JLabel notesLabel = new JLabel();
        JScrollPane notesArea = new JScrollPane();
        notesTextArea = new javax.swing.JTextArea();
        JButton minimizeButton = new JButton();
        // Variables declaration - do not modify
        JButton closeButton1 = new JButton();
        JLabel jLabel1 = new JLabel();

        // Set FlatLaf look and feel (dark theme)
        FlatLaf.setup(new FlatDarkLaf());
        // Set up the JFrame
        setTitle("Edit Entry");
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBackground(new Color(0,0,0,0));
        setSize(500, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        openPasswordGen.setBackground(new java.awt.Color(101, 75, 216));
        openPasswordGen.setFont(new java.awt.Font("Trebuchet MS", Font.BOLD, 12)); // NOI18N
        openPasswordGen.setForeground(new java.awt.Color(255, 255, 255));
        openPasswordGen.setText("\u2699");
        openPasswordGen.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        openPasswordGen.setBorderPainted(false);
        openPasswordGen.addActionListener(this::openPasswordGenActionPerformed);
        getContentPane().add(openPasswordGen, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 250, 40, 20));

        deleteEntryButton.setBackground(new java.awt.Color(101, 75, 216));
        deleteEntryButton.setFont(new java.awt.Font("Trebuchet MS", Font.BOLD, 12)); // NOI18N
        deleteEntryButton.setForeground(new java.awt.Color(255, 255, 255));
        deleteEntryButton.setText("Delete Entry");
        deleteEntryButton.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        deleteEntryButton.setBorderPainted(false);
        deleteEntryButton.addActionListener(e -> {
            try {
                deleteEntry();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        getContentPane().add(deleteEntryButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 460, 90, 30));

        saveEntryButton.setBackground(new java.awt.Color(101, 75, 216));
        saveEntryButton.setFont(new java.awt.Font("Trebuchet MS", Font.BOLD, 12)); // NOI18N
        saveEntryButton.setForeground(new java.awt.Color(255, 255, 255));
        saveEntryButton.setText("Save Entry");
        saveEntryButton.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        saveEntryButton.setBorderPainted(false);
        saveEntryButton.addActionListener(e -> {
            try {
                saveEntryButtonActionPerformed();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        getContentPane().add(saveEntryButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 460, 90, 30));

        revealPassword.setBackground(new java.awt.Color(101, 75, 216));
        revealPassword.setFont(new java.awt.Font("Trebuchet MS", Font.BOLD, 12)); // NOI18N
        revealPassword.setForeground(new java.awt.Color(255, 255, 255));
        revealPassword.setText("•••");
        revealPassword.setToolTipText("Reveal Password");
        revealPassword.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        revealPassword.setBorderPainted(false);
        revealPassword.addActionListener(e -> {
            isPassRevealed = !isPassRevealed;
            if (isPassRevealed) {
                revealPassword.setText("Hide"); // Change text to indicate action
                setRevealPassword(); // Reveal the password
            } else {
                revealPassword.setText("•••"); // Change text back to original
                setRevealPassword(); // Hide the password
            }
        });
        getContentPane().add(revealPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 200, 40, 20));

        programTitle.setFont(new java.awt.Font("Trebuchet MS", Font.BOLD, 12)); // NOI18N
        programTitle.setForeground(new java.awt.Color(255, 255, 255));
        programTitle.setText("Editing Entry");
        getContentPane().add(programTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        titleLabel.setFont(new java.awt.Font("Trebuchet MS", Font.PLAIN, 14)); // NOI18N
        titleLabel.setForeground(new java.awt.Color(255, 255, 255));
        titleLabel.setText("Title");
        getContentPane().add(titleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));

        titleField.setBackground(new java.awt.Color(57, 51, 77));
        titleField.setForeground(new java.awt.Color(255, 255, 255));
        titleField.setText(title);
        titleField.setBorder(null);
        getContentPane().add(titleField, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 460, 20));

        usernameLabel.setFont(new java.awt.Font("Trebuchet MS", Font.PLAIN, 14)); // NOI18N
        usernameLabel.setForeground(new java.awt.Color(255, 255, 255));
        usernameLabel.setText("Username");
        getContentPane().add(usernameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, -1, -1));

        usernameField.setBackground(new java.awt.Color(57, 51, 77));
        usernameField.setForeground(new java.awt.Color(255, 255, 255));
        usernameField.setBorder(null);
        usernameField.setText(username);
        getContentPane().add(usernameField, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 460, 20));

        passwordLabel.setFont(new java.awt.Font("Trebuchet MS", Font.PLAIN, 14)); // NOI18N
        passwordLabel.setForeground(new java.awt.Color(255, 255, 255));
        passwordLabel.setText("Password");
        getContentPane().add(passwordLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, -1, -1));

        passwordField.setBackground(new java.awt.Color(57, 51, 77));
        passwordField.setForeground(new java.awt.Color(255, 255, 255));
        passwordField.setBorder(null);
        passwordField.setText("•••••••");
        getContentPane().add(passwordField, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 410, 20));
        passwordField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateRepeatedPasswordField();
            }
            public void removeUpdate(DocumentEvent e) {
                updateRepeatedPasswordField();
            }
            public void changedUpdate(DocumentEvent e) {
                // Plain text components do not fire these events
            }
            private void updateRepeatedPasswordField() {
                // This will copy the text from the password field to the repeated password field
                if (isPassRevealed) {
                    repeatedPasswordField.setText((passwordField.getText()));
                } else {
                    // When the password is hidden, only update if the length is not equal to the placeholder length
                    if (passwordField.getText().length() != 7 || !passwordField.getText().equals("•••••••")) {
                        repeatedPasswordField.setText(passwordField.getText());
                    }
                }
            }
        });

        repeatLabel.setFont(new java.awt.Font("Trebuchet MS", Font.PLAIN, 14)); // NOI18N
        repeatLabel.setForeground(new java.awt.Color(255, 255, 255));
        repeatLabel.setText("Repeat");
        getContentPane().add(repeatLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, -1, -1));

        repeatedPasswordField.setBackground(new java.awt.Color(57, 51, 77));
        repeatedPasswordField.setForeground(new java.awt.Color(255, 255, 255));
        repeatedPasswordField.setBorder(null);
        repeatedPasswordField.setText("•••••••");
        repeatedPasswordField.setEditable(true);
        getContentPane().add(repeatedPasswordField, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 410, 20));

        notesLabel.setFont(new java.awt.Font("Trebuchet MS", Font.PLAIN, 14)); // NOI18N
        notesLabel.setForeground(new java.awt.Color(255, 255, 255));
        notesLabel.setText("Notes");
        getContentPane().add(notesLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, -1, -1));

        notesArea.setBorder(null);
        notesArea.setAutoscrolls(true);
        notesArea.setHorizontalScrollBar(null);

        notesTextArea.setBackground(new java.awt.Color(57, 51, 77));
        notesTextArea.setColumns(20);
        notesTextArea.setForeground(new java.awt.Color(255, 255, 255));
        notesTextArea.setRows(5);
        notesTextArea.setText(notes);
        notesTextArea.setBorder(null);
        notesArea.setViewportView(notesTextArea);

        getContentPane().add(notesArea, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, 460, 110));

        minimizeButton.setBackground(new java.awt.Color(101, 75, 216,0));
        minimizeButton.setOpaque(false);
        minimizeButton.setFont(new java.awt.Font("Trebuchet MS", Font.BOLD, 24)); // NOI18N
        minimizeButton.setForeground(new java.awt.Color(255, 255, 255));
        minimizeButton.setText("-");
        minimizeButton.setBorder(null);
        minimizeButton.addActionListener(this::minimizeButtonActionPerformed);
        getContentPane().add(minimizeButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 10, 30, 10));

        closeButton1.setBackground(new java.awt.Color(0,0,0,0));
        closeButton1.setOpaque(false);
        closeButton1.setFont(new java.awt.Font("Trebuchet MS", Font.BOLD, 14)); // NOI18N
        closeButton1.setForeground(new java.awt.Color(255, 255, 255));
        closeButton1.setText("X");
        closeButton1.setBorder(null);
        closeButton1.addActionListener(e -> dispose());
        getContentPane().add(closeButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 6, 20, 20));

        jLabel1.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/images/editEntryDT.png")))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        this.addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                posX=e.getX();
                posY=e.getY();
            }
        });

        this.addMouseMotionListener(new MouseAdapter()
        {
            public void mouseDragged(MouseEvent evt)
            {
                //sets frame position when mouse dragged
                setLocation (evt.getXOnScreen()-posX,evt.getYOnScreen()-posY);

            }
        });


        pack();
    }

    private String getNotesForTitle(String title) throws Exception {
        // Read data from JSON file
        List<passValues> entries = passMan.readDataFromFile(bgCreation.DIRECTORY_PATH + bgCreation.PASSWORD_JSON_FILENAME);
        if (entries != null) {
            for (passValues entry : entries) {
                // Decrypt and decode the title to check for a match
                if (decryptField(entry.getTitle()).equals(title)) {
                    // Return the decrypted notes
                    return decryptField(entry.getNotes());
                }
            }
        }
        return "Notes not found"; // Return a default message if not found
    }

    private String decryptField(String base64EncodedEncryptedData) {
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

    public void deleteEntry() throws Exception {
        // Confirm deletion with the user
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this entry?");
        if (confirm == JOptionPane.YES_OPTION) {
            // Encrypt the title to find the matching entry
            String encryptedTitle = Base64.getEncoder().encodeToString(encryption.encrypt(titleField.getText()));

            // Call a method from passMan to delete the entry, passing the encrypted title
            passMan.deleteEntry(encryptedTitle);

            notifyEntryDeleted();
            // Close the edit window
            dispose();
        }
    }

    private void saveEntryButtonActionPerformed() throws Exception {
        // Retrieve the values from the text fields
        String newTitle = titleField.getText();
        String newUsername = usernameField.getText();
        String newPassword;
        String newNotes = notesTextArea.getText();

        // Check if passwordField contains bullets to decide whether to decrypt the existing password or use the new one
        if (passwordField.getText().contains("•")) {
            newPassword = decryptField(getEncryptedPassword()); // Existing password remains unchanged
        } else {
            newPassword = passwordField.getText(); // New password provided by the user
        }

        // Encrypt the new values
        String encryptedTitle = Base64.getEncoder().encodeToString(encryption.encrypt(newTitle));
        String encryptedUsername = Base64.getEncoder().encodeToString(encryption.encrypt(newUsername));
        String encryptedPassword = Base64.getEncoder().encodeToString(encryption.encrypt(newPassword));
        String encryptedNotes = Base64.getEncoder().encodeToString(encryption.encrypt(newNotes));
        String encryptedCategory = getEncryptedCategoryForEntry(titleField.getText());


        // Call a method from passMan to update the entry
        passMan.updateEntry(encryptedTitle, encryptedUsername, encryptedPassword, encryptedNotes, encryptedCategory);

        // Confirm the entry has been saved
        JOptionPane.showMessageDialog(this, "Entry saved successfully.");
        notifyEntryUpdated();
    }

    private String getEncryptedCategoryForEntry(String title) throws IOException, NoSuchAlgorithmException {
        // Read data from JSON file
        List<passValues> entries = passMan.readDataFromFile(bgCreation.DIRECTORY_PATH + bgCreation.PASSWORD_JSON_FILENAME);
        if (entries != null) {
            for (passValues entry : entries) {
                // Check for a match with the encrypted title
                if (decryptField(entry.getTitle()).equals(title)) {
                    // Return the encrypted category directly
                    return entry.getCategory();
                }
            }
        }
        return null; // Return null or handle appropriately if not found
    }


    private void openPasswordGenActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String decryptedPassword = decryptField(getEncryptedPassword());
            passGenUI passGenUI = new passGenUI();
            passGenUI.receivePassword(decryptedPassword);
            passGenUI.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error opening Password Generator: " + ex.getMessage());
        }
    }

    private void setRevealPassword() {
        if (isPassRevealed) {
            // Retrieve and decrypt the password, then display it
            try {
                String decryptedPassword = decryptField(getEncryptedPassword());
                passwordField.setText(decryptedPassword);
                repeatedPasswordField.setText(decryptedPassword);
                repeatedPasswordField.setEditable(false);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            // Hide the password with a placeholder
            passwordField.setText("•••••••");
            repeatedPasswordField.setText("•••••••");
        }
    }

    private String getEncryptedPassword() throws NoSuchAlgorithmException, IOException {
        List<passValues> entry = passMan.readDataFromFile(bgCreation.DIRECTORY_PATH + bgCreation.PASSWORD_JSON_FILENAME);
        String entryTitle = titleField.getText();
        for (passValues entries : entry) {
            if (decryptField(entries.getTitle()).equals(entryTitle)) {
                return entries.getPassword();
            }
        }
        return null; // Handle the case where the password is not found
    }

    private void minimizeButtonActionPerformed(java.awt.event.ActionEvent evt) {
        setState(ICONIFIED);
    }

    private static javax.swing.JTextArea notesTextArea;
    private static javax.swing.JTextField passwordField;
    private static javax.swing.JTextField repeatedPasswordField;
    private static javax.swing.JButton revealPassword;
    private static javax.swing.JTextField titleField;
    private static javax.swing.JTextField usernameField;

    @Override
    public void receivePassword(String password) {
        passwordField.setText(password);
        repeatedPasswordField.setText(password);
    }
    // End of variables declaration

}
