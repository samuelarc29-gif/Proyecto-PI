package com.inventech.model;

public class Proveedor {
    private int idProveedor;
    private String nombreProveedor;
    private String contacto;
    private String telefono;
    private String correo;
    private String direccion;
    private String descripcionSoporte;

    public Proveedor() {}
    public int getIdProveedor() { return idProveedor; }
    public void setIdProveedor(int i) { this.idProveedor = i; }
    public String getNombreProveedor() { return nombreProveedor; }
    public void setNombreProveedor(String n) { this.nombreProveedor = n; }
    public String getContacto() { return contacto; }
    public void setContacto(String c) { this.contacto = c; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String t) { this.telefono = t; }
    public String getCorreo() { return correo; }
    public void setCorreo(String c) { this.correo = c; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String d) { this.direccion = d; }
    public String getDescripcionSoporte() { return descripcionSoporte; }
    public void setDescripcionSoporte(String d) { this.descripcionSoporte = d; }
    @Override public String toString() { return nombreProveedor; }
}
