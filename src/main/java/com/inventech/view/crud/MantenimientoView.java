package com.inventech.view.crud;

import com.inventech.config.ConexionBD;
import com.inventech.dao.MantenimientoDAO;
import com.inventech.model.Mantenimiento;
import com.inventech.util.Tema;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static com.inventech.view.crud.FormUtils.*;

public class MantenimientoView extends JPanel {

    private final MantenimientoDAO dao = new MantenimientoDAO();

    private final JComboBox<Item> cboTicket = new JComboBox<>();
    private final JComboBox<Item> cboTipo = new JComboBox<>();
    private final JTextField txtFecha = new JTextField(LocalDate.now().toString(), 12);
    private final JTextArea txtObs = new JTextArea(4, 25);

    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Ticket", "Tipo", "F. Programada", "Observaciones"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabla = new JTable(modelo);
    private Integer idSel;

    public MantenimientoView() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Tema.FONDO);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel t = new JLabel("Mantenimientos");
        t.setFont(Tema.FUENTE_TITULO); t.setForeground(Tema.VERDE);
        add(t, BorderLayout.NORTH);
        add(panelForm(), BorderLayout.WEST);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        tabla.getSelectionModel().addListSelectionListener(e -> seleccionar());
        cargarCombos();
        cargar();
    }

    private JPanel panelForm() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Tema.PANEL);
        p.setBorder(new EmptyBorder(16, 16, 16, 16));
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.fill = GridBagConstraints.HORIZONTAL; g.insets = new Insets(5, 5, 5, 5);
        int y = 0;
        g.gridy = y++; p.add(label("Ticket"), g);
        g.gridy = y++; p.add(cboTicket, g);
        g.gridy = y++; p.add(label("Tipo de mantenimiento"), g);
        g.gridy = y++; p.add(cboTipo, g);
        g.gridy = y++; p.add(label("Fecha programada (yyyy-mm-dd)"), g);
        g.gridy = y++; p.add(txtFecha, g);
        g.gridy = y++; p.add(label("Observaciones"), g);
        txtObs.setLineWrap(true); txtObs.setWrapStyleWord(true);
        g.gridy = y++; p.add(new JScrollPane(txtObs), g);

        JButton bG = boton("Guardar", Tema.VERDE);
        JButton bA = boton("Actualizar", Tema.AZUL);
        JButton bE = boton("Eliminar", Tema.PELIGRO);
        JButton bL = boton("Limpiar", new Color(100, 116, 139));
        bG.addActionListener(e -> guardar());
        bA.addActionListener(e -> actualizar());
        bE.addActionListener(e -> eliminar());
        bL.addActionListener(e -> limpiar());
        g.gridy = y++; p.add(bG, g);
        g.gridy = y++; p.add(bA, g);
        g.gridy = y++; p.add(bE, g);
        g.gridy = y++; p.add(bL, g);
        return p;
    }

    private void cargarCombos() {
        cboTicket.removeAllItems(); cboTipo.removeAllItems();
        try (Connection c = ConexionBD.getConnection()) {
            try (ResultSet rs = c.prepareStatement(
                    "SELECT id_ticket, CONCAT('#',id_ticket,' - ',LEFT(descripcion_falla,40)) FROM ticket ORDER BY id_ticket DESC").executeQuery()) {
                while (rs.next()) cboTicket.addItem(new Item(rs.getInt(1), rs.getString(2)));
            }
            try (ResultSet rs = c.prepareStatement(
                    "SELECT id_tipo_mantenimiento, nombre_tipo FROM tipo_mantenimiento ORDER BY nombre_tipo").executeQuery()) {
                while (rs.next()) cboTipo.addItem(new Item(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException e) { msg(e); }
    }

    private void cargar() {
        modelo.setRowCount(0);
        try {
            List<Mantenimiento> lista = dao.listar();
            for (Mantenimiento m : lista) {
                modelo.addRow(new Object[]{
                        m.getIdMantenimiento(), m.getDescripcionTicket(), m.getNombreTipo(),
                        m.getFechaProgramada(), m.getObservaciones()
                });
            }
        } catch (Exception e) { msg(e); }
    }

    private void seleccionar() {
        int r = tabla.getSelectedRow();
        if (r < 0) return;
        idSel = (Integer) modelo.getValueAt(r, 0);
        try {
            for (Mantenimiento m : dao.listar()) if (m.getIdMantenimiento() == idSel) {
                seleccionarPorId(cboTicket, m.getIdTicket());
                seleccionarPorId(cboTipo, m.getIdTipoMantenimiento());
                txtFecha.setText(m.getFechaProgramada() == null ? "" : m.getFechaProgramada().toString());
                txtObs.setText(m.getObservaciones() == null ? "" : m.getObservaciones());
                return;
            }
        } catch (Exception e) { msg(e); }
    }

    private Mantenimiento leerForm() {
        Mantenimiento m = new Mantenimiento();
        m.setIdTicket(idDe(cboTicket));
        m.setIdTipoMantenimiento(idDe(cboTipo));
        try { m.setFechaProgramada(txtFecha.getText().isBlank() ? null : LocalDate.parse(txtFecha.getText().trim())); }
        catch (Exception e) { m.setFechaProgramada(null); }
        m.setObservaciones(txtObs.getText());
        return m;
    }

    private void guardar() {
        try {
            Mantenimiento m = leerForm();
            if (m.getIdTicket() < 0 || m.getIdTipoMantenimiento() < 0) {
                JOptionPane.showMessageDialog(this, "Ticket y tipo son obligatorios"); return;
            }
            dao.insertar(m); limpiar(); cargar();
        } catch (Exception e) { msg(e); }
    }

    private void actualizar() {
        if (idSel == null) { JOptionPane.showMessageDialog(this, "Seleccione un registro"); return; }
        try {
            Mantenimiento m = leerForm(); m.setIdMantenimiento(idSel);
            dao.actualizar(m); limpiar(); cargar();
        } catch (Exception e) { msg(e); }
    }

    private void eliminar() {
        if (idSel == null) return;
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar mantenimiento?") != JOptionPane.YES_OPTION) return;
        try { dao.eliminar(idSel); limpiar(); cargar(); } catch (Exception e) { msg(e); }
    }

    private void limpiar() {
        idSel = null; txtFecha.setText(LocalDate.now().toString()); txtObs.setText("");
        tabla.clearSelection();
    }

    private void msg(Exception e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
}
