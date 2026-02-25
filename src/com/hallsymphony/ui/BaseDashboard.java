package com.hallsymphony.ui;

import com.hallsymphony.model.User;
import com.hallsymphony.util.StyleConfig;
import javax.swing.*;
import java.awt.*;

public abstract class BaseDashboard extends JPanel {
    protected MainFrame frame;
    protected User user;
    protected JPanel contentArea;

    public BaseDashboard(MainFrame frame, User user) {
        this.frame = frame;
        this.user = user;
        setLayout(new BorderLayout());
        setBackground(StyleConfig.BACKGROUND_COLOR);

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBackground(StyleConfig.SECONDARY_COLOR);

        JLabel logo = new JLabel("Hall Symphony");
        logo.setForeground(StyleConfig.WHITE);
        logo.setFont(StyleConfig.HEADER_FONT);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logo.setBorder(BorderFactory.createEmptyBorder(20, 0, 40, 0));
        sidebar.add(logo);

        addSidebarButtons(sidebar);

        sidebar.add(Box.createVerticalGlue());
        
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutBtn.setBackground(StyleConfig.ACCENT_COLOR);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.addActionListener(e -> frame.logout());
        sidebar.add(logoutBtn);
        sidebar.add(Box.createVerticalStrut(20));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(StyleConfig.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel welcomeLabel = new JLabel("Welcome, " + user.getFullName() + " (" + user.getRole() + ")");
        welcomeLabel.setFont(StyleConfig.HEADER_FONT);
        header.add(welcomeLabel, BorderLayout.WEST);

        // Content Area
        contentArea = new JPanel(new CardLayout());
        contentArea.setBackground(StyleConfig.BACKGROUND_COLOR);

        add(sidebar, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);
        add(contentArea, BorderLayout.CENTER);
    }

    protected abstract void addSidebarButtons(JPanel sidebar);

    protected void addSidebarButton(JPanel sidebar, String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(180, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(StyleConfig.SECONDARY_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        btn.addActionListener(e -> {
            CardLayout cl = (CardLayout) contentArea.getLayout();
            cl.show(contentArea, cardName);
        });
        sidebar.add(btn);
        sidebar.add(Box.createVerticalStrut(10));
    }
}
