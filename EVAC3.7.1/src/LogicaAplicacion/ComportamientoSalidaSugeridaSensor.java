package LogicaAplicacion;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Random;

//Daniel Larregola 16-03-2019
public class ComportamientoSalidaSugeridaSensor extends Comportamiento {

    private int salidaSugeridaSensor(Point nodo, AutomataCelular ac) {
        return ac.getCelda(nodo.y, nodo.x).getAgente().getSalidaSugeridaSensor();
    }

    @Override
    public boolean ejecutarComportamiento(AutomataCelular ac, AutomataCelular ac2, AutomataCelular acAmbiente, Point nodo) {
        //Modificar comportamiento por el momento me sirve este.
        ComportamientoSalidaMejorTiempo comp = new ComportamientoSalidaMejorTiempo();
        return comp.ejecutarComportamiento(ac,ac2,acAmbiente,nodo);
    }
}
