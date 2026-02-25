package com.hallsymphony.util;

import java.awt.*;

public class StyleConfig {
    // Colors
    public static final Color PRIMARY_COLOR = new Color(41, 128, 185); // Blue
    public static final Color SECONDARY_COLOR = new Color(44, 62, 80); // Dark Blue
    public static final Color ACCENT_COLOR = new Color(192, 57, 43); // Red
    public static final Color SUCCESS_COLOR = new Color(39, 174, 96); // Green
    public static final Color BACKGROUND_COLOR = new Color(236, 240, 241); // Light Gray
    public static final Color WHITE = Color.WHITE;
    public static final Color TEXT_COLOR = new Color(52, 73, 94);

    // Fonts
    public static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 24);
    public static final Font HEADER_FONT = new Font("SansSerif", Font.BOLD, 18);
    public static final Font NORMAL_FONT = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font SMALL_FONT = new Font("SansSerif", Font.PLAIN, 12);

    // Helper for buttons
    public static void styleButton(javax.swing.JButton button) {
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(WHITE);
        button.setFocusPainted(false);
        button.setFont(HEADER_FONT);
        button.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }
}
