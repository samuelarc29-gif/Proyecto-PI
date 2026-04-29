package com.inventech.model;

import java.time.LocalDateTime;

public class Ticket {
    private int idTicket;
    private int idActivo;
    private int idUsuarioSolicitante;
    private int idEstadoTicket;
    private Integer idAns;
    private String descripcionFalla;
    private LocalDateTime fechaReporte;
    private String prioridad;
    private LocalDateTime fechaCierreTecnico;
    private LocalDateTime fechaCierreUsuario;
    private String observacionCierreUsuario;

    // Joins
    private String codigoActivo;
    private String nombreUsuario;
    private String nombreEstado;

    public Ticket() {}
    public int getIdTicket() { return idTicket; }
    public void setIdTicket(int i) { this.idTicket = i; }
    public int getIdActivo() { return idActivo; }
    public void setIdActivo(int i) { this.idActivo = i; }
    public int getIdUsuarioSolicitante() { return idUsuarioSolicitante; }
    public void setIdUsuarioSolicitante(int i) { this.idUsuarioSolicitante = i; }
    public int getIdEstadoTicket() { return idEstadoTicket; }
    public void setIdEstadoTicket(int i) { this.idEstadoTicket = i; }
    public Integer getIdAns() { return idAns; }
    public void setIdAns(Integer i) { this.idAns = i; }
    public String getDescripcionFalla() { return descripcionFalla; }
    public void setDescripcionFalla(String s) { this.descripcionFalla = s; }
    public LocalDateTime getFechaReporte() { return fechaReporte; }
    public void setFechaReporte(LocalDateTime f) { this.fechaReporte = f; }
    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String p) { this.prioridad = p; }
    public LocalDateTime getFechaCierreTecnico() { return fechaCierreTecnico; }
    public void setFechaCierreTecnico(LocalDateTime f) { this.fechaCierreTecnico = f; }
    public LocalDateTime getFechaCierreUsuario() { return fechaCierreUsuario; }
    public void setFechaCierreUsuario(LocalDateTime f) { this.fechaCierreUsuario = f; }
    public String getObservacionCierreUsuario() { return observacionCierreUsuario; }
    public void setObservacionCierreUsuario(String s) { this.observacionCierreUsuario = s; }
    public String getCodigoActivo() { return codigoActivo; }
    public void setCodigoActivo(String s) { this.codigoActivo = s; }
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String s) { this.nombreUsuario = s; }
    public String getNombreEstado() { return nombreEstado; }
    public void setNombreEstado(String s) { this.nombreEstado = s; }
}
