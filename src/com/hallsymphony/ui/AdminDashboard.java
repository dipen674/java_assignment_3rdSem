package com.hallsymphony.ui;

import com.hallsymphony.model.*;
import com.hallsymphony.service.*;
import com.hallsymphony.util.StyleConfig;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminDashboard extends BaseDashboard {
    private Administrator admin;

    public AdminDashboard(MainFrame frame, Administrator admin) {
        super(frame, admin);
        this.admin = admin;
        initContent();
    }

    @Override
    protected void addSidebarButtons(JPanel sidebar) {
        addSidebarButton(sidebar, "Staff Management", "STAFF");
        addSidebarButton(sidebar, "User Management", "USERS");
        addSidebarButton(sidebar, "Central Bookings", "BOOKINGS");
    }

    private void initContent() {
        contentArea.add(createStaffManagementPanel(), "STAFF");
        contentArea.add(createUserManagementPanel(), "USERS");
        contentArea.add(createCentralBookingPanel(), "BOOKINGS");
    }

    private JPanel createStaffManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = {"ID", "Username", "Role", "Full Name"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        refreshStaffTable(model);

        JButton addBtn = new JButton("Add Scheduler Staff");
        addBtn.addActionListener(e -> {
            JTextField uField = new JTextField();
            JTextField pField = new JTextField();
            JTextField nField = new JTextField();
            JTextField cField = new JTextField();
            Object[] message = { "Username:", uField, "Password:", pField, "Name:", nField, "Contact:", cField };
            if (JOptionPane.showConfirmDialog(null, message, "Add Staff", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                AuthService.addStaff(uField.getText(), pField.getText(), nField.getText(), cField.getText(), "Scheduler");
                refreshStaffTable(model);
            }
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(addBtn, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshStaffTable(DefaultTableModel model) {
        model.setRowCount(0);
        AuthService.getAllUsers().stream()
                .filter(u -> u instanceof Scheduler)
                .forEach(u -> model.addRow(new Object[]{u.getId(), u.getUsername(), u.getRole(), u.getFullName()}));
    }

    private JPanel createUserManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = {"ID", "Username", "Type", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        refreshUserTable(model);
        panel.add(new JScrollPane(table));
        return panel;
    }

    private void refreshUserTable(DefaultTableModel model) {
        model.setRowCount(0);
        AuthService.getAllUsers().forEach(u -> model.addRow(new Object[]{u.getId(), u.getUsername(), u.getRole(), "Active"}));
    }

    private JPanel createCentralBookingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = {"ID", "Cust ID", "Hall ID", "Status", "Price"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        BookingService.getAllBookings().forEach(b -> model.addRow(new Object[]{b.getId(), b.getCustomerId(), b.getHallId(), b.getStatus(), b.getTotalPrice()}));
        panel.add(new JScrollPane(table));
        return panel;
    }
}
