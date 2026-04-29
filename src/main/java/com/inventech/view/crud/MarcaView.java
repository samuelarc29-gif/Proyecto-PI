package com.inventech.view.crud;

import com.inventech.dao.MarcaDAO;
import com.inventech.model.Marca;
import com.inventech.util.Tema;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MarcaView extends JPanel {

    private final MarcaDAO dao = new MarcaDAO();
    private final JTextField txtNombre = new JTextField(20);
    private final JTextField txtDesc = new JTextField(30);
    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Nombre", "Descripción"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabla = new JTable(modelo);
    private Integer idSeleccionado;

    public MarcaView() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Tema.FONDO);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Gestión de marcas");
        titulo.setFont(Tema.FUENTE_TITULO);
        titulo.setForeground(Tema.VERDE);
        add(titulo, BorderLayout.NORTH);

        add(panelFormulario(), BorderLayout.WEST);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        tabla.getSelectionModel().addListSelectionListener(e -> seleccionar());
        cargar();
    }

    private JPanel panelFormulario() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Tema.PANEL);
        p.setBorder(new EmptyBorder(16, 16, 16, 16));
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(6, 6, 6, 6);

        g.gridy = 0; p.add(label("Nombre"), g);
        g.gridy++; p.add(txtNombre, g);
        g.gridy++; p.add(label("Descripción"), g);
        g.gridy++; p.add(txtDesc, g);

        JButton btnGuardar = boton("Guardar", Tema.VERDE);
        JButton btnActualizar = boton("Actualizar", Tema.AZUL);
        JButton btnEliminar = boton("Eliminar", Tema.PELIGRO);
        JButton btnLimpiar = boton("Limpiar", new Color(100, 116, 139));

        btnGuardar.addActionListener(e -> guardar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiar());

        g.gridy++; p.add(btnGuardar, g);
        g.gridy++; p.add(btnActualizar, g);
        g.gridy++; p.add(btnEliminar, g);
        g.gridy++; p.add(btnLimpiar, g);
        return p;
    }

    private JLabel label(String t) { JLabel l = new JLabel(t); l.setForeground(Tema.TEXTO_MUTED); return l; }
    private JButton boton(String t, Color c) {
        JButton b = new JButton(t); b.setBackground(c); b.setForeground(Color.WHITE);
        b.setFocusPainted(false); b.setFont(Tema.FUENTE_SUB); return b;
    }

    private void cargar() {
        modelo.setRowCount(0);
        try {
            List<Marca> lista = dao.listar();
            for (Marca m : lista) modelo.addRow(new Object[]{m.getIdMarca(), m.getNombreMarca(), m.getDescripcion()});
        } catch (Exception e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
    }

    private void seleccionar() {
        int r = tabla.getSelectedRow();
        if (r < 0) return;
        idSeleccionado = (Integer) modelo.getValueAt(r, 0);
        txtNombre.setText(String.valueOf(modelo.getValueAt(r, 1)));
        txtDesc.setText(String.valueOf(modelo.getValueAt(r, 2)));
    }

    private void guardar() {
        try {
            if (txtNombre.getText().isBlank()) { JOptionPane.showMessageDialog(this, "Nombre obligatorio"); return; }
            Marca m = new Marca(0, txtNombre.getText(), txtDesc.getText());
            dao.insertar(m);
            limpiar(); cargar();
        } catch (Exception e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
    }

    private void actualizar() {
        if (idSeleccionado == null) { JOptionPane.showMessageDialog(this, "Seleccione un registro"); return; }
        try {
            Marca m = new Marca(idSeleccionado, txtNombre.getText(), txtDesc.getText());
            dao.actualizar(m);
            limpiar(); cargar();
        } catch (Exception e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
    }

    private void eliminar() {
        if (idSeleccionado == null) return;
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar marca?") != JOptionPane.YES_OPTION) return;
        try { dao.eliminar(idSeleccionado); limpiar(); cargar(); }
        catch (Exception e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
    }

    private void limpiar() {
        idSeleccionado = null;
        txtNombre.setText(""); txtDesc.setText("");
        tabla.clearSelection();
    }
}
