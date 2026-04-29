package com.inventech.view.crud;

import com.inventech.config.ConexionBD;
import com.inventech.dao.LicenciaSoftwareDAO;
import com.inventech.model.LicenciaSoftware;
import com.inventech.util.Tema;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static com.inventech.view.crud.FormUtils.*;

public class LicenciaSoftwareView extends JPanel {

    private final LicenciaSoftwareDAO dao = new LicenciaSoftwareDAO();

    private final JComboBox<Item> cboActivo = new JComboBox<>();
    private final JTextField txtSoftware = new JTextField(20);
    private final JTextField txtTipo = new JTextField(20);
    private final JTextField txtCodigo = new JTextField(20);
    private final JTextField txtFAdq = new JTextField(12);
    private final JTextField txtFVen = new JTextField(12);
    private final JComboBox<String> cboEstado = new JComboBox<>(new String[]{"Activa", "Vencida", "Cancelada"});
    private final JTextField txtObs = new JTextField(20);

    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Activo", "Software", "Tipo", "Código", "Adquisición", "Vencimiento", "Estado"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabla = new JTable(modelo);
    private Integer idSel;

    public LicenciaSoftwareView() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Tema.FONDO);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel t = new JLabel("Licencias de software");
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
        g.gridy = y++; p.add(label("Activo"), g);
        g.gridy = y++; p.add(cboActivo, g);
        g.gridy = y++; p.add(label("Nombre del software"), g);
        g.gridy = y++; p.add(txtSoftware, g);
        g.gridy = y++; p.add(label("Tipo de licencia"), g);
        g.gridy = y++; p.add(txtTipo, g);
        g.gridy = y++; p.add(label("Código de licencia"), g);
        g.gridy = y++; p.add(txtCodigo, g);
        g.gridy = y++; p.add(label("Fecha adquisición"), g);
        g.gridy = y++; p.add(txtFAdq, g);
        g.gridy = y++; p.add(label("Fecha vencimiento"), g);
        g.gridy = y++; p.add(txtFVen, g);
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
        try (Connection c = ConexionBD.getConnection();
             ResultSet rs = c.prepareStatement(
                     "SELECT id_activo, codigo_interno FROM activo ORDER BY codigo_interno").executeQuery()) {
            while (rs.next()) cboActivo.addItem(new Item(rs.getInt(1), rs.getString(2)));
        } catch (SQLException e) { msg(e); }
    }

    private void cargar() {
        modelo.setRowCount(0);
        try {
            List<LicenciaSoftware> lista = dao.listar();
            for (LicenciaSoftware l : lista) {
                modelo.addRow(new Object[]{
                        l.getIdLicencia(), l.getCodigoActivo(), l.getNombreSoftware(), l.getTipoLicencia(),
                        l.getCodigoLicencia(), l.getFechaAdquisicion(), l.getFechaVencimiento(), l.getEstadoLicencia()
                });
            }
        } catch (Exception e) { msg(e); }
    }

    private void seleccionar() {
        int r = tabla.getSelectedRow();
        if (r < 0) return;
        idSel = (Integer) modelo.getValueAt(r, 0);
        try {
            for (LicenciaSoftware l : dao.listar()) if (l.getIdLicencia() == idSel) {
                seleccionarPorId(cboActivo, l.getIdActivo());
                txtSoftware.setText(n(l.getNombreSoftware()));
                txtTipo.setText(n(l.getTipoLicencia()));
                txtCodigo.setText(n(l.getCodigoLicencia()));
                txtFAdq.setText(l.getFechaAdquisicion() == null ? "" : l.getFechaAdquisicion().toString());
                txtFVen.setText(l.getFechaVencimiento() == null ? "" : l.getFechaVencimiento().toString());
                cboEstado.setSelectedItem(l.getEstadoLicencia());
                txtObs.setText(n(l.getObservaciones()));
                return;
            }
        } catch (Exception e) { msg(e); }
    }

    private String n(String s) { return s == null ? "" : s; }
    private LocalDate parse(String s) {
        if (s == null || s.isBlank()) return null;
        try { return LocalDate.parse(s.trim()); } catch (Exception e) { return null; }
    }

    private LicenciaSoftware leerForm() {
        LicenciaSoftware l = new LicenciaSoftware();
        l.setIdActivo(idDe(cboActivo));
        l.setNombreSoftware(txtSoftware.getText());
        l.setTipoLicencia(txtTipo.getText());
        l.setCodigoLicencia(txtCodigo.getText());
        l.setFechaAdquisicion(parse(txtFAdq.getText()));
        l.setFechaVencimiento(parse(txtFVen.getText()));
        l.setEstadoLicencia((String) cboEstado.getSelectedItem());
        l.setObservaciones(txtObs.getText());
        return l;
    }

    private void guardar() {
        try {
            LicenciaSoftware l = leerForm();
            if (l.getIdActivo() < 0 || l.getNombreSoftware().isBlank()) {
                JOptionPane.showMessageDialog(this, "Activo y nombre del software son obligatorios"); return;
            }
            dao.insertar(l); limpiar(); cargar();
        } catch (Exception e) { msg(e); }
    }

    private void actualizar() {
        if (idSel == null) { JOptionPane.showMessageDialog(this, "Seleccione un registro"); return; }
        try {
            LicenciaSoftware l = leerForm(); l.setIdLicencia(idSel);
            dao.actualizar(l); limpiar(); cargar();
        } catch (Exception e) { msg(e); }
    }

    private void eliminar() {
        if (idSel == null) return;
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar licencia?") != JOptionPane.YES_OPTION) return;
        try { dao.eliminar(idSel); limpiar(); cargar(); } catch (Exception e) { msg(e); }
    }

    private void limpiar() {
        idSel = null;
        txtSoftware.setText(""); txtTipo.setText(""); txtCodigo.setText("");
        txtFAdq.setText(""); txtFVen.setText(""); txtObs.setText("");
        cboEstado.setSelectedIndex(0);
        tabla.clearSelection();
    }

    private void msg(Exception e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
}
