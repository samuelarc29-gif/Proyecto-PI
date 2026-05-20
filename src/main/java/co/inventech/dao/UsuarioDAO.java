package co.inventech.dao;

import co.inventech.db.ConexionDB;
import co.inventech.model.Usuario;
import co.inventech.util.PasswordUtil;

import java.sql.*;
import java.util.*;

public class UsuarioDAO {

    private Usuario map(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setIdUsuario(rs.getInt("id_usuario"));
        u.setIdRol(rs.getInt("id_rol"));
        int area = rs.getInt("id_area"); u.setIdArea(rs.wasNull() ? null : area);
        u.setNombreCompleto(rs.getString("nombre_completo"));
        u.setDocumento(rs.getString("documento"));
        u.setCargo(rs.getString("cargo"));
        u.setCorreo(rs.getString("correo"));
        u.setTelefono(rs.getString("telefono"));
        u.setNombreUsuario(rs.getString("nombre_usuario"));
        u.setContrasenaHash(rs.getString("contrasena_hash"));
        u.setEstadoUsuario(rs.getString("estado_usuario"));
        Timestamp fc = rs.getTimestamp("fecha_creacion");
        if (fc != null) u.setFechaCreacion(fc.toLocalDateTime());
        try { u.setRolNombre(rs.getString("rol_nombre")); } catch (SQLException ignored) {}
        try { u.setAreaNombre(rs.getString("area_nombre")); } catch (SQLException ignored) {}
        return u;
    }

    public Optional<Usuario> autenticar(String usuario, String clave) throws SQLException {
        String sql = """
            SELECT u.*, r.nombre_rol AS rol_nombre, a.nombre_area AS area_nombre
            FROM usuario u
            JOIN rol r ON r.id_rol = u.id_rol
            LEFT JOIN area a ON a.id_area = u.id_area
            WHERE u.nombre_usuario = ? AND u.estado_usuario = 'Activo'
            """;
        try (Connection c = ConexionDB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, usuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = map(rs);
                    if (PasswordUtil.verify(clave, u.getContrasenaHash())) return Optional.of(u);
                }
            }
        }
        return Optional.empty();
    }

    public List<Usuario> listar(String filtro) throws SQLException {
        String sql = """
            SELECT u.*, r.nombre_rol AS rol_nombre, a.nombre_area AS area_nombre
            FROM usuario u
            JOIN rol r ON r.id_rol = u.id_rol
            LEFT JOIN area a ON a.id_area = u.id_area
            WHERE (? = '' OR u.nombre_completo LIKE ? OR u.nombre_usuario LIKE ? OR u.documento LIKE ?)
            ORDER BY u.nombre_completo
            """;
        List<Usuario> out = new ArrayList<>();
        try (Connection c = ConexionDB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            String f = filtro == null ? "" : filtro.trim();
            String like = "%" + f + "%";
            ps.setString(1, f); ps.setString(2, like); ps.setString(3, like); ps.setString(4, like);
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) out.add(map(rs)); }
        }
        return out;
    }

    public int insertar(Usuario u, String clavePlana) throws SQLException {
        String sql = """
            INSERT INTO usuario (id_rol, id_area, nombre_completo, documento, cargo, correo,
                                 telefono, nombre_usuario, contrasena_hash, estado_usuario)
            VALUES (?,?,?,?,?,?,?,?,?,?)
            """;
        try (Connection c = ConexionDB.get(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, u.getIdRol());
            if (u.getIdArea() == null) ps.setNull(2, Types.INTEGER); else ps.setInt(2, u.getIdArea());
            ps.setString(3, u.getNombreCompleto());
            ps.setString(4, u.getDocumento());
            ps.setString(5, u.getCargo());
            ps.setString(6, u.getCorreo());
            ps.setString(7, u.getTelefono());
            ps.setString(8, u.getNombreUsuario());
            ps.setString(9, PasswordUtil.hash(clavePlana));
            ps.setString(10, u.getEstadoUsuario());
            ps.executeUpdate();
            try (ResultSet k = ps.getGeneratedKeys()) { if (k.next()) return k.getInt(1); }
        }
        return -1;
    }

    public void actualizar(Usuario u, String nuevaClavePlana) throws SQLException {
        StringBuilder sb = new StringBuilder("UPDATE usuario SET id_rol=?, id_area=?, nombre_completo=?, documento=?, cargo=?, correo=?, telefono=?, nombre_usuario=?, estado_usuario=?");
        if (nuevaClavePlana != null && !nuevaClavePlana.isBlank()) sb.append(", contrasena_hash=?");
        sb.append(" WHERE id_usuario=?");
        try (Connection c = ConexionDB.get(); PreparedStatement ps = c.prepareStatement(sb.toString())) {
            int i = 1;
            ps.setInt(i++, u.getIdRol());
            if (u.getIdArea() == null) ps.setNull(i++, Types.INTEGER); else ps.setInt(i++, u.getIdArea());
            ps.setString(i++, u.getNombreCompleto());
            ps.setString(i++, u.getDocumento());
            ps.setString(i++, u.getCargo());
            ps.setString(i++, u.getCorreo());
            ps.setString(i++, u.getTelefono());
            ps.setString(i++, u.getNombreUsuario());
            ps.setString(i++, u.getEstadoUsuario());
            if (nuevaClavePlana != null && !nuevaClavePlana.isBlank())
                ps.setString(i++, PasswordUtil.hash(nuevaClavePlana));
            ps.setInt(i, u.getIdUsuario());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        try (Connection c = ConexionDB.get(); PreparedStatement ps = c.prepareStatement("DELETE FROM usuario WHERE id_usuario=?")) {
            ps.setInt(1, id); ps.executeUpdate();
        }
    }

    public List<Usuario> listarTecnicos() throws SQLException {
        String sql = """
            SELECT u.*, r.nombre_rol AS rol_nombre, a.nombre_area AS area_nombre
            FROM usuario u JOIN rol r ON r.id_rol = u.id_rol
            LEFT JOIN area a ON a.id_area = u.id_area
            WHERE r.nombre_rol IN ('Técnico de soporte','Coordinador de soporte técnico')
              AND u.estado_usuario = 'Activo'
            ORDER BY u.nombre_completo
            """;
        List<Usuario> out = new ArrayList<>();
        try (Connection c = ConexionDB.get(); PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) { while (rs.next()) out.add(map(rs)); }
        return out;
    }
}
