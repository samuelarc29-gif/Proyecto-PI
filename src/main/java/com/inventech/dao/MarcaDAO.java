package com.inventech.dao;

import com.inventech.config.ConexionBD;
import com.inventech.model.Marca;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MarcaDAO {

    public List<Marca> listar() throws SQLException {
        List<Marca> lista = new ArrayList<>();
        String sql = "SELECT id_marca, nombre_marca, descripcion FROM marca ORDER BY nombre_marca";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(new Marca(rs.getInt(1), rs.getString(2), rs.getString(3)));
        }
        return lista;
    }

    public int insertar(Marca m) throws SQLException {
        String sql = "INSERT INTO marca (nombre_marca, descripcion) VALUES (?, ?)";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, m.getNombreMarca());
            ps.setString(2, m.getDescripcion());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) return rs.getInt(1); }
        }
        return -1;
    }

    public boolean actualizar(Marca m) throws SQLException {
        String sql = "UPDATE marca SET nombre_marca=?, descripcion=? WHERE id_marca=?";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, m.getNombreMarca());
            ps.setString(2, m.getDescripcion());
            ps.setInt(3, m.getIdMarca());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM marca WHERE id_marca=?";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
