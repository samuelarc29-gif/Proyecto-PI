package com.inventech.dao;

import com.inventech.config.ConexionBD;
import com.inventech.model.Activo;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ActivoDAO {

    private static final String SELECT_BASE =
            "SELECT a.id_activo, a.id_tipo_activo, a.id_marca, a.id_proveedor, a.id_estado_activo, a.id_area, " +
            "       a.modelo, a.numero_serie, a.codigo_interno, a.fecha_adquisicion, a.fecha_garantia, a.observaciones, " +
            "       ta.nombre_tipo, m.nombre_marca, p.nombre_proveedor, ea.nombre_estado, ar.nombre_area " +
            "FROM activo a " +
            "INNER JOIN tipo_activo ta ON ta.id_tipo_activo = a.id_tipo_activo " +
            "INNER JOIN marca m ON m.id_marca = a.id_marca " +
            "LEFT JOIN proveedor p ON p.id_proveedor = a.id_proveedor " +
            "INNER JOIN estado_activo ea ON ea.id_estado_activo = a.id_estado_activo " +
            "LEFT JOIN area ar ON ar.id_area = a.id_area ";

    private Activo map(ResultSet rs) throws SQLException {
        Activo a = new Activo();
        a.setIdActivo(rs.getInt("id_activo"));
        a.setIdTipoActivo(rs.getInt("id_tipo_activo"));
        a.setIdMarca(rs.getInt("id_marca"));
        int idProv = rs.getInt("id_proveedor");
        a.setIdProveedor(rs.wasNull() ? null : idProv);
        a.setIdEstadoActivo(rs.getInt("id_estado_activo"));
        int idArea = rs.getInt("id_area");
        a.setIdArea(rs.wasNull() ? null : idArea);
        a.setModelo(rs.getString("modelo"));
        a.setNumeroSerie(rs.getString("numero_serie"));
        a.setCodigoInterno(rs.getString("codigo_interno"));
        Date fa = rs.getDate("fecha_adquisicion");
        a.setFechaAdquisicion(fa == null ? null : fa.toLocalDate());
        Date fg = rs.getDate("fecha_garantia");
        a.setFechaGarantia(fg == null ? null : fg.toLocalDate());
        a.setObservaciones(rs.getString("observaciones"));
        a.setNombreTipo(rs.getString("nombre_tipo"));
        a.setNombreMarca(rs.getString("nombre_marca"));
        a.setNombreProveedor(rs.getString("nombre_proveedor"));
        a.setNombreEstado(rs.getString("nombre_estado"));
        a.setNombreArea(rs.getString("nombre_area"));
        return a;
    }

    public List<Activo> listar() throws SQLException {
        List<Activo> list = new ArrayList<>();
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_BASE + " ORDER BY a.codigo_interno");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public List<Activo> listarPorArea(int idArea) throws SQLException {
        List<Activo> list = new ArrayList<>();
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_BASE + " WHERE a.id_area = ? ORDER BY a.codigo_interno")) {
            ps.setInt(1, idArea);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public int insertar(Activo a) throws SQLException {
        String sql = "INSERT INTO activo (id_tipo_activo, id_marca, id_proveedor, id_estado_activo, id_area, " +
                "modelo, numero_serie, codigo_interno, fecha_adquisicion, fecha_garantia, observaciones) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, a.getIdTipoActivo());
            ps.setInt(2, a.getIdMarca());
            if (a.getIdProveedor() == null) ps.setNull(3, Types.INTEGER); else ps.setInt(3, a.getIdProveedor());
            ps.setInt(4, a.getIdEstadoActivo());
            if (a.getIdArea() == null) ps.setNull(5, Types.INTEGER); else ps.setInt(5, a.getIdArea());
            ps.setString(6, a.getModelo());
            ps.setString(7, a.getNumeroSerie());
            ps.setString(8, a.getCodigoInterno());
            ps.setDate(9, a.getFechaAdquisicion() == null ? null : Date.valueOf(a.getFechaAdquisicion()));
            ps.setDate(10, a.getFechaGarantia() == null ? null : Date.valueOf(a.getFechaGarantia()));
            ps.setString(11, a.getObservaciones());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) return rs.getInt(1); }
        }
        return -1;
    }

    public boolean actualizar(Activo a) throws SQLException {
        String sql = "UPDATE activo SET id_tipo_activo=?, id_marca=?, id_proveedor=?, id_estado_activo=?, id_area=?, " +
                "modelo=?, numero_serie=?, codigo_interno=?, fecha_adquisicion=?, fecha_garantia=?, observaciones=? " +
                "WHERE id_activo=?";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, a.getIdTipoActivo());
            ps.setInt(2, a.getIdMarca());
            if (a.getIdProveedor() == null) ps.setNull(3, Types.INTEGER); else ps.setInt(3, a.getIdProveedor());
            ps.setInt(4, a.getIdEstadoActivo());
            if (a.getIdArea() == null) ps.setNull(5, Types.INTEGER); else ps.setInt(5, a.getIdArea());
            ps.setString(6, a.getModelo());
            ps.setString(7, a.getNumeroSerie());
            ps.setString(8, a.getCodigoInterno());
            ps.setDate(9, a.getFechaAdquisicion() == null ? null : Date.valueOf(a.getFechaAdquisicion()));
            ps.setDate(10, a.getFechaGarantia() == null ? null : Date.valueOf(a.getFechaGarantia()));
            ps.setString(11, a.getObservaciones());
            ps.setInt(12, a.getIdActivo());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM activo WHERE id_activo=?";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    /** Cambia el estado del activo a uno indicado por nombre. */
    public boolean cambiarEstadoPorNombre(int idActivo, String nombreEstado) throws SQLException {
        String sql = "UPDATE activo SET id_estado_activo = (SELECT id_estado_activo FROM estado_activo WHERE nombre_estado=?) WHERE id_activo=?";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nombreEstado);
            ps.setInt(2, idActivo);
            return ps.executeUpdate() > 0;
        }
    }
}
