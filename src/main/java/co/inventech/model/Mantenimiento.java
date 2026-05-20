package co.inventech.model;
import java.time.LocalDate; import java.time.LocalDateTime;
public class Mantenimiento {
    private int idMantenimiento, idActivo, idTipoMantenimiento;
    private Integer idTicket, idUsuarioSolicitante;
    private LocalDate fechaProgramada; private LocalDateTime fechaRealizacion;
    private String descripcion, prioridad="Media", estadoMantenimiento="Programado";
    private String activoCodigo, tipoNombre;
    public int getIdMantenimiento(){return idMantenimiento;} public void setIdMantenimiento(int v){idMantenimiento=v;}
    public int getIdActivo(){return idActivo;} public void setIdActivo(int v){idActivo=v;}
    public int getIdTipoMantenimiento(){return idTipoMantenimiento;} public void setIdTipoMantenimiento(int v){idTipoMantenimiento=v;}
    public Integer getIdTicket(){return idTicket;} public void setIdTicket(Integer v){idTicket=v;}
    public Integer getIdUsuarioSolicitante(){return idUsuarioSolicitante;} public void setIdUsuarioSolicitante(Integer v){idUsuarioSolicitante=v;}
    public LocalDate getFechaProgramada(){return fechaProgramada;} public void setFechaProgramada(LocalDate v){fechaProgramada=v;}
    public LocalDateTime getFechaRealizacion(){return fechaRealizacion;} public void setFechaRealizacion(LocalDateTime v){fechaRealizacion=v;}
    public String getDescripcion(){return descripcion;} public void setDescripcion(String v){descripcion=v;}
    public String getPrioridad(){return prioridad;} public void setPrioridad(String v){prioridad=v;}
    public String getEstadoMantenimiento(){return estadoMantenimiento;} public void setEstadoMantenimiento(String v){estadoMantenimiento=v;}
    public String getActivoCodigo(){return activoCodigo;} public void setActivoCodigo(String v){activoCodigo=v;}
    public String getTipoNombre(){return tipoNombre;} public void setTipoNombre(String v){tipoNombre=v;}
}
