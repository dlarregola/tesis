
package LogicaAplicacion;

public class Contador {
    int valor;

    public Contador(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return this.valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public void incrementarContador() {
        ++this.valor;
    }
    

    public void inicializarContador() {
        this.valor = 0;
    }
}

