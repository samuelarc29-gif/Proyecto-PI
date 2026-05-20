package co.inventech.model;
import java.time.LocalDateTime;
public class Ticket {
    private int idTicket, idActivo, idUsuarioSolicitante, idEstadoTicket;
    private Integer idAns;
    private String descripcionFalla, prioridad="Media", observacionCierre;
    private LocalDateTime fechaReporte, fechaCierreUsuario;
    private String activoCodigo, solicitanteNombre, estadoNombre, tecnicoAsignado;
    public int getIdTicket(){return idTicket;} public void setIdTicket(int v){idTicket=v;}
    public int getIdActivo(){return idActivo;} public void setIdActivo(int v){idActivo=v;}
    public int getIdUsuarioSolicitante(){return idUsuarioSolicitante;} public void setIdUsuarioSolicitante(int v){idUsuarioSolicitante=v;}
    public int getIdEstadoTicket(){return idEstadoTicket;} public void setIdEstadoTicket(int v){idEstadoTicket=v;}
    public Integer getIdAns(){return idAns;} public void setIdAns(Integer v){idAns=v;}
    public String getDescripcionFalla(){return descripcionFalla;} public void setDescripcionFalla(String v){descripcionFalla=v;}
    public String getPrioridad(){return prioridad;} public void setPrioridad(String v){prioridad=v;}
    public String getObservacionCierre(){return observacionCierre;} public void setObservacionCierre(String v){observacionCierre=v;}
    public LocalDateTime getFechaReporte(){return fechaReporte;} public void setFechaReporte(LocalDateTime v){fechaReporte=v;}
    public LocalDateTime getFechaCierreUsuario(){return fechaCierreUsuario;} public void setFechaCierreUsuario(LocalDateTime v){fechaCierreUsuario=v;}
    public String getActivoCodigo(){return activoCodigo;} public void setActivoCodigo(String v){activoCodigo=v;}
    public String getSolicitanteNombre(){return solicitanteNombre;} public void setSolicitanteNombre(String v){solicitanteNombre=v;}
    public String getEstadoNombre(){return estadoNombre;} public void setEstadoNombre(String v){estadoNombre=v;}
    public String getTecnicoAsignado(){return tecnicoAsignado;} public void setTecnicoAsignado(String v){tecnicoAsignado=v;}
}
