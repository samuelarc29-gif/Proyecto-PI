package co.inventech.dao;
import co.inventech.db.ConexionDB;
import co.inventech.model.Ans;
import java.sql.*;
import java.util.*;
public class AnsDAO {
    private Ans map(ResultSet rs) throws SQLException {
        Ans a = new Ans();
        a.setIdAns(rs.getInt("id_ans"));
        a.setTipoSolicitud(rs.getString("tipo_solicitud"));
        a.setPrioridad(rs.getString("prioridad"));
        a.setTiempoRespuestaHoras(rs.getInt("tiempo_respuesta_horas"));
        a.setTiempoSolucionHoras(rs.getInt("tiempo_solucion_horas"));
        a.setEstadoAns(rs.getString("estado_ans"));
        return a;
    }
    public List<Ans> listar() throws SQLException {
        List<Ans> out = new ArrayList<>();
        try (Connection c = ConexionDB.get(); PreparedStatement ps = c.prepareStatement("SELECT * FROM ans ORDER BY prioridad");
             ResultSet rs = ps.executeQuery()) { while (rs.next()) out.add(map(rs)); }
        return out;
    }
    public void insertar(Ans a) throws SQLException {
        try (Connection c = ConexionDB.get(); PreparedStatement ps = c.prepareStatement(
                "INSERT INTO ans (tipo_solicitud,prioridad,tiempo_respuesta_horas,tiempo_solucion_horas,estado_ans) VALUES (?,?,?,?,?)")) {
            ps.setString(1,a.getTipoSolicitud()); ps.setString(2,a.getPrioridad());
            ps.setInt(3,a.getTiempoRespuestaHoras()); ps.setInt(4,a.getTiempoSolucionHoras());
            ps.setString(5,a.getEstadoAns()); ps.executeUpdate();
        }
    }
    public void actualizar(Ans a) throws SQLException {
        try (Connection c = ConexionDB.get(); PreparedStatement ps = c.prepareStatement(
                "UPDATE ans SET tipo_solicitud=?,prioridad=?,tiempo_respuesta_horas=?,tiempo_solucion_horas=?,estado_ans=? WHERE id_ans=?")) {
            ps.setString(1,a.getTipoSolicitud()); ps.setString(2,a.getPrioridad());
            ps.setInt(3,a.getTiempoRespuestaHoras()); ps.setInt(4,a.getTiempoSolucionHoras());
            ps.setString(5,a.getEstadoAns()); ps.setInt(6,a.getIdAns()); ps.executeUpdate();
        }
    }
    public void eliminar(int id) throws SQLException {
        try (Connection c = ConexionDB.get(); PreparedStatement ps = c.prepareStatement("DELETE FROM ans WHERE id_ans=?")) {
            ps.setInt(1,id); ps.executeUpdate();
        }
    }
}
