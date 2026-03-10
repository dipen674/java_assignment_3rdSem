package com.hallsymphony.ui;

import com.hallsymphony.model.*;
import com.hallsymphony.service.*;
import com.hallsymphony.util.StyleConfig;
import com.hallsymphony.util.ValidationUtil;
import com.hallsymphony.ui.components.HallButton;
import javax.swing.*;
import com.hallsymphony.ui.components.HallButton;
import javax.swing.border.*;
import com.hallsymphony.ui.components.HallButton;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

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
        addSidebarButton(sidebar, "All Bookings", "BOOKINGS");
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
        topPanel.setBorder(new EmptyBorder(0, 0, 24, 0));
        topPanel.add(StyleConfig.createSectionTitle("Scheduler Staff Management"), BorderLayout.WEST);

        JPanel filterPanel = StyleConfig.createFilterPanel();
        JTextField searchField = new JTextField(18);
        HallButton searchBtn = HallButton.secondary("Search");
        
        filterPanel.add(new JLabel("Search: "));
        filterPanel.add(searchField);
        filterPanel.add(searchBtn);
        
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(new JLabel("Sort By: "));
        JComboBox<String> sortBox = new JComboBox<>(new String[]{"ID", "Username", "Name"});
        filterPanel.add(sortBox);

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
        searchField.addActionListener(e -> searchBtn.doClick());

        sortBox.addActionListener(e -> {
            int col = sortBox.getSelectedIndex() == 0 ? 0 : (sortBox.getSelectedIndex() == 1 ? 1 : 3);
            table.getRowSorter().setSortKeys(java.util.List.of(new RowSorter.SortKey(col, SortOrder.ASCENDING)));
        });

        panel.add(StyleConfig.createStyledScrollPane(table), BorderLayout.CENTER);
        
        JPanel btnPanel = StyleConfig.createActionBar();
        btnPanel.setBorder(new EmptyBorder(16, 0, 0, 0));
        HallButton addBtn = HallButton.success("Add Scheduler");
        
        HallButton editBtn = HallButton.primary("Edit Staff");
        
        HallButton deleteBtn = HallButton.danger("Delete");
        

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
            String user = uField.getText().trim();
            String pass = pField.getText();
            String name = nField.getText().trim();
            String contact = cField.getText().trim();

            if (user.isEmpty() || pass.isEmpty() || name.isEmpty() || contact.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            StringBuilder errors = new StringBuilder();
            if (!ValidationUtil.isValidUsername(user)) errors.append("\u2022 Username must be 4-20 chars (letters/numbers/_).\n");
            if (!ValidationUtil.isValidPassword(pass)) errors.append("\u2022 Password must be >= 6 chars.\n");
            if (!ValidationUtil.isValidName(name)) errors.append("\u2022 Name should only contain letters.\n");
            if (!ValidationUtil.isValidContact(contact)) errors.append("\u2022 Contact must be 10-12 digits.\n");

            if (errors.length() > 0) {
                JOptionPane.showMessageDialog(this, "Please fix errors:\n" + errors.toString(), "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (staff == null) {
                if (AuthService.addStaff(user, pass, name, contact, "Scheduler")) {
                    JOptionPane.showMessageDialog(this, "Scheduler added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                if (AuthService.updateProfile(staff.getId(), user, pass, name, contact)) {
                    JOptionPane.showMessageDialog(this, "Staff updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Update failed. Username may already be in use.", "Error", JOptionPane.ERROR_MESSAGE);
                }
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
        topPanel.setBorder(new EmptyBorder(0, 0, 24, 0));
        topPanel.add(StyleConfig.createSectionTitle("All Users"), BorderLayout.WEST);

        JPanel filterPanel = StyleConfig.createFilterPanel();
        JTextField searchField = new JTextField(18);
        HallButton searchBtn = HallButton.secondary("Search");
        
        filterPanel.add(new JLabel("Search: "));
        filterPanel.add(searchField);
        filterPanel.add(searchBtn);
        
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(new JLabel("Sort By: "));
        JComboBox<String> sortBox = new JComboBox<>(new String[]{"ID", "Username", "Role", "Name"});
        filterPanel.add(sortBox);

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
        searchField.addActionListener(e -> searchBtn.doClick());
        
        sortBox.addActionListener(e -> {
            int col = sortBox.getSelectedIndex();
            if (col == 3) col = 3; // Name is index 3
            table.getRowSorter().setSortKeys(java.util.List.of(new RowSorter.SortKey(col, SortOrder.ASCENDING)));
        });

        // Render the "Status" column (Active) as a green badge
        table.getColumnModel().getColumn(4).setCellRenderer(new StyleConfig.StatusBadgeRenderer());

        panel.add(StyleConfig.createStyledScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = StyleConfig.createActionBar();
        btnPanel.setBorder(new EmptyBorder(16, 0, 0, 0));
        HallButton deleteUserBtn = HallButton.danger("Delete User");
        
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
        topPanel.setBorder(new EmptyBorder(0, 0, 24, 0));
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

        HallButton filterBtn = HallButton.secondary("Apply");
        
        filterPanel.add(filterBtn);

        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(new JLabel("Sort By: "));
        JComboBox<String> sortBox = new JComboBox<>(new String[]{"Date", "Price", "ID"});
        filterPanel.add(sortBox);

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
        
        sortBox.addActionListener(e -> {
            int col = sortBox.getSelectedIndex() == 0 ? 3 : (sortBox.getSelectedIndex() == 1 ? 6 : 0);
            table.getRowSorter().setSortKeys(java.util.List.of(new RowSorter.SortKey(col, col == 6 ? SortOrder.DESCENDING : SortOrder.ASCENDING)));
        });

        table.getColumnModel().getColumn(5).setCellRenderer(new StyleConfig.StatusBadgeRenderer());
        
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
