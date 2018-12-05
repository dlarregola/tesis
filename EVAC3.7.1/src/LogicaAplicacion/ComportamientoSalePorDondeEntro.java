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

public class ComportamientoSalePorDondeEntro extends Comportamiento {
    
    private Random rand = new Random (System.currentTimeMillis());
    
    @Override
    public boolean ejecutarComportamiento(AutomataCelular ac, AutomataCelular ac2, AutomataCelular acAmbiente, Point nodo) {
        
        int cantidadSalidas=Proyecto.getProyecto().getSalidas().size();
        int puerta;
        //System.out.println("Puerta ENtro"+ puerta);
        //int puerta = rand.nextInt(cantidadSalidas);
        
        this.borrarVecinos();
        this.setVecinos(Utilidades.chequearMoore((Point)nodo, (AutomataCelular)ac));
        //SI VECINOS=0 EL AGENTE ESTA ENCERRADO PORQUE NO ENCUENTRA VECINOS DONDE MOVERSE (11-10-2015)
        if ( this.getVecinos().size()>0 && (ac.getCelda(nodo.y, nodo.x).getAgente().getContadorVelocidad() < ac.getCelda(nodo.y, nodo.x).getAgente().getVelocidad()) ) {
            Point p;
            int i;
            int comparacion=1;
            
            try {
                //SE LE ASIGNA UNA SALIDA
                if((ac.getCelda(nodo.y, nodo.x).getAgente().getSalidaElegida()) == -1){
           
                    //puerta =  Utilidades.getRandom((cantidadSalidas -1) +1 ) +1 ;
                    puerta =  Utilidades.getRandom(cantidadSalidas)+1 ;
                    ac.getCelda(nodo.y, nodo.x).getAgente().setSalidaElegida(puerta);
                    ((Contador)ThreadSimulacion.intenciones.get( puerta -1 )).incrementarContador();
                }
             }
            catch (Exception ex) {
                System.out.println("CHEKING...  CELDA (" + nodo.x + "," + nodo.y + ")");
                ex.printStackTrace();
            }
            

            //EN CASO DE QUE LA SALIDA ASIGNADA SEA INACCESIBLE CAMBIA EL COMPORTAMIENTO
            puerta= ac.getCelda(nodo.y, nodo.x).getAgente().getSalidaElegida();
            LinkedList distanciasSalidas = ac.getCelda(nodo.y, nodo.x).getDistanciasSalidas();
            ListIterator iterador = distanciasSalidas.listIterator();
       
            while (iterador.hasNext()) {
                     
                DistanciaSalida ds = (DistanciaSalida)iterador.next();
            
                if (ds.getSalida()== puerta ){ 
                    comparacion=ds.getDistancia().compareTo(-1.0);
                }
            }
                 
            if(comparacion==0){
            
                int random = Utilidades.getRandom(2);
                    
                if(random==0)
                {
                    ac.getCelda(nodo.y, nodo.x).getAgente().setTipo(0);
                    ac.getCelda(nodo.y, nodo.x).getAgente().setRespuesta(2);
                    ac.getCelda(nodo.y, nodo.x).getAgente().setComportamiento(new ComportamientoSalidaMasCercana());     
                }
                else
                {
                    ac.getCelda(nodo.y, nodo.x).getAgente().setTipo(1);
                    ac.getCelda(nodo.y, nodo.x).getAgente().setRespuesta(2);
                    ac.getCelda(nodo.y, nodo.x).getAgente().setComportamiento(new ComportamientoSalidaMejorTiempo());         
                }     
                ac.getCelda(nodo.y, nodo.x).getAgente().getComportamiento().ejecutarComportamiento(ac, ac2, acAmbiente, nodo); 
            }
           
           
            else {
                
                for (i = 0; i < this.getVecinos().size(); ++i) {
                    p = Utilidades.getPunto((int)((Integer)this.getVecinos().get(i)));
                
                    switch (ac.getCelda(p.y, p.x).getEstado()) {
                            case 0: {
                                    if(acAmbiente.getCelda(p.y, p.x).getNivelFuego()<=0){ //INTENTO EVITAR QUE EL AGENTE SE MUEVA A UNA POSICION CON FUEGO (10-10-2015)
                                        if( (Utilidades.obtenerDistanciaSalida(acAmbiente.getCelda(p.y, p.x), ac.getCelda(nodo.y, nodo.x).getAgente().getSalidaElegida())) < ( Utilidades.obtenerDistanciaSalida(ac.getCelda(nodo.y, nodo.x), ac.getCelda(nodo.y, nodo.x).getAgente().getSalidaElegida()) )  ){
                                            this.getVecinosVaciosMejorDistancia().add(p);
                                    }
                                //AQUI DEBO COMPARAR DE ESTA FORMA PORQUE SINO PUEDO TENER ERRORES POR COMPRACION CON OPERADORES
                                //NUMERICOS CON DOUBLE. NO DA EL RESULTADO ESPERADO SI SE HAC CON OPERADORES. SE DEBE HACER ASI
                                //O EN SU DEFECTO CON EL METODO CompareTo DE DOUBLE 12-10-2015
                                    else if(Objects.equals(Utilidades.obtenerDistanciaSalida(acAmbiente.getCelda(p.y, p.x), ac.getCelda(nodo.y, nodo.x).getAgente().getSalidaElegida()), Utilidades.obtenerDistanciaSalida(ac.getCelda(nodo.y, nodo.x), ac.getCelda(nodo.y, nodo.x).getAgente().getSalidaElegida()))){
                                        this.getVecinosVaciosIgualDistancia().add(p);
                                    }
                                }
                            break;
                            }
                            case 4: {
                                break;
                            }
                            case 5: {
                                this.getVecinosSalida().add(p);
                                break;
                            }
                            case 6: {
                                if(acAmbiente.getCelda(p.y, p.x).getNivelFuego()<=0){////INTENTO EVITAR QUE EL AGENTE SE MUEVA A UNA POSICION CON FUEGO (10-10-2015)
                                    if( (Utilidades.obtenerDistanciaSalida(acAmbiente.getCelda(p.y, p.x), ac.getCelda(nodo.y, nodo.x).getAgente().getSalidaElegida())) < ( Utilidades.obtenerDistanciaSalida(ac.getCelda(nodo.y, nodo.x), ac.getCelda(nodo.y, nodo.x).getAgente().getSalidaElegida()) )  ){
                                        //this.getVecinosVaciosMejorDistancia().add(p);
                                        this.getVecinosOcupadosMejorDistancia().add(p);
                                    }
                                //AQUI DEBO COMPARAR DE ESTA FORMA PORQUE SINO PUEDO TENER ERRORES POR COMPRACION CON OPERADORES
                                //NUMERICOS CON DOUBLE. NO DA EL RESULTADO ESPERADO SI SE HAC CON OPERADORES. SE DEBE HACER ASI
                                //O EN SU DEFECTO CON EL METODO CompareTo DE DOUBLE 12-10-2015
                                    else if(Objects.equals(Utilidades.obtenerDistanciaSalida(acAmbiente.getCelda(p.y, p.x), ac.getCelda(nodo.y, nodo.x).getAgente().getSalidaElegida()), Utilidades.obtenerDistanciaSalida(ac.getCelda(nodo.y, nodo.x), ac.getCelda(nodo.y, nodo.x).getAgente().getSalidaElegida()))){
                                        //this.getVecinosVaciosIgualDistancia().add(p);
                                        this.getVecinosOcupadosIgualDistancia().add(p);
                                    }
                                }
                            break;
                            }
                    }
                }
            
                if (ac.getCelda(nodo.y, nodo.x).getAgente().getTiempoNoActualizacion() < 3){
                    if (this.getVecinosSalida().size() > 0) {
                        int randomDesicion = Utilidades.getRandom((int)this.getVecinosSalida().size());
                        ac.getCelda(((Point)this.getVecinosSalida().get((int)randomDesicion)).y, ((Point)this.getVecinosSalida().get((int)randomDesicion)).x).getPeticiones().add(nodo);
                        ac.getCelda(nodo.y, nodo.x).getAgente().setDestino((Point)this.getVecinosSalida().get(randomDesicion));
                    } else if (this.getVecinosVaciosMejorDistancia().size() > 0) {
                        
                        
                         //CRISTIAN 18/04/2018 CODIGO PARA MEJORAR LA NAVEGACION. SE PROBO Y AUN TIENE PROBLEMAS
                        /*int anterior=100,actual=10,vecino=0;
                        for (i = 0; i < this.getVecinosVaciosMejorDistancia().size(); i++) {
                            p =(Point) this.getVecinosVaciosMejorDistancia().get(i);
                            actual = Math.abs(nodo.x - p.x) + Math.abs(nodo.y - p.y);
                            if(actual<anterior){
                                vecino=i;
                                anterior=actual;
                            }
                        }
                        ac.getCelda(((Point)this.getVecinosVaciosMejorDistancia().get(vecino)).y, ((Point)this.getVecinosVaciosMejorDistancia().get(vecino)).x).getPeticiones().add(nodo);
                        ac.getCelda(nodo.y, nodo.x).getAgente().setDestino((Point)this.getVecinosVaciosMejorDistancia().get(vecino));*/
                        
                        int randomDesicion = Utilidades.getRandom((int)this.getVecinosVaciosMejorDistancia().size());
                        ac.getCelda(((Point)this.getVecinosVaciosMejorDistancia().get((int)randomDesicion)).y, ((Point)this.getVecinosVaciosMejorDistancia().get((int)randomDesicion)).x).getPeticiones().add(nodo);
                        ac.getCelda(nodo.y, nodo.x).getAgente().setDestino((Point)this.getVecinosVaciosMejorDistancia().get(randomDesicion));
                    
                    } else if (this.getVecinosOcupadosMejorDistancia().size() > 0){
                        int randomDesicion = Utilidades.getRandom((int)this.getVecinosOcupadosMejorDistancia().size());
                        ac.getCelda(((Point)this.getVecinosOcupadosMejorDistancia().get((int)randomDesicion)).y, ((Point)this.getVecinosOcupadosMejorDistancia().get((int)randomDesicion)).x).getPeticiones().add(nodo);
                        ac.getCelda(nodo.y, nodo.x).getAgente().setDestino((Point)this.getVecinosOcupadosMejorDistancia().get(randomDesicion));
                    }else{
                        ac.getCelda(nodo.y, nodo.x).getAgente().setRespuesta(2);
                    }
                } else {
                    if (this.getVecinosVaciosIgualDistancia().size() > 0) {
                        int randomDesicion = Utilidades.getRandom((int)this.getVecinosVaciosIgualDistancia().size());
                        ac.getCelda(((Point)this.getVecinosVaciosIgualDistancia().get((int)randomDesicion)).y, ((Point)this.getVecinosVaciosIgualDistancia().get((int)randomDesicion)).x).getPeticiones().add(nodo);
                        ac.getCelda(nodo.y, nodo.x).getAgente().setDestino((Point)this.getVecinosVaciosIgualDistancia().get(randomDesicion));
                    } else if (this.getVecinosOcupadosIgualDistancia().size() > 0) {
                        int randomDesicion = Utilidades.getRandom((int)this.getVecinosOcupadosIgualDistancia().size());
                        ac.getCelda(((Point)this.getVecinosOcupadosIgualDistancia().get((int)randomDesicion)).y, ((Point)this.getVecinosOcupadosIgualDistancia().get((int)randomDesicion)).x).getPeticiones().add(nodo);
                        ac.getCelda(nodo.y, nodo.x).getAgente().setDestino((Point)this.getVecinosOcupadosIgualDistancia().get(randomDesicion));
                    } else {
                        ac.getCelda(nodo.y, nodo.x).getAgente().setRespuesta(2);
                    }
                    if (ac.getCelda(nodo.y, nodo.x).getAgente().getTiempoNoActualizacion() > 5) {
                        ac.getCelda(nodo.y, nodo.x).getAgente().setTiempoNoActualizacion(0);
                    }
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

