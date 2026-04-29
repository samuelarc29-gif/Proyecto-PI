package com.inventech.dao;

import com.inventech.config.ConexionBD;
import com.inventech.model.AsignacionTecnico;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AsignacionTecnicoDAO {

    private static final String SELECT_BASE =
            "SELECT at.id_asignacion_tecnico, at.id_ticket, at.id_usuario_tecnico, at.fecha_asignacion, " +
            "       at.fecha_atencion, at.diagnostico, at.solucion_aplicada, at.observaciones, at.cierre_tecnico, " +
            "       t.descripcion_falla, u.nombre_completo " +
            "FROM asignacion_tecnico at " +
            "INNER JOIN ticket t ON t.id_ticket = at.id_ticket " +
            "INNER JOIN usuario u ON u.id_usuario = at.id_usuario_tecnico ";

    private AsignacionTecnico map(ResultSet rs) throws SQLException {
        AsignacionTecnico a = new AsignacionTecnico();
        a.setIdAsignacionTecnico(rs.getInt("id_asignacion_tecnico"));
        a.setIdTicket(rs.getInt("id_ticket"));
        a.setIdUsuarioTecnico(rs.getInt("id_usuario_tecnico"));
        Timestamp fa = rs.getTimestamp("fecha_asignacion");
        a.setFechaAsignacion(fa == null ? null : fa.toLocalDateTime());
        Timestamp fat = rs.getTimestamp("fecha_atencion");
        a.setFechaAtencion(fat == null ? null : fat.toLocalDateTime());
        a.setDiagnostico(rs.getString("diagnostico"));
        a.setSolucionAplicada(rs.getString("solucion_aplicada"));
        a.setObservaciones(rs.getString("observaciones"));
        a.setCierreTecnico(rs.getString("cierre_tecnico"));
        a.setDescripcionTicket(rs.getString("descripcion_falla"));
        a.setNombreTecnico(rs.getString("nombre_completo"));
        return a;
    }

    public List<AsignacionTecnico> listar() throws SQLException {
        List<AsignacionTecnico> lista = new ArrayList<>();
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_BASE + " ORDER BY at.fecha_asignacion DESC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        }
        return lista;
    }

    public int insertar(AsignacionTecnico a) throws SQLException {
        String sql = "INSERT INTO asignacion_tecnico (id_ticket, id_usuario_tecnico, diagnostico, solucion_aplicada, observaciones, cierre_tecnico) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, a.getIdTicket());
            ps.setInt(2, a.getIdUsuarioTecnico());
            ps.setString(3, a.getDiagnostico());
            ps.setString(4, a.getSolucionAplicada());
            ps.setString(5, a.getObservaciones());
            ps.setString(6, a.getCierreTecnico());
            ps.executeUpdate();
            // Marcar ticket como Asignado si existe ese estado
            try (PreparedStatement up = c.prepareStatement(
                    "UPDATE ticket SET id_estado_ticket = (SELECT id_estado_ticket FROM estado_ticket WHERE nombre_estado='Asignado' LIMIT 1) " +
                    "WHERE id_ticket=? AND EXISTS (SELECT 1 FROM estado_ticket WHERE nombre_estado='Asignado')")) {
                up.setInt(1, a.getIdTicket());
                up.executeUpdate();
            }
            try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) return rs.getInt(1); }
        }
        return -1;
    }

    public boolean actualizar(AsignacionTecnico a) throws SQLException {
        String sql = "UPDATE asignacion_tecnico SET id_ticket=?, id_usuario_tecnico=?, diagnostico=?, solucion_aplicada=?, " +
                "observaciones=?, cierre_tecnico=? WHERE id_asignacion_tecnico=?";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, a.getIdTicket());
            ps.setInt(2, a.getIdUsuarioTecnico());
            ps.setString(3, a.getDiagnostico());
            ps.setString(4, a.getSolucionAplicada());
            ps.setString(5, a.getObservaciones());
            ps.setString(6, a.getCierreTecnico());
            ps.setInt(7, a.getIdAsignacionTecnico());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM asignacion_tecnico WHERE id_asignacion_tecnico=?";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
