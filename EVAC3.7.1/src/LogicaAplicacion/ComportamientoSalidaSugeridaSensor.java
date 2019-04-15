package LogicaAplicacion;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Random;

//Daniel Larregola 16-03-2019
public class ComportamientoSalidaSugeridaSensor extends Comportamiento {

    private int puertaMasCercana(Point nodo, AutomataCelular ac) { //MODIFICADO PARA ENCONTAR LA SALIDA CON MENOR DISTANCIA
        int puerta = 0;                                            //AHORA YA NO EXISTE EL CAMPO DISTANCIA SALIDA QUE GUARDABA
        Double distancia = 5000.0;                                    //LA DISTANCIA SOLO HACIA LA SALIDA MAS CERCANA (05-10-2015)
        LinkedList distanciasSalidas = ac.getCelda(nodo.y, nodo.x).getDistanciasSalidas();
        ListIterator iterador = distanciasSalidas.listIterator();
        int comparacion;
        while (iterador.hasNext()) {
            DistanciaSalida ds = (DistanciaSalida)iterador.next();

            comparacion=ds.getDistancia().compareTo(-1.0);

            if (comparacion!=0 && ds.getDistancia() < distancia ){
                distancia = ds.getDistancia();
                puerta = ds.getSalida();
            }
        }
        return puerta;
    }

    private int salidaSugeridaSensor(Point nodo, AutomataCelular ac) {
        int salida = ac.getCelda(nodo.y, nodo.x).getAgente().getSalidaSugeridaSensor().getSalida();
        if(salida == -1){
            salida = puertaMasCercana(nodo,ac);
        }
        return salida;
    }

    @Override
    public boolean ejecutarComportamiento(AutomataCelular ac, AutomataCelular ac2, AutomataCelular acAmbiente, Point nodo) {
        //Modificar comportamiento por el momento me sirve este.
        this.borrarVecinos();
        this.setVecinos(Utilidades.chequearMoore((Point)nodo, (AutomataCelular)ac));
        //SI VECINOS=0 EL AGENTE ESTA ENCERRADO PORQUE NO ENCUENTRA VECINOS DONDE MOVERSE (11-10-2015)
        if ( this.getVecinos().size()>0 && (ac.getCelda(nodo.y, nodo.x).getAgente().getContadorVelocidad() < ac.getCelda(nodo.y, nodo.x).getAgente().getVelocidad()) ) {
            Point p;
            int i;
            try {

                if((this.salidaSugeridaSensor(nodo, ac)-1 ) ==-1){
                    //System.out.println("ENCERRADO CON POSIBLE MOVIMIENTO...  CELDA (" + nodo.x + "," + nodo.y + ")");
                    //EL INDIVIDUO NO SE PODRA MOVER PORQUE ESTA ENCERRADO DEBE SER MARCADO COMO CAIDO
                    //ENCERRADO CON CELDAS LIBRES A SU ALREDEDOR 12-10-2015
                    return false;
                }
                else{

                    ac.getCelda(nodo.y, nodo.x).getAgente().setSalidaElegida(this.salidaSugeridaSensor(nodo, ac));
                    ((Contador)ThreadSimulacion.intenciones.get( this.salidaSugeridaSensor(nodo, ac)-1 )).incrementarContador();

                }
            }
            catch (Exception ex) {
                System.out.println(this.salidaSugeridaSensor(nodo, ac));
                System.out.println("CHEKING...  CELDA (" + nodo.x + "," + nodo.y + ")");
                ex.printStackTrace();
            }

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
                                this.getVecinosOcupadosMejorDistancia().add(p);
                            }
                            //AQUI DEBO COMPARAR DE ESTA FORMA PORQUE SINO PUEDO TENER ERRORES POR COMPRACION CON OPERADORES
                            //NUMERICOS CON DOUBLE. NO DA EL RESULTADO ESPERADO SI SE HAC CON OPERADORES. SE DEBE HACER ASI
                            //O EN SU DEFECTO CON EL METODO CompareTo DE DOUBLE 12-10-2015
                            else if(Objects.equals(Utilidades.obtenerDistanciaSalida(acAmbiente.getCelda(p.y, p.x), ac.getCelda(nodo.y, nodo.x).getAgente().getSalidaElegida()), Utilidades.obtenerDistanciaSalida(ac.getCelda(nodo.y, nodo.x), ac.getCelda(nodo.y, nodo.x).getAgente().getSalidaElegida()))){
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

                    //CRISTIAN 18/04/2018 CODIGO PARA MEJORAR LA NAVEGACION : PROBADO NUEVAMENTE EL 06/06/2018 NO ANDUVO
                    //TODOS LOS INDIVUDOS SE VAN HACIA UN LADO DEBIDO AL ORDEN DE ELECCION DE LA CELDA
                    //LA POSIBLE SOLUCION ES TIRAR UN RAMDOM CON LAS CELDAS MENORES O USAR EL VECTOR
                    //LO DEJAMOS COMO ANTES
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
       // ComportamientoSalidaMejorTiempo comp = new ComportamientoSalidaMejorTiempo();
        //return comp.ejecutarComportamiento(ac,ac2,acAmbiente,nodo);
}

