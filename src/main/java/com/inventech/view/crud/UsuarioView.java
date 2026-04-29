package com.inventech.view.crud;

import com.inventech.dao.AreaDAO;
import com.inventech.dao.RolDAO;
import com.inventech.dao.UsuarioDAO;
import com.inventech.model.Area;
import com.inventech.model.Rol;
import com.inventech.model.Usuario;
import com.inventech.util.Tema;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UsuarioView extends JPanel {

    private final UsuarioDAO dao = new UsuarioDAO();
    private final RolDAO rolDao = new RolDAO();
    private final AreaDAO areaDao = new AreaDAO();

    private final JTextField txtNombre = new JTextField(20);
    private final JTextField txtDocumento = new JTextField(20);
    private final JTextField txtCargo = new JTextField(20);
    private final JTextField txtCorreo = new JTextField(20);
    private final JTextField txtTelefono = new JTextField(20);
    private final JComboBox<String> cmbEstado = new JComboBox<>(new String[]{"Activo", "Inactivo"});
    private final JComboBox<Rol> cmbRol = new JComboBox<>();
    private final JComboBox<Area> cmbArea = new JComboBox<>();

    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID","Nombre","Documento","Cargo","Correo","Teléfono","Estado","Rol","Área"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabla = new JTable(modelo);
    private Integer idSel;

    public UsuarioView() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Tema.FONDO);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Gestión de usuarios");
        titulo.setFont(Tema.FUENTE_TITULO); titulo.setForeground(Tema.VERDE);
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
        g.insets = new Insets(4, 6, 4, 6);
        int y = 0;
        Object[][] campos = {
                {"Nombre completo", txtNombre},
                {"Documento", txtDocumento},
                {"Cargo", txtCargo},
                {"Correo", txtCorreo},
                {"Teléfono", txtTelefono},
                {"Estado", cmbEstado},
                {"Rol", cmbRol},
                {"Área", cmbArea}
        };
        for (Object[] f : campos) {
            g.gridy = y++; JLabel l = new JLabel((String) f[0]); l.setForeground(Tema.TEXTO_MUTED); p.add(l, g);
            g.gridy = y++; p.add((Component) f[1], g);
        }
        g.gridy = y++; p.add(btn("Guardar", Tema.VERDE, this::guardar), g);
        g.gridy = y++; p.add(btn("Actualizar", Tema.AZUL, this::actualizar), g);
        g.gridy = y++; p.add(btn("Eliminar", Tema.PELIGRO, this::eliminar), g);
        g.gridy = y++; p.add(btn("Limpiar", new Color(100,116,139), this::limpiar), g);
        return p;
    }
    private JButton btn(String t, Color c, Runnable r) {
        JButton b = new JButton(t); b.setBackground(c); b.setForeground(Color.WHITE);
        b.setFocusPainted(false); b.addActionListener(e -> r.run()); return b;
    }

    private void cargarCombos() {
        try {
            cmbRol.removeAllItems();
            for (Rol r : rolDao.listar()) cmbRol.addItem(r);
            cmbArea.removeAllItems();
            for (Area a : areaDao.listar()) cmbArea.addItem(a);
        } catch (Exception e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
    }

    private void cargar() {
        modelo.setRowCount(0);
        try {
            List<Usuario> ls = dao.listar();
            for (Usuario u : ls) modelo.addRow(new Object[]{
                    u.getIdUsuario(), u.getNombreCompleto(), u.getDocumento(), u.getCargo(),
                    u.getCorreo(), u.getTelefono(), u.getEstadoUsuario(),
                    u.getNombreRol(), u.getNombreArea()});
        } catch (Exception e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
    }

    private void seleccionar() {
        int r = tabla.getSelectedRow(); if (r < 0) return;
        idSel = (Integer) modelo.getValueAt(r, 0);
        txtNombre.setText(s(modelo.getValueAt(r,1))); txtDocumento.setText(s(modelo.getValueAt(r,2)));
        txtCargo.setText(s(modelo.getValueAt(r,3))); txtCorreo.setText(s(modelo.getValueAt(r,4)));
        txtTelefono.setText(s(modelo.getValueAt(r,5))); cmbEstado.setSelectedItem(s(modelo.getValueAt(r,6)));
        seleccionarComboPorTexto(cmbRol, s(modelo.getValueAt(r,7)));
        seleccionarComboPorTexto(cmbArea, s(modelo.getValueAt(r,8)));
    }
    private void seleccionarComboPorTexto(JComboBox<?> cb, String txt) {
        for (int i = 0; i < cb.getItemCount(); i++)
            if (cb.getItemAt(i) != null && cb.getItemAt(i).toString().equals(txt)) { cb.setSelectedIndex(i); return; }
    }
    private String s(Object o) { return o == null ? "" : o.toString(); }

    private Usuario leer() {
        Usuario u = new Usuario();
        if (idSel != null) u.setIdUsuario(idSel);
        u.setNombreCompleto(txtNombre.getText());
        u.setDocumento(txtDocumento.getText());
        u.setCargo(txtCargo.getText());
        u.setCorreo(txtCorreo.getText());
        u.setTelefono(txtTelefono.getText());
        u.setEstadoUsuario((String) cmbEstado.getSelectedItem());
        Rol r = (Rol) cmbRol.getSelectedItem();
        Area a = (Area) cmbArea.getSelectedItem();
        if (r != null) u.setIdRol(r.getIdRol());
        if (a != null) u.setIdArea(a.getIdArea());
        return u;
    }
    private void guardar() {
        try {
            if (txtNombre.getText().isBlank() || txtDocumento.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Nombre y documento son obligatorios"); return;
            }
            dao.insertar(leer()); limpiar(); cargar();
        } catch (Exception e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
    }
    private void actualizar() {
        if (idSel == null) { JOptionPane.showMessageDialog(this, "Seleccione un registro"); return; }
        try { dao.actualizar(leer()); limpiar(); cargar(); }
        catch (Exception e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
    }
    private void eliminar() {
        if (idSel == null) return;
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar usuario?") != JOptionPane.YES_OPTION) return;
        try { dao.eliminar(idSel); limpiar(); cargar(); }
        catch (Exception e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
    }
    private void limpiar() {
        idSel = null;
        for (JTextField t : new JTextField[]{txtNombre,txtDocumento,txtCargo,txtCorreo,txtTelefono}) t.setText("");
        cmbEstado.setSelectedIndex(0);
        if (cmbRol.getItemCount() > 0) cmbRol.setSelectedIndex(0);
        if (cmbArea.getItemCount() > 0) cmbArea.setSelectedIndex(0);
        tabla.clearSelection();
    }
}
