package co.inventech.view.panels;

import co.inventech.dao.UsuarioDAO;
import co.inventech.model.Usuario;
import co.inventech.view.components.CrudToolbar;
import co.inventech.view.components.Tables;
import co.inventech.view.dialogs.UsuarioDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class UsuariosPanel extends JPanel {
    private final UsuarioDAO dao = new UsuarioDAO();
    private final DefaultTableModel model = Tables.readOnlyModel("ID","Usuario","Nombre","Documento","Rol","Área","Correo","Estado");
    private final JTable table = new JTable(model);
    private final CrudToolbar tb = new CrudToolbar("Buscar por nombre, usuario o documento...");

    public UsuariosPanel() {
        setOpaque(false); setLayout(new BorderLayout());
        add(Tables.pageHeader("Usuarios y roles", "Administra cuentas de usuario, roles y permisos del sistema."), BorderLayout.NORTH);
        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(tb, BorderLayout.NORTH);
        center.add(Tables.wrap(table), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        tb.btnRefrescar.addActionListener(e -> cargar());
        tb.search.addActionListener(e -> cargar());
        tb.btnNuevo.addActionListener(e -> { new UsuarioDialog(SwingUtilities.getWindowAncestor(this), null); cargar(); });
        tb.btnEditar.addActionListener(e -> editar());
        tb.btnEliminar.addActionListener(e -> eliminar());
        cargar();
    }
    private void cargar() {
        try {
            model.setRowCount(0);
            List<Usuario> ls = dao.listar(tb.search.getText());
            for (Usuario u : ls) model.addRow(new Object[]{
                u.getIdUsuario(), u.getNombreUsuario(), u.getNombreCompleto(),
                u.getDocumento(), u.getRolNombre(), u.getAreaNombre(), u.getCorreo(), u.getEstadoUsuario()
            });
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); }
    }
    private Usuario seleccionado() {
        int r = table.getSelectedRow();
        if (r < 0) { JOptionPane.showMessageDialog(this,"Selecciona una fila."); return null; }
        try {
            int id = (int) model.getValueAt(r, 0);
            return dao.listar("").stream().filter(u -> u.getIdUsuario()==id).findFirst().orElse(null);
        } catch (SQLException ex) { return null; }
    }
    private void editar() { Usuario u = seleccionado(); if (u!=null) { new UsuarioDialog(SwingUtilities.getWindowAncestor(this), u); cargar(); } }
    private void eliminar() {
        Usuario u = seleccionado(); if (u==null) return;
        if (JOptionPane.showConfirmDialog(this,"¿Eliminar usuario "+u.getNombreCompleto()+"?","Confirmar",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
            try { dao.eliminar(u.getIdUsuario()); cargar(); } catch (SQLException ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); }
        }
    }
}
