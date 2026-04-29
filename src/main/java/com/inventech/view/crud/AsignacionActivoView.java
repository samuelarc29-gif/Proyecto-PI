package com.inventech.view.crud;

import com.inventech.config.ConexionBD;
import com.inventech.dao.AsignacionActivoDAO;
import com.inventech.model.AsignacionActivo;
import com.inventech.util.Tema;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static com.inventech.view.crud.FormUtils.*;

public class AsignacionActivoView extends JPanel {

    private final AsignacionActivoDAO dao = new AsignacionActivoDAO();

    private final JComboBox<Item> cboActivo = new JComboBox<>();
    private final JComboBox<Item> cboUsuario = new JComboBox<>();
    private final JTextField txtFAsig = new JTextField(LocalDate.now().toString(), 12);
    private final JTextField txtFDev = new JTextField(12);
    private final JTextField txtObs = new JTextField(25);
    private final JComboBox<String> cboEstado = new JComboBox<>(new String[]{"Activa", "Finalizada"});

    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Activo", "Usuario", "F. Asig.", "F. Dev.", "Estado", "Obs."}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabla = new JTable(modelo);
    private Integer idSel;

    public AsignacionActivoView() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Tema.FONDO);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Asignación de activos");
        titulo.setFont(Tema.FUENTE_TITULO);
        titulo.setForeground(Tema.VERDE);
        add(titulo, BorderLayout.NORTH);

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
        g.gridx = 0; g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(5, 5, 5, 5);
        int y = 0;
        g.gridy = y++; p.add(label("Activo"), g);
        g.gridy = y++; p.add(cboActivo, g);
        g.gridy = y++; p.add(label("Usuario"), g);
        g.gridy = y++; p.add(cboUsuario, g);
        g.gridy = y++; p.add(label("Fecha asignación (yyyy-mm-dd)"), g);
        g.gridy = y++; p.add(txtFAsig, g);
        g.gridy = y++; p.add(label("Fecha devolución"), g);
        g.gridy = y++; p.add(txtFDev, g);
        g.gridy = y++; p.add(label("Estado"), g);
        g.gridy = y++; p.add(cboEstado, g);
        g.gridy = y++; p.add(label("Observaciones"), g);
        g.gridy = y++; p.add(txtObs, g);

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
        cboActivo.removeAllItems();
        cboUsuario.removeAllItems();
        try (Connection c = ConexionBD.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT id_activo, codigo_interno FROM activo ORDER BY codigo_interno");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) cboActivo.addItem(new Item(rs.getInt(1), rs.getString(2)));
            }
            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT id_usuario, nombre_completo FROM usuario WHERE estado_usuario='Activo' ORDER BY nombre_completo");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) cboUsuario.addItem(new Item(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException e) { msg(e); }
    }

    private void cargar() {
        modelo.setRowCount(0);
        try {
            List<AsignacionActivo> lista = dao.listar();
            for (AsignacionActivo a : lista) {
                modelo.addRow(new Object[]{
                        a.getIdAsignacionActivo(), a.getCodigoActivo(), a.getNombreUsuario(),
                        a.getFechaAsignacion(), a.getFechaDevolucion(),
                        a.getEstadoAsignacion(), a.getObservaciones()
                });
            }
        } catch (Exception e) { msg(e); }
    }

    private void seleccionar() {
        int r = tabla.getSelectedRow();
        if (r < 0) return;
        idSel = (Integer) modelo.getValueAt(r, 0);
        // Buscar IDs por texto
        try {
            List<AsignacionActivo> lista = dao.listar();
            for (AsignacionActivo a : lista) if (a.getIdAsignacionActivo() == idSel) {
                seleccionarPorId(cboActivo, a.getIdActivo());
                seleccionarPorId(cboUsuario, a.getIdUsuario());
                txtFAsig.setText(a.getFechaAsignacion() == null ? "" : a.getFechaAsignacion().toString());
                txtFDev.setText(a.getFechaDevolucion() == null ? "" : a.getFechaDevolucion().toString());
                cboEstado.setSelectedItem(a.getEstadoAsignacion());
                txtObs.setText(a.getObservaciones() == null ? "" : a.getObservaciones());
                return;
            }
        } catch (Exception e) { msg(e); }
    }

    private AsignacionActivo leerForm() {
        AsignacionActivo a = new AsignacionActivo();
        a.setIdActivo(idDe(cboActivo));
        a.setIdUsuario(idDe(cboUsuario));
        a.setFechaAsignacion(parse(txtFAsig.getText()));
        a.setFechaDevolucion(parse(txtFDev.getText()));
        a.setEstadoAsignacion((String) cboEstado.getSelectedItem());
        a.setObservaciones(txtObs.getText());
        return a;
    }

    private LocalDate parse(String s) {
        if (s == null || s.isBlank()) return null;
        try { return LocalDate.parse(s.trim()); } catch (Exception e) { return null; }
    }

    private void guardar() {
        try {
            AsignacionActivo a = leerForm();
            if (a.getIdActivo() < 0 || a.getIdUsuario() < 0 || a.getFechaAsignacion() == null) {
                JOptionPane.showMessageDialog(this, "Activo, usuario y fecha de asignación son obligatorios"); return;
            }
            dao.insertar(a); limpiar(); cargar();
        } catch (Exception e) { msg(e); }
    }

    private void actualizar() {
        if (idSel == null) { JOptionPane.showMessageDialog(this, "Seleccione un registro"); return; }
        try {
            AsignacionActivo a = leerForm();
            a.setIdAsignacionActivo(idSel);
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
        txtFAsig.setText(LocalDate.now().toString());
        txtFDev.setText(""); txtObs.setText("");
        cboEstado.setSelectedItem("Activa");
        tabla.clearSelection();
    }

    private void msg(Exception e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
}
