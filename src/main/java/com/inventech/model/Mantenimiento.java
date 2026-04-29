package com.inventech.model;

import java.time.LocalDate;

public class Mantenimiento {
    private int idMantenimiento;
    private int idTicket;
    private int idTipoMantenimiento;
    private LocalDate fechaProgramada;
    private String observaciones;

    private String descripcionTicket;
    private String nombreTipo;

    public Mantenimiento() {}

    public int getIdMantenimiento() { return idMantenimiento; }
    public void setIdMantenimiento(int v) { this.idMantenimiento = v; }
    public int getIdTicket() { return idTicket; }
    public void setIdTicket(int v) { this.idTicket = v; }
    public int getIdTipoMantenimiento() { return idTipoMantenimiento; }
    public void setIdTipoMantenimiento(int v) { this.idTipoMantenimiento = v; }
    public LocalDate getFechaProgramada() { return fechaProgramada; }
    public void setFechaProgramada(LocalDate v) { this.fechaProgramada = v; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String v) { this.observaciones = v; }
    public String getDescripcionTicket() { return descripcionTicket; }
    public void setDescripcionTicket(String v) { this.descripcionTicket = v; }
    public String getNombreTipo() { return nombreTipo; }
    public void setNombreTipo(String v) { this.nombreTipo = v; }
}
