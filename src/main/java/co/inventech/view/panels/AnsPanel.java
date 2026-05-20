package co.inventech.view.panels;
import co.inventech.dao.AnsDAO; import co.inventech.model.Ans;
import co.inventech.view.components.CrudToolbar; import co.inventech.view.components.Tables;
import co.inventech.view.dialogs.AnsDialog;
import javax.swing.*; import javax.swing.table.DefaultTableModel;
import java.awt.*; import java.sql.SQLException;

public class AnsPanel extends JPanel {
    private final AnsDAO dao = new AnsDAO();
    private final DefaultTableModel model = Tables.readOnlyModel("ID","Tipo","Prioridad","Respuesta (h)","Solución (h)","Estado");
    private final JTable table = new JTable(model);
    private final CrudToolbar tb = new CrudToolbar("Filtrar...");
    public AnsPanel() {
        setOpaque(false); setLayout(new BorderLayout());
        add(Tables.pageHeader("Acuerdos de Nivel de Servicio (ANS)","Define los tiempos de respuesta y solución por prioridad."), BorderLayout.NORTH);
        JPanel c=new JPanel(new BorderLayout()); c.setOpaque(false);
        c.add(tb, BorderLayout.NORTH); c.add(Tables.wrap(table), BorderLayout.CENTER); add(c, BorderLayout.CENTER);
        tb.btnRefrescar.addActionListener(e -> cargar());
        tb.btnNuevo.addActionListener(e -> { new AnsDialog(SwingUtilities.getWindowAncestor(this), null); cargar(); });
        tb.btnEditar.addActionListener(e -> { Ans a=sel(); if(a!=null){ new AnsDialog(SwingUtilities.getWindowAncestor(this), a); cargar(); } });
        tb.btnEliminar.addActionListener(e -> { Ans a=sel(); if (a==null) return;
            if (JOptionPane.showConfirmDialog(this,"¿Eliminar?","Confirmar",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
                try { dao.eliminar(a.getIdAns()); cargar(); } catch (SQLException ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); }
        });
        cargar();
    }
    private void cargar() {
        try { model.setRowCount(0);
            for (Ans a : dao.listar())
                model.addRow(new Object[]{a.getIdAns(),a.getTipoSolicitud(),a.getPrioridad(),a.getTiempoRespuestaHoras(),a.getTiempoSolucionHoras(),a.getEstadoAns()});
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); }
    }
    private Ans sel() { int r=table.getSelectedRow(); if(r<0){JOptionPane.showMessageDialog(this,"Selecciona una fila."); return null;}
        try { int id=(int)model.getValueAt(r,0); return dao.listar().stream().filter(x->x.getIdAns()==id).findFirst().orElse(null);} catch (SQLException ex) { return null; }
    }
}
