package Modelo;

public final class Empresa extends Cliente {
    private static final long serialVersionUID = 1L;
    private String numeroTelefono;
    private Contacto suContacto;
    public Empresa(String id,String nombre,String ciudad,String numeroTelefono,Contacto contacto){super(id,nombre,ciudad);this.numeroTelefono=numeroTelefono;this.suContacto=contacto;}
    public String getNumeroTelefono(){return numeroTelefono;} public void setNumeroTelefono(String v){numeroTelefono=v;}
    public Contacto getSuContacto(){return suContacto;} public void setSuContacto(Contacto v){suContacto=v;}
    @Override public String getTipoCliente(){return "Empresa";}
    @Override public String toString(){return super.toString()+" | Teléfono: "+numeroTelefono+" | Contacto: "+suContacto;}
}
