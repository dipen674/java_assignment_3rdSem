package com.hallsymphony.ui;

import com.hallsymphony.model.User;
import com.hallsymphony.util.StyleConfig;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private User currentUser;

    public MainFrame() {
        setTitle("Hall Symphony - Hall Booking Management System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize screens
        mainPanel.add(new LoginPanel(this), "LOGIN");
        mainPanel.add(new RegistrationPanel(this), "REGISTER");

        add(mainPanel);
        showScreen("LOGIN");
    }

    public void showScreen(String name) {
        cardLayout.show(mainPanel, name);
    }

    public void loginSuccess(User user) {
        this.currentUser = user;
        // Dynamically add dashboard based on role
        JPanel dashboard = null;
        switch (user.getRole()) {
            case "Customer": dashboard = new CustomerDashboard(this, (com.hallsymphony.model.Customer) user); break;
            case "Scheduler": dashboard = new SchedulerDashboard(this, (com.hallsymphony.model.Scheduler) user); break;
            case "Administrator": dashboard = new AdminDashboard(this, (com.hallsymphony.model.Administrator) user); break;
            case "Manager": dashboard = new ManagerDashboard(this, (com.hallsymphony.model.Manager) user); break;
        }
        
        if (dashboard != null) {
            mainPanel.add(dashboard, "DASHBOARD");
            showScreen("DASHBOARD");
        }
    }

    public void logout() {
        this.currentUser = null;
        showScreen("LOGIN");
    }

    public static void main(String[] args) {
        com.hallsymphony.util.DataInitializer.initialize();
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
