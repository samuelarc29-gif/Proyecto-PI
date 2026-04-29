package com.inventech.model;

/** Modelo genérico para tablas tipo catálogo (id + nombre + descripcion). */
public class Catalogo {
    private int id;
    private String nombre;
    private String descripcion;
    public Catalogo() {}
    public Catalogo(int id, String nombre, String descripcion) {
        this.id = id; this.nombre = nombre; this.descripcion = descripcion;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String n) { this.nombre = n; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String d) { this.descripcion = d; }
    @Override public String toString() { return nombre; }
}
