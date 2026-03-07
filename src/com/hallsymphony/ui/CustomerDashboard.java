package com.hallsymphony.ui;

import com.hallsymphony.model.*;
import com.hallsymphony.service.*;
import com.hallsymphony.util.StyleConfig;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class CustomerDashboard extends BaseDashboard {
    private Customer customer;

    public CustomerDashboard(MainFrame frame, Customer customer) {
        super(frame, customer);
        this.customer = customer;
        
        initContent();
    }

    @Override
    protected void addSidebarButtons(JPanel sidebar) {
        addSidebarButton(sidebar, "Available Halls", "HALLS");
        addSidebarButton(sidebar, "My Bookings", "BOOKINGS");
        addSidebarButton(sidebar, "Report Issue", "ISSUES");
        addSidebarButton(sidebar, "Profile", "PROFILE");
    }

    private void initContent() {
        contentArea.add(createHallPanel(), "HALLS");
        contentArea.add(createBookingPanel(), "BOOKINGS");
        contentArea.add(createIssuePanel(), "ISSUES");
        contentArea.add(createProfilePanel(), "PROFILE");
    }

    private JPanel createHallPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = {"ID", "Name", "Type", "Capacity", "Rate/Hr", "Action"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        
        refreshHallTable(model);

        JButton bookBtn = new JButton("Book Selected");
        StyleConfig.styleButton(bookBtn);
        bookBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String hallId = (String) model.getValueAt(row, 0);
                showBookingDialog(hallId);
            }
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(bookBtn, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshHallTable(DefaultTableModel model) {
        model.setRowCount(0);
        List<Schedule> schedules = HallService.getAllSchedules();
        List<Hall> allHalls = HallService.getAllHalls();
        
        for (Hall h : allHalls) {
            boolean isAvailable = schedules.stream()
                .anyMatch(s -> s.getHallId().equals(h.getId()) && s.getType().equals("AVAILABILITY"));
            if (isAvailable) {
                model.addRow(new Object[]{h.getId(), h.getName(), h.getType(), h.getCapacity(), h.getRatePerHour(), "Book Now"});
            }
        }
    }

    private void showBookingDialog(String hallId) {
        // Simple booking dialog (can be expanded)
        JTextField startField = new JTextField("2024-07-20T10:00:00");
        JTextField endField = new JTextField("2024-07-20T14:00:00");
        
        Object[] message = {
            "Start Time (ISO):", startField,
            "End Time (ISO):", endField,
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Book Hall", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                LocalDateTime start = LocalDateTime.parse(startField.getText());
                LocalDateTime end = LocalDateTime.parse(endField.getText());
                // Calculate price logic here or in service
                double rate = HallService.getAllHalls().stream().filter(h -> h.getId().equals(hallId)).findFirst().get().getRatePerHour();
                double hours = java.time.Duration.between(start, end).toHours();
                double total = hours * rate;

                if (BookingService.createBooking(customer.getId(), hallId, start, end, total, "Customer Booking")) {
                    JOptionPane.showMessageDialog(this, "Booking Successful! Total: RM " + total);
                    showReceipt(hallId, start, end, total);
                } else {
                    JOptionPane.showMessageDialog(this, "Booking Failed (Check hours or overlap with maintenance/existing bookings)", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Date Format", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JPanel createBookingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = {"ID", "Hall", "Start", "End", "Status", "Price"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JComboBox<String> statusFilter = new JComboBox<>(new String[]{"All", "Upcoming", "Past", "CANCELLED", "PAID"});
        JButton refreshBtn = new JButton("Filter");
        filterPanel.add(new JLabel("Status:"));
        filterPanel.add(statusFilter);
        filterPanel.add(refreshBtn);
        refreshBtn.addActionListener(e -> refreshBookingTable(model, (String) statusFilter.getSelectedItem()));

        refreshBookingTable(model, "All");

        JButton cancelBtn = new JButton("Cancel Selected");
        cancelBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) model.getValueAt(row, 0);
                if (BookingService.cancelBooking(id)) {
                    JOptionPane.showMessageDialog(this, "Cancelled!");
                    refreshBookingTable(model, (String) statusFilter.getSelectedItem());
                } else {
                    JOptionPane.showMessageDialog(this, "Cannot cancel (Must be 3 days before)", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(cancelBtn, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshBookingTable(DefaultTableModel model, String filter) {
        model.setRowCount(0);
        LocalDateTime now = LocalDateTime.now();
        for (Booking b : BookingService.getBookingsByCustomer(customer.getId())) {
            boolean match = filter.equals("All") || b.getStatus().equals(filter);
            if (filter.equals("Upcoming")) match = b.getStartTime().isAfter(now) && !b.getStatus().equals("CANCELLED");
            if (filter.equals("Past")) match = b.getStartTime().isBefore(now);
            
            if (match) {
                model.addRow(new Object[]{b.getId(), b.getHallId(), b.getStartTime(), b.getEndTime(), b.getStatus(), b.getTotalPrice()});
            }
        }
    }

    private JPanel createIssuePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = {"ID", "Booking ID", "Description", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        JButton reportBtn = new JButton("Report New Issue");
        reportBtn.addActionListener(e -> {
            String bId = JOptionPane.showInputDialog("Enter Booking ID:");
            String desc = JOptionPane.showInputDialog("Enter Issue Description:");
            if (bId != null && desc != null) {
                IssueService.raiseIssue(customer.getId(), bId, desc);
                refreshIssueTable(model);
            }
        });

        refreshIssueTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(reportBtn, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshIssueTable(DefaultTableModel model) {
        model.setRowCount(0);
        for (Issue i : IssueService.getIssuesByCustomer(customer.getId())) {
            model.addRow(new Object[]{i.getId(), i.getBookingId(), i.getDescription(), i.getStatus()});
        }
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panel.add(new JLabel("Full Name:"));
        JTextField nameField = new JTextField(customer.getFullName());
        panel.add(nameField);

        panel.add(new JLabel("Contact:"));
        JTextField contactField = new JTextField(customer.getContact());
        panel.add(contactField);

        panel.add(new JLabel("Username:"));
        panel.add(new JLabel(customer.getUsername()));

        JButton saveBtn = new JButton("Update Profile");
        saveBtn.addActionListener(e -> {
            String newName = nameField.getText();
            String newContact = contactField.getText();
            if (AuthService.updateProfile(customer.getId(), newName, newContact)) {
                customer.setFullName(newName);
                customer.setContact(newContact);
                JOptionPane.showMessageDialog(this, "Profile Updated Successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update profile", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(saveBtn);

        return panel;
    }

    private void showReceipt(String hallId, LocalDateTime start, LocalDateTime end, double total) {
        JFrame receiptFrame = new JFrame("Booking Receipt");
        receiptFrame.setSize(400, 500);
        receiptFrame.setLayout(new BorderLayout());
        
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        
        Hall hall = HallService.getAllHalls().stream().filter(h -> h.getId().equals(hallId)).findFirst().get();
        
        StringBuilder sb = new StringBuilder();
        sb.append("===============================\n");
        sb.append("       HALL SYMPHONY INC       \n");
        sb.append("===============================\n\n");
        sb.append("Customer: ").append(customer.getFullName()).append("\n");
        sb.append("Hall: ").append(hall.getName()).append(" (").append(hall.getType()).append(")\n");
        sb.append("Start: ").append(start.toString()).append("\n");
        sb.append("End:   ").append(end.toString()).append("\n");
        sb.append("-------------------------------\n");
        sb.append("TOTAL PAID:  RM ").append(String.format("%.2f", total)).append("\n");
        sb.append("-------------------------------\n\n");
        sb.append("      Thank you for booking!   \n");
        
        area.setText(sb.toString());
        receiptFrame.add(new JScrollPane(area), BorderLayout.CENTER);
        receiptFrame.setLocationRelativeTo(this);
        receiptFrame.setVisible(true);
    }
}
