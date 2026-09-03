
package proyectolista;


public class Nodo {
    private boolean sw;
    private Nodo ligalista;
    private Persona info;
    private Nodo liga;

    public Nodo(Persona info) {
    this.info = info;
    this.sw = false;
    this.ligalista = null;
    this.liga = null;
}

    public boolean isSw() {
        return sw;
    }

    public void setSw(boolean sw) {
        this.sw = sw;
    }

    public Nodo getLigalista() {
        return ligalista;
    }

    public void setLigalista(Nodo ligalista) {
        this.ligalista = ligalista;
    }

    public Persona getInfo() {
        return info;
    }

    public void setInfo(Persona info) {
        this.info = info;
    }

    public Nodo getLiga() {
        return liga;
    }

    public void setLiga(Nodo liga) {
        this.liga = liga;
    }
}
    