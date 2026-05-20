package co.inventech.model;
import java.time.LocalDate; import java.time.LocalDateTime;
public class Activo {
    private int idActivo; private int idTipoActivo, idMarca, idEstadoActivo;
    private Integer idProveedor, idArea;
    private String modelo, numeroSerie, codigoInterno, observaciones;
    private LocalDate fechaAdquisicion, fechaGarantia;
    private LocalDateTime fechaRegistro;
    // Derivados
    private String tipoNombre, marcaNombre, estadoNombre, areaNombre, proveedorNombre;
    public int getIdActivo(){return idActivo;} public void setIdActivo(int v){idActivo=v;}
    public int getIdTipoActivo(){return idTipoActivo;} public void setIdTipoActivo(int v){idTipoActivo=v;}
    public int getIdMarca(){return idMarca;} public void setIdMarca(int v){idMarca=v;}
    public int getIdEstadoActivo(){return idEstadoActivo;} public void setIdEstadoActivo(int v){idEstadoActivo=v;}
    public Integer getIdProveedor(){return idProveedor;} public void setIdProveedor(Integer v){idProveedor=v;}
    public Integer getIdArea(){return idArea;} public void setIdArea(Integer v){idArea=v;}
    public String getModelo(){return modelo;} public void setModelo(String v){modelo=v;}
    public String getNumeroSerie(){return numeroSerie;} public void setNumeroSerie(String v){numeroSerie=v;}
    public String getCodigoInterno(){return codigoInterno;} public void setCodigoInterno(String v){codigoInterno=v;}
    public String getObservaciones(){return observaciones;} public void setObservaciones(String v){observaciones=v;}
    public LocalDate getFechaAdquisicion(){return fechaAdquisicion;} public void setFechaAdquisicion(LocalDate v){fechaAdquisicion=v;}
    public LocalDate getFechaGarantia(){return fechaGarantia;} public void setFechaGarantia(LocalDate v){fechaGarantia=v;}
    public LocalDateTime getFechaRegistro(){return fechaRegistro;} public void setFechaRegistro(LocalDateTime v){fechaRegistro=v;}
    public String getTipoNombre(){return tipoNombre;} public void setTipoNombre(String v){tipoNombre=v;}
    public String getMarcaNombre(){return marcaNombre;} public void setMarcaNombre(String v){marcaNombre=v;}
    public String getEstadoNombre(){return estadoNombre;} public void setEstadoNombre(String v){estadoNombre=v;}
    public String getAreaNombre(){return areaNombre;} public void setAreaNombre(String v){areaNombre=v;}
    public String getProveedorNombre(){return proveedorNombre;} public void setProveedorNombre(String v){proveedorNombre=v;}
    @Override public String toString(){return codigoInterno+" - "+modelo;}
}
