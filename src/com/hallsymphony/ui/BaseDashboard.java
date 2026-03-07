package com.hallsymphony.ui;

import com.hallsymphony.model.User;
import com.hallsymphony.util.StyleConfig;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class BaseDashboard extends JPanel {
    protected MainFrame frame;
    protected User user;
    protected JPanel contentArea;

    public BaseDashboard(MainFrame frame, User user) {
        this.frame = frame;
        this.user = user;
        setLayout(new BorderLayout());
        setBackground(StyleConfig.BACKGROUND_COLOR);

        // ═══════════════════ SIDEBAR ═══════════════════
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(StyleConfig.SECONDARY_COLOR);
        sidebar.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Logo area
        JPanel logoArea = new JPanel();
        logoArea.setLayout(new BoxLayout(logoArea, BoxLayout.Y_AXIS));
        logoArea.setBackground(StyleConfig.SECONDARY_COLOR);
        logoArea.setBorder(new EmptyBorder(24, 16, 24, 16));
        logoArea.setMaximumSize(new Dimension(220, 100));

        JLabel logoIcon = new JLabel("\uD83C\uDFDB");
        logoIcon.setFont(new Font("SansSerif", Font.PLAIN, 28));
        logoIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoArea.add(logoIcon);
        logoArea.add(Box.createVerticalStrut(4));

        JLabel logo = new JLabel("Hall Symphony");
        logo.setForeground(StyleConfig.WHITE);
        logo.setFont(new Font("SansSerif", Font.BOLD, 18));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoArea.add(logo);

        sidebar.add(logoArea);

        // Separator
        JSeparator sidebarSep = new JSeparator();
        sidebarSep.setForeground(StyleConfig.SIDEBAR_HOVER);
        sidebarSep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sidebar.add(sidebarSep);
        sidebar.add(Box.createVerticalStrut(12));

        // Navigation label
        JLabel navLabel = new JLabel("   NAVIGATION");
        navLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        navLabel.setForeground(new Color(148, 163, 184)); // Slate-400
        navLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        navLabel.setBorder(new EmptyBorder(0, 16, 8, 0));
        sidebar.add(navLabel);

        addSidebarButtons(sidebar);

        sidebar.add(Box.createVerticalGlue());

        // User info at bottom
        JSeparator bottomSep = new JSeparator();
        bottomSep.setForeground(StyleConfig.SIDEBAR_HOVER);
        bottomSep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sidebar.add(bottomSep);

        JPanel userInfo = new JPanel();
        userInfo.setLayout(new BoxLayout(userInfo, BoxLayout.Y_AXIS));
        userInfo.setBackground(StyleConfig.SECONDARY_COLOR);
        userInfo.setBorder(new EmptyBorder(12, 16, 8, 16));
        userInfo.setMaximumSize(new Dimension(220, 60));

        JLabel userName = new JLabel(user.getFullName());
        userName.setForeground(StyleConfig.WHITE);
        userName.setFont(new Font("SansSerif", Font.BOLD, 13));
        userName.setAlignmentX(Component.LEFT_ALIGNMENT);
        userInfo.add(userName);

        JLabel userRole = new JLabel(user.getRole());
        userRole.setForeground(new Color(148, 163, 184));
        userRole.setFont(new Font("SansSerif", Font.PLAIN, 12));
        userRole.setAlignmentX(Component.LEFT_ALIGNMENT);
        userInfo.add(userRole);

        sidebar.add(userInfo);

        // Logout button
        JButton logoutBtn = new JButton("  \u2190  Sign Out");
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutBtn.setMaximumSize(new Dimension(200, 40));
        logoutBtn.setBackground(StyleConfig.ACCENT_COLOR);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(new CompoundBorder(
            new LineBorder(StyleConfig.ACCENT_HOVER, 1, true),
            new EmptyBorder(8, 16, 8, 16)
        ));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setOpaque(true);
        logoutBtn.addActionListener(e -> frame.logout());
        logoutBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { logoutBtn.setBackground(StyleConfig.ACCENT_HOVER); }
            public void mouseExited(MouseEvent e) { logoutBtn.setBackground(StyleConfig.ACCENT_COLOR); }
        });

        sidebar.add(logoutBtn);
        sidebar.add(Box.createVerticalStrut(16));

        // ═══════════════════ HEADER ═══════════════════
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(StyleConfig.WHITE);
        header.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, StyleConfig.BORDER_COLOR),
            new EmptyBorder(14, 24, 14, 24)
        ));

        JLabel welcomeLabel = new JLabel("Welcome back, " + user.getFullName());
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        welcomeLabel.setForeground(StyleConfig.TEXT_COLOR);
        header.add(welcomeLabel, BorderLayout.WEST);

        JLabel roleTag = new JLabel(user.getRole().toUpperCase());
        roleTag.setFont(new Font("SansSerif", Font.BOLD, 12));
        roleTag.setForeground(StyleConfig.PRIMARY_COLOR);
        roleTag.setOpaque(true);
        roleTag.setBackground(StyleConfig.PRIMARY_LIGHT);
        roleTag.setBorder(new CompoundBorder(
            new LineBorder(StyleConfig.PRIMARY_COLOR, 1, true),
            new EmptyBorder(4, 12, 4, 12)
        ));
        header.add(roleTag, BorderLayout.EAST);

        // ═══════════════════ CONTENT AREA ═══════════════════
        contentArea = new JPanel(new CardLayout());
        contentArea.setBackground(StyleConfig.BACKGROUND_COLOR);
        contentArea.setBorder(new EmptyBorder(16, 16, 16, 16));

        add(sidebar, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);
        add(contentArea, BorderLayout.CENTER);
    }

    protected abstract void addSidebarButtons(JPanel sidebar);

    protected void addSidebarButton(JPanel sidebar, String text, String cardName) {
        JButton btn = new JButton("   " + text);
        btn.setMaximumSize(new Dimension(200, 42));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBackground(StyleConfig.SECONDARY_COLOR);
        btn.setForeground(new Color(203, 213, 225)); // Slate-300
        btn.setFont(new Font("SansSerif", Font.PLAIN, 15));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(StyleConfig.SIDEBAR_HOVER);
                btn.setForeground(StyleConfig.WHITE);
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(StyleConfig.SECONDARY_COLOR);
                btn.setForeground(new Color(203, 213, 225));
            }
        });

        btn.addActionListener(e -> {
            CardLayout cl = (CardLayout) contentArea.getLayout();
            cl.show(contentArea, cardName);
        });

        sidebar.add(btn);
        sidebar.add(Box.createVerticalStrut(2));
    }
}
