package LogicaAplicacion;

import java.util.*;

public class MotorSensado {

    public static void inicializarFactorDesalojo(ThreadSimulacion thread){

        Iterator<Salida> it = Proyecto.getProyecto().getSalidas().iterator();
        int totalAgentes = Proyecto.getProyecto().getCantidadPersonas();
        int cantidadSalidas = Proyecto.getProyecto().getCantidadSalidas();
        thread.salidasDisponibles = Proyecto.getProyecto().getSalidas().size();
        thread.salidasDisponiblesAnteriores = thread.salidasDisponibles;


        while(it.hasNext()){
            Salida sal = it.next();

            Map<String,Integer> aux = new HashMap<String, Integer>();
            aux.put("factorDesalojo",sal.getNodos().size());
            //calculo la cantidad de agentes que tengo q mandar a cada puerta Inicialemnte
            int cantAgentesXPuerta = (int) Math.ceil((Double.valueOf(sal.getNodos().size()) * Double.valueOf(totalAgentes)/Double.valueOf(cantidadSalidas)));
            aux.put("cantidadAgentesPorPuerta",cantAgentesXPuerta);
            aux.put("agentesAsignados",0);

            thread.factorSalidas.put(sal.getNumeroSalida(),aux);
            thread.mapaAgentePorSalida.put(sal.getNumeroSalida(),new LinkedList());
        }

    }

    public static void asignarSalidasSugeridasSensor(ThreadSimulacion thread){
        //Necesito mantener la intencion (cantidad de agentes que envio a cada puerta):
        //Guardo por cada puerta cuantos agente envio a dicha puerta
        //  inicializarFactorDesalojo();

//        System.out.println("agentes por salida "+ this.mapaAgentePorSalida);

        calcularDensidadSensores(thread);
        distribuirAgentesEntrePuertas(thread);

        //this.calcularFactorDesalojoCadaPuerta();
        //System.out.println("agentes por salida actualizado "+ this.mapaAgentePorSalida);
    }

    private  void calcularDensidadSensores(AutomataCelular ac1){
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
            /*
            System.out.println("\n********** ");
            System.out.println("ID SENSOR: "+ proy.getAc().getCelda(y,x).getSensor().getId()+"\n" +
                    "vecinos "+ this.ac1.getCelda(y,x).getSensor().getCantidadAgentes()  +
                    " Salida  :"+this.ac1.getCelda(y,x).getSensor().getSalidaMasCercana());

            this.ac1.getCelda(y,x).getSensor().printDistanciaSalidas();
            this.ac1.getCelda(y,x).getSensor().printAgentes();
            System.out.println("\n**********");
            */

        }

    }

    private  LinkedList vecinosAgentesRadio(int y,int x, int ancho, int alto, int radio, AutomataCelular ac1){
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

    private ArrayList<DistanciaSalida> sortSalidas (LinkedList salidas){
        ArrayList<DistanciaSalida>  puerta = new ArrayList<DistanciaSalida> (salidas);
        Collections.sort(puerta, new SortByDistancia());
        return puerta;
    }
}
