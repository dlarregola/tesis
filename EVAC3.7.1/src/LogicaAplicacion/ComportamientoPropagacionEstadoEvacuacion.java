/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  evacesquiva.AutomataCelular
 *  evacesquiva.Comportamiento
 *  evacesquiva.ComportamientoSalidaMasCercana
 *  evacesquiva.Contador
 *  evacesquiva.DistanciaSalida
 *  evacesquiva.Estado
 *  evacesquiva.EstadoAgente
 *  evacesquiva.ThreadSimulacion
 *  evacesquiva.Utilidades
 */
package LogicaAplicacion;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Random;

public class ComportamientoPropagacionEstadoEvacuacion extends Comportamiento {
    
    private Random rand = new Random (System.currentTimeMillis());
    //private int random =rand.nextInt(2);
    
    @Override
    public boolean ejecutarComportamiento(AutomataCelular ac, AutomataCelular ac2, AutomataCelular acAmbiente, Point nodo) {
        int i, random = rand.nextInt(3);
        Point p;
        this.borrarVecinos();
        this.setVecinos(Utilidades.chequearMoore((Point)nodo, (AutomataCelular)ac));
        //SI VECINOS=0 EL AGENTE ESTA ENCERRADO PORQUE NO ENCUENTRA VECINOS DONDE MOVERSE (11-10-2015)
        if ( this.getVecinos().size()>0 && (ac.getCelda(nodo.y, nodo.x).getAgente().getContadorVelocidad() < ac.getCelda(nodo.y, nodo.x).getAgente().getVelocidad()) ) {
            int cuadradosAncho = (int)((double)Proyecto.getProyecto().getTamañox() / 0.4) + 1;
            int cuadradosAlto = (int)((double)Proyecto.getProyecto().getTamañoy() / 0.4) + 1;
            Point punto;
            int diametro=2, evacuar=0, y,x;
            
            LinkedList vecinos = new LinkedList();
            vecinos=Utilidades.vecindarioRadio(nodo.y, nodo.x, cuadradosAncho, cuadradosAlto, diametro, ac);
            ListIterator it = vecinos.listIterator();
         
            while(it.hasNext())
            {
                punto =(Point)it.next();
       
                    //CERCA DE ALGUIEN QUE ESTA EVACUANDO
                    if(ac.getCelda(punto.y, punto.x).getEstado()==6)
                    {
                        if(ac.getCelda(punto.y, punto.x).getAgente().getTipo()!=3)
                        {    
                            int Salida=ac.getCelda(punto.y,punto.x).getAgente().getSalidaElegida();
                        
                            if(Salida!=-1)
                            {
                                evacuar=1;
                            }
                        }   
                    }
                    
                    //CERCA DE FUEGO
                    if(ac.getCelda(punto.y, punto.x).getEstado()==3)
                    {
                        evacuar=1;   
                    }
                    
                    //CERCA DE HUMO
                    if(ac.getCelda(punto.y, punto.x).getNivelHumo()>0.0)
                    {
                        evacuar=1;   
                    }
            }
            
            if(evacuar==1){
                
                int random2= Utilidades.getRandom(3);
                if(random2==0)
                {
                    ac.getCelda(nodo.y, nodo.x).getAgente().setTipo(0);
                    ac.getCelda(nodo.y, nodo.x).getAgente().setRespuesta(2);
                    ac.getCelda(nodo.y, nodo.x).getAgente().setComportamiento(new ComportamientoSalidaMasCercana());
                }
                else
                {    
                    if(random2==1)
                    {
                        ac.getCelda(nodo.y, nodo.x).getAgente().setTipo(1);
                        ac.getCelda(nodo.y, nodo.x).getAgente().setRespuesta(2);
                        ac.getCelda(nodo.y, nodo.x).getAgente().setComportamiento(new ComportamientoSalidaMejorTiempo()); 
                    }
                    else
                    {    
                        if(random2==2)
                        {
                            ac.getCelda(nodo.y, nodo.x).getAgente().setTipo(2);
                            ac.getCelda(nodo.y, nodo.x).getAgente().setRespuesta(2);
                            ac.getCelda(nodo.y, nodo.x).getAgente().setComportamiento(new ComportamientoSalePorDondeEntro());
                        }    
                    }
                        
                }
                ac.getCelda(nodo.y, nodo.x).getAgente().getComportamiento().ejecutarComportamiento(ac, ac2, acAmbiente, nodo); 
            }            
            else
            {
                for (i = 0; i < this.getVecinos().size(); ++i) {
                    p = Utilidades.getPunto((int)((Integer)this.getVecinos().get(i)));
                
                    switch (ac.getCelda(p.y, p.x).getEstado()) {
                            case 0: {
                                if(acAmbiente.getCelda(p.y, p.x).getNivelFuego()<=0){
                                    this.getVecinosVaciosIgualDistancia().add(p);
                                    break;
                                }
                            }    
                             case 6: {
                                if(acAmbiente.getCelda(p.y, p.x).getNivelFuego()<=0){
                                    this.getVecinosOcupadosIgualDistancia().add(p);
                                    break;
                                }    
                                
                            }
                        }
                }
         
                    if (this.getVecinosVaciosIgualDistancia().size() > 0) {
                        int randomDesicion = Utilidades.getRandom((int)this.getVecinosVaciosIgualDistancia().size());
                        ac.getCelda(((Point)this.getVecinosVaciosIgualDistancia().get((int)randomDesicion)).y, ((Point)this.getVecinosVaciosIgualDistancia().get((int)randomDesicion)).x).getPeticiones().add(nodo);
                        ac.getCelda(nodo.y, nodo.x).getAgente().setDestino((Point)this.getVecinosVaciosIgualDistancia().get(randomDesicion));
                    }
                        else if (this.getVecinosOcupadosIgualDistancia().size() > 0) {
                        int randomDesicion = Utilidades.getRandom((int)this.getVecinosOcupadosIgualDistancia().size());
                        ac.getCelda(((Point)this.getVecinosOcupadosIgualDistancia().get((int)randomDesicion)).y, ((Point)this.getVecinosOcupadosIgualDistancia().get((int)randomDesicion)).x).getPeticiones().add(nodo);
                        ac.getCelda(nodo.y, nodo.x).getAgente().setDestino((Point)this.getVecinosOcupadosIgualDistancia().get(randomDesicion));
                    }
                    else
                    {
                        ac.getCelda(nodo.y, nodo.x).getAgente().setRespuesta(2); 
                    }    
            }
        } else {
            if(this.getVecinos().size()==0){//EL AGENTE ESTA ENCERRADO Y DEBE EN ESTE CASO MORIR!!!
                 return false; //SI EL COMPORTAMIENTO RETORNA FALSO ES PORQUE EL AGENTE NO PUEDE MOVERSE Y DEBE DESCONTARSE DE LOS
                //INDIVIDUOS QUE DEBEN SALIR ESTO SE REALIZA EN THREADSIMULACION EN EL METODO FASE INTENCION DONDE SE 
                //LLAMA AL COMPORTAMIENTO. (11-10-2015)
            }
            else{//NO SE MUEVE POR HABER ALCANZADO LA TOTALIDAD DE MOVIMIENTOS
                ac.getCelda(nodo.y, nodo.x).getAgente().setRespuesta(2);
            }
        }
        return true;
    }
}