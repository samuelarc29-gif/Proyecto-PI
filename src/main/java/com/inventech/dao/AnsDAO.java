package com.inventech.dao;

import com.inventech.config.ConexionBD;
import com.inventech.model.Ans;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnsDAO {

    public List<Ans> listar() throws SQLException {
        List<Ans> lista = new ArrayList<>();
        String sql = "SELECT id_ans, tipo_solicitud, prioridad, tiempo_respuesta_horas, tiempo_solucion_horas, descripcion " +
                "FROM ans ORDER BY tipo_solicitud, prioridad";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Ans a = new Ans();
                a.setIdAns(rs.getInt("id_ans"));
                a.setTipoSolicitud(rs.getString("tipo_solicitud"));
                a.setPrioridad(rs.getString("prioridad"));
                a.setTiempoRespuestaHoras(rs.getInt("tiempo_respuesta_horas"));
                a.setTiempoSolucionHoras(rs.getInt("tiempo_solucion_horas"));
                a.setDescripcion(rs.getString("descripcion"));
                lista.add(a);
            }
        }
        return lista;
    }

    public int insertar(Ans a) throws SQLException {
        String sql = "INSERT INTO ans (tipo_solicitud, prioridad, tiempo_respuesta_horas, tiempo_solucion_horas, descripcion) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, a.getTipoSolicitud());
            ps.setString(2, a.getPrioridad());
            ps.setInt(3, a.getTiempoRespuestaHoras());
            ps.setInt(4, a.getTiempoSolucionHoras());
            ps.setString(5, a.getDescripcion());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) return rs.getInt(1); }
        }
        return -1;
    }

    public boolean actualizar(Ans a) throws SQLException {
        String sql = "UPDATE ans SET tipo_solicitud=?, prioridad=?, tiempo_respuesta_horas=?, tiempo_solucion_horas=?, descripcion=? WHERE id_ans=?";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, a.getTipoSolicitud());
            ps.setString(2, a.getPrioridad());
            ps.setInt(3, a.getTiempoRespuestaHoras());
            ps.setInt(4, a.getTiempoSolucionHoras());
            ps.setString(5, a.getDescripcion());
            ps.setInt(6, a.getIdAns());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM ans WHERE id_ans=?";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
