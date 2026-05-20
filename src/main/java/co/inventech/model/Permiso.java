package co.inventech.model;
public class Permiso {
    private int idPermiso; private String nombrePermiso; private String descripcion;
    public int getIdPermiso(){return idPermiso;} public void setIdPermiso(int v){idPermiso=v;}
    public String getNombrePermiso(){return nombrePermiso;} public void setNombrePermiso(String v){nombrePermiso=v;}
    public String getDescripcion(){return descripcion;} public void setDescripcion(String v){descripcion=v;}
    @Override public String toString(){return nombrePermiso;}
}
