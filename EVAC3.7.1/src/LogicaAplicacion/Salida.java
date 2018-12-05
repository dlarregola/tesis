
package LogicaAplicacion;

import java.awt.Point;
import java.util.LinkedList;

public class Salida {
    private LinkedList nodos = new LinkedList();
    private int numeroSalida;

    public int getNumeroSalida() {
        return this.numeroSalida;
    }

    public void setNumeroSalida(int numeroSalida) {
        this.numeroSalida = numeroSalida;
    }

    public LinkedList getNodos() {
        return this.nodos;
    }

    public void setNodos(LinkedList nodos) {
        this.nodos = nodos;
    }

    public void agregarNodo(Point punto) {
        this.nodos.addLast(punto);
    }
}

