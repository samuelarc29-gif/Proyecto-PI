package co.inventech.view.components;
import co.inventech.util.UITheme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public final class Tables {
    public static DefaultTableModel readOnlyModel(String... cols) {
        return new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
    }
    public static JScrollPane wrap(JTable table) {
        table.setRowHeight(34);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(new Color(0x1F3A8A));
        table.setSelectionForeground(Color.WHITE);
        table.setFillsViewportHeight(true);
        table.setFont(UITheme.font(12));
        JTableHeader h = table.getTableHeader();
        h.setBackground(UITheme.BG_CARD);
        h.setForeground(UITheme.TEXT_MUTED);
        h.setFont(UITheme.font(11, Font.BOLD));
        h.setBorder(BorderFactory.createMatteBorder(0,0,1,0, UITheme.BORDER));
        DefaultTableCellRenderer pad = new DefaultTableCellRenderer();
        pad.setBorder(new EmptyBorder(0, 12, 0, 12));
        for (int i = 0; i < table.getColumnCount(); i++) table.getColumnModel().getColumn(i).setCellRenderer(pad);
        JScrollPane sp = new JScrollPane(table);
        sp.getViewport().setBackground(UITheme.BG_SECONDARY);
        sp.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        return sp;
    }
    public static JPanel pageHeader(String title, String subtitle) {
        JPanel p = new JPanel(); p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(0,0,14,0));
        JLabel t = new JLabel(title); t.setForeground(UITheme.TEXT_PRIMARY); t.setFont(UITheme.font(20, Font.BOLD));
        JLabel s = new JLabel(subtitle); s.setForeground(UITheme.TEXT_MUTED); s.setFont(UITheme.font(12));
        t.setAlignmentX(Component.LEFT_ALIGNMENT); s.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(t); p.add(Box.createVerticalStrut(2)); p.add(s);
        return p;
    }
    private Tables() {}
}
