
package LogicaAplicacion;

import java.awt.Point;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Objects;

public class ComportamientoSalidaMejorTiempo extends Comportamiento {
    private static final int CANTIDADDESICIONES=5; //CRISTIAN 28-03-2016
    
    private double buscarDistanciaSalida(AutomataCelular ac, Point nodo, int salidaBuscada) {
        double distancia = -1.0;
        ListIterator iterador = ac.getCelda(nodo.y, nodo.x).getDistanciasSalidas().listIterator();
        while (iterador.hasNext()) {
            DistanciaSalida ds = (DistanciaSalida)iterador.next();
            if (ds.getSalida() != salidaBuscada) continue;
            distancia = ds.getDistancia();
        }
        return distancia;
    }

    private int tamañoPuertaBuscada(int salidaBuscada) {
        int factor = -1;
        ListIterator iterador = Proyecto.getProyecto().getSalidas().listIterator();
        while (iterador.hasNext()) {
            Salida s = (Salida)iterador.next();
            if (s.getNumeroSalida() != salidaBuscada) continue;
            factor = s.getNodos().size();
        }
        return factor;
    }
    
    //**********************************************************************************************************
    //*** ESTE COMPORTAMIENTO PUEDE SER SIMPLIFICADO SI CADA VEZ QUE UN INDIVIDUO ESTA EN UNA CELDA CON FUEGO***
    //*** DIRECTAMENTO LO DOY BAJA. ESTO CAMBIARIA EL CODIGO DENTRO DEL METODO EJECUTAR COMPORTAMIENTO  SE   ***
    //*** PODRIA AGREGAR UNA CONDICION DONDE SE CONSULTE LA SITUACION DE LA CELDA DONDE SE ENCUENTA EL AGENTE***
    //*** SI LA CELDA TIENE FUEGO SE RETORNA false CON ESTO EL AGENTE MORIRIA. ESTO SIMPLICARIA LAS OTRAS    ***
    //*** CONDICIONES PARA CONOCER LA SITUACION EN LA QUE SE ENCUENTRA EL AGENTE Y POR LO TANTO SIMPLIFICARIA***
    //*** EL CODIGO DEL COMPORTAMIENTO. CRISTIAN 12-04-2016
    //**********************************************************************************************************
    
    
    
    @Override
    public boolean ejecutarComportamiento(AutomataCelular ac, AutomataCelular ac2, AutomataCelular acAmbiente, Point nodo) {
        //ES NECSARIO CONTOLAR A FUTURO ESTA LINEA DE CODIGO. PUESTO QUE LA CONDICION QUE CHEQUEA ESTE 
        //VALOR CUANDO EL AMBIENTE SEA MAS GANDE PUEDE TRAER PROBLEMAS. 
        //POR LO TANTO SE DEBE REARMAR EL VALOR INICIAL Y EL IF QUE CHEQUEA ESTA CONDICION\
        //CRISTIAN 17-11-2015
        double resultadoEvaluacion = 100000000000.0;
        int salidaElegida = -3;
        //CRISTIAN 28-03-2016
        int tiempoEntreDesiciones=Utilidades.getRandom((int)(((Proyecto.getProyecto().getTamañox()/0.4)+(Proyecto.getProyecto().getTamañoy()/0.4))/2));
        
        //int tiempoEntreDesiciones=60*3;
        int factorDesalojo = 0;
        double tpoLlegar = 0;
        int tamañoPuerta = 1;
        int intencionesPuerta = 1;
        int randomDesicion = 0;
        double distancia;
        Point punto;
        this.borrarVecinos();
        this.setVecinos(Utilidades.chequearMoore((Point)nodo, (AutomataCelular)ac));
        ListIterator iteradorDistancias = ac.getCelda(nodo.y, nodo.x).getDistanciasSalidas().listIterator();
        int valorComparacion=0;
        int moduloEleccion=0;
        
        
        //ANTES COMENZAR A PREGUNTAR LA SITUACION DEL AGENTE ME FIJO SI EL MISMO NO ESTA ENCERRADO
        //ES DECIR PUEDE QUE """TODAS, ABSOLUTAMENTE LAS SALDIAS""" ESTEN BLOQUEDAS POR FUEGO O EL AGENTE QUEDAR ENCERRADO
        //POR MULTIPLES FRENTES DE FUEGO (EN CUADRADO DE FUEGO), PERO CON CELDAS LIBRES A LAS QUE PUEDE DESPLAZARSE Y QUE NO SE 
        //PRENDERAN FUEGO. EN ESTE CASO EL INDIVIDUO ESTA ENCERRADO POR FUEGO Y DEBE MORIR. CRISTIAN 12-04-2016
        if(Utilidades.estaEncerrado(nodo, ac)){
            return false;
        }
        
        //SI AUN ME PUEDO MOVER PORQUE MI VELOCIDAD ME LO PERMITE Y TENGO VECINOS
        if (this.getVecinos().size()>0 && ac.getCelda(nodo.y, nodo.x).getAgente().getContadorVelocidad() < ac.getCelda(nodo.y, nodo.x).getAgente().getVelocidad()) {
            
            //------------SELECCIONO LA SALIDA POR LA CUAL EVACUAR-------------------------------------------------------------
            //SI AUN MI TIEMPO PARA REALIZAR UNA INFERENCIA NO HA TERMINADO O SI YA TODAS LAS DESICIONES QUE PODIA TOMAR 
            //ME QUEDO CON LA SALIDA QUE YA TENIA SELECCIONADA DESDE ANTES
 
            //SI QUEDE CON UNA SALIDA CONFIGURADA QUE SE BLOQUEO Y ADEMAS NO PUEDO REALIZAR MAS INFERENCIAS
            //CAMBIO DE COMPORTAMIENTO A SALIDA MAS CERCANA
            valorComparacion= ((Double)buscarDistanciaSalida(ac,nodo,ac.getCelda(nodo.y,nodo.x).getAgente().getSalidaElegida())).compareTo(-1.0);

            if( (valorComparacion==0) && (ac.getCelda(nodo.y, nodo.x).getAgente().getDesiciones() > CANTIDADDESICIONES)){
                ac.getCelda(nodo.y, nodo.x).getAgente().setTipo(0);
                ac.getCelda(nodo.y, nodo.x).getAgente().setRespuesta(2);
                ac.getCelda(nodo.y, nodo.x).getAgente().setComportamiento(new ComportamientoSalidaMasCercana());
                return true;
            }
            
            //SI LA PUERTA SE BLOQUEO Y ESTOY EN UNA CELDA CON POSIBILIDAD DE MOVIMIENTO DEBO CAMBAIR LA DESICION DE MI PUERTA
            //PARA LOGAR ESTO SETEO EL TIEMPO DE INFERENCIA A 0.
            //CRISTIAN 24/11/2015
            /*int valorComparacion= ((Double)buscarDistanciaSalida(ac,nodo,ac.getCelda(nodo.y,nodo.x).getAgente().getSalidaElegida())).compareTo(-1.0);*/
            if( ( valorComparacion==0) && (ac.getCelda(nodo.y, nodo.x).getNivelFuego()<=0) && (ac.getCelda(nodo.y, nodo.x).getAgente().getTiempoInferencia()>0) ){
                if(ac.getCelda(nodo.y, nodo.x).getAgente().getDesiciones() >= CANTIDADDESICIONES){
                    ac.getCelda(nodo.y, nodo.x).getAgente().setDesiciones(4);
                }
                ac.getCelda(nodo.y, nodo.x).getAgente().setTiempoInferencia(0);
                //ac.getCelda(nodo.y, nodo.x).getAgente().setDesiciones(ac.getCelda(nodo.y, nodo.x).getAgente().getDesiciones()-1);
            }
            
            
            
            //SI DEBO REVISAR LA PUERTA A LA QUE ME DIRIJO (INFERENCIA==0) Y ADEMAS JUSTO EN ESTE MOMENTO ESTOY PARADO EN UNA CELDA CON FUEGO
            //NO PUEDO BUSCAR UNA NUEVA SALIDA, POR LO TANTO MANTENGO LA QUE YA TENGO.
            //O SI MI TIEMPO ENTRE INFERENCIAS NO HA EXPIRADO O SI YA TOME LAS DESICIONES QUE PODIA MANTENGO LA SALIDA CONFIGURADA ANTERIORMENTE
            if ((ac.getCelda(nodo.y, nodo.x).getAgente().getTiempoInferencia()==0 && ac.getCelda(nodo.y, nodo.x).getNivelFuego()>0) ||
                    (ac.getCelda(nodo.y, nodo.x).getAgente().getTiempoInferencia() > 0) || 
                    (ac.getCelda(nodo.y, nodo.x).getAgente().getDesiciones() > CANTIDADDESICIONES) ) {
                
                 
                //ESTOY EN UNA CELDA CON FUEGO, YA NO HAY TIEMPO PARA INFERIR, ES DECIR HE SIDO ALCANZADO POR EL FUEGO
                //Y ADEMAS ME DIRIJO A UNA SALIDA BLOQUEADA, ENTONCE MARCO EL INDIVIDUO COMO MUERTO. 
                //PORQ NO PUEDO BUSCAR DISTANCIAS A UNA SALIDA QUE YA SE BLOQUEO
                //SIN EMBARGO ESTO A FUTURO 
                //SE PUEDE MEJORAR E INTENTAR QUE EL ïNDIVIDUO SALGA DE ESTA SITUACION CRISTIAN 12-04-2016
                if( (valorComparacion==0) && (ac.getCelda(nodo.y, nodo.x).getNivelFuego()>0) && (ac.getCelda(nodo.y, nodo.x).getAgente().getTiempoInferencia()==0)){
                    return false;
                }
                
                ac.getCelda(nodo.y, nodo.x).getAgente().setTiempoInferencia(ac.getCelda(nodo.y, nodo.x).getAgente().getTiempoInferencia() - 1);
                salidaElegida = ac.getCelda(nodo.y, nodo.x).getAgente().getSalidaElegida();
            }
            else {//SINO ELIJO UNA NUEVA SALIDA
               
                    while (iteradorDistancias.hasNext()){ //POR CADA SALIDA DE LA CELDA DONDE ESTA EL AGENTE
                        DistanciaSalida distanciaSalida = (DistanciaSalida)iteradorDistancias.next();
                        int comparacion=distanciaSalida.getDistancia().compareTo(-1.0);
                        if (comparacion==0){ 
                            distancia=10000000.0;
                        }
                        else{
                            distancia=distanciaSalida.getDistancia();
                        }

                        tamañoPuerta = this.tamañoPuertaBuscada(distanciaSalida.getSalida());
                        factorDesalojo = ((Contador)ThreadSimulacion.intencionesAnteriores.get(distanciaSalida.getSalida()-1)).getValor() / tamañoPuerta + 1;
                        tpoLlegar = (distanciaSalida.getDistancia() / ac.getCelda(nodo.y, nodo.x).getAgente().getVelocidad()) + 1;
                        intencionesPuerta = ((Contador)ThreadSimulacion.intencionesAnteriores.get(distanciaSalida.getSalida()-1)).getValor() + 1;
                        if (tpoLlegar > factorDesalojo) {//SI EL TIEMPO QUE TARDO EN LLEGAR ES MAYOR QUE EL TIEMPO QUE TARDA EN DESALOJARSE LA PUERTA
                            intencionesPuerta = 1; //MARCO MI INTENCION DIRECTAMENTE PARA DESALOJAR POR ESA PUERTA
                        }
                        if(resultadoEvaluacion > factorDesalojo * tpoLlegar * distancia * intencionesPuerta){
                            salidaElegida = distanciaSalida.getSalida();
                            resultadoEvaluacion = factorDesalojo * tpoLlegar * distancia * intencionesPuerta;
                        }
                    }

                if(salidaElegida==-3){
                    System.out.print("-----> NODO: (" + nodo.x + "," + nodo.y + ")  ESTADO: "  + ac.getCelda(nodo.y,nodo.x).getEstado() + "   TIPO:" + ac.getCelda(nodo.y,nodo.x).getAgente().getTipo() +   "   FUEGO: " + ac.getCelda(nodo.y,nodo.x).getNivelFuego() + "   ");
                    System.out.print("SALIDA: " + ac.getCelda(nodo.y,nodo.x).getAgente().getSalidaElegida() + " DESTINO: (" + ac.getCelda(nodo.y,nodo.x).getAgente().getDestino().x + "," + ac.getCelda(nodo.y,nodo.x).getAgente().getDestino().y + ")");
                    System.out.println("---->    DECISIONES: " + ac.getCelda(nodo.y, nodo.x).getAgente().getDesiciones() + "   INFERENCIA: " + ac.getCelda(nodo.y, nodo.x).getAgente().getTiempoInferencia());
                    
                    //ESTOY EN UNA CELDA CON FUEGO, ME QUEDA TIEMPO PARA INFERIR, ES DECIR HE SIDO ALCANZADO POR EL FUEGO
                    //Y ME DIRIJO A UNA SALIDA BLOQUEADA, ENTONCE MARCO EL INDIVIDUO COMO MUERTO. SIN EMBARGO ESTO A FUTURO 
                    //SE PUEDE MEJORAR E INTENTAR QUE EL ïNDIVIDUO SALGA DE ESTA SITUACION CRISTIAN 12-04-2016
                    if( (valorComparacion==0) && (ac.getCelda(nodo.y, nodo.x).getNivelFuego()>0) && (ac.getCelda(nodo.y, nodo.x).getAgente().getTiempoInferencia()>0)){
                        return false;
                    }
                }
                
                //TIEMPO ENTRE DESICIONES ES IGUAL A EL PROMEDIO ENTRE EL ANCHO Y EL ALTO y TIENE EN CUENTA LA VEL. DEL INDIVIDUO
                
                tiempoEntreDesiciones=Utilidades.getRandom(200)+50;
                
                //tiempoEntreDesiciones= (int)(((Proyecto.getProyecto().getTamañox()/0.4)+(Proyecto.getProyecto().getTamañoy()/0.4))/2);
                //tiempoEntreDesiciones=((int)tiempoEntreDesiciones/ac.getCelda(nodo.y, nodo.x).getAgente().getVelocidad())+1;
                
                //tiempoEntreDesiciones= (int)(((Proyecto.getProyecto().getTamañox()/0.4)+(Proyecto.getProyecto().getTamañoy()/0.4))/16);

                //tiempoEntreDesiciones=Proyecto.getProyecto().getTamañox()*Proyecto.getProyecto().getTamañoy();
                //tiempoEntreDesiciones=((Double)Math.sqrt(Proyecto.getProyecto().getTamañox()*Proyecto.getProyecto().getTamañoy())).intValue();
                ac.getCelda(nodo.y, nodo.x).getAgente().setDesiciones(ac.getCelda(nodo.y, nodo.x).getAgente().getDesiciones() + 1);
                ac.getCelda(nodo.y, nodo.x).getAgente().setTiempoInferencia(  Utilidades.getRandom(tiempoEntreDesiciones) + ((int)(tiempoEntreDesiciones/2))    );
                ac.getCelda(nodo.y, nodo.x).getAgente().setSalidaElegida(salidaElegida);
            }
            
            try{
                ((Contador)ThreadSimulacion.intenciones.get(salidaElegida-1)).incrementarContador();
            }
            catch(Exception e){
                System.out.println("ERROR POSIBLEMENTE PROVOCADO PORQUE LA SALIDA SE BLOQUEO");
                
                if( (buscarDistanciaSalida( ac,nodo,ac.getCelda(nodo.y,nodo.x).getAgente().getSalidaElegida() ) == -1.0) ){
                    System.out.println("COMPARACION == -1.0");
                }
                
                if( ((Double)buscarDistanciaSalida( ac,nodo,ac.getCelda(nodo.y,nodo.x).getAgente().getSalidaElegida())).compareTo(-1.0) == 0  ){
                    System.out.println("COMPARACION CompareTo");
                }
                
                imprimirEstado(nodo, ac);
            }
            //------------------------FIN SELECCIONO LA SALIDA POR LA QUE EVACUAR ------------------------------------------
            
            //OBTENGO LOS VECINOS Y LOS CLASIFICOS PARA SABER DONDE DEBO MOVERME
            Point p;
            int i;
            for (i = 0; i < this.getVecinos().size(); ++i){
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
                            else{
                                this.getVecinosVaciosPeorDistancia().add(p);
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
                                else{
                                    this.getVecinosOcupadosPeorDistancia().add(p);
                                }
                            }
                            break;
                        }
                }
            }
            //FIN OBTENGO LOS VECINOS Y LOS CLASIFICOS PARA SABER DONDE DEBO MOVERME
            
            
            //---------------------SELECCIONO EL VECINO DONDE MOVERME-----------------------------------------------------
            
            //OBTENGO EL MODULO PARA SABER SI DEBO ELEGIR UNA CELDA PARA MOVERME HACIA LA PUERTA ELEGIDA O SI DEBO MOVERME 
            //ALGUNA CELDA HACIA EL COSTADO PARA CONSEGUIR UNA MEJOR POSICION. ADEMAS PUEDO OPTAR POR MOVERME A UNA CELDA DE MANERA 
            //ALEATORIA PARA CONSEGUIR UNA POSICION DESDE LA CUAL MOVERME HACIA LA PUERTA SELECCIONADA NUEVAMENTE.
            //0-4 INTENTO HACIA LA SALIDA
            //5-7 INTENTO HACIA EL COSTADO
            //8-9 INTENTO ALEATORIO
            //CRISTIAN 24-11-2015
            
            moduloEleccion=ac.getCelda(nodo.y, nodo.x).getAgente().getTiempoNoActualizacion()%10;
            
            if(moduloEleccion <5){//INTENTO HACIA LA SALIDA
                if (this.getVecinosSalida().size() > 0) {
                    randomDesicion = Utilidades.getRandom((int)this.getVecinosSalida().size());
                    ac.getCelda(((Point)this.getVecinosSalida().get((int)randomDesicion)).y, ((Point)this.getVecinosSalida().get((int)randomDesicion)).x).getPeticiones().add(nodo);
                    ac.getCelda(nodo.y, nodo.x).getAgente().setDestino((Point)this.getVecinosSalida().get(randomDesicion));
                } else if (this.getVecinosVaciosMejorDistancia().size() > 0) {
                    
                    
                     //CRISTIAN 18/04/2018 CODIGO PARA MEJORAR LA NAVEGACION SE PROBO Y AUN TIENE PROBLEMAS
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
                    
                    
                    randomDesicion = Utilidades.getRandom((int)this.getVecinosVaciosMejorDistancia().size());
                    ac.getCelda(((Point)this.getVecinosVaciosMejorDistancia().get((int)randomDesicion)).y, ((Point)this.getVecinosVaciosMejorDistancia().get((int)randomDesicion)).x).getPeticiones().add(nodo);
                    ac.getCelda(nodo.y, nodo.x).getAgente().setDestino((Point)this.getVecinosVaciosMejorDistancia().get(randomDesicion));
                } else if (this.getVecinosOcupadosMejorDistancia().size() > 0){
                    randomDesicion = Utilidades.getRandom((int)this.getVecinosOcupadosMejorDistancia().size());
                    ac.getCelda(((Point)this.getVecinosOcupadosMejorDistancia().get((int)randomDesicion)).y, ((Point)this.getVecinosOcupadosMejorDistancia().get((int)randomDesicion)).x).getPeticiones().add(nodo);
                    ac.getCelda(nodo.y, nodo.x).getAgente().setDestino((Point)this.getVecinosOcupadosMejorDistancia().get(randomDesicion));
                }else{
                    ac.getCelda(nodo.y, nodo.x).getAgente().setRespuesta(2);
                }
            }
            else if(moduloEleccion>=5 && moduloEleccion<8){ //INTENTO HACIA LOS COSTADOS
                if (this.getVecinosVaciosIgualDistancia().size() > 0) {
                    randomDesicion = Utilidades.getRandom((int)this.getVecinosVaciosIgualDistancia().size());
                    ac.getCelda(((Point)this.getVecinosVaciosIgualDistancia().get((int)randomDesicion)).y, ((Point)this.getVecinosVaciosIgualDistancia().get((int)randomDesicion)).x).getPeticiones().add(nodo);
                    ac.getCelda(nodo.y, nodo.x).getAgente().setDestino((Point)this.getVecinosVaciosIgualDistancia().get(randomDesicion));
                } else if (this.getVecinosOcupadosIgualDistancia().size() > 0) {
                    randomDesicion = Utilidades.getRandom((int)this.getVecinosOcupadosIgualDistancia().size());
                    ac.getCelda(((Point)this.getVecinosOcupadosIgualDistancia().get((int)randomDesicion)).y, ((Point)this.getVecinosOcupadosIgualDistancia().get((int)randomDesicion)).x).getPeticiones().add(nodo);
                    ac.getCelda(nodo.y, nodo.x).getAgente().setDestino((Point)this.getVecinosOcupadosIgualDistancia().get(randomDesicion));
                } else {
                    ac.getCelda(nodo.y, nodo.x).getAgente().setRespuesta(2);
                }
            }
            else{//INTENTO ALETORIO
                //System.out.println("REPLICO");
                //System.out.print("NODO: (" + nodo.x + "," + nodo.y + ")  ESTADO: "  + ac.getCelda(nodo.y,nodo.x).getEstado() + "   TIPO:" + ac.getCelda(nodo.y,nodo.x).getAgente().getTipo() +   "   FUEGO: " + ac.getCelda(nodo.y,nodo.x).getNivelFuego() + "   ");
                //System.out.print("SALIDA: " + ac.getCelda(nodo.y,nodo.x).getAgente().getSalidaElegida() + " DESTINO: (" + ac.getCelda(nodo.y,nodo.x).getAgente().getDestino().x + "," + ac.getCelda(nodo.y,nodo.x).getAgente().getDestino().y + ")");
                   if(this.getVecinosVaciosPeorDistancia().size()>0){
                        randomDesicion = Utilidades.getRandom((int)this.getVecinosVaciosPeorDistancia().size());
                        ac.getCelda(((Point)this.getVecinosVaciosPeorDistancia().get((int)randomDesicion)).y, ((Point)this.getVecinosVaciosPeorDistancia().get((int)randomDesicion)).x).getPeticiones().add(nodo);
                        ac.getCelda(nodo.y, nodo.x).getAgente().setDestino((Point)this.getVecinosVaciosPeorDistancia().get(randomDesicion));
                   }else if(this.getVecinosOcupadosPeorDistancia().size()>0){
                        randomDesicion = Utilidades.getRandom((int)this.getVecinosOcupadosPeorDistancia().size());
                        ac.getCelda(((Point)this.getVecinosOcupadosPeorDistancia().get((int)randomDesicion)).y, ((Point)this.getVecinosOcupadosPeorDistancia().get((int)randomDesicion)).x).getPeticiones().add(nodo);
                        ac.getCelda(nodo.y, nodo.x).getAgente().setDestino((Point)this.getVecinosOcupadosPeorDistancia().get(randomDesicion));
                   }else{
                       ac.getCelda(nodo.y, nodo.x).getAgente().setRespuesta(2);
                   }
            }
            
            return true;
            
            
            /*if (ac.getCelda(nodo.y, nodo.x).getAgente().getTiempoNoActualizacion() < 3){
                if (this.getVecinosSalida().size() > 0) {
                    randomDesicion = Utilidades.getRandom((int)this.getVecinosSalida().size());
                    ac.getCelda(((Point)this.getVecinosSalida().get((int)randomDesicion)).y, ((Point)this.getVecinosSalida().get((int)randomDesicion)).x).getPeticiones().add(nodo);
                    ac.getCelda(nodo.y, nodo.x).getAgente().setDestino((Point)this.getVecinosSalida().get(randomDesicion));
                } else if (this.getVecinosVaciosMejorDistancia().size() > 0) {
                    randomDesicion = Utilidades.getRandom((int)this.getVecinosVaciosMejorDistancia().size());
                    ac.getCelda(((Point)this.getVecinosVaciosMejorDistancia().get((int)randomDesicion)).y, ((Point)this.getVecinosVaciosMejorDistancia().get((int)randomDesicion)).x).getPeticiones().add(nodo);
                    ac.getCelda(nodo.y, nodo.x).getAgente().setDestino((Point)this.getVecinosVaciosMejorDistancia().get(randomDesicion));
                } else if (this.getVecinosOcupadosMejorDistancia().size() > 0){
                    randomDesicion = Utilidades.getRandom((int)this.getVecinosOcupadosMejorDistancia().size());
                    ac.getCelda(((Point)this.getVecinosOcupadosMejorDistancia().get((int)randomDesicion)).y, ((Point)this.getVecinosOcupadosMejorDistancia().get((int)randomDesicion)).x).getPeticiones().add(nodo);
                    ac.getCelda(nodo.y, nodo.x).getAgente().setDestino((Point)this.getVecinosOcupadosMejorDistancia().get(randomDesicion));
                }else{
                    ac.getCelda(nodo.y, nodo.x).getAgente().setRespuesta(2);
                }
            } else {
                if (this.getVecinosVaciosIgualDistancia().size() > 0) {
                    randomDesicion = Utilidades.getRandom((int)this.getVecinosVaciosIgualDistancia().size());
                    ac.getCelda(((Point)this.getVecinosVaciosIgualDistancia().get((int)randomDesicion)).y, ((Point)this.getVecinosVaciosIgualDistancia().get((int)randomDesicion)).x).getPeticiones().add(nodo);
                    ac.getCelda(nodo.y, nodo.x).getAgente().setDestino((Point)this.getVecinosVaciosIgualDistancia().get(randomDesicion));
                } else if (this.getVecinosOcupadosIgualDistancia().size() > 0) {
                    randomDesicion = Utilidades.getRandom((int)this.getVecinosOcupadosIgualDistancia().size());
                    ac.getCelda(((Point)this.getVecinosOcupadosIgualDistancia().get((int)randomDesicion)).y, ((Point)this.getVecinosOcupadosIgualDistancia().get((int)randomDesicion)).x).getPeticiones().add(nodo);
                    ac.getCelda(nodo.y, nodo.x).getAgente().setDestino((Point)this.getVecinosOcupadosIgualDistancia().get(randomDesicion));
                } else {
                    ac.getCelda(nodo.y, nodo.x).getAgente().setRespuesta(2);
                }
                if (ac.getCelda(nodo.y, nodo.x).getAgente().getTiempoNoActualizacion() > 5) {
                    
                    //ac.getCelda(nodo.y, nodo.x).getAgente().setTipo(0);
                    //ac.getCelda(nodo.y, nodo.x).getAgente().setRespuesta(2);
                    //ac.getCelda(nodo.y, nodo.x).getAgente().setComportamiento(new ComportamientoSalidaMasCercana());
                    ac.getCelda(nodo.y, nodo.x).getAgente().setTiempoNoActualizacion(0);
                    return true;
                }
            }*/
            
            //--------------------FIN SELECCIONO EL VECINO DONDE MOVERME-----------------------------------------------------
            
        } else {
            if(this.getVecinos().size()==0){//EL AGENTE ESTA ENCERRADO Y DEBE EN ESTE CASO MORIR!!!
                //System.out.println("ENTREEEEEEW");
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

    private void imprimirEstado(Point nodo, AutomataCelular ac){
        System.out.print("NODO: (" + nodo.x + "," + nodo.y + ")  ESTADO: "  + ac.getCelda(nodo.y,nodo.x).getEstado() + "   TIPO:" + ac.getCelda(nodo.y,nodo.x).getAgente().getTipo() +   "   FUEGO: " + ac.getCelda(nodo.y,nodo.x).getNivelFuego() + "   ");
        System.out.print("SALIDA: " + ac.getCelda(nodo.y,nodo.x).getAgente().getSalidaElegida() + " DESTINO: (" + ac.getCelda(nodo.y,nodo.x).getAgente().getDestino().x + "," + ac.getCelda(nodo.y,nodo.x).getAgente().getDestino().y + ")");
        System.out.println("    DECISIONES: " + ac.getCelda(nodo.y, nodo.x).getAgente().getDesiciones() + "   INFERENCIA: " + ac.getCelda(nodo.y, nodo.x).getAgente().getTiempoInferencia());
        Point vecino;
        LinkedList vecinos = Utilidades.obtenerVecindarioCompleto(nodo, ac);
        ListIterator it = vecinos.listIterator();
        while(it.hasNext()){
            vecino=(Point)it.next();
            System.out.println("NODO: (" + vecino.x + "," + vecino.y + ")  ESTADO: "  + ac.getCelda(vecino.y,vecino.x).getEstado() + "   FUEGO: " + ac.getCelda(vecino.y,vecino.x).getNivelFuego());
        }
    }
}

