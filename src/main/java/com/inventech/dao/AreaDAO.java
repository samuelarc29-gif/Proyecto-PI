package com.inventech.dao;

import com.inventech.config.ConexionBD;
import com.inventech.model.Area;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AreaDAO {

    public List<Area> listar() throws SQLException {
        List<Area> lista = new ArrayList<>();
        String sql = "SELECT id_area, nombre_area, descripcion FROM area ORDER BY nombre_area";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Area(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
        }
        return lista;
    }
}
