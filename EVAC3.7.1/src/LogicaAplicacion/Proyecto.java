/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicaAplicacion;

import java.awt.FileDialog;
import java.awt.HeadlessException;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.StringTokenizer;
import javax.swing.*;


/**
 *
 * @author Cristian
 */
public class Proyecto { 
    
    private static Proyecto proyectoActual;
    private int tamañox;
    private int tamañoy;
    private int propagacionHumo;
    private int propagacionFuego;
    private int visualizacion;
    private String nombreProyecto;
    private String notasProyecto;
    private AutomataCelular ac;
    private Point puntoInicio;
    private int proyectoValido;
    private int cantidadPersonas;
    private int cantidadSalidas;
    private LinkedList salidas;
    private LinkedList sensores;
    private int cantidadSensores;
    private int potenciaSensor;
    private static final int potenciaConst =10;
    
    //CRISTIAN 07/08/2018
    private int velocidadMinima;
    private int velocidadMaxima;
    private int tiempoReaccionMinimo;
    private int tiempoReaccionMaximo;
    //CRISTIAN 07/08/2018
    
    private Proyecto(int tamañox, int tamañoy, int visualizacion, int propagacionHumo, int propagacionFuego, int personas, int salidas, int valido, AutomataCelular ac, String nombreProyecto, String notasProyecto, int cantSensores, LinkedList listSensores, int potenciaSensores) {
        this.tamañox = tamañox;
        this.tamañoy = tamañoy;
        this.visualizacion = visualizacion;
        this.propagacionHumo = propagacionHumo;
        this.propagacionFuego = propagacionFuego;
        this.cantidadPersonas=personas;
        this.cantidadSalidas=salidas;
        this.nombreProyecto = nombreProyecto;
        this.notasProyecto = notasProyecto;
        this.puntoInicio = new Point(-1, -1);
        this.ac = ac;
        this.proyectoValido=valido;
        this.salidas=new LinkedList();
        this.cantidadSensores = cantSensores;
        this.sensores = listSensores;
        this.potenciaSensor = potenciaSensores;
    }

    private Proyecto() {
        this.tamañox = 10;
        this.tamañoy = 10;
        this.visualizacion = 1;
        this.propagacionHumo = 1;
        this.propagacionFuego = 1;
        this.nombreProyecto = "SIN NOMBRE";
        this.notasProyecto = "INGRESE NOTAS AQUI";
        this.proyectoValido=0;
        this.cantidadPersonas=0;
        this.cantidadSalidas=0;
        this.puntoInicio = new Point(-1, -1);
        this.ac = new AutomataCelular();
        this.salidas=new LinkedList();
        this.cantidadSensores = 0;
        this.sensores = new LinkedList();
        this.potenciaSensor = potenciaConst;
    }

    public static Proyecto getProyecto() {   //Proyecto nuevo
        if (!Proyecto.hayProyectoActual()) {
            proyectoActual = new Proyecto();
        }
        return proyectoActual;
    }
    
    public static boolean proyectoValido(){
        if (!Proyecto.hayProyectoActual()) {
            return false;
        }
        else{
            if(Proyecto.getProyecto().proyectoValido==0){
                return(false);
            }
            return(true);
        }
    }
    
    public static void setProyectoBatch(String ruta){ //Lee el proyecto desde el archivo
    try {
            FileReader fr = new FileReader(ruta);
            BufferedReader br = new BufferedReader(fr);
            String leerArchivo = br.readLine();
            StringTokenizer filtroDatosGenerales = new StringTokenizer(leerArchivo, ",");
            int tamañox = new Integer(filtroDatosGenerales.nextToken());
            int tamañoy = new Integer(filtroDatosGenerales.nextToken());
            int visualizacion = new Integer(filtroDatosGenerales.nextToken());
            int propagacionFuego = new Integer(filtroDatosGenerales.nextToken());
            int propagacionHumo = new Integer(filtroDatosGenerales.nextToken());
            int personas = new Integer(filtroDatosGenerales.nextToken());
            int salidas = new Integer(filtroDatosGenerales.nextToken());
            int valido = new Integer(filtroDatosGenerales.nextToken());
            int alto = new Integer(filtroDatosGenerales.nextToken());
            int ancho = new Integer(filtroDatosGenerales.nextToken());
            int cantSensores = new Integer(filtroDatosGenerales.nextToken());
            int potenciaSensores = new Integer(filtroDatosGenerales.nextToken());
            String nombreProyecto = filtroDatosGenerales.nextToken();
            String notasProyecto = filtroDatosGenerales.nextToken();
            LinkedList listSensores = new LinkedList();
            while (!notasProyecto.endsWith("@F@")) {
                notasProyecto = notasProyecto + "\n" + br.readLine();
            }
            //CODIGO ANTERIOR
            //notasProyecto = notasProyecto.substring(0, notasProyecto.length() - 3);
            
            //EN LAS NOTAS DEL PROYECTO VAMOS A GUARDAR EL PUNTO DE INICIO 
            //CRISTIAN 19/04/2018 y 09/09/2018
            Point puntoInicio = new Point(-1,-1);
            int velMaxima=3, velMinima=3, reaccionMinima=0, reaccionMaxima=0;
            
            if(notasProyecto.contains("(") ){
               String stringInicio = notasProyecto.substring( notasProyecto.indexOf("(") + 1 , notasProyecto.indexOf(")") );
                String stringVelocidad;
                String stringReaccion;
                StringTokenizer filtroInicio;
                if(notasProyecto.contains("<")){
                    stringVelocidad=notasProyecto.substring( notasProyecto.indexOf("<") + 1 , notasProyecto.indexOf(">") ); 
                    filtroInicio = new StringTokenizer(stringVelocidad, "-");
                    velMinima=Integer.parseInt(filtroInicio.nextToken());
                    velMaxima=Integer.parseInt(filtroInicio.nextToken());
                }
                if(notasProyecto.contains("{")){
                    stringReaccion=notasProyecto.substring( notasProyecto.indexOf("{") + 1 , notasProyecto.indexOf("}") );
                    filtroInicio = new StringTokenizer(stringReaccion, "-");
                    reaccionMinima=Integer.parseInt(filtroInicio.nextToken());
                    reaccionMaxima=Integer.parseInt(filtroInicio.nextToken());
                }

                notasProyecto = notasProyecto.substring(0, notasProyecto.indexOf("("));
                filtroInicio = new StringTokenizer(stringInicio, "-");
                puntoInicio = new Point( new Integer(filtroInicio.nextToken())  ,  new Integer(filtroInicio.nextToken())  ); 
            }
            else{
                notasProyecto = notasProyecto.substring(0, notasProyecto.length() - 3);
            }

            
            AutomataCelular automataCelular = new AutomataCelular(alto, ancho);
            for (int i = 0; i < alto; ++i) {
                leerArchivo = br.readLine();
                StringTokenizer filtroAC = new StringTokenizer(leerArchivo, "/");
                for (int j = 0; j < ancho; ++j) {
                    Celda celda;
                    String relleno = filtroAC.nextToken();
                    relleno = relleno.substring(relleno.indexOf("[") + 1, relleno.indexOf("]"));
                    StringTokenizer filtroACE = new StringTokenizer(relleno, ",");
                    int estadoAC = new Integer(filtroACE.nextToken());
                    int velocidadAC = new Integer(filtroACE.nextToken());
                    int combustionAC = new Integer(filtroACE.nextToken());
                    Double nivelHumoAC = new Double(filtroACE.nextToken());
                    Double nivelFuegoAC = new Double(filtroACE.nextToken());
                    int tipoSensorAC = new Integer(filtroACE.nextToken());
                    celda = new Celda(estadoAC, velocidadAC, combustionAC, nivelHumoAC, nivelFuegoAC);
                    celda.setTipoSensor(tipoSensorAC);
                    if(tipoSensorAC > 0 ){
                        celda.setSensor(new Sensor(potenciaSensores, i, j, tipoSensorAC));
                        listSensores.add(celda.getSensor());
                    }
                    if (estadoAC == 6){//La celda tiene un agente
                        int tipoAgente = new Integer(filtroACE.nextToken());
                        Agente agente=new Agente(tipoAgente);
                        agente.setUbicacion(new Point(i,j));
                        celda.setAgente(agente);
                    }
                    automataCelular.setCelda(i, j, celda);
                }
            }


            proyectoActual = new Proyecto(tamañox, tamañoy, visualizacion, propagacionHumo, propagacionFuego, personas, salidas, valido, automataCelular, nombreProyecto, notasProyecto, cantSensores ,listSensores,potenciaSensores);
            
            //EN LAS NOTAS DEL PROYECTO VAMOS A GUARDAR EL PUNTO DE INICIO 
            //CRISTIAN 19/04/2018
            //EN LAS NOTAS DEL PROYECTO VAMOS A GUARDAR EL PUNTO DE INICIO 
            //CRISTIAN 19/04/2018 y 09/08/2018
            proyectoActual.setPuntoInicio(puntoInicio);
            proyectoActual.setVelocidadMinima(velMinima);
            proyectoActual.setVelocidadMaxima(velMaxima);
            proyectoActual.setTiempoReaccionMinimo(reaccionMinima);
            proyectoActual.setTiempoReaccionMaximo(reaccionMaxima);

        }
        catch (HeadlessException | IOException | NumberFormatException ex) {
          System.out.println("ERROR EN APERTURA DE ARCHIVO");
        }
    }
    
    public static void setProyectoArchivo(){ //Lee el proyecto desde el archivo
    try {
         JFrame abrir = new JFrame("ABRIR");
         FileDialog dialogoAbrir = new FileDialog(abrir, "Abrir Archivo", 0);
         dialogoAbrir.setFile("*.evc");
         dialogoAbrir.setVisible(true);
         if (dialogoAbrir.getDirectory() != null && dialogoAbrir.getFile() != null) {
            FileReader fr = new FileReader(dialogoAbrir.getDirectory() + dialogoAbrir.getFile());
            BufferedReader br = new BufferedReader(fr);
            String leerArchivo = br.readLine();
            StringTokenizer filtroDatosGenerales = new StringTokenizer(leerArchivo, ",");
            int tamañox = new Integer(filtroDatosGenerales.nextToken());
            int tamañoy = new Integer(filtroDatosGenerales.nextToken());
            int visualizacion = new Integer(filtroDatosGenerales.nextToken());
            int propagacionFuego = new Integer(filtroDatosGenerales.nextToken());
            int propagacionHumo = new Integer(filtroDatosGenerales.nextToken());
            int personas = new Integer(filtroDatosGenerales.nextToken());
            int salidas = new Integer(filtroDatosGenerales.nextToken());
            int valido = new Integer(filtroDatosGenerales.nextToken());
            int alto = new Integer(filtroDatosGenerales.nextToken());
            int ancho = new Integer(filtroDatosGenerales.nextToken());
            int cantSensores = new Integer(filtroDatosGenerales.nextToken());
            int potenciaSensores = new Integer(filtroDatosGenerales.nextToken());
            String nombreProyecto = filtroDatosGenerales.nextToken();
            String notasProyecto = filtroDatosGenerales.nextToken();
             LinkedList listSensores = new LinkedList();
             while (!notasProyecto.endsWith("@F@")) {
                notasProyecto = notasProyecto + "\n" + br.readLine();
            }
            //notasProyecto = notasProyecto.substring(0, notasProyecto.length() - 3);
            
            
            //EN LAS NOTAS DEL PROYECTO VAMOS A GUARDAR EL PUNTO DE INICIO 
            //CRISTIAN 19/04/2018 y 09/09/2018
            Point puntoInicio = new Point(-1,-1);
            int velMaxima=3, velMinima=3, reaccionMinima=0, reaccionMaxima=0;
            
            if(notasProyecto.contains("(") ){
                String stringInicio = notasProyecto.substring( notasProyecto.indexOf("(") + 1 , notasProyecto.indexOf(")") );
                String stringVelocidad;
                String stringReaccion;
                StringTokenizer filtroInicio;
                if(notasProyecto.contains("<")){
                    stringVelocidad=notasProyecto.substring( notasProyecto.indexOf("<") + 1 , notasProyecto.indexOf(">") ); 
                    filtroInicio = new StringTokenizer(stringVelocidad, "-");
                    velMinima=Integer.parseInt(filtroInicio.nextToken());
                    velMaxima=Integer.parseInt(filtroInicio.nextToken());
                }
                if(notasProyecto.contains("{")){
                    stringReaccion=notasProyecto.substring( notasProyecto.indexOf("{") + 1 , notasProyecto.indexOf("}") );
                    filtroInicio = new StringTokenizer(stringReaccion, "-");
                    reaccionMinima=Integer.parseInt(filtroInicio.nextToken());
                    reaccionMaxima=Integer.parseInt(filtroInicio.nextToken());
                }

                notasProyecto = notasProyecto.substring(0, notasProyecto.indexOf("("));
                filtroInicio = new StringTokenizer(stringInicio, "-");
                puntoInicio = new Point( new Integer(filtroInicio.nextToken())  ,  new Integer(filtroInicio.nextToken())  );    
            }
            else{
                notasProyecto = notasProyecto.substring(0, notasProyecto.length() - 3);
            }
            
            
            AutomataCelular automataCelular = new AutomataCelular(alto, ancho);
            for (int i = 0; i < alto; ++i) {
                leerArchivo = br.readLine();
                StringTokenizer filtroAC = new StringTokenizer(leerArchivo, "/");
                for (int j = 0; j < ancho; ++j) {
                    Celda celda;
                    String relleno = filtroAC.nextToken();
                    relleno = relleno.substring(relleno.indexOf("[") + 1, relleno.indexOf("]"));
                    StringTokenizer filtroACE = new StringTokenizer(relleno, ",");
                    int estadoAC = new Integer(filtroACE.nextToken());
                    int velocidadAC = new Integer(filtroACE.nextToken());
                    int combustionAC = new Integer(filtroACE.nextToken());
                    Double nivelHumoAC = new Double(filtroACE.nextToken());
                    Double nivelFuegoAC = new Double(filtroACE.nextToken());
                    int tipoSensorAC = new Integer(filtroACE.nextToken());
                    celda = new Celda(estadoAC, velocidadAC, combustionAC, nivelHumoAC, nivelFuegoAC);
                    celda.setTipoSensor(tipoSensorAC);
                    if(tipoSensorAC > 100 ){
                        //JOptionPane.showMessageDialog(null, "tipo"+tipoSensorAC);
                        celda.setSensor(new Sensor(potenciaSensores, i, j, tipoSensorAC));
                        listSensores.add(celda.getSensor());
                    }
                    if (estadoAC == 6){//La celda tiene un agente
                        int tipoAgente = new Integer(filtroACE.nextToken());
                        Agente agente=new Agente(tipoAgente);
                        agente.setUbicacion(new Point(i,j));
                        celda.setAgente(agente);
                    }
                    automataCelular.setCelda(i, j, celda);
                }
            }
            proyectoActual = new Proyecto(tamañox, tamañoy, visualizacion, propagacionHumo, propagacionFuego, personas, salidas, valido, automataCelular, nombreProyecto, notasProyecto,cantSensores,listSensores,potenciaSensores);
            
            //EN LAS NOTAS DEL PROYECTO VAMOS A GUARDAR EL PUNTO DE INICIO 
            //CRISTIAN 19/04/2018 y 09/08/2018
            proyectoActual.setPuntoInicio(puntoInicio);
            proyectoActual.setVelocidadMinima(velMinima);
            proyectoActual.setVelocidadMaxima(velMaxima);
            proyectoActual.setTiempoReaccionMinimo(reaccionMinima);
            proyectoActual.setTiempoReaccionMaximo(reaccionMaxima);
          }
        }
        catch (HeadlessException | IOException | NumberFormatException ex) {
          System.out.println("ERROR EN APERTURA DE ARCHIVO");
        }
    }
    
    public static void setProyectoArchivoCompatibilidad() {
    try {
            JFrame abrir = new JFrame("ABRIR");
            FileDialog dialogoAbrir = new FileDialog(abrir, "Abrir Archivo", 0);
            dialogoAbrir.setFile("*.evc");
            dialogoAbrir.setVisible(true);
            FileReader fr = new FileReader(dialogoAbrir.getDirectory() + dialogoAbrir.getFile());
            BufferedReader br = new BufferedReader(fr);
            if (dialogoAbrir.getDirectory() != null && dialogoAbrir.getFile() != null) {
                LinkedList listSensores = new LinkedList();
                String leerArchivo = br.readLine();
                StringTokenizer filtroDatosGenerales = new StringTokenizer(leerArchivo, ",");
                int tamañox = new Integer(filtroDatosGenerales.nextToken());
                int tamañoy = new Integer(filtroDatosGenerales.nextToken());
                int visualizacion = new Integer(filtroDatosGenerales.nextToken());
                int propagacionFuego = new Integer(filtroDatosGenerales.nextToken());
                int propagacionHumo = new Integer(filtroDatosGenerales.nextToken());
                int personas = new Integer(filtroDatosGenerales.nextToken());
                int salidas = new Integer(filtroDatosGenerales.nextToken());
                int valido = new Integer(filtroDatosGenerales.nextToken());
                int alto = new Integer(filtroDatosGenerales.nextToken());
                int ancho = new Integer(filtroDatosGenerales.nextToken());
                int cantSensores = new Integer(filtroDatosGenerales.nextToken());
                int potenciaSensores = new Integer(filtroDatosGenerales.nextToken());
                String nombreProyecto = filtroDatosGenerales.nextToken();
                String notasProyecto = filtroDatosGenerales.nextToken();
                while (!notasProyecto.endsWith("@F@")) {
                    notasProyecto = notasProyecto + "\n" + br.readLine();
                }
                notasProyecto = notasProyecto.substring(0, notasProyecto.length() - 3);
                AutomataCelular guardadoAC = new AutomataCelular(alto, ancho);
                for (int i = 0; i < alto; ++i) {
                    leerArchivo = br.readLine();
                    StringTokenizer filtroAC = new StringTokenizer(leerArchivo, "/");
                    for (int j = 0; j < ancho; ++j) {
                        Celda estado;
                        String relleno = filtroAC.nextToken();
                        relleno = relleno.substring(relleno.indexOf("[") + 1, relleno.indexOf("]"));
                        StringTokenizer filtroACE = new StringTokenizer(relleno, ",");
                        int estadoAC = new Integer(filtroACE.nextToken());
                        int velocidadAC = new Integer(filtroACE.nextToken());
                        int combustionAC = new Integer(filtroACE.nextToken());
                        int nivelHumoAC = new Integer(filtroACE.nextToken());
                        int nivelFuegoAC = new Integer(filtroACE.nextToken());
                        int tipoSensorAC = new Integer(filtroACE.nextToken());
                        estado = new Celda(estadoAC, velocidadAC, combustionAC, nivelHumoAC, nivelFuegoAC);
                        estado.setTipoSensor(tipoSensorAC);
                        if(tipoSensorAC > 0 ){
                            estado.setSensor(new Sensor(potenciaSensores, i, j, tipoSensorAC));
                            listSensores.add(estado.getSensor());
                        }
                        if (estadoAC == 6){//La celda tiene un agente
                            int dañoAC = new Integer(filtroACE.nextToken());
                            int tipoAgente = new Integer(filtroACE.nextToken());
                            Agente agente=new Agente(tipoAgente);
                            estado.setAgente(agente);
                        }
                        guardadoAC.setCelda(i, j, estado);
                    }
                }
                proyectoActual = new Proyecto(tamañox, tamañoy, visualizacion, propagacionHumo, propagacionFuego, personas, salidas, valido, guardadoAC, nombreProyecto, notasProyecto,cantSensores,listSensores,potenciaSensores);
            }
        }
        catch (HeadlessException | IOException | NumberFormatException ex) {
                    System.out.println("ERROR EN APERTURA DE ARCHIVO");
        }
    }
        
    public static void guardarProyecto(){
        String almacenarArchivo;
        String nombreArchivo;
        String puntoIncio;
        String velocidad;
        String reaccion;
        FileWriter fw;
        try {
            JFrame guardar = new JFrame("GUARDAR");
            FileDialog dialogoGuardar = new FileDialog(guardar, "Guardar Archivo", 1);
            dialogoGuardar.setFile("*.evc");
            dialogoGuardar.setVisible(true);
            nombreArchivo = dialogoGuardar.getFile();
            if (dialogoGuardar.getDirectory() != null && dialogoGuardar.getFile() != null) {
                fw = nombreArchivo.endsWith(".evc") ? new FileWriter(dialogoGuardar.getDirectory() + dialogoGuardar.getFile()) : new FileWriter(dialogoGuardar.getDirectory() + dialogoGuardar.getFile() + ".evc");
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw);
                almacenarArchivo = "";
                almacenarArchivo = Integer.toString(Proyecto.getProyecto().getTamañox()) + ",";
                almacenarArchivo = almacenarArchivo + Integer.toString(Proyecto.getProyecto().getTamañoy()) + ",";
                almacenarArchivo = almacenarArchivo + Integer.toString(Proyecto.getProyecto().getVisualizacion()) + ",";
                almacenarArchivo = almacenarArchivo + Integer.toString(Proyecto.getProyecto().getPropagacionFuego())+ ",";
                almacenarArchivo = almacenarArchivo + Integer.toString(Proyecto.getProyecto().getPropagacionHumo())+ ",";
                almacenarArchivo = almacenarArchivo + Integer.toString(Proyecto.getProyecto().getCantidadPersonas())+ ",";
                almacenarArchivo = almacenarArchivo + Integer.toString(Proyecto.getProyecto().getCantidadSalidas())+ ",";
                almacenarArchivo = Proyecto.proyectoValido() ? almacenarArchivo + Integer.toString(1) + "," : almacenarArchivo + Integer.toString(0)+ ",";
                almacenarArchivo = almacenarArchivo + Integer.toString(Proyecto.getProyecto().getAc().getAlto()) + ",";
                almacenarArchivo = almacenarArchivo + Integer.toString(Proyecto.getProyecto().getAc().getAncho()) + ",";
                almacenarArchivo = almacenarArchivo + Integer.toString(Proyecto.getProyecto().getCantidadSensores()) + ",";
                almacenarArchivo = almacenarArchivo + Integer.toString(Proyecto.getProyecto().getPotenciaSensor()) + ",";
                String cadenaSinBlancos = "";
                StringTokenizer stTexto = new StringTokenizer(Proyecto.getProyecto().getNombreProyecto());
                while (stTexto.hasMoreElements()) {
                    cadenaSinBlancos = cadenaSinBlancos + stTexto.nextElement();
                }
                almacenarArchivo = cadenaSinBlancos.length() > 0 ? almacenarArchivo + Proyecto.getProyecto().getNombreProyecto() + "," : almacenarArchivo + "SIN NOMBRE,";
                cadenaSinBlancos = "";
                stTexto = new StringTokenizer(Proyecto.getProyecto().getNotasProyecto());
                while (stTexto.hasMoreElements()) {
                    cadenaSinBlancos = cadenaSinBlancos + stTexto.nextElement();
                }
                
                //CRISTIAN 19/04/2018  y 07/058/2018
                puntoIncio="(" + Integer.toString(Proyecto.getProyecto().getPuntoInicio().x) + "-" + Integer.toString(Proyecto.getProyecto().getPuntoInicio().y) + ")";
                velocidad="<" + Proyecto.getProyecto().getVelocidadMinima() +"-" + Proyecto.getProyecto().getVelocidadMaxima()+">";
                reaccion="{" + Proyecto.getProyecto().getTiempoReaccionMinimo() + "-" + Proyecto.getProyecto().getTiempoReaccionMaximo()+"}";
                almacenarArchivo = cadenaSinBlancos.length() > 0 ? almacenarArchivo + Proyecto.getProyecto().getNotasProyecto() + puntoIncio + velocidad + reaccion + "@F@" : almacenarArchivo + "INGRESE NOTAS AQUI" + puntoIncio + velocidad + reaccion + "@F@";
                          
                
                //ASI ESTABA ANTES
                //almacenarArchivo = cadenaSinBlancos.length() > 0 ? almacenarArchivo + Proyecto.getProyecto().getNotasProyecto() + "@F@" : almacenarArchivo + "INGRESE NOTAS AQUI @F@";
                pw.println(almacenarArchivo);
                almacenarArchivo = "";
                for (int i = 0; i < Proyecto.getProyecto().getAc().getAlto(); ++i) {
                    for (int j = 0; j < Proyecto.getProyecto().getAc().getAncho(); ++j) {
                        almacenarArchivo = almacenarArchivo + "[";
                        almacenarArchivo = almacenarArchivo + Integer.toString(Proyecto.getProyecto().getAc().getCelda(i, j).getEstado());
                        almacenarArchivo = almacenarArchivo + "," + Integer.toString(Proyecto.getProyecto().getAc().getCelda(i, j).getNivelTransito());
                        almacenarArchivo = almacenarArchivo + "," + Integer.toString(Proyecto.getProyecto().getAc().getCelda(i, j).getNivelCombustibilidad());
                        almacenarArchivo = almacenarArchivo + "," + Double.toString(Proyecto.getProyecto().getAc().getCelda(i, j).getNivelHumo());
                        almacenarArchivo = almacenarArchivo + "," + Double.toString(Proyecto.getProyecto().getAc().getCelda(i, j).getNivelFuego());

                        almacenarArchivo = almacenarArchivo + "," + Integer.toString(Proyecto.getProyecto().getAc().getCelda(i, j).getTipoSensor());
                        if (Proyecto.getProyecto().getAc().getCelda(i, j).getEstado() == 6) {
                            almacenarArchivo = almacenarArchivo + "," + Integer.toString(Proyecto.getProyecto().getAc().getCelda(i, j).getAgente().getTipo());
                        }
                        almacenarArchivo = almacenarArchivo + "]";
                        almacenarArchivo = almacenarArchivo + "/";
                    }
                    pw.println(almacenarArchivo);
                    almacenarArchivo = "";
                }
                pw.close();
            }
        }
        catch (IOException ex) {
            System.out.println("ERROR EN APERTURA DE ARCHIVO");
        }
    
    }
        
    public static boolean hayProyectoActual() {
        if (proyectoActual == null) {
            return false;
        }
        return true;
    }

    public static void setNullProyectoActual() {
        proyectoActual = null;
    }

    public int getPropagacionFuego() {
        return this.propagacionFuego;
    }

    public void setPropagacionFuego(int propagacionFuego) {
        this.propagacionFuego = propagacionFuego;
    }

    public int getPropagacionHumo() {
        return this.propagacionHumo;
    }

    public void setPropagacionHumo(int propagacionHumo) {
        this.propagacionHumo = propagacionHumo;
    }

    public int getTamañox() {
        return this.tamañox;
    }

    public void setTamañox(int tamañox) {
        this.tamañox = tamañox;
    }

    public int getTamañoy() {
        return this.tamañoy;
    }

    public void setTamañoy(int tamañoy) {
        this.tamañoy = tamañoy;
    }

    public AutomataCelular getAc() {
        return this.ac;
    }

    public void setAc(AutomataCelular ac) {
        this.ac = ac;
    }

    public int getVisualizacion() {
        return this.visualizacion;
    }

    public void setVisualizacion(int visualizacion) {
        this.visualizacion = visualizacion;
    }

    public String getNombreProyecto() {
        return this.nombreProyecto;
    }

    public void setNombreProyecto(String nombrePoyecto) {
        this.nombreProyecto = nombrePoyecto;
    }

    public String getNotasProyecto() {
        return this.notasProyecto;
    }

    public void setNotasProyecto(String notasProyecto) {
        this.notasProyecto = notasProyecto;
    }

    public Point getPuntoInicio() {
        return this.puntoInicio;
    }

    public void setPuntoInicio(Point puntoInicio) {
        this.puntoInicio = puntoInicio;
    }

    public int getProyectoValido() {
        return proyectoValido;
    }

    public void setProyectoValido(int proyectoValido) {
        this.proyectoValido = proyectoValido;
    }

    public int getCantidadPersonas() {
        return cantidadPersonas;
    }

    public void setCantidadPersonas(int cantidadPersonas) {
        this.cantidadPersonas = cantidadPersonas;
    }

    public int getCantidadSalidas() {
        return cantidadSalidas;
    }

    public void setCantidadSalidas(int cantidadSalidas) {
        this.cantidadSalidas = cantidadSalidas;
    }

    public LinkedList getSalidas() {
        return salidas;
    }

    public void setSalidas(LinkedList salidas) {
        this.salidas = salidas;
    }
    
    
    //CRISTIAN 07/08/2018
    public int getVelocidadMinima() {
        return velocidadMinima;
    }

    public void setVelocidadMinima(int velocidadMinima) {
        this.velocidadMinima = velocidadMinima;
    }

    public int getVelocidadMaxima() {
        return velocidadMaxima;
    }

    public void setVelocidadMaxima(int velocidadMaxima) {
        this.velocidadMaxima = velocidadMaxima;
    }

    public int getTiempoReaccionMinimo() {
        return tiempoReaccionMinimo;
    }

    public void setTiempoReaccionMinimo(int tiempoReccionMinimo) {
        this.tiempoReaccionMinimo = tiempoReccionMinimo;
    }

    public int getTiempoReaccionMaximo() {
        return tiempoReaccionMaximo;
    }

    public void setTiempoReaccionMaximo(int tiempoReaccionMaximo) {
        this.tiempoReaccionMaximo = tiempoReaccionMaximo;
    }
    //CRISTIAN 0708/2018


    public LinkedList getListSensores() {
        return sensores;
    }

    public void setSensores(LinkedList sensores) {
        this.sensores = sensores;
    }

    public void addListSensores(Sensor sensor) {
        this.sensores.add(sensor);
    }
    public int getCantidadSensores() {
        return this.sensores.size();
    }

    public void setCantidadSensores(int cantidadSensores) {
        this.cantidadSensores = cantidadSensores;
    }

    public int getPotenciaSensor() {
        return potenciaSensor;
    }

    public void setPotenciaSensor(int potenciaSensor) {
        this.potenciaSensor = potenciaSensor;
    }
}
