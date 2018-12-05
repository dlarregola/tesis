package LogicaAplicacion;

import java.awt.*;

public class Sensor {

    private int potencia;
    private Point ubicacion;



    public Sensor(int potencia) {
        this.potencia = potencia;
        this.ubicacion = new Point();

    }


    public int getPotencia() {
        return potencia;
    }

    public void setPotencia(int potencia) {
        this.potencia = potencia;
    }

    public Point getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Point ubicacion) {
        this.ubicacion = ubicacion;
    }
}
