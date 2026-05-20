package co.inventech.model;
public class Ans {
    private int idAns; private String tipoSolicitud, prioridad, estadoAns="Activo";
    private int tiempoRespuestaHoras, tiempoSolucionHoras;
    public int getIdAns(){return idAns;} public void setIdAns(int v){idAns=v;}
    public String getTipoSolicitud(){return tipoSolicitud;} public void setTipoSolicitud(String v){tipoSolicitud=v;}
    public String getPrioridad(){return prioridad;} public void setPrioridad(String v){prioridad=v;}
    public int getTiempoRespuestaHoras(){return tiempoRespuestaHoras;} public void setTiempoRespuestaHoras(int v){tiempoRespuestaHoras=v;}
    public int getTiempoSolucionHoras(){return tiempoSolucionHoras;} public void setTiempoSolucionHoras(int v){tiempoSolucionHoras=v;}
    public String getEstadoAns(){return estadoAns;} public void setEstadoAns(String v){estadoAns=v;}
    @Override public String toString(){return tipoSolicitud+" / "+prioridad;}
}
