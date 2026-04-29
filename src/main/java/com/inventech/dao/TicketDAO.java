package com.inventech.dao;

import com.inventech.config.ConexionBD;
import com.inventech.model.Ticket;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {

    private static final String SELECT_BASE =
            "SELECT t.id_ticket, t.id_activo, t.id_usuario_solicitante, t.id_estado_ticket, t.id_ans, " +
            "       t.descripcion_falla, t.fecha_reporte, t.prioridad, t.fecha_cierre_tecnico, " +
            "       t.fecha_cierre_usuario, t.observacion_cierre_usuario, " +
            "       a.codigo_interno, u.nombre_completo, et.nombre_estado " +
            "FROM ticket t " +
            "INNER JOIN activo a ON a.id_activo = t.id_activo " +
            "INNER JOIN usuario u ON u.id_usuario = t.id_usuario_solicitante " +
            "INNER JOIN estado_ticket et ON et.id_estado_ticket = t.id_estado_ticket ";

    private Ticket map(ResultSet rs) throws SQLException {
        Ticket t = new Ticket();
        t.setIdTicket(rs.getInt("id_ticket"));
        t.setIdActivo(rs.getInt("id_activo"));
        t.setIdUsuarioSolicitante(rs.getInt("id_usuario_solicitante"));
        t.setIdEstadoTicket(rs.getInt("id_estado_ticket"));
        int idAns = rs.getInt("id_ans");
        t.setIdAns(rs.wasNull() ? null : idAns);
        t.setDescripcionFalla(rs.getString("descripcion_falla"));
        Timestamp fr = rs.getTimestamp("fecha_reporte");
        t.setFechaReporte(fr == null ? null : fr.toLocalDateTime());
        t.setPrioridad(rs.getString("prioridad"));
        Timestamp fct = rs.getTimestamp("fecha_cierre_tecnico");
        t.setFechaCierreTecnico(fct == null ? null : fct.toLocalDateTime());
        Timestamp fcu = rs.getTimestamp("fecha_cierre_usuario");
        t.setFechaCierreUsuario(fcu == null ? null : fcu.toLocalDateTime());
        t.setObservacionCierreUsuario(rs.getString("observacion_cierre_usuario"));
        t.setCodigoActivo(rs.getString("codigo_interno"));
        t.setNombreUsuario(rs.getString("nombre_completo"));
        t.setNombreEstado(rs.getString("nombre_estado"));
        return t;
    }

    public List<Ticket> listar() throws SQLException {
        List<Ticket> lista = new ArrayList<>();
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_BASE + " ORDER BY t.fecha_reporte DESC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        }
        return lista;
    }

    public int insertar(Ticket t) throws SQLException {
        String sql = "INSERT INTO ticket (id_activo, id_usuario_solicitante, id_estado_ticket, id_ans, " +
                "descripcion_falla, prioridad) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, t.getIdActivo());
            ps.setInt(2, t.getIdUsuarioSolicitante());
            ps.setInt(3, t.getIdEstadoTicket());
            if (t.getIdAns() == null) ps.setNull(4, Types.INTEGER); else ps.setInt(4, t.getIdAns());
            ps.setString(5, t.getDescripcionFalla());
            ps.setString(6, t.getPrioridad());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) return rs.getInt(1); }
        }
        return -1;
    }

    public boolean actualizar(Ticket t) throws SQLException {
        String sql = "UPDATE ticket SET id_activo=?, id_usuario_solicitante=?, id_estado_ticket=?, id_ans=?, " +
                "descripcion_falla=?, prioridad=? WHERE id_ticket=?";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, t.getIdActivo());
            ps.setInt(2, t.getIdUsuarioSolicitante());
            ps.setInt(3, t.getIdEstadoTicket());
            if (t.getIdAns() == null) ps.setNull(4, Types.INTEGER); else ps.setInt(4, t.getIdAns());
            ps.setString(5, t.getDescripcionFalla());
            ps.setString(6, t.getPrioridad());
            ps.setInt(7, t.getIdTicket());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM ticket WHERE id_ticket=?";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
