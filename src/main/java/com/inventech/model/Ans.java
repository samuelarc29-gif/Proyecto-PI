package com.inventech.model;

public class Ans {
    private int idAns;
    private String tipoSolicitud;
    private String prioridad;
    private int tiempoRespuestaHoras;
    private int tiempoSolucionHoras;
    private String descripcion;

    public Ans() {}
    public int getIdAns() { return idAns; }
    public void setIdAns(int i) { this.idAns = i; }
    public String getTipoSolicitud() { return tipoSolicitud; }
    public void setTipoSolicitud(String s) { this.tipoSolicitud = s; }
    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String s) { this.prioridad = s; }
    public int getTiempoRespuestaHoras() { return tiempoRespuestaHoras; }
    public void setTiempoRespuestaHoras(int i) { this.tiempoRespuestaHoras = i; }
    public int getTiempoSolucionHoras() { return tiempoSolucionHoras; }
    public void setTiempoSolucionHoras(int i) { this.tiempoSolucionHoras = i; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String s) { this.descripcion = s; }
    @Override public String toString() { return tipoSolicitud + " (" + prioridad + ")"; }
}
