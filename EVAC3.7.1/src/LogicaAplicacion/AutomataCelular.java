
package LogicaAplicacion;

public class AutomataCelular {
    private Celda[][] celdas;
    private int ancho;
    private int alto;
    

    public AutomataCelular() {
        this.celdas = null;
        this.ancho = 0;
        this.alto = 0;
    }

    public Celda[][] getCeldas() {
        return celdas;
    }

    public void setCeldas(Celda[][] celdas) {
        this.celdas = celdas;
    }

    
    public AutomataCelular(int alto, int ancho) {
        
        this.alto=alto;
        this.ancho=ancho;
        this.celdas = new Celda[alto][ancho];
        this.ancho = ancho;
        this.alto = alto;
        for (int i = 0; i < alto; ++i) {
            for (int j = 0; j < ancho; ++j) {
                this.celdas[i][j] = new Celda();
            }
        }
    }

    public boolean setCelda(int fila, int columna, Celda c) {
        if (fila <= this.getAlto() - 1 && columna <= this.getAncho() - 1) {
            this.celdas[fila][columna] = c;
            return true;
        }
        return false;
    }

    public Celda getCelda(int fila, int columna) {
        if (this.celdas[fila][columna] == null) {
            return null;
        }
        return this.celdas[fila][columna];
    }

    public int getAlto() {
        return this.alto;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }

    public int getAncho() {
        return this.ancho;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }
}

