package com.inventech.view.crud;

import com.inventech.dao.ReporteDAO;
import com.inventech.util.Tema;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import static com.inventech.view.crud.FormUtils.boton;
import static com.inventech.view.crud.FormUtils.label;

public class ReporteView extends JPanel {

    private final ReporteDAO dao = new ReporteDAO();
    private final JComboBox<String> cboReporte = new JComboBox<>();
    private final DefaultTableModel modelo = new DefaultTableModel();
    private final JTable tabla = new JTable(modelo);

    public ReporteView() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Tema.FONDO);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel t = new JLabel("Reportes");
        t.setFont(Tema.FUENTE_TITULO); t.setForeground(Tema.VERDE);
        add(t, BorderLayout.NORTH);

        for (String n : dao.reportesDisponibles().keySet()) cboReporte.addItem(n);
        JButton btn = boton("Generar reporte", Tema.AZUL);
        JButton btnExp = boton("Exportar CSV", Tema.VERDE);
        btn.addActionListener(e -> generar());
        btnExp.addActionListener(e -> exportar());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        top.setBackground(Tema.PANEL);
        top.setBorder(new EmptyBorder(8, 12, 8, 12));
        JLabel l = label("Seleccione reporte:");
        top.add(l); top.add(cboReporte); top.add(btn); top.add(btnExp);

        JPanel center = new JPanel(new BorderLayout(8, 8));
        center.setBackground(Tema.FONDO);
        center.add(top, BorderLayout.NORTH);
        center.add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        generar();
    }

    private void generar() {
        try {
            String sel = (String) cboReporte.getSelectedItem();
            if (sel == null) return;
            ReporteDAO.Tabla t = dao.ejecutarReporte(sel);
            modelo.setDataVector(
                    t.filas.toArray(new Object[0][]),
                    t.columnas.toArray()
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void exportar() {
        if (modelo.getRowCount() == 0) { JOptionPane.showMessageDialog(this, "No hay datos para exportar"); return; }
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new java.io.File("reporte.csv"));
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        try (java.io.PrintWriter pw = new java.io.PrintWriter(fc.getSelectedFile(), java.nio.charset.StandardCharsets.UTF_8)) {
            int cols = modelo.getColumnCount();
            for (int i = 0; i < cols; i++) { if (i > 0) pw.print(","); pw.print(escape(modelo.getColumnName(i))); }
            pw.println();
            for (int r = 0; r < modelo.getRowCount(); r++) {
                for (int c = 0; c < cols; c++) {
                    if (c > 0) pw.print(",");
                    Object v = modelo.getValueAt(r, c);
                    pw.print(escape(v == null ? "" : v.toString()));
                }
                pw.println();
            }
            JOptionPane.showMessageDialog(this, "Reporte exportado.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private String escape(String s) {
        if (s.contains(",") || s.contains("\"") || s.contains("\n")) {
            return "\"" + s.replace("\"", "\"\"") + "\"";
        }
        return s;
    }
}
