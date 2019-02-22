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
public class Mapa extends Canvas{
    private int ratonX;
    private int ratonY;
    private int cuadradrosAncho;
    private int cuadradosAlto;
    private int i;
    private int j;
    private int x;
    private int y;
    private AutomataCelular matriz;
    private LinkedList visitados = new LinkedList();
    private boolean ejecutandoDesdeThreadSimulacion = false;
    private int tipoAgente;
    
    //CRISTIAN 14-06-2018 Variable para manejar el tamaño de las celdas de una manera más sensilla
    private int tamanoAspecto=7;
    
    //ESTAS LINEAS ESTABAN DE ANTES. LAS MOVI ABAJO PARA QUE TOMARAN EL VALOR DE LA VARIALBE tamanoAspecto
    private int ancho = tamanoAspecto + tamanoAspecto * ((int)((double)Proyecto.getProyecto().getTamañox() / 0.4) + 1);
    private int alto = tamanoAspecto + tamanoAspecto * ((int)((double)Proyecto.getProyecto().getTamañoy() / 0.4) + 1);
    
    
    
    public Mapa() {
        this.setSize(this.ancho + tamanoAspecto, this.alto + tamanoAspecto);
        this.setBackground(Color.WHITE);
        this.cuadradrosAncho = (int)((double)Proyecto.getProyecto().getTamañox() / 0.4) + 1;
        this.cuadradosAlto = (int)((double)Proyecto.getProyecto().getTamañoy() / 0.4) + 1;
        this.matriz = Proyecto.getProyecto().getAc();
    }

    private void updatePerimetro(Graphics g) {
        this.i = this.ratonX % tamanoAspecto;
        this.i = this.ratonX - this.i;
        this.x = this.i / tamanoAspecto - 1;
        this.j = this.ratonY % tamanoAspecto;
        this.j = this.ratonY - this.j;
        this.y = this.j / tamanoAspecto - 1;
        System.out.println("----------   Boton updatePerimetro   --------- \n");

        switch (this.matriz.getCelda(this.y, this.x).getEstado()) {
            case 0: 
            case 1: {
                g.setColor(Color.BLACK);
                this.matriz.getCelda(this.y, this.x).setEstado(1);
                g.fillRect(this.i + 1, this.j + 1, tamanoAspecto-1, tamanoAspecto-1);
                Proyecto.getProyecto().setProyectoValido(0);
                break;
            }
            default: {
                //BORRE LOS CARTELES PARA PODER DISEÑAR AMBIENTES MAS RAPIDO.
                //ESTO ES PORQUE YA SE SABEN COMO SE USA EL SISTEMA
                //17-11-2015 
                //JOptionPane.showMessageDialog(new JFrame(), "La celda debe ser de 'Perimetro' o 'Vacia'", "ERROR, SELECCIONE CELDA VALIDA", 1);
            }
        }
    }

    private void updateInterior(Graphics g) {
        this.i = this.ratonX % tamanoAspecto;
        this.i = this.ratonX - this.i;
        this.x = this.i / tamanoAspecto - 1;
        this.j = this.ratonY % tamanoAspecto;
        this.j = this.ratonY - this.j;
        this.y = this.j / tamanoAspecto - 1;
        System.out.println("----------   Boton updateInterior   --------- \n");

        switch (this.matriz.getCelda(this.y, this.x).getEstado()) {
            case 0: 
            case 2: {
                g.setColor(new Color(121, 116, 182));
                this.matriz.getCelda(this.y, this.x).setEstado(2);
                g.fillRect(this.i + 1, this.j + 1, tamanoAspecto-1, tamanoAspecto-1);
                Proyecto.getProyecto().setProyectoValido(0);
                break;
            }
            default: {
                //BORRE LOS CARTELES PARA PODER DISEÑAR AMBIENTES MAS RAPIDO.
                //ESTO ES PORQUE YA SE SABEN COMO SE USA EL SISTEMA
                //17-11-2015 
                //JOptionPane.showMessageDialog(new JFrame(), "La celda debe ser de 'Interior' o 'Vacia'", "ERROR, SELECCIONE CELDA VALIDA", 1);
            }
        }
    }

    private void updateTerreno(Graphics g) {
        this.i = this.ratonX % tamanoAspecto;
        this.i = this.ratonX - this.i;
        this.x = this.i / tamanoAspecto - 1;
        this.j = this.ratonY % tamanoAspecto;
        this.j = this.ratonY - this.j;
        this.y = this.j / tamanoAspecto - 1;
        switch (this.matriz.getCelda(this.y, this.x).getEstado()) {
            case 0: 
            case 6: {
                g.setColor(Color.BLACK);
                this.matriz.getCelda(this.y, this.x).setNivelTransito(0);
                g.drawLine(this.i + tamanoAspecto-1, this.j + 1, this.i + 1, this.j + tamanoAspecto-1);
                break;
            }
            default: {
                //BORRE LOS CARTELES PARA PODER DISEÑAR AMBIENTES MAS RAPIDO.
                //ESTO ES PORQUE YA SE SABEN COMO SE USA EL SISTEMA
                //17-11-2015 
                //JOptionPane.showMessageDialog(new JFrame(), "La celda debe ser 'Vacia'", "ERROR, SELECCIONE CELDA VALIDA", 1);
            }
        }
    }

    private void updateCombustion(Graphics g) {
        this.i = this.ratonX % tamanoAspecto;
        this.i = this.ratonX - this.i;
        this.x = this.i / tamanoAspecto - 1;
        this.j = this.ratonY % tamanoAspecto;
        this.j = this.ratonY - this.j;
        this.y = this.j / tamanoAspecto - 1;
        System.out.println("----------   Boton updateCombustion   --------- \n");

        switch (this.matriz.getCelda(this.y, this.x).getEstado()) {
            case 0: 
            case 2: 
            case 6: {
                g.setColor(Color.BLACK);
                this.matriz.getCelda(this.y, this.x).setNivelCombustibilidad(1);
                g.drawLine(this.i + 1, this.j + 1, this.i + tamanoAspecto-1, this.j + tamanoAspecto-1);
                break;
            }
            default: {
                //BORRE LOS CARTELES PARA PODER DISEÑAR AMBIENTES MAS RAPIDO.
                //ESTO ES PORQUE YA SE SABEN COMO SE USA EL SISTEMA
                //17-11-2015 
                //JOptionPane.showMessageDialog(new JFrame(), "La celda debe ser 'Vacia' o 'Interior'", "ERROR, SELECCIONE CELDA VALIDA", 1);
            }
        }
    }

    private void updateSalidas(Graphics g) {
        this.i = this.ratonX % tamanoAspecto;
        this.i = this.ratonX - this.i;
        this.x = this.i / tamanoAspecto - 1;
        this.j = this.ratonY % tamanoAspecto;
        this.j = this.ratonY - this.j;
        this.y = this.j / tamanoAspecto - 1;
        System.out.println("----------   Boton updateSalidas   --------- \n");

        switch (this.matriz.getCelda(this.y, this.x).getEstado()) {
            case 1: {
                Proyecto.getProyecto().setCantidadSalidas(Proyecto.getProyecto().getCantidadSalidas() + 1);
            }
            case 5: {
                g.setColor(Color.LIGHT_GRAY);
                this.matriz.getCelda(this.y, this.x).setEstado(5);
                g.fillRect(this.i + 1, this.j + 1, tamanoAspecto-1, tamanoAspecto-1);
                Proyecto.getProyecto().setProyectoValido(0);
                break;
            }
            default: {
                //BORRE LOS CARTELES PARA PODER DISEÑAR AMBIENTES MAS RAPIDO.
                //ESTO ES PORQUE YA SE SABEN COMO SE USA EL SISTEMA
                //17-11-2015 
                //JOptionPane.showMessageDialog(new JFrame(), "La celda debe ser de 'Perimetro' o 'Salida'", "ERROR, SELECCIONE CELDA VALIDA", 1);
            }
        }
    }

    private void updateBorrar(Graphics g) {
        this.i = this.ratonX % tamanoAspecto;
        this.i = this.ratonX - this.i;
        this.x = this.i / tamanoAspecto - 1;
        this.j = this.ratonY % tamanoAspecto;
        this.j = this.ratonY - this.j;
        this.y = this.j / tamanoAspecto - 1;
        g.setColor(Color.WHITE);
        System.out.println("----------   Boton borrar --------- \n");
        switch (this.matriz.getCelda(this.y, this.x).getEstado()) {
            case 2: {
                Proyecto.getProyecto().setProyectoValido(0);
                break;
            }
            case 6: {
                Proyecto.getProyecto().setCantidadPersonas(Proyecto.getProyecto().getCantidadPersonas()-1);
                Proyecto.getProyecto().setProyectoValido(0);
                break;
            }
            case 5: {
                Proyecto.getProyecto().setCantidadSalidas(Proyecto.getProyecto().getCantidadSalidas()-1);
                Proyecto.getProyecto().setProyectoValido(0);
            }
        }
        this.matriz.getCelda(this.y, this.x).setEstado(0);
        this.matriz.getCelda(this.y, this.x).setNivelCombustibilidad(0);
        this.matriz.getCelda(this.y, this.x).setNivelFuego(0);
        this.matriz.getCelda(this.y, this.x).setNivelHumo(0);
        this.matriz.getCelda(this.y, this.x).setNivelTransito(0);
        this.matriz.getCelda(this.y, this.x).deleteSensor();

        g.fillRect(this.i + 1, this.j + 1, tamanoAspecto-1, tamanoAspecto-1);
    }
    
    private void updateFuego(Graphics g) {
        this.i = this.ratonX % tamanoAspecto;
        this.i = this.ratonX - this.i;
        this.x = this.i / tamanoAspecto - 1;
        this.j = this.ratonY % tamanoAspecto;
        this.j = this.ratonY - this.j;
        this.y = this.j / tamanoAspecto - 1;
        System.out.println("----------   Boton updateFuego   --------- \n");

        switch (this.matriz.getCelda(this.y, this.x).getEstado()) {
            case 0: 
            case 4: {
                g.setColor(Color.RED);
                g.fillRect(this.i + 1, this.j + 1, tamanoAspecto-1, tamanoAspecto-1);
                this.matriz.getCelda(this.y, this.x).setEstado(4);
                this.matriz.getCelda(this.y, this.x).setNivelFuego(1);
                this.matriz.getCelda(this.y, this.x).setNivelHumo(1);
                break;
            }
            default: {
                //BORRE LOS CARTELES PARA PODER DISEÑAR AMBIENTES MAS RAPIDO.
                //ESTO ES PORQUE YA SE SABEN COMO SE USA EL SISTEMA
                //17-11-2015 
                //JOptionPane.showMessageDialog(new JFrame(), "La celda debe ser 'Vacia'", "ERROR, SELECCIONE CELDA VALIDA", 1);
            }
        }
    }

    private void updateSMC (Graphics g) {
        this.i = this.ratonX % tamanoAspecto;
        this.i = this.ratonX - this.i;
        this.x = this.i / tamanoAspecto - 1;
        this.j = this.ratonY % tamanoAspecto;
        this.j = this.ratonY - this.j;
        this.y = this.j / tamanoAspecto - 1;
        System.out.println("----------   Boton updateSMC   --------- \n");

        switch (this.matriz.getCelda(this.y, this.x).getEstado()) {
            case 0: {
                Proyecto.getProyecto().setCantidadPersonas(Proyecto.getProyecto().getCantidadPersonas()+1);
            }
            case 6: {
                g.setColor(new Color(51,202,204));
                g.fillOval(this.i + 1, this.j + 1, tamanoAspecto-2, tamanoAspecto-2);
                this.matriz.getCelda(this.y, this.x).setAgente(new Agente(0));
                this.matriz.getCelda(this.y, this.x).getAgente().setUbicacion(new Point(this.y, this.x));
                this.matriz.getCelda(this.y, this.x).setEstado(6);
                break;
            }
            default: {
                //BORRE LOS CARTELES PARA PODER DISEÑAR AMBIENTES MAS RAPIDO.
                //ESTO ES PORQUE YA SE SABEN COMO SE USA EL SISTEMA
                //17-11-2015 
                //JOptionPane.showMessageDialog(new JFrame(), "La celda debe ser 'Vacia'", "ERROR, SELECCIONE CELDA VALIDA", 1);
            }
        }
    }
    
    private void updateSMTE (Graphics g) {
        this.i = this.ratonX % tamanoAspecto;
        this.i = this.ratonX - this.i;
        this.x = this.i / tamanoAspecto - 1;
        this.j = this.ratonY % tamanoAspecto;
        this.j = this.ratonY - this.j;
        this.y = this.j / tamanoAspecto - 1;
        System.out.println("----------   Boton updateSMTE--------- \n");

        switch (this.matriz.getCelda(this.y, this.x).getEstado()) {
            case 0: {
                Proyecto.getProyecto().setCantidadPersonas(Proyecto.getProyecto().getCantidadPersonas()+1);
            }
            case 6: {
                g.setColor(new Color(7,147,20));
                g.fillOval(this.i + 1, this.j + 1, tamanoAspecto-2, tamanoAspecto-2);
                this.matriz.getCelda(this.y, this.x).setAgente(new Agente(1));
                this.matriz.getCelda(this.y, this.x).getAgente().setUbicacion(new Point(this.y, this.x));
                this.matriz.getCelda(this.y, this.x).setEstado(6);
                break;
            }
            default: {
                //BORRE LOS CARTELES PARA PODER DISEÑAR AMBIENTES MAS RAPIDO.
                //ESTO ES PORQUE YA SE SABEN COMO SE USA EL SISTEMA
                //17-11-2015 
                //JOptionPane.showMessageDialog(new JFrame(), "La celda debe ser 'Vacia'", "ERROR, SELECCIONE CELDA VALIDA", 1);
            }
        }
    }
    
    
    private void updateSPDE (Graphics g) {
        this.i = this.ratonX % tamanoAspecto;
        this.i = this.ratonX - this.i;
        this.x = this.i / tamanoAspecto - 1;
        this.j = this.ratonY % tamanoAspecto;
        this.j = this.ratonY - this.j;
        this.y = this.j / tamanoAspecto - 1;
        System.out.println("----------   Boton updateSPDE   --------- \n");

        switch (this.matriz.getCelda(this.y, this.x).getEstado()) {
            case 0: {
                Proyecto.getProyecto().setCantidadPersonas(Proyecto.getProyecto().getCantidadPersonas()+1);
            }
            case 6: {
                g.setColor(new Color(255,0,0));
                g.fillOval(this.i + 1, this.j + 1, tamanoAspecto-2, tamanoAspecto-2);
                this.matriz.getCelda(this.y, this.x).setAgente(new Agente(2));
                this.matriz.getCelda(this.y, this.x).getAgente().setUbicacion(new Point(this.y, this.x));
                this.matriz.getCelda(this.y, this.x).setEstado(6);
                break;
            }
            default: {
                JOptionPane.showMessageDialog(new JFrame(), "La celda debe ser 'Vacia'", "ERROR, SELECCIONE CELDA VALIDA", 1);
            }
        }
    }
    
    
    private void updatePEE (Graphics g) {
        this.i = this.ratonX % tamanoAspecto;
        this.i = this.ratonX - this.i;
        this.x = this.i / tamanoAspecto - 1;
        this.j = this.ratonY % tamanoAspecto;
        this.j = this.ratonY - this.j;
        this.y = this.j / tamanoAspecto - 1;
        System.out.println("----------   Boton updatePEE   --------- \n");

        switch (this.matriz.getCelda(this.y, this.x).getEstado()) {
            case 0: {
                Proyecto.getProyecto().setCantidadPersonas(Proyecto.getProyecto().getCantidadPersonas()+1);
            }
            case 6: {
                g.setColor(new Color(156,156,156));
                g.fillOval(this.i + 1, this.j + 1, tamanoAspecto-2, tamanoAspecto-2);
                this.matriz.getCelda(this.y, this.x).setAgente(new Agente(3));
                this.matriz.getCelda(this.y, this.x).getAgente().setUbicacion(new Point(this.y, this.x));
                this.matriz.getCelda(this.y, this.x).setEstado(6);
                break;
            }
            default: {
                JOptionPane.showMessageDialog(new JFrame(), "La celda debe ser 'Vacia'", "ERROR, SELECCIONE CELDA VALIDA", 1);
            }
        }
    }
    
    private void updateSML (Graphics g) {
        this.i = this.ratonX % tamanoAspecto;
        this.i = this.ratonX - this.i;
        this.x = this.i / tamanoAspecto - 1;
        this.j = this.ratonY % tamanoAspecto;
        this.j = this.ratonY - this.j;
        this.y = this.j / tamanoAspecto - 1;
        System.out.println("----------   Boton updateSML   --------- \n");
        switch (this.matriz.getCelda(this.y, this.x).getEstado()) {
            case 0: {
                Proyecto.getProyecto().setCantidadPersonas(Proyecto.getProyecto().getCantidadPersonas()+1);
            }
            case 6: {
                g.setColor(new Color(76,0,153));
                g.fillOval(this.i + 1, this.j + 1, tamanoAspecto-2, tamanoAspecto-2);
                this.matriz.getCelda(this.y, this.x).setAgente(new Agente(4));
                this.matriz.getCelda(this.y, this.x).getAgente().setUbicacion(new Point(this.y, this.x));
                this.matriz.getCelda(this.y, this.x).setEstado(6);
                break;
            }
            default: {
                JOptionPane.showMessageDialog(new JFrame(), "La celda debe ser 'Vacia'", "ERROR, SELECCIONE CELDA VALIDA", 1);
            }
        }
    }

    private void updateASSS (Graphics g) {
        this.i = this.ratonX % tamanoAspecto;
        this.i = this.ratonX - this.i;
        this.x = this.i / tamanoAspecto - 1;
        this.j = this.ratonY % tamanoAspecto;
        this.j = this.ratonY - this.j;
        this.y = this.j / tamanoAspecto - 1;
        System.out.println("----------   Boton updateASSS   --------- \n");
        switch (this.matriz.getCelda(this.y, this.x).getEstado()) {
            case 0: {
                Proyecto.getProyecto().setCantidadPersonas(Proyecto.getProyecto().getCantidadPersonas()+1);
            }
            case 6: {
                g.setColor(new Color(125,52,121));
                g.fillOval(this.i + 1, this.j + 1, tamanoAspecto-2, tamanoAspecto-2);
                this.matriz.getCelda(this.y, this.x).setAgente(new Agente(5));
                this.matriz.getCelda(this.y, this.x).getAgente().setUbicacion(new Point(this.y, this.x));
                this.matriz.getCelda(this.y, this.x).setEstado(6);
                break;
            }
            default: {
                JOptionPane.showMessageDialog(new JFrame(), "La celda debe ser 'Vacia'", "ERROR, SELECCIONE CELDA VALIDA", 1);
            }
        }
    }
    
    
    private void updateDensidad (Graphics g) {
        this.i = this.ratonX % tamanoAspecto;
        this.i = this.ratonX - this.i;
        this.x = this.i / tamanoAspecto - 1;
        this.j = this.ratonY % tamanoAspecto;
        this.j = this.ratonY - this.j;
        this.y = this.j / tamanoAspecto - 1;
        switch (this.matriz.getCelda(this.y, this.x).getEstado()) {
            case 0:
            case 4: {
                System.out.println("----------   Boton Densidad   --------- \n");
                Sensor sens = new Sensor(Proyecto.getProyecto().getPotenciaSensor(),this.x,this.y,101);
                this.matriz.getCelda(this.y, this.x).setSensor(sens);
                Proyecto.getProyecto().addListSensores(this.matriz.getCelda(this.y, this.x).getSensor());
                g.setColor(Color.green);
                g.drawOval(this.i+1,this.j+1,5,5);

                Proyecto.getProyecto().setProyectoValido(0);

                break;
            }
            default: {
                //BORRE LOS CARTELES PARA PODER DISEÑAR AMBIENTES MAS RAPIDO.
                //ESTO ES PORQUE YA SE SABEN COMO SE USA EL SISTEMA
                //17-11-2015
                //JOptionPane.showMessageDialog(new JFrame(), "La celda debe ser 'Vacia'", "ERROR, SELECCIONE CELDA VALIDA", 1);
            }
        }
    }
    
    
    private void updateInicio(Graphics g) {
        Point p = new Point();
        this.i = this.ratonX % tamanoAspecto;
        this.i = this.ratonX - this.i;
        this.x = this.i / tamanoAspecto - 1;
        this.j = this.ratonY % tamanoAspecto;
        this.j = this.ratonY - this.j;
        this.y = this.j / tamanoAspecto - 1;
        p.x = this.x;
        p.y = this.y;
        switch (this.matriz.getCelda(this.y, this.x).getEstado()) {
            case 1: {
                System.out.println("----------   Boton Inicio   --------- \n");
                Proyecto.getProyecto().setPuntoInicio(p);
                break;
            }
            default: {
                //BORRE LOS CARTELES PARA PODER DISEÑAR AMBIENTES MAS RAPIDO.
                //ESTO ES PORQUE YA SE SABEN COMO SE USA EL SISTEMA
                //17-11-2015 
                //JOptionPane.showMessageDialog(new JFrame(), "La celda debe ser 'Perimetro'", "ERROR, SELECCIONE CELDA VALIDA", 1);
            }
        }
    }

    @Override
    public void update(Graphics g) {
        if (this.ratonX > 7 && this.ratonY > 7 && this.ratonX < this.ancho && this.ratonY < this.alto) {
            switch (VentanaConfiguracionAmbiente.getVentanaConfiguracionAmbiente().botonSeleccionado()) {
                case 0: {
                    this.updatePerimetro(g);
                    break;
                }
                case 1: {
                    this.updateInterior(g);
                    break;
                }
                case 2: {
                    this.updateSalidas(g);
                    break;
                }
                case 3: {
                    this.updateFuego(g);
                    break;
                }
                case 4: {
                    this.updateCombustion(g);
                    break;
                }
                case 5: {
                    this.updateSMC(g);
                    break;
                }
                case 6: {
                    this.updateSMTE(g);
                    break;
                }
                case 7: {
                    this.updateSPDE(g);
                    break;
                }
                case 8: {
                    this.updateASSS(g);
                    break;
                }
                case 9:{
                    this.updateSML(g);
                    break;
                }
                case 11: {
                    this.updateDensidad(g);
                    break;
                }
                case 12: {
                    this.updateBorrar(g);
                    break;
                }
                case 13:{
                    this.updateInicio(g);
                    break;
                }
            }
        }
    }

    @Override
    public void paint(Graphics g) {
               
        /*if(ejecutandoDesdeThreadSimulacion){
            System.out.println("----------   SI   --------- \n");
            ejecutandoDesdeThreadSimulacion=false;
        }
        else{
            System.out.println("----------   no   --------- \n");
        }*/
        
        for (int j = tamanoAspecto; j < this.alto; j+=tamanoAspecto) {
            for (int i = tamanoAspecto; i < this.ancho; i+=tamanoAspecto) {
                this.x = i / tamanoAspecto - 1;
                this.y = j / tamanoAspecto - 1;
                //System.out.println("(" + j + "," + i + "\n");
                g.setColor(Color.BLACK);
                g.drawRect(i, j, tamanoAspecto, tamanoAspecto);
                switch (this.matriz.getCelda(this.y, this.x).getEstado()) {
                    case 0: { //ESTADO 0 CELDA VACIA
                        if (this.matriz.getCelda(this.y, this.x).getNivelHumo() >= 0.90) {
                            g.setColor(new Color(110, 110, 110));
                            g.fillRect(i + 1, j + 1, tamanoAspecto-1, tamanoAspecto-1);
                        }else if(this.matriz.getCelda(this.y, this.x).getNivelHumo() >= 0.70 && this.matriz.getCelda(this.y, this.x).getNivelHumo() < 0.90){
                            g.setColor(new Color(140, 140, 140));
                            g.fillRect(i + 1, j + 1, tamanoAspecto-1, tamanoAspecto-1);
                        } else if(this.matriz.getCelda(this.y, this.x).getNivelHumo() >= 0.40 && this.matriz.getCelda(this.y, this.x).getNivelHumo() < 0.70){
                            g.setColor(new Color(170, 170, 170));
                            g.fillRect(i + 1, j + 1, tamanoAspecto-1, tamanoAspecto-1);
                        } else if(this.matriz.getCelda(this.y, this.x).getNivelHumo() > 0.0 && this.matriz.getCelda(this.y, this.x).getNivelHumo() < 0.40){
                            g.setColor(new Color(200, 200, 200));
                            g.fillRect(i + 1, j + 1, tamanoAspecto-1, tamanoAspecto-1);
                        } 
                        else {
                            g.setColor(Color.WHITE);
                            g.fillRect(i + 1, j + 1, tamanoAspecto-1, tamanoAspecto-1);
                        }
                        if (this.matriz.getCelda(this.y, this.x).getNivelCombustibilidad() == 1) {
                            g.setColor(Color.BLACK);
                            g.drawLine(i + 1, j + 1, i + tamanoAspecto-1, j + tamanoAspecto-1);
                        }

                        //Dibujo sensor de densidad cuando cargo un archivo
                        if (this.matriz.getCelda(this.y, this.x).getTipoSensor() == 101){
                           // System.out.println("Celda x: "+this.y+" y: "+this.x+" tipo Sensor "+this.matriz.getCelda(this.y, this.x).getTipoSensor() );

                            g.setColor(Color.green);
                            g.drawOval(i + 1,j + 1,5,5);
                        }
                        break;
                    }
                    case 1: {//PARED EXTERIOR
                        g.setColor(Color.BLACK);
                        g.fillRect(i + 1, j + 1, tamanoAspecto-1, tamanoAspecto-1);
                        break;
                    }
                    case 2: {//OBSTACULO INTERIOR
                        g.setColor(new Color(121, 116, 182));
                        g.fillRect(i + 1, j + 1, tamanoAspecto-1, tamanoAspecto-1);
                        if (this.matriz.getCelda(this.y, this.x).getNivelCombustibilidad() == 1) {
                            g.setColor(Color.BLACK);
                            g.drawLine(i + 1, j + 1, i + tamanoAspecto-1, j + tamanoAspecto-1);
                        }
                        /*if (this.matriz.getEstado(this.y, this.x).getNivelCombustibilidad() != 1) continue block9;
                        g.setColor(Color.BLACK);
                        g.drawLine(i + 1, j + 1, i + 6, j + 6);
                        g.drawLine(i + 6, j + 1, i + 1, j + 6);*/
                        break;
                    }
                    case 4: {//CELDA CON FUEGO
                        if (this.matriz.getCelda(this.y, this.x).getNivelFuego() >= 0.90) {
                            g.setColor(new Color(205, 0, 0));
                            g.fillRect(i + 1, j + 1, tamanoAspecto-1, tamanoAspecto-1);
                        }else if(this.matriz.getCelda(this.y, this.x).getNivelFuego() >= 0.70 && this.matriz.getCelda(this.y, this.x).getNivelFuego() < 0.90){
                            g.setColor(new Color(255, 0, 0));
                            g.fillRect(i + 1, j + 1, tamanoAspecto-1, tamanoAspecto-1);
                        } else if(this.matriz.getCelda(this.y, this.x).getNivelFuego() >= 0.40 && this.matriz.getCelda(this.y, this.x).getNivelFuego() < 0.70){
                            g.setColor(new Color(255, 50, 0));
                            g.fillRect(i + 1, j + 1, tamanoAspecto-1, tamanoAspecto-1);
                        } else if(this.matriz.getCelda(this.y, this.x).getNivelFuego() > 0.20 && this.matriz.getCelda(this.y, this.x).getNivelFuego() < 0.40){
                            g.setColor(new Color(255, 100, 0));
                            g.fillRect(i + 1, j + 1, tamanoAspecto-1, tamanoAspecto-1);
                        } else if(this.matriz.getCelda(this.y, this.x).getNivelFuego() > 0.0 && this.matriz.getCelda(this.y, this.x).getNivelFuego() < 0.20){
                            g.setColor(new Color(255, 200, 0));
                            g.fillRect(i + 1, j + 1, tamanoAspecto-1, tamanoAspecto-1);
                        }
                        //Dibujo sensor de densidad cuando cargo un archivo
                        if (this.matriz.getCelda(this.y, this.x).getTipoSensor() == 101){
                            g.setColor(Color.green);
                            g.drawOval(i+1,j + 1,5,5);
                        }
                        break;
                    }
                    case 5: {//CELDA SALIDA
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(i + 1, j + 1, tamanoAspecto-1, tamanoAspecto-1);
                        break;
                    }
                    case 6: {//CELDA CON AGENTE
                        //SACO EL TIPO DEL AGENTE ANTES PARA LUEGO PREGUNTAR DE QUE COLOR LO DEBO PINTAR
                        //PORQUE SI PREGUNTO UTILIZANDO EL METODO getTipo DE AGENTE MAS DE UNA VEZ ME DA ERROR 
                        //APARENTE BUG EN JAVA.
                        tipoAgente=this.matriz.getCelda(this.y, this.x).getAgente().getTipo();
                        //CASO DE UNA CELDA CON AGENTE Y FUEGO (06-10-2015)
                        if(this.matriz.getCelda(this.y, this.x).getNivelFuego()>0){ 
                            g.setColor(new Color(255, 50, 0));
                            g.fillRect(i + 1, j + 1, tamanoAspecto-1, tamanoAspecto-1);
                            g.setColor(Color.WHITE);
                            g.fillOval(i + 1, j + 1, tamanoAspecto-3, tamanoAspecto-3);
                        }
                        else{ //CASO DE SOLO UN AGENTE 
                            if (this.matriz.getCelda(this.y, this.x).getNivelHumo() > 0) {
                                g.setColor(new Color(51,202,204));
                                g.fillOval(i + 1, j + 1, tamanoAspecto-2, tamanoAspecto-2);
                            }
                            if (this.matriz.getCelda(this.y, this.x).getNivelTransito() != 0) {
                                g.setColor(Color.BLACK);
                                g.drawLine(i + tamanoAspecto-1, j + 1, i + 1, j + tamanoAspecto-1);
                            }
                            if (tipoAgente == 0) {
                                g.setColor(new Color(51,202,204));
                                g.fillOval(i + 1, j + 1, tamanoAspecto-2, tamanoAspecto-2);
                            } 
                            if  (tipoAgente == 1){
                                g.setColor(new Color(7,147,20));
                                g.fillOval(i + 1, j + 1, tamanoAspecto-2, tamanoAspecto-2);
                            }
                            if  (tipoAgente == 2){
                                g.setColor(new Color(255,0,0));
                                g.fillOval(i + 1, j + 1, tamanoAspecto-2, tamanoAspecto-2);
                            }
                            if  (tipoAgente == 3){
                                g.setColor(new Color(156,156,156));
                                g.fillOval(i + 1, j + 1, tamanoAspecto-2, tamanoAspecto-2);
                            }
                            if  (tipoAgente == 4){
                                g.setColor(new Color(76,0,153));
                                g.fillOval(i + 1, j + 1, tamanoAspecto-2, tamanoAspecto-2);
                            }
                            if  (tipoAgente == 5){
                                g.setColor(new Color(125,52,121));
                                g.fillOval(i + 1, j + 1, tamanoAspecto-2, tamanoAspecto-2);
                            }
                            if (this.matriz.getCelda(this.y, this.x).getNivelCombustibilidad() == 1) {
                                g.setColor(Color.BLACK);
                                g.drawLine(i + 1, j + 1, i + tamanoAspecto-1, j + tamanoAspecto-1);
                            }   
                        }
                        //Dibujo sensor de densidad cuando cargo un archivo
                        if (this.matriz.getCelda(this.y, this.x).getTipoSensor() == 101){
                            g.setColor(Color.green);
                            g.drawOval(i+1,j + 1,5,5);
                        }
                    }
                }
            }
        }
    }
    
    public void paint(Graphics g, AutomataCelular ac){
        this.matriz=ac;
        this.paint(g);
        ejecutandoDesdeThreadSimulacion=true;
    }
    
    public void setRatonX(int ratonX) {
        this.ratonX = ratonX;
    }

    public void setRatonY(int ratonY) {
        this.ratonY = ratonY;
    }
    
    
    
    
    //CRISTIAN 06/08/2018
    public void configurarAgentes(){
        try{
            String reaccion=VentanaConfiguracionAmbiente.getVentanaConfiguracionAmbiente().getTiempoReaccion();
            //System.out.println("REACCION:" + reaccion);
            //System.out.println("MIN:" + reaccion.substring(1,reaccion.indexOf("-")) + "   MAX:" + reaccion.substring(reaccion.indexOf("-")+1, reaccion.indexOf("]")) );
            int velMinima, velMaxima, velDif, reaccionMinimo, reaccionMaximo, reaccionDif;
            velMinima=VentanaConfiguracionAmbiente.getVentanaConfiguracionAmbiente().getVelMinima()+1;
            velMaxima=VentanaConfiguracionAmbiente.getVentanaConfiguracionAmbiente().getVelMaxima()+1;
            velDif=velMaxima-velMinima;
            //CONTOLAR ESTO PORQUE ME PUEDE SETEAR AL REVES ES DECIR LA MINIMA MAS GANDE QUE LA MAXIMA
            reaccionMinimo=Integer.parseInt(reaccion.substring(1,reaccion.indexOf("-")));
            reaccionMaximo=Integer.parseInt( reaccion.substring(reaccion.indexOf("-")+1, reaccion.indexOf("]")) );
            reaccionMinimo= reaccionMinimo*10; //Cantidad de sub pasos de tiempo
            reaccionMaximo= reaccionMaximo*10;        
            reaccionDif=reaccionMaximo-reaccionMinimo;
            if(velDif >= 0 && reaccionDif >= 0){
                for (int i = 0; i < this.cuadradosAlto; ++i) {
                    for (int j = 0; j < this.cuadradrosAncho; ++j) {
                        if (this.matriz.getCelda(i, j).getEstado() == 6){
                        
                            if(velDif!=0){
                                //+1 Por como se sacan los randoms. Sino me queda fuera el limite superior
                                this.matriz.getCelda(i, j).getAgente().setVelocidad(Utilidades.getRandom(velDif + 1) + velMinima);
                            }
                            else{ 
                                this.matriz.getCelda(i, j).getAgente().setVelocidad(velMinima); //La minima y la maxima son iguales 
                            }
                        
                            if(reaccionDif!=0){
                                //+1 Por como se sacan los randoms. Sino me queda fuera el limite superior
                                this.matriz.getCelda(i,j).getAgente().setDemoraReaccion(Utilidades.getRandom(reaccionDif + 1)+reaccionMinimo);
                            }
                            else{ 
                                this.matriz.getCelda(i, j).getAgente().setDemoraReaccion(reaccionMinimo); //La minima y la maxima son iguales 
                            }
                        }
                    }
                }
            }
            else{
                JOptionPane.showMessageDialog(new JFrame(),"ERROR en datos: Tpo. de reacción o Velocidad");
            }
        }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(new JFrame(),"ERROR en formato de datos: Tpo. de reacción");
        }    
    }
    
    
    public boolean validarMapa() {
        int errores = 0;
        int tipoLocal = 0;
        JProgressBar barraProgreso=VentanaConfiguracionAmbiente.getVentanaConfiguracionAmbiente().getProgressBar(); 
        barraProgreso.setMinimum(1);
        barraProgreso.setMaximum(Proyecto.getProyecto().getCantidadSalidas() + 3);
        barraProgreso.setValue(barraProgreso.getValue() + 1);
        barraProgreso.paint(barraProgreso.getGraphics());
        for (int i = 0; i < this.cuadradosAlto; ++i) {
            for (int j = 0; j < this.cuadradrosAncho; ++j) {
                if (this.matriz.getCelda(i, j).getEstado() != 1) continue;
                tipoLocal = this.validarOrtogonales(new Point(j, i));
                if (tipoLocal < 3) {
                    if (tipoLocal != 2 || this.validarMoore(new Point(j, i)) == 0) continue;
                    JOptionPane.showMessageDialog(new JFrame(), "Contorno Multiple: (" + i + "," + j + ")", "ERROR DE VALIDACION", 1);
                    ++errores;
                    continue;
                }
                JOptionPane.showMessageDialog(new JFrame(), "Contorno Multiple o el Contorno no Cierra un Ciclo: (" + i + "," + j + ")", "ERROR DE VALIDACI\u00d3N", 1);
                ++errores;
            }
        }
     
        //CRISTIAN 07/08/2018
        this.configurarAgentes();
                
        if (errores == 0) {
            barraProgreso.setValue(barraProgreso.getValue() + 1);
            barraProgreso.paint(barraProgreso.getGraphics());
            barraProgreso.setValue(barraProgreso.getValue() + 1);
            barraProgreso.paint(barraProgreso.getGraphics());
            
                                         
            if (Proyecto.getProyecto().getPuntoInicio().getX() == -1 && Proyecto.getProyecto().getPuntoInicio().getY() == -1){
            //CRISTIAN 07/08/2018 Cambie la linea de abajo por la de arriba. Es decir estaba la linea de abajo
            //if (Proyecto.getProyecto().getPuntoInicio().getX() == 0.0 && Proyecto.getProyecto().getPuntoInicio().getY() == 0.0) {
                Proyecto.getProyecto().setProyectoValido(0);
                JOptionPane.showMessageDialog(new JFrame(), "Debe configurar el Punto de Inicio", "Error Punto de Inicio", 1);
            } else {
                 Proyecto.getProyecto().setProyectoValido(1);
                 barraProgreso.setValue(0);
                 return true;
            }
        }
        barraProgreso.setValue(0);
        return false;
    }
    
    private boolean chequearPunto(Point nodo) {
        if (nodo.x > -1 && nodo.x < this.cuadradrosAncho && nodo.y > -1 && nodo.y < this.cuadradosAlto && this.matriz.getCelda(nodo.y, nodo.x).getEstado() != 2 && this.matriz.getCelda(nodo.y, nodo.x).getEstado() != 4) {
            return true;
        }
        return false;
    }
    
    private int validarOrtogonales(Point origen) {
        ArrayList<Point> vecinos = new ArrayList<Point>();
        int contador = 0;
        Point p1 = new Point(origen.x, origen.y - 1);
        Point p2 = new Point(origen.x, origen.y + 1);
        Point p3 = new Point(origen.x - 1, origen.y);
        Point p4 = new Point(origen.x + 1, origen.y);
        if (this.chequearPunto(p1) && (this.matriz.getCelda(p1.y, p1.x).getEstado() == 1 || this.matriz.getCelda(p1.y, p1.x).getEstado() == 5)) {
            vecinos.add(p1);
        }
        if (this.chequearPunto(p2) && (this.matriz.getCelda(p2.y, p2.x).getEstado() == 1 || this.matriz.getCelda(p2.y, p2.x).getEstado() == 5)) {
            vecinos.add(p2);
        }
        if (this.chequearPunto(p3) && (this.matriz.getCelda(p3.y, p3.x).getEstado() == 1 || this.matriz.getCelda(p3.y, p3.x).getEstado() == 5)) {
            vecinos.add(p3);
        }
        if (this.chequearPunto(p4) && (this.matriz.getCelda(p4.y, p4.x).getEstado() == 1 || this.matriz.getCelda(p4.y, p4.x).getEstado() == 5)) {
            vecinos.add(p4);
        }
        ListIterator it = vecinos.listIterator();
        while (it.hasNext()) {
            Point p0 = (Point)it.next();
            if (this.matriz.getCelda(p0.y, p0.x).getEstado() != 1 && this.matriz.getCelda(p0.y, p0.x).getEstado() != 5) continue;
            ++contador;
        }
        if (contador == 2) {
            if (((Point)vecinos.get((int)0)).x == origen.x && ((Point)vecinos.get((int)1)).x == origen.x || ((Point)vecinos.get((int)0)).y == origen.y && ((Point)vecinos.get((int)1)).y == origen.y) {
                return 1;
            }
            return 2;
        }
        return 3;
    }

    private int validarMoore(Point origen) {
        ArrayList<Point> vecinos = new ArrayList<Point>();
        int contador = 0;
        Point p1 = new Point(origen.x - 1, origen.y - 1);
        Point p2 = new Point(origen.x + 1, origen.y - 1);
        Point p3 = new Point(origen.x - 1, origen.y + 1);
        Point p4 = new Point(origen.x + 1, origen.y + 1);
        if (this.chequearPunto(p1)) {
            vecinos.add(p1);
        }
        if (this.chequearPunto(p2)) {
            vecinos.add(p2);
        }
        if (this.chequearPunto(p3)) {
            vecinos.add(p3);
        }
        if (this.chequearPunto(p4)) {
            vecinos.add(p4);
        }
        ListIterator it = vecinos.listIterator();
        while (it.hasNext()) {
            Point p0 = (Point)it.next();
            if (this.matriz.getCelda(p0.y, p0.x).getEstado() != 1) continue;
            ++contador;
        }
        return contador;
    }  

    public AutomataCelular getMatriz() {
        return matriz;
    }

    public void setMatriz(AutomataCelular matriz) {
        this.matriz = matriz;
    }
    
    
}
