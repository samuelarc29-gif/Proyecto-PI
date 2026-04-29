package com.inventech.model;

public class Area {
    private int idArea;
    private String nombreArea;
    private String descripcion;

    public Area() {}
    public Area(int idArea, String nombreArea, String descripcion) {
        this.idArea = idArea; this.nombreArea = nombreArea; this.descripcion = descripcion;
    }
    public int getIdArea() { return idArea; }
    public void setIdArea(int idArea) { this.idArea = idArea; }
    public String getNombreArea() { return nombreArea; }
    public void setNombreArea(String nombreArea) { this.nombreArea = nombreArea; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    @Override public String toString() { return nombreArea; }
}
