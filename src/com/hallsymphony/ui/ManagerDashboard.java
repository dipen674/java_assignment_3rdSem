package com.hallsymphony.ui;

import com.hallsymphony.model.*;
import com.hallsymphony.service.*;
import com.hallsymphony.util.StyleConfig;
import javax.swing.*;
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
        addSidebarButton(sidebar, "Sales Stats", "SALES");
        addSidebarButton(sidebar, "Maintenance", "MAINTENANCE");
    }

    private void initContent() {
        contentArea.add(createSalesPanel(), "SALES");
        contentArea.add(createMaintenanceOversightPanel(), "MAINTENANCE");
    }

    private JPanel createSalesPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(createStatCard("Weekly Sales", "RM " + ReportService.getWeeklySales().values().iterator().next()));
        panel.add(createStatCard("Monthly Sales", "RM " + ReportService.getMonthlySales().values().iterator().next()));
        panel.add(createStatCard("Yearly Sales", "RM " + ReportService.getYearlySales().values().iterator().next()));

        return panel;
    }

    private JPanel createStatCard(String title, String value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(StyleConfig.WHITE);
        card.setBorder(BorderFactory.createLineBorder(StyleConfig.PRIMARY_COLOR, 2));
        
        JLabel tLbl = new JLabel(title, SwingConstants.CENTER);
        tLbl.setFont(StyleConfig.HEADER_FONT);
        JLabel vLbl = new JLabel(value, SwingConstants.CENTER);
        vLbl.setFont(StyleConfig.TITLE_FONT);
        vLbl.setForeground(StyleConfig.SUCCESS_COLOR);

        card.add(tLbl, BorderLayout.NORTH);
        card.add(vLbl, BorderLayout.CENTER);
        return card;
    }

    private JPanel createMaintenanceOversightPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = {"ID", "Description", "Assigned To", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        refreshIssueTable(model);

        JButton assignBtn = new JButton("Assign Scheduler");
        assignBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String issueId = (String) model.getValueAt(row, 0);
                String staffId = JOptionPane.showInputDialog("Enter Scheduler ID to assign:");
                if (staffId != null) {
                    IssueService.updateIssueStatus(issueId, "IN_PROGRESS", staffId);
                    refreshIssueTable(model);
                }
            }
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(assignBtn, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshIssueTable(DefaultTableModel model) {
        model.setRowCount(0);
        IssueService.getAllIssues().forEach(i -> model.addRow(new Object[]{i.getId(), i.getDescription(), i.getAssignedTo(), i.getStatus()}));
    }
}
