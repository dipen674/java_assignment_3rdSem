package com.hallsymphony.ui;

import com.hallsymphony.service.AuthService;
import com.hallsymphony.util.StyleConfig;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private MainFrame frame;
    private JTextField userField;
    private JPasswordField passField;

    public LoginPanel(MainFrame frame) {
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
                new EmptyBorder(2, 2, 4, 2) // shadow effect
            ),
            new EmptyBorder(40, 48, 40, 48)
        ));
        card.setPreferredSize(new Dimension(420, 480));
        card.setMaximumSize(new Dimension(420, 480));

        // ── Icon / Branding ──
        JLabel icon = new JLabel("\uD83C\uDFDB", SwingConstants.CENTER); // 🏛 emoji
        icon.setFont(new Font("SansSerif", Font.PLAIN, 48));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(icon);
        card.add(Box.createVerticalStrut(8));

        JLabel title = new JLabel("Hall Symphony");
        title.setFont(StyleConfig.TITLE_FONT);
        title.setForeground(StyleConfig.TEXT_COLOR);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(title);

        JLabel subtitle = new JLabel("Sign in to manage your bookings");
        subtitle.setFont(StyleConfig.SMALL_FONT);
        subtitle.setForeground(StyleConfig.TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(subtitle);

        card.add(Box.createVerticalStrut(32));

        // ── Username Field ──
        userField = new JTextField(20);
        StyleConfig.styleTextField(userField, "Username");
        userField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        userField.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(userField);
        card.add(Box.createVerticalStrut(12));

        // ── Password Field ──
        passField = new JPasswordField(20);
        StyleConfig.styleTextField(passField, "Password");
        passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        passField.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(passField);
        card.add(Box.createVerticalStrut(24));

        // ── Login Button ──
        JButton loginBtn = new JButton("Sign In");
        StyleConfig.styleButton(loginBtn);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.addActionListener(e -> handleLogin());
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(16));

        // ── Divider ──
        JSeparator sep = new JSeparator();
        sep.setForeground(StyleConfig.BORDER_COLOR);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        card.add(sep);
        card.add(Box.createVerticalStrut(16));

        // ── Register Link ──
        JButton regBtn = new JButton("Don't have an account? Register here");
        regBtn.setFont(StyleConfig.SMALL_FONT);
        regBtn.setBorder(null);
        regBtn.setContentAreaFilled(false);
        regBtn.setForeground(StyleConfig.PRIMARY_COLOR);
        regBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        regBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        regBtn.addActionListener(e -> frame.showScreen("REGISTER"));

        regBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                regBtn.setForeground(StyleConfig.PRIMARY_DARK);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                regBtn.setForeground(StyleConfig.PRIMARY_COLOR);
            }
        });
        card.add(regBtn);

        // ── Enter key to login ──
        passField.addActionListener(e -> handleLogin());
        userField.addActionListener(e -> passField.requestFocusInWindow());

        add(card);
    }

    private void handleLogin() {
        String username = userField.getText().trim();
        String password = new String(passField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.",
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        AuthService.login(username, password).ifPresentOrElse(
            user -> frame.loginSuccess(user),
            () -> JOptionPane.showMessageDialog(this, "Invalid username or password.",
                "Login Failed", JOptionPane.ERROR_MESSAGE)
        );
    }
}
