package co.inventech.dao;

import co.inventech.db.ConexionDB;
import co.inventech.model.Activo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;

import java.util.ArrayList;
import java.util.List;

public class ActivoDAO {

    private Activo map(ResultSet rs) throws SQLException {
        Activo a = new Activo();
        a.setIdActivo(rs.getInt("id_activo"));
        a.setIdTipoActivo(rs.getInt("id_tipo_activo"));
        a.setIdMarca(rs.getInt("id_marca"));
        a.setIdEstadoActivo(rs.getInt("id_estado_activo"));
        int p = rs.getInt("id_proveedor"); a.setIdProveedor(rs.wasNull() ? null : p);
        int ar = rs.getInt("id_area"); a.setIdArea(rs.wasNull() ? null : ar);
        a.setModelo(rs.getString("modelo"));
        a.setNumeroSerie(rs.getString("numero_serie"));
        a.setCodigoInterno(rs.getString("codigo_interno"));
        a.setObservaciones(rs.getString("observaciones"));
        java.sql.Date fa = rs.getDate("fecha_adquisicion"); if (fa != null) a.setFechaAdquisicion(fa.toLocalDate());
        java.sql.Date fg = rs.getDate("fecha_garantia"); if (fg != null) a.setFechaGarantia(fg.toLocalDate());
        Timestamp fr = rs.getTimestamp("fecha_registro"); if (fr != null) a.setFechaRegistro(fr.toLocalDateTime());
        try { a.setTipoNombre(rs.getString("tipo_nombre")); } catch (SQLException ignored) {}
        try { a.setMarcaNombre(rs.getString("marca_nombre")); } catch (SQLException ignored) {}
        try { a.setEstadoNombre(rs.getString("estado_nombre")); } catch (SQLException ignored) {}
        try { a.setAreaNombre(rs.getString("area_nombre")); } catch (SQLException ignored) {}
        try { a.setProveedorNombre(rs.getString("proveedor_nombre")); } catch (SQLException ignored) {}
        return a;
    }

    private static final String SELECT_BASE = """
        SELECT a.*,
               t.nombre_tipo AS tipo_nombre, m.nombre_marca AS marca_nombre,
               e.nombre_estado AS estado_nombre, ar.nombre_area AS area_nombre,
               p.nombre_proveedor AS proveedor_nombre
        FROM activo a
        JOIN tipo_activo t ON t.id_tipo_activo = a.id_tipo_activo
        JOIN marca m ON m.id_marca = a.id_marca
        JOIN estado_activo e ON e.id_estado_activo = a.id_estado_activo
        LEFT JOIN area ar ON ar.id_area = a.id_area
        LEFT JOIN proveedor p ON p.id_proveedor = a.id_proveedor
        """;

    public List<Activo> listar(String filtro) throws SQLException {
        String sql = SELECT_BASE +
                " WHERE (? = '' OR a.codigo_interno LIKE ? OR a.numero_serie LIKE ? OR a.modelo LIKE ?)" +
                " ORDER BY a.codigo_interno";
        List<Activo> out = new ArrayList<>();
        try (Connection c = ConexionDB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            String f = filtro == null ? "" : filtro.trim();
            String like = "%" + f + "%";
            ps.setString(1, f); ps.setString(2, like); ps.setString(3, like); ps.setString(4, like);
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) out.add(map(rs)); }
        }
        return out;
    }

    public List<Activo> listarDisponibles() throws SQLException {
        String sql = SELECT_BASE + " WHERE e.nombre_estado = 'Disponible' ORDER BY a.codigo_interno";
        List<Activo> out = new ArrayList<>();
        try (Connection c = ConexionDB.get(); PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) { while (rs.next()) out.add(map(rs)); }
        return out;
    }

    public List<Activo> listarTodos() throws SQLException { return listar(""); }

    public int insertar(Activo a) throws SQLException {
        String sql = """
            INSERT INTO activo (id_tipo_activo, id_marca, id_proveedor, id_estado_activo, id_area,
                                modelo, numero_serie, codigo_interno, fecha_adquisicion, fecha_garantia, observaciones)
            VALUES (?,?,?,?,?,?,?,?,?,?,?)
            """;
        try (Connection c = ConexionDB.get(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, a.getIdTipoActivo());
            ps.setInt(2, a.getIdMarca());
            if (a.getIdProveedor()==null) ps.setNull(3, Types.INTEGER); else ps.setInt(3, a.getIdProveedor());
            ps.setInt(4, a.getIdEstadoActivo());
            if (a.getIdArea()==null) ps.setNull(5, Types.INTEGER); else ps.setInt(5, a.getIdArea());
            ps.setString(6, a.getModelo());
            ps.setString(7, a.getNumeroSerie());
            ps.setString(8, a.getCodigoInterno());
            ps.setDate(9, a.getFechaAdquisicion()==null?null:java.sql.Date.valueOf(a.getFechaAdquisicion()));
            ps.setDate(10, a.getFechaGarantia()==null?null:java.sql.Date.valueOf(a.getFechaGarantia()));
            ps.setString(11, a.getObservaciones());
            ps.executeUpdate();
            try (ResultSet k = ps.getGeneratedKeys()) { if (k.next()) return k.getInt(1); }
        }
        return -1;
    }

    public void actualizar(Activo a) throws SQLException {
        String sql = """
            UPDATE activo SET id_tipo_activo=?, id_marca=?, id_proveedor=?, id_estado_activo=?, id_area=?,
                              modelo=?, numero_serie=?, codigo_interno=?, fecha_adquisicion=?, fecha_garantia=?, observaciones=?
            WHERE id_activo=?
            """;
        try (Connection c = ConexionDB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, a.getIdTipoActivo());
            ps.setInt(2, a.getIdMarca());
            if (a.getIdProveedor()==null) ps.setNull(3, Types.INTEGER); else ps.setInt(3, a.getIdProveedor());
            ps.setInt(4, a.getIdEstadoActivo());
            if (a.getIdArea()==null) ps.setNull(5, Types.INTEGER); else ps.setInt(5, a.getIdArea());
            ps.setString(6, a.getModelo());
            ps.setString(7, a.getNumeroSerie());
            ps.setString(8, a.getCodigoInterno());
            ps.setDate(9, a.getFechaAdquisicion()==null?null:java.sql.Date.valueOf(a.getFechaAdquisicion()));
            ps.setDate(10, a.getFechaGarantia()==null?null:java.sql.Date.valueOf(a.getFechaGarantia()));
            ps.setString(11, a.getObservaciones());
            ps.setInt(12, a.getIdActivo());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        try (Connection c = ConexionDB.get(); PreparedStatement ps = c.prepareStatement("DELETE FROM activo WHERE id_activo=?")) {
            ps.setInt(1, id); ps.executeUpdate();
        }
    }

    public void cambiarEstado(int idActivo, int idEstadoNuevo, Integer idUsuarioRegistra, String observacion) throws SQLException {
        try (Connection c = ConexionDB.get()) {
            c.setAutoCommit(false);
            try {
                Integer estadoAnterior = null;
                try (PreparedStatement q = c.prepareStatement("SELECT id_estado_activo FROM activo WHERE id_activo=?")) {
                    q.setInt(1, idActivo);
                    try (ResultSet rs = q.executeQuery()) { if (rs.next()) estadoAnterior = rs.getInt(1); }
                }
                try (PreparedStatement u = c.prepareStatement("UPDATE activo SET id_estado_activo=? WHERE id_activo=?")) {
                    u.setInt(1, idEstadoNuevo); u.setInt(2, idActivo); u.executeUpdate();
                }
                try (PreparedStatement h = c.prepareStatement(
                        "INSERT INTO historial_estado_activo (id_activo, id_estado_anterior, id_estado_nuevo, id_usuario_registra, observacion) VALUES (?,?,?,?,?)")) {
                    h.setInt(1, idActivo);
                    if (estadoAnterior == null) h.setNull(2, Types.INTEGER); else h.setInt(2, estadoAnterior);
                    h.setInt(3, idEstadoNuevo);
                    if (idUsuarioRegistra == null) h.setNull(4, Types.INTEGER); else h.setInt(4, idUsuarioRegistra);
                    h.setString(5, observacion);
                    h.executeUpdate();
                }
                c.commit();
            } catch (SQLException ex) { c.rollback(); throw ex; }
            finally { c.setAutoCommit(true); }
        }
    }
}
