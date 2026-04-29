package com.inventech.view.crud;

import com.inventech.dao.AnsDAO;
import com.inventech.model.Ans;
import com.inventech.util.Tema;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import static com.inventech.view.crud.FormUtils.*;

public class AnsView extends JPanel {

    private final AnsDAO dao = new AnsDAO();

    private final JTextField txtTipo = new JTextField(20);
    private final JComboBox<String> cboPrioridad = new JComboBox<>(new String[]{"Alta", "Media", "Baja"});
    private final JTextField txtResp = new JTextField(6);
    private final JTextField txtSol = new JTextField(6);
    private final JTextField txtDesc = new JTextField(25);

    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Tipo solicitud", "Prioridad", "Resp.(h)", "Sol.(h)", "Descripción"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabla = new JTable(modelo);
    private Integer idSel;

    public AnsView() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Tema.FONDO);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel t = new JLabel("Acuerdos de Nivel de Servicio (ANS)");
        t.setFont(Tema.FUENTE_TITULO); t.setForeground(Tema.VERDE);
        add(t, BorderLayout.NORTH);
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
        g.gridx = 0; g.fill = GridBagConstraints.HORIZONTAL; g.insets = new Insets(5, 5, 5, 5);
        int y = 0;
        g.gridy = y++; p.add(label("Tipo de solicitud"), g);
        g.gridy = y++; p.add(txtTipo, g);
        g.gridy = y++; p.add(label("Prioridad"), g);
        g.gridy = y++; p.add(cboPrioridad, g);
        g.gridy = y++; p.add(label("Tiempo respuesta (horas)"), g);
        g.gridy = y++; p.add(txtResp, g);
        g.gridy = y++; p.add(label("Tiempo solución (horas)"), g);
        g.gridy = y++; p.add(txtSol, g);
        g.gridy = y++; p.add(label("Descripción"), g);
        g.gridy = y++; p.add(txtDesc, g);

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

    private void cargar() {
        modelo.setRowCount(0);
        try {
            List<Ans> lista = dao.listar();
            for (Ans a : lista) modelo.addRow(new Object[]{
                    a.getIdAns(), a.getTipoSolicitud(), a.getPrioridad(),
                    a.getTiempoRespuestaHoras(), a.getTiempoSolucionHoras(), a.getDescripcion()
            });
        } catch (Exception e) { msg(e); }
    }

    private void seleccionar() {
        int r = tabla.getSelectedRow();
        if (r < 0) return;
        idSel = (Integer) modelo.getValueAt(r, 0);
        txtTipo.setText(String.valueOf(modelo.getValueAt(r, 1)));
        cboPrioridad.setSelectedItem(modelo.getValueAt(r, 2));
        txtResp.setText(String.valueOf(modelo.getValueAt(r, 3)));
        txtSol.setText(String.valueOf(modelo.getValueAt(r, 4)));
        Object d = modelo.getValueAt(r, 5);
        txtDesc.setText(d == null ? "" : d.toString());
    }

    private Ans leerForm() {
        Ans a = new Ans();
        a.setTipoSolicitud(txtTipo.getText());
        a.setPrioridad((String) cboPrioridad.getSelectedItem());
        try { a.setTiempoRespuestaHoras(Integer.parseInt(txtResp.getText().trim())); } catch (Exception e) { a.setTiempoRespuestaHoras(0); }
        try { a.setTiempoSolucionHoras(Integer.parseInt(txtSol.getText().trim())); } catch (Exception e) { a.setTiempoSolucionHoras(0); }
        a.setDescripcion(txtDesc.getText());
        return a;
    }

    private void guardar() {
        try {
            Ans a = leerForm();
            if (a.getTipoSolicitud().isBlank()) { JOptionPane.showMessageDialog(this, "Tipo de solicitud obligatorio"); return; }
            dao.insertar(a); limpiar(); cargar();
        } catch (Exception e) { msg(e); }
    }

    private void actualizar() {
        if (idSel == null) { JOptionPane.showMessageDialog(this, "Seleccione un registro"); return; }
        try { Ans a = leerForm(); a.setIdAns(idSel); dao.actualizar(a); limpiar(); cargar(); }
        catch (Exception e) { msg(e); }
    }

    private void eliminar() {
        if (idSel == null) return;
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar ANS?") != JOptionPane.YES_OPTION) return;
        try { dao.eliminar(idSel); limpiar(); cargar(); } catch (Exception e) { msg(e); }
    }

    private void limpiar() {
        idSel = null;
        txtTipo.setText(""); txtResp.setText(""); txtSol.setText(""); txtDesc.setText("");
        cboPrioridad.setSelectedIndex(1);
        tabla.clearSelection();
    }

    private void msg(Exception e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
}
