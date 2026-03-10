package com.hallsymphony.util;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import com.hallsymphony.ui.components.HallButton;

/**
 * StyleConfig — Central design system for Hall Symphony.
 *
 * Key design decisions:
 *  • Uses Metal L&F as base — 100% Java-painted, pixel-perfect on ALL platforms
 *    (Windows / Linux / WSL / macOS) with zero rendering differences.
 *  • Custom HallButton bypasses L&F button painting entirely.
 *  • "Dialog" font = JVM logical font, resolves to system's best sans-serif
 *    on every platform (DejaVu Sans on Linux, Segoe UI on Windows, etc.)
 *  • Anti-aliasing forced globally for crisp text at all DPI levels.
 */
public class StyleConfig {

    // ── Primary Palette ──────────────────────────────────────────────────────
    public static final Color PRIMARY_COLOR    = new Color(37,  99, 235);   // Blue-600
    public static final Color PRIMARY_DARK     = new Color(29,  78, 216);   // Blue-700
    public static final Color PRIMARY_LIGHT    = new Color(219,234, 254);   // Blue-100
    public static final Color PRIMARY_XLIGHT   = new Color(239,246, 255);   // Blue-50

    // ── Sidebar ──────────────────────────────────────────────────────────────
    public static final Color SIDEBAR_BG      = new Color(11,  17,  38);   // Deep navy
    public static final Color SIDEBAR_HOVER   = new Color(30,  41,  59);   // Slate-800
    public static final Color SIDEBAR_ACTIVE  = new Color(37,  99, 235);   // Blue-600
    public static final Color SIDEBAR_TEXT    = new Color(203,213, 225);   // Slate-300

    // ── Semantic ─────────────────────────────────────────────────────────────
    public static final Color ACCENT_COLOR    = new Color(220, 38,  38);   // Red-600
    public static final Color ACCENT_DARK     = new Color(185, 28,  28);   // Red-700
    public static final Color SUCCESS_COLOR   = new Color(22,  163, 74);   // Green-600
    public static final Color SUCCESS_DARK    = new Color(21,  128, 61);   // Green-700
    public static final Color WARNING_COLOR   = new Color(217, 119,  6);   // Amber-600

    // ── Surfaces ─────────────────────────────────────────────────────────────
    public static final Color BACKGROUND_COLOR = new Color(240,244, 251);  // Cool grey-blue
    public static final Color CARD_COLOR       = Color.WHITE;
    public static final Color WHITE            = Color.WHITE;

    // ── Typography ───────────────────────────────────────────────────────────
    public static final Color TEXT_COLOR       = new Color(15,  23,  42);  // Slate-900
    public static final Color TEXT_SECONDARY   = new Color(71,  85, 105);  // Slate-600
    public static final Color TEXT_MUTED       = new Color(148,163, 184);  // Slate-400

    // ── Borders ──────────────────────────────────────────────────────────────
    public static final Color BORDER_COLOR     = new Color(226,232, 240);  // Slate-200
    public static final Color BORDER_STRONG    = new Color(203,213, 225);  // Slate-300

    // ── Table ─────────────────────────────────────────────────────────────────
    public static final Color TABLE_HEADER_BG  = new Color(248,250, 252);  // Slate-50
    public static final Color TABLE_ALT_ROW    = new Color(248,250, 252);

    // ── Fonts (Dialog = cross-platform JVM logical font) ─────────────────────
    public static final Font TITLE_FONT  = new Font("Dialog", Font.BOLD,  24);
    public static final Font HEADER_FONT = new Font("Dialog", Font.BOLD,  17);
    public static final Font NORMAL_FONT = new Font("Dialog", Font.PLAIN, 14);
    public static final Font BOLD_FONT   = new Font("Dialog", Font.BOLD,  14);
    public static final Font SMALL_FONT  = new Font("Dialog", Font.PLAIN, 12);
    public static final Font SMALL_BOLD  = new Font("Dialog", Font.BOLD,  12);
    public static final Font TABLE_FONT  = new Font("Dialog", Font.PLAIN, 13);
    public static final Font MONO_FONT   = new Font("Monospaced", Font.PLAIN, 13);

    // ── Spacing ──────────────────────────────────────────────────────────────
    public static final int GAP_XS = 4;
    public static final int GAP_SM = 8;
    public static final int GAP_MD = 16;
    public static final int GAP_LG = 24;
    public static final int GAP_XL = 40;

    // ─────────────────────────────────────────────────────────────────────────
    // Global Theme — call ONCE before any Swing code
    // ─────────────────────────────────────────────────────────────────────────
    public static void applyGlobalTheme() {

        // 1. Enable anti-aliasing & sub-pixel rendering globally
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext",                "true");

        // 2. Force Metal L&F — 100% Java-painted, pixel-perfect on all platforms
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception ignored) { /* fall back gracefully */ }

        // 3. Metal theme overrides — palette
        UIManager.put("control",               BACKGROUND_COLOR);
        UIManager.put("info",                  new Color(255, 251, 205));
        UIManager.put("nimbusBase",            PRIMARY_COLOR);

        // 4. Fonts — every Swing component in one place
        Font[] fonts = { NORMAL_FONT };
        String[] components = {
            "Button", "Label", "TextField", "PasswordField", "TextArea",
            "ComboBox", "OptionPane", "ToolTip", "List", "MenuItem",
            "CheckBox", "RadioButton", "Spinner", "TabbedPane", "ToggleButton"
        };
        for (String c : components) UIManager.put(c + ".font", NORMAL_FONT);
        UIManager.put("Table.font",       TABLE_FONT);
        UIManager.put("TableHeader.font", SMALL_BOLD);
        UIManager.put("OptionPane.buttonFont", BOLD_FONT);
        UIManager.put("OptionPane.messageFont", NORMAL_FONT);

        // 5. Table
        UIManager.put("Table.rowHeight",              36);
        UIManager.put("Table.gridColor",              BORDER_COLOR);
        UIManager.put("Table.selectionBackground",    PRIMARY_LIGHT);
        UIManager.put("Table.selectionForeground",    TEXT_COLOR);
        UIManager.put("Table.background",             WHITE);
        UIManager.put("Table.alternateRowColor",      TABLE_ALT_ROW);
        UIManager.put("TableHeader.background",       TABLE_HEADER_BG);
        UIManager.put("TableHeader.foreground",       TEXT_SECONDARY);

        // 6. ScrollPane — remove default raised Metal border
        UIManager.put("ScrollPane.border", BorderFactory.createLineBorder(BORDER_COLOR, 1));

        // 7. Text fields
        UIManager.put("TextField.background",         WHITE);
        UIManager.put("TextField.foreground",         TEXT_COLOR);
        UIManager.put("TextField.caretForeground",    PRIMARY_COLOR);
        UIManager.put("TextField.selectionBackground",PRIMARY_LIGHT);
        UIManager.put("TextField.selectionForeground",TEXT_COLOR);
        UIManager.put("PasswordField.background",     WHITE);

        // 8. ComboBox
        UIManager.put("ComboBox.background",          WHITE);
        UIManager.put("ComboBox.foreground",          TEXT_COLOR);
        UIManager.put("ComboBox.selectionBackground", PRIMARY_LIGHT);
        UIManager.put("ComboBox.selectionForeground", TEXT_COLOR);
        UIManager.put("ComboBox.border",
            new CompoundBorder(new LineBorder(BORDER_COLOR,1,true), new EmptyBorder(2,6,2,6)));

        // 9. OptionPane colours
        UIManager.put("OptionPane.background", WHITE);
        UIManager.put("Panel.background",      BACKGROUND_COLOR);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Button factories — use HallButton for cross-platform consistency
    // ─────────────────────────────────────────────────────────────────────────

    public static void styleButton(JButton btn) {
        applyHallButtonStyle(btn, HallButton.Variant.PRIMARY);
    }
    public static void styleDangerButton(JButton btn) {
        applyHallButtonStyle(btn, HallButton.Variant.DANGER);
    }
    public static void styleSuccessButton(JButton btn) {
        applyHallButtonStyle(btn, HallButton.Variant.SUCCESS);
    }
    public static void styleSecondaryButton(JButton btn) {
        applyHallButtonStyle(btn, HallButton.Variant.SECONDARY);
    }
    public static void styleGhostButton(JButton btn) {
        applyHallButtonStyle(btn, HallButton.Variant.GHOST);
    }

    /** Apply HallButton-style custom painting to a plain JButton (retrofit helper). */
    private static void applyHallButtonStyle(JButton btn, HallButton.Variant v) {
        // For new code, prefer HallButton.primary() etc. directly.
        // This helper makes existing JButtons look right without refactoring every class.
        Color bg, hover, fg, border;
        switch (v) {
            case DANGER:
                bg=ACCENT_COLOR; hover=ACCENT_DARK; fg=WHITE; border=ACCENT_DARK; break;
            case SUCCESS:
                bg=SUCCESS_COLOR; hover=SUCCESS_DARK; fg=WHITE; border=SUCCESS_DARK; break;
            case SECONDARY:
                bg=WHITE; hover=BACKGROUND_COLOR; fg=TEXT_COLOR; border=BORDER_COLOR; break;
            case GHOST:
                bg=new Color(0,0,0,0); hover=PRIMARY_XLIGHT; fg=PRIMARY_COLOR; border=new Color(0,0,0,0); break;
            default: // PRIMARY
                bg=PRIMARY_COLOR; hover=PRIMARY_DARK; fg=WHITE; border=PRIMARY_DARK; break;
        }
        final Color _bg=bg, _hover=hover;
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(BOLD_FONT);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new CompoundBorder(
            new LineBorder(border, 1, true),
            new EmptyBorder(8, 20, 8, 20)));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(_hover); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(_bg); }
        });
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Input field styling
    // ─────────────────────────────────────────────────────────────────────────

    public static void styleTextField(JTextField field, String label) {
        field.setBorder(new CompoundBorder(
            BorderFactory.createTitledBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                label,
                TitledBorder.LEFT, TitledBorder.ABOVE_TOP,
                SMALL_BOLD, TEXT_SECONDARY),
            new EmptyBorder(2, 6, 4, 6)));
        field.setFont(NORMAL_FONT);
        field.setBackground(WHITE);
        field.setForeground(TEXT_COLOR);
        field.setCaretColor(PRIMARY_COLOR);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Table
    // ─────────────────────────────────────────────────────────────────────────

    public static void styleTable(JTable table) {
        table.setFont(TABLE_FONT);
        table.setRowHeight(42);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(PRIMARY_LIGHT);
        table.setSelectionForeground(TEXT_COLOR);
        table.setFillsViewportHeight(true);
        table.setBackground(WHITE);
        table.setBorder(BorderFactory.createEmptyBorder());

        // Row renderer with alternating bg and bottom divider
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean sel, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, value, sel, focus, row, col);
                setBorder(new CompoundBorder(
                    new MatteBorder(0, 0, 1, 0, BORDER_COLOR),
                    new EmptyBorder(8, 16, 8, 16)));
                if (!sel) {
                    c.setBackground(row % 2 == 0 ? WHITE : TABLE_ALT_ROW);
                    c.setForeground(TEXT_COLOR);
                }
                return c;
            }
        });

        // Header
        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setFont(SMALL_BOLD);
        header.setBackground(TABLE_HEADER_BG);
        header.setForeground(TEXT_SECONDARY);
        header.setPreferredSize(new Dimension(0, 40));

        DefaultTableCellRenderer hr = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean s, boolean f, int r, int col) {
                super.getTableCellRendererComponent(t, v, s, f, r, col);
                setBackground(TABLE_HEADER_BG);
                setForeground(TEXT_SECONDARY);
                setFont(SMALL_BOLD);
                setBorder(new CompoundBorder(
                    new MatteBorder(0, 0, 2, 0, BORDER_STRONG),
                    new EmptyBorder(6, 12, 6, 12)));
                setHorizontalAlignment(SwingConstants.LEFT);
                return this;
            }
        };
        for (int i = 0; i < table.getColumnCount(); i++)
            table.getColumnModel().getColumn(i).setHeaderRenderer(hr);
    }

    public static JScrollPane createStyledScrollPane(JTable table) {
        styleTable(table);
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        sp.getViewport().setBackground(WHITE);
        return sp;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Composite component builders
    // ─────────────────────────────────────────────────────────────────────────

    public static JPanel createCard(int padding) {
        JPanel p = new JPanel();
        p.setBackground(CARD_COLOR);
        p.setBorder(new CompoundBorder(
            new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(0, 0, 2, 0)),
            new EmptyBorder(padding, padding, padding, padding)));
        return p;
    }

    public static JPanel createFilterPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, GAP_SM, GAP_XS));
        p.setBackground(WHITE);
        p.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER_COLOR),
            new EmptyBorder(6, GAP_SM, 6, GAP_SM)));
        return p;
    }

    public static JPanel createButtonPanel() {
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, GAP_MD, 16));
        btnPanel.setBackground(BACKGROUND_COLOR);
        return btnPanel;
    }

    public static JPanel createActionBar() {
        JPanel actionBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, GAP_MD, 0));
        actionBar.setBackground(BACKGROUND_COLOR);
        return actionBar;
    }

    public static JPanel createFormPanel(int rows, int cols) {
        JPanel form = new JPanel(new GridLayout(rows, cols, GAP_LG, GAP_LG));
        form.setBackground(BACKGROUND_COLOR);
        return form;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Table Cell Renderers
    // ─────────────────────────────────────────────────────────────────────────

    public static class StatusBadgeRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int col) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            label.setFont(SMALL_BOLD);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setOpaque(true);

            String status = value != null ? value.toString().toUpperCase() : "";
            Color statusBg, statusFg;

            switch (status) {
                case "PAID":
                case "DONE":
                case "SUCCESS":
                    statusBg = SUCCESS_COLOR;
                    statusFg = WHITE;
                    break;
                case "PENDING":
                case "IN_PROGRESS":
                    statusBg = WARNING_COLOR;
                    statusFg = WHITE;
                    break;
                case "CANCELLED":
                case "CLOSED":
                case "REJECTED":
                    statusBg = ACCENT_COLOR;
                    statusFg = WHITE;
                    break;
                default:
                    statusBg = BORDER_STRONG;
                    statusFg = TEXT_COLOR;
                    break;
            }

            label.setBackground(statusBg);
            label.setForeground(statusFg);
            
            // Re-apply the standard padding and bottom border
            label.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, BORDER_COLOR),
                new EmptyBorder(6, 12, 6, 12)
            ));

            if (isSelected) {
                // Dim the colors slightly on selection to retain contrast
                label.setBackground(statusBg.darker());
            }

            return label;
        }
    }

    public static JPanel createContentWrapper(JPanel content) {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(BACKGROUND_COLOR);
        wrap.setBorder(new EmptyBorder(GAP_MD, GAP_MD, GAP_MD, GAP_MD));
        wrap.add(content, BorderLayout.CENTER);
        return wrap;
    }

    public static JLabel createSectionTitle(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(HEADER_FONT);
        lbl.setForeground(TEXT_COLOR);
        lbl.setBorder(new EmptyBorder(0, 0, GAP_SM, 0));
        return lbl;
    }

    public static JLabel createBadge(String text, Color bg, Color fg, Color border) {
        JLabel badge = new JLabel(" " + text + " ");
        badge.setFont(SMALL_BOLD);
        badge.setForeground(fg);
        badge.setBackground(bg);
        badge.setOpaque(true);
        badge.setBorder(new CompoundBorder(
            new LineBorder(border, 1, true),
            new EmptyBorder(3, 8, 3, 8)));
        return badge;
    }

    /** A styled ComboBox with consistent rendering. */
    public static <T> JComboBox<T> createStyledComboBox(T[] items) {
        JComboBox<T> combo = new JComboBox<>(items);
        combo.setBackground(WHITE);
        combo.setForeground(TEXT_COLOR);
        combo.setFont(NORMAL_FONT);
        combo.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(2, 6, 2, 4)));
        return combo;
    }
}
