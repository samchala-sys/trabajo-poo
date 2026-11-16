package Modelo;

import java.io.Serializable;

public class Empleado implements Serializable {
    private static final long serialVersionUID = 1L;
    private String numeroIdentificacion,nombreCompleto,tituloProfesional;
    private byte aniosExperiencia;
    public Empleado(String id,String nombreCompleto,String tituloProfesional,byte aniosExperiencia){this.numeroIdentificacion=id;this.nombreCompleto=nombreCompleto;this.tituloProfesional=tituloProfesional;this.aniosExperiencia=aniosExperiencia;}
    public String getNumeroIdentificacion(){return numeroIdentificacion;} public void setNumeroIdentificacion(String v){numeroIdentificacion=v;}
    public String getNombreCompleto(){return nombreCompleto;} public void setNombreCompleto(String v){nombreCompleto=v;}
    public String getTituloProfesional(){return tituloProfesional;} public void setTituloProfesional(String v){tituloProfesional=v;}
    public byte getAniosExperiencia(){return aniosExperiencia;} public void setAniosExperiencia(byte v){aniosExperiencia=v;}
    @Override public String toString(){return "Identificación: "+numeroIdentificacion+" | Nombre: "+nombreCompleto+" | Título: "+tituloProfesional+" | Experiencia: "+aniosExperiencia+" años";}
}
