/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicaAplicacion;

import java.awt.Point;

/**
 *
 * @author Cristian
 */
public class Agente {

    static private int listId = 0;
    private int id = 0;
    private int daño;
    private int tiempoNoActualizacion;
    private int tiempoEvacuacion;
    private Point ubicacion;
    private Point destino;
    private int respuesta;
    private int velocidad;
    private int contadorVelocidad;
    private int tipo;
    private int salidaElegida;
    private int tiempoInferencia;
    private int desiciones;
    private int demoraReaccion;
    private Comportamiento comportamiento;
    private Memoria memoria;
    //CRISTIAN 30/07/2018
    private int pasosDados;
    private DistanciaSalida salidaSugeridaSensor;

    // 0 - Comportamiento: SALIDA MAS CERCANA  (SMC) 
    // 1 - comportamiento: MEJOR SALIDA ESTIMADA (MSE)
    
    public Agente() {
        this.id = this.listId++;
        this.daño = 0;
        this.tiempoNoActualizacion = 0;
        this.tiempoEvacuacion = 0;
        this.ubicacion = new Point();
        this.ubicacion.x = -1;
        this.ubicacion.y = -1;
        this.destino = new Point();
        this.destino.x = -1;
        this.destino.y = -1;
        this.respuesta = 0;
        
        //VELOCIDAD ALETORIA ENTRE 1.2 y 2.4 m/s
        //CRISTIAN 24/11/2015
        //this.velocidad = Utilidades.getRandom(4)+3;
        this.velocidad = 3;
        this.contadorVelocidad = 0;
        this.tipo = 0;
        this.tiempoInferencia = 0;
        this.salidaElegida = -1;
        this.desiciones = 0;
        this.demoraReaccion=10;
        //this.demoraReaccion=Utilidades.getRandom(200)+120;
        this.asignarComportamiento();
        this.memoria=new Memoria();
        this.salidaSugeridaSensor = new DistanciaSalida(-1,5000.0);
    }

    public Agente(int tipo) {
        this.id = this.listId++;
        this.daño = 0;
        this.tiempoNoActualizacion = 0;
        this.tiempoEvacuacion = 0;
        this.ubicacion = new Point();
        this.ubicacion.x = -1;
        this.ubicacion.y = -1;
        this.destino = new Point();
        this.destino.x = -1;
        this.destino.y = -1;
        this.respuesta = 0;
        //VELOCIDAD ALETORIA ENTRE 1.2 y 2.4 m/s
        //CRISTIAN 24/11/2015
        //this.velocidad = Utilidades.getRandom(4)+3;
        this.velocidad = 3;
        this.contadorVelocidad = 0;
        this.tipo = tipo;
        this.tiempoInferencia = 0;
        this.salidaElegida = -1;
        this.desiciones = 0;
        this.demoraReaccion=10;
        //this.demoraReaccion=Utilidades.getRandom(200)+120;
        this.asignarComportamiento();
        this.memoria=new Memoria();
        this.salidaSugeridaSensor = new DistanciaSalida(-1,5000.0);
    }
    
    
    public Agente(int daño,int tiempoNoActualizacion,int tiempoEvacuacion,Point ubicacion, Point destino,int respuesta,int velocidad,int contadorVelocidad,int tipo,int tiempoInferencia,int salidaElegida,int desiciones, int demoraReaccion) {
        this.id = this.listId++;
        this.daño = daño;
        this.tiempoNoActualizacion = tiempoNoActualizacion;
        this.tiempoEvacuacion = tiempoEvacuacion;
        this.ubicacion = ubicacion;
        this.destino = destino;
        this.respuesta = respuesta;
        this.velocidad = velocidad;
        this.contadorVelocidad = contadorVelocidad;
        this.tipo = tipo;
        this.tiempoInferencia = tiempoInferencia;
        this.salidaElegida = salidaElegida;
        this.desiciones = desiciones;
        this.demoraReaccion=demoraReaccion;
        this.asignarComportamiento();
        this.memoria=new Memoria();
        this.salidaSugeridaSensor =new DistanciaSalida(-1,5000.0);
    }
    
    private void asignarComportamiento(){ //AQUI SE DEBE COLOCAR LA ASIGNACION DEL NUEVO COMPORTAMIENTO
        switch(this.tipo){
            case(0):{
                     this.comportamiento=new ComportamientoSalidaMasCercana();
                     break;
            }
            case(1):{
                    this.comportamiento=new ComportamientoSalidaMejorTiempo();
                    break;
            }
            case(2):{
                    this.comportamiento=new ComportamientoSalePorDondeEntro();
                    break;
            }
            case(3):{
                    this.comportamiento=new ComportamientoPropagacionEstadoEvacuacion();
                    break;
            }
            case(4):{
                    this.comportamiento=new ComportamientoSalidaMasLejana();
                    break;
            }
            case(5):{
                this.comportamiento=new ComportamientoSalidaSugeridaSensor();
                break;
            }
            default:{
                    this.comportamiento=new ComportamientoPropagacionEstadoEvacuacion();
                    break;
            }
        }
    }
    
    public void ejecutarComportamiento(AutomataCelular actual, AutomataCelular adelantado, AutomataCelular ambiente){
        this.comportamiento.ejecutarComportamiento(actual, adelantado, ambiente ,this.ubicacion);
    }

    public int getDaño() {
        return daño;
    }

    public void setDaño(int daño) {
        this.daño = daño;
    }

    public int getTiempoNoActualizacion() {
        return tiempoNoActualizacion;
    }

    public void setTiempoNoActualizacion(int tiempoNoActualizacion) {
        this.tiempoNoActualizacion = tiempoNoActualizacion;
    }

    public int getTiempoEvacuacion() {
        return tiempoEvacuacion;
    }

    public void setTiempoEvacuacion(int tiempoEvacuacion) {
        this.tiempoEvacuacion = tiempoEvacuacion;
    }

    public Point getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Point ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Point getDestino() {
        return destino;
    }

    public void setDestino(Point destino) {
        this.destino = destino;
    }

    public int getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(int respuesta) {
        this.respuesta = respuesta;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    public int getContadorVelocidad() {
        return contadorVelocidad;
    }

    public void setContadorVelocidad(int contadorVelocidad) {
        this.contadorVelocidad = contadorVelocidad;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getSalidaElegida() {
        return salidaElegida;
    }

    public void setSalidaElegida(int salidaElegida) {
        this.salidaElegida = salidaElegida;
    }

    public int getTiempoInferencia() {
        return tiempoInferencia;
    }

    public void setTiempoInferencia(int tiempoInferencia) {
        this.tiempoInferencia = tiempoInferencia;
    }

    public int getDesiciones() {
        return desiciones;
    }

    public void setDesiciones(int desiciones) {
        this.desiciones = desiciones;
    }

    public Comportamiento getComportamiento() {
        return comportamiento;
    }

    public void setComportamiento(Comportamiento comportamiento) {
        this.comportamiento = comportamiento;
    }

    public int getDemoraReaccion() {
        return demoraReaccion;
    }

    public void setDemoraReaccion(int demoraReaccion) {
        this.demoraReaccion = demoraReaccion;
    }

    public Memoria getMemoria() {
        return memoria;
    }

    public void setMemoria(Memoria memoria) {
        this.memoria = memoria;
    }
    
    //CRISTIAN 30/07/2018
    public int getPasosDados(){
        return this.pasosDados;
    }
    //CRISTIAN 30/07/2018
    public void setPasosDados(int pasos){
        this.pasosDados=pasos;
    }

    public DistanciaSalida getSalidaSugeridaSensor() {
        return salidaSugeridaSensor;
    }

    public void setSalidaSugeridaSensor(DistanciaSalida salidaSugeridaSensor) {
        this.salidaSugeridaSensor = salidaSugeridaSensor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
