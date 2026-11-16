package Modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Proyecto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String codigo,titulo,unidadDuracion,estado;
    private LocalDate fechaInicio;
    private byte duracionEstimada;
    private Cliente suCliente;
    private Empleado suGerente;
    private ArrayList<Empleado> suEquipo;
    public Proyecto(String codigo,String titulo,LocalDate fechaInicio,String unidadDuracion,byte duracionEstimada,Cliente cliente,Empleado gerente,ArrayList<Empleado> equipo,String estado){
        this.codigo=codigo;this.titulo=titulo;this.fechaInicio=fechaInicio;this.unidadDuracion=unidadDuracion;this.duracionEstimada=duracionEstimada;this.suCliente=cliente;this.suGerente=gerente;this.suEquipo=equipo==null?new ArrayList<>():new ArrayList<>(equipo);this.estado=estado==null?"INICIADO":estado;
    }
    public Proyecto(String codigo,String titulo,LocalDate fechaInicio,String unidadDuracion,byte duracionEstimada,Cliente cliente,Empleado gerente,String estado){this(codigo,titulo,fechaInicio,unidadDuracion,duracionEstimada,cliente,gerente,new ArrayList<>(),estado);}
    public String getCodigo(){return codigo;} public void setCodigo(String v){codigo=v;}
    public String getTitulo(){return titulo;} public void setTitulo(String v){titulo=v;}
    public LocalDate getFechaInicio(){return fechaInicio;} public void setFechaInicio(LocalDate v){fechaInicio=v;}
    public String getUnidadDuracion(){return unidadDuracion;} public void setUnidadDuracion(String v){unidadDuracion=v;}
    public byte getDuracionEstimada(){return duracionEstimada;} public void setDuracionEstimada(byte v){duracionEstimada=v;}
    public Cliente getSuCliente(){return suCliente;} public void setSuCliente(Cliente v){suCliente=v;}
    public Empleado getSuGerente(){return suGerente;} public void setSuGerente(Empleado v){suGerente=v;}
    public ArrayList<Empleado> getSuEquipo(){return suEquipo;} public void setSuEquipo(ArrayList<Empleado> v){suEquipo=v==null?new ArrayList<>():v;}
    public String getEstado(){return estado;} public void setEstado(String v){estado=v;}
    public LocalDate determinarFechaFinalizacion(){
        if(fechaInicio==null)return null;
        return switch(unidadDuracion.toLowerCase()){case "semanas" -> fechaInicio.plusWeeks(duracionEstimada);case "meses" -> fechaInicio.plusMonths(duracionEstimada);case "años" -> fechaInicio.plusYears(duracionEstimada);default -> throw new IllegalStateException("Unidad de duración no válida: "+unidadDuracion);};
    }
    public void agregarIntegrante(Empleado nuevo){if(nuevo==null)throw new IllegalArgumentException("El integrante no puede ser nulo.");if(suEquipo.stream().anyMatch(e->e.getNumeroIdentificacion().equals(nuevo.getNumeroIdentificacion())))throw new IllegalArgumentException("El empleado ya pertenece al equipo.");suEquipo.add(nuevo);}
    public Empleado eliminarIntegrante(String id){for(int i=0;i<suEquipo.size();i++)if(suEquipo.get(i).getNumeroIdentificacion().equals(id))return suEquipo.remove(i);return null;}
    @Override public String toString(){return "Código: "+codigo+" | Título: "+titulo+" | Inicio: "+fechaInicio+" | Duración: "+duracionEstimada+" "+unidadDuracion+" | Finalización: "+determinarFechaFinalizacion()+" | Estado: "+estado+"\nCliente: "+suCliente+"\nGerente: "+suGerente+"\nEquipo: "+(suEquipo.isEmpty()?"Sin integrantes":suEquipo);}
}
