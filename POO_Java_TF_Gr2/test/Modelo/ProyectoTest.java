package Modelo;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class ProyectoTest {
    private Proyecto crear(){
        Cliente c=new PersonaNatural("100","Ana Pérez","Cali","ana@example.com");
        Empleado g=new Empleado("200","Luis Díaz","Ingeniero",(byte)5);
        return new Proyecto("2026-001","Desarrollo de plataforma multimedia",LocalDate.of(2026,1,10),"semanas",(byte)2,c,g,new ArrayList<>(),"INICIADO");
    }
    @Test void calculaFechaFinalizacion(){assertEquals(LocalDate.of(2026,1,24),crear().determinarFechaFinalizacion());}
    @Test void iniciaSinEquipo(){assertTrue(crear().getSuEquipo().isEmpty());}
    @Test void agregaYEliminaIntegrante(){Proyecto p=crear();Empleado e=new Empleado("300","Marta Ruiz","Diseñadora",(byte)2);p.agregarIntegrante(e);assertEquals(1,p.getSuEquipo().size());assertEquals(e,p.eliminarIntegrante("300"));assertTrue(p.getSuEquipo().isEmpty());}
    @Test void evitaIntegrantesDuplicados(){Proyecto p=crear();Empleado e=new Empleado("300","Marta Ruiz","Diseñadora",(byte)2);p.agregarIntegrante(e);assertThrows(IllegalArgumentException.class,()->p.agregarIntegrante(e));}
}
