package proyectolista;

/**
 *
 * @author ASUS
 */
public class Lista {

    private Nodo punta;

    public Lista(Nodo punta) {
        this.punta = null;
    }

    public Nodo getPunta() {
        return punta;
    }

    public void setPunta(Nodo punta) {
        this.punta = punta;
    }

    public void MostrarDatos() {
        if (punta == null) {
            System.out.println("\nLista Vacia \n");
        } else {
            Nodo p = punta;
            System.out.println(" ");
            while (p != null) {

                if (p != punta) {
                    System.out.print(" - ");
                }
                System.out.print(p.getDato());
                p = p.getliga();
            }
            System.out.println(" ");
        }
    }

    public void InsertarFinal(int dato) {
        Nodo nuevo = new Nodo(dato);
        Nodo p = punta;
        if (punta != null) {
            while (p.getliga() != null) {
                p = p.getliga();
            }
            p.setliga(nuevo);
        } else {
            punta = nuevo;
        }
    }

    public void InsertarEnMedio(int dato) {
        Nodo nuevo = new Nodo(dato);
        Nodo p = punta;
        Nodo q = punta;
        if (punta != null) {
            while (p.getDato() < nuevo.getDato()) {
                q = p;
                p = p.getliga();
            }
            nuevo.setliga(p);
            q.setliga(nuevo);
        } else {
            punta = nuevo;
        }
    }

    public void BorrarInicio() {
        punta = punta.getliga();
    }

    public void BorrarAlFinal() {
        Nodo p = punta;
        Nodo q = punta;
        while (p.getliga() != null) {
            q = p;
            p = p.getliga();
        }
        q.setliga(null);
    }

    public void BorrarEnMedio(int dato) {
        Nodo p = punta;
        Nodo q = punta;
        while (p.getDato() != dato) {
            q = p;
            p = p.getliga();
        }
        q.setliga(p.getliga());
        p.setliga(null);
    }

    public void InsertarInicio(int dato) {
        Nodo nuevo = new Nodo(dato);
        nuevo.setliga(punta);
        punta = nuevo;
    }
}
