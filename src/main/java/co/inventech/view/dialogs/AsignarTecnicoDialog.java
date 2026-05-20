package co.inventech.view.dialogs;
import co.inventech.dao.TicketDAO;
import co.inventech.dao.UsuarioDAO;
import co.inventech.model.Usuario;
import co.inventech.view.components.FormDialog;
import javax.swing.*; import java.awt.*; import java.sql.SQLException;

public class AsignarTecnicoDialog extends FormDialog {
    private final int idTicket; private final TicketDAO dao = new TicketDAO();
    public AsignarTecnicoDialog(Window owner, int idTicket) {
        super(owner, "Asignar técnico al ticket #"+idTicket);
        this.idTicket = idTicket;
        try {
            JComboBox<Usuario> cb = new JComboBox<>(new UsuarioDAO().listarTecnicos().toArray(new Usuario[0]));
            addField("tec","Técnico", cb);
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); }
        showCentered(420, 200);
    }
    @Override protected boolean onSave() {
        try {
            Usuario u = (Usuario)((JComboBox<?>)fields.get("tec")).getSelectedItem();
            if (u == null) { JOptionPane.showMessageDialog(this,"Selecciona un técnico."); return false; }
            dao.asignarTecnico(idTicket, u.getIdUsuario());
            return true;
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); return false; }
    }
}
