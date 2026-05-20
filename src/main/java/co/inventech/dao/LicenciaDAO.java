package co.inventech.dao;

import co.inventech.db.ConexionDB;
import co.inventech.model.LicenciaSoftware;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class LicenciaDAO {
    private LicenciaSoftware map(ResultSet rs) throws SQLException {
        LicenciaSoftware l = new LicenciaSoftware();
        l.setIdLicencia(rs.getInt("id_licencia"));
        int a = rs.getInt("id_activo"); l.setIdActivo(rs.wasNull()?null:a);
        l.setNombreSoftware(rs.getString("nombre_software"));
        l.setTipoLicencia(rs.getString("tipo_licencia"));
        l.setCodigoLicencia(rs.getString("codigo_licencia"));
        java.sql.Date fa = rs.getDate("fecha_adquisicion"); if (fa!=null) l.setFechaAdquisicion(fa.toLocalDate());
        java.sql.Date fv = rs.getDate("fecha_vencimiento"); if (fv!=null) l.setFechaVencimiento(fv.toLocalDate());
        l.setEstadoLicencia(rs.getString("estado_licencia"));
        l.setObservaciones(rs.getString("observaciones"));
        try { l.setActivoCodigo(rs.getString("activo_codigo")); } catch (SQLException ignored) {}
        return l;
    }
    public List<LicenciaSoftware> listar(String filtro) throws SQLException {
        String sql = "SELECT l.*, a.codigo_interno AS activo_codigo FROM licencia_software l LEFT JOIN activo a ON a.id_activo = l.id_activo WHERE (? = '' OR l.nombre_software LIKE ? OR l.codigo_licencia LIKE ?) ORDER BY l.fecha_vencimiento";
        List<LicenciaSoftware> out = new ArrayList<>();
        try (Connection c = ConexionDB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            String f=filtro==null?"":filtro.trim(); String like="%"+f+"%";
            ps.setString(1,f); ps.setString(2,like); ps.setString(3,like);
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) out.add(map(rs)); }
        }
        return out;
    }
    public void insertar(LicenciaSoftware l) throws SQLException {
        String sql = "INSERT INTO licencia_software (id_activo,nombre_software,tipo_licencia,codigo_licencia,fecha_adquisicion,fecha_vencimiento,estado_licencia,observaciones) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection c = ConexionDB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            if (l.getIdActivo()==null) ps.setNull(1,Types.INTEGER); else ps.setInt(1,l.getIdActivo());
            ps.setString(2,l.getNombreSoftware()); ps.setString(3,l.getTipoLicencia()); ps.setString(4,l.getCodigoLicencia());
            ps.setDate(5,l.getFechaAdquisicion()==null?null:java.sql.Date.valueOf(l.getFechaAdquisicion()));
            ps.setDate(6,l.getFechaVencimiento()==null?null:java.sql.Date.valueOf(l.getFechaVencimiento()));
            ps.setString(7,l.getEstadoLicencia()); ps.setString(8,l.getObservaciones());
            ps.executeUpdate();
        }
    }
    public void actualizar(LicenciaSoftware l) throws SQLException {
        String sql = "UPDATE licencia_software SET id_activo=?,nombre_software=?,tipo_licencia=?,codigo_licencia=?,fecha_adquisicion=?,fecha_vencimiento=?,estado_licencia=?,observaciones=? WHERE id_licencia=?";
        try (Connection c = ConexionDB.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            if (l.getIdActivo()==null) ps.setNull(1,Types.INTEGER); else ps.setInt(1,l.getIdActivo());
            ps.setString(2,l.getNombreSoftware()); ps.setString(3,l.getTipoLicencia()); ps.setString(4,l.getCodigoLicencia());
            ps.setDate(5,l.getFechaAdquisicion()==null?null:java.sql.Date.valueOf(l.getFechaAdquisicion()));
            ps.setDate(6,l.getFechaVencimiento()==null?null:java.sql.Date.valueOf(l.getFechaVencimiento()));
            ps.setString(7,l.getEstadoLicencia()); ps.setString(8,l.getObservaciones()); ps.setInt(9,l.getIdLicencia());
            ps.executeUpdate();
        }
    }
    public void eliminar(int id) throws SQLException {
        try (Connection c = ConexionDB.get(); PreparedStatement ps = c.prepareStatement("DELETE FROM licencia_software WHERE id_licencia=?")) {
            ps.setInt(1,id); ps.executeUpdate();
        }
    }
}
