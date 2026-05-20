package co.inventech.view.dialogs;

import co.inventech.dao.*;
import co.inventech.model.*;
import co.inventech.view.components.FormDialog;
import javax.swing.*; import java.awt.*; import java.sql.SQLException; import java.time.LocalDate;

public class MantenimientoDialog extends FormDialog {
    private final Mantenimiento m; private final boolean nuevo; private final MantenimientoDAO dao = new MantenimientoDAO();
    public MantenimientoDialog(Window owner, Mantenimiento base) {
        super(owner, base==null?"Programar mantenimiento":"Editar mantenimiento");
        nuevo = base==null; m = base==null?new Mantenimiento():base;
        try {
            ActivoDAO ad = new ActivoDAO(); CatalogoDAO cd = new CatalogoDAO();
            JComboBox<Activo> activo = new JComboBox<>(ad.listarTodos().toArray(new Activo[0]));
            JComboBox<TipoMantenimiento> tipo = new JComboBox<>(cd.listarTiposMantenimiento().toArray(new TipoMantenimiento[0]));
            JComboBox<String> prio = new JComboBox<>(new String[]{"Baja","Media","Alta","Crítica"});
            prio.setSelectedItem(m.getPrioridad()==null?"Media":m.getPrioridad());
            JComboBox<String> est = new JComboBox<>(new String[]{"Programado","En proceso","Finalizado","Cancelado"});
            est.setSelectedItem(m.getEstadoMantenimiento()==null?"Programado":m.getEstadoMantenimiento());
            for (int i = 0; i < activo.getItemCount(); i++) if (activo.getItemAt(i).getIdActivo()==m.getIdActivo()) activo.setSelectedIndex(i);
            for (int i = 0; i < tipo.getItemCount(); i++) if (tipo.getItemAt(i).getId()==m.getIdTipoMantenimiento()) tipo.setSelectedIndex(i);
            addField("activo","Activo", activo);
            addField("tipo","Tipo", tipo);
            addField("prog","Fecha programada (YYYY-MM-DD)", new JTextField(m.getFechaProgramada()==null?"":m.getFechaProgramada().toString()));
            JTextArea desc = new JTextArea(m.getDescripcion(), 3, 30); desc.setLineWrap(true);
            addField("desc","Descripción", new JScrollPane(desc));
            fields.put("descTA", desc);
            addField("prio","Prioridad", prio);
            addField("est","Estado", est);
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); }
        showCentered(500, 520);
    }
    @Override protected boolean onSave() {
        try {
            Activo a = (Activo)((JComboBox<?>)fields.get("activo")).getSelectedItem();
            TipoMantenimiento tm = (TipoMantenimiento)((JComboBox<?>)fields.get("tipo")).getSelectedItem();
            if (a==null||tm==null) { JOptionPane.showMessageDialog(this,"Activo y tipo obligatorios."); return false; }
            m.setIdActivo(a.getIdActivo()); m.setIdTipoMantenimiento(tm.getId());
            m.setFechaProgramada(text("prog").isEmpty()?null:LocalDate.parse(text("prog")));
            m.setDescripcion(((JTextArea)fields.get("descTA")).getText().trim());
            m.setPrioridad(text("prio")); m.setEstadoMantenimiento(text("est"));
            if (nuevo) dao.insertar(m); else dao.actualizar(m);
            return true;
        } catch (Exception ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); return false; }
    }
}
