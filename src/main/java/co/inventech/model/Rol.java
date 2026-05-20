package co.inventech.model;
public class Rol {
    private int idRol; private String nombreRol; private String descripcion;
    public Rol() {} public Rol(int id, String n) { this.idRol=id; this.nombreRol=n; }
    public int getIdRol(){return idRol;} public void setIdRol(int v){idRol=v;}
    public String getNombreRol(){return nombreRol;} public void setNombreRol(String v){nombreRol=v;}
    public String getDescripcion(){return descripcion;} public void setDescripcion(String v){descripcion=v;}
    @Override public String toString(){return nombreRol;}
}
