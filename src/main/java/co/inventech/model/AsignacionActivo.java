package co.inventech.model;
import java.time.LocalDateTime;
public class AsignacionActivo {
    private int idAsignacion, idActivo, idUsuarioResponsable;
    private Integer idArea;
    private LocalDateTime fechaAsignacion, fechaDevolucion;
    private String observaciones, estadoAsignacion="Activa";
    private String activoCodigo, usuarioNombre;
    public int getIdAsignacion(){return idAsignacion;} public void setIdAsignacion(int v){idAsignacion=v;}
    public int getIdActivo(){return idActivo;} public void setIdActivo(int v){idActivo=v;}
    public int getIdUsuarioResponsable(){return idUsuarioResponsable;} public void setIdUsuarioResponsable(int v){idUsuarioResponsable=v;}
    public Integer getIdArea(){return idArea;} public void setIdArea(Integer v){idArea=v;}
    public LocalDateTime getFechaAsignacion(){return fechaAsignacion;} public void setFechaAsignacion(LocalDateTime v){fechaAsignacion=v;}
    public LocalDateTime getFechaDevolucion(){return fechaDevolucion;} public void setFechaDevolucion(LocalDateTime v){fechaDevolucion=v;}
    public String getObservaciones(){return observaciones;} public void setObservaciones(String v){observaciones=v;}
    public String getEstadoAsignacion(){return estadoAsignacion;} public void setEstadoAsignacion(String v){estadoAsignacion=v;}
    public String getActivoCodigo(){return activoCodigo;} public void setActivoCodigo(String v){activoCodigo=v;}
    public String getUsuarioNombre(){return usuarioNombre;} public void setUsuarioNombre(String v){usuarioNombre=v;}
}
