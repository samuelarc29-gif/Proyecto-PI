package co.inventech.dao;

import co.inventech.db.ConexionDB;
import co.inventech.model.Ticket;
import java.sql.*;
import java.util.*;

public class TicketDAO {
    private Ticket map(ResultSet rs) throws SQLException {
        Ticket t = new Ticket();
        t.setIdTicket(rs.getInt("id_ticket"));
        t.setIdActivo(rs.getInt("id_activo"));
        t.setIdUsuarioSolicitante(rs.getInt("id_usuario_solicitante"));
        t.setIdEstadoTicket(rs.getInt("id_estado_ticket"));
        int a = rs.getInt("id_ans"); t.setIdAns(rs.wasNull()?null:a);
        t.setDescripcionFalla(rs.getString("descripcion_falla"));
        t.setPrioridad(rs.getString("prioridad"));
        t.setObservacionCierre(rs.getString("observacion_cierre"));
        Timestamp fr = rs.getTimestamp("fecha_reporte"); if (fr!=null) t.setFechaReporte(fr.toLocalDateTime());
        Timestamp fc = rs.getTimestamp("fecha_cierre_usuario"); if (fc!=null) t.setFechaCierreUsuario(fc.toLocalDateTime());
        try { t.setActivoCodigo(rs.getString("activo_codigo")); } catch (SQLException ignored) {}
        try { t.setSolicitanteNombre(rs.getString("solicitante")); } catch (SQLException ignored) {}
        try { t.setEstadoNombre(rs.getString("estado_nombre")); } catch (SQLException ignored) {}
        try { t.setTecnicoAsignado(rs.getString("tecnico")); } catch (SQLException ignored) {}
        return t;
    }

    private static final String BASE = """
        SELECT t.*, a.codigo_interno AS activo_codigo, u.nombre_completo AS solicitante,
               e.nombre_estado AS estado_nombre,
               (SELECT GROUP_CONCAT(ut.nombre_completo SEPARATOR ', ')
                  FROM asignacion_tecnico at JOIN usuario ut ON ut.id_usuario = at.id_usuario_tecnico
                  WHERE at.id_ticket = t.id_ticket) AS tecnico
        FROM ticket t
        JOIN activo a ON a.id_activo = t.id_activo
        JOIN usuario u ON u.id_usuario = t.id_usuario_solicitante
        JOIN estado_ticket e ON e.id_estado_ticket = t.id_estado_ticket
        """;

    public List<Ticket> listar(String filtro) throws SQLException {
        String sql = BASE + " WHERE (? = '' OR a.codigo_interno LIKE ? OR t.descripcion_falla LIKE ?) ORDER BY t.fecha_reporte DESC";
        List<Ticket> out = new ArrayList<>();
        try (Connection c = ConexionDB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            String f = filtro==null?"":filtro.trim(); String like = "%"+f+"%";
            ps.setString(1,f); ps.setString(2,like); ps.setString(3,like);
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) out.add(map(rs)); }
        }
        return out;
    }

    public int insertar(Ticket t) throws SQLException {
        String sql = "INSERT INTO ticket (id_activo,id_usuario_solicitante,id_estado_ticket,id_ans,descripcion_falla,prioridad) VALUES (?,?,?,?,?,?)";
        try (Connection c = ConexionDB.get(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1,t.getIdActivo()); ps.setInt(2,t.getIdUsuarioSolicitante()); ps.setInt(3,t.getIdEstadoTicket());
            if (t.getIdAns()==null) ps.setNull(4,Types.INTEGER); else ps.setInt(4,t.getIdAns());
            ps.setString(5,t.getDescripcionFalla()); ps.setString(6,t.getPrioridad());
            ps.executeUpdate();
            try (ResultSet k = ps.getGeneratedKeys()) { if (k.next()) return k.getInt(1); }
        }
        return -1;
    }

    public void actualizarEstado(int idTicket, int idEstado, String observacion) throws SQLException {
        String sql = "UPDATE ticket SET id_estado_ticket=?, observacion_cierre=COALESCE(?, observacion_cierre) WHERE id_ticket=?";
        try (Connection c = ConexionDB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1,idEstado); ps.setString(2,observacion); ps.setInt(3,idTicket); ps.executeUpdate();
        }
    }

    public void asignarTecnico(int idTicket, int idTecnico) throws SQLException {
        try (Connection c = ConexionDB.get()) {
            c.setAutoCommit(false);
            try {
                try (PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO asignacion_tecnico (id_ticket,id_usuario_tecnico) VALUES (?,?)")) {
                    ps.setInt(1,idTicket); ps.setInt(2,idTecnico); ps.executeUpdate();
                }
                try (PreparedStatement ps = c.prepareStatement(
                        "UPDATE ticket SET id_estado_ticket = (SELECT id_estado_ticket FROM estado_ticket WHERE nombre_estado='Asignado') WHERE id_ticket=?")) {
                    ps.setInt(1,idTicket); ps.executeUpdate();
                }
                c.commit();
            } catch (SQLException ex) { c.rollback(); throw ex; } finally { c.setAutoCommit(true); }
        }
    }

    public void eliminar(int id) throws SQLException {
        try (Connection c = ConexionDB.get(); PreparedStatement ps = c.prepareStatement("DELETE FROM ticket WHERE id_ticket=?")) {
            ps.setInt(1,id); ps.executeUpdate();
        }
    }
}
