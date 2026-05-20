package co.inventech.view.components;

import co.inventech.util.UITheme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class StatCard extends JPanel {
    private final JLabel lblTitulo = new JLabel();
    private final JLabel lblValor  = new JLabel("0");
    private final Color accent;

    public StatCard(String titulo, String valor, Color accent) {
        this.accent = accent;
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(20, 22, 20, 22));
        setPreferredSize(new Dimension(230, 110));

        lblTitulo.setText(titulo.toUpperCase());
        lblTitulo.setForeground(UITheme.TEXT_MUTED);
        lblTitulo.setFont(UITheme.font(11, Font.BOLD));

        lblValor.setText(valor);
        lblValor.setForeground(UITheme.TEXT_PRIMARY);
        lblValor.setFont(UITheme.font(34, Font.BOLD));

        JPanel center = new JPanel(new BorderLayout(0, 6));
        center.setOpaque(false);
        center.add(lblTitulo, BorderLayout.NORTH);
        center.add(lblValor, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
    }

    public void setValor(String v) { lblValor.setText(v); }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(UITheme.BG_CARD);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
        g2.setColor(accent);
        g2.fillRect(0, 0, 4, getHeight());
        g2.dispose();
        super.paintComponent(g);
    }
}
