package Modelo;

import java.io.Serializable;

public class Contacto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nombreCompleto, cargo, numeroTelefono, correoElectronico;
    public Contacto(String nombreCompleto, String cargo, String numeroTelefono, String correoElectronico) {
        this.nombreCompleto=nombreCompleto; this.cargo=cargo; this.numeroTelefono=numeroTelefono; this.correoElectronico=correoElectronico;
    }
    public String getNombreCompleto(){return nombreCompleto;} public void setNombreCompleto(String v){nombreCompleto=v;}
    public String getCargo(){return cargo;} public void setCargo(String v){cargo=v;}
    public String getNumeroTelefono(){return numeroTelefono;} public void setNumeroTelefono(String v){numeroTelefono=v;}
    public String getCorreoElectronico(){return correoElectronico;} public void setCorreoElectronico(String v){correoElectronico=v;}
    @Override public String toString(){return "Nombre: "+nombreCompleto+" | Cargo: "+cargo+" | Teléfono: "+numeroTelefono+" | Correo: "+correoElectronico;}
}
