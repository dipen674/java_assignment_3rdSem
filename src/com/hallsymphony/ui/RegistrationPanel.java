package com.hallsymphony.ui;

import com.hallsymphony.service.AuthService;
import com.hallsymphony.util.StyleConfig;
import javax.swing.*;
import java.awt.*;

public class RegistrationPanel extends JPanel {
    private MainFrame frame;
    private JTextField userField, nameField, contactField;
    private JPasswordField passField;

    public RegistrationPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new GridBagLayout());
        setBackground(StyleConfig.BACKGROUND_COLOR);

        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setOpaque(false);

        JLabel title = new JLabel("Customer Registration", SwingConstants.CENTER);
        title.setFont(StyleConfig.TITLE_FONT);
        title.setForeground(StyleConfig.PRIMARY_COLOR);

        userField = new JTextField(20);
        userField.setBorder(BorderFactory.createTitledBorder("Username"));

        passField = new JPasswordField(20);
        passField.setBorder(BorderFactory.createTitledBorder("Password"));

        nameField = new JTextField(20);
        nameField.setBorder(BorderFactory.createTitledBorder("Full Name"));

        contactField = new JTextField(20);
        contactField.setBorder(BorderFactory.createTitledBorder("Contact Number"));

        JButton regBtn = new JButton("Register");
        StyleConfig.styleButton(regBtn);
        regBtn.addActionListener(e -> handleRegistration());

        JButton backBtn = new JButton("Back to Login");
        backBtn.setFont(StyleConfig.SMALL_FONT);
        backBtn.setContentAreaFilled(false);
        backBtn.setBorder(null);
        backBtn.addActionListener(e -> frame.showScreen("LOGIN"));

        panel.add(title);
        panel.add(userField);
        panel.add(passField);
        panel.add(nameField);
        panel.add(contactField);
        panel.add(regBtn);
        panel.add(backBtn);

        add(panel);
    }

    private void handleRegistration() {
        if (AuthService.registerCustomer(userField.getText(), new String(passField.getPassword()), nameField.getText(), contactField.getText())) {
            JOptionPane.showMessageDialog(this, "Registration Successful!");
            frame.showScreen("LOGIN");
        } else {
            JOptionPane.showMessageDialog(this, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
