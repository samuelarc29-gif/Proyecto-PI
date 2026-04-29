package com.inventech.model;

import java.time.LocalDate;

public class AsignacionActivo {
    private int idAsignacionActivo;
    private int idActivo;
    private int idUsuario;
    private LocalDate fechaAsignacion;
    private LocalDate fechaDevolucion;
    private String observaciones;
    private String estadoAsignacion;

    // joins
    private String codigoActivo;
    private String nombreUsuario;

    public AsignacionActivo() {}

    public int getIdAsignacionActivo() { return idAsignacionActivo; }
    public void setIdAsignacionActivo(int v) { this.idAsignacionActivo = v; }
    public int getIdActivo() { return idActivo; }
    public void setIdActivo(int v) { this.idActivo = v; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int v) { this.idUsuario = v; }
    public LocalDate getFechaAsignacion() { return fechaAsignacion; }
    public void setFechaAsignacion(LocalDate v) { this.fechaAsignacion = v; }
    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(LocalDate v) { this.fechaDevolucion = v; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String v) { this.observaciones = v; }
    public String getEstadoAsignacion() { return estadoAsignacion; }
    public void setEstadoAsignacion(String v) { this.estadoAsignacion = v; }
    public String getCodigoActivo() { return codigoActivo; }
    public void setCodigoActivo(String v) { this.codigoActivo = v; }
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String v) { this.nombreUsuario = v; }
}
