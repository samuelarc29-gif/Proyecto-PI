package com.inventech.dao;

import com.inventech.config.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Reportes básicos. Ejecuta consultas agregadas sobre la base de datos
 * y devuelve estructuras tabulares listas para mostrar en JTable.
 */
public class ReporteDAO {

    public static class Tabla {
        public final List<String> columnas;
        public final List<Object[]> filas;
        public Tabla(List<String> columnas, List<Object[]> filas) {
            this.columnas = columnas; this.filas = filas;
        }
    }

    public Tabla activosPorEstado() throws SQLException {
        String sql = "SELECT ea.nombre_estado, COUNT(*) AS total " +
                "FROM activo a INNER JOIN estado_activo ea ON ea.id_estado_activo = a.id_estado_activo " +
                "GROUP BY ea.nombre_estado ORDER BY total DESC";
        return ejecutar(sql, List.of("Estado", "Total"));
    }

    public Tabla activosPorArea() throws SQLException {
        String sql = "SELECT COALESCE(ar.nombre_area, 'Sin área') AS area, COUNT(*) AS total " +
                "FROM activo a LEFT JOIN area ar ON ar.id_area = a.id_area " +
                "GROUP BY area ORDER BY total DESC";
        return ejecutar(sql, List.of("Área", "Total"));
    }

    public Tabla ticketsPorEstado() throws SQLException {
        String sql = "SELECT et.nombre_estado, COUNT(*) AS total " +
                "FROM ticket t INNER JOIN estado_ticket et ON et.id_estado_ticket = t.id_estado_ticket " +
                "GROUP BY et.nombre_estado ORDER BY total DESC";
        return ejecutar(sql, List.of("Estado", "Total"));
    }

    public Tabla ticketsPorPrioridad() throws SQLException {
        String sql = "SELECT COALESCE(prioridad, 'Sin prioridad') AS prioridad, COUNT(*) AS total " +
                "FROM ticket GROUP BY prioridad ORDER BY total DESC";
        return ejecutar(sql, List.of("Prioridad", "Total"));
    }

    public Tabla licenciasPorVencer() throws SQLException {
        String sql = "SELECT l.nombre_software, l.codigo_licencia, l.fecha_vencimiento, l.estado_licencia " +
                "FROM licencia_software l " +
                "WHERE l.fecha_vencimiento IS NOT NULL AND l.fecha_vencimiento <= CURDATE() + INTERVAL 30 DAY " +
                "ORDER BY l.fecha_vencimiento";
        return ejecutar(sql, List.of("Software", "Código", "Vence", "Estado"));
    }

    public Tabla mantenimientosPorTipo() throws SQLException {
        String sql = "SELECT tm.nombre_tipo, COUNT(*) AS total " +
                "FROM mantenimiento m INNER JOIN tipo_mantenimiento tm ON tm.id_tipo_mantenimiento = m.id_tipo_mantenimiento " +
                "GROUP BY tm.nombre_tipo ORDER BY total DESC";
        return ejecutar(sql, List.of("Tipo", "Total"));
    }

    public Map<String, Tabla> reportesDisponibles() {
        Map<String, Tabla> m = new LinkedHashMap<>();
        m.put("Activos por estado", null);
        m.put("Activos por área", null);
        m.put("Tickets por estado", null);
        m.put("Tickets por prioridad", null);
        m.put("Licencias por vencer (30 días)", null);
        m.put("Mantenimientos por tipo", null);
        return m;
    }

    public Tabla ejecutarReporte(String nombre) throws SQLException {
        return switch (nombre) {
            case "Activos por estado" -> activosPorEstado();
            case "Activos por área" -> activosPorArea();
            case "Tickets por estado" -> ticketsPorEstado();
            case "Tickets por prioridad" -> ticketsPorPrioridad();
            case "Licencias por vencer (30 días)" -> licenciasPorVencer();
            case "Mantenimientos por tipo" -> mantenimientosPorTipo();
            default -> {
                List<Object[]> filas = new ArrayList<>();
                filas.add(new Object[]{"Reporte no disponible"});
                yield new Tabla(List.of("Mensaje"), filas);
            }
        };
    }

    private Tabla ejecutar(String sql, List<String> cols) throws SQLException {
        List<Object[]> filas = new ArrayList<>();
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            int n = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Object[] f = new Object[n];
                for (int i = 0; i < n; i++) f[i] = rs.getObject(i + 1);
                filas.add(f);
            }
        }
        return new Tabla(cols, filas);
    }
}
