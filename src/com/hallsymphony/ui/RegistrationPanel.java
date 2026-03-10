package com.hallsymphony.ui;

import com.hallsymphony.service.AuthService;
import com.hallsymphony.util.StyleConfig;
import com.hallsymphony.ui.components.HallButton;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * RegistrationPanel — Premium new-account screen.
 * Mirrors LoginPanel's split-panel design for a coherent onboarding flow.
 */
public class RegistrationPanel extends JPanel {
    private final MainFrame frame;
    private JTextField nameField, userField, contactField;
    private JPasswordField passField;

    public RegistrationPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildBrandPanel(), buildFormPanel());
        split.setDividerLocation(420);
        split.setDividerSize(0);
        split.setBorder(null);
        split.setEnabled(false);
        add(split, BorderLayout.CENTER);
    }

    // ── Left: branding panel (same style as LoginPanel) ───────────────────────
    private JPanel buildBrandPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                    0, 0,            new Color(15, 23, 42),
                    getWidth(), getHeight(), new Color(22, 163, 74));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(255, 255, 255, 15));
                g2.fillOval(-80, -80, 340, 340);
                g2.fillOval(getWidth() - 160, getHeight() - 200, 320, 320);
                g2.dispose();
            }
        };
        panel.setLayout(new GridBagLayout());
        panel.setPreferredSize(new Dimension(420, 0));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(0, 48, 0, 48));

        JLabel icon = new JLabel("\uD83D\uDCDD");
        icon.setFont(new Font("Dialog", Font.PLAIN, 64));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(icon);
        content.add(Box.createVerticalStrut(24));

        JLabel title = new JLabel("Join Hall Symphony");
        title.setFont(new Font("Dialog", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(title);
        content.add(Box.createVerticalStrut(10));

        JLabel subtitle = new JLabel("<html><center>Create your account and start<br>booking halls instantly</center></html>");
        subtitle.setFont(new Font("Dialog", Font.PLAIN, 14));
        subtitle.setForeground(new Color(167, 243, 208)); // Green-200
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(subtitle);
        content.add(Box.createVerticalStrut(40));

        String[] features = { "✓  Free customer account", "✓  Book any available hall", "✓  Instant booking receipt" };
        for (String f : features) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            row.setOpaque(false);
            row.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel lbl = new JLabel(f);
            lbl.setFont(new Font("Dialog", Font.PLAIN, 13));
            lbl.setForeground(new Color(187, 247, 208));
            row.add(lbl);
            content.add(row);
            content.add(Box.createVerticalStrut(8));
        }

        panel.add(content);
        return panel;
    }

    // ── Right: form panel ──────────────────────────────────────────────────────
    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(0, 52, 0, 52));
        form.setMaximumSize(new Dimension(400, Integer.MAX_VALUE));

        JLabel greeting = new JLabel("Get started \uD83C\uDF89");
        greeting.setFont(new Font("Dialog", Font.PLAIN, 14));
        greeting.setForeground(StyleConfig.TEXT_MUTED);
        greeting.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(greeting);
        form.add(Box.createVerticalStrut(6));

        JLabel heading = new JLabel("Create your account");
        heading.setFont(new Font("Dialog", Font.BOLD, 26));
        heading.setForeground(StyleConfig.TEXT_COLOR);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(heading);
        form.add(Box.createVerticalStrut(28));

        // Fields
        form.add(makeLabel("Full Name"));
        form.add(Box.createVerticalStrut(5));
        nameField = LoginPanel.createInput(false);
        form.add(nameField);
        form.add(Box.createVerticalStrut(16));

        form.add(makeLabel("Username"));
        form.add(Box.createVerticalStrut(5));
        userField = LoginPanel.createInput(false);
        form.add(userField);
        form.add(Box.createVerticalStrut(16));

        form.add(makeLabel("Password"));
        form.add(Box.createVerticalStrut(5));
        passField = (JPasswordField) LoginPanel.createInput(true);
        form.add(passField);
        form.add(Box.createVerticalStrut(16));

        form.add(makeLabel("Contact Number"));
        form.add(Box.createVerticalStrut(5));
        contactField = LoginPanel.createInput(false);
        form.add(contactField);
        form.add(Box.createVerticalStrut(28));

        HallButton regBtn = HallButton.success("Create Account →");
        regBtn.setFont(new Font("Dialog", Font.BOLD, 15));
        regBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        regBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        regBtn.addActionListener(e -> handleRegistration());
        form.add(regBtn);
        form.add(Box.createVerticalStrut(18));

        JPanel linkRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        linkRow.setOpaque(false);
        linkRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel linkText = new JLabel("Already have an account? ");
        linkText.setFont(StyleConfig.SMALL_FONT);
        linkText.setForeground(StyleConfig.TEXT_MUTED);
        HallButton backBtn = HallButton.ghost("Sign in");
        backBtn.setFont(StyleConfig.SMALL_BOLD);
        backBtn.setBorder(new EmptyBorder(0, 0, 0, 0));
        backBtn.addActionListener(e -> frame.showScreen("LOGIN"));
        linkRow.add(linkText);
        linkRow.add(backBtn);
        form.add(linkRow);

        panel.add(form, new GridBagConstraints());
        return panel;
    }

    private JLabel makeLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(StyleConfig.SMALL_BOLD);
        lbl.setForeground(StyleConfig.TEXT_SECONDARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private void handleRegistration() {
        String fullName = nameField.getText().trim();
        String username = userField.getText().trim();
        String password = new String(passField.getPassword());
        String contact  = contactField.getText().trim();

        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || contact.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.",
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (AuthService.registerCustomer(username, password, fullName, contact)) {
            JOptionPane.showMessageDialog(this, "Registration successful! You can now sign in.",
                "Success \uD83C\uDF89", JOptionPane.INFORMATION_MESSAGE);
            frame.showScreen("LOGIN");
        } else {
            JOptionPane.showMessageDialog(this,
                "Username already exists. Please choose a different one.",
                "Registration Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
