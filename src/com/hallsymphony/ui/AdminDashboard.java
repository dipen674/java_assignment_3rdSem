package com.hallsymphony.ui;

import com.hallsymphony.model.*;
import com.hallsymphony.service.*;
import com.hallsymphony.util.StyleConfig;
import javax.swing.*;
import javax.swing.border.*;
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
        addSidebarButton(sidebar, "\uD83D\uDC65  Staff Management", "STAFF");
        addSidebarButton(sidebar, "\uD83D\uDC64  User Management", "USERS");
        addSidebarButton(sidebar, "\uD83D\uDCCB  All Bookings", "BOOKINGS");
    }

    private void initContent() {
        contentArea.add(createStaffManagementPanel(), "STAFF");
        contentArea.add(createUserManagementPanel(), "USERS");
        contentArea.add(createCentralBookingPanel(), "BOOKINGS");
    }

    // ═══════════════════ STAFF MANAGEMENT ═══════════════════
    private JPanel createStaffManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(StyleConfig.BACKGROUND_COLOR);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(StyleConfig.BACKGROUND_COLOR);
        topPanel.setBorder(new EmptyBorder(0, 0, 12, 0));
        topPanel.add(StyleConfig.createSectionTitle("Scheduler Staff Management"), BorderLayout.WEST);

        JPanel filterPanel = StyleConfig.createFilterPanel();
        JTextField searchField = new JTextField(18);
        JButton searchBtn = new JButton("Search");
        StyleConfig.styleSecondaryButton(searchBtn);
        filterPanel.add(new JLabel("Search Staff: "));
        filterPanel.add(searchField);
        filterPanel.add(searchBtn);

        JPanel headerArea = new JPanel(new BorderLayout());
        headerArea.setBackground(StyleConfig.BACKGROUND_COLOR);
        headerArea.add(topPanel, BorderLayout.NORTH);
        headerArea.add(filterPanel, BorderLayout.SOUTH);
        panel.add(headerArea, BorderLayout.NORTH);

        String[] columns = {"ID", "Username", "Role", "Full Name", "Contact"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        refreshStaffTable(model, "");
        searchBtn.addActionListener(e -> refreshStaffTable(model, searchField.getText()));
        panel.add(StyleConfig.createStyledScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = StyleConfig.createButtonPanel();
        JButton addBtn = new JButton("Add Scheduler");
        StyleConfig.styleSuccessButton(addBtn);
        JButton editBtn = new JButton("Edit Staff");
        StyleConfig.styleButton(editBtn);
        JButton deleteBtn = new JButton("Delete");
        StyleConfig.styleDangerButton(deleteBtn);

        addBtn.addActionListener(e -> showStaffForm(null, model));
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) model.getValueAt(row, 0);
                User staff = AuthService.getAllUsers().stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
                if (staff != null) showStaffForm(staff, model);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a staff member first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this staff member?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    AuthService.deleteUser((String) model.getValueAt(row, 0));
                    refreshStaffTable(model, "");
                }
            }
        });

        btnPanel.add(addBtn);
        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void showStaffForm(User staff, DefaultTableModel model) {
        JTextField uField = new JTextField(staff != null ? staff.getUsername() : "");
        JTextField pField = new JTextField(staff != null ? staff.getPassword() : "");
        JTextField nField = new JTextField(staff != null ? staff.getFullName() : "");
        JTextField cField = new JTextField(staff != null ? staff.getContact() : "");

        StyleConfig.styleTextField(uField, "Username");
        StyleConfig.styleTextField(pField, "Password");
        StyleConfig.styleTextField(nField, "Full Name");
        StyleConfig.styleTextField(cField, "Contact Number");

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.add(uField);
        formPanel.add(Box.createVerticalStrut(6));
        formPanel.add(pField);
        formPanel.add(Box.createVerticalStrut(6));
        formPanel.add(nField);
        formPanel.add(Box.createVerticalStrut(6));
        formPanel.add(cField);

        String title = staff == null ? "Add New Scheduler" : "Edit Staff Details";
        if (JOptionPane.showConfirmDialog(this, formPanel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
            if (staff == null) {
                if (AuthService.addStaff(uField.getText().trim(), pField.getText(), nField.getText().trim(), cField.getText().trim(), "Scheduler")) {
                    JOptionPane.showMessageDialog(this, "Scheduler added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                AuthService.updateProfile(staff.getId(), nField.getText().trim(), cField.getText().trim());
                JOptionPane.showMessageDialog(this, "Staff updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            refreshStaffTable(model, "");
        }
    }

    private void refreshStaffTable(DefaultTableModel model, String filter) {
        model.setRowCount(0);
        AuthService.getAllUsers().stream()
                .filter(u -> u instanceof Scheduler)
                .filter(u -> filter.isEmpty() || u.getFullName().toLowerCase().contains(filter.toLowerCase()) || u.getUsername().toLowerCase().contains(filter.toLowerCase()))
                .forEach(u -> model.addRow(new Object[]{u.getId(), u.getUsername(), u.getRole(), u.getFullName(), u.getContact()}));
    }

    // ═══════════════════ USER MANAGEMENT ═══════════════════
    private JPanel createUserManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(StyleConfig.BACKGROUND_COLOR);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(StyleConfig.BACKGROUND_COLOR);
        topPanel.setBorder(new EmptyBorder(0, 0, 12, 0));
        topPanel.add(StyleConfig.createSectionTitle("All Users"), BorderLayout.WEST);

        JPanel filterPanel = StyleConfig.createFilterPanel();
        JTextField searchField = new JTextField(18);
        JButton searchBtn = new JButton("Search");
        StyleConfig.styleSecondaryButton(searchBtn);
        filterPanel.add(new JLabel("Search Users: "));
        filterPanel.add(searchField);
        filterPanel.add(searchBtn);

        JPanel headerArea = new JPanel(new BorderLayout());
        headerArea.setBackground(StyleConfig.BACKGROUND_COLOR);
        headerArea.add(topPanel, BorderLayout.NORTH);
        headerArea.add(filterPanel, BorderLayout.SOUTH);
        panel.add(headerArea, BorderLayout.NORTH);

        String[] columns = {"ID", "Username", "Role", "Full Name", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        refreshUserTable(model, "");
        searchBtn.addActionListener(e -> refreshUserTable(model, searchField.getText()));
        panel.add(StyleConfig.createStyledScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = StyleConfig.createButtonPanel();
        JButton deleteUserBtn = new JButton("Delete User");
        StyleConfig.styleDangerButton(deleteUserBtn);
        deleteUserBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) model.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (AuthService.deleteUser(id)) {
                        JOptionPane.showMessageDialog(this, "User deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        refreshUserTable(model, "");
                    }
                }
            }
        });
        btnPanel.add(deleteUserBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshUserTable(DefaultTableModel model, String filter) {
        model.setRowCount(0);
        AuthService.getAllUsers().stream()
                .filter(u -> filter.isEmpty() || u.getFullName().toLowerCase().contains(filter.toLowerCase()) || u.getUsername().toLowerCase().contains(filter.toLowerCase()))
                .forEach(u -> model.addRow(new Object[]{u.getId(), u.getUsername(), u.getRole(), u.getFullName(), "Active"}));
    }

    // ═══════════════════ ALL BOOKINGS ═══════════════════
    private JPanel createCentralBookingPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(StyleConfig.BACKGROUND_COLOR);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(StyleConfig.BACKGROUND_COLOR);
        topPanel.setBorder(new EmptyBorder(0, 0, 12, 0));
        topPanel.add(StyleConfig.createSectionTitle("All Customer Bookings"), BorderLayout.WEST);

        JPanel filterPanel = StyleConfig.createFilterPanel();
        
        filterPanel.add(new JLabel("Status: "));
        JComboBox<String> statusFilter = new JComboBox<>(new String[]{"All", "PENDING", "PAID", "CANCELLED"});
        statusFilter.setPreferredSize(new Dimension(110, 32));
        filterPanel.add(statusFilter);

        filterPanel.add(new JLabel("  Time: "));
        JComboBox<String> timeFilter = new JComboBox<>(new String[]{"All", "Upcoming", "Past"});
        timeFilter.setPreferredSize(new Dimension(110, 32));
        filterPanel.add(timeFilter);

        JButton filterBtn = new JButton("Apply Filters");
        StyleConfig.styleSecondaryButton(filterBtn);
        filterPanel.add(filterBtn);

        JPanel headerArea = new JPanel(new BorderLayout());
        headerArea.setBackground(StyleConfig.BACKGROUND_COLOR);
        headerArea.add(topPanel, BorderLayout.NORTH);
        headerArea.add(filterPanel, BorderLayout.SOUTH);
        panel.add(headerArea, BorderLayout.NORTH);

        String[] columns = {"Booking ID", "Customer ID", "Hall ID", "Start", "End", "Status", "Price (RM)"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        refreshBookingTable(model, "All", "All");
        filterBtn.addActionListener(e -> refreshBookingTable(model, (String) statusFilter.getSelectedItem(), (String) timeFilter.getSelectedItem()));
        panel.add(StyleConfig.createStyledScrollPane(table), BorderLayout.CENTER);

        return panel;
    }

    private void refreshBookingTable(DefaultTableModel model, String statusFilter, String timeFilter) {
        model.setRowCount(0);
        java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        java.time.LocalDateTime now = java.time.LocalDateTime.now();

        BookingService.getAllBookings().stream()
                .filter(b -> statusFilter.equals("All") || b.getStatus().equals(statusFilter))
                .filter(b -> {
                    if (timeFilter.equals("Upcoming")) return b.getStartTime().isAfter(now);
                    if (timeFilter.equals("Past")) return b.getStartTime().isBefore(now);
                    return true;
                })
                .forEach(b -> model.addRow(new Object[]{b.getId(), b.getCustomerId(), b.getHallId(),
                    b.getStartTime().format(fmt), b.getEndTime().format(fmt), b.getStatus(), String.format("%.2f", b.getTotalPrice())}));
    }
}
