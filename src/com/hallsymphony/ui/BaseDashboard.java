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
                g2.setColor(StyleConfig.SIDEBAR_BG);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(280, 0));
        sidebar.setOpaque(false);

        // 1. Logo area
        JPanel logoArea = buildLogoArea();
        logoArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(logoArea);
        sidebar.add(Box.createVerticalStrut(10));

        // 2. Nav label
        JLabel navCap = new JLabel("MAIN MENU");
        navCap.setFont(new Font("Segoe UI", Font.BOLD, 12));
        navCap.setForeground(new Color(148, 163, 184)); // Slate-400
        navCap.setAlignmentX(Component.LEFT_ALIGNMENT);
        navCap.setBorder(new EmptyBorder(25, 24, 5, 0)); // Less left margin (24), more top margin (25)
        sidebar.add(navCap);
        sidebar.add(Box.createVerticalStrut(10)); // Extra gap after header

        // 3. Role-specific nav buttons
        addSidebarButtons(sidebar);

        sidebar.add(Box.createVerticalGlue());
        
        // 4. User Profile Section
        JPanel profileCard = new JPanel(new BorderLayout(15, 0));
        profileCard.setOpaque(false);
        profileCard.setMaximumSize(new Dimension(280, 80));
        profileCard.setBorder(new EmptyBorder(10, 20, 10, 20));
        profileCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        String initial = user.getFullName().substring(0, 1).toUpperCase();
        JLabel avatar = new JLabel(initial, SwingConstants.CENTER);
        avatar.setPreferredSize(new Dimension(44, 44));
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        avatar.setForeground(Color.WHITE);
        avatar.setOpaque(true);
        avatar.setBackground(StyleConfig.PRIMARY_COLOR);
        
        JPanel userInfo = new JPanel(new GridLayout(2, 1, 0, 2));
        userInfo.setOpaque(false);
        JLabel nameLbl = new JLabel(user.getFullName());
        nameLbl.setFont(StyleConfig.BOLD_FONT);
        nameLbl.setForeground(Color.WHITE);
        JLabel roleLbl = new JLabel(user.getRole().toUpperCase());
        roleLbl.setFont(StyleConfig.SMALL_FONT);
        roleLbl.setForeground(StyleConfig.SIDEBAR_TEXT);
        userInfo.add(nameLbl);
        userInfo.add(roleLbl);
        
        profileCard.add(avatar, BorderLayout.WEST);
        profileCard.add(userInfo, BorderLayout.CENTER);
        sidebar.add(profileCard);
        sidebar.add(Box.createVerticalStrut(5));

        // 5. Sign Out Button - Using HallButton for perfect visibility
        JPanel logoutWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 0));
        logoutWrapper.setOpaque(false);
        logoutWrapper.setMaximumSize(new Dimension(280, 60));
        logoutWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        HallButton logoutBtn = HallButton.danger("Sign Out");
        logoutBtn.setPreferredSize(new Dimension(220, 42));
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to sign out?", "Confirm logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                frame.logout();
            }
        });
        
        logoutWrapper.add(logoutBtn);
        sidebar.add(logoutWrapper);
        sidebar.add(Box.createVerticalStrut(20));

        return sidebar;
    }

    private JPanel buildLogoArea() {
        JPanel area = new JPanel();
        area.setLayout(new BoxLayout(area, BoxLayout.X_AXIS));
        area.setOpaque(false);
        area.setBorder(new EmptyBorder(30, 24, 20, 24));
        area.setMaximumSize(new Dimension(280, 100));

        JLabel logoIcon = new JLabel("H"); // Simple letter instead of symbol
        logoIcon.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        logoIcon.setForeground(StyleConfig.PRIMARY_COLOR);
        area.add(logoIcon);
        area.add(Box.createHorizontalStrut(15));

        JPanel textPart = new JPanel();
        textPart.setLayout(new BoxLayout(textPart, BoxLayout.Y_AXIS));
        textPart.setOpaque(false);

        JLabel name = new JLabel("Hall Symphony");
        name.setForeground(Color.WHITE);
        name.setFont(new Font("Segoe UI", Font.BOLD, 18));
        textPart.add(name);

        JLabel tag = new JLabel("Premium Booking");
        tag.setForeground(StyleConfig.TEXT_MUTED);
        tag.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textPart.add(tag);

        area.add(textPart);
        return area;
    }

    // ── Top bar ───────────────────────────────────────────────────────────────
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Color.WHITE);
        bar.setPreferredSize(new Dimension(0, 75));
        bar.setBorder(new MatteBorder(0, 0, 1, 0, StyleConfig.BORDER_COLOR));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 18));
        left.setOpaque(false);

        JLabel title = new JLabel("Dashboard Overview");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(StyleConfig.TEXT_COLOR);
        left.add(title);
        bar.add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 24, 15));
        right.setOpaque(false);

        // Real-time clock
        JLabel clock = new JLabel();
        clock.setFont(new Font("Segoe UI", Font.BOLD, 15));
        clock.setForeground(StyleConfig.TEXT_SECONDARY);
        Timer timer = new Timer(1000, e -> {
            clock.setText(java.time.format.DateTimeFormatter.ofPattern("hh:mm:ss a | MMM dd").format(java.time.LocalDateTime.now()));
        });
        timer.start();
        right.add(clock);
        
        right.add(Box.createHorizontalStrut(10));
        
        // Role Badge
        JLabel badge = StyleConfig.createBadge(user.getRole(), StyleConfig.PRIMARY_LIGHT, StyleConfig.PRIMARY_COLOR, StyleConfig.PRIMARY_COLOR);
        right.add(badge);

        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    protected void addSidebarButton(JPanel sidebar, String text, String cardName) {
        JButton btn = new JButton(text) {
            boolean hovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                    public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                boolean active = (activeNavBtn == this);
                if (active) {
                    g2.setColor(new Color(255, 255, 255, 10));
                    g2.fillRoundRect(12, 4, getWidth()-24, getHeight()-8, 10, 10);
                    g2.setColor(StyleConfig.PRIMARY_COLOR);
                    g2.fillRoundRect(0, 12, 5, getHeight()-24, 0, 0); // Active indicator
                } else if (hovered) {
                    g2.setColor(new Color(255, 255, 255, 5));
                    g2.fillRoundRect(12, 4, getWidth()-24, getHeight()-8, 10, 10);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };

        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(StyleConfig.SIDEBAR_TEXT);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(280, 50));
        btn.setBorder(new EmptyBorder(0, 44, 0, 20)); // Increased left margin (44) for hierarchy

        btn.addActionListener(e -> {
            if (activeNavBtn != null) {
                activeNavBtn.setForeground(StyleConfig.SIDEBAR_TEXT);
                activeNavBtn.setFont(StyleConfig.SIDEBAR_NAV);
            }
            activeNavBtn = btn;
            btn.setForeground(Color.WHITE);
            btn.setFont(StyleConfig.SIDEBAR_BOLD);
            
            CardLayout cl = (CardLayout) contentArea.getLayout();
            cl.show(contentArea, cardName);
            repaint();
        });

        sidebar.add(btn);
        sidebar.add(Box.createVerticalStrut(2));
        
        // Auto-select first button
        if (activeNavBtn == null) {
            activeNavBtn = btn; // Tentatively assign to prevent subsequent buttons from auto-clicking
            SwingUtilities.invokeLater(() -> btn.doClick());
        }
    }
}
