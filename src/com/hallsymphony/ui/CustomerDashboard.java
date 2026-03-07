package com.hallsymphony.ui;

import com.hallsymphony.model.*;
import com.hallsymphony.service.*;
import com.hallsymphony.util.StyleConfig;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        addSidebarButton(sidebar, "\uD83C\uDFE2  Available Halls", "HALLS");
        addSidebarButton(sidebar, "\uD83D\uDCC5  My Bookings", "BOOKINGS");
        addSidebarButton(sidebar, "\u26A0  Report Issue", "ISSUES");
        addSidebarButton(sidebar, "\uD83D\uDC64  Profile", "PROFILE");
    }

    private void initContent() {
        contentArea.add(createHallPanel(), "HALLS");
        contentArea.add(createBookingPanel(), "BOOKINGS");
        contentArea.add(createIssuePanel(), "ISSUES");
        contentArea.add(createProfilePanel(), "PROFILE");
    }

    // ═══════════════════ AVAILABLE HALLS ═══════════════════
    private JPanel createHallPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(StyleConfig.BACKGROUND_COLOR);

        // Title
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(StyleConfig.BACKGROUND_COLOR);
        topPanel.setBorder(new EmptyBorder(0, 0, 12, 0));
        topPanel.add(StyleConfig.createSectionTitle("Available Halls"), BorderLayout.WEST);
        panel.add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Name", "Type", "Capacity", "Rate/Hr (RM)"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        refreshHallTable(model);
        panel.add(StyleConfig.createStyledScrollPane(table), BorderLayout.CENTER);

        // Button bar
        JPanel btnPanel = StyleConfig.createButtonPanel();
        JButton bookBtn = new JButton("Book Selected Hall");
        StyleConfig.styleButton(bookBtn);
        bookBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String hallId = (String) model.getValueAt(row, 0);
                showBookingDialog(hallId, model);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a hall first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        btnPanel.add(bookBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshHallTable(DefaultTableModel model) {
        model.setRowCount(0);
        List<Schedule> schedules = HallService.getAllSchedules();
        for (Hall h : HallService.getAllHalls()) {
            boolean isAvailable = schedules.stream()
                .anyMatch(s -> s.getHallId().equals(h.getId()) && s.getType().equals("AVAILABILITY"));
            if (isAvailable) {
                model.addRow(new Object[]{h.getId(), h.getName(), h.getType(), h.getCapacity(), String.format("%.2f", h.getRatePerHour())});
            }
        }
    }

    private void showBookingDialog(String hallId, DefaultTableModel hallModel) {
        JTextField startField = new JTextField("2024-07-20T10:00:00");
        JTextField endField = new JTextField("2024-07-20T14:00:00");
        JTextField remarksField = new JTextField("Event Booking");

        StyleConfig.styleTextField(startField, "Start Time (yyyy-MM-ddTHH:mm:ss)");
        StyleConfig.styleTextField(endField, "End Time (yyyy-MM-ddTHH:mm:ss)");
        StyleConfig.styleTextField(remarksField, "Remarks (Optional)");

        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
        dialogPanel.setBorder(new EmptyBorder(8, 0, 8, 0));
        dialogPanel.add(startField);
        dialogPanel.add(Box.createVerticalStrut(8));
        dialogPanel.add(endField);
        dialogPanel.add(Box.createVerticalStrut(8));
        dialogPanel.add(remarksField);

        int option = JOptionPane.showConfirmDialog(this, dialogPanel, "Book Hall", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            try {
                LocalDateTime start = LocalDateTime.parse(startField.getText().trim());
                LocalDateTime end = LocalDateTime.parse(endField.getText().trim());

                // Validation: start must be before end
                if (!start.isBefore(end)) {
                    JOptionPane.showMessageDialog(this, "Start time must be before end time.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Hall hall = HallService.getAllHalls().stream().filter(h -> h.getId().equals(hallId)).findFirst().orElse(null);
                if (hall == null) return;

                double rate = hall.getRatePerHour();
                double hours = java.time.Duration.between(start, end).toHours();
                double total = hours * rate;
                String remarks = remarksField.getText().trim().isEmpty() ? "Customer Booking" : remarksField.getText().trim();

                // ── Payment Confirmation Dialog ──
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                String confirmMsg = String.format(
                    "═══ PAYMENT SUMMARY ═══\n\n" +
                    "Hall:       %s (%s)\n" +
                    "Start:      %s\n" +
                    "End:        %s\n" +
                    "Duration:   %.0f hour(s)\n" +
                    "Rate:       RM %.2f/hr\n" +
                    "─────────────────────\n" +
                    "TOTAL:      RM %.2f\n\n" +
                    "Proceed with payment?",
                    hall.getName(), hall.getType(),
                    start.format(fmt), end.format(fmt),
                    hours, rate, total
                );

                int confirm = JOptionPane.showConfirmDialog(this, confirmMsg,
                    "Confirm Payment", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    if (BookingService.createBooking(customer.getId(), hallId, start, end, total, remarks)) {
                        showReceipt(hallId, start, end, total);
                    } else {
                        JOptionPane.showMessageDialog(this,
                            "Booking failed. Possible reasons:\n• Hours outside 8 AM – 6 PM\n• Overlaps with an existing booking\n• Hall under maintenance\n• Outside availability window",
                            "Booking Failed", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Use: yyyy-MM-ddTHH:mm:ss", "Format Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ═══════════════════ MY BOOKINGS ═══════════════════
    private JPanel createBookingPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(StyleConfig.BACKGROUND_COLOR);

        // Title + Filter
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(StyleConfig.BACKGROUND_COLOR);
        topPanel.setBorder(new EmptyBorder(0, 0, 12, 0));
        topPanel.add(StyleConfig.createSectionTitle("My Bookings"), BorderLayout.WEST);

        JPanel filterPanel = StyleConfig.createFilterPanel();
        JComboBox<String> statusFilter = new JComboBox<>(new String[]{"All", "Upcoming", "Past", "PAID", "CANCELLED"});
        statusFilter.setPreferredSize(new Dimension(130, 32));
        JButton refreshBtn = new JButton("Apply Filter");
        StyleConfig.styleSecondaryButton(refreshBtn);

        filterPanel.add(new JLabel("Filter: "));
        filterPanel.add(statusFilter);
        filterPanel.add(refreshBtn);

        JPanel headerArea = new JPanel(new BorderLayout());
        headerArea.setBackground(StyleConfig.BACKGROUND_COLOR);
        headerArea.add(topPanel, BorderLayout.NORTH);
        headerArea.add(filterPanel, BorderLayout.SOUTH);
        panel.add(headerArea, BorderLayout.NORTH);

        // Table
        String[] columns = {"Booking ID", "Hall", "Start", "End", "Status", "Price (RM)"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        refreshBookingTable(model, "All");
        refreshBtn.addActionListener(e -> refreshBookingTable(model, (String) statusFilter.getSelectedItem()));

        panel.add(StyleConfig.createStyledScrollPane(table), BorderLayout.CENTER);

        // Cancel button
        JPanel btnPanel = StyleConfig.createButtonPanel();
        JButton cancelBtn = new JButton("Cancel Selected Booking");
        StyleConfig.styleDangerButton(cancelBtn);
        cancelBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) model.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to cancel this booking?", "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (BookingService.cancelBooking(id)) {
                        JOptionPane.showMessageDialog(this, "Booking cancelled successfully.", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
                        refreshBookingTable(model, (String) statusFilter.getSelectedItem());
                    } else {
                        JOptionPane.showMessageDialog(this, "Cannot cancel — must be at least 3 days before the booking date.", "Cannot Cancel", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        btnPanel.add(cancelBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshBookingTable(DefaultTableModel model, String filter) {
        model.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime now = LocalDateTime.now();
        for (Booking b : BookingService.getBookingsByCustomer(customer.getId())) {
            boolean match = filter.equals("All") || b.getStatus().equals(filter);
            if (filter.equals("Upcoming")) match = b.getStartTime().isAfter(now) && !b.getStatus().equals("CANCELLED");
            if (filter.equals("Past")) match = b.getStartTime().isBefore(now);
            if (match) {
                model.addRow(new Object[]{b.getId(), b.getHallId(), b.getStartTime().format(fmt), b.getEndTime().format(fmt), b.getStatus(), String.format("%.2f", b.getTotalPrice())});
            }
        }
    }

    // ═══════════════════ REPORT ISSUE ═══════════════════
    private JPanel createIssuePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(StyleConfig.BACKGROUND_COLOR);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(StyleConfig.BACKGROUND_COLOR);
        topPanel.setBorder(new EmptyBorder(0, 0, 12, 0));
        topPanel.add(StyleConfig.createSectionTitle("My Issues"), BorderLayout.WEST);
        panel.add(topPanel, BorderLayout.NORTH);

        String[] columns = {"Issue ID", "Booking ID", "Description", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        refreshIssueTable(model);

        panel.add(StyleConfig.createStyledScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = StyleConfig.createButtonPanel();
        JButton reportBtn = new JButton("Report New Issue");
        StyleConfig.styleButton(reportBtn);
        reportBtn.addActionListener(e -> {
            JTextField bIdField = new JTextField();
            JTextField descField = new JTextField();
            StyleConfig.styleTextField(bIdField, "Booking ID");
            StyleConfig.styleTextField(descField, "Issue Description");

            JPanel formPanel = new JPanel();
            formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
            formPanel.add(bIdField);
            formPanel.add(Box.createVerticalStrut(8));
            formPanel.add(descField);

            if (JOptionPane.showConfirmDialog(this, formPanel, "Report Issue", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
                String bId = bIdField.getText().trim();
                String desc = descField.getText().trim();
                if (!bId.isEmpty() && !desc.isEmpty()) {
                    IssueService.raiseIssue(customer.getId(), bId, desc);
                    refreshIssueTable(model);
                    JOptionPane.showMessageDialog(this, "Issue reported successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        btnPanel.add(reportBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshIssueTable(DefaultTableModel model) {
        model.setRowCount(0);
        for (Issue i : IssueService.getIssuesByCustomer(customer.getId())) {
            model.addRow(new Object[]{i.getId(), i.getBookingId(), i.getDescription(), i.getStatus()});
        }
    }

    // ═══════════════════ PROFILE ═══════════════════
    private JPanel createProfilePanel() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(StyleConfig.BACKGROUND_COLOR);

        JPanel card = StyleConfig.createCard(32);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(450, 350));
        card.setMaximumSize(new Dimension(450, 350));

        JLabel title = StyleConfig.createSectionTitle("Profile Settings");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(16));

        JTextField nameField = new JTextField(customer.getFullName());
        StyleConfig.styleTextField(nameField, "Full Name");
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        card.add(nameField);
        card.add(Box.createVerticalStrut(12));

        JTextField contactField = new JTextField(customer.getContact());
        StyleConfig.styleTextField(contactField, "Contact Number");
        contactField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        card.add(contactField);
        card.add(Box.createVerticalStrut(12));

        JLabel usernameLabel = new JLabel("Username: " + customer.getUsername());
        usernameLabel.setFont(StyleConfig.SMALL_FONT);
        usernameLabel.setForeground(StyleConfig.TEXT_SECONDARY);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(usernameLabel);
        card.add(Box.createVerticalStrut(20));

        JButton saveBtn = new JButton("Save Changes");
        StyleConfig.styleSuccessButton(saveBtn);
        saveBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        saveBtn.addActionListener(e -> {
            String newName = nameField.getText().trim();
            String newContact = contactField.getText().trim();
            if (newName.isEmpty() || newContact.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fields cannot be empty.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (AuthService.updateProfile(customer.getId(), newName, newContact)) {
                customer.setFullName(newName);
                customer.setContact(newContact);
                JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update profile.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        card.add(saveBtn);

        wrapper.add(card);
        return wrapper;
    }

    // ═══════════════════ RECEIPT ═══════════════════
    private void showReceipt(String hallId, LocalDateTime start, LocalDateTime end, double total) {
        JFrame receiptFrame = new JFrame("Booking Receipt");
        receiptFrame.setSize(450, 520);
        receiptFrame.setLocationRelativeTo(this);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(StyleConfig.WHITE);

        // Header
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(StyleConfig.PRIMARY_COLOR);
        header.setBorder(new EmptyBorder(20, 24, 20, 24));

        JLabel headerIcon = new JLabel("\u2705  Booking Confirmed!");
        headerIcon.setFont(new Font("SansSerif", Font.BOLD, 18));
        headerIcon.setForeground(StyleConfig.WHITE);
        headerIcon.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(headerIcon);

        JLabel headerSub = new JLabel("Your hall has been reserved successfully.");
        headerSub.setFont(StyleConfig.SMALL_FONT);
        headerSub.setForeground(new Color(219, 234, 254));
        headerSub.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(headerSub);

        main.add(header, BorderLayout.NORTH);

        // Body
        Hall hall = HallService.getAllHalls().stream().filter(h -> h.getId().equals(hallId)).findFirst().get();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(StyleConfig.WHITE);
        body.setBorder(new EmptyBorder(24, 24, 24, 24));

        addReceiptRow(body, "Customer", customer.getFullName());
        addReceiptRow(body, "Hall", hall.getName() + " (" + hall.getType() + ")");
        addReceiptRow(body, "Capacity", hall.getCapacity() + " seats");
        addReceiptRow(body, "Start", start.format(fmt));
        addReceiptRow(body, "End", end.format(fmt));
        addReceiptRow(body, "Duration", java.time.Duration.between(start, end).toHours() + " hours");
        addReceiptRow(body, "Rate", "RM " + String.format("%.2f", hall.getRatePerHour()) + "/hr");

        // Divider
        JSeparator sep = new JSeparator();
        sep.setForeground(StyleConfig.BORDER_COLOR);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(Box.createVerticalStrut(12));
        body.add(sep);
        body.add(Box.createVerticalStrut(12));

        // Total
        JPanel totalRow = new JPanel(new BorderLayout());
        totalRow.setBackground(StyleConfig.WHITE);
        totalRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        totalRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel totalLabel = new JLabel("TOTAL PAID");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        totalLabel.setForeground(StyleConfig.TEXT_COLOR);
        totalRow.add(totalLabel, BorderLayout.WEST);

        JLabel totalValue = new JLabel("RM " + String.format("%.2f", total));
        totalValue.setFont(new Font("SansSerif", Font.BOLD, 20));
        totalValue.setForeground(StyleConfig.SUCCESS_COLOR);
        totalRow.add(totalValue, BorderLayout.EAST);

        body.add(totalRow);
        body.add(Box.createVerticalStrut(24));

        JLabel thanks = new JLabel("Thank you for choosing Hall Symphony!");
        thanks.setFont(StyleConfig.SMALL_FONT);
        thanks.setForeground(StyleConfig.TEXT_SECONDARY);
        thanks.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(thanks);

        main.add(body, BorderLayout.CENTER);
        receiptFrame.add(main);
        receiptFrame.setVisible(true);
    }

    private void addReceiptRow(JPanel parent, String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(StyleConfig.WHITE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setBorder(new EmptyBorder(2, 0, 2, 0));

        JLabel l = new JLabel(label);
        l.setFont(StyleConfig.NORMAL_FONT);
        l.setForeground(StyleConfig.TEXT_SECONDARY);
        row.add(l, BorderLayout.WEST);

        JLabel v = new JLabel(value);
        v.setFont(new Font("SansSerif", Font.BOLD, 14));
        v.setForeground(StyleConfig.TEXT_COLOR);
        row.add(v, BorderLayout.EAST);

        parent.add(row);
    }
}
