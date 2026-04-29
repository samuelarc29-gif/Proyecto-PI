package com.inventech.view.crud;

import com.inventech.config.ConexionBD;
import com.inventech.dao.AsignacionTecnicoDAO;
import com.inventech.model.AsignacionTecnico;
import com.inventech.util.Tema;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.List;

import static com.inventech.view.crud.FormUtils.*;

public class AsignacionTecnicoView extends JPanel {

    private final AsignacionTecnicoDAO dao = new AsignacionTecnicoDAO();

    private final JComboBox<Item> cboTicket = new JComboBox<>();
    private final JComboBox<Item> cboTecnico = new JComboBox<>();
    private final JTextArea txtDiag = new JTextArea(3, 25);
    private final JTextArea txtSol = new JTextArea(3, 25);
    private final JTextArea txtObs = new JTextArea(2, 25);
    private final JTextArea txtCierre = new JTextArea(2, 25);

    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Ticket", "Técnico", "F. Asignación", "F. Atención", "Diagnóstico"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabla = new JTable(modelo);
    private Integer idSel;

    public AsignacionTecnicoView() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Tema.FONDO);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel t = new JLabel("Asignación de técnicos");
        t.setFont(Tema.FUENTE_TITULO); t.setForeground(Tema.VERDE);
        add(t, BorderLayout.NORTH);
        add(panelForm(), BorderLayout.WEST);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        tabla.getSelectionModel().addListSelectionListener(e -> seleccionar());
        cargarCombos(); cargar();
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
        g.gridy = y++; p.add(label("Técnico"), g);
        g.gridy = y++; p.add(cboTecnico, g);
        g.gridy = y++; p.add(label("Diagnóstico"), g);
        wrap(txtDiag); g.gridy = y++; p.add(new JScrollPane(txtDiag), g);
        g.gridy = y++; p.add(label("Solución aplicada"), g);
        wrap(txtSol); g.gridy = y++; p.add(new JScrollPane(txtSol), g);
        g.gridy = y++; p.add(label("Observaciones"), g);
        wrap(txtObs); g.gridy = y++; p.add(new JScrollPane(txtObs), g);
        g.gridy = y++; p.add(label("Cierre técnico"), g);
        wrap(txtCierre); g.gridy = y++; p.add(new JScrollPane(txtCierre), g);

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

    private void wrap(JTextArea a) { a.setLineWrap(true); a.setWrapStyleWord(true); }

    private void cargarCombos() {
        cboTicket.removeAllItems(); cboTecnico.removeAllItems();
        try (Connection c = ConexionBD.getConnection()) {
            try (ResultSet rs = c.prepareStatement(
                    "SELECT id_ticket, CONCAT('#',id_ticket,' - ',LEFT(descripcion_falla,40)) FROM ticket ORDER BY id_ticket DESC").executeQuery()) {
                while (rs.next()) cboTicket.addItem(new Item(rs.getInt(1), rs.getString(2)));
            }
            // Técnicos: usuarios con rol 'Técnico de soporte'
            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT u.id_usuario, u.nombre_completo FROM usuario u INNER JOIN rol r ON r.id_rol=u.id_rol " +
                    "WHERE r.nombre_rol IN ('Técnico de soporte','Coordinador de soporte técnico') AND u.estado_usuario='Activo' " +
                    "ORDER BY u.nombre_completo");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) cboTecnico.addItem(new Item(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException e) { msg(e); }
    }

    private void cargar() {
        modelo.setRowCount(0);
        try {
            List<AsignacionTecnico> lista = dao.listar();
            for (AsignacionTecnico a : lista) {
                modelo.addRow(new Object[]{
                        a.getIdAsignacionTecnico(), a.getDescripcionTicket(), a.getNombreTecnico(),
                        a.getFechaAsignacion(), a.getFechaAtencion(), a.getDiagnostico()
                });
            }
        } catch (Exception e) { msg(e); }
    }

    private void seleccionar() {
        int r = tabla.getSelectedRow();
        if (r < 0) return;
        idSel = (Integer) modelo.getValueAt(r, 0);
        try {
            for (AsignacionTecnico a : dao.listar()) if (a.getIdAsignacionTecnico() == idSel) {
                seleccionarPorId(cboTicket, a.getIdTicket());
                seleccionarPorId(cboTecnico, a.getIdUsuarioTecnico());
                txtDiag.setText(n(a.getDiagnostico()));
                txtSol.setText(n(a.getSolucionAplicada()));
                txtObs.setText(n(a.getObservaciones()));
                txtCierre.setText(n(a.getCierreTecnico()));
                return;
            }
        } catch (Exception e) { msg(e); }
    }
    private String n(String s) { return s == null ? "" : s; }

    private AsignacionTecnico leerForm() {
        AsignacionTecnico a = new AsignacionTecnico();
        a.setIdTicket(idDe(cboTicket));
        a.setIdUsuarioTecnico(idDe(cboTecnico));
        a.setDiagnostico(txtDiag.getText());
        a.setSolucionAplicada(txtSol.getText());
        a.setObservaciones(txtObs.getText());
        a.setCierreTecnico(txtCierre.getText());
        return a;
    }

    private void guardar() {
        try {
            AsignacionTecnico a = leerForm();
            if (a.getIdTicket() < 0 || a.getIdUsuarioTecnico() < 0) {
                JOptionPane.showMessageDialog(this, "Ticket y técnico son obligatorios"); return;
            }
            dao.insertar(a); limpiar(); cargar();
        } catch (Exception e) { msg(e); }
    }

    private void actualizar() {
        if (idSel == null) { JOptionPane.showMessageDialog(this, "Seleccione un registro"); return; }
        try {
            AsignacionTecnico a = leerForm(); a.setIdAsignacionTecnico(idSel);
            dao.actualizar(a); limpiar(); cargar();
        } catch (Exception e) { msg(e); }
    }

    private void eliminar() {
        if (idSel == null) return;
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar asignación?") != JOptionPane.YES_OPTION) return;
        try { dao.eliminar(idSel); limpiar(); cargar(); } catch (Exception e) { msg(e); }
    }

    private void limpiar() {
        idSel = null;
        txtDiag.setText(""); txtSol.setText(""); txtObs.setText(""); txtCierre.setText("");
        tabla.clearSelection();
    }

    private void msg(Exception e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
}
