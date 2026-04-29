package com.inventech.model;

import java.time.LocalDate;

public class LicenciaSoftware {
    private int idLicencia;
    private int idActivo;
    private String nombreSoftware;
    private String tipoLicencia;
    private String codigoLicencia;
    private LocalDate fechaAdquisicion;
    private LocalDate fechaVencimiento;
    private String estadoLicencia;
    private String observaciones;

    private String codigoActivo;

    public LicenciaSoftware() {}
    public int getIdLicencia() { return idLicencia; }
    public void setIdLicencia(int i) { this.idLicencia = i; }
    public int getIdActivo() { return idActivo; }
    public void setIdActivo(int i) { this.idActivo = i; }
    public String getNombreSoftware() { return nombreSoftware; }
    public void setNombreSoftware(String s) { this.nombreSoftware = s; }
    public String getTipoLicencia() { return tipoLicencia; }
    public void setTipoLicencia(String s) { this.tipoLicencia = s; }
    public String getCodigoLicencia() { return codigoLicencia; }
    public void setCodigoLicencia(String s) { this.codigoLicencia = s; }
    public LocalDate getFechaAdquisicion() { return fechaAdquisicion; }
    public void setFechaAdquisicion(LocalDate f) { this.fechaAdquisicion = f; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate f) { this.fechaVencimiento = f; }
    public String getEstadoLicencia() { return estadoLicencia; }
    public void setEstadoLicencia(String s) { this.estadoLicencia = s; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String s) { this.observaciones = s; }
    public String getCodigoActivo() { return codigoActivo; }
    public void setCodigoActivo(String s) { this.codigoActivo = s; }
}
