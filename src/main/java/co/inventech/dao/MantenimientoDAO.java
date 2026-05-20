package co.inventech.dao;

import co.inventech.db.ConexionDB;
import co.inventech.model.Mantenimiento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MantenimientoDAO {
    private Mantenimiento map(ResultSet rs) throws SQLException {
        Mantenimiento m = new Mantenimiento();
        m.setIdMantenimiento(rs.getInt("id_mantenimiento"));
        m.setIdActivo(rs.getInt("id_activo"));
        m.setIdTipoMantenimiento(rs.getInt("id_tipo_mantenimiento"));
        int t = rs.getInt("id_ticket"); m.setIdTicket(rs.wasNull()?null:t);
        int u = rs.getInt("id_usuario_solicitante"); m.setIdUsuarioSolicitante(rs.wasNull()?null:u);
        java.sql.Date fp = rs.getDate("fecha_programada"); if (fp!=null) m.setFechaProgramada(fp.toLocalDate());
        Timestamp fr = rs.getTimestamp("fecha_realizacion"); if (fr!=null) m.setFechaRealizacion(fr.toLocalDateTime());
        m.setDescripcion(rs.getString("descripcion"));
        m.setPrioridad(rs.getString("prioridad"));
        m.setEstadoMantenimiento(rs.getString("estado_mantenimiento"));
        try { m.setActivoCodigo(rs.getString("activo_codigo")); } catch (SQLException ignored) {}
        try { m.setTipoNombre(rs.getString("tipo_nombre")); } catch (SQLException ignored) {}
        return m;
    }
    private static final String BASE = """
        SELECT m.*, a.codigo_interno AS activo_codigo, tm.nombre_tipo AS tipo_nombre
        FROM mantenimiento m
        JOIN activo a ON a.id_activo = m.id_activo
        JOIN tipo_mantenimiento tm ON tm.id_tipo_mantenimiento = m.id_tipo_mantenimiento
        """;
    public List<Mantenimiento> listar(String filtro) throws SQLException {
        String sql = BASE + " WHERE (? = '' OR a.codigo_interno LIKE ? OR m.descripcion LIKE ?) ORDER BY m.fecha_programada DESC";
        List<Mantenimiento> out = new ArrayList<>();
        try (Connection c = ConexionDB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            String f=filtro==null?"":filtro.trim(); String like="%"+f+"%";
            ps.setString(1,f); ps.setString(2,like); ps.setString(3,like);
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) out.add(map(rs)); }
        }
        return out;
    }
    public void insertar(Mantenimiento m) throws SQLException {
        String sql = "INSERT INTO mantenimiento (id_activo,id_tipo_mantenimiento,id_ticket,id_usuario_solicitante,fecha_programada,descripcion,prioridad,estado_mantenimiento) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection c = ConexionDB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1,m.getIdActivo()); ps.setInt(2,m.getIdTipoMantenimiento());
            if (m.getIdTicket()==null) ps.setNull(3,Types.INTEGER); else ps.setInt(3,m.getIdTicket());
            if (m.getIdUsuarioSolicitante()==null) ps.setNull(4,Types.INTEGER); else ps.setInt(4,m.getIdUsuarioSolicitante());
            ps.setDate(5, m.getFechaProgramada()==null?null:java.sql.Date.valueOf(m.getFechaProgramada()));
            ps.setString(6,m.getDescripcion()); ps.setString(7,m.getPrioridad()); ps.setString(8,m.getEstadoMantenimiento());
            ps.executeUpdate();
        }
    }
    public void actualizar(Mantenimiento m) throws SQLException {
        String sql = "UPDATE mantenimiento SET id_tipo_mantenimiento=?, fecha_programada=?, fecha_realizacion=?, descripcion=?, prioridad=?, estado_mantenimiento=? WHERE id_mantenimiento=?";
        try (Connection c = ConexionDB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1,m.getIdTipoMantenimiento());
            ps.setDate(2, m.getFechaProgramada()==null?null:java.sql.Date.valueOf(m.getFechaProgramada()));
            ps.setTimestamp(3, m.getFechaRealizacion()==null?null:Timestamp.valueOf(m.getFechaRealizacion()));
            ps.setString(4,m.getDescripcion()); ps.setString(5,m.getPrioridad()); ps.setString(6,m.getEstadoMantenimiento());
            ps.setInt(7,m.getIdMantenimiento()); ps.executeUpdate();
        }
    }
    public void eliminar(int id) throws SQLException {
        try (Connection c = ConexionDB.get(); PreparedStatement ps = c.prepareStatement("DELETE FROM mantenimiento WHERE id_mantenimiento=?")) {
            ps.setInt(1,id); ps.executeUpdate();
        }
    }
}
