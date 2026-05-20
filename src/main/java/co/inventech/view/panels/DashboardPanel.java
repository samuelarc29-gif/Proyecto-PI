package co.inventech.view.panels;

import co.inventech.dao.DashboardDAO;
import co.inventech.util.Sesion;
import co.inventech.util.UITheme;
import co.inventech.view.components.StatCard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class DashboardPanel extends JPanel {
    private final DashboardDAO dao = new DashboardDAO();
    private final JPanel kpis = new JPanel(new GridLayout(2, 4, 16, 16));
    private final BarPanel barArea = new BarPanel("Activos por área", UITheme.ACCENT_BLUE);
    private final BarPanel barTickets = new BarPanel("Tickets por estado", UITheme.ACCENT_TEAL);

    public DashboardPanel() {
        setOpaque(false);
        setLayout(new BorderLayout(0, 16));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Bienvenido, " + (Sesion.usuario()==null?"":Sesion.usuario().getNombreCompleto()));
        title.setFont(UITheme.font(22, Font.BOLD));
        title.setForeground(UITheme.TEXT_PRIMARY);
        JLabel sub = new JLabel("Resumen ejecutivo del sistema de gestión de activos tecnológicos");
        sub.setFont(UITheme.font(12));
        sub.setForeground(UITheme.TEXT_MUTED);
        JPanel t = new JPanel(); t.setOpaque(false); t.setLayout(new BoxLayout(t, BoxLayout.Y_AXIS));
        t.add(title); t.add(Box.createVerticalStrut(4)); t.add(sub);
        header.add(t, BorderLayout.WEST);

        kpis.setOpaque(false);
        kpis.setBorder(new EmptyBorder(4, 0, 4, 0));

        JPanel charts = new JPanel(new GridLayout(1, 2, 16, 0));
        charts.setOpaque(false);
        charts.add(barArea);
        charts.add(barTickets);

        add(header, BorderLayout.NORTH);
        add(kpis, BorderLayout.CENTER);
        add(charts, BorderLayout.SOUTH);

        refresh();
        // Refrescar al hacerse visible
        addHierarchyListener(e -> { if (isShowing()) refresh(); });
    }

    public void refresh() {
        kpis.removeAll();
        try {
            Map<String,Integer> data = dao.kpis();
            Color[] colors = { UITheme.ACCENT_BLUE, UITheme.ACCENT_AMBER, UITheme.ACCENT_TEAL, new Color(0x8B5CF6),
                               UITheme.ACCENT_RED, new Color(0x06B6D4), new Color(0x84CC16), new Color(0xEC4899) };
            int i = 0;
            for (var e : data.entrySet()) {
                kpis.add(new StatCard(e.getKey(), String.valueOf(e.getValue()), colors[i++ % colors.length]));
            }
            barArea.setData(dao.activosPorArea());
            barTickets.setData(dao.ticketsPorEstado());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error cargando dashboard: "+ex.getMessage());
        }
        kpis.revalidate(); kpis.repaint();
    }

    /** Gráfico de barras horizontal mínimo. */
    static class BarPanel extends JPanel {
        private final String title; private final Color color;
        private LinkedHashMap<String,Integer> data = new LinkedHashMap<>();
        BarPanel(String title, Color color) { this.title = title; this.color = color; setPreferredSize(new Dimension(0, 260)); setOpaque(false); }
        void setData(LinkedHashMap<String,Integer> d) { this.data = d; repaint(); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(UITheme.BG_CARD);
            g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),16,16));
            g2.setColor(UITheme.TEXT_PRIMARY);
            g2.setFont(UITheme.font(13, Font.BOLD));
            g2.drawString(title, 20, 28);
            int top = 50, rowH = 26, left = 140, right = getWidth()-20;
            int max = data.values().stream().mapToInt(Integer::intValue).max().orElse(1);
            if (max == 0) max = 1;
            int i = 0;
            g2.setFont(UITheme.font(11));
            for (var e : data.entrySet()) {
                int y = top + i*rowH;
                g2.setColor(UITheme.TEXT_MUTED);
                String label = e.getKey(); if (label.length()>18) label = label.substring(0,17)+"…";
                g2.drawString(label, 20, y + 14);
                int w = (int) ((right - left) * (e.getValue()/(double)max));
                g2.setColor(color);
                g2.fill(new RoundRectangle2D.Float(left, y+4, Math.max(2,w), 14, 8, 8));
                g2.setColor(UITheme.TEXT_PRIMARY);
                g2.drawString(String.valueOf(e.getValue()), left + Math.max(2,w) + 8, y + 14);
                i++; if (top + i*rowH > getHeight() - 16) break;
            }
            g2.dispose();
        }
    }
}
