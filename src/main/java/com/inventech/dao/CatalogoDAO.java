package com.inventech.dao;

import com.inventech.config.ConexionBD;
import com.inventech.model.Catalogo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO genérico para tablas con esquema (id, nombre, descripcion).
 * Sirve para: rol, area, tipo_activo, estado_activo, tipo_mantenimiento, estado_ticket, permiso.
 *
 * Configuración: indique nombre de tabla, columna id, columna nombre y columna descripcion.
 */
public class CatalogoDAO {

    private final String tabla;
    private final String colId;
    private final String colNombre;
    private final String colDescripcion;

    public CatalogoDAO(String tabla, String colId, String colNombre, String colDescripcion) {
        this.tabla = tabla;
        this.colId = colId;
        this.colNombre = colNombre;
        this.colDescripcion = colDescripcion;
    }

    public List<Catalogo> listar() throws SQLException {
        List<Catalogo> lista = new ArrayList<>();
        String sql = "SELECT " + colId + ", " + colNombre + ", " + colDescripcion +
                " FROM " + tabla + " ORDER BY " + colNombre;
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Catalogo(
                        rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
        }
        return lista;
    }

    public int insertar(Catalogo c) throws SQLException {
        String sql = "INSERT INTO " + tabla + " (" + colNombre + ", " + colDescripcion + ") VALUES (?, ?)";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getDescripcion());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public boolean actualizar(Catalogo c) throws SQLException {
        String sql = "UPDATE " + tabla + " SET " + colNombre + "=?, " + colDescripcion + "=? WHERE " + colId + "=?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getDescripcion());
            ps.setInt(3, c.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM " + tabla + " WHERE " + colId + "=?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
