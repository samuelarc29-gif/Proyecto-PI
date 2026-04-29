package com.inventech.model;

public class Usuario {
    private int idUsuario;
    private int idRol;
    private Integer idArea;
    private String nombreCompleto;
    private String documento;
    private String cargo;
    private String correo;
    private String telefono;
    private String estadoUsuario;

    // Campos de visualización (joins)
    private String nombreRol;
    private String nombreArea;

    public Usuario() {}

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public int getIdRol() { return idRol; }
    public void setIdRol(int idRol) { this.idRol = idRol; }
    public Integer getIdArea() { return idArea; }
    public void setIdArea(Integer idArea) { this.idArea = idArea; }
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String n) { this.nombreCompleto = n; }
    public String getDocumento() { return documento; }
    public void setDocumento(String d) { this.documento = d; }
    public String getCargo() { return cargo; }
    public void setCargo(String c) { this.cargo = c; }
    public String getCorreo() { return correo; }
    public void setCorreo(String c) { this.correo = c; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String t) { this.telefono = t; }
    public String getEstadoUsuario() { return estadoUsuario; }
    public void setEstadoUsuario(String e) { this.estadoUsuario = e; }
    public String getNombreRol() { return nombreRol; }
    public void setNombreRol(String nombreRol) { this.nombreRol = nombreRol; }
    public String getNombreArea() { return nombreArea; }
    public void setNombreArea(String nombreArea) { this.nombreArea = nombreArea; }
    @Override public String toString() { return nombreCompleto; }
}
