package co.inventech.view.dialogs;

import co.inventech.dao.CatalogoDAO;
import co.inventech.dao.UsuarioDAO;
import co.inventech.model.Area;
import co.inventech.model.Rol;
import co.inventech.model.Usuario;
import co.inventech.view.components.FormDialog;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class UsuarioDialog extends FormDialog {
    private final Usuario u;
    private final UsuarioDAO dao = new UsuarioDAO();
    private final boolean nuevo;

    public UsuarioDialog(Window owner, Usuario base) {
        super(owner, base == null ? "Nuevo usuario" : "Editar usuario");
        this.nuevo = (base == null);
        this.u = base == null ? new Usuario() : base;

        try {
            CatalogoDAO cd = new CatalogoDAO();
            JComboBox<Rol> cbRol = new JComboBox<>(cd.listarRoles().toArray(new Rol[0]));
            JComboBox<Area> cbArea = new JComboBox<>();
            cbArea.addItem(null);
            for (Area a : cd.listarAreas()) cbArea.addItem(a);

            addField("nombre", "Nombre completo", new JTextField(u.getNombreCompleto()));
            addField("documento", "Documento", new JTextField(u.getDocumento()));
            addField("correo", "Correo", new JTextField(u.getCorreo()));
            addField("telefono", "Teléfono", new JTextField(u.getTelefono()));
            addField("cargo", "Cargo", new JTextField(u.getCargo()));
            addField("usuario", "Usuario (login)", new JTextField(u.getNombreUsuario()));
            addField("clave", nuevo ? "Contraseña" : "Nueva contraseña (opcional)", new JPasswordField());
            for (int i = 0; i < cbRol.getItemCount(); i++) if (cbRol.getItemAt(i).getIdRol() == u.getIdRol()) cbRol.setSelectedIndex(i);
            for (int i = 0; i < cbArea.getItemCount(); i++) {
                Area a = cbArea.getItemAt(i);
                if ((a==null && u.getIdArea()==null) || (a!=null && u.getIdArea()!=null && a.getIdArea()==u.getIdArea())) cbArea.setSelectedIndex(i);
            }
            addField("rol", "Rol", cbRol);
            addField("area", "Área", cbArea);

            JComboBox<String> estado = new JComboBox<>(new String[]{"Activo","Inactivo"});
            estado.setSelectedItem(u.getEstadoUsuario() == null ? "Activo" : u.getEstadoUsuario());
            addField("estado", "Estado", estado);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error cargando catálogos: " + ex.getMessage());
        }
        showCentered(480, 560);
    }

    @Override protected boolean onSave() {
        try {
            String nombre = text("nombre"); String doc = text("documento"); String correo = text("correo");
            String usr = text("usuario"); String clave = text("clave");
            if (nombre.isEmpty() || doc.isEmpty() || correo.isEmpty() || usr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Completa todos los campos obligatorios."); return false;
            }
            if (nuevo && clave.isEmpty()) {
                JOptionPane.showMessageDialog(this, "La contraseña es obligatoria."); return false;
            }
            u.setNombreCompleto(nombre); u.setDocumento(doc); u.setCorreo(correo);
            u.setTelefono(text("telefono")); u.setCargo(text("cargo")); u.setNombreUsuario(usr);
            Rol r = (Rol) ((JComboBox<?>) fields.get("rol")).getSelectedItem();
            Area a = (Area) ((JComboBox<?>) fields.get("area")).getSelectedItem();
            u.setIdRol(r.getIdRol());
            u.setIdArea(a == null ? null : a.getIdArea());
            u.setEstadoUsuario(text("estado"));
            if (nuevo) dao.insertar(u, clave);
            else dao.actualizar(u, clave);
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
            return false;
        }
    }
}
