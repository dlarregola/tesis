/*
 *
 */
package LogicaAplicacion;

import java.awt.Point;
import java.util.ArrayList;

public abstract class Comportamiento {
    private ArrayList vecinos = new ArrayList();
    private ArrayList vecinosVaciosMejorDistancia = new ArrayList();
    private ArrayList vecinosOcupadosMejorDistancia = new ArrayList();
    private ArrayList vecinosSalida = new ArrayList();
    private ArrayList vecinosIgualDistancia = new ArrayList();
    private ArrayList vecinosVaciosIgualDistancia = new ArrayList();
    private ArrayList vecinosOcupadosIgualDistancia = new ArrayList();
    private ArrayList vecinosVaciosPeorDistancia = new ArrayList();
    private ArrayList vecinosOcupadosPeorDistancia= new ArrayList();

    public ArrayList getVecinosVaciosPeorDistancia() {
        return this.vecinosVaciosPeorDistancia;
    }

    public void setVecinosVaciosPeorDistancia(ArrayList vecinosVaciosPeorDistancia) {
        this.vecinosVaciosPeorDistancia = vecinosVaciosPeorDistancia;
    }

    public ArrayList getVecinosOcupadosPeorDistancia() {
        return this.vecinosOcupadosPeorDistancia;
    }

    public void setVecinosOcupadosPeorDistancia(ArrayList vecinosOcupadosPeorDistancia) {
        this.vecinosOcupadosPeorDistancia = vecinosOcupadosPeorDistancia;
    }

    public ArrayList getVecinos() {
        return this.vecinos;
    }

    public void setVecinos(ArrayList vecinos) {
        this.vecinos = vecinos;
    }

    public ArrayList getVecinosIgualDistancia() {
        return this.vecinosIgualDistancia;
    }

    public void setVecinosIgualDistancia(ArrayList vecinosIgualDistancia) {
        this.vecinosIgualDistancia = vecinosIgualDistancia;
    }

    public ArrayList getVecinosOcupadosMejorDistancia() {
        return this.vecinosOcupadosMejorDistancia;
    }

    public void setVecinosOcupadosMejorDistancia(ArrayList vecinosOcupados) {
        this.vecinosOcupadosMejorDistancia = vecinosOcupados;
    }

    public ArrayList getVecinosOcupadosIgualDistancia() {
        return this.vecinosOcupadosIgualDistancia;
    }

    public void setVecinosOcupadosIgualDistancia(ArrayList vecinosOcupadosIgualDistancia) {
        this.vecinosOcupadosIgualDistancia = vecinosOcupadosIgualDistancia;
    }

    public ArrayList getVecinosSalida() {
        return this.vecinosSalida;
    }

    public void setVecinosSalida(ArrayList vecinosSalida) {
        this.vecinosSalida = vecinosSalida;
    }

    public ArrayList getVecinosVaciosMejorDistancia() {
        return this.vecinosVaciosMejorDistancia;
    }

    public void setVecinosVacios(ArrayList vecinosVacios) {
        this.vecinosVaciosMejorDistancia = vecinosVacios;
    }

    public ArrayList getVecinosVaciosIgualDistancia() {
        return this.vecinosVaciosIgualDistancia;
    }

    public void setVecinosVaciosIgualDistancia(ArrayList vecinosVaciosIgualDistancia) {
        this.vecinosVaciosIgualDistancia = vecinosVaciosIgualDistancia;
    }

    public void borrarVecinos() {
        this.vecinos.clear();
        this.vecinosIgualDistancia.clear();
        this.vecinosOcupadosMejorDistancia.clear();
        this.vecinosOcupadosIgualDistancia.clear();
        this.vecinosSalida.clear();
        this.vecinosVaciosMejorDistancia.clear();
        this.vecinosVaciosIgualDistancia.clear();
        this.vecinosVaciosPeorDistancia.clear();
        this.vecinosOcupadosPeorDistancia.clear();
    }
    

    public abstract boolean ejecutarComportamiento(AutomataCelular tiempoActual, AutomataCelular tiempoSiguiente,AutomataCelular tiempoSiguienteAmbiente, Point posicionActual);
}

