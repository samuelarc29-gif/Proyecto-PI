package co.inventech.dao;

import co.inventech.db.ConexionDB;
import co.inventech.model.*;
import java.sql.*;
import java.util.*;

public class CatalogoDAO {

    public List<Rol> listarRoles() throws SQLException {
        List<Rol> out = new ArrayList<>();
        try (Connection c = ConexionDB.get();
             PreparedStatement ps = c.prepareStatement("SELECT id_rol, nombre_rol, descripcion FROM rol ORDER BY nombre_rol");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Rol r = new Rol();
                r.setIdRol(rs.getInt(1)); r.setNombreRol(rs.getString(2)); r.setDescripcion(rs.getString(3));
                out.add(r);
            }
        }
        return out;
    }

    public List<Area> listarAreas() throws SQLException {
        List<Area> out = new ArrayList<>();
        try (Connection c = ConexionDB.get();
             PreparedStatement ps = c.prepareStatement("SELECT id_area, nombre_area, descripcion, estado_area FROM area ORDER BY nombre_area");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Area a = new Area();
                a.setIdArea(rs.getInt(1)); a.setNombreArea(rs.getString(2));
                a.setDescripcion(rs.getString(3)); a.setEstadoArea(rs.getString(4));
                out.add(a);
            }
        }
        return out;
    }

    public List<TipoActivo> listarTiposActivo() throws SQLException {
        List<TipoActivo> out = new ArrayList<>();
        try (Connection c = ConexionDB.get();
             PreparedStatement ps = c.prepareStatement("SELECT id_tipo_activo, nombre_tipo FROM tipo_activo ORDER BY nombre_tipo");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(new TipoActivo(rs.getInt(1), rs.getString(2)));
        }
        return out;
    }

    public List<Marca> listarMarcas() throws SQLException {
        List<Marca> out = new ArrayList<>();
        try (Connection c = ConexionDB.get();
             PreparedStatement ps = c.prepareStatement("SELECT id_marca, nombre_marca FROM marca ORDER BY nombre_marca");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(new Marca(rs.getInt(1), rs.getString(2)));
        }
        return out;
    }

    public List<EstadoActivo> listarEstadosActivo() throws SQLException {
        List<EstadoActivo> out = new ArrayList<>();
        try (Connection c = ConexionDB.get();
             PreparedStatement ps = c.prepareStatement("SELECT id_estado_activo, nombre_estado FROM estado_activo ORDER BY nombre_estado");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(new EstadoActivo(rs.getInt(1), rs.getString(2)));
        }
        return out;
    }

    public List<EstadoTicket> listarEstadosTicket() throws SQLException {
        List<EstadoTicket> out = new ArrayList<>();
        try (Connection c = ConexionDB.get();
             PreparedStatement ps = c.prepareStatement("SELECT id_estado_ticket, nombre_estado FROM estado_ticket ORDER BY id_estado_ticket");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(new EstadoTicket(rs.getInt(1), rs.getString(2)));
        }
        return out;
    }

    public List<TipoMantenimiento> listarTiposMantenimiento() throws SQLException {
        List<TipoMantenimiento> out = new ArrayList<>();
        try (Connection c = ConexionDB.get();
             PreparedStatement ps = c.prepareStatement("SELECT id_tipo_mantenimiento, nombre_tipo FROM tipo_mantenimiento ORDER BY nombre_tipo");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(new TipoMantenimiento(rs.getInt(1), rs.getString(2)));
        }
        return out;
    }

    public Set<String> permisosDeRol(int idRol) throws SQLException {
        Set<String> out = new HashSet<>();
        String sql = "SELECT p.nombre_permiso FROM rol_permiso rp JOIN permiso p ON p.id_permiso = rp.id_permiso WHERE rp.id_rol = ?";
        try (Connection c = ConexionDB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idRol);
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) out.add(rs.getString(1)); }
        }
        return out;
    }
}
