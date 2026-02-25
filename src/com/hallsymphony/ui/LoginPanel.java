package com.hallsymphony.ui;

import com.hallsymphony.service.AuthService;
import com.hallsymphony.util.StyleConfig;
import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private MainFrame frame;
    private JTextField userField;
    private JPasswordField passField;

    public LoginPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new GridBagLayout());
        setBackground(StyleConfig.BACKGROUND_COLOR);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setOpaque(false);

        JLabel title = new JLabel("Hall Symphony Login", SwingConstants.CENTER);
        title.setFont(StyleConfig.TITLE_FONT);
        title.setForeground(StyleConfig.PRIMARY_COLOR);

        userField = new JTextField(20);
        userField.setBorder(BorderFactory.createTitledBorder("Username"));

        passField = new JPasswordField(20);
        passField.setBorder(BorderFactory.createTitledBorder("Password"));

        JButton loginBtn = new JButton("Login");
        StyleConfig.styleButton(loginBtn);
        loginBtn.addActionListener(e -> handleLogin());

        JButton regBtn = new JButton("Register as Customer");
        regBtn.setFont(StyleConfig.SMALL_FONT);
        regBtn.setBorder(null);
        regBtn.setContentAreaFilled(false);
        regBtn.setForeground(StyleConfig.PRIMARY_COLOR);
        regBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        regBtn.addActionListener(e -> frame.showScreen("REGISTER"));

        panel.add(title);
        panel.add(userField);
        panel.add(passField);
        panel.add(loginBtn);
        panel.add(regBtn);

        add(panel);
    }

    private void handleLogin() {
        String username = userField.getText();
        String password = new String(passField.getPassword());

        AuthService.login(username, password).ifPresentOrElse(
            user -> frame.loginSuccess(user),
            () -> JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE)
        );
    }
}
