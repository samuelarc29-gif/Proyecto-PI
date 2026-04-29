package com.inventech.dao;

import com.inventech.config.ConexionBD;
import com.inventech.model.Mantenimiento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MantenimientoDAO {

    private static final String SELECT_BASE =
            "SELECT m.id_mantenimiento, m.id_ticket, m.id_tipo_mantenimiento, m.fecha_programada, m.observaciones, " +
            "       t.descripcion_falla, tm.nombre_tipo " +
            "FROM mantenimiento m " +
            "INNER JOIN ticket t ON t.id_ticket = m.id_ticket " +
            "INNER JOIN tipo_mantenimiento tm ON tm.id_tipo_mantenimiento = m.id_tipo_mantenimiento ";

    private Mantenimiento map(ResultSet rs) throws SQLException {
        Mantenimiento m = new Mantenimiento();
        m.setIdMantenimiento(rs.getInt("id_mantenimiento"));
        m.setIdTicket(rs.getInt("id_ticket"));
        m.setIdTipoMantenimiento(rs.getInt("id_tipo_mantenimiento"));
        Date fp = rs.getDate("fecha_programada");
        m.setFechaProgramada(fp == null ? null : fp.toLocalDate());
        m.setObservaciones(rs.getString("observaciones"));
        m.setDescripcionTicket(rs.getString("descripcion_falla"));
        m.setNombreTipo(rs.getString("nombre_tipo"));
        return m;
    }

    public List<Mantenimiento> listar() throws SQLException {
        List<Mantenimiento> lista = new ArrayList<>();
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_BASE + " ORDER BY m.fecha_programada DESC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        }
        return lista;
    }

    public int insertar(Mantenimiento m) throws SQLException {
        String sql = "INSERT INTO mantenimiento (id_ticket, id_tipo_mantenimiento, fecha_programada, observaciones) VALUES (?, ?, ?, ?)";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, m.getIdTicket());
            ps.setInt(2, m.getIdTipoMantenimiento());
            ps.setDate(3, m.getFechaProgramada() == null ? null : Date.valueOf(m.getFechaProgramada()));
            ps.setString(4, m.getObservaciones());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) return rs.getInt(1); }
        }
        return -1;
    }

    public boolean actualizar(Mantenimiento m) throws SQLException {
        String sql = "UPDATE mantenimiento SET id_ticket=?, id_tipo_mantenimiento=?, fecha_programada=?, observaciones=? WHERE id_mantenimiento=?";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, m.getIdTicket());
            ps.setInt(2, m.getIdTipoMantenimiento());
            ps.setDate(3, m.getFechaProgramada() == null ? null : Date.valueOf(m.getFechaProgramada()));
            ps.setString(4, m.getObservaciones());
            ps.setInt(5, m.getIdMantenimiento());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM mantenimiento WHERE id_mantenimiento=?";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
