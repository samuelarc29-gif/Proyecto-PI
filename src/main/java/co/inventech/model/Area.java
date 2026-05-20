package co.inventech.model;
public class Area {
    private int idArea; private String nombreArea; private String descripcion; private String estadoArea="Activa";
    public Area(){} public Area(int id, String n){this.idArea=id; this.nombreArea=n;}
    public int getIdArea(){return idArea;} public void setIdArea(int v){idArea=v;}
    public String getNombreArea(){return nombreArea;} public void setNombreArea(String v){nombreArea=v;}
    public String getDescripcion(){return descripcion;} public void setDescripcion(String v){descripcion=v;}
    public String getEstadoArea(){return estadoArea;} public void setEstadoArea(String v){estadoArea=v;}
    @Override public String toString(){return nombreArea;}
}
