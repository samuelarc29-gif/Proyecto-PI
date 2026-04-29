package com.inventech.model;

import java.time.LocalDateTime;

public class AsignacionTecnico {
    private int idAsignacionTecnico;
    private int idTicket;
    private int idUsuarioTecnico;
    private LocalDateTime fechaAsignacion;
    private LocalDateTime fechaAtencion;
    private String diagnostico;
    private String solucionAplicada;
    private String observaciones;
    private String cierreTecnico;

    private String descripcionTicket;
    private String nombreTecnico;

    public AsignacionTecnico() {}

    public int getIdAsignacionTecnico() { return idAsignacionTecnico; }
    public void setIdAsignacionTecnico(int v) { this.idAsignacionTecnico = v; }
    public int getIdTicket() { return idTicket; }
    public void setIdTicket(int v) { this.idTicket = v; }
    public int getIdUsuarioTecnico() { return idUsuarioTecnico; }
    public void setIdUsuarioTecnico(int v) { this.idUsuarioTecnico = v; }
    public LocalDateTime getFechaAsignacion() { return fechaAsignacion; }
    public void setFechaAsignacion(LocalDateTime v) { this.fechaAsignacion = v; }
    public LocalDateTime getFechaAtencion() { return fechaAtencion; }
    public void setFechaAtencion(LocalDateTime v) { this.fechaAtencion = v; }
    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String v) { this.diagnostico = v; }
    public String getSolucionAplicada() { return solucionAplicada; }
    public void setSolucionAplicada(String v) { this.solucionAplicada = v; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String v) { this.observaciones = v; }
    public String getCierreTecnico() { return cierreTecnico; }
    public void setCierreTecnico(String v) { this.cierreTecnico = v; }
    public String getDescripcionTicket() { return descripcionTicket; }
    public void setDescripcionTicket(String v) { this.descripcionTicket = v; }
    public String getNombreTecnico() { return nombreTecnico; }
    public void setNombreTecnico(String v) { this.nombreTecnico = v; }
}
