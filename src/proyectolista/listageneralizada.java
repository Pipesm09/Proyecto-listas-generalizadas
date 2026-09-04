package proyectolista;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;

public class listageneralizada {

    private Nodo raiz;

    public listageneralizada() {
        this.raiz = null;
    }

    public Nodo getRaiz() {
        return raiz;
    }

    public void setRaiz(Nodo raiz) {
        this.raiz = raiz;
    }

    public void registrarRaiz(String cedula, String fechaNacimiento, String nombre) {

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fecha = LocalDate.parse(fechaNacimiento, formato);

        Persona persona = new Persona(cedula, fecha, nombre);

        Nodo nuevo = new Nodo(persona);

        if (raiz == null) {
            raiz = nuevo;
        } else {
            System.out.println("Ya existe una raíz.");
        }
    }

    public void registrarPersona(String cedula, String fechaNacimiento,
            String nombre, String cedulaPadre) {

        //Configuración para la vuelta de las fechas de nacimiento
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate fecha = LocalDate.parse(fechaNacimiento, formato);
        
        // Guarda los datos ingresados en la clase persona
        Persona persona = new Persona(cedula, fecha, nombre);
        // Y ahora los mete dentro del nodo del arbol
        Nodo nuevo = new Nodo(persona);
        // Busca cual es el padre ingresado para ubicar el nodo
        Nodo padre = buscarNodo(raiz, cedulaPadre);

        if (padre == null) {
            JOptionPane.showConfirmDialog(null,"No se encontró la persona.");
            return;
        }

        // Si no tiene hijos
        if (padre.getLigalista() == null) {
            padre.setLigalista(nuevo);
            return;
        }

        // Si la nueva persona es mayor que el primer hijo
        if (fecha.isBefore(
                padre.getLigalista().getInfo().getFechaNacimiento())) {

            nuevo.setLiga(padre.getLigalista());
            padre.setLigalista(nuevo);
            return;
        }

        // Buscar dónde insertarla
        Nodo anterior = padre.getLigalista();
        Nodo actual = anterior.getLiga();

        while (actual != null
                && actual.getInfo().getFechaNacimiento().isBefore(fecha)) {

            anterior = actual;
            actual = actual.getLiga();
        }

        nuevo.setLiga(actual);
        anterior.setLiga(nuevo);
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

}
