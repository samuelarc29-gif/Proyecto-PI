package co.inventech.model;
public class TipoMantenimiento {
    private int id; private String nombreTipo; private String descripcion;
    public TipoMantenimiento(){} public TipoMantenimiento(int id, String n){this.id=id; this.nombreTipo=n;}
    public int getId(){return id;} public void setId(int v){id=v;}
    public String getNombreTipo(){return nombreTipo;} public void setNombreTipo(String v){nombreTipo=v;}
    public String getDescripcion(){return descripcion;} public void setDescripcion(String v){descripcion=v;}
    @Override public String toString(){return nombreTipo;}
}
