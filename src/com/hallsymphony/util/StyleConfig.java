package com.hallsymphony.util;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class StyleConfig {
    // ── Primary Palette ──
    public static final Color PRIMARY_COLOR   = new Color(37, 99, 235);   // Vibrant Blue
    public static final Color PRIMARY_DARK    = new Color(29, 78, 216);   // Darker Blue (hover)
    public static final Color PRIMARY_LIGHT   = new Color(219, 234, 254); // Light Blue tint

    // ── Secondary / Sidebar ──
    public static final Color SECONDARY_COLOR = new Color(15, 23, 42);    // Slate-900
    public static final Color SIDEBAR_HOVER   = new Color(30, 41, 59);    // Slate-800

    // ── Semantic Colours ──
    public static final Color ACCENT_COLOR    = new Color(220, 38, 38);   // Red-600
    public static final Color ACCENT_HOVER    = new Color(185, 28, 28);   // Red-700
    public static final Color SUCCESS_COLOR   = new Color(22, 163, 74);   // Green-600
    public static final Color WARNING_COLOR   = new Color(234, 179, 8);   // Yellow-500

    // ── Backgrounds & Surfaces ──
    public static final Color BACKGROUND_COLOR = new Color(241, 245, 249); // Slate-100
    public static final Color CARD_COLOR       = Color.WHITE;
    public static final Color WHITE            = Color.WHITE;
    public static final Color TEXT_COLOR        = new Color(15, 23, 42);  // Slate-900
    public static final Color TEXT_SECONDARY    = new Color(100, 116, 139); // Slate-500
    public static final Color BORDER_COLOR      = new Color(226, 232, 240); // Slate-200
    public static final Color TABLE_HEADER_BG   = new Color(248, 250, 252); // Slate-50
    public static final Color TABLE_ALT_ROW     = new Color(248, 250, 252); // Slate-50
    public static final Color TABLE_HOVER_ROW   = new Color(241, 245, 249); // Slate-100

    // ── Fonts ──
    public static final Font TITLE_FONT   = new Font("SansSerif", Font.BOLD, 28);
    public static final Font HEADER_FONT  = new Font("SansSerif", Font.BOLD, 18);
    public static final Font NORMAL_FONT  = new Font("SansSerif", Font.PLAIN, 15);
    public static final Font SMALL_FONT   = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font TABLE_FONT   = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font MONO_FONT    = new Font("Monospaced", Font.PLAIN, 14);

    // ── Global Theme (call once at startup) ──
    public static void applyGlobalTheme() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Buttons
        UIManager.put("Button.font", NORMAL_FONT);
        UIManager.put("Button.arc", 8);

        // Text Fields
        UIManager.put("TextField.font", NORMAL_FONT);
        UIManager.put("PasswordField.font", NORMAL_FONT);

        // Labels
        UIManager.put("Label.font", NORMAL_FONT);

        // ComboBox
        UIManager.put("ComboBox.font", NORMAL_FONT);

        // Tables
        UIManager.put("Table.font", TABLE_FONT);
        UIManager.put("TableHeader.font", new Font("SansSerif", Font.BOLD, 14));
        UIManager.put("Table.rowHeight", 36);
        UIManager.put("Table.gridColor", BORDER_COLOR);
        UIManager.put("Table.selectionBackground", PRIMARY_LIGHT);
        UIManager.put("Table.selectionForeground", TEXT_COLOR);

        // ScrollPane
        UIManager.put("ScrollPane.border", BorderFactory.createEmptyBorder());

        // OptionPane
        UIManager.put("OptionPane.messageFont", NORMAL_FONT);
        UIManager.put("OptionPane.buttonFont", NORMAL_FONT);

        // ToolTip
        UIManager.put("ToolTip.font", SMALL_FONT);
    }

    // ── Button Helpers ──
    public static void styleButton(JButton button) {
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 15));
        button.setBorder(new CompoundBorder(
            new LineBorder(PRIMARY_DARK, 1, true),
            new EmptyBorder(10, 24, 10, 24)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(PRIMARY_DARK);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
            }
        });
    }

    public static void styleDangerButton(JButton button) {
        button.setBackground(ACCENT_COLOR);
        button.setForeground(WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 15));
        button.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_HOVER, 1, true),
            new EmptyBorder(10, 24, 10, 24)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(ACCENT_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(ACCENT_COLOR);
            }
        });
    }

    public static void styleSuccessButton(JButton button) {
        button.setBackground(SUCCESS_COLOR);
        button.setForeground(WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 15));
        button.setBorder(new CompoundBorder(
            new LineBorder(new Color(21, 128, 61), 1, true),
            new EmptyBorder(10, 24, 10, 24)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(21, 128, 61));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(SUCCESS_COLOR);
            }
        });
    }

    public static void styleSecondaryButton(JButton button) {
        button.setBackground(WHITE);
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 15));
        button.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(10, 24, 10, 24)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(BACKGROUND_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(WHITE);
            }
        });
    }

    // ── Table Styling ──
    public static void styleTable(JTable table) {
        table.setFont(TABLE_FONT);
        table.setRowHeight(38);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(PRIMARY_LIGHT);
        table.setSelectionForeground(TEXT_COLOR);
        table.setFillsViewportHeight(true);
        table.setBorder(BorderFactory.createEmptyBorder());

        // Cell padding via custom renderer
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                setBorder(new CompoundBorder(
                    new MatteBorder(0, 0, 1, 0, BORDER_COLOR),
                    new EmptyBorder(6, 14, 6, 14)
                ));
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? WHITE : TABLE_ALT_ROW);
                }
                c.setForeground(TEXT_COLOR);
                return c;
            }
        };
        table.setDefaultRenderer(Object.class, cellRenderer);

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setBackground(TABLE_HEADER_BG);
        header.setForeground(TEXT_SECONDARY);
        header.setBorder(new MatteBorder(0, 0, 2, 0, BORDER_COLOR));
        header.setPreferredSize(new Dimension(header.getWidth(), 44));

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                setBackground(TABLE_HEADER_BG);
                setForeground(TEXT_SECONDARY);
                setFont(new Font("SansSerif", Font.BOLD, 14));
                setBorder(new CompoundBorder(
                    new MatteBorder(0, 0, 2, 0, BORDER_COLOR),
                    new EmptyBorder(6, 14, 6, 14)
                ));
                setHorizontalAlignment(SwingConstants.LEFT);
                return c;
            }
        };
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
    }

    // ── Styled Scroll Pane for Tables ──
    public static JScrollPane createStyledScrollPane(JTable table) {
        styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        scrollPane.getViewport().setBackground(WHITE);
        return scrollPane;
    }

    // ── Styled Text Field ──
    public static void styleTextField(JTextField field, String label) {
        field.setBorder(new CompoundBorder(
            BorderFactory.createTitledBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                label,
                TitledBorder.LEFT,
                TitledBorder.ABOVE_TOP,
                SMALL_FONT,
                TEXT_SECONDARY
            ),
            new EmptyBorder(4, 8, 4, 8)
        ));
        field.setFont(NORMAL_FONT);
        field.setCaretColor(PRIMARY_COLOR);
    }

    // ── Card Panel (white bg, shadow border, padding) ──
    public static JPanel createCard(int padding) {
        JPanel card = new JPanel();
        card.setBackground(CARD_COLOR);
        card.setBorder(new CompoundBorder(
            new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(1, 1, 3, 1) // Simulated shadow bottom
            ),
            new EmptyBorder(padding, padding, padding, padding)
        ));
        return card;
    }

    // ── Filter Panel (consistent styling for top filter bar) ──
    public static JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        panel.setBackground(WHITE);
        panel.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER_COLOR),
            new EmptyBorder(4, 8, 4, 8)
        ));
        return panel;
    }

    // ── Button Panel (consistent styling for bottom action bar) ──
    public static JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        panel.setBackground(WHITE);
        panel.setBorder(new CompoundBorder(
            new MatteBorder(1, 0, 0, 0, BORDER_COLOR),
            new EmptyBorder(4, 16, 4, 16)
        ));
        return panel;
    }

    // ── Content Wrapper (adds margins around content area panels) ──
    public static JPanel createContentWrapper(JPanel content) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BACKGROUND_COLOR);
        wrapper.setBorder(new EmptyBorder(16, 16, 16, 16));
        wrapper.add(content, BorderLayout.CENTER);
        return wrapper;
    }

    // ── Section Title Label ──
    public static JLabel createSectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        label.setForeground(TEXT_COLOR);
        label.setBorder(new EmptyBorder(0, 0, 12, 0));
        return label;
    }
}
