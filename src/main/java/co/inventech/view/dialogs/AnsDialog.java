package co.inventech.view.dialogs;
import co.inventech.dao.AnsDAO;
import co.inventech.model.Ans;
import co.inventech.view.components.FormDialog;
import javax.swing.*; import java.awt.*; import java.sql.SQLException;

public class AnsDialog extends FormDialog {
    private final Ans a; private final boolean nuevo; private final AnsDAO dao = new AnsDAO();
    public AnsDialog(Window owner, Ans base) {
        super(owner, base==null?"Nuevo ANS":"Editar ANS");
        nuevo = base==null; a = base==null?new Ans():base;
        addField("tipo","Tipo de solicitud", new JTextField(a.getTipoSolicitud()));
        JComboBox<String> p = new JComboBox<>(new String[]{"Baja","Media","Alta","Crítica"});
        if (a.getPrioridad()!=null) p.setSelectedItem(a.getPrioridad());
        addField("prio","Prioridad", p);
        addField("resp","Tiempo respuesta (horas)", new JTextField(a.getTiempoRespuestaHoras()>0?String.valueOf(a.getTiempoRespuestaHoras()):""));
        addField("sol","Tiempo solución (horas)", new JTextField(a.getTiempoSolucionHoras()>0?String.valueOf(a.getTiempoSolucionHoras()):""));
        JComboBox<String> est = new JComboBox<>(new String[]{"Activo","Inactivo"});
        est.setSelectedItem(a.getEstadoAns()==null?"Activo":a.getEstadoAns());
        addField("est","Estado", est);
        showCentered(460, 400);
    }
    @Override protected boolean onSave() {
        try {
            a.setTipoSolicitud(text("tipo")); a.setPrioridad(text("prio"));
            a.setTiempoRespuestaHoras(Integer.parseInt(text("resp")));
            a.setTiempoSolucionHoras(Integer.parseInt(text("sol")));
            a.setEstadoAns(text("est"));
            if (nuevo) dao.insertar(a); else dao.actualizar(a);
            return true;
        } catch (Exception ex) { JOptionPane.showMessageDialog(this,"Datos inválidos: "+ex.getMessage()); return false; }
    }
}
