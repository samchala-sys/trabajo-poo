package Modelo;

public final class PersonaNatural extends Cliente {
    private static final long serialVersionUID = 1L;
    private String correoElectronico;
    public PersonaNatural(String id,String nombre,String ciudad,String correoElectronico){super(id,nombre,ciudad);this.correoElectronico=correoElectronico;}
    public String getCorreoElectronico(){return correoElectronico;} public void setCorreoElectronico(String v){correoElectronico=v;}
    @Override public String getTipoCliente(){return "PersonaNatural";}
    @Override public String toString(){return super.toString()+" | Correo: "+correoElectronico;}
}
