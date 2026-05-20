package co.inventech.view.panels;
import co.inventech.dao.MantenimientoDAO; import co.inventech.model.Mantenimiento;
import co.inventech.view.components.CrudToolbar; import co.inventech.view.components.Tables;
import co.inventech.view.dialogs.MantenimientoDialog;
import javax.swing.*; import javax.swing.table.DefaultTableModel;
import java.awt.*; import java.sql.SQLException;

public class MantenimientosPanel extends JPanel {
    private final MantenimientoDAO dao = new MantenimientoDAO();
    private final DefaultTableModel model = Tables.readOnlyModel("ID","Activo","Tipo","Programada","Prioridad","Estado","Descripción");
    private final JTable table = new JTable(model);
    private final CrudToolbar tb = new CrudToolbar("Buscar mantenimiento...");
    public MantenimientosPanel() {
        setOpaque(false); setLayout(new BorderLayout());
        add(Tables.pageHeader("Mantenimientos","Programación de mantenimientos preventivos y correctivos."), BorderLayout.NORTH);
        JPanel c=new JPanel(new BorderLayout()); c.setOpaque(false);
        c.add(tb, BorderLayout.NORTH); c.add(Tables.wrap(table), BorderLayout.CENTER); add(c, BorderLayout.CENTER);
        tb.btnRefrescar.addActionListener(e -> cargar()); tb.search.addActionListener(e -> cargar());
        tb.btnNuevo.addActionListener(e -> { new MantenimientoDialog(SwingUtilities.getWindowAncestor(this), null); cargar(); });
        tb.btnEditar.addActionListener(e -> { Mantenimiento m=sel(); if (m!=null){ new MantenimientoDialog(SwingUtilities.getWindowAncestor(this), m); cargar(); } });
        tb.btnEliminar.addActionListener(e -> { Mantenimiento m=sel(); if (m==null) return;
            if (JOptionPane.showConfirmDialog(this,"¿Eliminar?","Confirmar",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
                try { dao.eliminar(m.getIdMantenimiento()); cargar(); } catch (SQLException ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); }
        });
        cargar();
    }
    private void cargar() {
        try { model.setRowCount(0);
            for (Mantenimiento m : dao.listar(tb.search.getText()))
                model.addRow(new Object[]{m.getIdMantenimiento(), m.getActivoCodigo(), m.getTipoNombre(), m.getFechaProgramada(), m.getPrioridad(), m.getEstadoMantenimiento(), m.getDescripcion()});
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); }
    }
    private Mantenimiento sel() { int r=table.getSelectedRow(); if(r<0){JOptionPane.showMessageDialog(this,"Selecciona una fila."); return null;}
        try { int id=(int)model.getValueAt(r,0); return dao.listar("").stream().filter(x->x.getIdMantenimiento()==id).findFirst().orElse(null);} catch (SQLException ex) { return null; }
    }
}
