package co.inventech.view.panels;
import co.inventech.dao.ActivoDAO;
import co.inventech.model.Activo;
import co.inventech.view.components.CrudToolbar; import co.inventech.view.components.Tables;
import co.inventech.view.dialogs.ActivoDialog;
import javax.swing.*; import javax.swing.table.DefaultTableModel;
import java.awt.*; import java.sql.SQLException;

public class ActivosPanel extends JPanel {
    private final ActivoDAO dao = new ActivoDAO();
    private final DefaultTableModel model = Tables.readOnlyModel("ID","Código","Serie","Tipo","Marca","Modelo","Área","Estado","Proveedor");
    private final JTable table = new JTable(model);
    private final CrudToolbar tb = new CrudToolbar("Buscar por código, serie o modelo...");
    public ActivosPanel() {
        setOpaque(false); setLayout(new BorderLayout());
        add(Tables.pageHeader("Activos tecnológicos","Inventario completo de hardware y dispositivos asignados a la organización."), BorderLayout.NORTH);
        JPanel c = new JPanel(new BorderLayout()); c.setOpaque(false);
        c.add(tb, BorderLayout.NORTH); c.add(Tables.wrap(table), BorderLayout.CENTER);
        add(c, BorderLayout.CENTER);
        tb.btnRefrescar.addActionListener(e -> cargar()); tb.search.addActionListener(e -> cargar());
        tb.btnNuevo.addActionListener(e -> { new ActivoDialog(SwingUtilities.getWindowAncestor(this), null); cargar(); });
        tb.btnEditar.addActionListener(e -> { Activo a = sel(); if (a!=null){ new ActivoDialog(SwingUtilities.getWindowAncestor(this), a); cargar(); } });
        tb.btnEliminar.addActionListener(e -> { Activo a = sel(); if (a==null) return;
            if (JOptionPane.showConfirmDialog(this,"¿Eliminar activo "+a.getCodigoInterno()+"?","Confirmar",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
                try { dao.eliminar(a.getIdActivo()); cargar(); } catch (SQLException ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); }
        });
        cargar();
    }
    private void cargar() {
        try { model.setRowCount(0);
            for (Activo a : dao.listar(tb.search.getText()))
                model.addRow(new Object[]{a.getIdActivo(),a.getCodigoInterno(),a.getNumeroSerie(),a.getTipoNombre(),a.getMarcaNombre(),a.getModelo(),a.getAreaNombre(),a.getEstadoNombre(),a.getProveedorNombre()});
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); }
    }
    private Activo sel() {
        int r = table.getSelectedRow(); if (r<0) { JOptionPane.showMessageDialog(this,"Selecciona una fila."); return null; }
        try { int id=(int)model.getValueAt(r,0);
            return dao.listar("").stream().filter(x->x.getIdActivo()==id).findFirst().orElse(null);
        } catch (SQLException ex) { return null; }
    }
}
