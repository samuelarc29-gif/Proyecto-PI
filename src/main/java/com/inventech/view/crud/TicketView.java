package com.inventech.view.crud;

import com.inventech.config.ConexionBD;
import com.inventech.dao.TicketDAO;
import com.inventech.model.Ticket;
import com.inventech.util.Sesion;
import com.inventech.util.Tema;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.List;

import static com.inventech.view.crud.FormUtils.*;

public class TicketView extends JPanel {

    private final TicketDAO dao = new TicketDAO();

    private final JComboBox<Item> cboActivo = new JComboBox<>();
    private final JComboBox<Item> cboUsuario = new JComboBox<>();
    private final JComboBox<Item> cboEstado = new JComboBox<>();
    private final JComboBox<Item> cboAns = new JComboBox<>();
    private final JComboBox<String> cboPrioridad = new JComboBox<>(new String[]{"Alta", "Media", "Baja"});
    private final JTextArea txtDesc = new JTextArea(4, 25);

    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Activo", "Solicitante", "Estado", "Prioridad", "F. Reporte", "Descripción"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabla = new JTable(modelo);
    private Integer idSel;

    public TicketView() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Tema.FONDO);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel t = new JLabel("Tickets de soporte");
        t.setFont(Tema.FUENTE_TITULO);
        t.setForeground(Tema.VERDE);
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
        g.gridy = y++; p.add(label("Activo"), g);
        g.gridy = y++; p.add(cboActivo, g);
        g.gridy = y++; p.add(label("Solicitante"), g);
        g.gridy = y++; p.add(cboUsuario, g);
        g.gridy = y++; p.add(label("Estado"), g);
        g.gridy = y++; p.add(cboEstado, g);
        g.gridy = y++; p.add(label("ANS"), g);
        g.gridy = y++; p.add(cboAns, g);
        g.gridy = y++; p.add(label("Prioridad"), g);
        g.gridy = y++; p.add(cboPrioridad, g);
        g.gridy = y++; p.add(label("Descripción de la falla"), g);
        txtDesc.setLineWrap(true); txtDesc.setWrapStyleWord(true);
        g.gridy = y++; p.add(new JScrollPane(txtDesc), g);

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
        cboActivo.removeAllItems(); cboUsuario.removeAllItems();
        cboEstado.removeAllItems(); cboAns.removeAllItems();
        try (Connection c = ConexionBD.getConnection()) {
            try (ResultSet rs = c.prepareStatement(
                    "SELECT id_activo, codigo_interno FROM activo ORDER BY codigo_interno").executeQuery()) {
                while (rs.next()) cboActivo.addItem(new Item(rs.getInt(1), rs.getString(2)));
            }
            try (ResultSet rs = c.prepareStatement(
                    "SELECT id_usuario, nombre_completo FROM usuario WHERE estado_usuario='Activo' ORDER BY nombre_completo").executeQuery()) {
                while (rs.next()) cboUsuario.addItem(new Item(rs.getInt(1), rs.getString(2)));
            }
            try (ResultSet rs = c.prepareStatement(
                    "SELECT id_estado_ticket, nombre_estado FROM estado_ticket ORDER BY id_estado_ticket").executeQuery()) {
                while (rs.next()) cboEstado.addItem(new Item(rs.getInt(1), rs.getString(2)));
            }
            cboAns.addItem(new Item(-1, "(Ninguno)"));
            try (ResultSet rs = c.prepareStatement(
                    "SELECT id_ans, CONCAT(tipo_solicitud,' - ',prioridad) FROM ans ORDER BY tipo_solicitud").executeQuery()) {
                while (rs.next()) cboAns.addItem(new Item(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException e) { msg(e); }
        // Preseleccionar usuario logueado si existe
        if (Sesion.getUsuario() != null) seleccionarPorId(cboUsuario, Sesion.getUsuario().getIdUsuario());
    }

    private void cargar() {
        modelo.setRowCount(0);
        try {
            List<Ticket> lista = dao.listar();
            for (Ticket t : lista) {
                modelo.addRow(new Object[]{
                        t.getIdTicket(), t.getCodigoActivo(), t.getNombreUsuario(),
                        t.getNombreEstado(), t.getPrioridad(), t.getFechaReporte(), t.getDescripcionFalla()
                });
            }
        } catch (Exception e) { msg(e); }
    }

    private void seleccionar() {
        int r = tabla.getSelectedRow();
        if (r < 0) return;
        idSel = (Integer) modelo.getValueAt(r, 0);
        try {
            for (Ticket t : dao.listar()) if (t.getIdTicket() == idSel) {
                seleccionarPorId(cboActivo, t.getIdActivo());
                seleccionarPorId(cboUsuario, t.getIdUsuarioSolicitante());
                seleccionarPorId(cboEstado, t.getIdEstadoTicket());
                seleccionarPorId(cboAns, t.getIdAns() == null ? -1 : t.getIdAns());
                cboPrioridad.setSelectedItem(t.getPrioridad());
                txtDesc.setText(t.getDescripcionFalla());
                return;
            }
        } catch (Exception e) { msg(e); }
    }

    private Ticket leerForm() {
        Ticket t = new Ticket();
        t.setIdActivo(idDe(cboActivo));
        t.setIdUsuarioSolicitante(idDe(cboUsuario));
        t.setIdEstadoTicket(idDe(cboEstado));
        int ans = idDe(cboAns);
        t.setIdAns(ans <= 0 ? null : ans);
        t.setPrioridad((String) cboPrioridad.getSelectedItem());
        t.setDescripcionFalla(txtDesc.getText());
        return t;
    }

    private void guardar() {
        try {
            Ticket t = leerForm();
            if (t.getIdActivo() < 0 || t.getIdUsuarioSolicitante() < 0 || t.getIdEstadoTicket() < 0
                    || t.getDescripcionFalla() == null || t.getDescripcionFalla().isBlank()) {
                JOptionPane.showMessageDialog(this, "Activo, solicitante, estado y descripción son obligatorios"); return;
            }
            dao.insertar(t); limpiar(); cargar();
        } catch (Exception e) { msg(e); }
    }

    private void actualizar() {
        if (idSel == null) { JOptionPane.showMessageDialog(this, "Seleccione un registro"); return; }
        try {
            Ticket t = leerForm(); t.setIdTicket(idSel);
            dao.actualizar(t); limpiar(); cargar();
        } catch (Exception e) { msg(e); }
    }

    private void eliminar() {
        if (idSel == null) return;
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar ticket?") != JOptionPane.YES_OPTION) return;
        try { dao.eliminar(idSel); limpiar(); cargar(); } catch (Exception e) { msg(e); }
    }

    private void limpiar() {
        idSel = null;
        txtDesc.setText("");
        cboPrioridad.setSelectedIndex(1);
        if (cboEstado.getItemCount() > 0) cboEstado.setSelectedIndex(0);
        if (cboAns.getItemCount() > 0) cboAns.setSelectedIndex(0);
        tabla.clearSelection();
    }

    private void msg(Exception e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
}
