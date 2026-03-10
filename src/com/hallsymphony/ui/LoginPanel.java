package com.hallsymphony.ui;

import com.hallsymphony.service.AuthService;
import com.hallsymphony.util.StyleConfig;
import com.hallsymphony.ui.components.HallButton;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * LoginPanel — Premium sign-in screen.
 *
 * Left panel: vibrant blue gradient with branding.
 * Right panel: white card with clean form.
 * Fully consistent on Windows / Linux / WSL via Metal L&F + custom painting.
 */
public class LoginPanel extends JPanel {
    private final MainFrame frame;
    private JTextField userField;
    private JPasswordField passField;

    public LoginPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(StyleConfig.BACKGROUND_COLOR);

        // Split layout: left branding panel + right form panel
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildBrandPanel(), buildFormPanel());
        split.setDividerLocation(420);
        split.setDividerSize(0);
        split.setBorder(null);
        split.setEnabled(false); // not draggable
        add(split, BorderLayout.CENTER);
    }

    // ── Left: Gradient branding panel ────────────────────────────────────────
    private JPanel buildBrandPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Deep navy → rich blue diagonal gradient
                GradientPaint gp = new GradientPaint(
                    0, 0,            new Color(15, 23, 42),
                    getWidth(), getHeight(), new Color(37, 99, 235));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Decorative circles
                g2.setColor(new Color(255, 255, 255, 15));
                g2.fillOval(-80, -80, 340, 340);
                g2.fillOval(getWidth() - 160, getHeight() - 200, 320, 320);
                g2.setColor(new Color(255, 255, 255, 8));
                g2.fillOval(60, getHeight() - 140, 220, 220);
                g2.dispose();
            }
        };
        panel.setLayout(new GridBagLayout());
        panel.setPreferredSize(new Dimension(420, 0));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(0, 48, 0, 48));

        JLabel icon = new JLabel("\uD83C\uDFDB");
        icon.setFont(new Font("Dialog", Font.PLAIN, 64));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(icon);
        content.add(Box.createVerticalStrut(24));

        JLabel title = new JLabel("Hall Symphony");
        title.setFont(new Font("Dialog", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(title);
        content.add(Box.createVerticalStrut(10));

        JLabel subtitle = new JLabel("<html><center>Hall Booking Management<br>System</center></html>");
        subtitle.setFont(new Font("Dialog", Font.PLAIN, 15));
        subtitle.setForeground(new Color(147, 197, 253)); // Blue-300
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(subtitle);
        content.add(Box.createVerticalStrut(40));

        // Feature pills
        String[] features = { "✓  Multi-role access control", "✓  Real-time availability", "✓  Instant receipts" };
        for (String f : features) {
            JPanel pill = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            pill.setOpaque(false);
            pill.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel lbl = new JLabel(f);
            lbl.setFont(new Font("Dialog", Font.PLAIN, 13));
            lbl.setForeground(new Color(186, 230, 253)); // Blue-200
            pill.add(lbl);
            content.add(pill);
            content.add(Box.createVerticalStrut(8));
        }

        panel.add(content);
        return panel;
    }

    // ── Right: White form panel ───────────────────────────────────────────────
    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(0, 52, 0, 52));
        form.setMaximumSize(new Dimension(400, Integer.MAX_VALUE));

        // Header
        JLabel greeting = new JLabel("Welcome back \uD83D\uDC4B");
        greeting.setFont(new Font("Dialog", Font.PLAIN, 14));
        greeting.setForeground(StyleConfig.TEXT_MUTED);
        greeting.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(greeting);
        form.add(Box.createVerticalStrut(6));

        JLabel heading = new JLabel("Sign in to continue");
        heading.setFont(new Font("Dialog", Font.BOLD, 26));
        heading.setForeground(StyleConfig.TEXT_COLOR);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(heading);
        form.add(Box.createVerticalStrut(36));

        // Username
        JLabel userLbl = new JLabel("Username");
        userLbl.setFont(StyleConfig.SMALL_BOLD);
        userLbl.setForeground(StyleConfig.TEXT_SECONDARY);
        userLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(userLbl);
        form.add(Box.createVerticalStrut(6));

        userField = createStyledInput(false);
        form.add(userField);
        form.add(Box.createVerticalStrut(20));

        // Password
        JLabel passLbl = new JLabel("Password");
        passLbl.setFont(StyleConfig.SMALL_BOLD);
        passLbl.setForeground(StyleConfig.TEXT_SECONDARY);
        passLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(passLbl);
        form.add(Box.createVerticalStrut(6));

        passField = (JPasswordField) createStyledInput(true);
        form.add(passField);
        form.add(Box.createVerticalStrut(32));

        // Sign In button
        HallButton loginBtn = HallButton.primary("Sign In →");
        loginBtn.setFont(new Font("Dialog", Font.BOLD, 15));
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginBtn.addActionListener(e -> handleLogin());
        form.add(loginBtn);
        form.add(Box.createVerticalStrut(20));

        // Divider
        JPanel divider = new JPanel(new BorderLayout(8, 0));
        divider.setOpaque(false);
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        JSeparator s1 = new JSeparator(); s1.setForeground(StyleConfig.BORDER_COLOR);
        JSeparator s2 = new JSeparator(); s2.setForeground(StyleConfig.BORDER_COLOR);
        JLabel orLbl = new JLabel("OR"); orLbl.setFont(StyleConfig.SMALL_FONT);
        orLbl.setForeground(StyleConfig.TEXT_MUTED); orLbl.setHorizontalAlignment(SwingConstants.CENTER);
        divider.add(s1, BorderLayout.WEST); divider.add(orLbl, BorderLayout.CENTER); divider.add(s2, BorderLayout.EAST);
        form.add(divider);
        form.add(Box.createVerticalStrut(16));

        // Register link
        JPanel linkRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        linkRow.setOpaque(false);
        linkRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel linkText = new JLabel("Don't have an account? ");
        linkText.setFont(StyleConfig.SMALL_FONT);
        linkText.setForeground(StyleConfig.TEXT_MUTED);
        HallButton regBtn = HallButton.ghost("Register here");
        regBtn.setFont(StyleConfig.SMALL_BOLD);
        regBtn.setBorder(new EmptyBorder(0, 0, 0, 0));
        regBtn.addActionListener(e -> frame.showScreen("REGISTER"));
        linkRow.add(linkText);
        linkRow.add(regBtn);
        form.add(linkRow);

        // Key bindings
        passField.addActionListener(e -> handleLogin());
        userField.addActionListener(e -> passField.requestFocusInWindow());

        panel.add(form, new GridBagConstraints());
        return panel;
    }

    /** Creates a styled, borderless flat input field with a bottom-line accent. */
    private JTextField createStyledInput(boolean isPassword) {
        JTextField field = isPassword ? new JPasswordField() : new JTextField();
        field.setFont(new Font("Dialog", Font.PLAIN, 15));
        field.setForeground(StyleConfig.TEXT_COLOR);
        field.setBackground(StyleConfig.PRIMARY_XLIGHT);
        field.setCaretColor(StyleConfig.PRIMARY_COLOR);
        field.setBorder(new CompoundBorder(
            new CompoundBorder(
                new LineBorder(StyleConfig.BORDER_COLOR, 1, true),
                new MatteBorder(0, 0, 2, 0, StyleConfig.PRIMARY_COLOR)
            ),
            new EmptyBorder(10, 14, 10, 14)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Focus border accent
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                field.setBackground(Color.WHITE);
                field.setBorder(new CompoundBorder(
                    new CompoundBorder(
                        new LineBorder(StyleConfig.PRIMARY_COLOR, 1, true),
                        new MatteBorder(0, 0, 2, 0, StyleConfig.PRIMARY_COLOR)
                    ),
                    new EmptyBorder(10, 14, 10, 14)
                ));
            }
            public void focusLost(FocusEvent e) {
                field.setBackground(StyleConfig.PRIMARY_XLIGHT);
                field.setBorder(new CompoundBorder(
                    new CompoundBorder(
                        new LineBorder(StyleConfig.BORDER_COLOR, 1, true),
                        new MatteBorder(0, 0, 2, 0, StyleConfig.PRIMARY_COLOR)
                    ),
                    new EmptyBorder(10, 14, 10, 14)
                ));
            }
        });
        return field;
    }

    // Exposes the factory method for RegistrationPanel to reuse
    static JTextField createInput(boolean isPassword) {
        JTextField field = isPassword ? new JPasswordField() : new JTextField();
        field.setFont(new Font("Dialog", Font.PLAIN, 15));
        field.setForeground(StyleConfig.TEXT_COLOR);
        field.setBackground(StyleConfig.PRIMARY_XLIGHT);
        field.setCaretColor(StyleConfig.PRIMARY_COLOR);
        field.setBorder(new CompoundBorder(
            new CompoundBorder(
                new LineBorder(StyleConfig.BORDER_COLOR, 1, true),
                new MatteBorder(0, 0, 2, 0, StyleConfig.PRIMARY_COLOR)),
            new EmptyBorder(10, 14, 10, 14)));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                field.setBackground(Color.WHITE);
                field.setBorder(new CompoundBorder(
                    new CompoundBorder(
                        new LineBorder(StyleConfig.PRIMARY_COLOR, 1, true),
                        new MatteBorder(0, 0, 2, 0, StyleConfig.PRIMARY_COLOR)),
                    new EmptyBorder(10, 14, 10, 14)));
            }
            public void focusLost(FocusEvent e) {
                field.setBackground(StyleConfig.PRIMARY_XLIGHT);
                field.setBorder(new CompoundBorder(
                    new CompoundBorder(
                        new LineBorder(StyleConfig.BORDER_COLOR, 1, true),
                        new MatteBorder(0, 0, 2, 0, StyleConfig.PRIMARY_COLOR)),
                    new EmptyBorder(10, 14, 10, 14)));
            }
        });
        return field;
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
                "Login Failed", JOptionPane.ERROR_MESSAGE));
    }
}
