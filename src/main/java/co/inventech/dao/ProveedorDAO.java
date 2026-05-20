package co.inventech.dao;

import co.inventech.db.ConexionDB;
import co.inventech.model.Proveedor;
import java.sql.*;
import java.util.*;

public class ProveedorDAO {
    private Proveedor map(ResultSet rs) throws SQLException {
        Proveedor p = new Proveedor();
        p.setIdProveedor(rs.getInt("id_proveedor"));
        p.setNombreProveedor(rs.getString("nombre_proveedor"));
        p.setContacto(rs.getString("contacto"));
        p.setTelefono(rs.getString("telefono"));
        p.setCorreo(rs.getString("correo"));
        p.setDireccion(rs.getString("direccion"));
        p.setDescripcionSoporte(rs.getString("descripcion_soporte"));
        p.setEstadoProveedor(rs.getString("estado_proveedor"));
        return p;
    }
    public List<Proveedor> listar(String filtro) throws SQLException {
        String sql = "SELECT * FROM proveedor WHERE (? = '' OR nombre_proveedor LIKE ? OR contacto LIKE ?) ORDER BY nombre_proveedor";
        List<Proveedor> out = new ArrayList<>();
        try (Connection c = ConexionDB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            String f = filtro==null?"":filtro.trim(); String like = "%"+f+"%";
            ps.setString(1,f); ps.setString(2,like); ps.setString(3,like);
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) out.add(map(rs)); }
        }
        return out;
    }
    public void insertar(Proveedor p) throws SQLException {
        String sql = "INSERT INTO proveedor (nombre_proveedor,contacto,telefono,correo,direccion,descripcion_soporte,estado_proveedor) VALUES (?,?,?,?,?,?,?)";
        try (Connection c = ConexionDB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1,p.getNombreProveedor()); ps.setString(2,p.getContacto()); ps.setString(3,p.getTelefono());
            ps.setString(4,p.getCorreo()); ps.setString(5,p.getDireccion()); ps.setString(6,p.getDescripcionSoporte());
            ps.setString(7,p.getEstadoProveedor()); ps.executeUpdate();
        }
    }
    public void actualizar(Proveedor p) throws SQLException {
        String sql = "UPDATE proveedor SET nombre_proveedor=?,contacto=?,telefono=?,correo=?,direccion=?,descripcion_soporte=?,estado_proveedor=? WHERE id_proveedor=?";
        try (Connection c = ConexionDB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1,p.getNombreProveedor()); ps.setString(2,p.getContacto()); ps.setString(3,p.getTelefono());
            ps.setString(4,p.getCorreo()); ps.setString(5,p.getDireccion()); ps.setString(6,p.getDescripcionSoporte());
            ps.setString(7,p.getEstadoProveedor()); ps.setInt(8,p.getIdProveedor()); ps.executeUpdate();
        }
    }
    public void eliminar(int id) throws SQLException {
        try (Connection c = ConexionDB.get(); PreparedStatement ps = c.prepareStatement("DELETE FROM proveedor WHERE id_proveedor=?")) {
            ps.setInt(1,id); ps.executeUpdate();
        }
    }
}
