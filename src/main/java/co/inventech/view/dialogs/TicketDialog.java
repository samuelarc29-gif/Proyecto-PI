package co.inventech.view.dialogs;

import co.inventech.dao.*;
import co.inventech.model.*;
import co.inventech.util.Sesion;
import co.inventech.view.components.FormDialog;
import javax.swing.*; import java.awt.*; import java.sql.SQLException;

public class TicketDialog extends FormDialog {
    private final TicketDAO dao = new TicketDAO();
    public TicketDialog(Window owner) {
        super(owner, "Nuevo ticket de soporte");
        try {
            ActivoDAO ad = new ActivoDAO();
            CatalogoDAO cd = new CatalogoDAO();
            JComboBox<Activo> activo = new JComboBox<>(ad.listarTodos().toArray(new Activo[0]));
            JComboBox<EstadoTicket> estado = new JComboBox<>(cd.listarEstadosTicket().toArray(new EstadoTicket[0]));
            JComboBox<String> prio = new JComboBox<>(new String[]{"Baja","Media","Alta","Crítica"});
            JTextArea desc = new JTextArea(4, 30); desc.setLineWrap(true); desc.setWrapStyleWord(true);
            addField("activo","Activo afectado", activo);
            addField("desc","Descripción de la falla", new JScrollPane(desc));
            fields.put("descTA", desc);
            addField("prio","Prioridad", prio);
            addField("estado","Estado inicial", estado);
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); }
        showCentered(500, 460);
    }
    @Override protected boolean onSave() {
        try {
            Activo a = (Activo)((JComboBox<?>)fields.get("activo")).getSelectedItem();
            String d = ((JTextArea)fields.get("descTA")).getText().trim();
            if (a == null || d.isEmpty()) { JOptionPane.showMessageDialog(this,"Activo y descripción obligatorios."); return false; }
            Ticket t = new Ticket();
            t.setIdActivo(a.getIdActivo());
            t.setIdUsuarioSolicitante(Sesion.usuario().getIdUsuario());
            t.setIdEstadoTicket(((EstadoTicket)((JComboBox<?>)fields.get("estado")).getSelectedItem()).getId());
            t.setPrioridad(text("prio")); t.setDescripcionFalla(d);
            dao.insertar(t);
            return true;
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); return false; }
    }
}
