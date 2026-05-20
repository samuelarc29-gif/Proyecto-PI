package co.inventech.view.dialogs;

import co.inventech.dao.*;
import co.inventech.model.*;
import co.inventech.view.components.FormDialog;
import javax.swing.*; import java.awt.*;
import java.sql.SQLException; import java.time.LocalDate;

public class ActivoDialog extends FormDialog {
    private final Activo a; private final boolean nuevo; private final ActivoDAO dao = new ActivoDAO();
    public ActivoDialog(Window owner, Activo base) {
        super(owner, base==null?"Nuevo activo":"Editar activo");
        nuevo = base==null; a = base==null?new Activo():base;
        try {
            CatalogoDAO cd = new CatalogoDAO();
            ProveedorDAO pd = new ProveedorDAO();
            JComboBox<TipoActivo> tipo = new JComboBox<>(cd.listarTiposActivo().toArray(new TipoActivo[0]));
            JComboBox<Marca> marca = new JComboBox<>(cd.listarMarcas().toArray(new Marca[0]));
            JComboBox<EstadoActivo> estado = new JComboBox<>(cd.listarEstadosActivo().toArray(new EstadoActivo[0]));
            JComboBox<Area> area = new JComboBox<>(); area.addItem(null);
            for (Area ar : cd.listarAreas()) area.addItem(ar);
            JComboBox<Proveedor> prov = new JComboBox<>(); prov.addItem(null);
            for (Proveedor pp : pd.listar("")) prov.addItem(pp);

            select(tipo, t -> t.getId()==a.getIdTipoActivo());
            select(marca, m -> m.getId()==a.getIdMarca());
            select(estado, e -> e.getId()==a.getIdEstadoActivo());
            selectN(area, ar -> a.getIdArea()!=null && ar!=null && ar.getIdArea()==a.getIdArea());
            selectN(prov, pp -> a.getIdProveedor()!=null && pp!=null && pp.getIdProveedor()==a.getIdProveedor());

            addField("codigo","Código interno", new JTextField(a.getCodigoInterno()));
            addField("serie","N° de serie", new JTextField(a.getNumeroSerie()));
            addField("modelo","Modelo", new JTextField(a.getModelo()));
            addField("tipo","Tipo de activo", tipo);
            addField("marca","Marca", marca);
            addField("estado","Estado", estado);
            addField("area","Área", area);
            addField("proveedor","Proveedor", prov);
            addField("adq","Fecha adquisición (YYYY-MM-DD)", new JTextField(a.getFechaAdquisicion()==null?"":a.getFechaAdquisicion().toString()));
            addField("gar","Fecha garantía (YYYY-MM-DD)", new JTextField(a.getFechaGarantia()==null?"":a.getFechaGarantia().toString()));
            JTextArea obs = new JTextArea(a.getObservaciones(), 3, 30); obs.setLineWrap(true);
            addField("obs","Observaciones", new JScrollPane(obs));
            fields.put("obsTA", obs);
        } catch (SQLException e) { JOptionPane.showMessageDialog(this,"Error: "+e.getMessage()); }
        showCentered(500, 640);
    }
    private <T> void select(JComboBox<T> cb, java.util.function.Predicate<T> p) {
        for (int i = 0; i < cb.getItemCount(); i++) if (cb.getItemAt(i) != null && p.test(cb.getItemAt(i))) { cb.setSelectedIndex(i); return; }
    }
    private <T> void selectN(JComboBox<T> cb, java.util.function.Predicate<T> p) {
        for (int i = 0; i < cb.getItemCount(); i++) if (p.test(cb.getItemAt(i))) { cb.setSelectedIndex(i); return; }
    }
    @Override protected boolean onSave() {
        try {
            if (text("codigo").isEmpty() || text("serie").isEmpty()) {
                JOptionPane.showMessageDialog(this,"Código y serie son obligatorios."); return false;
            }
            a.setCodigoInterno(text("codigo")); a.setNumeroSerie(text("serie")); a.setModelo(text("modelo"));
            a.setIdTipoActivo(((TipoActivo)((JComboBox<?>)fields.get("tipo")).getSelectedItem()).getId());
            a.setIdMarca(((Marca)((JComboBox<?>)fields.get("marca")).getSelectedItem()).getId());
            a.setIdEstadoActivo(((EstadoActivo)((JComboBox<?>)fields.get("estado")).getSelectedItem()).getId());
            Area ar = (Area)((JComboBox<?>)fields.get("area")).getSelectedItem();
            a.setIdArea(ar==null?null:ar.getIdArea());
            Proveedor pp = (Proveedor)((JComboBox<?>)fields.get("proveedor")).getSelectedItem();
            a.setIdProveedor(pp==null?null:pp.getIdProveedor());
            a.setFechaAdquisicion(text("adq").isEmpty()?null:LocalDate.parse(text("adq")));
            a.setFechaGarantia(text("gar").isEmpty()?null:LocalDate.parse(text("gar")));
            a.setObservaciones(((JTextArea)fields.get("obsTA")).getText().trim());
            if (nuevo) dao.insertar(a); else dao.actualizar(a);
            return true;
        } catch (Exception ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); return false; }
    }
}
