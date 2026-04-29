package com.inventech.controller;

import com.inventech.dao.UsuarioDAO;
import com.inventech.model.Usuario;

import java.sql.SQLException;

public class LoginController {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    /** Autentica por correo o documento. NOTA: el esquema actual no tiene columna de contraseña.
     *  En un proyecto real, agrega la columna password_hash y valida con BCrypt. */
    public Usuario autenticar(String correoOdocumento) throws SQLException {
        if (correoOdocumento == null || correoOdocumento.isBlank()) return null;
        return usuarioDAO.autenticar(correoOdocumento.trim());
    }
}
