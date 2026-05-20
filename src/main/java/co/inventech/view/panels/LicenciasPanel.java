package co.inventech.view.panels;
import co.inventech.dao.LicenciaDAO; import co.inventech.model.LicenciaSoftware;
import co.inventech.view.components.CrudToolbar; import co.inventech.view.components.Tables;
import co.inventech.view.dialogs.LicenciaDialog;
import javax.swing.*; import javax.swing.table.DefaultTableModel;
import java.awt.*; import java.sql.SQLException;

public class LicenciasPanel extends JPanel {
    private final LicenciaDAO dao = new LicenciaDAO();
    private final DefaultTableModel model = Tables.readOnlyModel("ID","Software","Tipo","Código","Adquisición","Vencimiento","Activo","Estado");
    private final JTable table = new JTable(model);
    private final CrudToolbar tb = new CrudToolbar("Buscar licencia...");
    public LicenciasPanel() {
        setOpaque(false); setLayout(new BorderLayout());
        add(Tables.pageHeader("Licencias de software","Inventario, vencimientos y control de uso de licencias."), BorderLayout.NORTH);
        JPanel c=new JPanel(new BorderLayout()); c.setOpaque(false);
        c.add(tb, BorderLayout.NORTH); c.add(Tables.wrap(table), BorderLayout.CENTER); add(c, BorderLayout.CENTER);
        tb.btnRefrescar.addActionListener(e -> cargar()); tb.search.addActionListener(e -> cargar());
        tb.btnNuevo.addActionListener(e -> { new LicenciaDialog(SwingUtilities.getWindowAncestor(this), null); cargar(); });
        tb.btnEditar.addActionListener(e -> { LicenciaSoftware l=sel(); if(l!=null){ new LicenciaDialog(SwingUtilities.getWindowAncestor(this), l); cargar(); } });
        tb.btnEliminar.addActionListener(e -> { LicenciaSoftware l=sel(); if (l==null) return;
            if (JOptionPane.showConfirmDialog(this,"¿Eliminar?","Confirmar",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
                try { dao.eliminar(l.getIdLicencia()); cargar(); } catch (SQLException ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); }
        });
        cargar();
    }
    private void cargar() {
        try { model.setRowCount(0);
            for (LicenciaSoftware l : dao.listar(tb.search.getText()))
                model.addRow(new Object[]{l.getIdLicencia(),l.getNombreSoftware(),l.getTipoLicencia(),l.getCodigoLicencia(),l.getFechaAdquisicion(),l.getFechaVencimiento(),l.getActivoCodigo(),l.getEstadoLicencia()});
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); }
    }
    private LicenciaSoftware sel() { int r=table.getSelectedRow(); if(r<0){JOptionPane.showMessageDialog(this,"Selecciona una fila."); return null;}
        try { int id=(int)model.getValueAt(r,0); return dao.listar("").stream().filter(x->x.getIdLicencia()==id).findFirst().orElse(null);} catch (SQLException ex) { return null; }
    }
}
