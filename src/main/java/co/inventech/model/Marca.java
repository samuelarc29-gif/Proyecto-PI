package co.inventech.model;
public class Marca {
    private int id; private String nombreMarca; private String descripcion;
    public Marca(){} public Marca(int id, String n){this.id=id; this.nombreMarca=n;}
    public int getId(){return id;} public void setId(int v){id=v;}
    public String getNombreMarca(){return nombreMarca;} public void setNombreMarca(String v){nombreMarca=v;}
    public String getDescripcion(){return descripcion;} public void setDescripcion(String v){descripcion=v;}
    @Override public String toString(){return nombreMarca;}
}
