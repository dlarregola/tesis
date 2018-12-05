/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicaAplicacion;

/**
 *
 * @author Cristian
 */
public class Batch {
    private String rutaProyecto;
    private String resultadoProyecto; //Media de Medias del tiempode evacuación
    private String intervaloEvacuacion99; //intervalos de confianza de tiempo de evacuacion total al 95%
    private String cantidadIndividuosEvacuados;
    private String cantidadIndividuosCaidos;
    private String descripcionPuertas;
    private String evacuadosCaidos;
    private String tiempoMedioEvacuacionPersona;
    private String espacioMedioRecorrido;
            
    public Batch(){
        this.rutaProyecto="";
        this.resultadoProyecto="";
        this.intervaloEvacuacion99="";
    }
    public Batch(String ruta, String resultado, String intervalo){
        this.rutaProyecto=ruta;
        this.resultadoProyecto=resultado;
        this.intervaloEvacuacion99=intervalo;
    }
    public String getRutaProyecto() {
        return rutaProyecto;
    }

    public void setRutaProyecto(String rutaProyecto) {
        this.rutaProyecto = rutaProyecto;
    }

    public String getResultadoProyecto() {
        return resultadoProyecto;
    }

    public void setResultadoProyecto(String resultadoProyecto) {
        this.resultadoProyecto = resultadoProyecto;
    }

    public String getIntervaloEvacuacion99() {
        return intervaloEvacuacion99;
    }

    public void setIntervaloEvacuacion99(String intervaloEvacuacion95) {
        this.intervaloEvacuacion99 = intervaloEvacuacion95;
    }

    public String getCantidadIndividuosEvacuados() {
        return cantidadIndividuosEvacuados;
    }

    public void setCantidadIndividuosEvacuados(String cantidadIndividuosEvacuados) {
        this.cantidadIndividuosEvacuados = cantidadIndividuosEvacuados;
    }

    public String getCantidadIndividuosCaidos() {
        return cantidadIndividuosCaidos;
    }

    public void setCantidadIndividuosCaidos(String cantidadIndividuosCaidos) {
        this.cantidadIndividuosCaidos = cantidadIndividuosCaidos;
    }

    public String getDescripcionPuertas() {
        return descripcionPuertas;
    }

    public void setDescripcionPuertas(String descripcionPuertas) {
        this.descripcionPuertas = descripcionPuertas;
    }

    public String getEvacuadosCaidos() {
        return evacuadosCaidos;
    }

    public void setEvacuadosCaidos(String evacuadosCaidos) {
        this.evacuadosCaidos = evacuadosCaidos;
    }

    public String getTiempoMedioEvacuacionPersona() {
        return tiempoMedioEvacuacionPersona;
    }

    public void setTiempoMedioEvacuacionPersona(String tiempoMedioEvacuacionPersona) {
        this.tiempoMedioEvacuacionPersona = tiempoMedioEvacuacionPersona;
    }

    public String getEspacioMedioRecorrido() {
        return espacioMedioRecorrido;
    }

    public void setEspacioMedioRecorrido(String espacioMedioRecorrido) {
        this.espacioMedioRecorrido = espacioMedioRecorrido;
    }

    
    
}
