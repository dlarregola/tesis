
package LogicaAplicacion;

import java.util.Iterator;
import java.util.LinkedList;

public class Celda {
    private int estado; //0-vacia 1-Muro 2-Obstaculo 4-Fuego 5-Salida 6-Agente 
    private int nivelTransito; // desde 1 a 100 indica el porcentaje de disminución de velocidad de transito en el agente debido a terreno dificil de transitar
    private int nivelCombustibilidad;
    private double nivelHumo;
    private double nivelFuego;
    private int contadorTransitoAgentes;         
    private LinkedList peticiones;
    private LinkedList distanciasSalidas;
    private Agente agente;
    private boolean actualizada;
    private int tipoSensor;
    private Sensor sensor; // 100 sin sensor, 101 densidad, 102 fuego

    public Celda(int estado, int nivelTransito, int nivelCombustibilidad, double nivelHumo, double nivelFuego, Agente agente, boolean actualizada) {
        this.estado = estado;
        this.nivelTransito = nivelTransito;
        this.nivelCombustibilidad = nivelCombustibilidad;
        this.nivelHumo = nivelHumo;
        this.nivelFuego = nivelFuego;
        this.peticiones = new LinkedList();
        this.distanciasSalidas = new LinkedList();
        this.agente=agente;
        this.actualizada=actualizada;
        this.tipoSensor = 100;
    }
    
    public Celda(int estado, int nivelTransito, int nivelCombustibilidad, double nivelHumo, double nivelFuego, boolean actualizada,Sensor sensor, int tipoSensor) {
        this.estado = estado;
        this.nivelTransito = nivelTransito;
        this.nivelCombustibilidad = nivelCombustibilidad;
        this.nivelHumo = nivelHumo;
        this.nivelFuego = nivelFuego;
        this.peticiones = new LinkedList();
        this.distanciasSalidas = new LinkedList();
        this.agente=null;
        this.actualizada=actualizada;
        this.sensor = sensor;
        this.tipoSensor = tipoSensor;
    }
    
    public Celda(int estado, int nivelTransito, int nivelCombustibilidad, double nivelHumo, double nivelFuego) {
        this.estado = estado;
        this.nivelTransito = nivelTransito;
        this.nivelCombustibilidad = nivelCombustibilidad;
        this.nivelHumo = nivelHumo;
        this.nivelFuego = nivelFuego;
        this.peticiones = new LinkedList();
        this.distanciasSalidas = new LinkedList();
        this.agente=null;
        this.actualizada=false;
        this.tipoSensor = 100;
    }

    public Celda() {
        this.estado = 0;
        this.nivelTransito = 0;
        this.nivelCombustibilidad = 0;
        this.nivelHumo = -1.0;
        this.nivelFuego = -1.0;
        this.peticiones = new LinkedList();
        this.distanciasSalidas = new LinkedList();
        this.agente=null;
        this.actualizada=false;
        this.tipoSensor = 100;
    }

    public int getEstado() {
        return this.estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getNivelCombustibilidad() {
        return this.nivelCombustibilidad;
    }

    public void setNivelCombustibilidad(int nivelCombustibilidad) {
        this.nivelCombustibilidad = nivelCombustibilidad;
    }

    public double getNivelFuego() {
        return this.nivelFuego;
    }

    public void setNivelFuego(double nivelFuego) {
        this.nivelFuego = nivelFuego;
    }

    public double getNivelHumo() {
        return this.nivelHumo;
    }

    public void setNivelHumo(double nivelHumo) {
        this.nivelHumo = nivelHumo;
    }

    public LinkedList getPeticiones() {
        return this.peticiones;
    }

    public void setPeticiones(LinkedList peticiones) {
        this.peticiones = peticiones;
    }

    public void renovarPeticiones() {
        this.peticiones = null;
        this.peticiones = new LinkedList();
    }

    public LinkedList getDistanciasSalidas() {
        return this.distanciasSalidas;
    }

    public void setDistanciasSalidas(LinkedList distanciasSalidas) {
        this.distanciasSalidas = distanciasSalidas;
    }


    public int getContadorTransitoAgentes() {
        return contadorTransitoAgentes;
    }

    public void setContadorTransitoAgentes(int contadorTransitoAgentes) {
        this.contadorTransitoAgentes = contadorTransitoAgentes;
    }

    public int getNivelTransito() {
        return nivelTransito;
    }

    public void setNivelTransito(int nivelTransito) {
        this.nivelTransito = nivelTransito;
    }

    public Agente getAgente() {
        return this.agente;
    }

    public void setAgente(Agente agente) {
        this.agente = agente;
    }
    
    public boolean hayAgente(){
        if(this.agente==null)
            return false;
        else
            return true;
    }

    public boolean isActualizada() {
        return actualizada;
    }

    public void setActualizada(boolean actualizada) {
        this.actualizada = actualizada;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void deleteSensor(){
        this.tipoSensor = 100;
        this.sensor = null;
    }
    public void setSensor(Sensor sensor) {
        this.tipoSensor = sensor.getTipo();
        this.sensor = sensor;
    }

    public int getTipoSensor() {
        return tipoSensor;
    }

    public void setTipoSensor(int tipoSensor) {
        this.tipoSensor = tipoSensor;
    }
}

