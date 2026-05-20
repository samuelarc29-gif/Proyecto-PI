package co.inventech.controller;

import co.inventech.dao.CatalogoDAO;
import co.inventech.dao.UsuarioDAO;
import co.inventech.model.Usuario;
import co.inventech.util.Sesion;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

public class AuthController {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final CatalogoDAO catalogoDAO = new CatalogoDAO();

    public boolean login(String usuario, String clave) throws SQLException {
        Optional<Usuario> opt = usuarioDAO.autenticar(usuario, clave);
        if (opt.isEmpty()) return false;
        Usuario u = opt.get();
        Set<String> perms = catalogoDAO.permisosDeRol(u.getIdRol());
        Sesion.iniciar(u, perms);
        return true;
    }

    public void logout() { Sesion.cerrar(); }
}
