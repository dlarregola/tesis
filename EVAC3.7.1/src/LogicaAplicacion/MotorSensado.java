package LogicaAplicacion;

import java.util.*;

public class MotorSensado {


 public static void inicializarFactorDesalojo(ThreadSimulacion thread){

        Iterator<Salida> it = Proyecto.getProyecto().getSalidas().iterator();
        int totalAgentes = Proyecto.getProyecto().getCantidadPersonas();
        int cantidadSalidas = Proyecto.getProyecto().getCantidadSalidas();

        while(it.hasNext()){
            Salida sal = it.next();

            Map<String,Integer> aux = new HashMap<String, Integer>();
            aux.put("factorDesalojo",sal.getNodos().size());
            //calculo la cantidad de agentes que tengo q mandar a cada puerta Inicialemnte
            int cantAgentesXPuerta = (int) Math.ceil((Double.valueOf(sal.getNodos().size()) * Double.valueOf(totalAgentes)/Double.valueOf(cantidadSalidas)));
            aux.put("cantidadAgentesPorPuerta",cantAgentesXPuerta);
            aux.put("agentesAsignados",0);

            thread.addFactorSalidas(sal.getNumeroSalida(),aux);

            thread.initMapaAgentePorSalida(sal.getNumeroSalida());
        }

 }

 public static void asignarSalidasSugeridasSensor(ThreadSimulacion thread){
        //Necesito mantener la intencion (cantidad de agentes que envio a cada puerta):
        //Guardo por cada puerta cuantos agente envio a dicha puerta
        calcularDensidadSensores(thread.getAc1());
        distribuirAgentesEntrePuertas(thread);
 }

    private static void calcularDensidadSensores(AutomataCelular ac1){
        Proyecto proy = Proyecto.getProyecto();
        LinkedList listSensores = proy.getListSensores();

        int potenciaSensor = proy.getPotenciaSensor();
        int cuadradosAncho = (int)((double)proy.getProyecto().getTamañox() / 0.4) + 1;
        int cuadradosAlto = (int)((double)proy.getProyecto().getTamañoy() / 0.4) + 1;

        Iterator<Sensor> it = listSensores.iterator();

        while (it.hasNext()){
            Sensor sensor = it.next();
            int x =  (int)sensor.getUbicacion().getX();
            int y =  (int)sensor.getUbicacion().getY();

            ac1.getCelda(y,x).getSensor().setSalidasPorDistancia(sortSalidas(ac1.getCelda(y,x).getDistanciasSalidas()));


            LinkedList vecinos = vecinosAgentesRadio(y,x,cuadradosAncho,cuadradosAlto,potenciaSensor,ac1);

            ac1.getCelda(y,x).getSensor().setListaAgentes(vecinos);


        }

    }

    private static  LinkedList vecinosAgentesRadio(int y,int x, int ancho, int alto, int radio, AutomataCelular ac1){
        LinkedList vecinos = new LinkedList() ;
        for(int i=(y-radio); i<=(y+radio) ;i++){
            if(i>=1 && i<alto) {
                for (int j = (x - radio); j <= (x + radio); j++) {

                    if (j >= 1 && j < ancho) {
                        int estado = ac1.getCelda(i, j).getEstado();
                        if (estado == 6) {
                            vecinos.add(ac1.getCelda(i, j).getAgente());
                        }

                    }
                }
            }
        }
        return vecinos;

    }

    private  static ArrayList<DistanciaSalida> sortSalidas (LinkedList salidas){
        ArrayList<DistanciaSalida>  puerta = new ArrayList<DistanciaSalida> (salidas);
        Collections.sort(puerta, new SortByDistancia());
        return puerta;
    }

    private static void distribuirAgentesEntrePuertas(ThreadSimulacion thread){
        Proyecto proy = Proyecto.getProyecto();
        Iterator<Sensor> it = proy.getListSensores().iterator();
        while(it.hasNext()){
            Sensor sensor = it.next();
            Iterator<Agente> itAgentes = sensor.getListaAgentes().iterator();
            while (itAgentes.hasNext()){
                Agente ag = itAgentes.next();
                //Si tengo q recalcular porque actualizo el fuego
                // Y la salida sugerida por el sensor que tenia previamente asignada se bloqueo elijo una nueva salida.
                if(ag.getSalidaSugeridaSensor().getSalida() != -1 ){
                    if(checkDistanciaSalida(ag.getSalidaSugeridaSensor().getSalida(),sensor.getSalidasPorDistancia())){
                        ag.setSalidaSugeridaSensor(new DistanciaSalida(-1,-1.0));
                    }
                }

                if(ag.getSalidaSugeridaSensor().getSalida() == -1 ) {
                    ag.setSalidaSugeridaSensor(asignarSalidaAgente(sensor.getSalidasPorDistancia(), ag.getId(),thread));
                }
            }

        }
    }


    private static boolean checkDistanciaSalida(int idSalida,ArrayList<DistanciaSalida> listSalidasSensor ){
        Iterator<DistanciaSalida> itSalidas = listSalidasSensor.iterator();
        while(itSalidas.hasNext()) {
            DistanciaSalida salida = itSalidas.next();
            if (salida.getSalida() == idSalida && salida.getDistancia().compareTo(-1.0) <= 0) {
                return true;
            }
        }


        return false;

    }


    private static DistanciaSalida asignarSalidaAgente(ArrayList<DistanciaSalida> listSalidasSensor,int idAgente, ThreadSimulacion thread){
        DistanciaSalida sSS = new DistanciaSalida(-1,5000.0);
        Iterator<DistanciaSalida> itSalidas = listSalidasSensor.iterator();
        boolean flagSalidasBloqueadas = false;
        int posibleSalida = -1;
        Double distPosibleSalida = 5000.0;
        while(itSalidas.hasNext()){
            DistanciaSalida salida = itSalidas.next();
            if(salida.getDistancia().compareTo(-1.0) > 0 &&
                    thread.getFactorSalidas().get(salida.getSalida()).get("agentesAsignados") < thread.getFactorSalidas().get(salida.getSalida()).get("cantidadAgentesPorPuerta")){

                thread.addPropertisFactorSalida(salida.getSalida(),"agentesAsignados",thread.getFactorSalidas().get(salida.getSalida()).get("agentesAsignados")+1);
                sSS.setSalida(salida.getSalida());
                sSS.setDistancia(salida.getDistancia());
                thread.addMapaAgentePorSalida(salida.getSalida(),idAgente);
                break;
            } else if(salida.getDistancia().compareTo(-1.0) > 0 &&
                    thread.getFactorSalidas().get(salida.getSalida()).get("agentesAsignados") >= thread.getFactorSalidas().get(salida.getSalida()).get("cantidadAgentesPorPuerta")){
                flagSalidasBloqueadas = true;
                posibleSalida = salida.getSalida();
                distPosibleSalida = salida.getDistancia();
            }
        }


        if(sSS.getSalida() == -1 && flagSalidasBloqueadas){
            thread.addPropertisFactorSalida(posibleSalida,"cantidadAgentesPorPuerta",thread.getFactorSalidas().get(posibleSalida).get("cantidadAgentesPorPuerta")+1);
            thread.addPropertisFactorSalida(posibleSalida,"agentesAsignados",thread.getFactorSalidas().get(posibleSalida).get("agentesAsignados")+1);

            thread.addMapaAgentePorSalida(posibleSalida,idAgente);
            sSS.setSalida(posibleSalida);
            sSS.setDistancia(distPosibleSalida);

        }


        return sSS;
    }

}
