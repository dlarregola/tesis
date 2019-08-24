/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicaAplicacion;

import InterfazUsusario.VentanaAnimacion;
import InterfazUsusario.VentanaBatch;
import InterfazUsusario.VentanaSimulacion;
import java.awt.Point;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Cristian
 */
public class ThreadSimulacion extends Thread {

    private boolean detectarBug = false;
    private boolean pausado = false;
    private boolean parar = false;
    private int cantidadIndividuos;
    private int cantidadIndividuosSalieron;
    private int cantidadIndividuosEvacuaron;
    private int cantidadIndividuosCaidos;
    private int cantidadIndividuosEvacuaronTotal;
    private int cantidadIndividuosCaidosTotal;
    private AutomataCelular ac1; //UTILIZADO EN CONJUTNO CON AC2 PARA TABAJAR SOBRE LAS POSICIONES DE LOS AGENTES DURANTE LOS SUB PASOS
    private AutomataCelular ac2;
    private AutomataCelular ac3;//UTILIZADO PARA CONSTUIR T + 1 PARA EL AMBIENTE.
    private ArrayList ordenActualizacion = new ArrayList();
    private ListIterator it;
    private ArrayList datosCorridas = new ArrayList();
    private String datosIntervalos;
    private Corrida corrida;
    private int contadorHumo;
    private int contadorFuego;
    private int cuadradrosAncho;
    private int cuadradosAlto;
    private int corridas;
    private double tiempoEvacuacionPersona = 0.0;
    private int timeSteps = 0;
    private double tiempoEvacuacion;
    private double espacioEvacuacionPersona = 0.0;
    private double tiempoExposicionPersona = 0.0;
    private double varianzaTiempoEvacuacion = 0.0;
    private double varianzaTiempoEvacuacionPersona = 0.0;
    private double varianzaEspacioEvacuacionPersona = 0.0;
    private double varianzaTiempoExposicionPersona = 0.0;
    private double desviacionEstandarTiempoEvacuacion = 0.0;
    private double desviacionEstandarTiempoEvacuacionPersona = 0.0;
    private double desviacionEstandarEspacioEvacuacionPersona = 0.0;
    private double desviacionEstandarTiempoExposicionPersona = 0.0;
    private int ventana;
    private Random random = new Random(System.currentTimeMillis());
    public boolean pleaseWait = false;
    public boolean allDone = false;
    public static LinkedList intenciones;
    public static LinkedList intencionesAnteriores;
    private LinkedList contadorSalidas, contadorSalidasTotal;
    private boolean recalcular;
    private Map<Integer, Map<String, Integer>> factorSalidas; //SUMO Mapa con con cada puerta y el factor de desalojo de cada puerta
    private Map<Integer, LinkedList> mapaAgentePorSalida;

    private int salidasDisponibles, salidasDisponiblesAnteriores; //Esto lo voy a usar porque si


    public ThreadSimulacion(int corridas, int ventana) {
        this.corridas = corridas;
        this.ventana = ventana;
        this.cuadradrosAncho = (int) ((double) Proyecto.getProyecto().getTamañox() / 0.4) + 1;
        this.cuadradosAlto = (int) ((double) Proyecto.getProyecto().getTamañoy() / 0.4) + 1;
        this.recalcular = false;
        Utilidades.construirSalidas(); //SOLO CONSTRUYO SALIDAS, NO CALCULO DISTANCIAS ESO SE HACE LUEGO EN EL METODO RUN
        intenciones = new LinkedList();
        intencionesAnteriores = new LinkedList();
        contadorSalidas = new LinkedList();
        contadorSalidasTotal = new LinkedList();
        for (int z = 0; z < Proyecto.getProyecto().getSalidas().size(); ++z) {
            intenciones.add(new Contador(0));
            intencionesAnteriores.add(new Contador(0));
            contadorSalidas.add(new int[10]); //TENGO POR CADA SALIDA 10 POSICIONES PARA 10 TIPOS DE AGENTES (28-09-2015)
            //ESTO DEBERIA DE CAMBIAR AL MOMENTO EN QUE TENGAMOS MAS DE TIPOS DE COMPORTAMIENTOS.
            contadorSalidasTotal.add(new int[10]);
        }
        for (int z = 0; z < Proyecto.getProyecto().getSalidas().size(); ++z) {
            for (int q = 0; q < 10; q++) {//TENGO POR CADA SALIDA 10 POSICIONES PARA 10 TIPOS DE AGENTES (28-09-2015)
                //ESTO DEBERIA DE CAMBIAR AL MOMENTO EN QUE TENGAMOS MAS DE TIPOS DE COMPORTAMIENTOS.
                ((int[]) contadorSalidasTotal.get(z))[q] = 0;
            }
        }
        mapaAgentePorSalida = new HashMap<Integer, LinkedList>();
        factorSalidas = new HashMap<Integer, Map<String, Integer>>();
        MotorSensado.inicializarFactorDesalojo(this);

    }

    private void reinicializacion() {
        this.ac1 = new AutomataCelular(this.cuadradosAlto, this.cuadradrosAncho);
        this.ac2 = new AutomataCelular(this.cuadradosAlto, this.cuadradrosAncho);
        this.ac3 = new AutomataCelular(this.cuadradosAlto, this.cuadradrosAncho);
        this.ac1 = Utilidades.copiarAutomata(Proyecto.getProyecto().getAc(), this.cuadradosAlto, this.cuadradrosAncho);
        Utilidades.calcularDistanciasFuego(ac1);
        this.cantidadIndividuosEvacuaron = 0;
        this.cantidadIndividuosCaidos = 0;
        this.recalcular = false;

        this.timeSteps = 0;
        this.tiempoEvacuacionPersona = 0.0f;
        this.espacioEvacuacionPersona = 0.0f;
        this.tiempoExposicionPersona = 0.0f;
        this.contadorHumo = Proyecto.getProyecto().getPropagacionHumo();
        this.contadorFuego = Proyecto.getProyecto().getPropagacionFuego();
        mapaAgentePorSalida = new HashMap<Integer, LinkedList>();
        factorSalidas = new HashMap<Integer, Map<String, Integer>>();
        MotorSensado.inicializarFactorDesalojo(this);
        if (this.ventana == 0) {
            VentanaAnimacion.getVentanaAnimacion().getMapa().paint(VentanaAnimacion.getVentanaAnimacion().getMapa().getGraphics());
            VentanaAnimacion.getVentanaAnimacion().getMapaCalor().paint(VentanaAnimacion.getVentanaAnimacion().getMapaCalor().getGraphics());
        }
        for (int z = 0; z < Proyecto.getProyecto().getSalidas().size(); ++z) {
            for (int q = 0; q < 10; q++) {//TENGO POR CADA SALIDA 10 POSICIONES PARA 10 TIPOS DE AGENTES (28-09-2015)
                //ESTO DEBERIA DE CAMBIAR AL MOMENTO EN QUE TENGAMOS MAS DE TIPOS DE COMPORTAMIENTOS.
                ((int[]) contadorSalidas.get(z))[q] = 0;
            }
        }
    }

    private void randomActualizacion() {
        ArrayList intermedio = new ArrayList();
        this.ordenActualizacion.clear();
        int posicion = 0;
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < this.cuadradosAlto * this.cuadradrosAncho; ++i) {
            intermedio.add(new Integer(i));
        }
        while (intermedio.size() > 0) {
            posicion = random.nextInt(intermedio.size());
            this.ordenActualizacion.add(intermedio.get(posicion));
            intermedio.remove(posicion);
        }
    }

    private Point getPunto(int nodo) {
        Point punto = new Point();
        punto.setLocation(nodo % this.cuadradrosAncho, nodo / this.cuadradrosAncho);
        return punto;
    }

    public boolean isRecalcular() {
        return this.recalcular;
    }

    public void setRecalcular(boolean recalcular) {
        this.recalcular = recalcular;
    }

    private int getRandom(int limiteSuperior) {
        return this.random.nextInt(limiteSuperior);
    }

    private double getRandomDouble() {
        return this.random.nextDouble();
    }

    private boolean chequearPunto(Point nodo) {
        if (nodo.x > -1 && nodo.x < this.cuadradrosAncho && nodo.y > -1 && nodo.y < this.cuadradosAlto) {
            return true;
        }
        return false;
    }

    private ArrayList chequearMoore(Point origen) {
        ArrayList<Integer> vecinos = new ArrayList<Integer>();
        Point p1 = new Point(origen.x - 1, origen.y - 1);
        Point p2 = new Point(origen.x, origen.y - 1);
        Point p3 = new Point(origen.x + 1, origen.y - 1);
        Point p4 = new Point(origen.x - 1, origen.y);
        Point p5 = new Point(origen.x + 1, origen.y);
        Point p6 = new Point(origen.x - 1, origen.y + 1);
        Point p7 = new Point(origen.x, origen.y + 1);
        Point p8 = new Point(origen.x + 1, origen.y + 1);
        if (this.chequearPunto(p1) && this.ac1.getCelda(p1.y, p1.x).getEstado() != 1 && this.ac1.getCelda(p1.y, p1.x).getEstado() != 2) {
            vecinos.add(new Integer(p1.x + p1.y * this.cuadradrosAncho));
        }
        if (this.chequearPunto(p2) && this.ac1.getCelda(p2.y, p2.x).getEstado() != 1 && this.ac1.getCelda(p2.y, p2.x).getEstado() != 2) {
            vecinos.add(new Integer(p2.x + p2.y * this.cuadradrosAncho));
        }
        if (this.chequearPunto(p3) && this.ac1.getCelda(p3.y, p3.x).getEstado() != 1 && this.ac1.getCelda(p3.y, p3.x).getEstado() != 2) {
            vecinos.add(new Integer(p3.x + p3.y * this.cuadradrosAncho));
        }
        if (this.chequearPunto(p4) && this.ac1.getCelda(p4.y, p4.x).getEstado() != 1 && this.ac1.getCelda(p4.y, p4.x).getEstado() != 2) {
            vecinos.add(new Integer(p4.x + p4.y * this.cuadradrosAncho));
        }
        if (this.chequearPunto(p5) && this.ac1.getCelda(p5.y, p5.x).getEstado() != 1 && this.ac1.getCelda(p5.y, p5.x).getEstado() != 2) {
            vecinos.add(new Integer(p5.x + p5.y * this.cuadradrosAncho));
        }
        if (this.chequearPunto(p6) && this.ac1.getCelda(p6.y, p6.x).getEstado() != 1 && this.ac1.getCelda(p6.y, p6.x).getEstado() != 2) {
            vecinos.add(new Integer(p6.x + p6.y * this.cuadradrosAncho));
        }
        if (this.chequearPunto(p7) && this.ac1.getCelda(p7.y, p7.x).getEstado() != 1 && this.ac1.getCelda(p7.y, p7.x).getEstado() != 2) {
            vecinos.add(new Integer(p7.x + p7.y * this.cuadradrosAncho));
        }
        if (this.chequearPunto(p8) && this.ac1.getCelda(p8.y, p8.x).getEstado() != 1 && this.ac1.getCelda(p8.y, p8.x).getEstado() != 2) {
            vecinos.add(new Integer(p8.x + p8.y * this.cuadradrosAncho));
        }
        return vecinos;
    }

    private int obtenerPuertaDestino(Point destino) {
        ListIterator iteradorSalidas = Proyecto.getProyecto().getSalidas().listIterator();
        while (iteradorSalidas.hasNext()) {
            Salida salida = (Salida) iteradorSalidas.next();
            ListIterator iteradorNodosSalida = salida.getNodos().listIterator();
            while (iteradorNodosSalida.hasNext()) {
                Point nodoSalida = (Point) iteradorNodosSalida.next();
                if (!nodoSalida.equals(destino)) continue;
                return salida.getNumeroSalida();
            }
        }
        return -1;
    }

    private boolean buscarPuntoLista(Point destino, LinkedList visitados) {
        Point nodo;
        ListIterator iterador = visitados.listIterator();
        while (iterador.hasNext()) {
            nodo = (Point) iterador.next();
            if (destino.x == nodo.x && destino.y == nodo.y) {
                return true;
            }
        }
        return false;
    }

    private boolean detectarDeadlock(Point nodo, AutomataCelular ac) {
        LinkedList visitados = new LinkedList();
        boolean continuarBuscando = true;
        Point proximo, actual;
        visitados.add(new Point(nodo.x, nodo.y));
        actual = new Point(nodo.x, nodo.y);
        while (continuarBuscando) {
            proximo = new Point();
            proximo.x = ac.getCelda(actual.y, actual.x).getAgente().getDestino().x;
            proximo.y = ac.getCelda(actual.y, actual.x).getAgente().getDestino().y;
            if (ac.getCelda(proximo.y, proximo.x).getEstado() == 6) { //SI LA CCELDA DESTINO DEL AGENTE ACTUAL TIENE UN AGENTE DEBO ANALIZARLA
                if (buscarPuntoLista(proximo, visitados)) {
                    continuarBuscando = false;
                    //System.out.println("Deadlock Detectado");
                    return (true);
                } else {
                    actual = proximo;
                    visitados.add(new Point(proximo.x, proximo.y));
                    continuarBuscando = true;
                }
            } else { //EL PROXIMO NO TIENE AGENTE, POR LO TANTO NO HAY DEADLOCK
                continuarBuscando = false;
            }
        }
        return false;
    }

    private void reglaHumo(Point nodo) {
        double probabilidadHumo = 0;
        if (this.ac1.getCelda(nodo.y, nodo.x).getNivelHumo() <= 0) {
            ArrayList vecinos = this.chequearMoore(nodo);
            for (int i = 0; i < vecinos.size(); ++i) {
                Point p = this.getPunto(((Integer) vecinos.get(i)).intValue());
                if (this.ac1.getCelda(p.y, p.x).getNivelHumo() > 0) {

                    if (this.ac1.getCelda(p.y, p.x).getNivelHumo() < 0.5) {
                        probabilidadHumo = probabilidadHumo + 0.65 * (1.0 / 8.0);
                    } else {
                        probabilidadHumo = probabilidadHumo + ((this.ac1.getCelda(p.y, p.x).getNivelHumo()) * (1.0 / 8.0));
                    }
                    //CODIGO ANTERIOR
                    //probabilidadHumo+=((this.ac1.getCelda(p.y, p.x).getNivelHumo())*(1.0/8.0));
                }
            }
            if (this.getRandomDouble() <= probabilidadHumo) {
                this.ac3.getCelda(nodo.y, nodo.x).setNivelHumo(0.1);
            }
        } else {
            this.ac3.getCelda(nodo.y, nodo.x).setNivelHumo(this.ac1.getCelda(nodo.y, nodo.x).getNivelHumo() + 0.1);
        }
    }

    private void reglaFuego(Point nodo) { //EL FUEGO Y EL HUMO SE INCREMENTA DE 0.1 EN 0.1 HASTA ALCANZAR SU MAXIMO NIVEL DE 1 (29-09-2015)
        double probabilidadFuego = 0.0;     //EXISTEN 10 NIVELES POSIBLES DE FUEGO y HUMO
        if (this.ac1.getCelda(nodo.y, nodo.x).getNivelFuego() <= 0 && this.ac1.getCelda(nodo.y, nodo.x).getNivelCombustibilidad() >= 1) {
            ArrayList vecinos = this.chequearMoore(nodo);
            for (int i = 0; i < vecinos.size(); ++i) {
                Point p = this.getPunto(((Integer) vecinos.get(i)).intValue());
                if (this.ac1.getCelda(p.y, p.x).getNivelFuego() > 0) {

                    //CODIGO PARA QUE EL FUEGO SE DESPLACE MAS RAPIDO CRISTIAN 07-04-2016
                    if (this.ac1.getCelda(p.y, p.x).getNivelFuego() < 0.5) {
                        probabilidadFuego = probabilidadFuego + 0.85 * (1.0 / 8.0);
                    } else {
                        probabilidadFuego = probabilidadFuego + ((this.ac1.getCelda(p.y, p.x).getNivelFuego()) * (1.0 / 8.0));
                    }
                    //FIN CODIGO PARA QUE EL FUEGO SE DESPLACE MAS RAPIDO CRISTIAN 07-04-2016

                    //ESTE ES EL CÓDIGO QUE ESTAABA
                    //probabilidadFuego=probabilidadFuego + ((this.ac1.getCelda(p.y, p.x).getNivelFuego())*(1.0/8.0));
                }
            }
            if (this.getRandomDouble() <= probabilidadFuego) {
                this.ac3.getCelda(nodo.y, nodo.x).setNivelFuego(0.1);
                this.ac3.getCelda(nodo.y, nodo.x).setNivelHumo(this.ac1.getCelda(nodo.y, nodo.x).getNivelHumo() + 0.1);
                this.ac3.getCelda(nodo.y, nodo.x).setEstado(4);
                this.setRecalcular(true);
            }
        }
    }


    //ESTA FASE CONSTRUYE EL AMBIENTE PARA EL TIEMPO T + 1 SOBRE AC3. Es decir, que AC3 contendrá el nuevo estado del ambiente.
    //Los automatas AC1 y AC2 son utilizados para poder ir actualizando los agentes durante los subpasos de tiempo para lograr
    //el efecto de continuidad. Sin embargo, lograr el efecto de continuidad es un problema que viene dado por la discretización.
    //Mientras que debo lograr el efecto de continuidad en los agentes durante un paso de tiempo (para ello uso los sub pasos)
    //el ambiente debe permanecer sin alteración alguna durante los mecionados sub pasos de tiempo. Es decir, en un paso de tiempo
    //el ambiente evoluciona una única vez, mientras que los agentes para lograr el efecto de continuidad deben realizar sub pasos.
    //TODO: actualizar datos del sensor
    private void faseAutomata() {
        ListIterator iterador = this.ordenActualizacion.listIterator();
        while (iterador.hasNext()) {
            Point nodo = this.getPunto(((Integer) iterador.next()).intValue());
            Celda nuevaCelda = Utilidades.copiarCeldaSinAgente(ac1.getCelda(nodo.y, nodo.x)); //Nueva Celda (29-09-2015)
            this.ac3.setCelda(nodo.y, nodo.x, nuevaCelda);
            //EL CODIGO DEL SWTICH ESTA DESCRIPTO DE ESTA MANERA SOLO PARA UNA MEJOR COMPRENSION
            switch (this.ac1.getCelda(nodo.y, nodo.x).getEstado()) {
                case 0: {
                    this.reglaHumo(nodo); //DEBEN IR EN ESTE ORDEN, PUES SI UNA CELDA VACIA NO TIENE O TIENE POCO HUMO PODRIA
                    this.reglaFuego(nodo);//TENER MAS EN T+1, Y SI ADEMAS SE PRENDE FUEGO DEBE TENER MAS HUMO AUN.
                    break;                //CASO CONTARIO SI LA CELDA RECIEN SE PRENDE FUEGO NO DEBERIA DE JUNTAR HUMO EXTRA
                }                         //Y ES LO QUE SUCEDERIA SI APLICO PRIMERO LA REGLA FUEGO (29-09-2015)
                case 4: {
                    //if(random.nextDouble()<0.5){
                    this.ac3.getCelda(nodo.y, nodo.x).setNivelFuego(this.ac3.getCelda(nodo.y, nodo.x).getNivelFuego() + 0.1);
                    //}
                    //if(random.nextDouble()<0.5){
                    this.ac3.getCelda(nodo.y, nodo.x).setNivelHumo(this.ac3.getCelda(nodo.y, nodo.x).getNivelHumo() + 0.1);

                    //}
                    break;
                }
                case 6: { //ACTUALIZADO (12-10-2015)
                    this.ac3.getCelda(nodo.y, nodo.x).setAgente(null);
                    this.ac3.getCelda(nodo.y, nodo.x).setEstado(0); //INDICO QUE ESTA CELDA NO TIENE MAS AGENTE PORQUE ESTOY ARMANDO
                    //EL NUEVO ESTADO DEL AMBIENTE. LUEGO DE ESTE PASO DE TIEMPO JUNTARE EL ESTADO DEL SUB MODELO AMBIENTAL
                    //Y EL ESTADO DEL SUB MODELO PEDESTRE.

                    //AQUI SE CONTROLA QUE: DEL PASO DE TIEMPO ANTERIOR UNA CELDA CON AGENTE PUEDE HABER ADQUIRIDO FUEGO
                    //ESTA SITUACIÓN ES VISIBLE SOLO EN EL MOMENTO QUE SE HACE EL MERGE DE AMBOS SUB MODELOS. POR LO TANTO
                    //PODEMOS TENER EL CASO DE QUE UNA CELDA CON AGENTE HAYA ADQUIRIDO FUEGO.
                    if (this.ac1.getCelda(nodo.y, nodo.x).getNivelFuego() > 0) {
                        this.ac3.getCelda(nodo.y, nodo.x).setEstado(4);
                        this.ac3.getCelda(nodo.y, nodo.x).setNivelFuego(this.ac3.getCelda(nodo.y, nodo.x).getNivelFuego() + 0.1);
                        this.ac3.getCelda(nodo.y, nodo.x).setNivelHumo(this.ac3.getCelda(nodo.y, nodo.x).getNivelHumo() + 0.1);
                        this.setRecalcular(true);//SE DEBEN RECALCULAR DISTANCIAS
                    } else {
                        this.reglaHumo(nodo);
                        this.reglaFuego(nodo);
                    }
                    break;
                }
            }
        }
        if (this.isRecalcular()) { // DESPUES DE APLICAR LAS REGLAS DE FUEGO, SI ES NECESARIO SE DEBEN RECALCULAR LAS DISTANCIAS
            Utilidades.recalcularDistancias(this.ac3);//HACIAS CADA UNA DE LAS SALIDAS
            this.setRecalcular(false);
        }

        //TODO: Llamar metodo sobre la lista de sensores que calcule
        //La densidad de cada sensor paso como parametro lista de sensores
        //Y aplicar politica de avisos


        //Esto lo tengo que llamar solo si cambian la cantidad de salidas es decir
        MotorSensado.asignarSalidasSugeridasSensor(this);

    }


    private void faseIntencion() {
        boolean chequearComportamiento;
        ListIterator iterador = this.ordenActualizacion.listIterator();
        while (iterador.hasNext()) {
            Point nodo = this.getPunto(((Integer) iterador.next()).intValue());
            switch (this.ac1.getCelda(nodo.y, nodo.x).getEstado()) {
                case 6: {
                    //System.out.println("----------   case 6 entre    --------- \n");

                    //SI EL TIEMPO DE REACCION A LLEGADO A 0 COMIENZO A MOVERME SINO NO ME QUEDO EN MI LUGAR
                    if (!(ac1.getCelda(nodo.y, nodo.x).getAgente().getDemoraReaccion() > 0)) {

                        chequearComportamiento = ac1.getCelda(nodo.y, nodo.x).getAgente().getComportamiento().ejecutarComportamiento(this.ac1, this.ac2, this.ac3, nodo);
                        //SE DETECTA QUE EL AGENTE ESTA ENCERRADO Y NO PUEDE MOVERSE POR LO TANTO SE DEBE TOMAR UNA ACCION
                        //LA ACCION POR EL MOMENTO ES MARCARLO COMO AGENTE CAIDO O MUERTO DESCONTARLO DE LOS AGENTE QUE DEBEN
                        //EVACUAR. (11-10-2015)
                        //TAMBIEN AQUI SE DETECTA CUANDO EL AGENTE ESTA PARADO SOBRE EL FUEGO.
                        if (!chequearComportamiento) { //ACTUALIZADO (12-10-2015)
                            ac1.getCelda(nodo.y, nodo.x).setAgente(null); //MARCO LA CELDA COMO SIN AGENTE, AGENTE CAIDO Y CAMBIO EL ESTADO A LA CELDA
                            this.cantidadIndividuos--;//RESTO EN UNO LA CANTIDAD DE INDIVIDUOS
                            this.cantidadIndividuosCaidos++;//AUMENTO LA CANTIDAD DE INDIVIDUOS CAIDOS
                            if (ac1.getCelda(nodo.y, nodo.x).getNivelFuego() > 0) {
                                ac1.getCelda(nodo.y, nodo.x).setEstado(4);
                            } else {
                                ac1.getCelda(nodo.y, nodo.x).setEstado(0);
                            }
                        }


                        if (detectarBug) {
                            System.out.println("ErrorIntencion ..........................................");
                            System.out.print("NODO: (" + nodo.x + "," + nodo.y + ")  ESTADO: " + ac1.getCelda(nodo.y, nodo.x).getEstado() + "   TIPO:" + ac1.getCelda(nodo.y, nodo.x).getAgente().getTipo() + "   FUEGO: " + ac1.getCelda(nodo.y, nodo.x).getNivelFuego() + "   ");
                            System.out.print("SALIDA: " + ac1.getCelda(nodo.y, nodo.x).getAgente().getSalidaElegida() + " DESTINO: (" + ac1.getCelda(nodo.y, nodo.x).getAgente().getDestino().x + "," + ac1.getCelda(nodo.y, nodo.x).getAgente().getDestino().y + ")");
                            System.out.println("    DECISIONES: " + ac1.getCelda(nodo.y, nodo.x).getAgente().getDesiciones() + "   INFERENCIA: " + ac1.getCelda(nodo.y, nodo.x).getAgente().getTiempoInferencia());
                            Point vecino;
                            LinkedList vecinos = Utilidades.obtenerVecindarioCompleto(nodo, ac1);
                            ListIterator it = vecinos.listIterator();
                            while (it.hasNext()) {
                                vecino = (Point) it.next();
                                System.out.println("NODO: (" + vecino.x + "," + vecino.y + ")  ESTADO: " + ac1.getCelda(vecino.y, vecino.x).getEstado() + "   FUEGO: " + ac1.getCelda(vecino.y, vecino.x).getNivelFuego());
                            }
                        }


                    } else {
                        ac1.getCelda(nodo.y, nodo.x).getAgente().setRespuesta(2);
                        ac1.getCelda(nodo.y, nodo.x).getAgente().setDemoraReaccion(ac1.getCelda(nodo.y, nodo.x).getAgente().getDemoraReaccion() - 1);
                    }

                }
            }
        }
    }

    private void faseDesicion() {

        int randomDesicion = 0;
        double factorPersonas, factorVelocidad;
        int sumaVelocidades;
        Point ganador = null;
        ListIterator iterador = this.ordenActualizacion.listIterator();
        while (iterador.hasNext()) {
            sumaVelocidades = 0;
            factorPersonas = 0.0;
            factorVelocidad = 0.0;
            Point nodo = this.getPunto(((Integer) iterador.next()).intValue());
            if (this.ac1.getCelda(nodo.y, nodo.x).getPeticiones().size() <= 0) continue;

            //A TODOS LOS AGENTES QUE SOLICITARON MOVERSE LES CONTESTO QUE NO
            for (int i = 0; i < this.ac1.getCelda(nodo.y, nodo.x).getPeticiones().size(); ++i) {
                Point peticionDesde = (Point) this.ac1.getCelda(nodo.y, nodo.x).getPeticiones().get(i);
                this.ac1.getCelda(peticionDesde.y, peticionDesde.x).getAgente().setRespuesta(2);
                sumaVelocidades = sumaVelocidades + this.ac1.getCelda(peticionDesde.y, peticionDesde.x).getAgente().getVelocidad();
            }

            //CODIGO AGREGADO PARA TENER EN CUENTA EL EFECTO FREEZE BY HEATING
            //CRISTIAN 16-11-2015
            //LA SIGUIENTE LINEA DETERMINA LA RELACION ENTRE LA CANTIDAD DE AGENTES EN EL VECINDARIO
            //QUE PRETENDEN LLEGAR A ESGTA POSICIÓN Y LA CANTIDAD DE CLDAS QUE EXISTEN EL VECINDARIO

            if (Utilidades.chequearMoore(nodo, ac1).size() > 0) {
                //PUEDE PASAR QUE INTENTE DIVIDIR POR CERO. UNA CELDA PUEDE TENER PETICIONES DE UN VECINDARIO DONDE HAYA FUEGO Y PAREDES
                //POR LO TANTO EL METODO chequearMoore PUEDE DEVOLVER EL VALOR 0. NO SE PUEDE DIVIDIR POR 0.
                factorPersonas = ((float) (this.ac1.getCelda(nodo.y, nodo.x).getPeticiones().size())) / ((float) Utilidades.chequearMoore(nodo, ac1).size());
                factorVelocidad = (sumaVelocidades / ((double) this.ac1.getCelda(nodo.y, nodo.x).getPeticiones().size())) / 10;//OBTENGO UN NUMERO ENTE 0.1 y 1
                //System.out.println("factorVelocidad: " + factorVelocidad  + "    factorPersonas: " + factorPersonas);
            } else {
                factorPersonas = 0;
                factorVelocidad = 0;
            }

            //factorAlfa=(float) 0.5;
            //AHORA TOMO DOS FACTOORES A TENER EN CUENTA Velocidad y Cantidad de Personas QUE INTENTAN ACCEDER A LA MISMA CELDA
            //CON ESTO LOGO REPRODUCIR EL EFECTO DE FREEZE BY HEATING CRISTIAN 29-01-2016
            if (this.getRandomDouble() > (factorVelocidad + factorPersonas) / 2) {

                //OBTENGO EL AGENTE GANADOR EN CASO DE EXISTIR VARIAS PETICIONES. POR EL MOMENTO EL AGENTE GANADOR
                //SE OBTIENE SOLO DE MANERA ALEATORIA. SI DESEO TENER EN CUENTA LOS PUNTOS DE DAÑO DEBO AÑADIR EL CODIGO
                //CORRESPONDIENTE AQUI (05-10-2015)
                randomDesicion = this.getRandom(this.ac1.getCelda(nodo.y, nodo.x).getPeticiones().size());
                ganador = (Point) this.ac1.getCelda(nodo.y, nodo.x).getPeticiones().get(randomDesicion);
                switch (this.ac1.getCelda(nodo.y, nodo.x).getEstado()) {
                    case 0: { //SI LA CELDA DESTINO ES VACIA INDICO QUE PUEDE MOVERSE
                        this.ac1.getCelda(ganador.y, ganador.x).getAgente().setRespuesta(1);
                        break;
                    }
                    case 5: {// AHORA LAS PUERTAS SIEMPRE ESTAN ABIERRTAS CRISTIAN 20/01/2016
                        this.ac1.getCelda(ganador.y, ganador.x).getAgente().setRespuesta(1);
                        break;
                    }
                    case 6: {//SI LA CELDA ESTA OCUPADA POR OTRO AGENTE INDICO QUE EL MOVIMIENTO ES INDETERMINADO
                        this.ac1.getCelda(ganador.y, ganador.x).getAgente().setRespuesta(0);
                    }
                }
                this.ac1.getCelda(nodo.y, nodo.x).getPeticiones().clear();
                this.ac1.getCelda(nodo.y, nodo.x).getPeticiones().add(ganador);
            }
            //else{
            //    System.out.println("NOOOO");
            //}
        }
    }

    private void faseBackTraking() {
        int cantidadRespuestas = 0;
        Point nodo = null;
        Point ganador = null;
        int respuesta = 0;
        int respuestasAnterior = 0, respuestasActual = 0, cantidadVeces = 0, cuentaAgente = 0;
        while (cantidadRespuestas < this.cantidadIndividuos) {
            cantidadRespuestas = 0;
            ListIterator iterador = this.ordenActualizacion.listIterator();
            while (iterador.hasNext()) {
                nodo = this.getPunto(((Integer) iterador.next()).intValue());
                if (this.ac1.getCelda(nodo.y, nodo.x).getPeticiones().size() > 0) {
                    ganador = (Point) this.ac1.getCelda(nodo.y, nodo.x).getPeticiones().getFirst();
                    try {
                        if (this.ac1.getCelda(ganador.y, ganador.x).getAgente().getRespuesta() == 0) {

                            respuesta = this.ac1.getCelda(nodo.y, nodo.x).getAgente().getRespuesta();
                            this.ac1.getCelda(ganador.y, ganador.x).getAgente().setRespuesta(respuesta);

                            //System.out.println("ANTES DETECTAR DEADLOCK ->->->->");
                            //DETECTAR DEADLOCK
                            if (this.ac1.getCelda(nodo.y, nodo.x).getEstado() == 6 && this.ac1.getCelda(nodo.y, nodo.x).getAgente().getRespuesta() == 0 && this.detectarDeadlock(nodo, this.ac1)) {

                                this.ac1.getCelda(nodo.y, nodo.x).getAgente().setRespuesta(2);
                                this.ac1.getCelda(ganador.y, ganador.x).getAgente().setRespuesta(2);

                                //CODIGO PARA HACER SWAP INTERCAMBIAR DOS y SOLO EL CASO DE DOS INDIVIDUOS QUE ESTAN DE FRENTE
                                //EN UN DEADLOCK CRISTIAN 09-04-2016
                                Point destinoNodoActual = (Point) (Point) this.ac1.getCelda(nodo.y, nodo.x).getAgente().getDestino();
                                Point destinoGanador = this.ac1.getCelda(ganador.y, ganador.x).getAgente().getDestino();

                                if ((destinoGanador.x == nodo.x && destinoGanador.y == nodo.y) && (destinoNodoActual.x == ganador.x && destinoNodoActual.y == ganador.y)) {
                                    //System.out.println("HIC SWAP");
                                    this.ac1.getCelda(nodo.y, nodo.x).getAgente().setRespuesta(1);
                                    this.ac1.getCelda(ganador.y, ganador.x).getAgente().setRespuesta(1);
                                }
                                //FIN CODIGO SWAP
                            }

                            //System.out.println("PASE DETECTAR DEADLOCK ->->->->");
                            /*if (this.ac1.getCelda(nodo.y, nodo.x).getEstado() == 6 && nodo.y == this.ac1.getCelda((int)ganador.y, (int)ganador.x).getAgente().getDestino().y && nodo.x == this.ac1.getCelda((int)ganador.y, (int)ganador.x).getAgente().getDestino().x && this.ac1.getCelda(ganador.y, ganador.x).getAgente().getRespuesta() == 0 && this.ac1.getCelda(nodo.y, nodo.x).getAgente().getRespuesta() == 0) {
                                this.ac1.getCelda(nodo.y, nodo.x).getAgente().setRespuesta(2);
                                this.ac1.getCelda(ganador.y, ganador.x).getAgente().setRespuesta(2);
                            }*/
                        }
                    } catch (Exception ex) {
                        /*System.out.println("YO SOY:(" + nodo.y + "," + nodo.x + ")     DistanciaSalida:" + this.ac1.getCelda(nodo.y, nodo.x).getDistanciaSalida());
                        System.out.println("ESTADO:" + this.ac1.getEstado(nodo.y, nodo.x).getEstado());
                        System.out.println("GANADOR:(" + ganador.y + "," + ganador.x + ")   EST.GAN:" + this.ac1.getEstado(ganador.y, ganador.x).getEstado());
                        System.out.println("PETICIONES:" + this.ac1.getEstado(nodo.y, nodo.x).getPeticiones().size());*/
                    }
                }
                if (this.ac1.getCelda(nodo.y, nodo.x).getEstado() != 6 || this.ac1.getCelda(nodo.y, nodo.x).getAgente().getRespuesta() == 0)
                    continue;
                ++cantidadRespuestas;
            }

            //VARIABLE E IF ELSE PARA VER PORQUE NO FUNCIONA DESP PUEDO BORRARLO CRISTIAN 27/11/2015
            respuestasActual = cantidadRespuestas;
            if (respuestasAnterior == respuestasActual) {
                cantidadVeces++;
                //System.out.println("IGUAL   RAnterior: " + respuestasAnterior + "    RACTUAL: " + respuestasActual + "   VECES:" + cantidadVeces + "   INDIVIDUOS:"+this.cantidadIndividuos);
            } else {
                //System.out.println("NO IGUAL   RAnterior: " + respuestasAnterior + "    RACTUAL: " + respuestasActual + "   INDIVIDUOS:"+this.cantidadIndividuos);
                cantidadVeces = 0;
                respuestasAnterior = respuestasActual;
            }
            //IF SOLO PARA VER PORQUE NO FUNCIONA DESPUES PUEDO BORRARLO 27/11/2015 CRISTIAN
            /*if(cantidadVeces>=50){
                cuentaAgente=0;
                iterador = this.ordenActualizacion.listIterator();
                while (iterador.hasNext()) {
                    nodo = this.getPunto(((Integer)iterador.next()).intValue());
                    if (this.ac1.getCelda(nodo.y, nodo.x).getEstado()==6) {
                      cuentaAgente++;
                      if(this.ac1.getCelda(nodo.y, nodo.x).getAgente().getRespuesta()==0){
                        System.out.println("Error Backtraking..........................................");
                        System.out.print("NODO: (" + nodo.x + "," + nodo.y + ")  ESTADO: "  + ac1.getCelda(nodo.y,nodo.x).getEstado() + "   TIPO:" + ac1.getCelda(nodo.y,nodo.x).getAgente().getTipo() +   "   FUEGO: " + ac1.getCelda(nodo.y,nodo.x).getNivelFuego() + "   ");
                        System.out.print("SALIDA: " + ac1.getCelda(nodo.y,nodo.x).getAgente().getSalidaElegida() + " DESTINO: (" + ac1.getCelda(nodo.y,nodo.x).getAgente().getDestino().x + "," + ac1.getCelda(nodo.y,nodo.x).getAgente().getDestino().y + ")");
                        System.out.println("    DECISIONES: " + ac1.getCelda(nodo.y, nodo.x).getAgente().getDesiciones() + "   INFERENCIA: " + ac1.getCelda(nodo.y, nodo.x).getAgente().getTiempoInferencia());
                        Point vecino;
                        LinkedList vecinos = Utilidades.obtenerVecindarioCompleto(nodo, ac1);
                        ListIterator it = vecinos.listIterator();
                        while(it.hasNext()){
                            vecino=(Point)it.next();
                            System.out.println("NODO: (" + vecino.x + "," + vecino.y + ")  ESTADO: "  + ac1.getCelda(vecino.y,vecino.x).getEstado() + "   FUEGO: " + ac1.getCelda(vecino.y,vecino.x).getNivelFuego());
                        }
                      }

                    }
                }
                System.out.println("AGENTES: " + cuentaAgente + "   INDIVIDUOS TOTAL:" + this.cantidadIndividuos + "   CANT. RESP." + cantidadRespuestas  + "   CANT. SALI:" + this.cantidadIndividuosSalieron);


            }*/

        }
    }

    public void faseActualizacion() {
        Point nodo = null;
        Point destino = null;
        ListIterator iterador = this.ordenActualizacion.listIterator();
        while (iterador.hasNext()) { //PRIMERO CREO AC2 SIN AGENTES (05-10-2015)
            nodo = this.getPunto(((Integer) iterador.next()).intValue());
            Celda celda = Utilidades.copiarCeldaSinAgente(this.ac1.getCelda(nodo.y, nodo.x));
            if (celda.getEstado() == 6) { //MARCO LA CELDA COMO VACÍA SIN AGENTE
                celda.setEstado(0);   //A CONTINUACIÓN ESTA INFORMACIÓN SE ACTUALIZA Y COLOCA LOS AGENTES
            }
            this.ac2.setCelda(nodo.y, nodo.x, celda);
            //ESTA LINEA ES PARA RENOVAR LA LISTA DE PETICIONES DE CADA CELDA
            //YA NO DEBERIA DE HACER FALTA
            //this.ac2.getCelda(nodo.y,nodo.x).renovarPeticiones();
        }
        iterador = this.ordenActualizacion.listIterator();
        while (iterador.hasNext()) {//AQUI ACTUALIZO LOS AGENTES A SUS RESPECTIVAS POSICIONES
            nodo = this.getPunto(((Integer) iterador.next()).intValue());
            if (this.ac1.getCelda(nodo.y, nodo.x).getEstado() == 6) {
                if (this.ac1.getCelda(nodo.y, nodo.x).getAgente().getRespuesta() == 1) {//SI EL MOVIMIENTO FUE ACEPTADO
                    this.ac1.getCelda(nodo.y, nodo.x).getAgente().setContadorVelocidad(this.ac1.getCelda(nodo.y, nodo.x).getAgente().getContadorVelocidad() + 1);
                    destino = this.ac1.getCelda(nodo.y, nodo.x).getAgente().getDestino();
                    switch (this.ac1.getCelda(destino.y, destino.x).getEstado()) {
                        case 5: {
                            --this.cantidadIndividuos;
                            ++this.cantidadIndividuosSalieron;                                                       //TIEMPO DE EVACUACION MULTIPLICADO POR 3 PORQ AVANZA TRES POSICIONES POR PASO DE TIEMPO
                            //CRISTIAN 30/07/2018
                            this.espacioEvacuacionPersona = (float) ((double) this.espacioEvacuacionPersona + (double) (this.ac1.getCelda(nodo.y, nodo.x).getAgente().getPasosDados() * 0.4));
                            //ANTES ESTABA LA SIGUIENTE LINEA
                            //this.espacioEvacuacionPersona = (float)((double)this.espacioEvacuacionPersona + (double)((this.ac1.getCelda(nodo.y, nodo.x).getAgente().getTiempoEvacuacion() - 1)*3) * 0.4);
                            //tiempoEvacuacionPersona SUMO LOS TIEMPOS Y LUEGO DEBERIA DIVIDIR POR EL TOTAL DE PERSONAS
                            //ESTO AHORA ES ASI PUESTO QUE EL TIEMPO DE VACUACION ES LA CANTIDAD DE PASOS HASTA QUE EL INDIVIDUO
                            //LOGO EVACUAR. (05-10-2015)
                            this.tiempoEvacuacionPersona += (float) (this.ac1.getCelda(nodo.y, nodo.x).getAgente().getTiempoEvacuacion());
                            //ESTA ESTADISTICA TAMBIEN CAMBIO POR LAS RAZONES MENCIONADAS ANTERIORMENTE (05-10-2015)
                            this.tiempoExposicionPersona = (float) ((double) this.tiempoExposicionPersona + (double) (this.ac1.getCelda(nodo.y, nodo.x).getAgente().getDaño()));
                            ((int[]) (contadorSalidas.get(this.obtenerPuertaDestino(this.ac1.getCelda(nodo.y, nodo.x).getAgente().getDestino()) - 1)))[this.ac1.getCelda(nodo.y, nodo.x).getAgente().getTipo()]++;
                            //TODO: cuando sale un agente tengo que dejar de tenerlo
                            // en cuenta en la lista de agentes q tengo para cada puerta

                            break;
                        }
                        case 0:
                        case 6: {
                            //PREPARO EL AGENTE PARA ACTUALIZAR SU POSICION. ESTO LO LOGRAMOS SIMPLEMENTE CAMBIANDO LA REFERENCIA
                            //HAGO QUE LA CELDA DESTINO A LA QUE EL AGENTE DEBE MOVERSE EN AC2 APUNTE A ESTE AGENTE

                            //CRISTIAN 30/07/2018
                            this.ac1.getCelda(nodo.y, nodo.x).getAgente().setPasosDados(this.ac1.getCelda(nodo.y, nodo.x).getAgente().getPasosDados() + 1);

                            this.ac2.getCelda(destino.y, destino.x).setAgente(this.ac1.getCelda(nodo.y, nodo.x).getAgente());
                            this.ac2.getCelda(destino.y, destino.x).setEstado(6);//INDICO QUE LA CELDA TIENE UN AGENTE
                            if (this.ac2.getCelda(destino.y, destino.x).getNivelHumo() > 1) {
                                this.ac2.getCelda(destino.y, destino.x).getAgente().setDaño(this.ac2.getCelda(destino.y, destino.x).getAgente().getDaño() + 1);
                            }
                            this.ac2.getCelda(destino.y, destino.x).getAgente().setTiempoNoActualizacion(0);
                            this.ac2.getCelda(destino.y, destino.x).getAgente().setTiempoEvacuacion(timeSteps);//CAMBIO EN EL SETEO DE ESTA VARIABLE (05-10-2015)

                        }
                    }
                } else {//SI EL MOVIMIENTO NO FUE ACEPTADO DEJO EL AGENTE EN LA CELDA QUE ESTABA
                    this.ac2.getCelda(nodo.y, nodo.x).setAgente(this.ac1.getCelda(nodo.y, nodo.x).getAgente());
                    this.ac2.getCelda(nodo.y, nodo.x).setEstado(6);//INDICO QUE LA CELDA TIENE UN AGENTE
                    if (this.ac2.getCelda(nodo.y, nodo.x).getAgente().getContadorVelocidad() < this.ac2.getCelda(nodo.y, nodo.x).getAgente().getVelocidad()) {
                        //PUEDO CONTINUAR MOVIENDOME, NO LO HICE PORQUE MI MOVIMIENTO FUE NO ACEPTADO
                        //AUMENTO EL TIEMPO DE NO ACTUALIZACION
                        this.ac2.getCelda(nodo.y, nodo.x).getAgente().setTiempoNoActualizacion(this.ac2.getCelda(nodo.y, nodo.x).getAgente().getTiempoNoActualizacion() + 1);
                    }
                }
                //ANTES DE TERMINAR SETEO LA RESPUESTA DEL AGENTE A 0
                this.ac1.getCelda(nodo.y, nodo.x).getAgente().setRespuesta(0);
            }
        }

    }

    public void faseActualizacionPintadoPasoTiempo() {
        for (int i = 0; i < this.cuadradosAlto; ++i) {
            for (int j = 0; j < this.cuadradrosAncho; ++j) {
                if (this.ac1.getCelda(i, j).getEstado() == 6) {
                    this.ac3.getCelda(i, j).setAgente(this.ac1.getCelda(i, j).getAgente());
                    this.ac3.getCelda(i, j).getAgente().setContadorVelocidad(0);
                    //CASO DE UNA CELDA CON FUEGO Y AGENTE
                    //if(this.ac3.getCelda(i,j).getEstado()==4){
                    this.ac3.getCelda(i, j).setEstado(6);
                    //}
                }
            }
        }
        this.ac1 = ac3;
        this.ac3 = new AutomataCelular(this.cuadradosAlto, this.cuadradrosAncho);
        if (this.ventana == 0) {
            try {
                VentanaAnimacion.getVentanaAnimacion().getMapa().paint(VentanaAnimacion.getVentanaAnimacion().getMapa().getGraphics(), ac1);
                VentanaAnimacion.getVentanaAnimacion().getMapaCalor().paint(VentanaAnimacion.getVentanaAnimacion().getMapaCalor().getGraphics(), ac1);
                //Thread.sleep(10);
            } catch (Exception ex) {
                System.out.println("Falla En Pintado de Canvas");
                Logger.getLogger(ThreadSimulacion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


    public void timeStep() {

        //FASE DEL SUB MODELO AMBIENTAL
        this.faseAutomata();

        //CRISTIAN 07/08/2018
        //Al variar el nuero K debo recordar que tambien debo modificar el codigo que configura el tiempo de reaccion en
        //clase mapa. Dadod que allí este factor se utiliza para multiplicarlos por los segundos de Demora.
        for (int k = 0; k < 10; ++k) {

            //CUATRO FASES DEL SUB MODELO PEDESTE
            this.faseIntencion();
            //System.out.println("PASE INTENCION ...");
            this.faseDesicion();
            //System.out.println("PASE DESICION >>>>>");
            this.faseBackTraking();
            //System.out.println("PASE BACKTAKING ****");
            this.faseActualizacion();
            //System.out.println("PASE ACTUALIZACION ?????");
            //ACTUALIZO LAS INTENCIONES PAR ALO COMPORTAMIENTOS
            intencionesAnteriores = intenciones;
            intenciones = new LinkedList();
            for (int z = 0; z < Proyecto.getProyecto().getSalidas().size(); ++z) {
                intenciones.add(new Contador(0));
            }
            //REASIGNO LOS AUTOMATAS PARA CONTINUAR CON LOS SUB PASOS
            this.ac1 = this.ac2;
            this.ac2 = new AutomataCelular(this.cuadradosAlto, this.cuadradrosAncho);

            if (this.ventana == 0) {
                try {
                    VentanaAnimacion.getVentanaAnimacion().getTextPasoTiempo().setText(Integer.toString(this.timeSteps));
                    VentanaAnimacion.getVentanaAnimacion().getTextSubPasoTiempo().setText(Integer.toString(k));
                    VentanaAnimacion.getVentanaAnimacion().getMapa().paint(VentanaAnimacion.getVentanaAnimacion().getMapa().getGraphics(), ac1);
                    VentanaAnimacion.getVentanaAnimacion().getMapaCalor().paint(VentanaAnimacion.getVentanaAnimacion().getMapaCalor().getGraphics(), ac1);
                    //Thread.sleep(10);
                } catch (Exception ex) { //catch (InterruptedException ex){
                    System.out.println("Falla En Pintado de Canvas");
                    Logger.getLogger(ThreadSimulacion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        //ESTA NUEVA FASE ES CREADA SOLO A LOS EFECTOS DE HACER UN MERGE ENTRE EL SUB MODELO AMBIENTAL
        //Y EL SUB MODELO PEDESTRE PUESTO QUE HASTA EL MOMENTO EL RESULTADO DE LA EVOLUCION DEL SUB MODELO AMBIENTAL
        //SE ENCUENTA EN AC3 y EL SUB MODELO PEDESTRE SE ENCUENTA EN AC1.
        //ADEMAS SE ENCARGA DE PINTAR EN EL CANVAS LA EVOLUCION.
        faseActualizacionPintadoPasoTiempo();

        /*this.faseAutomata();
        faseActualizacionPintadoPasoTiempo();
        this.faseAutomata();
        faseActualizacionPintadoPasoTiempo();*/
    }

    @Override
    public void run() {
        String datos;
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setRoundingMode(RoundingMode.CEILING);
        int cantidadIndividuosAnterior = 0;
        int control = 350;
        boolean exit = false;
        this.cantidadIndividuos = Proyecto.getProyecto().getCantidadPersonas();
        int numeroCorrida = 1;

        //PARA IMPRIMIR NOMBRE DEL PROYECTO
        if (this.ventana == 1) {
            System.out.println("--- " + Proyecto.getProyecto().getNombreProyecto() + " ---");
        }
        if (this.ventana == 1) {
            datos = VentanaSimulacion.getVentanaSimulacion().getCantidadEjecuciones();
            this.corridas = Integer.parseInt(datos);
            VentanaSimulacion.getVentanaSimulacion().getBarraProgreso().setMinimum(0);
            VentanaSimulacion.getVentanaSimulacion().getBarraProgreso().setMaximum(this.corridas);
        } else if (this.ventana == 2) {
            datos = VentanaBatch.getVentanaBatch().getCantidadEjecuciones();
            this.corridas = Integer.parseInt(datos);
        }

        while (numeroCorrida <= this.corridas) {
            this.reinicializacion(); //ARMO EL AUTOMATA PARA INICIAR AC1,
            cantidadIndividuosAnterior = this.cantidadIndividuos = Proyecto.getProyecto().getCantidadPersonas();
            while (!(this.cantidadIndividuos <= 0 || exit)) {
                if (cantidadIndividuosAnterior == this.cantidadIndividuos) {
                    if (--control == 0) {
                        String mensaje = "Es posible que la simulación haya incurrido en un estado incorrecto, por favor controle la configuración de la misma.\n DESEA CONTINUAR CON LA EJECUCION ?";
                        int res = JOptionPane.showConfirmDialog(new JFrame(), mensaje, "AVISO, POSIBLE ESTADO ERRONEO", 0, 2, null);

                        detectarBug = true;

                        if (res == 1) {
                            exit = true;
                        } else {
                            control = 350;
                        }
                    }
                } else {
                    cantidadIndividuosAnterior = this.cantidadIndividuos;
                    control = 350;
                }
                this.randomActualizacion();
                this.it = this.ordenActualizacion.listIterator();
                this.timeStep();
                this.cantidadIndividuosEvacuaron += this.cantidadIndividuosSalieron;

                //IMPRESION PARA OBTENER GRAFICAS DE SALIDA
                if (ventana == 1) {
                    //System.out.println(this.cantidadIndividuosEvacuaron);
                }

                this.cantidadIndividuosSalieron = 0;
                ++this.timeSteps;

                synchronized (this) {//PAUSAR EL THREAD (11-10-2015)
                    while (pausado) {
                        try {
                            wait();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ThreadSimulacion.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                if (parar)//PARAR EL THREAD (11-10-2015)
                    break;
            }

            //LO HAGO DOS VECS PARA CORTAR AMBOS BUCLES
            if (parar)//PARAR EL THREAD (11-10-2015)
                break;

            if (this.ventana == 1) {
                this.tiempoEvacuacion = this.timeSteps;
                this.tiempoEvacuacion *= 1.0f;
                this.tiempoEvacuacionPersona /= (float) (Proyecto.getProyecto().getCantidadPersonas() - this.cantidadIndividuos);
                this.espacioEvacuacionPersona /= (float) (Proyecto.getProyecto().getCantidadPersonas() - this.cantidadIndividuos);
                this.tiempoExposicionPersona /= (float) (Proyecto.getProyecto().getCantidadPersonas() - this.cantidadIndividuos);
                datos = "";
                datos = "Ejecución Nro: " + numeroCorrida + "   --------------------------------------------------------\n";
                datos = datos + "Tiempo Total de Evacuación: " + this.tiempoEvacuacion + "\n";
                datos = datos + "Tiempo Medio de Evacuación: " + this.tiempoEvacuacionPersona + "\n";
                datos = datos + "Espacio Medio Recorrido: " + this.espacioEvacuacionPersona + "\n";
                //datos = datos + "Tiempo Medio de Exp. Al Humo:" + this.tiempoExposicionPersona + "\n";
                datos = datos + "\n";
                datos = datos + "Individuos Evacuados: " + this.cantidadIndividuosEvacuaron + "    Individuos Caidos: " + this.cantidadIndividuosCaidos + "\n";
                for (int q = 0; q < Proyecto.getProyecto().getSalidas().size(); q++) {
                    int total = 0;
                    //Point salida = ((Point)((Salida)Proyecto.getProyecto().getSalidas().get(q)).getNodos().getFirst());
                    for (int x = 0; x < 10; x++) {
                        if (((int[]) contadorSalidas.get(q))[x] != 0) {
                            total = total + ((int[]) contadorSalidas.get(q))[x];
                        }
                    }

                    datos = datos + " ---- Salida: " + q + "    Cant. Individuos: " + total + " ---- \n";
                    for (int x = 0; x < 10; x++) {
                        if (((int[]) contadorSalidas.get(q))[x] != 0) {
                            datos = datos + "Tipo: " + x + "   Cantidad: " + ((int[]) contadorSalidas.get(q))[x] + "\n";
                            ((int[]) contadorSalidasTotal.get(q))[x] += ((int[]) contadorSalidas.get(q))[x];
                        }
                    }
                }
                datos = datos + "\n";
                VentanaSimulacion.getVentanaSimulacion().getParciales().setText(VentanaSimulacion.getVentanaSimulacion().getParciales().getText() + datos);

                this.datosCorridas.add(new Corrida(this.corridas, this.timeSteps, this.tiempoEvacuacion, this.tiempoEvacuacionPersona, this.espacioEvacuacionPersona, this.tiempoExposicionPersona, this.cantidadIndividuosEvacuaron, this.cantidadIndividuosCaidos));
                VentanaSimulacion.getVentanaSimulacion().getBarraProgreso().setValue(VentanaSimulacion.getVentanaSimulacion().getBarraProgreso().getValue() + 1);
            }
            if (this.ventana == 2) {
                this.tiempoEvacuacion = this.timeSteps;
                this.tiempoEvacuacion *= 1.0f;
                this.tiempoEvacuacionPersona /= (float) (Proyecto.getProyecto().getCantidadPersonas() - this.cantidadIndividuos);
                this.espacioEvacuacionPersona /= (float) (Proyecto.getProyecto().getCantidadPersonas() - this.cantidadIndividuos);
                this.tiempoExposicionPersona /= (float) (Proyecto.getProyecto().getCantidadPersonas() - this.cantidadIndividuos);

                for (int q = 0; q < Proyecto.getProyecto().getSalidas().size(); q++) {//VOY SUMANDO QUE INDIVIDUOS DE QUE TIPO
                    for (int x = 0; x < 10; x++) {                                    //SALIERON PORQUE PUERTA CRISTIAN 23-03-2016
                        if (((int[]) contadorSalidas.get(q))[x] != 0) {
                            ((int[]) contadorSalidasTotal.get(q))[x] += ((int[]) contadorSalidas.get(q))[x];
                        }
                    }
                }

                this.datosCorridas.add(new Corrida(this.corridas, this.timeSteps, this.tiempoEvacuacion, this.tiempoEvacuacionPersona, this.espacioEvacuacionPersona, this.tiempoExposicionPersona, this.cantidadIndividuosEvacuaron, this.cantidadIndividuosCaidos));
            }
            ++numeroCorrida;
        }
        if (this.ventana == 1) {
            this.tiempoEvacuacion = 0.0f;
            this.tiempoEvacuacionPersona = 0.0f;
            this.espacioEvacuacionPersona = 0.0f;
            this.tiempoExposicionPersona = 0.0f;
            this.it = this.datosCorridas.listIterator();
            while (this.it.hasNext()) {
                this.corrida = (Corrida) this.it.next();
                this.tiempoEvacuacion += this.corrida.getTiempoEvacuacion();
                this.tiempoEvacuacionPersona += this.corrida.getTiempoEvacuacionPersona();
                this.espacioEvacuacionPersona += this.corrida.getEspacioPersona();
                this.tiempoExposicionPersona += this.corrida.getTiempoExposicionPersona();
                this.cantidadIndividuosEvacuaronTotal += this.corrida.getCantidadIndividuosEvacuados();
                this.cantidadIndividuosCaidosTotal += this.corrida.getCantidadIndividuosCaidos();
            }
            this.tiempoEvacuacion /= this.corridas;
            this.tiempoEvacuacionPersona /= this.corridas;
            this.espacioEvacuacionPersona /= this.corridas;
            this.tiempoExposicionPersona /= this.corridas;
            while (this.it.hasPrevious()) {
                this.corrida = (Corrida) this.it.previous();
                this.varianzaTiempoEvacuacion = (this.varianzaTiempoEvacuacion + Math.pow(this.corrida.getTiempoEvacuacion() - this.tiempoEvacuacion, 2.0));
                this.varianzaTiempoEvacuacionPersona = (this.varianzaTiempoEvacuacionPersona + Math.pow(this.corrida.getTiempoEvacuacionPersona() - this.tiempoEvacuacionPersona, 2.0));
                this.varianzaEspacioEvacuacionPersona = (this.varianzaEspacioEvacuacionPersona + Math.pow(this.corrida.getEspacioPersona() - this.espacioEvacuacionPersona, 2.0));
                this.varianzaTiempoExposicionPersona = (this.varianzaTiempoExposicionPersona + Math.pow(this.corrida.getTiempoExposicionPersona() - this.tiempoExposicionPersona, 2.0));
            }

            this.desviacionEstandarTiempoEvacuacion = Math.pow(this.varianzaTiempoEvacuacion, 0.5);
            this.desviacionEstandarTiempoEvacuacionPersona = Math.pow(this.varianzaTiempoEvacuacionPersona, 0.5);
            this.desviacionEstandarEspacioEvacuacionPersona = Math.pow(this.varianzaEspacioEvacuacionPersona, 0.5);
            this.desviacionEstandarTiempoExposicionPersona = Math.pow(this.varianzaTiempoExposicionPersona, 0.5);

            datos = "";
            datos = datos + "Promedio de Individuos Evacuados: " + numberFormat.format(this.cantidadIndividuosEvacuaronTotal / (this.corridas)) + "    Promedio de Individuos Caidos: " + this.cantidadIndividuosCaidosTotal / (float) (this.corridas) + "\n";
            datos = datos + "MdM Tiempo Total de Evacuación: " + numberFormat.format(((double) this.tiempoEvacuacion)) + "\n";
            datos = datos + " Intervalo de Confianza de 90% \n";
            datos = datos + " Tiempo Total de Evacuaci\u00f3n: [  " + numberFormat.format(this.tiempoEvacuacion - (1.645 * (this.desviacionEstandarTiempoEvacuacion / Math.pow(this.corridas, 0.5)))) + " - " + numberFormat.format(this.tiempoEvacuacion + (1.645 * (this.desviacionEstandarTiempoEvacuacion / Math.pow(this.corridas, 0.5)))) + "  ]\n";
            datos = datos + " Tpo. Medio de Evacuaci\u00f3n: [  " + numberFormat.format(this.tiempoEvacuacionPersona - (1.645 * this.desviacionEstandarTiempoEvacuacionPersona / Math.pow(this.corridas, 0.5))) + " - " + numberFormat.format(this.tiempoEvacuacionPersona + (1.645 * this.desviacionEstandarTiempoEvacuacionPersona / Math.pow(this.corridas, 0.5))) + "  ]\n";
            datos = datos + " Esp. Medio Recorrido: [  " + numberFormat.format(this.espacioEvacuacionPersona - (1.645 * this.desviacionEstandarEspacioEvacuacionPersona / Math.pow(this.corridas, 0.5))) + " - " + numberFormat.format(this.espacioEvacuacionPersona + (1.645 * this.desviacionEstandarEspacioEvacuacionPersona / Math.pow(this.corridas, 0.5))) + "  ]\n";
            datos = datos + " =========================================================== \n";
            datos = datos + " Intervalo de Confianza de 95% \n";
            datos = datos + " Tiempo Total de Evacuaci\u00f3n: [  " + numberFormat.format(this.tiempoEvacuacion - (1.96 * (this.desviacionEstandarTiempoEvacuacion / Math.pow(this.corridas, 0.5)))) + " - " + numberFormat.format(this.tiempoEvacuacion + (1.96 * (this.desviacionEstandarTiempoEvacuacion / Math.pow(this.corridas, 0.5)))) + "  ]\n";
            datos = datos + " Tpo. Medio de Evacuaci\u00f3n: [  " + numberFormat.format(this.tiempoEvacuacionPersona - (1.96 * this.desviacionEstandarTiempoEvacuacionPersona / Math.pow(this.corridas, 0.5))) + " - " + numberFormat.format(this.tiempoEvacuacionPersona + (1.96 * this.desviacionEstandarTiempoEvacuacionPersona / Math.pow(this.corridas, 0.5))) + "  ]\n";
            datos = datos + " Esp. Medio Recorrido: [  " + numberFormat.format(this.espacioEvacuacionPersona - (1.96 * this.desviacionEstandarEspacioEvacuacionPersona / Math.pow(this.corridas, 0.5))) + " - " + numberFormat.format(this.espacioEvacuacionPersona + (1.96 * this.desviacionEstandarEspacioEvacuacionPersona / Math.pow(this.corridas, 0.5))) + "  ]\n";
            datos = datos + " =========================================================== \n";
            datos = datos + " Intervalo de Confianza de 99% \n";
            datos = datos + " Tiempo Total de Evacuaci\u00f3n: [  " + numberFormat.format(this.tiempoEvacuacion - (2.575 * (this.desviacionEstandarTiempoEvacuacion / Math.pow(this.corridas, 0.5)))) + " - " + numberFormat.format(this.tiempoEvacuacion + (2.575 * (this.desviacionEstandarTiempoEvacuacion / Math.pow(this.corridas, 0.5)))) + "  ]\n";
            datos = datos + " Tpo. Medio de Evacuaci\u00f3n: [  " + numberFormat.format(this.tiempoEvacuacionPersona - (2.575 * this.desviacionEstandarTiempoEvacuacionPersona / Math.pow(this.corridas, 0.5))) + " - " + numberFormat.format(this.tiempoEvacuacionPersona + (2.575 * this.desviacionEstandarTiempoEvacuacionPersona / Math.pow(this.corridas, 0.5))) + "  ]\n";
            datos = datos + " Esp. Medio Recorrido: [  " + numberFormat.format(this.espacioEvacuacionPersona - (2.575 * this.desviacionEstandarEspacioEvacuacionPersona / Math.pow(this.corridas, 0.5))) + " - " + numberFormat.format(this.espacioEvacuacionPersona + (2.575 * this.desviacionEstandarEspacioEvacuacionPersona / Math.pow(this.corridas, 0.5))) + "  ]\n";
            datos = datos + " =========================================================== \n";
            datos = datos + "\n";

            for (int q = 0; q < Proyecto.getProyecto().getSalidas().size(); q++) {
                int total = 0;
                //Point salida = ((Point)((Salida)Proyecto.getProyecto().getSalidas().get(q)).getNodos().getFirst());
                for (int x = 0; x < 10; x++) {
                    if (((int[]) contadorSalidasTotal.get(q))[x] != 0) {
                        total = total + ((int[]) contadorSalidasTotal.get(q))[x];
                    }
                }
                //datos = datos + "SALIDA " + q + " ("+ salida.x + ","+ salida.y +") TOTAL: " + total/(float)this.corridas + "\n";

                datos = datos + " ---- Salida: " + q + "    Cant. Individuos: " + total / (float) this.corridas + "  ----\n";
                for (int x = 0; x < 10; x++) {
                    if (((int[]) contadorSalidasTotal.get(q))[x] != 0) {
                        datos = datos + "Tipo: " + x + "   Cantidad: " + ((int[]) contadorSalidasTotal.get(q))[x] / (float) this.corridas + "\n";
                        //datos = datos + "Individuos Tipo " + x + ": " + ((int[])contadorSalidasTotal.get(q))[x]/(float)this.corridas + "\n";
                    }
                }
            }
            VentanaSimulacion.getVentanaSimulacion().getFinales().setText(datos);
            this.datosIntervalos = datos;
        }
        if (this.ventana == 2) {
            String linferior = "";
            String lsuperior = "";
            this.tiempoEvacuacion = 0.0f;
            this.tiempoEvacuacionPersona = 0.0f;
            this.espacioEvacuacionPersona = 0.0f;
            this.tiempoExposicionPersona = 0.0f;
            this.it = this.datosCorridas.listIterator();
            while (this.it.hasNext()) {
                this.corrida = (Corrida) this.it.next();
                this.tiempoEvacuacion += this.corrida.getTiempoEvacuacion();
                this.tiempoEvacuacionPersona += this.corrida.getTiempoEvacuacionPersona();
                this.espacioEvacuacionPersona += this.corrida.getEspacioPersona();
                this.tiempoExposicionPersona += this.corrida.getTiempoExposicionPersona();
                this.cantidadIndividuosEvacuaronTotal += this.corrida.getCantidadIndividuosEvacuados();
                this.cantidadIndividuosCaidosTotal += this.corrida.getCantidadIndividuosCaidos();
            }
            this.tiempoEvacuacion /= (float) this.corridas;
            this.tiempoEvacuacionPersona /= (float) this.corridas;
            this.espacioEvacuacionPersona /= (float) this.corridas;
            this.tiempoExposicionPersona /= (float) this.corridas;
            while (this.it.hasPrevious()) {
                this.corrida = (Corrida) this.it.previous();
                this.varianzaTiempoEvacuacion = (float) ((double) this.varianzaTiempoEvacuacion + Math.pow(this.corrida.getTiempoEvacuacion() - this.tiempoEvacuacion, 2.0));
                this.varianzaTiempoEvacuacionPersona = (float) ((double) this.varianzaTiempoEvacuacionPersona + Math.pow(this.corrida.getTiempoEvacuacionPersona() - this.tiempoEvacuacionPersona, 2.0));
                this.varianzaEspacioEvacuacionPersona = (float) ((double) this.varianzaEspacioEvacuacionPersona + Math.pow(this.corrida.getEspacioPersona() - this.espacioEvacuacionPersona, 2.0));
                this.varianzaTiempoExposicionPersona = (float) ((double) this.varianzaTiempoExposicionPersona + Math.pow(this.corrida.getTiempoExposicionPersona() - this.tiempoExposicionPersona, 2.0));
            }

            this.desviacionEstandarTiempoEvacuacion = Math.pow(this.varianzaTiempoEvacuacion, 0.5);
            this.desviacionEstandarTiempoEvacuacionPersona = Math.pow(this.varianzaTiempoEvacuacionPersona, 0.5);
            this.desviacionEstandarEspacioEvacuacionPersona = Math.pow(this.varianzaEspacioEvacuacionPersona, 0.5);
            this.desviacionEstandarTiempoExposicionPersona = Math.pow(this.varianzaTiempoExposicionPersona, 0.5);

            linferior = numberFormat.format(this.tiempoEvacuacion - (2.575 * (this.desviacionEstandarTiempoEvacuacion / Math.pow(this.corridas, 0.5))));
            lsuperior = numberFormat.format(this.tiempoEvacuacion + (2.575 * (this.desviacionEstandarTiempoEvacuacion / Math.pow(this.corridas, 0.5))));

            datos = "";
            for (int q = 0; q < Proyecto.getProyecto().getSalidas().size(); q++) {
                int total = 0;
                //Point salida = ((Point)((Salida)Proyecto.getProyecto().getSalidas().get(q)).getNodos().getFirst());
                for (int x = 0; x < 10; x++) {
                    if (((int[]) contadorSalidasTotal.get(q))[x] != 0) {
                        total = total + ((int[]) contadorSalidasTotal.get(q))[x];
                    }
                }


                datos = datos + "S" + q + ": " + total / (float) this.corridas + " (";
                for (int x = 0; x < 10; x++) {
                    if (((int[]) contadorSalidasTotal.get(q))[x] != 0) {
                        datos = datos + "T" + x + ": " + ((int[]) contadorSalidasTotal.get(q))[x] / (float) this.corridas + " ,";
                        //datos = datos + "Individuos Tipo " + x + ": " + ((int[])contadorSalidasTotal.get(q))[x]/(float)this.corridas + "\n";
                    }
                }
                datos = datos + ")";
            }

            VentanaBatch.getVentanaBatch().getBatchActual().setEvacuadosCaidos("Evacuados:" + ((double) this.cantidadIndividuosEvacuaronTotal / (double) (this.corridas)) + "  Caidos:" + ((double) this.cantidadIndividuosCaidosTotal / (float) (this.corridas)));
            VentanaBatch.getVentanaBatch().getBatchActual().setDescripcionPuertas(datos);
            VentanaBatch.getVentanaBatch().getBatchActual().setResultadoProyecto(Double.toString(this.tiempoEvacuacion));
            VentanaBatch.getVentanaBatch().getBatchActual().setIntervaloEvacuacion99("[" + linferior + " - " + lsuperior + "]");
            VentanaBatch.getVentanaBatch().getBatchActual().setEspacioMedioRecorrido("[" + numberFormat.format(this.espacioEvacuacionPersona - (2.575 * this.desviacionEstandarEspacioEvacuacionPersona / Math.pow(this.corridas, 0.5))) + " - " + numberFormat.format(this.espacioEvacuacionPersona + (2.575 * this.desviacionEstandarEspacioEvacuacionPersona / Math.pow(this.corridas, 0.5))) + "]");
            VentanaBatch.getVentanaBatch().getBatchActual().setTiempoMedioEvacuacionPersona("[" + numberFormat.format(this.tiempoEvacuacionPersona - (2.575 * this.desviacionEstandarTiempoEvacuacionPersona / Math.pow(this.corridas, 0.5))) + " - " + numberFormat.format(this.tiempoEvacuacionPersona + (2.575 * this.desviacionEstandarTiempoEvacuacionPersona / Math.pow(this.corridas, 0.5))) + "]");
            //VentanaBatch.getVentanaBatch().reiniciar();
        }

        if (this.ventana == 0 || this.ventana == 1) {
            if (!parar) { //SI EL THREAD NO HA SIDO DETENIDO VOLUNTARIAMENTE QUE MUESTRE EL CARTEL
                JOptionPane.showMessageDialog(new JFrame(), "FIN DE LA SIMULACION", "ANIMACION", 1);
            }
        }
    }

    public void pausar() {
        pausado = true;
        System.out.println("Thread Pausado");
    }

    public synchronized void reiniciar() {
        pausado = false;
        notify();
        System.out.println("Thread Retomado");
    }

    public synchronized void parar() {
        parar = true;
        pausado = false;
        notify();
        System.out.println("Thread Parado");
    }

    public int getSalidasDisponibles() {
        return salidasDisponibles;
    }

    public void setSalidasDisponibles(int salidasDisponibles) {
        this.salidasDisponibles = salidasDisponibles;
        this.salidasDisponiblesAnteriores = this.salidasDisponibles;
    }

    public void addFactorSalidas(int salida, Map<String, Integer> aux) {
        this.factorSalidas.put(salida, aux);
    }

    public void initMapaAgentePorSalida(int salida) {
        this.mapaAgentePorSalida.put(salida, new LinkedList());
    }

    public AutomataCelular getAc1() {
        return ac1;
    }

    public Map<Integer, Map<String, Integer>> getFactorSalidas() {
        return factorSalidas;
    }

    public void addPropertisFactorSalida(int salida, String key, int value) {
        this.factorSalidas.get(salida).put(key, value);
    }

    public void addMapaAgentePorSalida(int salida, int idAgente ) {
        this.mapaAgentePorSalida.get(salida).add(idAgente);

    }
}