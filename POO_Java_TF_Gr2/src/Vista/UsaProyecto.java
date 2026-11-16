package Vista;

import Modelo.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

/** Ventana principal para administrar proyectos según el diagrama de clases. */
public class UsaProyecto extends JFrame {
    private static final long serialVersionUID = 1L;
    public static ArrayList<Proyecto> losProyectos = new ArrayList<>();
    public static ArrayList<Cliente> losClientes = new ArrayList<>();
    private final JTextArea salida = new JTextArea();
    private static final String ARCHIVO_PROYECTOS="datosProyectos", ARCHIVO_CLIENTES="datosClientes";
    private static final Set<String> ESTADOS=Set.of("INICIADO","EN CURSO","CANCELADO","FINALIZADO","SUSPENDIDO","APLAZADO");

    public UsaProyecto(){super("Gestión de proyectos - POO Java");initComponents();}
    private void initComponents(){setDefaultCloseOperation(EXIT_ON_CLOSE);setSize(950,620);setLocationRelativeTo(null);setLayout(new BorderLayout(8,8));
        JLabel titulo=new JLabel("SISTEMA DE GESTIÓN DE PROYECTOS",SwingConstants.CENTER);titulo.setFont(new Font("Arial",Font.BOLD,20));add(titulo,BorderLayout.NORTH);
        salida.setEditable(false);salida.setFont(new Font(Font.MONOSPACED,Font.PLAIN,12));add(new JScrollPane(salida),BorderLayout.CENTER);
        JPanel acciones=new JPanel(new GridLayout(0,3,6,6));
        String[] ops={"Registrar proyecto","Consultar todos","Proyectos en curso","Primer proyecto","Último proyecto","Consultar por código","Consultar gerente","Consultar equipo","Proyectos por gerente","Proyectos por cliente","Proyectos por tipo de cliente","Registrar integrante","Registrar equipo","Eliminar integrante","Actualizar estado","Guardar proyectos","Guardar clientes","Leer proyectos","Leer clientes"};
        Runnable[] tareas={this::uiRegistrarProyecto,()->mostrar(consultarTodosProyectos(losProyectos)),()->mostrar(consultarProyectosEnCurso(losProyectos)),()->mostrar(consultarPrimerProyecto(losProyectos)),()->mostrar(consultarUltimoProyecto(losProyectos)),this::uiConsultarCodigo,this::uiConsultarGerente,this::uiConsultarEquipo,this::uiPorGerente,this::uiPorCliente,this::uiPorTipo,this::uiRegistrarIntegrante,this::uiRegistrarEquipo,this::uiEliminarIntegrante,this::uiActualizarEstado,()->generarArchivoDeObjetosProyectos(losProyectos),()->generarArchivoDeObjetosClientes(losProyectos),()->{losProyectos=leerArchivoDeObjetosProyectos();mostrar("Proyectos cargados: "+losProyectos.size());},()->{losClientes=leerArchivoDeObjetosClientes();mostrar("Clientes cargados: "+losClientes.size());}};
        for(int i=0;i<ops.length;i++){final int k=i;JButton b=new JButton(ops[i]);b.addActionListener(e->{try{tareas[k].run();}catch(Exception ex){error(ex.getMessage());}});acciones.add(b);}add(acciones,BorderLayout.SOUTH);
        mostrar("Bienvenido. Registre primero un proyecto y utilice los botones para administrar la información.");
    }
    private void mostrar(String s){salida.setText(s==null?"":s);} private void error(String s){JOptionPane.showMessageDialog(this,s==null?"Ocurrió un error.":s,"Atención",JOptionPane.WARNING_MESSAGE);}
    private String pedir(String mensaje){return JOptionPane.showInputDialog(this,mensaje);} private String requerido(String msg){String s=pedir(msg);if(s==null)throw new IllegalArgumentException("Operación cancelada.");s=s.trim();if(s.isEmpty())throw new IllegalArgumentException("El campo es obligatorio.");return s;}
    private int entero(String msg,int min,int max){int n;try{n=Integer.parseInt(requerido(msg));}catch(NumberFormatException e){throw new IllegalArgumentException("Ingrese un número entero válido.");}if(n<min||n>max)throw new IllegalArgumentException("El valor debe estar entre "+min+" y "+max+".");return n;}
    private Empleado pedirEmpleado(){String id=requerido("Identificación del empleado/gerente:");String nombre=requerido("Nombre completo:");String titulo=requerido("Título profesional:");byte exp=(byte)entero("Años de experiencia (0-100):",0,100);return new Empleado(id,nombre,titulo,exp);}
    private Cliente pedirCliente(){String tipo=requerido("Tipo de cliente (Empresa/PersonaNatural):");String id=requerido("Identificación del cliente:");String nombre=requerido("Nombre o razón social:");String ciudad=requerido("Ciudad:");Cliente c;
        if(tipo.equalsIgnoreCase("Empresa")){String tel=requerido("Teléfono de la empresa:");Contacto ct=new Contacto(requerido("Nombre del contacto:") ,requerido("Cargo del contacto:"),requerido("Teléfono del contacto:"),requerido("Correo del contacto:"));c=new Empresa(id,nombre,ciudad,tel,ct);}
        else if(tipo.equalsIgnoreCase("PersonaNatural")){c=new PersonaNatural(id,nombre,ciudad,requerido("Correo electrónico:"));}else throw new IllegalArgumentException("Tipo de cliente inválido.");
        for(Cliente existente:losClientes)if(existente.getNumeroIdentificacion().equals(id))return existente;losClientes.add(c);return c;
    }
    public static String generarCodigoProyecto(ArrayList<Proyecto> datos){int anio=LocalDate.now().getYear(),max=0;for(Proyecto p:datos)if(p.getCodigo().startsWith(anio+"-")){try{max=Math.max(max,Integer.parseInt(p.getCodigo().substring(5)));}catch(NumberFormatException ignored){}}return String.format("%04d-%03d",anio,max+1);}
    private void uiRegistrarProyecto(){String titulo=requerido("Título del proyecto (20 a 100 caracteres):");if(titulo.length()<20||titulo.length()>100)throw new IllegalArgumentException("El título debe tener entre 20 y 100 caracteres.");LocalDate fecha;try{fecha=LocalDate.parse(requerido("Fecha de inicio (AAAA-MM-DD):"));}catch(DateTimeParseException e){throw new IllegalArgumentException("Fecha inválida. Use AAAA-MM-DD.");}
        String unidad=requerido("Unidad de duración (semanas/meses/años):").toLowerCase();int min=1,max=switch(unidad){case "semanas"->3;case "meses"->35;case "años"->3;default->0;};if(max==0)throw new IllegalArgumentException("Unidad inválida.");int dur=entero("Duración estimada (semanas 1-3; meses 1-11 o 13-23 o 25-35; años 1-3):",min,max);if(unidad.equals("meses")&&(dur==12||dur==24))throw new IllegalArgumentException("Para meses use 1-11, 13-23 o 25-35.");
        Cliente cliente=pedirCliente();Empleado gerente=pedirEmpleado();Proyecto pr=new Proyecto(generarCodigoProyecto(losProyectos),titulo,fecha,unidad,(byte)dur,cliente,gerente,"INICIADO");losProyectos.add(pr);mostrar("Proyecto registrado correctamente.\n"+pr);
    }
    private Proyecto buscar(String codigo){return losProyectos.stream().filter(p->p.getCodigo().equalsIgnoreCase(codigo)).findFirst().orElse(null);}
    private void uiConsultarCodigo(){String c=requerido("Código del proyecto:");Proyecto p=buscar(c);mostrar(p==null?"No existe un proyecto con ese código.":p.toString());}
    private void uiConsultarGerente(){mostrar(consultarGerenteProyectoDadoCodigo(losProyectos,requerido("Código del proyecto:")));}
    private void uiConsultarEquipo(){mostrar(consultarEquipoProyectoDadoCodigo(losProyectos,requerido("Código del proyecto:")));}
    private void uiPorGerente(){mostrar(consultarProyectosDadoNumIDEGerente(losProyectos,requerido("Identificación del gerente:")));}
    private void uiPorCliente(){mostrar(consultarProyectosDadoNumIDECliente(losProyectos,requerido("Identificación del cliente:")));}
    private void uiPorTipo(){mostrar(consultarProyectosDadoTipoCliente(losProyectos,requerido("Tipo de cliente (Empresa/PersonaNatural):")));}
    private void uiRegistrarIntegrante(){mostrar(registrarIntegranteProyectoDadoCodigo(losProyectos,requerido("Código del proyecto:"),pedirEmpleado()));}
    private void uiRegistrarEquipo(){String cod=requerido("Código del proyecto:");Proyecto p=buscar(cod);if(p==null)throw new IllegalArgumentException("Proyecto no encontrado.");int n=entero("Cantidad de integrantes:",1,100);for(int i=0;i<n;i++)p.agregarIntegrante(pedirEmpleado());mostrar("Equipo registrado.\n"+p.getSuEquipo());}
    private void uiEliminarIntegrante(){mostrar(eliminarIntegranteProyectoDadoCodigo(losProyectos,requerido("Código del proyecto:"),requerido("Identificación del integrante:")));}
    private void uiActualizarEstado(){String cod=requerido("Código del proyecto:");String estado=requerido("Nuevo estado (INICIADO, EN CURSO, CANCELADO, FINALIZADO, SUSPENDIDO, APLAZADO):").toUpperCase();if(!ESTADOS.contains(estado))throw new IllegalArgumentException("Estado no permitido.");Proyecto p=buscar(cod);if(p==null)throw new IllegalArgumentException("Proyecto no encontrado.");p.setEstado(estado);mostrar("Estado actualizado: "+estado);}

    public static ArrayList<Proyecto> registrarProyecto(ArrayList<Proyecto> datos){
        if(datos==null)datos=new ArrayList<>();
        try {
            String titulo=JOptionPane.showInputDialog("Título del proyecto (20 a 100 caracteres):");
            if(titulo==null)return datos; if(titulo.trim().length()<20||titulo.trim().length()>100)throw new IllegalArgumentException("El título debe tener entre 20 y 100 caracteres.");
            LocalDate fecha=LocalDate.parse(JOptionPane.showInputDialog("Fecha de inicio (AAAA-MM-DD):"));
            String unidad=JOptionPane.showInputDialog("Unidad (semanas/meses/años):").toLowerCase();
            int dur=Integer.parseInt(JOptionPane.showInputDialog("Duración estimada:"));
            int max=switch(unidad){case "semanas"->3;case "meses"->35;case "años"->3;default->0;};
            if(max==0||dur<1||dur>max||(unidad.equals("meses")&&(dur==12||dur==24)))throw new IllegalArgumentException("Duración o unidad no válida.");
            String tipo=JOptionPane.showInputDialog("Tipo de cliente (Empresa/PersonaNatural):");
            String id=JOptionPane.showInputDialog("Identificación del cliente:");String nombre=JOptionPane.showInputDialog("Nombre o razón social:");String ciudad=JOptionPane.showInputDialog("Ciudad:");
            Cliente cliente;
            if("Empresa".equalsIgnoreCase(tipo)){String tel=JOptionPane.showInputDialog("Teléfono de la empresa:");Contacto ct=new Contacto(JOptionPane.showInputDialog("Nombre del contacto:"),JOptionPane.showInputDialog("Cargo:"),JOptionPane.showInputDialog("Teléfono del contacto:"),JOptionPane.showInputDialog("Correo del contacto:"));cliente=new Empresa(id,nombre,ciudad,tel,ct);}
            else if("PersonaNatural".equalsIgnoreCase(tipo))cliente=new PersonaNatural(id,nombre,ciudad,JOptionPane.showInputDialog("Correo electrónico:"));
            else throw new IllegalArgumentException("Tipo de cliente inválido.");
            String gid=JOptionPane.showInputDialog("Identificación del gerente:");Empleado gerente=new Empleado(gid,JOptionPane.showInputDialog("Nombre del gerente:"),JOptionPane.showInputDialog("Título profesional:"),Byte.parseByte(JOptionPane.showInputDialog("Años de experiencia:")));
            datos.add(new Proyecto(generarCodigoProyecto(datos),titulo.trim(),fecha,unidad,(byte)dur,cliente,gerente,"INICIADO"));
            JOptionPane.showMessageDialog(null,"Proyecto registrado correctamente.");
        }catch(Exception ex){JOptionPane.showMessageDialog(null,"No se pudo registrar: "+ex.getMessage());}
        return datos;
    }
    public static String consultarTodosProyectos(ArrayList<Proyecto> datos){return consultar(datos,p->true,"No hay proyectos registrados.");}
    public static String consultarProyectosEnCurso(ArrayList<Proyecto> datos){return consultar(datos,p->p.getEstado().equalsIgnoreCase("EN CURSO"),"No hay proyectos en curso.");}
    public static String consultarPrimerProyecto(ArrayList<Proyecto> datos){return datos==null||datos.isEmpty()?"No hay proyectos registrados.":datos.get(0).toString();}
    public static String consultarUltimoProyecto(ArrayList<Proyecto> datos){return datos==null||datos.isEmpty()?"No hay proyectos registrados.":datos.get(datos.size()-1).toString();}
    public static String consultarProyectoDadoCodigo(ArrayList<Proyecto> datos,String codigo){return consultar(datos,p->p.getCodigo().equalsIgnoreCase(codigo),"No se encontró el proyecto.");}
    public static String consultarGerenteProyectoDadoCodigo(ArrayList<Proyecto> datos,String codigo){Proyecto p=datos==null?null:datos.stream().filter(x->x.getCodigo().equalsIgnoreCase(codigo)).findFirst().orElse(null);return p==null?"Proyecto no encontrado.":"GERENTE DEL PROYECTO "+p.getTitulo()+"\n"+p.getSuGerente();}
    public static String consultarEquipoProyectoDadoCodigo(ArrayList<Proyecto> datos,String codigo){Proyecto p=datos==null?null:datos.stream().filter(x->x.getCodigo().equalsIgnoreCase(codigo)).findFirst().orElse(null);return p==null?"Proyecto no encontrado.":"EQUIPO DEL PROYECTO "+p.getTitulo()+"\n"+(p.getSuEquipo().isEmpty()?"El proyecto no tiene integrantes.":String.join("\n",p.getSuEquipo().stream().map(Object::toString).toList()));}
    // Sobrecarga interactiva con la firma del diagrama UML.
    public static String registrarIntegranteProyectoDadoCodigo(ArrayList<Proyecto> datos,String codigo){
        String id=JOptionPane.showInputDialog("Identificación del integrante:"); if(id==null||id.isBlank())return "Operación cancelada.";
        String nombre=JOptionPane.showInputDialog("Nombre completo:"); if(nombre==null||nombre.isBlank())return "Operación cancelada.";
        String titulo=JOptionPane.showInputDialog("Título profesional:"); if(titulo==null||titulo.isBlank())return "Operación cancelada.";
        try { byte exp=Byte.parseByte(JOptionPane.showInputDialog("Años de experiencia:")); return registrarIntegranteProyectoDadoCodigo(datos,codigo,new Empleado(id,nombre,titulo,exp)); }
        catch(Exception ex){return "Datos del empleado inválidos.";}
    }
    public static String registrarIntegranteProyectoDadoCodigo(ArrayList<Proyecto> datos,String codigo,Empleado nuevo){Proyecto p=datos==null?null:datos.stream().filter(x->x.getCodigo().equalsIgnoreCase(codigo)).findFirst().orElse(null);if(p==null)return "Proyecto no encontrado.";try{p.agregarIntegrante(nuevo);return "Integrante agregado correctamente.";}catch(IllegalArgumentException e){return e.getMessage();}}
    // Sobrecarga programática para registrar el equipo completo.
    public static String registrarEquipoProyectoDadoCodigo(ArrayList<Proyecto> datos,String codigo){
        String cantidad=JOptionPane.showInputDialog("Cantidad de integrantes:"); if(cantidad==null)return "Operación cancelada.";
        try { int n=Integer.parseInt(cantidad); if(n<1||n>100)return "La cantidad debe estar entre 1 y 100."; ArrayList<Empleado> equipo=new ArrayList<>();
            for(int i=0;i<n;i++){String id=JOptionPane.showInputDialog("Identificación del integrante "+(i+1));String nom=JOptionPane.showInputDialog("Nombre completo:");String tit=JOptionPane.showInputDialog("Título profesional:");byte exp=Byte.parseByte(JOptionPane.showInputDialog("Años de experiencia:"));equipo.add(new Empleado(id,nom,tit,exp));}
            return registrarEquipoProyectoDadoCodigo(datos,codigo,equipo);
        }catch(Exception ex){return "Datos del equipo inválidos.";}
    }
    public static String registrarEquipoProyectoDadoCodigo(ArrayList<Proyecto> datos,String codigo,ArrayList<Empleado> equipo){Proyecto p=datos==null?null:datos.stream().filter(x->x.getCodigo().equalsIgnoreCase(codigo)).findFirst().orElse(null);if(p==null)return "Proyecto no encontrado.";try{for(Empleado e:equipo)p.agregarIntegrante(e);return "Equipo registrado correctamente.";}catch(IllegalArgumentException e){return e.getMessage();}}
    public static String actualizarEstadoProyectoDadoCodigo(ArrayList<Proyecto> datos,String codigo){
        String estado=JOptionPane.showInputDialog("Nuevo estado (INICIADO, EN CURSO, CANCELADO, FINALIZADO, SUSPENDIDO, APLAZADO):");
        return estado==null?"Operación cancelada.":actualizarEstadoProyectoDadoCodigo(datos,codigo,estado);
    }
    public static String actualizarEstadoProyectoDadoCodigo(ArrayList<Proyecto> datos,String codigo,String estado){Proyecto p=datos==null?null:datos.stream().filter(x->x.getCodigo().equalsIgnoreCase(codigo)).findFirst().orElse(null);if(p==null)return "Proyecto no encontrado.";if(!ESTADOS.contains(estado.toUpperCase()))return "Estado no permitido.";p.setEstado(estado.toUpperCase());return "Estado actualizado.";}
    public static String eliminarIntegranteProyectoDadoCodigo(ArrayList<Proyecto> datos,String codigo){
        String id=JOptionPane.showInputDialog("Identificación del integrante que desea eliminar:");
        return id==null?"Operación cancelada.":eliminarIntegranteProyectoDadoCodigo(datos,codigo,id);
    }
    public static String eliminarIntegranteProyectoDadoCodigo(ArrayList<Proyecto> datos,String codigo,String id){Proyecto p=datos==null?null:datos.stream().filter(x->x.getCodigo().equalsIgnoreCase(codigo)).findFirst().orElse(null);if(p==null)return "Proyecto no encontrado.";return p.eliminarIntegrante(id)==null?"No se encontró el integrante.":"Integrante eliminado correctamente.";}
    public static String consultarProyectosDadoNumIDEGerente(ArrayList<Proyecto> datos,String id){return consultar(datos,p->p.getSuGerente().getNumeroIdentificacion().equals(id),"No se encontraron proyectos para ese gerente.");}
    public static String consultarProyectosDadoNumIDECliente(ArrayList<Proyecto> datos,String id){return consultar(datos,p->p.getSuCliente().getNumeroIdentificacion().equals(id),"No se encontraron proyectos para ese cliente.");}
    public static String consultarProyectosDadoTipoCliente(ArrayList<Proyecto> datos,String tipo){return consultar(datos,p->p.getSuCliente().getTipoCliente().equalsIgnoreCase(tipo),"No se encontraron proyectos para ese tipo de cliente.");}
    private interface Filtro{boolean acepta(Proyecto p);} private static String consultar(ArrayList<Proyecto> datos,Filtro f,String vacio){if(datos==null||datos.isEmpty())return "No hay proyectos registrados.";StringBuilder sb=new StringBuilder();for(Proyecto p:datos)if(f.acepta(p))sb.append(p).append("\n\n------------------------------\n\n");return sb.length()==0?vacio:sb.toString();}
    public static void generarArchivoDeObjetosProyectos(ArrayList<Proyecto> datos){if(datos==null||datos.isEmpty())throw new IllegalStateException("No hay proyectos para guardar.");try(ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(ARCHIVO_PROYECTOS))){out.writeObject(datos);JOptionPane.showMessageDialog(null,"Archivo datosProyectos generado correctamente.");}catch(IOException e){throw new IllegalStateException("No fue posible guardar los proyectos: "+e.getMessage());}}
    public static ArrayList<Proyecto> leerArchivoDeObjetosProyectos(){try(ObjectInputStream in=new ObjectInputStream(new FileInputStream(ARCHIVO_PROYECTOS))){return (ArrayList<Proyecto>)in.readObject();}catch(IOException|ClassNotFoundException e){JOptionPane.showMessageDialog(null,"No fue posible leer datosProyectos: "+e.getMessage());return new ArrayList<>();}}
    public static void generarArchivoDeObjetosClientes(ArrayList<Proyecto> datos){if(datos==null||datos.isEmpty())throw new IllegalStateException("No hay proyectos para guardar clientes.");LinkedHashMap<String,Cliente> unicos=new LinkedHashMap<>();for(Proyecto p:datos)unicos.put(p.getSuCliente().getNumeroIdentificacion(),p.getSuCliente());try(ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(ARCHIVO_CLIENTES))){out.writeObject(new ArrayList<>(unicos.values()));JOptionPane.showMessageDialog(null,"Archivo datosClientes generado correctamente.");}catch(IOException e){throw new IllegalStateException("No fue posible guardar los clientes: "+e.getMessage());}}
    @SuppressWarnings("unchecked") public static ArrayList<Cliente> leerArchivoDeObjetosClientes(){try(ObjectInputStream in=new ObjectInputStream(new FileInputStream(ARCHIVO_CLIENTES))){return (ArrayList<Cliente>)in.readObject();}catch(IOException|ClassNotFoundException e){JOptionPane.showMessageDialog(null,"No fue posible leer datosClientes: "+e.getMessage());return new ArrayList<>();}}
    public static void main(String[] args){SwingUtilities.invokeLater(()->new UsaProyecto().setVisible(true));}
}
