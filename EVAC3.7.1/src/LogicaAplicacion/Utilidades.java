/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicaAplicacion;

import java.awt.Point;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Cristian
 */
public class Utilidades {
    
    static LinkedList visitados = new LinkedList();
    
    
    private static int cuadradosAncho;
    private static int cuadradosAlto;
    private static Random random=new Random();
    
    public static void recalcularDistancias(AutomataCelular ac) {
        cuadradosAncho = (int)((double)Proyecto.getProyecto().getTamañox() / 0.4) + 1;
        cuadradosAlto = (int)((double)Proyecto.getProyecto().getTamañoy() / 0.4) + 1;
        Utilidades.calcularDistanciasFuego(ac);
    }
    
    private static boolean buscarNodo(Point buscado) {
        ListIterator iterator = visitados.listIterator();
        while (iterator.hasNext()) {
            Point p = (Point)iterator.next();
            if (!p.equals(buscado)) continue;
            return true;
        }
        return false;
    }
    
    private static Point[] devolverOrtogonales(Point p) {
        Point[] ortogonales = new Point[4];
        ortogonales[0] = new Point();
        ortogonales[0].x = p.x;
        ortogonales[0].y = p.y - 1;
        ortogonales[1] = new Point();
        ortogonales[1].x = p.x + 1;
        ortogonales[1].y = p.y;
        ortogonales[2] = new Point();
        ortogonales[2].x = p.x;
        ortogonales[2].y = p.y + 1;
        ortogonales[3] = new Point();
        ortogonales[3].x = p.x - 1;
        ortogonales[3].y = p.y;
        return ortogonales;
    }
    
    public static int celdasDisponibles(){
        int x;
        int y;
        int cAncho = (int)((double)Proyecto.getProyecto().getTamañox() / 0.4) + 1;
        int cAlto = (int)((double)Proyecto.getProyecto().getTamañoy() / 0.4) + 1;
        AutomataCelular matriz = Proyecto.getProyecto().getAc();
        int contador=0;
        for (y = 0; y < cAlto; ++y) {
            for (x = 0; x < cAncho; ++x) {
                if (matriz.getCelda(y, x).getEstado() == 0 || matriz.getCelda(y, x).getEstado() == 6)
                    {
                        contador++;
                    }
            }
        }
        return contador;
    }
    
    public static int celdasOcupadas(){
        int x;
        int y;
        int cAncho = (int)((double)Proyecto.getProyecto().getTamañox() / 0.4) + 1;
        int cAlto = (int)((double)Proyecto.getProyecto().getTamañoy() / 0.4) + 1;
        AutomataCelular matriz = Proyecto.getProyecto().getAc();
        int contador=0;
        for (y = 0; y < cAlto; ++y) {
            for (x = 0; x < cAncho; ++x) {
                if (matriz.getCelda(y, x).getEstado() == 6) 
                {
                    contador++;
                }
            }
        }
        return contador;
    }

    /*public static void construirSalidasNuevo(){

    }*/


    public static void construirSalidas(){
        AutomataCelular matriz = Proyecto.getProyecto().getAc();
        Point p = new Point();
        Point anterior = new Point();
        Point actual = new Point();
        Point[] ortogonales = new Point[4];
        boolean condicion = true;
        int cAncho = (int)((double)Proyecto.getProyecto().getTamañox() / 0.4) + 1;
        int cAlto = (int)((double)Proyecto.getProyecto().getTamañoy() / 0.4) + 1;
        visitados.clear();
        Proyecto.getProyecto().getSalidas().clear();
        p.x = Proyecto.getProyecto().getPuntoInicio().x;
        p.y = Proyecto.getProyecto().getPuntoInicio().y;
        actual.x = p.x;
        actual.y = p.y;
        anterior.x = -10;
        anterior.y = -10;
        LinkedList nodos = new LinkedList(Proyecto.getProyecto().getNodosSalidas());
        ortogonales = devolverOrtogonales(p);
        if (matriz.getCelda(p.y, p.x).getEstado() == 1) {
            while (condicion) {
                Salida salida;
                if (!(ortogonales[1].x <= -1 || ortogonales[1].x >= cAncho || ortogonales[1].y <= -1 || ortogonales[1].y >= cAlto || buscarNodo(ortogonales[1]) || matriz.getCelda(ortogonales[1].y, ortogonales[1].x).getEstado() != 1 && matriz.getCelda(ortogonales[1].y, ortogonales[1].x).getEstado() != 5)) {
                    switch (matriz.getCelda(ortogonales[1].y, ortogonales[1].x).getEstado()) {
                        case 1: {
                            break;
                        }
                        case 5: {
                            if (Proyecto.getProyecto().getSalidas().size() > 0) {
                                if (matriz.getCelda(actual.y, actual.x).getEstado() == 5) {
                                    ((Salida)Proyecto.getProyecto().getSalidas().getLast()).agregarNodo(new Point(ortogonales[1].x, ortogonales[1].y));
                                    break;
                                }
                                salida = new Salida();
                                salida.agregarNodo(new Point(ortogonales[1].x, ortogonales[1].y));
                                Proyecto.getProyecto().getSalidas().add(salida);
                                ((Salida)Proyecto.getProyecto().getSalidas().getLast()).setNumeroSalida(Proyecto.getProyecto().getSalidas().size());
                                break;
                            }
                            salida = new Salida();
                            salida.agregarNodo(new Point(ortogonales[1].x, ortogonales[1].y));
                            Proyecto.getProyecto().getSalidas().add(salida);
                            ((Salida)Proyecto.getProyecto().getSalidas().getLast()).setNumeroSalida(Proyecto.getProyecto().getSalidas().size());
                        }
                    }
                    if(nodos.indexOf(actual) >= 0){
                        nodos.remove(actual);
                    }
                    anterior.x = actual.x;
                    anterior.y = actual.y;
                    actual.x = ortogonales[1].x;
                    actual.y = ortogonales[1].y;
                } else if (!(ortogonales[2].x <= -1 || ortogonales[2].x >= cAncho || ortogonales[2].y <= -1 || ortogonales[2].y >= cAlto || buscarNodo(ortogonales[2]) || matriz.getCelda(ortogonales[2].y, ortogonales[2].x).getEstado() != 1 && matriz.getCelda(ortogonales[2].y, ortogonales[2].x).getEstado() != 5)) {
                    switch (matriz.getCelda(ortogonales[2].y, ortogonales[2].x).getEstado()) {
                        case 1: {
                            break;
                        }
                        case 5: {
                            if (Proyecto.getProyecto().getSalidas().size() > 0) {
                                if (matriz.getCelda(actual.y, actual.x).getEstado() == 5) {
                                    ((Salida)Proyecto.getProyecto().getSalidas().getLast()).agregarNodo(new Point(ortogonales[2].x, ortogonales[2].y));
                                    break;
                                }
                                salida = new Salida();
                                salida.agregarNodo(new Point(ortogonales[2].x, ortogonales[2].y));
                                Proyecto.getProyecto().getSalidas().add(salida);
                                ((Salida)Proyecto.getProyecto().getSalidas().getLast()).setNumeroSalida(Proyecto.getProyecto().getSalidas().size());
                                break;
                            }
                            salida = new Salida();
                            salida.agregarNodo(new Point(ortogonales[2].x, ortogonales[2].y));
                            Proyecto.getProyecto().getSalidas().add(salida);
                            ((Salida)Proyecto.getProyecto().getSalidas().getLast()).setNumeroSalida(Proyecto.getProyecto().getSalidas().size());
                        }
                    }
                    if(nodos.indexOf(actual) >= 0){
                        nodos.remove(actual);
                    }
                    anterior.x = actual.x;
                    anterior.y = actual.y;
                    actual.x = ortogonales[2].x;
                    actual.y = ortogonales[2].y;
                } else if (!(ortogonales[3].x <= -1 || ortogonales[3].x >= cAncho || ortogonales[3].y <= -1 || ortogonales[3].y >= cAlto || buscarNodo(ortogonales[3]) || matriz.getCelda(ortogonales[3].y, ortogonales[3].x).getEstado() != 1 && matriz.getCelda(ortogonales[3].y, ortogonales[3].x).getEstado() != 5)) {
                    switch (matriz.getCelda(ortogonales[3].y, ortogonales[3].x).getEstado()) {
                        case 1: {
                            break;
                        }
                        case 5: {
                            if (Proyecto.getProyecto().getSalidas().size() > 0) {
                                if (matriz.getCelda(actual.y, actual.x).getEstado() == 5) {
                                    ((Salida)Proyecto.getProyecto().getSalidas().getLast()).agregarNodo(new Point(ortogonales[3].x, ortogonales[3].y));
                                    break;
                                }
                                salida = new Salida();
                                salida.agregarNodo(new Point(ortogonales[3].x, ortogonales[3].y));
                                Proyecto.getProyecto().getSalidas().add(salida);
                                ((Salida)Proyecto.getProyecto().getSalidas().getLast()).setNumeroSalida(Proyecto.getProyecto().getSalidas().size());
                                break;
                            }
                            salida = new Salida();
                            salida.agregarNodo(new Point(ortogonales[3].x, ortogonales[3].y));
                            Proyecto.getProyecto().getSalidas().add(salida);
                            ((Salida)Proyecto.getProyecto().getSalidas().getLast()).setNumeroSalida(Proyecto.getProyecto().getSalidas().size());
                        }
                    }
                    if(nodos.indexOf(actual) >= 0){
                        nodos.remove(actual);
                    }
                    anterior.x = actual.x;
                    anterior.y = actual.y;
                    actual.x = ortogonales[3].x;
                    actual.y = ortogonales[3].y;
                } else if (!(ortogonales[0].x <= -1 || ortogonales[0].x >= cAncho || ortogonales[0].y <= -1 || ortogonales[0].y >= cAlto || buscarNodo(ortogonales[0]) || matriz.getCelda(ortogonales[0].y, ortogonales[0].x).getEstado() != 1 && matriz.getCelda(ortogonales[0].y, ortogonales[0].x).getEstado() != 5)) {
                    switch (matriz.getCelda(ortogonales[0].y, ortogonales[0].x).getEstado()) {
                        case 1: {
                            break;
                        }
                        case 5: {
                            if (Proyecto.getProyecto().getSalidas().size() > 0) {
                                if (matriz.getCelda(actual.y, actual.x).getEstado() == 5) {
                                    ((Salida)Proyecto.getProyecto().getSalidas().getLast()).agregarNodo(new Point(ortogonales[0].x, ortogonales[0].y));
                                    break;
                                }
                                salida = new Salida();
                                salida.agregarNodo(new Point(ortogonales[0].x, ortogonales[0].y));
                                Proyecto.getProyecto().getSalidas().add(salida);
                                ((Salida)Proyecto.getProyecto().getSalidas().getLast()).setNumeroSalida(Proyecto.getProyecto().getSalidas().size());
                                break;
                            }
                            salida = new Salida();
                            salida.agregarNodo(new Point(ortogonales[0].x, ortogonales[0].y));
                            Proyecto.getProyecto().getSalidas().add(salida);
                            ((Salida)Proyecto.getProyecto().getSalidas().getLast()).setNumeroSalida(Proyecto.getProyecto().getSalidas().size());
                        }
                    }
                    if(nodos.indexOf(actual) >= 0){
                        nodos.remove(actual);
                    }
                    anterior.x = actual.x;
                    anterior.y = actual.y;
                    actual.x = ortogonales[0].x;
                    actual.y = ortogonales[0].y;
                }
               /* if (p.x == actual.x && p.y == actual.y) {
                    condicion = false;
                }*/

                if(nodos.size() == 0){
                    condicion = false;
                }
                visitados.add(new Point(actual.x, actual.y));
                ortogonales = devolverOrtogonales(actual);
            }
            //calcularDistancias(); //SOLO DEBE CONSTUIR LAS SALIDAS, LAS DISTANCIAS SON CALCULADAS LUEGO (28-09-2015)
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "Punto de inicio no valido", "ERROR DE VALIDACION", 1);
        }
    }
    
    public static void calcularDistanciasFuego(AutomataCelular ac) {
        ListIterator iteradorVecinos;
        LinkedList vecinos;
        int x;
        int y;
        int i;
        int cAncho = (int)((double)Proyecto.getProyecto().getTamañox() / 0.4) + 1;
        int cAlto = (int)((double)Proyecto.getProyecto().getTamañoy() / 0.4) + 1;
        Point actual = new Point();
        Point vecino = new Point();
        DistanciaSalida distanciaSalida = new DistanciaSalida();
        AutomataCelular matriz = ac;
        for (y = 0; y < cAlto; ++y) {
            for (x = 0; x < cAncho; ++x) {
                if (matriz.getCelda(y, x).getEstado() == 5 || matriz.getCelda(y, x).getEstado() == 1 || matriz.getCelda(y, x).getEstado() == 2) continue;
                matriz.getCelda(y, x).getDistanciasSalidas().clear();
            }
        }
        for (y = 0; y < cAlto; ++y) {
            for (x = 0; x < cAncho; ++x) {
                if (matriz.getCelda(y, x).getEstado() == 4 || matriz.getCelda(y, x).getEstado() == 5 || matriz.getCelda(y, x).getEstado() == 1 || matriz.getCelda(y, x).getEstado() == 2) continue;
                for (i = Proyecto.getProyecto().getSalidas().size(); i > 0; --i) {
                    matriz.getCelda(y, x).getDistanciasSalidas().add(new DistanciaSalida(i, -1.0));
                }
            }
        }
        LinkedList salidas = Proyecto.getProyecto().getSalidas();
        ListIterator iteradorSalidas = salidas.listIterator();
        while (iteradorSalidas.hasNext()) {
            Salida salida = (Salida)iteradorSalidas.next();
            ListIterator iteradorCeldas = salida.getNodos().listIterator();
            while (iteradorCeldas.hasNext()) {
                actual = (Point)iteradorCeldas.next();
                vecinos = Utilidades.obtenerMoore((Point)actual, (AutomataCelular)ac);
                iteradorVecinos = vecinos.listIterator();
                while (iteradorVecinos.hasNext()) {
                    vecino = (Point)iteradorVecinos.next();
                    if (Proyecto.getProyecto().getAc().getCelda(vecino.y, vecino.x).getEstado() == 5) continue;
                    ListIterator iteradorSalidasCelda = matriz.getCelda(vecino.y, vecino.x).getDistanciasSalidas().listIterator();
                    while (iteradorSalidasCelda.hasNext()) {
                        distanciaSalida = (DistanciaSalida)iteradorSalidasCelda.next();
                        if (distanciaSalida.getSalida() != salida.getNumeroSalida()) continue;
                        distanciaSalida.setDistancia(1.0);
                    }
                }
            }
        }
        actual = new Point();
        int n = cAncho >= cAlto ? cAncho * 4 : cAlto * 4;
        int valorComparacion;
        while (n >= 0) {
            --n;
            for (y = 0; y < cAlto; ++y) {
                for (x = 0; x < cAncho; ++x) {
                    if (matriz.getCelda(y, x).getEstado() == 4 || matriz.getCelda(y, x).getEstado() == 5 || matriz.getCelda(y, x).getEstado() == 1 || matriz.getCelda(y, x).getEstado() == 2) continue;
                    actual.x = x;
                    actual.y = y;
                    vecinos = Utilidades.obtenerMoore((Point)actual, (AutomataCelular)ac);
                    for (i = Proyecto.getProyecto().getSalidas().size(); i > 0; --i) {
                        iteradorVecinos = vecinos.listIterator();
                        Double distanciaVecino = -10.0;
                        Double distanciaCeldaActual = Utilidades.distanciaaSalida((Celda)matriz.getCelda(y, x), (int)i);
                        while (iteradorVecinos.hasNext()) {
                            vecino = (Point)iteradorVecinos.next();
                            Celda e = matriz.getCelda(vecino.y, vecino.x);
                            distanciaVecino = Utilidades.distanciaaSalida((Celda)e, (int)i);
                            distanciaCeldaActual = Utilidades.distanciaaSalida((Celda)matriz.getCelda(y, x), (int)i);
                            valorComparacion=distanciaVecino.compareTo(-1.0);
                            if (valorComparacion == 0) continue;
                            
                            valorComparacion=distanciaCeldaActual.compareTo(-1.0);
                            if (valorComparacion == 0) {
                                if(esVecinoDiagonal(new Point(x,y),vecino)){
                                    Utilidades.setearDistancia((Celda)matriz.getCelda(y, x), (int)i, distanciaVecino + 1.5);
                                }
                                else{
                                    Utilidades.setearDistancia((Celda)matriz.getCelda(y, x), (int)i, distanciaVecino + 1.0);
                                }
                                //Utilidades.setearDistancia((Celda)matriz.getCelda(y, x), (int)i, distanciaVecino + 1.0);
                                continue;
                            }
                            
                            Double dv = distanciaVecino + 1.0;
                            valorComparacion=dv.compareTo(distanciaCeldaActual);
                            if (valorComparacion >= 0) continue;
                                if(esVecinoDiagonal(new Point(x,y),vecino)){
                                    Utilidades.setearDistancia((Celda)matriz.getCelda(y, x), (int)i, distanciaVecino + 1.5);
                                }
                                else{
                                    Utilidades.setearDistancia((Celda)matriz.getCelda(y, x), (int)i, distanciaVecino + 1.0);
                                }
                            //Utilidades.setearDistancia((Celda)matriz.getCelda(y, x), (int)i, distanciaVecino + 1.0);
                        }
                    }
                }
            }
        }  
    }

    private static Double distanciaaSalida(Celda e, int salida) {
        ListIterator iteradorSalidas = e.getDistanciasSalidas().listIterator();
        while (iteradorSalidas.hasNext()) {
            DistanciaSalida distancia = (DistanciaSalida)iteradorSalidas.next();
            if (distancia.getSalida() != salida) continue;
            return distancia.getDistancia();
        }
        return -1.0;
    }

    private static void setearDistancia(Celda e, int salida, Double distanciaSalida) {
        ListIterator iteradorSalidas = e.getDistanciasSalidas().listIterator();
        while (iteradorSalidas.hasNext()) {
            DistanciaSalida distancia = (DistanciaSalida)iteradorSalidas.next();
            if (distancia.getSalida() != salida) continue;
            distancia.setDistancia(distanciaSalida);
        }
    }

    public static LinkedList obtenerMoore(Point origen, AutomataCelular ac) {
        AutomataCelular matriz = ac;
        LinkedList<Point> vecinos = new LinkedList<Point>();
        Point p1 = new Point(origen.x - 1, origen.y - 1);
        Point p2 = new Point(origen.x, origen.y - 1);
        Point p3 = new Point(origen.x + 1, origen.y - 1);
        Point p4 = new Point(origen.x - 1, origen.y);
        Point p5 = new Point(origen.x + 1, origen.y);
        Point p6 = new Point(origen.x - 1, origen.y + 1);
        Point p7 = new Point(origen.x, origen.y + 1);
        Point p8 = new Point(origen.x + 1, origen.y + 1);
        if (Utilidades.chequearPunto((Point)p1) && matriz.getCelda(p1.y, p1.x).getEstado() != 1 && matriz.getCelda(p1.y, p1.x).getEstado() != 2 && matriz.getCelda(p1.y, p1.x).getEstado() != 4) {
            vecinos.add(p1);
        }
        if (Utilidades.chequearPunto((Point)p2) && matriz.getCelda(p2.y, p2.x).getEstado() != 1 && matriz.getCelda(p2.y, p2.x).getEstado() != 2 && matriz.getCelda(p2.y, p2.x).getEstado() != 4) {
            vecinos.add(p2);
        }
        if (Utilidades.chequearPunto((Point)p3) && matriz.getCelda(p3.y, p3.x).getEstado() != 1 && matriz.getCelda(p3.y, p3.x).getEstado() != 2 && matriz.getCelda(p3.y, p3.x).getEstado() != 4) {
            vecinos.add(p3);
        }
        if (Utilidades.chequearPunto((Point)p4) && matriz.getCelda(p4.y, p4.x).getEstado() != 1 && matriz.getCelda(p4.y, p4.x).getEstado() != 2 && matriz.getCelda(p4.y, p4.x).getEstado() != 4) {
            vecinos.add(p4);
        }
        if (Utilidades.chequearPunto((Point)p5) && matriz.getCelda(p5.y, p5.x).getEstado() != 1 && matriz.getCelda(p5.y, p5.x).getEstado() != 2 && matriz.getCelda(p5.y, p5.x).getEstado() != 4) {
            vecinos.add(p5);
        }
        if (Utilidades.chequearPunto((Point)p6) && matriz.getCelda(p6.y, p6.x).getEstado() != 1 && matriz.getCelda(p6.y, p6.x).getEstado() != 2 && matriz.getCelda(p6.y, p6.x).getEstado() != 4) {
            vecinos.add(p6);
        }
        if (Utilidades.chequearPunto((Point)p7) && matriz.getCelda(p7.y, p7.x).getEstado() != 1 && matriz.getCelda(p7.y, p7.x).getEstado() != 2 && matriz.getCelda(p7.y, p7.x).getEstado() != 4) {
            vecinos.add(p7);
        }
        if (Utilidades.chequearPunto((Point)p8) && matriz.getCelda(p8.y, p8.x).getEstado() != 1 && matriz.getCelda(p8.y, p8.x).getEstado() != 2 && matriz.getCelda(p8.y, p8.x).getEstado() != 4) {
            vecinos.add(p8);
        }
        return vecinos;
    }


    public static int getRandom(int limiteSuperior) {
        return random.nextInt(limiteSuperior);
    }

    public static Point getPunto(int nodo) {
        int cuadradosAncho = (int)((double)Proyecto.getProyecto().getTamañox() / 0.4) + 1;
        int cuadradosAlto = (int)((double)Proyecto.getProyecto().getTamañoy() / 0.4) + 1;
        Point punto = new Point();
        punto.setLocation(nodo % cuadradosAncho, nodo / cuadradosAncho);
        return punto;
    }

    public static boolean chequearPunto(Point nodo) {
        int cuadradosAncho = (int)((double)Proyecto.getProyecto().getTamañox() / 0.4) + 1;
        int cuadradosAlto = (int)((double)Proyecto.getProyecto().getTamañoy() / 0.4) + 1;
        if (nodo.x > -1 && nodo.x < cuadradosAncho && nodo.y > -1 && nodo.y < cuadradosAlto) {
            return true;
        }
        return false;
    }

         
    public static ArrayList chequearMoore(Point origen, AutomataCelular ac1) {
        ArrayList<Integer> vecinos = new ArrayList<Integer>();
        Point p1 = new Point(origen.x - 1, origen.y - 1);
        Point p2 = new Point(origen.x, origen.y - 1);
        Point p3 = new Point(origen.x + 1, origen.y - 1);
        Point p4 = new Point(origen.x - 1, origen.y);
        Point p5 = new Point(origen.x + 1, origen.y);
        Point p6 = new Point(origen.x - 1, origen.y + 1);
        Point p7 = new Point(origen.x, origen.y + 1);
        Point p8 = new Point(origen.x + 1, origen.y + 1);
        Point p9 = new Point(origen.x, origen.y);
        int cuadradosAncho = (int)((double)Proyecto.getProyecto().getTamañox() / 0.4) + 1;
        int cuadradosAlto = (int)((double)Proyecto.getProyecto().getTamañoy() / 0.4) + 1;
        if (Utilidades.chequearPunto((Point)p1) && ac1.getCelda(p1.y, p1.x).getEstado() != 1 && ac1.getCelda(p1.y, p1.x).getEstado() != 2 && ac1.getCelda(p1.y, p1.x).getEstado() != 4 && ac1.getCelda(p1.y, p1.x).getNivelFuego()<=0) {
            vecinos.add(new Integer(p1.x + p1.y * cuadradosAncho));
        }
        if (Utilidades.chequearPunto((Point)p2) && ac1.getCelda(p2.y, p2.x).getEstado() != 1 && ac1.getCelda(p2.y, p2.x).getEstado() != 2 && ac1.getCelda(p2.y, p2.x).getEstado() != 4 && ac1.getCelda(p2.y, p2.x).getNivelFuego()<=0) {
            vecinos.add(new Integer(p2.x + p2.y * cuadradosAncho));
        }
        if (Utilidades.chequearPunto((Point)p3) && ac1.getCelda(p3.y, p3.x).getEstado() != 1 && ac1.getCelda(p3.y, p3.x).getEstado() != 2 && ac1.getCelda(p3.y, p3.x).getEstado() != 4 && ac1.getCelda(p3.y, p3.x).getNivelFuego()<=0) {
            vecinos.add(new Integer(p3.x + p3.y * cuadradosAncho));
        }
        if (Utilidades.chequearPunto((Point)p4) && ac1.getCelda(p4.y, p4.x).getEstado() != 1 && ac1.getCelda(p4.y, p4.x).getEstado() != 2 && ac1.getCelda(p4.y, p4.x).getEstado() != 4 && ac1.getCelda(p4.y, p4.x).getNivelFuego()<=0) {
            vecinos.add(new Integer(p4.x + p4.y * cuadradosAncho));
        }
        if (Utilidades.chequearPunto((Point)p5) && ac1.getCelda(p5.y, p5.x).getEstado() != 1 && ac1.getCelda(p5.y, p5.x).getEstado() != 2 && ac1.getCelda(p5.y, p5.x).getEstado() != 4 && ac1.getCelda(p5.y, p5.x).getNivelFuego()<=0) {
            vecinos.add(new Integer(p5.x + p5.y * cuadradosAncho));
        }
        if (Utilidades.chequearPunto((Point)p6) && ac1.getCelda(p6.y, p6.x).getEstado() != 1 && ac1.getCelda(p6.y, p6.x).getEstado() != 2 && ac1.getCelda(p6.y, p6.x).getEstado() != 4 && ac1.getCelda(p6.y, p6.x).getNivelFuego()<=0) {
            vecinos.add(new Integer(p6.x + p6.y * cuadradosAncho));
        }
        if (Utilidades.chequearPunto((Point)p7) && ac1.getCelda(p7.y, p7.x).getEstado() != 1 && ac1.getCelda(p7.y, p7.x).getEstado() != 2 && ac1.getCelda(p7.y, p7.x).getEstado() != 4 && ac1.getCelda(p7.y, p7.x).getNivelFuego()<=0) {
            vecinos.add(new Integer(p7.x + p7.y * cuadradosAncho));
        }
        if (Utilidades.chequearPunto((Point)p8) && ac1.getCelda(p8.y, p8.x).getEstado() != 1 && ac1.getCelda(p8.y, p8.x).getEstado() != 2 && ac1.getCelda(p8.y, p8.x).getEstado() != 4 && ac1.getCelda(p8.y, p8.x).getNivelFuego()<=0) {
            vecinos.add(new Integer(p8.x + p8.y * cuadradosAncho));
        }
        return vecinos;
    }
    
    public static AutomataCelular copiarAutomata(AutomataCelular origen, int alto, int ancho){
        AutomataCelular ac = new AutomataCelular();
        ac.setCeldas(new Celda[alto][ancho]);
        ac.setAlto(origen.getAlto());
        ac.setAncho(origen.getAncho());
        for (int i = 0; i < alto; ++i) {
            for (int j = 0; j < ancho; ++j) {
                Celda celda=new Celda(origen.getCelda(i,j).getEstado(),origen.getCelda(i,j).getNivelTransito(),origen.getCelda(i,j).getNivelCombustibilidad(),origen.getCelda(i,j).getNivelHumo(),origen.getCelda(i,j).getNivelFuego(),origen.getCelda(i,j).isActualizada(),origen.getCelda(i,j).getSensor(),origen.getCelda(i,j).getTipoSensor());
                celda.setDistanciasSalidas(copiarDistancias(origen.getCelda(i,j).getDistanciasSalidas()));
                if(origen.getCelda(i,j).hayAgente()){
                    celda.setAgente(copiarAgente(origen.getCelda(i,j).getAgente()));
                }
                ac.setCelda(i, j, celda);
            }
        }
        return(ac);
    }
    
    public static Celda copiarCeldaSinAgente(Celda original){
        Celda celda=new Celda(original.getEstado(),original.getNivelTransito(),original.getNivelCombustibilidad(),original.getNivelHumo(),original.getNivelFuego(),original.isActualizada(),original.getSensor(),original.getTipoSensor());
        celda.setDistanciasSalidas(copiarDistancias(original.getDistanciasSalidas()));
        celda.setAgente(null);
        return celda;
    }
    
    public static LinkedList copiarDistancias(LinkedList original){
        LinkedList distancias=new LinkedList();
        ListIterator iterador = original.listIterator();
        while(iterador.hasNext()){
            DistanciaSalida salidaOriginal=(DistanciaSalida)iterador.next();
            DistanciaSalida salidaCopia=new DistanciaSalida(salidaOriginal.getSalida(),salidaOriginal.getDistancia());
            distancias.add(salidaCopia);
        }
        return(distancias);
    }
    
    public static Agente copiarAgente(Agente original){        
        Agente copia;
        Point ubicacion=new Point(),destino=new Point();
        ubicacion.x=original.getUbicacion().x;
        ubicacion.y=original.getUbicacion().y;
        destino.x=original.getDestino().x;
        destino.y=original.getDestino().y;
        copia=new Agente(original.getDaño(),original.getTiempoNoActualizacion(),original.getTiempoEvacuacion(),ubicacion,destino,original.getRespuesta(),original.getVelocidad(),original.getContadorVelocidad(),original.getTipo(),original.getTiempoInferencia(),original.getSalidaElegida(),original.getDesiciones(),original.getDemoraReaccion());
        return(copia);
    }
    
    public static Double obtenerDistanciaSalida(Celda c, int salida){
        ListIterator iterador = c.getDistanciasSalidas().listIterator();
        while (iterador.hasNext()){
            DistanciaSalida s=(DistanciaSalida)iterador.next();
            if(s.getSalida()==salida){
                return (s.getDistancia());
            }
        }
        return (-1.0);
    }
    
    private static boolean esVecinoDiagonal(Point celda, Point vecino){
        if(celda.x != vecino.x && celda.y != vecino.y){
            return(true);
        }
        return false;
    }
    
    private static void imprimirDistancias(AutomataCelular ac){
        int cAncho = (int)((double)Proyecto.getProyecto().getTamañox() / 0.4) + 1;
        int cAlto = (int)((double)Proyecto.getProyecto().getTamañoy() / 0.4) + 1;
        for(int i=0;i<cAlto;i++){
            for(int j=0;j<cAncho;j++){
                if(ac.getCelda(i,j).getDistanciasSalidas().size()==0){
                    System.out.print("[*]");
                }
                else{
                    System.out.print("[" + ((DistanciaSalida)ac.getCelda(i,j).getDistanciasSalidas().getFirst()).getDistancia() + "]");
                }
                
            }
            System.out.println();
        }
    }
    
    
    public static LinkedList obtenerVecindarioCompleto(Point origen, AutomataCelular ac1) {
        LinkedList vecinos = new LinkedList();
        vecinos.add( new Point(origen.x - 1, origen.y - 1) );
        vecinos.add(new Point(origen.x, origen.y - 1));
        vecinos.add(new Point(origen.x + 1, origen.y - 1));
        vecinos.add(new Point(origen.x - 1, origen.y));
        vecinos.add(new Point(origen.x + 1, origen.y));
        vecinos.add(new Point(origen.x - 1, origen.y + 1));
        vecinos.add(new Point(origen.x, origen.y + 1));
        vecinos.add(new Point(origen.x + 1, origen.y + 1));
        vecinos.add(new Point(origen.x, origen.y));
        
        return vecinos;
    }
    
    public static LinkedList vecindarioRadio(int y,int x, int ancho, int alto, int radio, AutomataCelular ac1){
        LinkedList vecinos = new LinkedList() ;
        for(int i=(y-radio); i<=(y+radio) ;i++){
            for(int j=(x-radio); j<=(x+radio); j++){
              
                if(i>=1 && i<alto && j>=1 && j<ancho)
                {
                    if(ac1.getCelda(i,j).getEstado()!=1 && ac1.getCelda(i,j).getEstado()!=2 && ac1.getCelda(i,j).getEstado()!=5 )
                    {
                     vecinos.add(new Point(j,i));
                    }
           
                }   
            }
        }
        return vecinos;
    }
    
   public static boolean estaEncerrado(Point nodo, AutomataCelular ac){
       LinkedList distanciaSalidas=ac.getCelda(nodo.y,nodo.x).getDistanciasSalidas();
       ListIterator iterador = distanciaSalidas.listIterator();
       DistanciaSalida ds;
       int comparacion=5;

       while(iterador.hasNext()){
           ds=(DistanciaSalida)iterador.next();
           comparacion=ds.getDistancia().compareTo(-1.0);
           if(comparacion!=0){
               return false;
           }
       }
       return true;
   }




}
