package proyectolista;

public class listageneralizada {

    private Nodo raiz;

    public Nodo getRaiz() {
        return raiz;
    }

    public void setRaiz(Nodo raiz) {
        this.raiz = null;
    }

    public void construirArbolGenealogico(String cedula, String fechaNacimiento, String nombre) {

        Persona persona = new Persona(cedula, fechaNacimiento, nombre);

        Nodo nuevo = new Nodo(persona);

        if (raiz == null) {
            raiz = nuevo;
        }
    }

    public static void visualizarArbol(Nodo x, int nivel) {
        if (x == null) {
            return;
        }
        if (!x.isSw()) {
            Persona p = x.getInfo();
            imprimirSangria(nivel);
            System.out.println("└── " + p.getNombre() + " (ID: " + p.getCedula() + ")");
        }

        Nodo aux = x.getLiga();
        while (aux != null) {
            if (!aux.isSw()) {
                Persona p = aux.getInfo();
                imprimirSangria(nivel + 1);
                System.out.println("├── " + p.getNombre() + " (ID: " + p.getCedula() + ")");
            } else {
                visualizarArbol(aux.getLigalista(), nivel + 1);
            }
            aux = aux.getLiga();
        }
    }

    private static void imprimirSangria(int nivel) {
        for (int i = 0; i < nivel; i++) {
            System.out.print("    ");
        }
    }

    public static void imprimirComoLista(Nodo x) {
        if (x == null) {
            System.out.print("()");
            return;
        }

        System.out.print("(");
        Nodo aux = x;
        while (aux != null) {
            if (!aux.isSw()) {
                Persona p = (Persona) aux.getInfo();
                System.out.print(p.getNombre());
            } else {
                imprimirComoLista(aux.getLigalista());
            }

            aux = aux.getLiga();
            if (aux != null) {
                System.out.print(",");
            }
        }
        System.out.print(")");
    }

    public void registrarPersona(String cedula, String fechaNacimiento,
            String nombre, String cedulaPadre) {

        Persona persona = new Persona(cedula, fechaNacimiento, nombre);
        Nodo nuevo = new Nodo(persona);
        nuevo.setSw(false);

        // Si todavía no existe la raíz
        if (raiz == null) {
            raiz = nuevo;
            return;
        }

        // Buscar al padre/madre por su cédula
        Nodo padre = buscarNodo(raiz, cedulaPadre);

        if (padre == null) {
            System.out.println("No se encontró la persona con esa cédula.");
            return;
        }

        // Si el padre todavía no tiene hijos
        if (padre.getLigalista() == null) {
            padre.setLigalista(nuevo);
        } else {
            // Ir hasta el último hijo
            Nodo aux = padre.getLigalista();

            while (aux.getLiga() != null) {
                aux = aux.getLiga();
            }

            aux.setLiga(nuevo);
        }
    }

    public Nodo buscarNodo(Nodo actual, String cedula) {

        if (actual == null) {
            return null;
        }

        if (!actual.isSw()
                && actual.getInfo().getCedula().equals(cedula)) {
            return actual;
        }

        Nodo encontrado = buscarNodo(actual.getLigalista(), cedula);

        if (encontrado != null) {
            return encontrado;
        }

        return buscarNodo(actual.getLiga(), cedula);
    }
}
