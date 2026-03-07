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

        refreshStaffTable(model, "");

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(15);
        JButton searchBtn = new JButton("Filter");
        filterPanel.add(new JLabel("Search Staff:"));
        filterPanel.add(searchField);
        filterPanel.add(searchBtn);
        searchBtn.addActionListener(e -> refreshStaffTable(model, searchField.getText()));

        JPanel btnPanel = new JPanel();
        JButton addBtn = new JButton("Add Scheduler");
        JButton editBtn = new JButton("Edit Staff");
        JButton deleteBtn = new JButton("Delete");

        addBtn.addActionListener(e -> showStaffForm(null, model));
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) model.getValueAt(row, 0);
                User staff = AuthService.getAllUsers().stream().filter(u -> u.getId().equals(id)).findFirst().get();
                showStaffForm(staff, model);
            }
        });
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                AuthService.deleteUser((String) model.getValueAt(row, 0));
                refreshStaffTable(model, "");
            }
        });

        btnPanel.add(addBtn); btnPanel.add(editBtn); btnPanel.add(deleteBtn);

        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void showStaffForm(User staff, DefaultTableModel model) {
        JTextField uField = new JTextField(staff != null ? staff.getUsername() : "");
        JTextField pField = new JTextField(staff != null ? staff.getPassword() : "");
        JTextField nField = new JTextField(staff != null ? staff.getFullName() : "");
        JTextField cField = new JTextField(staff != null ? staff.getContact() : "");
        
        Object[] message = { "Username:", uField, "Password:", pField, "Name:", nField, "Contact:", cField };
        String title = staff == null ? "Add Scheduler" : "Edit Staff";
        
        if (JOptionPane.showConfirmDialog(null, message, title, JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            if (staff == null) {
                AuthService.addStaff(uField.getText(), pField.getText(), nField.getText(), cField.getText(), "Scheduler");
            } else {
                AuthService.updateProfile(staff.getId(), nField.getText(), cField.getText());
                // In a real app, you might also update username/password
            }
            refreshStaffTable(model, "");
        }
    }

    private void refreshStaffTable(DefaultTableModel model, String filter) {
        model.setRowCount(0);
        AuthService.getAllUsers().stream()
                .filter(u -> u instanceof Scheduler)
                .filter(u -> filter.isEmpty() || u.getFullName().toLowerCase().contains(filter.toLowerCase()) || u.getUsername().toLowerCase().contains(filter.toLowerCase()))
                .forEach(u -> model.addRow(new Object[]{u.getId(), u.getUsername(), u.getRole(), u.getFullName()}));
    }

    private JPanel createUserManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = {"ID", "Username", "Type", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        refreshUserTable(model, "");
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(15);
        JButton searchBtn = new JButton("Filter");
        filterPanel.add(new JLabel("Search Users:"));
        filterPanel.add(searchField);
        filterPanel.add(searchBtn);
        searchBtn.addActionListener(e -> refreshUserTable(model, searchField.getText()));

        JButton deleteUserBtn = new JButton("Delete User");
        deleteUserBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) model.getValueAt(row, 0);
                if (AuthService.deleteUser(id)) {
                    JOptionPane.showMessageDialog(this, "User deleted successfully!");
                    refreshUserTable(model, "");
                }
            }
        });

        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(deleteUserBtn, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshUserTable(DefaultTableModel model, String filter) {
        model.setRowCount(0);
        AuthService.getAllUsers().stream()
                .filter(u -> filter.isEmpty() || u.getFullName().toLowerCase().contains(filter.toLowerCase()) || u.getUsername().toLowerCase().contains(filter.toLowerCase()))
                .forEach(u -> model.addRow(new Object[]{u.getId(), u.getUsername(), u.getRole(), "Active"}));
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
