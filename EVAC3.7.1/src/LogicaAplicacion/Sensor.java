package LogicaAplicacion;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class Sensor {

    private int potencia;
    private Point ubicacion;
    private int tipo; // 100 sin sensor, 101 densidad, 102 fuego
    private int cantidadAgentes;
    private int salidaMasCercana;
    private LinkedList listaAgentes;

    public Sensor(int potencia, int fila, int columna ,int tipo) {
        this.potencia = potencia;
        this.ubicacion = new Point();
        this.ubicacion.x = columna;
        this.ubicacion.y = fila;
        this.tipo = tipo;
    }

    public Sensor(int potencia) {
        this.potencia = potencia;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getPotencia() {
        return potencia;
    }

    public void setPotencia(int potencia) {
        this.potencia = potencia;
    }

    public Point getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Point ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void printSensor(){
        JOptionPane.showMessageDialog(null, "Tipo "+this.tipo+" Potencia "+this.potencia+" x"+this.ubicacion.getX()+ "y "+this.ubicacion.getY());

    }

    public int getCantidadAgentes() {
        return cantidadAgentes;
    }

    public void setCantidadAgentes(int cantidadAgentes) {
        this.cantidadAgentes = cantidadAgentes;
    }

    public int getSalidaMasCercana() {
        return salidaMasCercana;
    }

    public void setSalidaMasCercana(int salidaMasCercana) {
        this.salidaMasCercana = salidaMasCercana;
    }

    public LinkedList getListaAgentes() {
        return listaAgentes;
    }

    public void setListaAgentes(LinkedList listaAgentes) {
        this.cantidadAgentes = listaAgentes.size();
        this.listaAgentes = listaAgentes;
    }
}
