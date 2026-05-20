package co.inventech.view.components;

import co.inventech.util.UITheme;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class LogoPanel extends JPanel {
    private final String tagline;
    public LogoPanel(String tagline) {
        this.tagline = tagline;
        setOpaque(false);
        setPreferredSize(new Dimension(220, 80));
    }
    @Override protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Cuadro logo con gradiente
        int sz = 44;
        int y = (getHeight()-sz)/2;
        GradientPaint gp = new GradientPaint(0,y,UITheme.ACCENT_BLUE, sz, y+sz, UITheme.ACCENT_TEAL);
        g2.setPaint(gp);
        g2.fill(new RoundRectangle2D.Float(8, y, sz, sz, 12, 12));
        g2.setColor(Color.WHITE);
        g2.setFont(UITheme.font(20, Font.BOLD));
        FontMetrics fm = g2.getFontMetrics();
        String s = "I";
        g2.drawString(s, 8 + (sz-fm.stringWidth(s))/2, y + (sz+fm.getAscent())/2 - 4);

        // Texto Inventech
        g2.setColor(UITheme.TEXT_PRIMARY);
        g2.setFont(UITheme.font(20, Font.BOLD));
        g2.drawString("Inventech", 64, y + 22);
        g2.setColor(UITheme.TEXT_MUTED);
        g2.setFont(UITheme.font(10));
        g2.drawString(tagline, 64, y + 38);
        g2.dispose();
    }
}
