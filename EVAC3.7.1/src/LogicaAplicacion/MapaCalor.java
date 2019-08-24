/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicaAplicacion;

import InterfazUsusario.VentanaConfiguracionAmbiente;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

/**
 *
 * @author Cristian
 */
public class MapaCalor extends Canvas{
    private int ratonX;
    private int ratonY;
    private int ancho = 7 + 7 * ((int)((double)Proyecto.getProyecto().getTamañox() / 0.4) + 1);
    private int alto = 7 + 7 * ((int)((double)Proyecto.getProyecto().getTamañoy() / 0.4) + 1);
    private int cuadradrosAncho;
    private int cuadradosAlto;
    private int i;
    private int j;
    private int x;
    private int y;
    private AutomataCelular matriz;
    private LinkedList visitados = new LinkedList();
    
     public MapaCalor() {
        this.setSize(this.ancho + 7, this.alto + 7);
        this.setBackground(Color.WHITE);
        this.cuadradrosAncho = (int)((double)Proyecto.getProyecto().getTamañox() / 0.4) + 1;
        this.cuadradosAlto = (int)((double)Proyecto.getProyecto().getTamañoy() / 0.4) + 1;
        this.matriz = Proyecto.getProyecto().getAc();
    }
    
  
  /*    
    public LinkedList vecindad(int y,int x, int ancho, int alto, int diametro){
        LinkedList lista = new LinkedList() ;
        for(int i=(y-diametro); i<=(y+diametro) ;i++){
            for(int j=(x-diametro); j<=(x+diametro); j++){
              
                if(i>=1 && i<alto && j>=1 && j<ancho)
                {
                    if(this.matriz.getCelda(i, j).getEstado()!=1 && this.matriz.getCelda(i, j).getEstado()!=2 )
                    {
                     lista.addLast(i);
                     lista.addLast(j);
                    }
           
                }   
            }
            
        }
        return lista;
        
    }
    */        
    
    public double porcentajeVecinos(int y, int x, int ancho, int alto, int diametro, AutomataCelular ac1) {
        int cantidadVecinos=0,cantidadAgentes=0;
        double porcentaje;
        Point punto;
        int equis,ye; 
        
        LinkedList vecinos = new LinkedList();
        vecinos=Utilidades.vecindarioRadio(y, x, ancho, alto, diametro, ac1);  
          
        ListIterator i = vecinos.listIterator();
         
        while(i.hasNext())
        {
                punto =(Point)i.next();
                ye = punto.y;
                equis= punto.x;
                
                    if(this.matriz.getCelda(ye, equis).getEstado()==6)
                    {
                        cantidadAgentes++;
                    }
                        
                    cantidadVecinos++;
        }
        porcentaje=(double)((double)cantidadAgentes/(double)cantidadVecinos);
        return porcentaje;
    }
    
    
    public void paint(Graphics g) {
       // System.out.println("--------------------------------------------------- \n");
        for (int j = 7; j < this.alto; j+=7) {
            for (int i = 7; i < this.ancho; i+=7) {
                this.x = i / 7 - 1;
                this.y = j / 7 - 1;
                //System.out.println("(" + j + "," + i + "\n");
                g.setColor(Color.BLACK);
                g.drawRect(i, j, 7, 7);
                switch (this.matriz.getCelda(this.y, this.x).getEstado()) {
                 /*   case 0: { //ESTADO 0 CELDA VACIA
                        if (this.matriz.getCelda(this.y, this.x).getNivelHumo() >= 0.90) {
                            g.setColor(new Color(110, 110, 110));
                            g.fillRect(i + 1, j + 1, 6, 6);
                        }else if(this.matriz.getCelda(this.y, this.x).getNivelHumo() >= 0.70 && this.matriz.getCelda(this.y, this.x).getNivelHumo() < 0.90){
                            g.setColor(new Color(140, 140, 140));
                            g.fillRect(i + 1, j + 1, 6, 6);
                        } else if(this.matriz.getCelda(this.y, this.x).getNivelHumo() >= 0.40 && this.matriz.getCelda(this.y, this.x).getNivelHumo() < 0.70){
                            g.setColor(new Color(170, 170, 170));
                            g.fillRect(i + 1, j + 1, 6, 6);
                        } else if(this.matriz.getCelda(this.y, this.x).getNivelHumo() > 0.0 && this.matriz.getCelda(this.y, this.x).getNivelHumo() < 0.40){
                            g.setColor(new Color(200, 200, 200));
                            g.fillRect(i + 1, j + 1, 6, 6);
                        } 
                        else {
                            g.setColor(Color.WHITE);
                            g.fillRect(i + 1, j + 1, 6, 6);
                        }
                        if (this.matriz.getCelda(this.y, this.x).getNivelCombustibilidad() == 1) {
                            g.setColor(Color.BLACK);
                            g.drawLine(i + 1, j + 1, i + 6, j + 6);
                        }
                        break;
                    }
                    */
                    case 1: {//PARED EXTERIOR
                        g.setColor(Color.BLACK);
                        g.fillRect(i + 1, j + 1, 6, 6);
                        break;
                    }
                    case 2: {//OBSTACULO INTERIOR
                        g.setColor(new Color(121, 116, 182));
                        g.fillRect(i + 1, j + 1, 6, 6);
                        if (this.matriz.getCelda(this.y, this.x).getNivelCombustibilidad() == 1) {
                            g.setColor(Color.BLACK);
                            g.drawLine(i + 1, j + 1, i + 6, j + 6);
                        }
                        /*if (this.matriz.getEstado(this.y, this.x).getNivelCombustibilidad() != 1) continue block9;
                        g.setColor(Color.BLACK);
                        g.drawLine(i + 1, j + 1, i + 6, j + 6);
                        g.drawLine(i + 6, j + 1, i + 1, j + 6);*/
                        break;
                    }
                    /*
                    case 4: {//CELDA CON FUEGO
                        if (this.matriz.getCelda(this.y, this.x).getNivelFuego() >= 0.90) {
                            g.setColor(new Color(205, 0, 0));
                            g.fillRect(i + 1, j + 1, 6, 6);
                        }else if(this.matriz.getCelda(this.y, this.x).getNivelFuego() >= 0.70 && this.matriz.getCelda(this.y, this.x).getNivelFuego() < 0.90){
                            g.setColor(new Color(255, 0, 0));
                            g.fillRect(i + 1, j + 1, 6, 6);
                        } else if(this.matriz.getCelda(this.y, this.x).getNivelFuego() >= 0.40 && this.matriz.getCelda(this.y, this.x).getNivelFuego() < 0.70){
                            g.setColor(new Color(255, 50, 0));
                            g.fillRect(i + 1, j + 1, 6, 6);
                        } else if(this.matriz.getCelda(this.y, this.x).getNivelFuego() > 0.0 && this.matriz.getCelda(this.y, this.x).getNivelFuego() < 0.40){
                            g.setColor(new Color(255, 100, 0));
                            g.fillRect(i + 1, j + 1, 6, 6);
                        } 
                        break;
                    }
                    */
                    case 5: {//CELDA SALIDA
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(i + 1, j + 1, 6, 6);
                        break;
                    }
                    /*
                    case 6: {//CELDA CON AGENTE
                        //CASO DE UNA CELDA CON AGENTE Y FUEGO (06-10-2015)
                        if(this.matriz.getCelda(this.y, this.x).getNivelFuego()>0){ 
                            g.setColor(new Color(255, 50, 0));
                            g.fillRect(i + 1, j + 1, 6, 6);
                            g.setColor(Color.WHITE);
                            g.fillOval(i + 1, j + 1, 4, 4);
                        }
                        else{ //CASO DE SOLO UN AGENTE 
                            if (this.matriz.getCelda(this.y, this.x).getNivelHumo() > 0) {
                                g.setColor(new Color(51,202,204));
                                g.fillOval(i + 1, j + 1, 5, 5);
                            }
                            if (this.matriz.getCelda(this.y, this.x).getNivelTransito() != 0) {
                                g.setColor(Color.BLACK);
                                g.drawLine(i + 6, j + 1, i + 1, j + 6);
                            }
                            if (this.matriz.getCelda(this.y, this.x).getAgente().getTipo() == 0) {
                                g.setColor(new Color(51,202,204));
                                g.fillOval(i + 1, j + 1, 5, 5);
                            } 
                            if  (this.matriz.getCelda(this.y, this.x).getAgente().getTipo() == 1){
                                g.setColor(new Color(7,147,20));
                                g.fillOval(i + 1, j + 1, 5, 5);
                            }
                            if (this.matriz.getCelda(this.y, this.x).getNivelCombustibilidad() == 1) {
                                g.setColor(Color.BLACK);
                                g.drawLine(i + 1, j + 1, i + 6, j + 6);
                            }   
                        }
                    */
                    
                    
                    default:{
                        int diametro=3;
                        double porcentaje= this.porcentajeVecinos(this.y,this.x, this.cuadradrosAncho,this.cuadradosAlto, diametro, this.matriz);
                        
                        int r=0,green=0,b=0;
                        
                        if(0.0<=porcentaje && porcentaje<=0.5)
                        {
                          r=255;
                          green=255;
                          b=(int)(255-((255*(2*porcentaje))));
                        }
                            else if (0.5<porcentaje && porcentaje<=1) 
                            { 
                                green= (int)((1.0-2*(porcentaje-0.5))*255); 
                                r = 255; 
                                b = 0; 
                            } 
                           
                        g.setColor(new Color(r ,green, b));
                        g.fillRect(i + 1, j + 1, 5, 5);
                        break;
                        
                        
                    }
                   
                }
            }
        }
    }
    
    
     public void paint(Graphics g, AutomataCelular ac){
        this.matriz=ac;
        this.paint(g);
    }
     
    
    public AutomataCelular getMatriz() {
        return matriz;
    }

    public void setMatriz(AutomataCelular matriz) {
        this.matriz = matriz;
    }
    
}