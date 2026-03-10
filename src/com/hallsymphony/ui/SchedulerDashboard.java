package com.hallsymphony.ui;

import com.hallsymphony.model.*;
import com.hallsymphony.service.*;
import com.hallsymphony.util.StyleConfig;
import com.hallsymphony.ui.components.HallButton;
import javax.swing.*;
import com.hallsymphony.ui.components.HallButton;
import javax.swing.border.*;
import com.hallsymphony.ui.components.HallButton;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SchedulerDashboard extends BaseDashboard {
    private Scheduler scheduler;

    public SchedulerDashboard(MainFrame frame, Scheduler scheduler) {
        super(frame, scheduler);
        this.scheduler = scheduler;
        initContent();
    }

    protected void addSidebarButtons(JPanel sidebar) {
        addSidebarButton(sidebar, "  Manage Halls", "HALLS");
        addSidebarButton(sidebar, "  Scheduling", "SCHEDULING");
        addSidebarButton(sidebar, "  Maintenance", "MAINTENANCE");
    }

    private void initContent() {
        contentArea.add(createHallManagementPanel(), "HALLS");
        contentArea.add(createSchedulingPanel(), "SCHEDULING");
        contentArea.add(createMaintenancePanel(), "MAINTENANCE");
    }

    // ═══════════════════ HALL MANAGEMENT ═══════════════════
    private JPanel createHallManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(StyleConfig.BACKGROUND_COLOR);

        // Title
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(StyleConfig.BACKGROUND_COLOR);
        topPanel.setBorder(new EmptyBorder(0, 0, 24, 0));
        topPanel.add(StyleConfig.createSectionTitle("Hall Management"), BorderLayout.WEST);

        // Filter
        JPanel filterPanel = StyleConfig.createFilterPanel();
        JTextField searchField = new JTextField(18);
        searchField.setPreferredSize(new Dimension(200, 32));
        HallButton searchBtn = HallButton.secondary("Search");
        
        filterPanel.add(new JLabel("Search: "));
        filterPanel.add(searchField);
        filterPanel.add(searchBtn);

        JPanel headerArea = new JPanel(new BorderLayout());
        headerArea.setBackground(StyleConfig.BACKGROUND_COLOR);
        headerArea.add(topPanel, BorderLayout.NORTH);
        headerArea.add(filterPanel, BorderLayout.SOUTH);
        panel.add(headerArea, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Name", "Type", "Capacity", "Rate/Hr (RM)"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        refreshHallTable(model, "");
        searchBtn.addActionListener(e -> refreshHallTable(model, searchField.getText()));
        panel.add(StyleConfig.createStyledScrollPane(table), BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = StyleConfig.createActionBar();
        btnPanel.setBorder(new EmptyBorder(16, 0, 0, 0));
        HallButton addBtn = HallButton.success("Add Hall");
        
        HallButton editBtn = HallButton.primary("Edit Hall");
        
        HallButton deleteBtn = HallButton.danger("Delete");
        

        addBtn.addActionListener(e -> showHallForm(null, model, searchField));
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) model.getValueAt(row, 0);
                Hall hall = HallService.getAllHalls().stream().filter(h -> h.getId().equals(id)).findFirst().get();
                showHallForm(hall, model, searchField);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a hall first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this hall?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    HallService.deleteHall((String) model.getValueAt(row, 0));
                    refreshHallTable(model, searchField.getText());
                }
            }
        });

        btnPanel.add(addBtn);
        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshHallTable(DefaultTableModel model, String filter) {
        model.setRowCount(0);
        for (Hall h : HallService.getAllHalls()) {
            if (filter.isEmpty() || h.getName().toLowerCase().contains(filter.toLowerCase()) || h.getType().toLowerCase().contains(filter.toLowerCase())) {
                model.addRow(new Object[]{h.getId(), h.getName(), h.getType(), h.getCapacity(), String.format("%.2f", h.getRatePerHour())});
            }
        }
    }

    private void showHallForm(Hall hall, DefaultTableModel model, JTextField searchField) {
        JTextField nameField = new JTextField(hall != null ? hall.getName() : "");
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Auditorium", "Banquet Hall", "Meeting Room"});
        if (hall != null) typeBox.setSelectedItem(hall.getType());
        JTextField capField = new JTextField(hall != null ? String.valueOf(hall.getCapacity()) : "");
        JTextField rateField = new JTextField(hall != null ? String.valueOf(hall.getRatePerHour()) : "");
        JTextField descField = new JTextField(hall != null ? hall.getDescription() : "");

        StyleConfig.styleTextField(nameField, "Hall Name");
        StyleConfig.styleTextField(capField, "Capacity");
        StyleConfig.styleTextField(rateField, "Rate per Hour (RM)");
        StyleConfig.styleTextField(descField, "Description");

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.add(nameField);
        formPanel.add(Box.createVerticalStrut(6));
        formPanel.add(new JLabel("Type:"));
        formPanel.add(typeBox);
        formPanel.add(Box.createVerticalStrut(6));
        formPanel.add(capField);
        formPanel.add(Box.createVerticalStrut(6));
        formPanel.add(rateField);
        formPanel.add(Box.createVerticalStrut(6));
        formPanel.add(descField);

        String title = hall == null ? "Add New Hall" : "Edit Hall";
        int option = JOptionPane.showConfirmDialog(this, formPanel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().trim();
                String type = (String) typeBox.getSelectedItem();
                int cap = Integer.parseInt(capField.getText().trim());
                double rate = Double.parseDouble(rateField.getText().trim());
                String desc = descField.getText().trim();

                if (hall == null) {
                    HallService.addHall(name, type, cap, rate, desc.isEmpty() ? "No description" : desc);
                } else {
                    hall.setName(name); hall.setType(type); hall.setCapacity(cap); hall.setRatePerHour(rate); hall.setDescription(desc);
                    HallService.updateHall(hall);
                }
                refreshHallTable(model, searchField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Capacity must be a number and rate must be a decimal.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ═══════════════════ SCHEDULING ═══════════════════
    private JPanel createSchedulingPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(StyleConfig.BACKGROUND_COLOR);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(StyleConfig.BACKGROUND_COLOR);
        topPanel.setBorder(new EmptyBorder(0, 0, 24, 0));
        topPanel.add(StyleConfig.createSectionTitle("Hall Schedules"), BorderLayout.WEST);
        panel.add(topPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Hall ID", "Start", "End", "Type", "Remarks"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        refreshScheduleTable(model);
        table.getColumnModel().getColumn(4).setCellRenderer(new StyleConfig.StatusBadgeRenderer());
        panel.add(StyleConfig.createStyledScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = StyleConfig.createActionBar();
        btnPanel.setBorder(new EmptyBorder(16, 0, 0, 0));
        HallButton addBtn = HallButton.success("Add Schedule");
        
        HallButton deleteBtn = HallButton.danger("Delete Schedule");
        

        addBtn.addActionListener(e -> showScheduleForm(model));
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this, "Delete this schedule?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    HallService.deleteSchedule((String) model.getValueAt(row, 0));
                    refreshScheduleTable(model);
                }
            }
        });

        btnPanel.add(addBtn);
        btnPanel.add(deleteBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshScheduleTable(DefaultTableModel model) {
        model.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (Schedule s : HallService.getAllSchedules()) {
            model.addRow(new Object[]{s.getId(), s.getHallId(), s.getStartTime().format(fmt), s.getEndTime().format(fmt), s.getType(), s.getRemarks()});
        }
    }

    private void showScheduleForm(DefaultTableModel model) {
        JComboBox<String> hallBox = new JComboBox<>();
        HallService.getAllHalls().forEach(h -> hallBox.addItem(h.getId() + " - " + h.getName()));

        JTextField startField = new JTextField("2024-07-20T08:00:00");
        JTextField endField = new JTextField("2024-07-20T18:00:00");
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"AVAILABILITY", "MAINTENANCE"});
        JTextField remarkField = new JTextField();

        StyleConfig.styleTextField(startField, "Start Time (yyyy-MM-ddTHH:mm:ss)");
        StyleConfig.styleTextField(endField, "End Time (yyyy-MM-ddTHH:mm:ss)");
        StyleConfig.styleTextField(remarkField, "Remarks (Optional)");

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.add(new JLabel("Hall:"));
        formPanel.add(hallBox);
        formPanel.add(Box.createVerticalStrut(6));
        formPanel.add(startField);
        formPanel.add(Box.createVerticalStrut(6));
        formPanel.add(endField);
        formPanel.add(Box.createVerticalStrut(6));
        formPanel.add(new JLabel("Type:"));
        formPanel.add(typeBox);
        formPanel.add(Box.createVerticalStrut(6));
        formPanel.add(remarkField);

        int option = JOptionPane.showConfirmDialog(this, formPanel, "Add Schedule", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String selectedHall = (String) hallBox.getSelectedItem();
                String hallId = selectedHall.split(" - ")[0];

                java.time.LocalDateTime start = java.time.LocalDateTime.parse(startField.getText().trim());
                java.time.LocalDateTime end = java.time.LocalDateTime.parse(endField.getText().trim());

                // Validation: start must be before end
                if (!start.isBefore(end)) {
                    JOptionPane.showMessageDialog(this, "Start time must be before end time.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String id = "SCH" + (HallService.getAllSchedules().size() + 1);
                Schedule schedule = new Schedule(id, hallId, start, end, (String) typeBox.getSelectedItem(), remarkField.getText().trim());
                if (HallService.addSchedule(schedule)) {
                    refreshScheduleTable(model);
                } else {
                    JOptionPane.showMessageDialog(this, "Schedule conflict detected!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ═══════════════════ MAINTENANCE (Assigned Issues) ═══════════════════
    private JPanel createMaintenancePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(StyleConfig.BACKGROUND_COLOR);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(StyleConfig.BACKGROUND_COLOR);
        topPanel.setBorder(new EmptyBorder(0, 0, 24, 0));
        topPanel.add(StyleConfig.createSectionTitle("My Assigned Maintenance Tasks"), BorderLayout.WEST);
        panel.add(topPanel, BorderLayout.NORTH);

        String[] columns = {"Issue ID", "Description", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        refreshMaintenanceTable(model);
        table.getColumnModel().getColumn(2).setCellRenderer(new StyleConfig.StatusBadgeRenderer());
        panel.add(StyleConfig.createStyledScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = StyleConfig.createActionBar();
        btnPanel.setBorder(new EmptyBorder(16, 0, 0, 0));
        HallButton completeBtn = HallButton.success("Mark as Done");
        
        completeBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String id = (String) model.getValueAt(row, 0);
                IssueService.updateIssueStatus(id, "DONE", scheduler.getId());
                refreshMaintenanceTable(model);
                JOptionPane.showMessageDialog(this, "Task marked as done!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        btnPanel.add(completeBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshMaintenanceTable(DefaultTableModel model) {
        model.setRowCount(0);
        for (Issue i : IssueService.getAllIssues()) {
            if (scheduler.getId().equals(i.getAssignedTo())) {
                model.addRow(new Object[]{i.getId(), i.getDescription(), i.getStatus()});
            }
        }
    }
}
