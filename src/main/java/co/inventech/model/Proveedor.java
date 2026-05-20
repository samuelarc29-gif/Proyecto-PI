package co.inventech.model;
public class Proveedor {
    private int idProveedor; private String nombreProveedor; private String contacto;
    private String telefono; private String correo; private String direccion;
    private String descripcionSoporte; private String estadoProveedor="Activo";
    public int getIdProveedor(){return idProveedor;} public void setIdProveedor(int v){idProveedor=v;}
    public String getNombreProveedor(){return nombreProveedor;} public void setNombreProveedor(String v){nombreProveedor=v;}
    public String getContacto(){return contacto;} public void setContacto(String v){contacto=v;}
    public String getTelefono(){return telefono;} public void setTelefono(String v){telefono=v;}
    public String getCorreo(){return correo;} public void setCorreo(String v){correo=v;}
    public String getDireccion(){return direccion;} public void setDireccion(String v){direccion=v;}
    public String getDescripcionSoporte(){return descripcionSoporte;} public void setDescripcionSoporte(String v){descripcionSoporte=v;}
    public String getEstadoProveedor(){return estadoProveedor;} public void setEstadoProveedor(String v){estadoProveedor=v;}
    @Override public String toString(){return nombreProveedor;}
}
