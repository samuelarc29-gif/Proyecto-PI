package com.inventech.view.crud;

import com.inventech.dao.ProveedorDAO;
import com.inventech.model.Proveedor;
import com.inventech.util.Tema;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProveedorView extends JPanel {

    private final ProveedorDAO dao = new ProveedorDAO();
    private final JTextField txtNombre = new JTextField(20);
    private final JTextField txtContacto = new JTextField(20);
    private final JTextField txtTelefono = new JTextField(20);
    private final JTextField txtCorreo = new JTextField(20);
    private final JTextField txtDireccion = new JTextField(20);
    private final JTextField txtSoporte = new JTextField(20);
    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Nombre", "Contacto", "Teléfono", "Correo", "Dirección", "Soporte"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabla = new JTable(modelo);
    private Integer idSel;

    public ProveedorView() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Tema.FONDO);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Gestión de proveedores");
        titulo.setFont(Tema.FUENTE_TITULO); titulo.setForeground(Tema.VERDE);
        add(titulo, BorderLayout.NORTH);

        add(panelForm(), BorderLayout.WEST);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        tabla.getSelectionModel().addListSelectionListener(e -> seleccionar());
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
        String[] labs = {"Nombre", "Contacto", "Teléfono", "Correo", "Dirección", "Soporte"};
        JTextField[] tfs = {txtNombre, txtContacto, txtTelefono, txtCorreo, txtDireccion, txtSoporte};
        for (int i = 0; i < labs.length; i++) {
            g.gridy = y++; JLabel l = new JLabel(labs[i]); l.setForeground(Tema.TEXTO_MUTED); p.add(l, g);
            g.gridy = y++; p.add(tfs[i], g);
        }
        JButton g1 = btn("Guardar", Tema.VERDE, this::guardar);
        JButton g2 = btn("Actualizar", Tema.AZUL, this::actualizar);
        JButton g3 = btn("Eliminar", Tema.PELIGRO, this::eliminar);
        JButton g4 = btn("Limpiar", new Color(100, 116, 139), this::limpiar);
        g.gridy = y++; p.add(g1, g);
        g.gridy = y++; p.add(g2, g);
        g.gridy = y++; p.add(g3, g);
        g.gridy = y++; p.add(g4, g);
        return p;
    }
    private JButton btn(String t, Color c, Runnable r) {
        JButton b = new JButton(t); b.setBackground(c); b.setForeground(Color.WHITE);
        b.setFocusPainted(false); b.addActionListener(e -> r.run()); return b;
    }

    private void cargar() {
        modelo.setRowCount(0);
        try {
            List<Proveedor> ls = dao.listar();
            for (Proveedor p : ls) modelo.addRow(new Object[]{
                    p.getIdProveedor(), p.getNombreProveedor(), p.getContacto(),
                    p.getTelefono(), p.getCorreo(), p.getDireccion(), p.getDescripcionSoporte()});
        } catch (Exception e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
    }
    private void seleccionar() {
        int r = tabla.getSelectedRow(); if (r < 0) return;
        idSel = (Integer) modelo.getValueAt(r, 0);
        txtNombre.setText(s(modelo.getValueAt(r,1))); txtContacto.setText(s(modelo.getValueAt(r,2)));
        txtTelefono.setText(s(modelo.getValueAt(r,3))); txtCorreo.setText(s(modelo.getValueAt(r,4)));
        txtDireccion.setText(s(modelo.getValueAt(r,5))); txtSoporte.setText(s(modelo.getValueAt(r,6)));
    }
    private String s(Object o) { return o == null ? "" : o.toString(); }

    private Proveedor leer() {
        Proveedor p = new Proveedor();
        p.setNombreProveedor(txtNombre.getText()); p.setContacto(txtContacto.getText());
        p.setTelefono(txtTelefono.getText()); p.setCorreo(txtCorreo.getText());
        p.setDireccion(txtDireccion.getText()); p.setDescripcionSoporte(txtSoporte.getText());
        if (idSel != null) p.setIdProveedor(idSel);
        return p;
    }
    private void guardar() {
        try { if (txtNombre.getText().isBlank()) { JOptionPane.showMessageDialog(this, "Nombre obligatorio"); return; }
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
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar proveedor?") != JOptionPane.YES_OPTION) return;
        try { dao.eliminar(idSel); limpiar(); cargar(); }
        catch (Exception e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
    }
    private void limpiar() {
        idSel = null;
        for (JTextField t : new JTextField[]{txtNombre,txtContacto,txtTelefono,txtCorreo,txtDireccion,txtSoporte}) t.setText("");
        tabla.clearSelection();
    }
}
