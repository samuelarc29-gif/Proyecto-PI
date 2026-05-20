package co.inventech.dao;
import co.inventech.db.ConexionDB;
import java.sql.*;
import java.util.*;

public class DashboardDAO {
    public Map<String,Integer> kpis() throws SQLException {
        Map<String,Integer> m = new LinkedHashMap<>();
        m.put("Total activos", count("SELECT COUNT(*) FROM activo"));
        m.put("En mantenimiento", count("SELECT COUNT(*) FROM activo a JOIN estado_activo e ON e.id_estado_activo=a.id_estado_activo WHERE e.nombre_estado='En mantenimiento'"));
        m.put("Tickets abiertos", count("SELECT COUNT(*) FROM ticket t JOIN estado_ticket e ON e.id_estado_ticket=t.id_estado_ticket WHERE e.nombre_estado IN ('Abierto','Asignado','En proceso')"));
        m.put("Tickets cerrados", count("SELECT COUNT(*) FROM ticket t JOIN estado_ticket e ON e.id_estado_ticket=t.id_estado_ticket WHERE e.nombre_estado IN ('Cerrado usuario','Cerrado técnico')"));
        m.put("Licencias por vencer", count("SELECT COUNT(*) FROM licencia_software WHERE fecha_vencimiento IS NOT NULL AND fecha_vencimiento <= CURDATE() + INTERVAL 30 DAY AND estado_licencia='Activa'"));
        m.put("Mantenimientos programados", count("SELECT COUNT(*) FROM mantenimiento WHERE estado_mantenimiento='Programado'"));
        m.put("Usuarios activos", count("SELECT COUNT(*) FROM usuario WHERE estado_usuario='Activo'"));
        m.put("Proveedores activos", count("SELECT COUNT(*) FROM proveedor WHERE estado_proveedor='Activo'"));
        return m;
    }
    public LinkedHashMap<String,Integer> activosPorArea() throws SQLException {
        LinkedHashMap<String,Integer> out = new LinkedHashMap<>();
        String sql = "SELECT COALESCE(a.nombre_area,'Sin área') area, COUNT(*) c FROM activo ac LEFT JOIN area a ON a.id_area=ac.id_area GROUP BY area ORDER BY c DESC";
        try (Connection c = ConexionDB.get(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.put(rs.getString(1), rs.getInt(2));
        }
        return out;
    }
    public LinkedHashMap<String,Integer> ticketsPorEstado() throws SQLException {
        LinkedHashMap<String,Integer> out = new LinkedHashMap<>();
        String sql = "SELECT e.nombre_estado, COUNT(t.id_ticket) c FROM estado_ticket e LEFT JOIN ticket t ON t.id_estado_ticket=e.id_estado_ticket GROUP BY e.nombre_estado ORDER BY e.id_estado_ticket";
        try (Connection c = ConexionDB.get(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.put(rs.getString(1), rs.getInt(2));
        }
        return out;
    }
    private int count(String sql) throws SQLException {
        try (Connection c = ConexionDB.get(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
}
