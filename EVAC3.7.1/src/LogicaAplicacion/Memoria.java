/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicaAplicacion;

import java.awt.Point;
import java.util.LinkedList;

/**
 *
 * @author Cristian
 */
public class Memoria {
    LinkedList sitiosVisitados;
    LinkedList puertasDeseadas;
    
    public void Memoria(){
        sitiosVisitados=new LinkedList();
        puertasDeseadas=new LinkedList();
    }
    
    public void agregarSitioVisitado(Point nodoVisitado){
        sitiosVisitados.add(nodoVisitado);
    }
    
    public void agregarPuertaDeseada(int numeroPuerta){
        puertasDeseadas.add(numeroPuerta);
    }

    public LinkedList getSitiosVisitados() {
        return sitiosVisitados;
    }

    public LinkedList getPuertasDeseadas() {
        return puertasDeseadas;
    }

}
