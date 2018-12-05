/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  evacesquiva.Corrida
 */
package LogicaAplicacion;

public class Corrida {
    private int corrida;
    private int timeSteps;
    private double tiempoEvacuacion;
    private double tiempoEvacuacionPersona;
    private double espacioPersona;
    private double tiempoExposicionPersona;
    private double cantidadIndividuosEvacuados;
    private double cantidadIndividuosCaidos;

    public Corrida(int corrida, int timeSteps, double tiempoEvacuacion, double tiempoEvacuacionPersona, double espacioPersona, double tiempoExposicionPersona, int cantidadIndividuos, int cantidadIndividuosCaidos) {
        this.corrida = corrida;
        this.timeSteps = timeSteps;
        this.tiempoEvacuacion = tiempoEvacuacion;
        this.tiempoEvacuacionPersona = tiempoEvacuacionPersona;
        this.espacioPersona = espacioPersona;
        this.tiempoExposicionPersona = tiempoExposicionPersona;
        this.cantidadIndividuosEvacuados=cantidadIndividuos;
        this.cantidadIndividuosCaidos=cantidadIndividuosCaidos;
    }

    public int getCorrida() {
        return this.corrida;
    }

    public void setCorrida(int corrida) {
        this.corrida = corrida;
    }

    public double getEspacioPersona() {
        return this.espacioPersona;
    }

    public void setEspacioPersona(float espacioPersona) {
        this.espacioPersona = espacioPersona;
    }

    public double getTiempoEvacuacion() {
        return this.tiempoEvacuacion;
    }

    public void setTiempoEvacuacion(float tiempoEvacuacion) {
        this.tiempoEvacuacion = tiempoEvacuacion;
    }

    public double getTiempoEvacuacionPersona() {
        return this.tiempoEvacuacionPersona;
    }

    public void setTiempoEvacuacionPersona(float tiempoEvacuacionPersona) {
        this.tiempoEvacuacionPersona = tiempoEvacuacionPersona;
    }

    public double getTiempoExposicionPersona() {
        return this.tiempoExposicionPersona;
    }

    public void setTiempoExposicionPersona(float tiempoExposicionPersona) {
        this.tiempoExposicionPersona = tiempoExposicionPersona;
    }

    public int getTimeSteps() {
        return this.timeSteps;
    }

    public void setTimeSteps(int timeSteps) {
        this.timeSteps = timeSteps;
    }

    public double getCantidadIndividuosEvacuados() {
        return cantidadIndividuosEvacuados;
    }

    public void setCantidadIndividuosEvacuados(float cantidadIndividuosEvacuados) {
        this.cantidadIndividuosEvacuados = cantidadIndividuosEvacuados;
    }

    public double getCantidadIndividuosCaidos() {
        return cantidadIndividuosCaidos;
    }

    public void setCantidadIndividuosCaidos(float cantidadIndividuosCaidos) {
        this.cantidadIndividuosCaidos = cantidadIndividuosCaidos;
    }
    
    
}

