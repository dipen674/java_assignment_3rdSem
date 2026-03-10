package com.hallsymphony.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * HallButton — A fully custom-painted rounded button.
 *
 * Bypasses the Look & Feel button renderer entirely, so it looks
 * IDENTICAL on Windows, Linux, WSL, and macOS — no platform quirks.
 *
 * Supports: PRIMARY (blue), DANGER (red), SUCCESS (green), SECONDARY (white/outline)
 */
public class HallButton extends JButton {

    public enum Variant { PRIMARY, DANGER, SUCCESS, SECONDARY, GHOST }

    // Colours for each variant
    private static final Color[] PRIMARY_COLS  = { new Color(37,99,235),  new Color(29,78,216),  new Color(0,0,0,0)   };
    private static final Color[] DANGER_COLS   = { new Color(220,38,38),  new Color(185,28,28),  new Color(0,0,0,0)   };
    private static final Color[] SUCCESS_COLS  = { new Color(22,163,74),  new Color(21,128,61),  new Color(0,0,0,0)   };
    private static final Color[] SECONDARY_COLS= { new Color(255,255,255),new Color(241,245,249),new Color(226,232,240) };
    private static final Color[] GHOST_COLS    = { new Color(0,0,0,0),    new Color(219,234,254),new Color(0,0,0,0)   };

    // [0]=bg normal  [1]=bg hover  [2]=border
    private Color bgNormal, bgHover, borderColor;
    private Color fgColor;
    private Color currentBg;
    private boolean hovered = false;
    private final int radius;

    public HallButton(String text, Variant variant) {
        super(text);
        this.radius = 8;
        applyVariant(variant);
        setup();
    }

    public HallButton(String text) {
        this(text, Variant.PRIMARY);
    }

    private void applyVariant(Variant v) {
        Color[] cols;
        switch (v) {
            case DANGER:    cols = DANGER_COLS;    fgColor = Color.WHITE; break;
            case SUCCESS:   cols = SUCCESS_COLS;   fgColor = Color.WHITE; break;
            case SECONDARY: cols = SECONDARY_COLS; fgColor = new Color(15,23,42); break;
            case GHOST:     cols = GHOST_COLS;     fgColor = new Color(37,99,235); break;
            default:        cols = PRIMARY_COLS;   fgColor = Color.WHITE; break;
        }
        bgNormal    = cols[0];
        bgHover     = cols[1];
        borderColor = cols[2];
        currentBg   = bgNormal;
    }

    private void setup() {
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setFont(new Font("Dialog", Font.BOLD, 13));
        setForeground(fgColor);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(9, 22, 9, 22));

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                currentBg = bgHover;
                repaint();
            }
            public void mouseExited(MouseEvent e) {
                hovered = false;
                currentBg = bgNormal;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,        RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,   RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,           RenderingHints.VALUE_RENDER_QUALITY);

        int w = getWidth(), h = getHeight();
        RoundRectangle2D rr = new RoundRectangle2D.Float(1, 1, w - 2, h - 2, radius, radius);

        // Fill
        g2.setColor(currentBg);
        g2.fill(rr);

        // Subtle gradient overlay for depth
        if (currentBg.getAlpha() > 0) {
            GradientPaint gp = new GradientPaint(
                0, 0, new Color(255, 255, 255, hovered ? 10 : 25),
                0, h, new Color(0, 0, 0, hovered ? 20 : 10));
            g2.setPaint(gp);
            g2.fill(rr);
        }

        // Border
        if (borderColor.getAlpha() > 0) {
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(1.2f));
            g2.draw(rr);
        }

        g2.dispose();
        super.paintComponent(g);  // Let Swing draw text and icon
    }

    @Override
    protected void paintBorder(Graphics g) {
        // handled in paintComponent
    }

    // ── Static factory helpers (drop-in replacements for StyleConfig.styleButton) ──

    public static HallButton primary(String text)   { return new HallButton(text, Variant.PRIMARY); }
    public static HallButton danger(String text)    { return new HallButton(text, Variant.DANGER); }
    public static HallButton success(String text)   { return new HallButton(text, Variant.SUCCESS); }
    public static HallButton secondary(String text) { return new HallButton(text, Variant.SECONDARY); }
    public static HallButton ghost(String text)     { return new HallButton(text, Variant.GHOST); }
}
