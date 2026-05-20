package co.inventech.model;
import java.time.LocalDate;
public class LicenciaSoftware {
    private int idLicencia; private Integer idActivo;
    private String nombreSoftware, tipoLicencia, codigoLicencia, observaciones;
    private LocalDate fechaAdquisicion, fechaVencimiento;
    private String estadoLicencia="Activa";
    private String activoCodigo;
    public int getIdLicencia(){return idLicencia;} public void setIdLicencia(int v){idLicencia=v;}
    public Integer getIdActivo(){return idActivo;} public void setIdActivo(Integer v){idActivo=v;}
    public String getNombreSoftware(){return nombreSoftware;} public void setNombreSoftware(String v){nombreSoftware=v;}
    public String getTipoLicencia(){return tipoLicencia;} public void setTipoLicencia(String v){tipoLicencia=v;}
    public String getCodigoLicencia(){return codigoLicencia;} public void setCodigoLicencia(String v){codigoLicencia=v;}
    public String getObservaciones(){return observaciones;} public void setObservaciones(String v){observaciones=v;}
    public LocalDate getFechaAdquisicion(){return fechaAdquisicion;} public void setFechaAdquisicion(LocalDate v){fechaAdquisicion=v;}
    public LocalDate getFechaVencimiento(){return fechaVencimiento;} public void setFechaVencimiento(LocalDate v){fechaVencimiento=v;}
    public String getEstadoLicencia(){return estadoLicencia;} public void setEstadoLicencia(String v){estadoLicencia=v;}
    public String getActivoCodigo(){return activoCodigo;} public void setActivoCodigo(String v){activoCodigo=v;}
}
