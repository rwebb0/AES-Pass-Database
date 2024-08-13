package GUI;


import bgPass.*;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import org.netbeans.lib.awtextra.AbsoluteLayout;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class passManGUI extends JFrame implements EntryUpdateListener {
    int posX=0,posY=0;
    private Timer inactivityTimer;
    private static final int INACTIVITY_LIMIT = 30 * 60 * 1000; // 30 minutes


    private passEncryption encryption;

    public passManGUI() throws Exception {
        try {
            this.encryption = new passEncryption(); // Initialize passEncryption
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Error setting Look and Feel: " + e.getMessage());
        }
        // Schedule regular checks for password expiry
        Timer timer = new Timer(24 * 60 * 60 * 1000, e -> checkForPasswordExpiry()); // 24 hours interval
        timer.start();
        FlatLaf.setup(new FlatDarkLaf());
        setUndecorated(true);
        initComponents();
        setLocationRelativeTo(null); // Center the window
        setMinimumSize(new Dimension(1020, 640)); // Set minimum size
        setVisible(true); // Ensure visibility
        setupInactivityTimer();
    }

    private void initComponents() throws Exception {

        //<editor-fold desc="Buttons/Labels">
        JComboBox<String> categoriesComboBox = new JComboBox<>();
        JScrollPane passwordHolderPane = new JScrollPane();
        scrollPanePass = new javax.swing.JPanel();
        JButton entryValuesButton = new JButton();
        JSeparator jSeparator1 = new JSeparator();
        JLabel programTitle = new JLabel();
        JLabel categoriesLabel = new JLabel();
        searchField = new javax.swing.JTextField();
        JButton searchButton = new JButton();
        JButton clearSearchButton = new JButton();
        JButton minimizeButton = new JButton();
        JButton closeButton1 = new JButton();
        JButton closeButton = new JButton();
        //<editor-fold desc="Variable declaration">
        // Variables declaration - do not modify
        JLabel bgImage = new JLabel();
        JButton addEntryButton = new JButton();
        //</editor-fold>

        //<editor-fold desc="misc">
        setTitle("Password Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1020, 640));
        setMinimumSize(new Dimension(1020, 640));
        setLocationRelativeTo(null);
        setBackground(new Color(0,0,0,0));
        setLayout(new AbsoluteLayout());
        setVisible(true);

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        initializeGUI();

        repaint();
        scrollPanePass.revalidate();
        scrollPanePass.repaint();
        categoriesComboBox.setRenderer(new StyledComboBoxRenderer());
        //</editor-fold>

        //<editor-fold desc="Scrollpane">
        passwordHolderPane.setBackground(new java.awt.Color(44, 40, 58));
        passwordHolderPane.setBorder(null);
        passwordHolderPane.setForeground(new java.awt.Color(44, 40, 58));
        passwordHolderPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        scrollPanePass.setBackground(new java.awt.Color(44, 40, 58));
        scrollPanePass.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        passwordHolderPane.setViewportView(scrollPanePass);

        scrollPanePass.revalidate();
        scrollPanePass.repaint();

        refreshValues();

        add(passwordHolderPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 100, 780, 470));
        add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 110, 10));
        //</editor-fold>

        //<editor-fold desc="Program title information">
        programTitle.setFont(new java.awt.Font("Segoe UI Historic", Font.BOLD, 8)); // NOI18N
        programTitle.setForeground(new java.awt.Color(242, 242, 242));
        programTitle.setText("Password Manager");
        add(programTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 80, 20));
        //</editor-fold>

        //<editor-fold desc="CatagoriesLabel">
        categoriesLabel.setFont(new java.awt.Font("Trebuchet MS", Font.BOLD, 18)); // NOI18N
        categoriesLabel.setForeground(new java.awt.Color(255, 255, 255));
        categoriesLabel.setText("Categories");
        add(categoriesLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, -1, -1));
        //</editor-fold>

        //<editor-fold desc="Search">
        searchField.setBackground(new java.awt.Color(127, 114, 173));
        searchField.setFont(new java.awt.Font("Trebuchet MS", Font.BOLD, 12)); // NOI18N
        searchField.setForeground(new java.awt.Color(255, 255, 255));
        searchField.setToolTipText(" Search....");
        searchField.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        searchField.setName(""); // NOI18N
        add(searchField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 50, 430, 30));


        categoriesComboBox.setPreferredSize(new Dimension(180, 30)); // Adjust the size to fit your UI
        categoriesComboBox.setMaximumRowCount(5); // Set the maximum rows to display without scrolling
        categoriesComboBox.setBackground(new Color(44, 40, 58)); // Background color
        categoriesComboBox.setForeground(Color.WHITE); // Text color
        categoriesComboBox.setBorder(BorderFactory.createLineBorder(new Color(121, 45, 202))); // Border color
        categoriesComboBox.addItem("All");
        categoriesComboBox.addItem("School");
        categoriesComboBox.addItem("Work");
        categoriesComboBox.addItem("Personal");
        categoriesComboBox.addActionListener(e -> {
            String selectedCategory = (String) categoriesComboBox.getSelectedItem();
            filterEntriesByCategory(selectedCategory);
        });
        add(categoriesComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));


        searchButton.setBackground(new java.awt.Color(101, 75, 216));
        searchButton.setFont(new java.awt.Font("Trebuchet MS", Font.BOLD, 12)); // NOI18N
        searchButton.setForeground(new java.awt.Color(255, 255, 255));
        searchButton.setText("Search");
        searchButton.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        searchButton.setBorderPainted(false);
        searchButton.addActionListener(evt -> {
            try {
                searchButtonActionPerformed();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        add(searchButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 50, 70, 30));

        clearSearchButton.setBackground(new java.awt.Color(101, 75, 216));
        clearSearchButton.setFont(new java.awt.Font("Trebuchet MS", Font.BOLD, 12)); // NOI18N
        clearSearchButton.setForeground(new java.awt.Color(255, 255, 255));
        clearSearchButton.setText("Clear Search");
        clearSearchButton.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        clearSearchButton.setBorderPainted(false);
        clearSearchButton.addActionListener(this::clearSearchButtonActionPerformed);
        add(clearSearchButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 50, 100, 30));
        //</editor-fold>

        //<editor-fold desc="Add entry">
        addEntryButton.setBackground(new java.awt.Color(101, 75, 216));
        addEntryButton.setFont(new java.awt.Font("Trebuchet MS", Font.BOLD, 12)); // NOI18N
        addEntryButton.setForeground(new java.awt.Color(255, 255, 255));
        addEntryButton.setText("Create Entry");
        addEntryButton.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        addEntryButton.setBorderPainted(false);
        addEntryButton.addActionListener(e -> {
            try {
                openAddPasswordWindow();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        add(addEntryButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 50, 100, 30));
        //</editor-fold>

        //<editor-fold desc="Header buttons">
        minimizeButton.setBackground(new java.awt.Color(101, 75, 216,0));
        minimizeButton.setOpaque(false);
        minimizeButton.setFont(new java.awt.Font("Trebuchet MS", Font.BOLD, 24)); // NOI18N
        minimizeButton.setForeground(new java.awt.Color(255, 255, 255));
        minimizeButton.setText("-");
        minimizeButton.setBorder(null);
        minimizeButton.addActionListener(evt -> setState(ICONIFIED));
        add(minimizeButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 10, 30, 10));

        closeButton1.setBackground(new java.awt.Color(44, 40, 58,0));
        closeButton1.setOpaque(false);
        closeButton1.setFont(new java.awt.Font("Trebuchet MS", Font.BOLD, 18)); // NOI18N
        closeButton1.setForeground(new java.awt.Color(255, 255, 255));
        closeButton1.setText("X");
        closeButton1.setBorder(null);
        closeButton1.addActionListener(e -> System.exit(0));
        add(closeButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 4, -1, -1));
        //</editor-fold>

        //<editor-fold desc="Close button, bottom left">
        closeButton.setBackground(new java.awt.Color(101, 75, 216));
        closeButton.setFont(new java.awt.Font("Trebuchet MS", Font.BOLD, 18)); // NOI18N
        closeButton.setForeground(new java.awt.Color(255, 255, 255));
        closeButton.setText("Close");
        closeButton.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        closeButton.setBorderPainted(false);
        closeButton.addActionListener(this::closeButtonActionPerformed);
        add(closeButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 580, 90, 40));
        //</editor-fold>

        //<editor-fold desc="Backgroundimage">
        bgImage.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/images/MDTC.png")))); // NOI18N
        add(bgImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));
        //</editor-fold>

        //<editor-fold desc="Move GUI based on mouse drag">
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
        //</editor-fold>

        popup = new JPopupMenu();
        JMenuItem createEntryPopup = new JMenuItem("Create Entry");
        createEntryPopup.addActionListener(e -> {
            try {
                openAddPasswordWindow();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        popup.add(createEntryPopup);
        popup.addSeparator();

        checkForPasswordExpiry();

    }

    private void autoType(passValues entry) {
        try {
            // Minimize the password manager window
            this.setState(Frame.ICONIFIED);

            // Obtain the username and password
            String username = decryptField(entry.getUsername());
            String password = decryptField(entry.getPassword());

            // Create an instance of Robot
            Robot robot = new Robot();

            // Delay to allow focus to shift
            robot.delay(500); // Delay for 0.5 seconds

            // Type the username
            for (char character : username.toCharArray()) {
                typeCharacter(robot, character);
            }

            // Simulate pressing TAB to move to the password field
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);

            // Type the password
            for (char character : password.toCharArray()) {
                typeCharacter(robot, character);
            }

            // Simulate pressing ENTER
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error during auto-type: " + ex.getMessage());
        }
    }

    private void typeCharacter(Robot robot, char character) {
        try {
            char upperChar = Character.toUpperCase(character);

            // Determine if the SHIFT key is needed (for upper case and symbols)
            boolean shiftNeeded = Character.isUpperCase(character) || !Character.isLetterOrDigit(character);

            // Press the SHIFT key if necessary
            if (shiftNeeded) {
                robot.keyPress(KeyEvent.VK_SHIFT);
            }

            // Press and release the key
            int keyCode = KeyEvent.getExtendedKeyCodeForChar(upperChar);
            robot.keyPress(keyCode);
            robot.delay(50);  // Delay between press and release to simulate natural typing speed
            robot.keyRelease(keyCode);

            // Release the SHIFT key if it was pressed
            if (shiftNeeded) {
                robot.keyRelease(KeyEvent.VK_SHIFT);
            }

            // Brief delay between characters to mimic human typing
            robot.delay(50);
        } catch (IllegalArgumentException e) {
            System.err.println("Cannot type character: " + character);
        }
    }


    private void filterEntriesByCategory(String category) {
        try {
            List<passValues> allEntries = passMan.readDataFromFile(bgCreation.DIRECTORY_PATH + bgCreation.PASSWORD_JSON_FILENAME);
            List<passValues> filteredEntries = new ArrayList<>();

            for (passValues entry : allEntries) {
                if ("All".equals(category)) {
                    filteredEntries = allEntries;
                    break;
                } else {
                    // Check if the category is not null or empty before trying to decrypt
                    if (entry.getCategory() != null && !entry.getCategory().trim().isEmpty()) {
                        // Decrypt the category
                        String entryCategory = decryptField(entry.getCategory());
                        if (category.equals(entryCategory)) {
                            filteredEntries.add(entry);
                        }
                    }
                }
            }

            // Update the display to show only the filtered entries
            displayEntries(filteredEntries);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error filtering entries: " + e.getMessage());
        }
    }

    // Method to create and display the popup menu on right-click
    private void showPopup(MouseEvent me, String username, String password, passValues entry) {
        if (me.isPopupTrigger()) {
            popup.removeAll(); // Clear previous items

            // Menu item for copying username
            JMenuItem copyUsername = new JMenuItem("Copy Username");
            copyUsername.addActionListener(e -> copyToClipboard(username));
            popup.add(copyUsername);

            // Menu item for copying password
            JMenuItem copyPassword = new JMenuItem("Copy Password");
            copyPassword.addActionListener(e -> copyToClipboard(password));
            popup.add(copyPassword);

            // Submenu for changing the category
            JMenu changeCategoryMenu = new JMenu("Change category");
            JMenuItem allCategory = new JMenuItem("All");
            allCategory.addActionListener(e -> changeEntryCategory(entry, "All"));
            changeCategoryMenu.add(allCategory);

            JMenuItem schoolCategory = new JMenuItem("School");
            schoolCategory.addActionListener(e -> changeEntryCategory(entry, "School"));
            changeCategoryMenu.add(schoolCategory);

            JMenuItem workCategory = new JMenuItem("Work");
            workCategory.addActionListener(e -> changeEntryCategory(entry, "Work"));
            changeCategoryMenu.add(workCategory);

            JMenuItem personalCategory = new JMenuItem("Personal");
            personalCategory.addActionListener(e -> changeEntryCategory(entry, "Personal"));
            changeCategoryMenu.add(personalCategory);

            popup.add(changeCategoryMenu);

            JMenuItem autoTypeItem = new JMenuItem("Auto Type");
            autoTypeItem.addActionListener(e -> autoType(entry));
            popup.add(autoTypeItem);

            popup.show(me.getComponent(), me.getX(), me.getY());
        }
    }

    private void changeEntryCategory(passValues entry, String newCategory) {
        try {
            List<passValues> entries = passMan.readDataFromFile(bgCreation.DIRECTORY_PATH + bgCreation.PASSWORD_JSON_FILENAME);
            boolean isEntryFound = false;

            // Encrypt the new category
            String encryptedCategory = Base64.getEncoder().encodeToString(encryption.encrypt(newCategory));

            for (passValues currentEntry : entries) {
                if (currentEntry.equals(entry)) {
                    // Set the encrypted category directly
                    currentEntry.setCategoryDirectly(encryptedCategory);
                    isEntryFound = true;
                    break;
                }
            }

            if (isEntryFound) {
                passMan.writeDataToFile(entries, bgCreation.DIRECTORY_PATH + bgCreation.PASSWORD_JSON_FILENAME);
                JOptionPane.showMessageDialog(this, "Category updated.");
                refreshValues(); // Refresh the display
            } else {
                JOptionPane.showMessageDialog(this, "Error: Entry not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating category: " + e.getMessage());
        }
    }

    private void copyToClipboard(String text) {
        StringSelection selection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, null);

        // Set a timer to clear the clipboard after 30 seconds
        Timer timer = new Timer(15000, e -> clearClipboard());
        timer.setRepeats(false); // Ensure the timer only runs once
        timer.start();
    }

    private void clearClipboard() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable blank = new StringSelection("");
        clipboard.setContents(blank, null);
    }

    public void refreshValues() throws Exception {
        List<passValues> entries = passMan.readDataFromFile(bgCreation.DIRECTORY_PATH + bgCreation.PASSWORD_JSON_FILENAME);
        if (entries == null) {
            return;
        }
        displayEntries(entries);
    }

    // Method to display entries
    private void displayEntries(List<passValues> entries) {
        scrollPanePass.removeAll();
        int yCoord = 20;

        for (passValues value : entries) {
            JButton entryButton = createEntryButton(value);
            // Set button background color here
            entryButton.setBackground(yCoord / 40 % 2 == 0 ? new Color(101, 75, 216) : new Color(168, 148, 255));
            scrollPanePass.add(entryButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, yCoord, 740, 30));
            yCoord += 40;
        }

        scrollPanePass.revalidate();
        scrollPanePass.repaint();
    }

    // Method to create a JButton for an entry
    private JButton createEntryButton(passValues value) {
        String decodedTitle = decryptField(value.getTitle());
        String formattedTitle = formatTitle(decodedTitle);
        String decodedUsername = decryptField(value.getUsername());
        String decodedPassword = decryptField(value.getPassword());

        JButton entryButton = new JButton();
        entryButton.setFont(new java.awt.Font("Trebuchet MS", Font.BOLD, 12));
        entryButton.setForeground(new java.awt.Color(255, 255, 255));
        entryButton.setText("<html><table><tr><td align='left'>Title: " + formattedTitle +"</td" + "<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>"
                + "</td><td>Username: " + decodedUsername + "</td>" + "<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>"
                + "<td> Password: *******</td></tr></table>");
        entryButton.setBorder(null);
        entryButton.setActionCommand(decodedTitle); // Store the title as the action command for easy access
        entryButton.addActionListener(e -> openEditEntry(decodedTitle, decodedUsername));
        entryButton.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                showPopup(e, decodedUsername, decodedPassword, value);
            }
        });

        return entryButton;
    }

    private String formatTitle(String title) {
        if (title == null) {
            return "";
        }
        return title.length() > 10 ? title.substring(0, 10) + "..." : title;
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

    public void openEditEntry(String title, String username) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Create the editEntry window
                editEntry editWindow = new editEntry(title, username);
                // Set passManGUI as the listener
                editWindow.setUpdateListener(passManGUI.this);
                editWindow.setVisible(true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        repaint();
        scrollPanePass.revalidate();
        scrollPanePass.repaint();
    }

    private void searchButtonActionPerformed() {
        try {
            // Get the search text and convert it to lowercase for case-insensitive comparison
            String searchText = searchField.getText().trim().toLowerCase();

            // Fetch all entries
            List<passValues> entries = passMan.readDataFromFile(bgCreation.DIRECTORY_PATH + bgCreation.PASSWORD_JSON_FILENAME);

            if (entries == null) {
                JOptionPane.showMessageDialog(this, "No entries to search.");
                return;
            }

            // Filter entries based on search text
            List<passValues> filteredEntries = entries.stream()
                    .filter(entry -> decryptField(entry.getTitle()).toLowerCase().contains(searchText))
                    .collect(Collectors.toList());

            // Display only the filtered entries
            displayEntries(filteredEntries);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error during search: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearSearchButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // Clear the text in the search field
        searchField.setText("");
        // Clear the current display in the scroll pane
        scrollPanePass.removeAll();

        refreshUI();
        // Re-add all the entry buttons to the scroll pane
        int yCoord = 20; // Starting y-coordinate, adjust as needed
        for (JButton entryButton : entryButtons) {
            scrollPanePass.add(entryButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, yCoord, 740, 30));
            yCoord += 40; // Increment y-coordinate for next button, adjust spacing as needed
        }


        refreshUI();
        // Refresh the scroll pane to show all entries
        scrollPanePass.revalidate();
        scrollPanePass.repaint();
    }

    private void openAddPasswordWindow() {
        JFrame addPasswordFrame = new JFrame("Add Password");
        addPasswordFrame.setSize(500, 300);
        addPasswordFrame.setLocationRelativeTo(this);

        JPanel addPasswordPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        JTextField titleField = new JTextField();
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField notesField = new JTextField();
        JSpinner passwordLengthSpinner = new JSpinner(new SpinnerNumberModel(12, 1, 64, 1));
        JButton generatePasswordButton = new JButton("Generate Password");
        JButton addButton = new JButton("Add");

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        addPasswordPanel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 2;
        gbc.gridwidth = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addPasswordPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        addPasswordPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 2;
        gbc.gridwidth = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addPasswordPanel.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        addPasswordPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 2;
        gbc.gridwidth = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addPasswordPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 4;
        addPasswordPanel.add(new JLabel("Notes:"), gbc);
        gbc.gridx = 2;
        gbc.gridwidth = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addPasswordPanel.add(notesField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        addPasswordPanel.add(new JLabel("Password Length:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 1;
        addPasswordPanel.add(passwordLengthSpinner, gbc);

        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        addPasswordPanel.add(generatePasswordButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 4;
        addPasswordPanel.add(addButton, gbc);

        addPasswordFrame.add(addPasswordPanel);
        addPasswordFrame.setVisible(true);

        generatePasswordButton.addActionListener(e -> {
            int desiredLength = (int) passwordLengthSpinner.getValue();
            String generatedPassword = passGen.generateRandomPassword(desiredLength);
            passwordField.setText(generatedPassword);
        });

        addButton.addActionListener(e -> {
            String newTitle = usernameField.getText();
            String newUsername = titleField.getText();
            char[] newPasswordChars = passwordField.getPassword();
            String newPassword = new String(newPasswordChars);
            String notes = notesField.getText();
            String category = "All";

            // Get the current date in the required format for last_updated
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = sdf.format(new Date());


            if (newTitle.isEmpty()) {
                JOptionPane.showMessageDialog(addPasswordFrame, "Title is required.");
            } else if (newUsername.isEmpty() || newPassword.isEmpty()) {
                JOptionPane.showMessageDialog(addPasswordFrame, "Username and password are required.");
            } else {
                try {
                    new passMan().addAccount(newTitle, newUsername, newPassword, notes, currentDate, category);
                    JOptionPane.showMessageDialog(addPasswordFrame, "Password added successfully.");
                    addPasswordFrame.dispose();
                    refreshValues();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(addPasswordFrame, "Error adding password: " + ex.getMessage());
                }
            }
        });
    }

    private void checkForPasswordExpiry() {
        try {
            List<passValues> entries = passMan.readDataFromFile(bgCreation.DIRECTORY_PATH + bgCreation.PASSWORD_JSON_FILENAME);
            Date currentDate = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            for (passValues entry : entries) {
                // Decrypt the lastUpdated field before parsing it as a date
                String decryptedLastUpdated = decryptField(entry.getLastUpdated());
                Date lastUpdated = sdf.parse(decryptedLastUpdated);
                long diff = currentDate.getTime() - lastUpdated.getTime();
                long diffDays = diff / (24 * 60 * 60 * 1000);

                if (diffDays >= 30) {
                    JOptionPane.showMessageDialog(this, "Password for " + decryptField(entry.getTitle()) + " needs to be updated.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }


    private javax.swing.JPanel scrollPanePass;
    private JPopupMenu popup;
    private javax.swing.JTextField searchField;
    public List<JButton> entryButtons = new ArrayList<>();
    // End of variables declaration
    //</editor-fold>

    @Override
    public void onEntryUpdated() {
        refreshUI();
    }

    @Override
    public void onEntryDeleted() {
        refreshUI();
    }

    // Use this method to update the UI
    private void refreshUI() {
        // Code to refresh your UI, e.g., call refreshValues()
        try {
            refreshValues();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setupInactivityTimer() {
        // Define the action to perform on timeout
        ActionListener logoutAction = e -> lockApplication();
        inactivityTimer = new Timer(INACTIVITY_LIMIT, logoutAction);
        inactivityTimer.setRepeats(false); // Ensure it only triggers once until reset
        inactivityTimer.start();
    }

    private void lockApplication() {
        // This method should lock the application or log out the user
        JOptionPane.showMessageDialog(this, "The application has been locked due to inactivity.");
        openLoginGUI();
    }

    private void resetInactivityTimer() {
        if (inactivityTimer.isRunning()) {
            inactivityTimer.restart();
        }
    }

    private void initializeGUI() {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                resetInactivityTimer(); // Reset the timer on mouse press
            }
        });

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                resetInactivityTimer(); // Reset the timer on key press
            }
        });

        // Add other components and set up the JFrame as usual
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1020, 640));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    protected void processEvent(AWTEvent e) {
        super.processEvent(e);
        if (e instanceof MouseEvent || e instanceof KeyEvent) {
            resetInactivityTimer(); // Reset the timer on any event
        }
    }

    private void openLoginGUI() {
        EventQueue.invokeLater(() -> {
            dispose(); // Dispose the current GUI
            try {
                new mainGUI().setVisible(true); // Open the main login GUI
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new passManGUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}

// Custom renderer class
class StyledComboBoxRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (isSelected) {
            setBackground(new Color(101, 75, 216)); // Selection background color
            setForeground(Color.WHITE); // Selection foreground color
        } else {
            setBackground(new Color(44, 40, 58)); // Regular background color
            setForeground(Color.WHITE); // Regular foreground color
        }
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Padding around text
        return this;
    }
}


