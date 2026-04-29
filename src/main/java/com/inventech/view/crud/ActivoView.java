package com.inventech.view.crud;

import com.inventech.dao.*;
import com.inventech.model.*;
import com.inventech.util.Tema;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class ActivoView extends JPanel {

    private final ActivoDAO dao = new ActivoDAO();
    private final MarcaDAO marcaDao = new MarcaDAO();
    private final ProveedorDAO provDao = new ProveedorDAO();
    private final AreaDAO areaDao = new AreaDAO();
    private final CatalogoDAO tipoDao = new CatalogoDAO("tipo_activo", "id_tipo_activo", "nombre_tipo", "descripcion");
    private final CatalogoDAO estadoDao = new CatalogoDAO("estado_activo", "id_estado_activo", "nombre_estado", "descripcion");

    private final JTextField txtModelo = new JTextField(20);
    private final JTextField txtSerie = new JTextField(20);
    private final JTextField txtCodigo = new JTextField(20);
    private final JTextField txtFAdq = new JTextField(20);   // yyyy-MM-dd
    private final JTextField txtFGar = new JTextField(20);
    private final JTextField txtObs = new JTextField(20);
    private final JComboBox<Catalogo> cmbTipo = new JComboBox<>();
    private final JComboBox<Marca> cmbMarca = new JComboBox<>();
    private final JComboBox<Proveedor> cmbProveedor = new JComboBox<>();
    private final JComboBox<Catalogo> cmbEstado = new JComboBox<>();
    private final JComboBox<Area> cmbArea = new JComboBox<>();

    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID","Código","Modelo","Serie","Tipo","Marca","Proveedor","Estado","Área","F.Adq","F.Garantía"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabla = new JTable(modelo);
    private Integer idSel;

    public ActivoView() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Tema.FONDO);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Gestión de activos");
        titulo.setFont(Tema.FUENTE_TITULO); titulo.setForeground(Tema.VERDE);
        add(titulo, BorderLayout.NORTH);

        add(new JScrollPane(panelForm()), BorderLayout.WEST);
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
                {"Tipo de activo", cmbTipo},
                {"Marca", cmbMarca},
                {"Proveedor", cmbProveedor},
                {"Estado", cmbEstado},
                {"Área", cmbArea},
                {"Modelo", txtModelo},
                {"N° serie", txtSerie},
                {"Código interno", txtCodigo},
                {"F. adquisición (YYYY-MM-DD)", txtFAdq},
                {"F. garantía (YYYY-MM-DD)", txtFGar},
                {"Observaciones", txtObs}
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
            cmbTipo.removeAllItems();
            for (Catalogo c : tipoDao.listar()) cmbTipo.addItem(c);
            cmbEstado.removeAllItems();
            for (Catalogo c : estadoDao.listar()) cmbEstado.addItem(c);
            cmbMarca.removeAllItems();
            for (Marca m : marcaDao.listar()) cmbMarca.addItem(m);
            cmbProveedor.removeAllItems();
            for (Proveedor p : provDao.listar()) cmbProveedor.addItem(p);
            cmbArea.removeAllItems();
            for (Area a : areaDao.listar()) cmbArea.addItem(a);
        } catch (Exception e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
    }

    private void cargar() {
        modelo.setRowCount(0);
        try {
            List<Activo> ls = dao.listar();
            for (Activo a : ls) modelo.addRow(new Object[]{
                    a.getIdActivo(), a.getCodigoInterno(), a.getModelo(), a.getNumeroSerie(),
                    a.getNombreTipo(), a.getNombreMarca(), a.getNombreProveedor(),
                    a.getNombreEstado(), a.getNombreArea(),
                    a.getFechaAdquisicion(), a.getFechaGarantia()});
        } catch (Exception e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
    }

    private void seleccionar() {
        int r = tabla.getSelectedRow(); if (r < 0) return;
        idSel = (Integer) modelo.getValueAt(r, 0);
        txtCodigo.setText(s(modelo.getValueAt(r,1)));
        txtModelo.setText(s(modelo.getValueAt(r,2)));
        txtSerie.setText(s(modelo.getValueAt(r,3)));
        seleccionarPorTexto(cmbTipo, s(modelo.getValueAt(r,4)));
        seleccionarPorTexto(cmbMarca, s(modelo.getValueAt(r,5)));
        seleccionarPorTexto(cmbProveedor, s(modelo.getValueAt(r,6)));
        seleccionarPorTexto(cmbEstado, s(modelo.getValueAt(r,7)));
        seleccionarPorTexto(cmbArea, s(modelo.getValueAt(r,8)));
        txtFAdq.setText(s(modelo.getValueAt(r,9)));
        txtFGar.setText(s(modelo.getValueAt(r,10)));
    }
    private void seleccionarPorTexto(JComboBox<?> cb, String txt) {
        for (int i = 0; i < cb.getItemCount(); i++)
            if (cb.getItemAt(i) != null && cb.getItemAt(i).toString().equals(txt)) { cb.setSelectedIndex(i); return; }
    }
    private String s(Object o) { return o == null ? "" : o.toString(); }
    private LocalDate parseFecha(String s) { return s == null || s.isBlank() ? null : LocalDate.parse(s.trim()); }

    private Activo leer() {
        Activo a = new Activo();
        if (idSel != null) a.setIdActivo(idSel);
        Catalogo tipo = (Catalogo) cmbTipo.getSelectedItem();
        Catalogo est  = (Catalogo) cmbEstado.getSelectedItem();
        Marca m = (Marca) cmbMarca.getSelectedItem();
        Proveedor p = (Proveedor) cmbProveedor.getSelectedItem();
        Area ar = (Area) cmbArea.getSelectedItem();
        if (tipo != null) a.setIdTipoActivo(tipo.getId());
        if (est != null) a.setIdEstadoActivo(est.getId());
        if (m != null) a.setIdMarca(m.getIdMarca());
        if (p != null) a.setIdProveedor(p.getIdProveedor());
        if (ar != null) a.setIdArea(ar.getIdArea());
        a.setModelo(txtModelo.getText());
        a.setNumeroSerie(txtSerie.getText());
        a.setCodigoInterno(txtCodigo.getText());
        a.setFechaAdquisicion(parseFecha(txtFAdq.getText()));
        a.setFechaGarantia(parseFecha(txtFGar.getText()));
        a.setObservaciones(txtObs.getText());
        return a;
    }
    private void guardar() {
        try { dao.insertar(leer()); limpiar(); cargar(); }
        catch (Exception e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
    }
    private void actualizar() {
        if (idSel == null) { JOptionPane.showMessageDialog(this, "Seleccione un registro"); return; }
        try { dao.actualizar(leer()); limpiar(); cargar(); }
        catch (Exception e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
    }
    private void eliminar() {
        if (idSel == null) return;
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar activo?") != JOptionPane.YES_OPTION) return;
        try { dao.eliminar(idSel); limpiar(); cargar(); }
        catch (Exception e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
    }
    private void limpiar() {
        idSel = null;
        for (JTextField t : new JTextField[]{txtModelo,txtSerie,txtCodigo,txtFAdq,txtFGar,txtObs}) t.setText("");
        tabla.clearSelection();
    }
}
