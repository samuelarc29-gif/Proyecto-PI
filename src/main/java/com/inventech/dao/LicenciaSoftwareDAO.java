package com.inventech.dao;

import com.inventech.config.ConexionBD;
import com.inventech.model.LicenciaSoftware;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LicenciaSoftwareDAO {

    private static final String SELECT_BASE =
            "SELECT l.id_licencia, l.id_activo, l.nombre_software, l.tipo_licencia, l.codigo_licencia, " +
            "       l.fecha_adquisicion, l.fecha_vencimiento, l.estado_licencia, l.observaciones, " +
            "       a.codigo_interno " +
            "FROM licencia_software l " +
            "INNER JOIN activo a ON a.id_activo = l.id_activo ";

    private LicenciaSoftware map(ResultSet rs) throws SQLException {
        LicenciaSoftware l = new LicenciaSoftware();
        l.setIdLicencia(rs.getInt("id_licencia"));
        l.setIdActivo(rs.getInt("id_activo"));
        l.setNombreSoftware(rs.getString("nombre_software"));
        l.setTipoLicencia(rs.getString("tipo_licencia"));
        l.setCodigoLicencia(rs.getString("codigo_licencia"));
        Date fa = rs.getDate("fecha_adquisicion");
        l.setFechaAdquisicion(fa == null ? null : fa.toLocalDate());
        Date fv = rs.getDate("fecha_vencimiento");
        l.setFechaVencimiento(fv == null ? null : fv.toLocalDate());
        l.setEstadoLicencia(rs.getString("estado_licencia"));
        l.setObservaciones(rs.getString("observaciones"));
        l.setCodigoActivo(rs.getString("codigo_interno"));
        return l;
    }

    public List<LicenciaSoftware> listar() throws SQLException {
        List<LicenciaSoftware> lista = new ArrayList<>();
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_BASE + " ORDER BY l.fecha_vencimiento");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        }
        return lista;
    }

    public int insertar(LicenciaSoftware l) throws SQLException {
        String sql = "INSERT INTO licencia_software (id_activo, nombre_software, tipo_licencia, codigo_licencia, " +
                "fecha_adquisicion, fecha_vencimiento, estado_licencia, observaciones) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, l.getIdActivo());
            ps.setString(2, l.getNombreSoftware());
            ps.setString(3, l.getTipoLicencia());
            ps.setString(4, l.getCodigoLicencia());
            ps.setDate(5, l.getFechaAdquisicion() == null ? null : Date.valueOf(l.getFechaAdquisicion()));
            ps.setDate(6, l.getFechaVencimiento() == null ? null : Date.valueOf(l.getFechaVencimiento()));
            ps.setString(7, l.getEstadoLicencia() == null ? "Activa" : l.getEstadoLicencia());
            ps.setString(8, l.getObservaciones());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) return rs.getInt(1); }
        }
        return -1;
    }

    public boolean actualizar(LicenciaSoftware l) throws SQLException {
        String sql = "UPDATE licencia_software SET id_activo=?, nombre_software=?, tipo_licencia=?, codigo_licencia=?, " +
                "fecha_adquisicion=?, fecha_vencimiento=?, estado_licencia=?, observaciones=? WHERE id_licencia=?";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, l.getIdActivo());
            ps.setString(2, l.getNombreSoftware());
            ps.setString(3, l.getTipoLicencia());
            ps.setString(4, l.getCodigoLicencia());
            ps.setDate(5, l.getFechaAdquisicion() == null ? null : Date.valueOf(l.getFechaAdquisicion()));
            ps.setDate(6, l.getFechaVencimiento() == null ? null : Date.valueOf(l.getFechaVencimiento()));
            ps.setString(7, l.getEstadoLicencia());
            ps.setString(8, l.getObservaciones());
            ps.setInt(9, l.getIdLicencia());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM licencia_software WHERE id_licencia=?";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
