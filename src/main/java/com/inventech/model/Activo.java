package com.inventech.model;

import java.time.LocalDate;

public class Activo {
    private int idActivo;
    private int idTipoActivo;
    private int idMarca;
    private Integer idProveedor;
    private int idEstadoActivo;
    private Integer idArea;
    private String modelo;
    private String numeroSerie;
    private String codigoInterno;
    private LocalDate fechaAdquisicion;
    private LocalDate fechaGarantia;
    private String observaciones;

    // Joins
    private String nombreTipo;
    private String nombreMarca;
    private String nombreProveedor;
    private String nombreEstado;
    private String nombreArea;

    public Activo() {}
    public int getIdActivo() { return idActivo; }
    public void setIdActivo(int i) { this.idActivo = i; }
    public int getIdTipoActivo() { return idTipoActivo; }
    public void setIdTipoActivo(int i) { this.idTipoActivo = i; }
    public int getIdMarca() { return idMarca; }
    public void setIdMarca(int i) { this.idMarca = i; }
    public Integer getIdProveedor() { return idProveedor; }
    public void setIdProveedor(Integer i) { this.idProveedor = i; }
    public int getIdEstadoActivo() { return idEstadoActivo; }
    public void setIdEstadoActivo(int i) { this.idEstadoActivo = i; }
    public Integer getIdArea() { return idArea; }
    public void setIdArea(Integer i) { this.idArea = i; }
    public String getModelo() { return modelo; }
    public void setModelo(String m) { this.modelo = m; }
    public String getNumeroSerie() { return numeroSerie; }
    public void setNumeroSerie(String n) { this.numeroSerie = n; }
    public String getCodigoInterno() { return codigoInterno; }
    public void setCodigoInterno(String c) { this.codigoInterno = c; }
    public LocalDate getFechaAdquisicion() { return fechaAdquisicion; }
    public void setFechaAdquisicion(LocalDate f) { this.fechaAdquisicion = f; }
    public LocalDate getFechaGarantia() { return fechaGarantia; }
    public void setFechaGarantia(LocalDate f) { this.fechaGarantia = f; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String o) { this.observaciones = o; }
    public String getNombreTipo() { return nombreTipo; }
    public void setNombreTipo(String s) { this.nombreTipo = s; }
    public String getNombreMarca() { return nombreMarca; }
    public void setNombreMarca(String s) { this.nombreMarca = s; }
    public String getNombreProveedor() { return nombreProveedor; }
    public void setNombreProveedor(String s) { this.nombreProveedor = s; }
    public String getNombreEstado() { return nombreEstado; }
    public void setNombreEstado(String s) { this.nombreEstado = s; }
    public String getNombreArea() { return nombreArea; }
    public void setNombreArea(String s) { this.nombreArea = s; }

    @Override public String toString() { return codigoInterno + " - " + modelo; }
}
