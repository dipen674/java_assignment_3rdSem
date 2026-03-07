package com.hallsymphony.ui;

import com.hallsymphony.model.*;
import com.hallsymphony.service.*;
import com.hallsymphony.util.StyleConfig;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManagerDashboard extends BaseDashboard {
    private Manager manager;

    public ManagerDashboard(MainFrame frame, Manager manager) {
        super(frame, manager);
        this.manager = manager;
        initContent();
    }

    @Override
    protected void addSidebarButtons(JPanel sidebar) {
        addSidebarButton(sidebar, "\uD83D\uDCCA  Sales Dashboard", "SALES");
        addSidebarButton(sidebar, "\uD83D\uDD27  Issue Management", "MAINTENANCE");
    }

    private void initContent() {
        contentArea.add(createSalesPanel(), "SALES");
        contentArea.add(createMaintenanceOversightPanel(), "MAINTENANCE");
    }

    // ═══════════════════ SALES DASHBOARD ═══════════════════
    private JPanel createSalesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(StyleConfig.BACKGROUND_COLOR);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(StyleConfig.BACKGROUND_COLOR);
        topPanel.setBorder(new EmptyBorder(0, 0, 16, 0));
        topPanel.add(StyleConfig.createSectionTitle("Sales Overview"), BorderLayout.WEST);

        JButton refreshBtn = new JButton("Refresh Data");
        StyleConfig.styleSecondaryButton(refreshBtn);
        topPanel.add(refreshBtn, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 16, 0));
        cardsPanel.setBackground(StyleConfig.BACKGROUND_COLOR);

        double weeklySales = ReportService.getWeeklySales().values().iterator().next();
        double monthlySales = ReportService.getMonthlySales().values().iterator().next();
        double yearlySales = ReportService.getYearlySales().values().iterator().next();

        JPanel weekCard = createStatCard("Weekly Sales", "RM " + String.format("%.2f", weeklySales), StyleConfig.PRIMARY_COLOR, "This Week");
        JPanel monthCard = createStatCard("Monthly Sales", "RM " + String.format("%.2f", monthlySales), StyleConfig.SUCCESS_COLOR, "This Month");
        JPanel yearCard = createStatCard("Yearly Sales", "RM " + String.format("%.2f", yearlySales), new Color(124, 58, 237), "This Year");

        cardsPanel.add(weekCard);
        cardsPanel.add(monthCard);
        cardsPanel.add(yearCard);

        refreshBtn.addActionListener(e -> {
            // Refresh by rebuilding cards
            double w = ReportService.getWeeklySales().values().iterator().next();
            double m = ReportService.getMonthlySales().values().iterator().next();
            double y = ReportService.getYearlySales().values().iterator().next();
            updateStatCard(weekCard, "RM " + String.format("%.2f", w));
            updateStatCard(monthCard, "RM " + String.format("%.2f", m));
            updateStatCard(yearCard, "RM " + String.format("%.2f", y));
        });

        // Wrap cards in a top-aligned container so they don't stretch vertically
        JPanel cardsWrapper = new JPanel(new BorderLayout());
        cardsWrapper.setBackground(StyleConfig.BACKGROUND_COLOR);
        cardsWrapper.add(cardsPanel, BorderLayout.NORTH);

        // Add booking summary table below
        JPanel tableSection = new JPanel(new BorderLayout());
        tableSection.setBackground(StyleConfig.BACKGROUND_COLOR);
        tableSection.setBorder(new EmptyBorder(16, 0, 0, 0));

        JLabel tableTitle = new JLabel("Recent Paid Bookings");
        tableTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        tableTitle.setForeground(StyleConfig.TEXT_COLOR);
        tableTitle.setBorder(new EmptyBorder(0, 0, 8, 0));
        tableSection.add(tableTitle, BorderLayout.NORTH);

        String[] columns = {"Booking ID", "Customer", "Hall", "Date", "Amount (RM)"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable bookingTable = new JTable(tableModel);
        java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        BookingService.getAllBookings().stream()
            .filter(b -> b.getStatus().equals("PAID"))
            .forEach(b -> tableModel.addRow(new Object[]{b.getId(), b.getCustomerId(), b.getHallId(), b.getStartTime().format(fmt), String.format("%.2f", b.getTotalPrice())}));
        tableSection.add(StyleConfig.createStyledScrollPane(bookingTable), BorderLayout.CENTER);

        cardsWrapper.add(tableSection, BorderLayout.CENTER);
        panel.add(cardsWrapper, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStatCard(String title, String value, Color accentColor, String subtitle) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(StyleConfig.CARD_COLOR);
        card.setBorder(new CompoundBorder(
            new CompoundBorder(
                new LineBorder(StyleConfig.BORDER_COLOR, 1, true),
                new MatteBorder(4, 0, 0, 0, accentColor)  // Top accent stripe
            ),
            new EmptyBorder(20, 24, 20, 24)
        ));
        card.setPreferredSize(new Dimension(0, 140));

        JPanel textArea = new JPanel();
        textArea.setLayout(new BoxLayout(textArea, BoxLayout.Y_AXIS));
        textArea.setBackground(StyleConfig.CARD_COLOR);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        titleLabel.setForeground(StyleConfig.TEXT_SECONDARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        textArea.add(titleLabel);
        textArea.add(Box.createVerticalStrut(8));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        valueLabel.setForeground(StyleConfig.TEXT_COLOR);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        valueLabel.setName("statValue"); // For refresh
        textArea.add(valueLabel);
        textArea.add(Box.createVerticalStrut(4));

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(StyleConfig.SMALL_FONT);
        subtitleLabel.setForeground(StyleConfig.TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        textArea.add(subtitleLabel);

        card.add(textArea, BorderLayout.CENTER);
        return card;
    }

    private void updateStatCard(JPanel card, String newValue) {
        for (Component c : ((JPanel) card.getComponent(0)).getComponents()) {
            if (c instanceof JLabel && "statValue".equals(c.getName())) {
                ((JLabel) c).setText(newValue);
                break;
            }
        }
        card.revalidate();
        card.repaint();
    }

    // ═══════════════════ ISSUE MANAGEMENT ═══════════════════
    private JPanel createMaintenanceOversightPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(StyleConfig.BACKGROUND_COLOR);

        // Header
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(StyleConfig.BACKGROUND_COLOR);
        topPanel.setBorder(new EmptyBorder(0, 0, 12, 0));
        topPanel.add(StyleConfig.createSectionTitle("Customer Issues & Maintenance"), BorderLayout.WEST);

        JPanel filterPanel = StyleConfig.createFilterPanel();
        JTextField searchField = new JTextField(18);
        JButton searchBtn = new JButton("Search");
        StyleConfig.styleSecondaryButton(searchBtn);
        filterPanel.add(new JLabel("Search Issues: "));
        filterPanel.add(searchField);
        filterPanel.add(searchBtn);

        JPanel headerArea = new JPanel(new BorderLayout());
        headerArea.setBackground(StyleConfig.BACKGROUND_COLOR);
        headerArea.add(topPanel, BorderLayout.NORTH);
        headerArea.add(filterPanel, BorderLayout.SOUTH);
        panel.add(headerArea, BorderLayout.NORTH);

        // Table
        String[] columns = {"Issue ID", "Customer", "Booking", "Description", "Assigned To", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        refreshIssueTable(model, "");
        searchBtn.addActionListener(e -> refreshIssueTable(model, searchField.getText()));
        panel.add(StyleConfig.createStyledScrollPane(table), BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = StyleConfig.createButtonPanel();

        JButton assignBtn = new JButton("Assign Scheduler");
        StyleConfig.styleButton(assignBtn);
        assignBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String issueId = (String) model.getValueAt(row, 0);

                // Build scheduler list for selection
                java.util.List<User> schedulers = AuthService.getAllUsers().stream()
                    .filter(u -> u instanceof Scheduler)
                    .collect(java.util.stream.Collectors.toList());

                if (schedulers.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No schedulers available.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String[] names = schedulers.stream().map(u -> u.getId() + " - " + u.getFullName()).toArray(String[]::new);
                String selected = (String) JOptionPane.showInputDialog(this, "Select a scheduler to assign:",
                    "Assign Scheduler", JOptionPane.PLAIN_MESSAGE, null, names, names[0]);

                if (selected != null) {
                    String staffId = selected.split(" - ")[0];
                    IssueService.updateIssueStatus(issueId, "IN_PROGRESS", staffId);
                    refreshIssueTable(model, searchField.getText());
                    JOptionPane.showMessageDialog(this, "Scheduler assigned successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an issue first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton statusBtn = new JButton("Update Status");
        StyleConfig.styleSuccessButton(statusBtn);
        statusBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String issueId = (String) model.getValueAt(row, 0);
                String[] statuses = {"IN_PROGRESS", "DONE", "CLOSED", "CANCELLED"};
                String status = (String) JOptionPane.showInputDialog(this, "Select new status:",
                    "Update Issue Status", JOptionPane.PLAIN_MESSAGE, null, statuses, statuses[0]);
                if (status != null) {
                    IssueService.updateIssueStatus(issueId, status, null);
                    refreshIssueTable(model, searchField.getText());
                    JOptionPane.showMessageDialog(this, "Status updated to: " + status, "Updated", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an issue first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnPanel.add(assignBtn);
        btnPanel.add(statusBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshIssueTable(DefaultTableModel model, String filter) {
        model.setRowCount(0);
        IssueService.getAllIssues().stream()
                .filter(i -> filter.isEmpty() || i.getDescription().toLowerCase().contains(filter.toLowerCase()))
                .forEach(i -> model.addRow(new Object[]{i.getId(), i.getCustomerId(), i.getBookingId(), i.getDescription(),
                    i.getAssignedTo() != null ? i.getAssignedTo() : "Unassigned", i.getStatus()}));
    }
}
