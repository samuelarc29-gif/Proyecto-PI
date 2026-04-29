package com.inventech.model;

public class Marca {
    private int idMarca;
    private String nombreMarca;
    private String descripcion;
    public Marca() {}
    public Marca(int idMarca, String nombreMarca, String descripcion) {
        this.idMarca = idMarca; this.nombreMarca = nombreMarca; this.descripcion = descripcion;
    }
    public int getIdMarca() { return idMarca; }
    public void setIdMarca(int i) { this.idMarca = i; }
    public String getNombreMarca() { return nombreMarca; }
    public void setNombreMarca(String n) { this.nombreMarca = n; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String d) { this.descripcion = d; }
    @Override public String toString() { return nombreMarca; }
}
