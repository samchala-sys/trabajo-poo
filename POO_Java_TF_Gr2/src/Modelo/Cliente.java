package Modelo;

import java.io.Serializable;

public abstract class Cliente implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String numeroIdentificacion, nombre, nombreCiudad;
    public Cliente(String numeroIdentificacion,String nombre,String nombreCiudad){this.numeroIdentificacion=numeroIdentificacion;this.nombre=nombre;this.nombreCiudad=nombreCiudad;}
    public String getNumeroIdentificacion(){return numeroIdentificacion;} public void setNumeroIdentificacion(String v){numeroIdentificacion=v;}
    public String getNombre(){return nombre;} public void setNombre(String v){nombre=v;}
    public String getNombreCiudad(){return nombreCiudad;} public void setNombreCiudad(String v){nombreCiudad=v;}
    public abstract String getTipoCliente();
    @Override public String toString(){return "Tipo: "+getTipoCliente()+" | Identificación: "+numeroIdentificacion+" | Nombre: "+nombre+" | Ciudad: "+nombreCiudad;}
}
