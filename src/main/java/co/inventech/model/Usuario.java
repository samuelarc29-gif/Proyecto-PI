package co.inventech.model;
import java.time.LocalDateTime;
public class Usuario {
    private int idUsuario; private int idRol; private Integer idArea;
    private String nombreCompleto, documento, cargo, correo, telefono, nombreUsuario, contrasenaHash;
    private String estadoUsuario="Activo"; private LocalDateTime fechaCreacion;
    // Derivados para joins
    private String rolNombre, areaNombre;
    public int getIdUsuario(){return idUsuario;} public void setIdUsuario(int v){idUsuario=v;}
    public int getIdRol(){return idRol;} public void setIdRol(int v){idRol=v;}
    public Integer getIdArea(){return idArea;} public void setIdArea(Integer v){idArea=v;}
    public String getNombreCompleto(){return nombreCompleto;} public void setNombreCompleto(String v){nombreCompleto=v;}
    public String getDocumento(){return documento;} public void setDocumento(String v){documento=v;}
    public String getCargo(){return cargo;} public void setCargo(String v){cargo=v;}
    public String getCorreo(){return correo;} public void setCorreo(String v){correo=v;}
    public String getTelefono(){return telefono;} public void setTelefono(String v){telefono=v;}
    public String getNombreUsuario(){return nombreUsuario;} public void setNombreUsuario(String v){nombreUsuario=v;}
    public String getContrasenaHash(){return contrasenaHash;} public void setContrasenaHash(String v){contrasenaHash=v;}
    public String getEstadoUsuario(){return estadoUsuario;} public void setEstadoUsuario(String v){estadoUsuario=v;}
    public LocalDateTime getFechaCreacion(){return fechaCreacion;} public void setFechaCreacion(LocalDateTime v){fechaCreacion=v;}
    public String getRolNombre(){return rolNombre;} public void setRolNombre(String v){rolNombre=v;}
    public String getAreaNombre(){return areaNombre;} public void setAreaNombre(String v){areaNombre=v;}
    @Override public String toString(){return nombreCompleto;}
}
