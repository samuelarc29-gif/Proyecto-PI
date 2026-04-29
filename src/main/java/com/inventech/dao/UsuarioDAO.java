package com.inventech.dao;

import com.inventech.config.ConexionBD;
import com.inventech.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private static final String SELECT_BASE =
            "SELECT u.id_usuario, u.id_rol, u.id_area, u.nombre_completo, u.documento, " +
            "       u.cargo, u.correo, u.telefono, u.estado_usuario, " +
            "       r.nombre_rol, a.nombre_area " +
            "FROM usuario u " +
            "INNER JOIN rol r ON r.id_rol = u.id_rol " +
            "LEFT JOIN area a ON a.id_area = u.id_area ";

    private Usuario map(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setIdUsuario(rs.getInt("id_usuario"));
        u.setIdRol(rs.getInt("id_rol"));
        int idArea = rs.getInt("id_area");
        u.setIdArea(rs.wasNull() ? null : idArea);
        u.setNombreCompleto(rs.getString("nombre_completo"));
        u.setDocumento(rs.getString("documento"));
        u.setCargo(rs.getString("cargo"));
        u.setCorreo(rs.getString("correo"));
        u.setTelefono(rs.getString("telefono"));
        u.setEstadoUsuario(rs.getString("estado_usuario"));
        u.setNombreRol(rs.getString("nombre_rol"));
        u.setNombreArea(rs.getString("nombre_area"));
        return u;
    }

    public List<Usuario> listar() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_BASE + " ORDER BY u.nombre_completo");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        }
        return lista;
    }

    /** Login: busca por correo O documento. */
    public Usuario autenticar(String correoOdocumento) throws SQLException {
        String sql = SELECT_BASE + " WHERE (u.correo = ? OR u.documento = ?) AND u.estado_usuario = 'Activo' LIMIT 1";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, correoOdocumento);
            ps.setString(2, correoOdocumento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    public int insertar(Usuario u) throws SQLException {
        String sql = "INSERT INTO usuario (id_rol, id_area, nombre_completo, documento, cargo, correo, telefono, estado_usuario) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, u.getIdRol());
            if (u.getIdArea() == null) ps.setNull(2, Types.INTEGER); else ps.setInt(2, u.getIdArea());
            ps.setString(3, u.getNombreCompleto());
            ps.setString(4, u.getDocumento());
            ps.setString(5, u.getCargo());
            ps.setString(6, u.getCorreo());
            ps.setString(7, u.getTelefono());
            ps.setString(8, u.getEstadoUsuario() == null ? "Activo" : u.getEstadoUsuario());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public boolean actualizar(Usuario u) throws SQLException {
        String sql = "UPDATE usuario SET id_rol=?, id_area=?, nombre_completo=?, documento=?, cargo=?, " +
                "correo=?, telefono=?, estado_usuario=? WHERE id_usuario=?";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, u.getIdRol());
            if (u.getIdArea() == null) ps.setNull(2, Types.INTEGER); else ps.setInt(2, u.getIdArea());
            ps.setString(3, u.getNombreCompleto());
            ps.setString(4, u.getDocumento());
            ps.setString(5, u.getCargo());
            ps.setString(6, u.getCorreo());
            ps.setString(7, u.getTelefono());
            ps.setString(8, u.getEstadoUsuario());
            ps.setInt(9, u.getIdUsuario());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int idUsuario) throws SQLException {
        String sql = "DELETE FROM usuario WHERE id_usuario=?";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;
        }
    }
}
