package com.hallsymphony.ui;

import com.hallsymphony.service.AuthService;
import com.hallsymphony.util.StyleConfig;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class RegistrationPanel extends JPanel {
    private MainFrame frame;
    private JTextField userField, nameField, contactField;
    private JPasswordField passField;

    public RegistrationPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new GridBagLayout());
        setBackground(StyleConfig.BACKGROUND_COLOR);

        // ── Card Container ──
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(StyleConfig.CARD_COLOR);
        card.setBorder(new CompoundBorder(
            new CompoundBorder(
                new LineBorder(StyleConfig.BORDER_COLOR, 1, true),
                new EmptyBorder(2, 2, 4, 2)
            ),
            new EmptyBorder(36, 48, 36, 48)
        ));
        card.setPreferredSize(new Dimension(420, 560));
        card.setMaximumSize(new Dimension(420, 560));

        // ── Branding ──
        JLabel icon = new JLabel("\uD83D\uDCDD", SwingConstants.CENTER);
        icon.setFont(new Font("SansSerif", Font.PLAIN, 40));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(icon);
        card.add(Box.createVerticalStrut(8));

        JLabel title = new JLabel("Create Account");
        title.setFont(StyleConfig.TITLE_FONT);
        title.setForeground(StyleConfig.TEXT_COLOR);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(title);

        JLabel subtitle = new JLabel("Register as a new customer");
        subtitle.setFont(StyleConfig.SMALL_FONT);
        subtitle.setForeground(StyleConfig.TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(subtitle);
        card.add(Box.createVerticalStrut(28));

        // ── Fields ──
        nameField = new JTextField(20);
        StyleConfig.styleTextField(nameField, "Full Name");
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        card.add(nameField);
        card.add(Box.createVerticalStrut(10));

        userField = new JTextField(20);
        StyleConfig.styleTextField(userField, "Username");
        userField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        card.add(userField);
        card.add(Box.createVerticalStrut(10));

        passField = new JPasswordField(20);
        StyleConfig.styleTextField(passField, "Password");
        passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        card.add(passField);
        card.add(Box.createVerticalStrut(10));

        contactField = new JTextField(20);
        StyleConfig.styleTextField(contactField, "Contact Number");
        contactField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        card.add(contactField);
        card.add(Box.createVerticalStrut(20));

        // ── Register Button ──
        JButton regBtn = new JButton("Create Account");
        StyleConfig.styleSuccessButton(regBtn);
        regBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        regBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        regBtn.addActionListener(e -> handleRegistration());
        card.add(regBtn);
        card.add(Box.createVerticalStrut(16));

        // ── Separator ──
        JSeparator sep = new JSeparator();
        sep.setForeground(StyleConfig.BORDER_COLOR);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        card.add(sep);
        card.add(Box.createVerticalStrut(16));

        // ── Back Link ──
        JButton backBtn = new JButton("Already have an account? Sign in");
        backBtn.setFont(StyleConfig.SMALL_FONT);
        backBtn.setContentAreaFilled(false);
        backBtn.setBorder(null);
        backBtn.setForeground(StyleConfig.PRIMARY_COLOR);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.addActionListener(e -> frame.showScreen("LOGIN"));

        backBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                backBtn.setForeground(StyleConfig.PRIMARY_DARK);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                backBtn.setForeground(StyleConfig.PRIMARY_COLOR);
            }
        });
        card.add(backBtn);

        add(card);
    }

    private void handleRegistration() {
        String fullName = nameField.getText().trim();
        String username = userField.getText().trim();
        String password = new String(passField.getPassword());
        String contact = contactField.getText().trim();

        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || contact.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.",
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (AuthService.registerCustomer(username, password, fullName, contact)) {
            JOptionPane.showMessageDialog(this, "Registration successful! You can now sign in.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            frame.showScreen("LOGIN");
        } else {
            JOptionPane.showMessageDialog(this, "Username already exists. Please choose a different one.",
                "Registration Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
