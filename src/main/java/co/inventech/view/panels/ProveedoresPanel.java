package co.inventech.view.panels;
import co.inventech.dao.ProveedorDAO;
import co.inventech.model.Proveedor;
import co.inventech.view.components.CrudToolbar; import co.inventech.view.components.Tables;
import co.inventech.view.dialogs.ProveedorDialog;
import javax.swing.*; import javax.swing.table.DefaultTableModel;
import java.awt.*; import java.sql.SQLException;

public class ProveedoresPanel extends JPanel {
    private final ProveedorDAO dao = new ProveedorDAO();
    private final DefaultTableModel model = Tables.readOnlyModel("ID","Nombre","Contacto","Teléfono","Correo","Estado");
    private final JTable table = new JTable(model);
    private final CrudToolbar tb = new CrudToolbar("Buscar proveedor...");
    public ProveedoresPanel() {
        setOpaque(false); setLayout(new BorderLayout());
        add(Tables.pageHeader("Proveedores","Gestiona los proveedores de hardware, software y servicios."), BorderLayout.NORTH);
        JPanel c = new JPanel(new BorderLayout()); c.setOpaque(false);
        c.add(tb, BorderLayout.NORTH); c.add(Tables.wrap(table), BorderLayout.CENTER);
        add(c, BorderLayout.CENTER);
        tb.btnRefrescar.addActionListener(e -> cargar());
        tb.search.addActionListener(e -> cargar());
        tb.btnNuevo.addActionListener(e -> { new ProveedorDialog(SwingUtilities.getWindowAncestor(this), null); cargar(); });
        tb.btnEditar.addActionListener(e -> { Proveedor p = sel(); if (p!=null){ new ProveedorDialog(SwingUtilities.getWindowAncestor(this), p); cargar(); } });
        tb.btnEliminar.addActionListener(e -> { Proveedor p = sel(); if (p==null) return;
            if (JOptionPane.showConfirmDialog(this,"¿Eliminar?","Confirmar",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
                try { dao.eliminar(p.getIdProveedor()); cargar(); } catch (SQLException ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); }
        });
        cargar();
    }
    private void cargar() {
        try { model.setRowCount(0);
            for (Proveedor p : dao.listar(tb.search.getText()))
                model.addRow(new Object[]{p.getIdProveedor(),p.getNombreProveedor(),p.getContacto(),p.getTelefono(),p.getCorreo(),p.getEstadoProveedor()});
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); }
    }
    private Proveedor sel() {
        int r = table.getSelectedRow(); if (r<0) { JOptionPane.showMessageDialog(this,"Selecciona una fila."); return null; }
        try { int id=(int)model.getValueAt(r,0);
            return dao.listar("").stream().filter(x->x.getIdProveedor()==id).findFirst().orElse(null);
        } catch (SQLException ex) { return null; }
    }
}
