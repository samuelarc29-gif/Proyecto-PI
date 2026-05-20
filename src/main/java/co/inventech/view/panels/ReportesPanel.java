package co.inventech.view.panels;
import co.inventech.db.ConexionDB; import co.inventech.util.UITheme;
import co.inventech.view.components.Tables;
import javax.swing.*; import javax.swing.table.DefaultTableModel;
import java.awt.*; import java.io.*; import java.sql.*; import java.util.*;
import java.util.List;

public class ReportesPanel extends JPanel {
    private final DefaultTableModel model = Tables.readOnlyModel("Datos");
    private final JTable table = new JTable(model);
    private final JComboBox<String> tipo = new JComboBox<>(new String[]{
        "Activos por área", "Activos por estado", "Tickets por estado",
        "Mantenimientos por mes", "Licencias por vencer (30 días)"
    });
    private final JButton btnGen = new JButton("Generar");
    private final JButton btnExp = new JButton("Exportar CSV");

    public ReportesPanel() {
        setOpaque(false); setLayout(new BorderLayout());
        add(Tables.pageHeader("Reportes y consultas","Visualiza y exporta información administrativa del sistema."), BorderLayout.NORTH);
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0)); top.setOpaque(false);
        JLabel l = new JLabel("Tipo de reporte:"); l.setForeground(UITheme.TEXT_MUTED);
        tipo.setPreferredSize(new Dimension(260, 32));
        btnGen.setBackground(UITheme.ACCENT_BLUE); btnGen.setForeground(Color.WHITE);
        btnExp.setBackground(UITheme.ACCENT_TEAL); btnExp.setForeground(Color.WHITE);
        top.add(l); top.add(tipo); top.add(btnGen); top.add(btnExp);
        JPanel c = new JPanel(new BorderLayout()); c.setOpaque(false);
        c.add(top, BorderLayout.NORTH); c.add(Tables.wrap(table), BorderLayout.CENTER); add(c, BorderLayout.CENTER);
        btnGen.addActionListener(e -> generar());
        btnExp.addActionListener(e -> exportar());
    }
    private String sqlFor(String t) {
        return switch (t) {
            case "Activos por área" -> "SELECT COALESCE(a.nombre_area,'Sin área') area, COUNT(*) total FROM activo ac LEFT JOIN area a ON a.id_area=ac.id_area GROUP BY area ORDER BY total DESC";
            case "Activos por estado" -> "SELECT e.nombre_estado, COUNT(*) total FROM activo ac JOIN estado_activo e ON e.id_estado_activo=ac.id_estado_activo GROUP BY e.nombre_estado";
            case "Tickets por estado" -> "SELECT e.nombre_estado, COUNT(t.id_ticket) total FROM estado_ticket e LEFT JOIN ticket t ON t.id_estado_ticket=e.id_estado_ticket GROUP BY e.nombre_estado";
            case "Mantenimientos por mes" -> "SELECT DATE_FORMAT(fecha_programada,'%Y-%m') mes, COUNT(*) total FROM mantenimiento WHERE fecha_programada IS NOT NULL GROUP BY mes ORDER BY mes DESC";
            default -> "SELECT id_licencia, nombre_software, codigo_licencia, fecha_vencimiento FROM licencia_software WHERE fecha_vencimiento <= CURDATE() + INTERVAL 30 DAY AND estado_licencia='Activa' ORDER BY fecha_vencimiento";
        };
    }
    private void generar() {
        try (Connection c = ConexionDB.get(); PreparedStatement ps = c.prepareStatement(sqlFor((String)tipo.getSelectedItem())); ResultSet rs = ps.executeQuery()) {
            ResultSetMetaData md = rs.getMetaData();
            int n = md.getColumnCount();
            String[] cols = new String[n];
            for (int i = 0; i < n; i++) cols[i] = md.getColumnLabel(i+1);
            DefaultTableModel nm = Tables.readOnlyModel(cols);
            while (rs.next()) {
                Object[] row = new Object[n];
                for (int i = 0; i < n; i++) row[i] = rs.getObject(i+1);
                nm.addRow(row);
            }
            table.setModel(nm);
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); }
    }
    private void exportar() {
        if (table.getRowCount()==0) { JOptionPane.showMessageDialog(this,"Genera primero un reporte."); return; }
        JFileChooser jc = new JFileChooser(); jc.setSelectedFile(new File("reporte_inventech.csv"));
        if (jc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        try (PrintWriter pw = new PrintWriter(jc.getSelectedFile(), "UTF-8")) {
            int c = table.getColumnCount();
            for (int i = 0; i < c; i++) { if (i>0) pw.print(","); pw.print('"'+table.getColumnName(i)+'"'); }
            pw.println();
            for (int r = 0; r < table.getRowCount(); r++) {
                for (int i = 0; i < c; i++) { if (i>0) pw.print(",");
                    Object v = table.getValueAt(r,i); pw.print('"'+(v==null?"":v.toString().replace("\"","\"\""))+'"'); }
                pw.println();
            }
            JOptionPane.showMessageDialog(this,"Exportado a "+jc.getSelectedFile().getAbsolutePath());
        } catch (Exception ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); }
    }
}
