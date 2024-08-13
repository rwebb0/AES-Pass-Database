package GUI;

import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

public class passGenUI extends javax.swing.JFrame implements PasswordReceiver {

    private Set<String> commonPasswords;
    private Set<String> dictionaryWords;

    public passGenUI() {
        initComponents();
        loadCommonPasswords();
        loadDictionaryEntries();
    }


    private void initComponents() {

        uppercaseCheckBox = new JCheckBox();
        lowercaseCheckBox = new JCheckBox();
        numberCheckBox = new JCheckBox();
        specialCheckBox = new JCheckBox();
        repeatPassField = new JTextField();
        passField = new JTextField();
        JLabel repeatPassLabel = new JLabel();
        JLabel passwordLabel = new JLabel();
        JButton generatePasswordButton = new JButton();
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(12, 12, Integer.MAX_VALUE, 1);
        passwordLengthSpinner = new javax.swing.JSpinner(spinnerModel);
        JLabel passwordLengthlabel = new JLabel();
        passwordStrengthBar = new JProgressBar(0, 100);
        passwordStrengthBar.setStringPainted(true);
        JButton evaluatePasswordButton = new JButton();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new AbsoluteLayout());

        uppercaseCheckBox.setText("Uppercase");
        getContentPane().add(uppercaseCheckBox, new AbsoluteConstraints(10, 260, -1, -1));

        lowercaseCheckBox.setText("Lowercase");
        getContentPane().add(lowercaseCheckBox, new AbsoluteConstraints(10, 290, -1, -1));

        numberCheckBox.setText("Numbers");
        numberCheckBox.setToolTipText("");
        getContentPane().add(numberCheckBox, new AbsoluteConstraints(10, 320, -1, -1));

        specialCheckBox.setText("Special (%,£,^,&)");
        getContentPane().add(specialCheckBox, new AbsoluteConstraints(10, 230, -1, -1));

        passField.setText("•••••••");
        getContentPane().add(passField, new AbsoluteConstraints(20, 40, 260, -1));

        repeatPassField.setText("•••••••");
        getContentPane().add(repeatPassField, new AbsoluteConstraints(20, 100, 260, -1));

        repeatPassLabel.setText("Repeated Password");
        getContentPane().add(repeatPassLabel, new AbsoluteConstraints(20, 80, -1, -1));

        passwordLabel.setText("Password");
        getContentPane().add(passwordLabel, new AbsoluteConstraints(20, 20, -1, -1));

        getContentPane().add(passwordStrengthBar, new AbsoluteConstraints(10, 350, 280, 20));

        generatePasswordButton.setText("Generate Password");
        generatePasswordButton.setBorder(null);
        generatePasswordButton.addActionListener(this::generatePasswordButtonActionPerformed);
        getContentPane().add(generatePasswordButton, new AbsoluteConstraints(80, 130, 120, 30));

        // Place the Evaluate Password button just below the Generate Password button with some vertical spacing
        evaluatePasswordButton.setText("Evaluate Password");
        evaluatePasswordButton.setBorder(null);
        evaluatePasswordButton.addActionListener(this::evaluatePasswordButtonActionPerformed);
        getContentPane().add(evaluatePasswordButton, new AbsoluteConstraints(66, 170, 150, 30));


        passwordLengthlabel.setText("Length");
        getContentPane().add(passwordLengthlabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 230, 40, 20));
        getContentPane().add(passwordLengthSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(224, 230, 50, -1));

        // Add a key listener to the password field to automatically copy the text to the repeated password field
        passField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                repeatPassField.setText(passField.getText());
            }
        });

        loadCommonPasswords(); // Load common passwords from the file

        setPreferredSize(new Dimension(320, 430));
        pack();
    }// </editor-fold>


    private void loadDictionaryEntries() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("src/main/resources/imp_files/dictionary_entries.txt"));
            dictionaryWords = new HashSet<>(lines);
        } catch (Exception e) {
            e.printStackTrace();
            dictionaryWords = new HashSet<>();
        }
    }


    private void loadCommonPasswords() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("src/main/resources/imp_files/cmm_p.txt"));
            commonPasswords = new HashSet<>(lines);
        } catch (Exception e) {
            e.printStackTrace();
            commonPasswords = new HashSet<>();
        }
    }

    // Handler for the Evaluate Password button click
    private void evaluatePasswordButtonActionPerformed(ActionEvent evt) {
        String password = passField.getText();
        updatePasswordStrength(password);
    }

    private void generatePasswordButtonActionPerformed(ActionEvent evt) {
        // Get the selected checkboxes
        boolean useUppercase = uppercaseCheckBox.isSelected();
        boolean useLowercase = lowercaseCheckBox.isSelected();
        boolean useNumbers = numberCheckBox.isSelected();
        boolean useSpecial = specialCheckBox.isSelected();

        int passwordLength = (int) passwordLengthSpinner.getValue();

        // Generate the password based on the selected options
        String generatedPassword = generatePassword(passwordLength, useUppercase, useLowercase, useNumbers, useSpecial);

        // Set the generated password in the passField text field
        passField.setText(generatedPassword);
        repeatPassField.setText(generatedPassword);
        updatePasswordStrength(generatedPassword);
    }

    // Implement the password generation logic based on selected options
    private String generatePassword(int length, boolean useUppercase, boolean useLowercase, boolean useNumbers, boolean useSpecial) {
        // Define character sets based on selected options
        String uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercaseChars = "abcdefghijklmnopqrstuvwxyz";
        String numberChars = "0123456789";
        String specialChars = "!@#$%^&*()_-+={}[]:;\"'<>,.?/\\|`~";

        // Build the character set based on selected options
        StringBuilder charSet = new StringBuilder();
        if (useUppercase) charSet.append(uppercaseChars);
        if (useLowercase) charSet.append(lowercaseChars);
        if (useNumbers) charSet.append(numberChars);
        if (useSpecial) charSet.append(specialChars);

        if (charSet.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please tick at least one of the boxes.");
            return "";
        }

        // Randomly select characters from the character set to create the password
        Random random = new Random();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(charSet.length());
            password.append(charSet.charAt(randomIndex));
        }

        return password.toString();
    }

    private void updatePasswordStrength(String password) {
        // Set default strength to 0
        int strength = 0;
        // If the password isn't empty, calculate the strength
        if (!password.isEmpty()) {
            strength = calculatePasswordStrength(password);
        }
        passwordStrengthBar.setValue(strength);
        passwordStrengthBar.setString("Strength: " + strength + "%");
    }

    private int calculatePasswordStrength(String password) {
        // Initial strength based on entropy
        double strength = calculateEntropy(password);

        // Apply penalties
        strength -= applyCommonPasswordPenalty(password);
        strength -= applyDictionaryWordPenalty(password);
        strength -= applySequentialAndPatternPenalty(password);

        // Normalize the strength to a scale of 0 to 100
        return normalizeStrength(strength);
    }


    private double calculateEntropy(String password) {
        int R = calculateCharacterSetSize();
        int L = password.length();
        return L * (Math.log(R) / Math.log(2)); // Corrected entropy calculation
    }

    private double applyCommonPasswordPenalty(String password) {
        if (commonPasswords.contains(password.toLowerCase())) {
            System.out.println("Common password penalty applied.");
            return 30;
        }
        return 0;
    }

    private double applyDictionaryWordPenalty(String password) {
        for (String word : dictionaryWords) {
            if (word.length() > 3 && password.toLowerCase().contains(word)) {
                System.out.println("Dictionary word penalty applied for word: " + word);
                return 10; // Lowered penalty
            }
        }
        return 0;
    }

    private double applySequentialAndPatternPenalty(String password) {
        double penalty = 0;
        Pattern sequentialPattern = Pattern.compile("(012|123|234|345|456|567|678|789|890|" +
                "abc|bcd|cde|def|efg|fgh|ghi|hij|ijk|jkl|klm|lmn|"+
                "mno|nop|opq|pqr|qrs|rst|stu|tuv|uvw|vwx|wxy|xyz)");
        Pattern repeatPattern = Pattern.compile("(.)\\1{2,}");

        if (sequentialPattern.matcher(password).find()) {
            penalty += 15;
            System.out.println("Sequential characters penalty applied.");
        }

        if (repeatPattern.matcher(password).find()) {
            penalty += 15;
            System.out.println("Repeated characters penalty applied.");
        }


        return penalty;
    }

    private int normalizeStrength(double entropy) {
        double maxEntropy = 128;
        int strength = (int)((entropy / maxEntropy) * 100);
        return Math.max(0, Math.min(100, strength)); // Ensure strength is within 0 to 100%
    }



    private int calculateCharacterSetSize() {
        int size = 0;
        if (uppercaseCheckBox.isSelected()) size += 26;
        if (lowercaseCheckBox.isSelected()) size += 26;
        if (numberCheckBox.isSelected()) size += 10;
        if (specialCheckBox.isSelected()) size += "!@#$%^&*()_-+={}[]:;\"'<>,.?/\\|`~".length();
        return size;
    }


    public static void main(String[] args) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(passGenUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            System.out.println("Visible");
            new passGenUI().setVisible(true);
        });
    }

    // Variables declaration - do not modify
    private javax.swing.JCheckBox uppercaseCheckBox;
    private JProgressBar passwordStrengthBar;
    private javax.swing.JCheckBox lowercaseCheckBox;
    private javax.swing.JCheckBox numberCheckBox;
    private javax.swing.JCheckBox specialCheckBox;
    private javax.swing.JTextField repeatPassField;
    private javax.swing.JTextField passField;
    private javax.swing.JSpinner passwordLengthSpinner;

    @Override
    public void receivePassword(String password) {
        passField.setText(password);
        repeatPassField.setText(password);
        // Optionally, you can update the strength bar based on the received password
        updatePasswordStrength(password);
    }
}
