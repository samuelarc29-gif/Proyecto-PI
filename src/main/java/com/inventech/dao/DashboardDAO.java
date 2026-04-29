package com.inventech.dao;

import com.inventech.config.ConexionBD;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/** Consultas agregadas para el dashboard. */
public class DashboardDAO {

    public Map<String, Integer> obtenerResumen() throws SQLException {
        Map<String, Integer> r = new HashMap<>();
        String[][] consultas = {
                {"totalActivos", "SELECT COUNT(*) FROM activo"},
                {"disponibles",  "SELECT COUNT(*) FROM activo a JOIN estado_activo e ON e.id_estado_activo=a.id_estado_activo WHERE e.nombre_estado='Disponible'"},
                {"asignados",    "SELECT COUNT(*) FROM activo a JOIN estado_activo e ON e.id_estado_activo=a.id_estado_activo WHERE e.nombre_estado='Asignado'"},
                {"mantenimiento","SELECT COUNT(*) FROM activo a JOIN estado_activo e ON e.id_estado_activo=a.id_estado_activo WHERE e.nombre_estado='En mantenimiento'"},
                {"ticketsAbiertos", "SELECT COUNT(*) FROM ticket t JOIN estado_ticket e ON e.id_estado_ticket=t.id_estado_ticket WHERE e.nombre_estado='Abierto'"},
                {"ticketsProceso",  "SELECT COUNT(*) FROM ticket t JOIN estado_ticket e ON e.id_estado_ticket=t.id_estado_ticket WHERE e.nombre_estado='En proceso'"},
                {"licenciasPorVencer", "SELECT COUNT(*) FROM licencia_software WHERE fecha_vencimiento IS NOT NULL AND fecha_vencimiento BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 30 DAY)"}
        };
        try (Connection c = ConexionBD.getConnection()) {
            for (String[] q : consultas) {
                try (PreparedStatement ps = c.prepareStatement(q[1]);
                     ResultSet rs = ps.executeQuery()) {
                    r.put(q[0], rs.next() ? rs.getInt(1) : 0);
                }
            }
        }
        return r;
    }
}
