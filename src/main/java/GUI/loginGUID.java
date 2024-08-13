package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class loginGUID extends javax.swing.JPanel {
    public loginGUID() {
        setOpaque(false);
        initComponents();
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        passwordLabel = new javax.swing.JLabel();
        userLoginName = new javax.swing.JLabel();
        programTitle = new javax.swing.JLabel();
        usernameLabel = new javax.swing.JLabel();
        usernameField = new javax.swing.JTextField();
        passwordField = new javax.swing.JPasswordField();
        minimizeButton = new javax.swing.JButton();
        exitButton = new javax.swing.JButton();
        welcomeLabel = new javax.swing.JLabel();
        loginButton = new javax.swing.JButton();
        bgImage = new javax.swing.JLabel();

        setBackground(new Color(0,0,0,0));
        setMinimumSize(new java.awt.Dimension(600, 440));
        setPreferredSize(new java.awt.Dimension(600, 440));
        setLayout(null);
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        passwordLabel.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        passwordLabel.setForeground(new java.awt.Color(242, 242, 242));
        passwordLabel.setText("Password:");
        add(passwordLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 310, 100, 20));

        userLoginName.setFont(new java.awt.Font("Trebuchet MS", 1, 36)); // NOI18N
        userLoginName.setForeground(new java.awt.Color(242, 242, 242));
        userLoginName.setText("User!");
        add(userLoginName, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 140, 90, 40));

        programTitle.setFont(new java.awt.Font("Segoe UI Historic", 1, 8)); // NOI18N
        programTitle.setForeground(new java.awt.Color(242, 242, 242));
        programTitle.setText("Login");
        add(programTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 50, 20));

        usernameLabel.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        usernameLabel.setForeground(new java.awt.Color(242, 242, 242));
        usernameLabel.setText("Username:");
        add(usernameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 270, 100, 30));

        usernameField.setBackground(new java.awt.Color(73, 68, 91));
        usernameField.setForeground(new java.awt.Color(255, 255, 255));
        usernameField.setBorder(null);
        add(usernameField, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 280, 140, -1));

        passwordField.setBackground(new java.awt.Color(73, 68, 91));
        passwordField.setForeground(new java.awt.Color(255, 255, 255));
        passwordField.setBorder(null);
        add(passwordField, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 310, 140, -1));

        minimizeButton.setBackground(new java.awt.Color(0,0,0,0));
        minimizeButton.setOpaque(false);
        minimizeButton.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        minimizeButton.setForeground(new java.awt.Color(255, 255, 255));
        minimizeButton.setText("-");
        minimizeButton.setBorder(null);
        add(minimizeButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 4, 30, 10));

        exitButton.setBackground(new java.awt.Color(235, 64, 52,0));
        exitButton.setOpaque(false);
        exitButton.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        exitButton.setForeground(new java.awt.Color(255, 255, 255));
        exitButton.setText("X");
        exitButton.setBorder(null);
        add(exitButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 0, 20, 20));
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        welcomeLabel.setFont(new java.awt.Font("Trebuchet MS", 1, 36)); // NOI18N
        welcomeLabel.setForeground(new java.awt.Color(242, 242, 242));
        welcomeLabel.setText("Welcome");
        add(welcomeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 110, 160, 30));

        loginButton.setBackground(new java.awt.Color(101, 75, 216));
        loginButton.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
        loginButton.setForeground(new java.awt.Color(255, 255, 255));
        loginButton.setText("Login");
        loginButton.setBorder(null);
        add(loginButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 360, 170, 30));

        bgImage.setBackground(new Color(0,0,0,0));
        bgImage.setIcon(new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/images/smallDTC.png")))); // NOI18N
        add(bgImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

    }// </editor-fold>




    // Variables declaration - do not modifyD
    private javax.swing.JLabel bgImage;
    private javax.swing.JButton exitButton;
    private javax.swing.JButton loginButton;
    private javax.swing.JButton minimizeButton;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JLabel programTitle;
    private javax.swing.JLabel userLoginName;
    private javax.swing.JTextField usernameField;
    private javax.swing.JLabel usernameLabel;
    private javax.swing.JLabel welcomeLabel;

    public JTextField getUsernameField() {
        return usernameField;
    }

    public JPasswordField getPasswordField(){
        return passwordField;
    }

    public JButton getLoginButton(){
        return loginButton;
    }

    public JButton getMinimizeButton(){
        return minimizeButton;
    }
    // End of variables declaration
}
