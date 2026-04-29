package com.inventech.dao;

import com.inventech.config.ConexionBD;
import com.inventech.model.AsignacionActivo;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AsignacionActivoDAO {

    private static final String SELECT_BASE =
            "SELECT aa.id_asignacion_activo, aa.id_activo, aa.id_usuario, aa.fecha_asignacion, " +
            "       aa.fecha_devolucion, aa.observaciones, aa.estado_asignacion, " +
            "       a.codigo_interno, u.nombre_completo " +
            "FROM asignacion_activo aa " +
            "INNER JOIN activo a ON a.id_activo = aa.id_activo " +
            "INNER JOIN usuario u ON u.id_usuario = aa.id_usuario ";

    private AsignacionActivo map(ResultSet rs) throws SQLException {
        AsignacionActivo a = new AsignacionActivo();
        a.setIdAsignacionActivo(rs.getInt("id_asignacion_activo"));
        a.setIdActivo(rs.getInt("id_activo"));
        a.setIdUsuario(rs.getInt("id_usuario"));
        Date f1 = rs.getDate("fecha_asignacion");
        a.setFechaAsignacion(f1 == null ? null : f1.toLocalDate());
        Date f2 = rs.getDate("fecha_devolucion");
        a.setFechaDevolucion(f2 == null ? null : f2.toLocalDate());
        a.setObservaciones(rs.getString("observaciones"));
        a.setEstadoAsignacion(rs.getString("estado_asignacion"));
        a.setCodigoActivo(rs.getString("codigo_interno"));
        a.setNombreUsuario(rs.getString("nombre_completo"));
        return a;
    }

    public List<AsignacionActivo> listar() throws SQLException {
        List<AsignacionActivo> lista = new ArrayList<>();
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_BASE + " ORDER BY aa.fecha_asignacion DESC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        }
        return lista;
    }

    public int insertar(AsignacionActivo a) throws SQLException {
        String sql = "INSERT INTO asignacion_activo (id_activo, id_usuario, fecha_asignacion, fecha_devolucion, observaciones, estado_asignacion) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, a.getIdActivo());
            ps.setInt(2, a.getIdUsuario());
            ps.setDate(3, a.getFechaAsignacion() == null ? null : Date.valueOf(a.getFechaAsignacion()));
            ps.setDate(4, a.getFechaDevolucion() == null ? null : Date.valueOf(a.getFechaDevolucion()));
            ps.setString(5, a.getObservaciones());
            ps.setString(6, a.getEstadoAsignacion() == null ? "Activa" : a.getEstadoAsignacion());
            ps.executeUpdate();
            // Marca activo como Asignado
            new ActivoDAO().cambiarEstadoPorNombre(a.getIdActivo(), "Asignado");
            try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) return rs.getInt(1); }
        }
        return -1;
    }

    public boolean actualizar(AsignacionActivo a) throws SQLException {
        String sql = "UPDATE asignacion_activo SET id_activo=?, id_usuario=?, fecha_asignacion=?, " +
                "fecha_devolucion=?, observaciones=?, estado_asignacion=? WHERE id_asignacion_activo=?";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, a.getIdActivo());
            ps.setInt(2, a.getIdUsuario());
            ps.setDate(3, a.getFechaAsignacion() == null ? null : Date.valueOf(a.getFechaAsignacion()));
            ps.setDate(4, a.getFechaDevolucion() == null ? null : Date.valueOf(a.getFechaDevolucion()));
            ps.setString(5, a.getObservaciones());
            ps.setString(6, a.getEstadoAsignacion());
            ps.setInt(7, a.getIdAsignacionActivo());
            boolean ok = ps.executeUpdate() > 0;
            // Si quedó finalizada, libera el activo
            if (ok && "Finalizada".equalsIgnoreCase(a.getEstadoAsignacion())) {
                new ActivoDAO().cambiarEstadoPorNombre(a.getIdActivo(), "Disponible");
            }
            return ok;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM asignacion_activo WHERE id_asignacion_activo=?";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    /** Atajo conservado para compatibilidad. */
    public boolean devolver(int idAsignacion, int idActivo, LocalDate fechaDevolucion) throws SQLException {
        String sql = "UPDATE asignacion_activo SET fecha_devolucion=?, estado_asignacion='Finalizada' WHERE id_asignacion_activo=?";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(fechaDevolucion));
            ps.setInt(2, idAsignacion);
            boolean ok = ps.executeUpdate() > 0;
            if (ok) new ActivoDAO().cambiarEstadoPorNombre(idActivo, "Disponible");
            return ok;
        }
    }
}
