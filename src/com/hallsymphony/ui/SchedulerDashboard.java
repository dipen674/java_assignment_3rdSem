package com.hallsymphony.ui;

import com.hallsymphony.model.*;
import com.hallsymphony.service.*;
import com.hallsymphony.util.StyleConfig;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SchedulerDashboard extends BaseDashboard {
    private Scheduler scheduler;

    public SchedulerDashboard(MainFrame frame, Scheduler scheduler) {
        super(frame, scheduler);
        this.scheduler = scheduler;
        initContent();
    }

    @Override
    protected void addSidebarButtons(JPanel sidebar) {
        addSidebarButton(sidebar, "Manage Halls", "HALLS");
        addSidebarButton(sidebar, "Maintenance", "MAINTENANCE");
    }

    private void initContent() {
        contentArea.add(createHallManagementPanel(), "HALLS");
        contentArea.add(createMaintenancePanel(), "MAINTENANCE");
    }

    private JPanel createHallManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = {"ID", "Name", "Type", "Capacity", "Rate"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        refreshHallTable(model);

        JPanel btnPanel = new JPanel();
        JButton addBtn = new JButton("Add Hall");
        JButton editBtn = new JButton("Edit Hall");
        JButton deleteBtn = new JButton("Delete");

        addBtn.addActionListener(e -> showHallForm(null, model));
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) model.getValueAt(row, 0);
                Hall hall = HallService.getAllHalls().stream().filter(h -> h.getId().equals(id)).findFirst().get();
                showHallForm(hall, model);
            }
        });
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                HallService.deleteHall((String) model.getValueAt(row, 0));
                refreshHallTable(model);
            }
        });

        btnPanel.add(addBtn); btnPanel.add(editBtn); btnPanel.add(deleteBtn);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshHallTable(DefaultTableModel model) {
        model.setRowCount(0);
        for (Hall h : HallService.getAllHalls()) {
            model.addRow(new Object[]{h.getId(), h.getName(), h.getType(), h.getCapacity(), h.getRatePerHour()});
        }
    }

    private void showHallForm(Hall hall, DefaultTableModel model) {
        JTextField nameField = new JTextField(hall != null ? hall.getName() : "");
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Auditorium", "Banquet Hall", "Meeting Room"});
        if (hall != null) typeBox.setSelectedItem(hall.getType());
        JTextField capField = new JTextField(hall != null ? String.valueOf(hall.getCapacity()) : "");
        JTextField rateField = new JTextField(hall != null ? String.valueOf(hall.getRatePerHour()) : "");

        Object[] message = { "Name:", nameField, "Type:", typeBox, "Capacity:", capField, "Rate:", rateField };
        int option = JOptionPane.showConfirmDialog(null, message, "Hall Information", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String type = (String) typeBox.getSelectedItem();
            int cap = Integer.parseInt(capField.getText());
            double rate = Double.parseDouble(rateField.getText());

            if (hall == null) {
                HallService.addHall(name, type, cap, rate, "Default Description");
            } else {
                hall.setName(name); hall.setType(type); hall.setCapacity(cap); hall.setRatePerHour(rate);
                HallService.updateHall(hall);
            }
            refreshHallTable(model);
        }
    }

    private JPanel createMaintenancePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = {"ID", "Description", "Status", "Action"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        refreshMaintenanceTable(model);

        JButton completeBtn = new JButton("Mark as Done");
        completeBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) model.getValueAt(row, 0);
                IssueService.updateIssueStatus(id, "DONE", scheduler.getId());
                refreshMaintenanceTable(model);
            }
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(completeBtn, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshMaintenanceTable(DefaultTableModel model) {
        model.setRowCount(0);
        for (Issue i : IssueService.getAllIssues()) {
            if (scheduler.getId().equals(i.getAssignedTo())) {
                model.addRow(new Object[]{i.getId(), i.getDescription(), i.getStatus(), "Finish"});
            }
        }
    }
}
