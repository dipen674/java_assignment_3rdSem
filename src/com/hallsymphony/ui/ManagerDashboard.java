package com.hallsymphony.ui;

import com.hallsymphony.model.*;
import com.hallsymphony.service.*;
import com.hallsymphony.util.StyleConfig;
import com.hallsymphony.ui.components.HallButton;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
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
        addSidebarButton(sidebar, "Sales Dashboard", "SALES");
        addSidebarButton(sidebar, "Issue Management", "MAINTENANCE");
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

        HallButton refreshBtn = HallButton.secondary("Refresh Data");
        
        topPanel.add(refreshBtn, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 16, 0)); // 4 cards in one row
        cardsPanel.setBackground(StyleConfig.BACKGROUND_COLOR);

        double weeklySales = getSafeSales(ReportService.getWeeklySales());
        double monthlySales = getSafeSales(ReportService.getMonthlySales());
        double totalRev = ReportService.getTotalRevenue();
        int totalBookings = ReportService.getTotalBookingCount();

        JPanel weekCard = createStatCard("Weekly", "RM " + String.format("%.2f", weeklySales), StyleConfig.PRIMARY_COLOR, "Past 7 Days");
        JPanel monthCard = createStatCard("Monthly", "RM " + String.format("%.2f", monthlySales), StyleConfig.SUCCESS_COLOR, "Past 30 Days");
        JPanel totalRevCard = createStatCard("Lifetime Revenue", "RM " + String.format("%.2f", totalRev), new Color(124, 58, 237), "All Time");
        JPanel totalBookCard = createStatCard("Bookings", String.valueOf(totalBookings), new Color(245, 158, 11), "Active & Completed");

        cardsPanel.add(weekCard);
        cardsPanel.add(monthCard);
        cardsPanel.add(totalRevCard);
        cardsPanel.add(totalBookCard);

        refreshBtn.addActionListener(e -> {
            updateStatCard(weekCard, "RM " + String.format("%.2f", getSafeSales(ReportService.getWeeklySales())));
            updateStatCard(monthCard, "RM " + String.format("%.2f", getSafeSales(ReportService.getMonthlySales())));
            updateStatCard(totalRevCard, "RM " + String.format("%.2f", ReportService.getTotalRevenue()));
            updateStatCard(totalBookCard, String.valueOf(ReportService.getTotalBookingCount()));
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

        String[] columns = {"Booking ID", "Customer", "Hall", "Date", "Amount (RM)", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable bookingTable = new JTable(tableModel);
        
        refreshBookingTable(tableModel);
        
        bookingTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        bookingTable.getColumnModel().getColumn(5).setCellRenderer(new StyleConfig.StatusBadgeRenderer());
        tableSection.add(StyleConfig.createStyledScrollPane(bookingTable), BorderLayout.CENTER);

        refreshBtn.addActionListener(e -> {
            updateStatCard(weekCard, "RM " + String.format("%.2f", getSafeSales(ReportService.getWeeklySales())));
            updateStatCard(monthCard, "RM " + String.format("%.2f", getSafeSales(ReportService.getMonthlySales())));
            updateStatCard(totalRevCard, "RM " + String.format("%.2f", ReportService.getTotalRevenue()));
            updateStatCard(totalBookCard, String.valueOf(ReportService.getTotalBookingCount()));
            refreshBookingTable(tableModel);
        });

        cardsWrapper.add(tableSection, BorderLayout.CENTER);
        panel.add(cardsWrapper, BorderLayout.CENTER);

        return panel;
    }

    private void refreshBookingTable(DefaultTableModel model) {
        model.setRowCount(0);
        java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        BookingService.getAllBookings().stream()
            .sorted((b1, b2) -> b2.getStartTime().compareTo(b1.getStartTime())) // Newest first
            .forEach(b -> model.addRow(new Object[]{
                b.getId(), 
                b.getCustomerId(), 
                b.getHallId(), 
                b.getStartTime().format(fmt), 
                String.format("%.2f", b.getTotalPrice()),
                b.getStatus()
            }));
    }

    private JPanel createStatCard(String title, String value, Color accentColor, String subtitle) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(StyleConfig.CARD_COLOR);
        card.setBorder(new CompoundBorder(
            new CompoundBorder(
                BorderFactory.createMatteBorder(0, 1, 3, 1, StyleConfig.BORDER_COLOR), // Faux drop-shadow
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
        topPanel.setBorder(new EmptyBorder(0, 0, 24, 0));
        topPanel.add(StyleConfig.createSectionTitle("Customer Issues & Maintenance"), BorderLayout.WEST);

        JPanel filterPanel = StyleConfig.createFilterPanel();
        JTextField searchField = new JTextField(18);
        HallButton searchBtn = HallButton.secondary("Search");
        
        filterPanel.add(new JLabel("Search: "));
        filterPanel.add(searchField);
        filterPanel.add(searchBtn);
        
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(new JLabel("Sort By: "));
        JComboBox<String> sortBox = new JComboBox<>(new String[]{"Issue ID", "Status", "Description"});
        filterPanel.add(sortBox);

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
        searchField.addActionListener(e -> searchBtn.doClick());

        sortBox.addActionListener(e -> {
            int col = sortBox.getSelectedIndex() == 0 ? 0 : (sortBox.getSelectedIndex() == 1 ? 5 : 3);
            table.getRowSorter().setSortKeys(java.util.List.of(new RowSorter.SortKey(col, SortOrder.ASCENDING)));
        });

        table.getColumnModel().getColumn(5).setCellRenderer(new StyleConfig.StatusBadgeRenderer());
        panel.add(StyleConfig.createStyledScrollPane(table), BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = StyleConfig.createActionBar();
        btnPanel.setBorder(new EmptyBorder(16, 0, 0, 0));

        HallButton assignBtn = HallButton.primary("Assign Scheduler");
        
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

        HallButton statusBtn = HallButton.success("Update Status");
        
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

    private double getSafeSales(java.util.Map<String, Double> sales) {
        if (sales == null || sales.isEmpty()) return 0.0;
        return sales.values().iterator().next();
    }
}
