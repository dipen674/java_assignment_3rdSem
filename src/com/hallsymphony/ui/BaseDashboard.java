package com.hallsymphony.ui;

import com.hallsymphony.model.User;
import com.hallsymphony.util.StyleConfig;
import com.hallsymphony.ui.components.HallButton;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * BaseDashboard — shared layout shell for all role dashboards.
 *
 * Layout:
 *   WEST   → rich dark sidebar (logo + nav + user card + logout)
 *   NORTH  → white top bar (welcome label + role badge)
 *   CENTER → CardLayout content pane
 *
 * The sidebar uses custom painting for the active nav button indicator
 * (a coloured left-edge bar) so it looks the same on every platform.
 */
public abstract class BaseDashboard extends JPanel {
    protected final MainFrame frame;
    protected final User user;
    protected JPanel contentArea;

    /** Currently active nav button — tracked for visual state. */
    private JButton activeNavBtn = null;

    public BaseDashboard(MainFrame frame, User user) {
        this.frame = frame;
        this.user  = user;
        setLayout(new BorderLayout());
        setBackground(StyleConfig.BACKGROUND_COLOR);

        add(buildSidebar(),   BorderLayout.WEST);
        add(buildTopBar(),    BorderLayout.NORTH);

        contentArea = new JPanel(new CardLayout());
        contentArea.setBackground(StyleConfig.BACKGROUND_COLOR);
        contentArea.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(contentArea, BorderLayout.CENTER);
    }

    protected abstract void addSidebarButtons(JPanel sidebar);

    // ── Sidebar ───────────────────────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Subtle top-to-bottom gradient on sidebar
                GradientPaint gp = new GradientPaint(
                    0, 0,         new Color(11, 17, 38),
                    0, getHeight(), new Color(15, 23, 42));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setOpaque(false);

        // Logo area
        sidebar.add(buildLogoArea());
        sidebar.add(makeSep());
        sidebar.add(Box.createVerticalStrut(8));

        // Nav label
        JLabel navCap = new JLabel("  NAVIGATION");
        navCap.setFont(new Font("Dialog", Font.BOLD, 10));
        navCap.setForeground(new Color(71, 85, 105));  // Slate-600
        navCap.setAlignmentX(Component.LEFT_ALIGNMENT);
        navCap.setBorder(new EmptyBorder(4, 18, 8, 0));
        sidebar.add(navCap);

        // Role-specific nav buttons (subclass fills these in)
        addSidebarButtons(sidebar);

        // Spacer
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(makeSep());
        sidebar.add(buildUserCard());

        // Logout button using HallButton
        HallButton logoutBtn = HallButton.danger("  ← Sign Out");
        logoutBtn.setFont(new Font("Dialog", Font.BOLD, 13));
        logoutBtn.setMaximumSize(new Dimension(220, 38));
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutBtn.addActionListener(e -> frame.logout());
        sidebar.add(logoutBtn);
        sidebar.add(Box.createVerticalStrut(16));

        return sidebar;
    }

    private JPanel buildLogoArea() {
        JPanel area = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                // Transparent — parent sidebar paints background
            }
        };
        area.setLayout(new BoxLayout(area, BoxLayout.Y_AXIS));
        area.setOpaque(false);
        area.setBorder(new EmptyBorder(24, 20, 20, 20));
        area.setMaximumSize(new Dimension(260, 120));

        JLabel icon = new JLabel("H S");
        icon.setFont(new Font("Dialog", Font.BOLD, 32));
        icon.setForeground(StyleConfig.PRIMARY_LIGHT);
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        area.add(icon);
        area.add(Box.createVerticalStrut(8));

        JLabel name = new JLabel("Hall Symphony");
        name.setForeground(Color.WHITE);
        name.setFont(new Font("Dialog", Font.BOLD, 16));
        name.setAlignmentX(Component.CENTER_ALIGNMENT);
        area.add(name);

        JLabel tagline = new JLabel("Booking Management System");
        tagline.setForeground(new Color(71, 85, 105));
        tagline.setFont(new Font("Dialog", Font.PLAIN, 10));
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);
        area.add(tagline);

        return area;
    }

    private JPanel buildUserCard() {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {}
        };
        card.setLayout(new BorderLayout(10, 0));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(12, 16, 12, 16));
        card.setMaximumSize(new Dimension(260, 60));

        // Avatar circle
        JLabel avatar = new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(StyleConfig.PRIMARY_COLOR);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Dialog", Font.BOLD, 13));
                FontMetrics fm = g2.getFontMetrics();
                String letter = user.getFullName().substring(0, 1).toUpperCase();
                g2.drawString(letter, (getWidth() - fm.stringWidth(letter)) / 2,
                    (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
                g2.dispose();
            }
        };
        avatar.setPreferredSize(new Dimension(36, 36));
        avatar.setMinimumSize(new Dimension(36, 36));
        card.add(avatar, BorderLayout.WEST);

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);

        JLabel nameLabel = new JLabel(user.getFullName());
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Dialog", Font.BOLD, 12));
        info.add(nameLabel);

        JLabel roleLabel = new JLabel(user.getRole());
        roleLabel.setForeground(new Color(100, 116, 139));
        roleLabel.setFont(new Font("Dialog", Font.PLAIN, 11));
        info.add(roleLabel);

        card.add(info, BorderLayout.CENTER);
        return card;
    }

    private JSeparator makeSep() {
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(30, 41, 59));
        sep.setBackground(new Color(30, 41, 59));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return sep;
    }

    // ── Top bar ───────────────────────────────────────────────────────────────
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Color.WHITE);
        bar.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, StyleConfig.BORDER_COLOR),
            new EmptyBorder(14, 24, 14, 24)));

        // Left: breadcrumb style welcome
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        JLabel subGreet = new JLabel("HALL SYMPHONY");
        subGreet.setFont(new Font("Dialog", Font.BOLD, 9));
        subGreet.setForeground(StyleConfig.TEXT_MUTED);
        left.add(subGreet);

        JLabel welcome = new JLabel("Welcome back, " + user.getFullName());
        welcome.setFont(new Font("Dialog", Font.BOLD, 17));
        welcome.setForeground(StyleConfig.TEXT_COLOR);
        left.add(welcome);

        bar.add(left, BorderLayout.WEST);

        // Right: role badge
        JLabel badge = StyleConfig.createBadge(
            user.getRole().toUpperCase(),
            StyleConfig.PRIMARY_LIGHT,
            StyleConfig.PRIMARY_COLOR,
            StyleConfig.PRIMARY_COLOR);
        bar.add(badge, BorderLayout.EAST);

        return bar;
    }

    // ── Sidebar nav button ────────────────────────────────────────────────────
    protected void addSidebarButton(JPanel sidebar, String text, String cardName) {
        // Custom nav button that draws an active indicator bar on the left edge
        JButton btn = new JButton(text) {
            boolean active = false;
            boolean hovered = false;

            {
                setOpaque(false);
                setContentAreaFilled(false);
                setBorderPainted(false);
                setFocusPainted(false);
                setFont(new Font("Dialog", Font.PLAIN, 13));
                setForeground(StyleConfig.SIDEBAR_TEXT);
                setHorizontalAlignment(SwingConstants.LEFT);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setBorder(new EmptyBorder(11, 20, 11, 20));
                setMaximumSize(new Dimension(260, 46));
                setAlignmentX(Component.LEFT_ALIGNMENT);

                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                    public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
                });

                addActionListener(e -> {
                    // Deactivate previous
                    if (activeNavBtn != null) {
                        activeNavBtn.setForeground(StyleConfig.SIDEBAR_TEXT);
                        activeNavBtn.repaint();
                    }
                    active = true;
                    activeNavBtn = this;
                    setForeground(Color.WHITE);
                    setFont(new Font("Dialog", Font.BOLD, 13));
                    repaint();

                    CardLayout cl = (CardLayout) contentArea.getLayout();
                    cl.show(contentArea, cardName);
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                boolean isActive = (activeNavBtn == this);
                int w = getWidth(), h = getHeight();

                if (isActive) {
                    // Active: semi-transparent blue fill + bright left-edge accent bar
                    g2.setColor(new Color(37, 99, 235, 40));
                    g2.fillRoundRect(6, 2, w - 12, h - 4, 8, 8);
                    g2.setColor(new Color(37, 99, 235));
                    g2.fillRoundRect(0, 6, 4, h - 12, 4, 4);
                } else if (hovered) {
                    // Hover: lighter fill
                    g2.setColor(new Color(255, 255, 255, 12));
                    g2.fillRoundRect(6, 2, w - 12, h - 4, 8, 8);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };

        sidebar.add(btn);
        sidebar.add(Box.createVerticalStrut(2));
    }
}
