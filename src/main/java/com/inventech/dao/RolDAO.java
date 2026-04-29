package com.inventech.dao;

import com.inventech.config.ConexionBD;
import com.inventech.model.Rol;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RolDAO {

    public List<Rol> listar() throws SQLException {
        List<Rol> lista = new ArrayList<>();
        String sql = "SELECT id_rol, nombre_rol, descripcion FROM rol ORDER BY nombre_rol";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Rol(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
        }
        return lista;
    }

    public Rol obtenerPorId(int idRol) throws SQLException {
        String sql = "SELECT id_rol, nombre_rol, descripcion FROM rol WHERE id_rol=?";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idRol);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new Rol(rs.getInt(1), rs.getString(2), rs.getString(3));
            }
        }
        return null;
    }
}
