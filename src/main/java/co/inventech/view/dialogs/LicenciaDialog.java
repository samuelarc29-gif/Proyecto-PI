package co.inventech.view.dialogs;

import co.inventech.dao.*;
import co.inventech.model.*;
import co.inventech.view.components.FormDialog;
import javax.swing.*; import java.awt.*; import java.sql.SQLException; import java.time.LocalDate;

public class LicenciaDialog extends FormDialog {
    private final LicenciaSoftware l; private final boolean nuevo; private final LicenciaDAO dao = new LicenciaDAO();
    public LicenciaDialog(Window owner, LicenciaSoftware base) {
        super(owner, base==null?"Nueva licencia":"Editar licencia");
        nuevo = base==null; l = base==null?new LicenciaSoftware():base;
        try {
            ActivoDAO ad = new ActivoDAO();
            JComboBox<Activo> activo = new JComboBox<>(); activo.addItem(null);
            for (Activo a : ad.listarTodos()) activo.addItem(a);
            for (int i = 0; i < activo.getItemCount(); i++) {
                Activo a = activo.getItemAt(i);
                if (a!=null && l.getIdActivo()!=null && a.getIdActivo()==l.getIdActivo()) activo.setSelectedIndex(i);
            }
            JComboBox<String> est = new JComboBox<>(new String[]{"Activa","Vencida","Suspendida"});
            est.setSelectedItem(l.getEstadoLicencia()==null?"Activa":l.getEstadoLicencia());
            addField("soft","Software", new JTextField(l.getNombreSoftware()));
            addField("tipo","Tipo de licencia", new JTextField(l.getTipoLicencia()));
            addField("cod","Código licencia", new JTextField(l.getCodigoLicencia()));
            addField("adq","Fecha adquisición (YYYY-MM-DD)", new JTextField(l.getFechaAdquisicion()==null?"":l.getFechaAdquisicion().toString()));
            addField("ven","Fecha vencimiento (YYYY-MM-DD)", new JTextField(l.getFechaVencimiento()==null?"":l.getFechaVencimiento().toString()));
            addField("activo","Activo asociado (opcional)", activo);
            addField("est","Estado", est);
            JTextArea obs = new JTextArea(l.getObservaciones(), 3, 30); obs.setLineWrap(true);
            addField("obs","Observaciones", new JScrollPane(obs));
            fields.put("obsTA", obs);
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); }
        showCentered(500, 560);
    }
    @Override protected boolean onSave() {
        try {
            if (text("soft").isEmpty() || text("cod").isEmpty()) { JOptionPane.showMessageDialog(this,"Software y código obligatorios."); return false; }
            l.setNombreSoftware(text("soft")); l.setTipoLicencia(text("tipo")); l.setCodigoLicencia(text("cod"));
            l.setFechaAdquisicion(text("adq").isEmpty()?null:LocalDate.parse(text("adq")));
            l.setFechaVencimiento(text("ven").isEmpty()?null:LocalDate.parse(text("ven")));
            Activo a = (Activo)((JComboBox<?>)fields.get("activo")).getSelectedItem();
            l.setIdActivo(a==null?null:a.getIdActivo());
            l.setEstadoLicencia(text("est"));
            l.setObservaciones(((JTextArea)fields.get("obsTA")).getText().trim());
            if (nuevo) dao.insertar(l); else dao.actualizar(l);
            return true;
        } catch (Exception ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); return false; }
    }
}
