package co.inventech.model;
public class EstadoTicket {
    private int id; private String nombreEstado; private String descripcion;
    public EstadoTicket(){} public EstadoTicket(int id, String n){this.id=id; this.nombreEstado=n;}
    public int getId(){return id;} public void setId(int v){id=v;}
    public String getNombreEstado(){return nombreEstado;} public void setNombreEstado(String v){nombreEstado=v;}
    public String getDescripcion(){return descripcion;} public void setDescripcion(String v){descripcion=v;}
    @Override public String toString(){return nombreEstado;}
}
