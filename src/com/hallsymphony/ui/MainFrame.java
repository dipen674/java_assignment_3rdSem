package com.hallsymphony.ui;

import com.hallsymphony.model.User;
import com.hallsymphony.util.StyleConfig;
import javax.swing.*;
import java.awt.*;

/**
 * MainFrame — Application window host.
 *
 * Size: 1200 × 760 (wide enough for the split-panel login + dashboards).
 * Minimum: 960 × 640.
 */
public class MainFrame extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    public MainFrame() {
        setTitle("Hall Symphony — Hall Booking Management System");
        setSize(1200, 760);
        setMinimumSize(new Dimension(960, 640));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel  = new JPanel(cardLayout);

        mainPanel.add(new LoginPanel(this),        "LOGIN");
        mainPanel.add(new RegistrationPanel(this), "REGISTER");

        add(mainPanel);
        showScreen("LOGIN");
    }

    public void showScreen(String name) {
        cardLayout.show(mainPanel, name);
    }

    public void loginSuccess(User user) {
        JPanel dashboard = switch (user.getRole().trim()) {
            case "Customer"      -> new CustomerDashboard(this, (com.hallsymphony.model.Customer) user);
            case "Scheduler"     -> new SchedulerDashboard(this, (com.hallsymphony.model.Scheduler) user);
            case "Administrator" -> new AdminDashboard(this, (com.hallsymphony.model.Administrator) user);
            case "Manager"       -> new ManagerDashboard(this, (com.hallsymphony.model.Manager) user);
            default -> null;
        };
        if (dashboard != null) {
            mainPanel.add(dashboard, "DASHBOARD");
            showScreen("DASHBOARD");
        }
    }

    public void logout() {
        mainPanel.removeAll();
        mainPanel.add(new LoginPanel(this),        "LOGIN");
        mainPanel.add(new RegistrationPanel(this), "REGISTER");
        showScreen("LOGIN");
        SwingUtilities.updateComponentTreeUI(mainPanel);
    }

    public static void main(String[] args) {
        StyleConfig.applyGlobalTheme();
        com.hallsymphony.util.DataInitializer.initialize();
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
