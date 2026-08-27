
package proyectolista;


public class Nodo {
    private boolean sw;
    private Nodo ligalista;
    private Object info;
    private Nodo liga;

    public Nodo(Object info) {
        this.info = info;
        
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

    public Object getInfo() {
        return info;
    }

    public void setInfo(Object info) {
        this.info = info;
    }

    public Nodo getLiga() {
        return liga;
    }

    public void setLiga(Nodo liga) {
        this.liga = liga;
    }
}
    