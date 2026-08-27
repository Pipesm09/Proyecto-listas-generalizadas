
package proyectolista;


public class nodog {
    private boolean sw;
    private nodog ligalista;
    private Object info;
    private nodog liga;

    public nodog(Object info) {
        this.info = info;
        
    }

    public boolean isSw() {
        return sw;
    }

    public void setSw(boolean sw) {
        this.sw = sw;
    }

    public nodog getLigalista() {
        return ligalista;
    }

    public void setLigalista(nodog ligalista) {
        this.ligalista = ligalista;
    }

    public Object getInfo() {
        return info;
    }

    public void setInfo(Object info) {
        this.info = info;
    }

    public nodog getLiga() {
        return liga;
    }

    public void setLiga(nodog liga) {
        this.liga = liga;
    }
    
    
    
    
}
