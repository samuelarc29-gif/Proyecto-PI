package co.inventech.view.dialogs;
import co.inventech.dao.ProveedorDAO;
import co.inventech.model.Proveedor;
import co.inventech.view.components.FormDialog;
import javax.swing.*; import java.awt.*; import java.sql.SQLException;

public class ProveedorDialog extends FormDialog {
    private final Proveedor p; private final boolean nuevo; private final ProveedorDAO dao = new ProveedorDAO();
    public ProveedorDialog(Window owner, Proveedor base) {
        super(owner, base==null?"Nuevo proveedor":"Editar proveedor");
        this.nuevo = base==null; this.p = base==null?new Proveedor():base;
        addField("nombre","Nombre", new JTextField(p.getNombreProveedor()));
        addField("contacto","Contacto", new JTextField(p.getContacto()));
        addField("telefono","Teléfono", new JTextField(p.getTelefono()));
        addField("correo","Correo", new JTextField(p.getCorreo()));
        addField("direccion","Dirección", new JTextField(p.getDireccion()));
        JTextArea ta = new JTextArea(p.getDescripcionSoporte(), 4, 30); ta.setLineWrap(true); ta.setWrapStyleWord(true);
        addField("desc","Descripción soporte", new JScrollPane(ta));
        fields.put("descTA", ta);
        JComboBox<String> est = new JComboBox<>(new String[]{"Activo","Inactivo"});
        est.setSelectedItem(p.getEstadoProveedor()==null?"Activo":p.getEstadoProveedor());
        addField("estado","Estado", est);
        showCentered(480, 480);
    }
    @Override protected boolean onSave() {
        try {
            if (text("nombre").isEmpty()) { JOptionPane.showMessageDialog(this,"Nombre obligatorio."); return false; }
            p.setNombreProveedor(text("nombre")); p.setContacto(text("contacto")); p.setTelefono(text("telefono"));
            p.setCorreo(text("correo")); p.setDireccion(text("direccion"));
            p.setDescripcionSoporte(((JTextArea)fields.get("descTA")).getText().trim());
            p.setEstadoProveedor(text("estado"));
            if (nuevo) dao.insertar(p); else dao.actualizar(p);
            return true;
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage()); return false; }
    }
}
