
package LogicaAplicacion;

public class DistanciaSalida {
    private Double distancia;
    private int salida;

    public DistanciaSalida() {
        this.distancia = -1.0;
        this.salida = -1;
    }

    public DistanciaSalida(int salida, Double distancia) {
        this.distancia = distancia;
        this.salida = salida;
    }

    public Double getDistancia() {
        return this.distancia;
    }

    public void setDistancia(Double distancia) {
        this.distancia = distancia;
    }

    public int getSalida() {
        return this.salida;
    }

    public void setSalida(int salida) {
        this.salida = salida;
    }
}

