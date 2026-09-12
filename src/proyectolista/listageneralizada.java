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

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fecha = LocalDate.parse(fechaNacimiento, formato);

        Persona persona = new Persona(cedula, fecha, nombre);
        Nodo nuevo = new Nodo(persona);

        Nodo padre = buscarNodo(raiz, cedulaPadre);

        if (padre == null) {
            JOptionPane.showMessageDialog(null,
                    "No se encontró al padre con cédula: " + cedulaPadre);
            return;
        }

        // Si el padre todavía no tiene hijos (su ligalista está vacío)
        if (padre.getLigalista() == null) {
            padre.setLigalista(nuevo);
            return;
        }

        // El primer hijo está directamente en la ligalista del padre
        Nodo primerHijo = padre.getLigalista();

        long cedulaNueva = Long.parseLong(cedula);
        long cedulaPrimerHijo = Long.parseLong(primerHijo.getInfo().getCedula());

        // Insertar como el nuevo primer hijo (si su cédula es menor)
        if (cedulaNueva < cedulaPrimerHijo) {
            nuevo.setLiga(primerHijo);
            padre.setLigalista(nuevo);
            return;
        }

        // Buscar la posición correcta entre los hermanos usando la 'liga'
        Nodo anterior = primerHijo;
        Nodo actual = anterior.getLiga();

        while (actual != null) {
            long cedulaActual = Long.parseLong(actual.getInfo().getCedula());

            if (cedulaNueva < cedulaActual) {
                break;
            }

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

    public void actualizarPersona(String cedulaActual, String nuevoNombre, String nuevaCedula, String nuevaFechaNacimiento) {
        Nodo nodoEcontrado = buscarNodo(raiz, cedulaActual);
        if (nodoEcontrado == null) {
            JOptionPane.showMessageDialog(null, "No se encontró ninguna persona con la cédula: " + cedulaActual);
            return;
        }
        Persona persona = nodoEcontrado.getInfo();
        if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
            persona.setNombre(nuevoNombre);
        }
        if (nuevaFechaNacimiento != null && !nuevaFechaNacimiento.trim().isEmpty()) {
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate nuevaFecha = LocalDate.parse(nuevaFechaNacimiento, formato);
            persona.setFechaNacimiento(nuevaFecha);
        }
        if (nuevaCedula != null && !nuevaCedula.trim().isEmpty()) {
            persona.setCedula(nuevaCedula);
        }
        JOptionPane.showMessageDialog(null, "Datos actualizados correctamente");
    }

    private Nodo encontrarHijoMayor(Nodo primerHijo) {
        if (primerHijo == null) {
            return null;
        }
        Nodo hijoMayor = primerHijo;
        Nodo actual = primerHijo.getLiga();
        //Recorrer si el nodo que esta como primerHijo es el mayor de todos
        while (actual != null) {
            if (actual.getInfo().getFechaNacimiento().isBefore(hijoMayor.getInfo().getFechaNacimiento())) {
                hijoMayor = actual;
            }
            actual = actual.getLiga();
        }
        return hijoMayor;
    }

    public void eliminarPersona(String cedulaAEliminar) {
        //eliminar la raiz xd
        if (raiz.getInfo().getCedula().equals(cedulaAEliminar)) {

            // Sub-caso A: La raíz no tiene hijos (es el único nodo del árbol)
            if (raiz.getLigalista() == null) {
                raiz = null; // Árbol completamente vacío
                JOptionPane.showMessageDialog(null, "Se ha eliminado la raíz. El árbol ahora está vacío.");
                return;
            }

            // Sub-caso B: La raíz tiene hijos. El hijo mayor asciende a ser la nueva Raíz.
            Nodo hijoMayor = encontrarHijoMayor(raiz.getLigalista());

            // Sacamos al hijo mayor de la lista horizontal de hermanos
            removerDeListaHermanos(raiz, hijoMayor);

            // El nuevo hijo mayor adopta a los demás hermanos restantes
            hijoMayor.setLiga(raiz.getLigalista());

            // Actualizamos la raíz global del árbol
            raiz = hijoMayor;

            JOptionPane.showMessageDialog(null, "Se ha eliminado la raíz anterior.\nEl hijo mayor ("
                    + hijoMayor.getInfo().getNombre() + ") es ahora el nuevo ancestro principal (Raíz).");
            return;
        }

        // Buscamos al padre del nodo que queremos eliminar para poder modificar su enlace 'ligalista' o 'liga'
        ResultadoBusqueda resultado = buscarNodoYPadre(null, raiz, cedulaAEliminar);

        if (resultado == null || resultado.nodoAEliminar == null) {
            JOptionPane.showMessageDialog(null, "No se encontró a la persona con cédula: " + cedulaAEliminar);
            return;
        }
        Nodo objetivo = resultado.nodoAEliminar;
        Nodo padreDirecto = resultado.padreDirecto;
        Nodo hermanoAnterior = resultado.hermanoAnterior;

        if (objetivo.getLigalista() == null) {
            desconectarNodo(padreDirecto, hermanoAnterior, objetivo);
            JOptionPane.showMessageDialog(null, "Persona eliminada con exito");
            return;
        }
        Nodo hijoMayor = encontrarHijoMayor(objetivo.getLigalista());
        removerDeListaHermanos(objetivo, hijoMayor);
        hijoMayor.setLiga(objetivo.getLigalista());

        if (padreDirecto != null) {
            padreDirecto.setLigalista(hijoMayor);
        } else if (hermanoAnterior != null) {
            hermanoAnterior.setLiga(hijoMayor);
        }

        JOptionPane.showMessageDialog(null, "Persona eliminada con exito\nEl hijo mayor ("
                + hijoMayor.getInfo().getNombre() + ") ha heredado su posición en la jerarquia");
    }

    private ResultadoBusqueda buscarNodoYPadre(Nodo padre, Nodo actual, String cedula) {
        if (actual == null) {
            return null;
        }

        if (!actual.isSw() && actual.getInfo().getCedula().equals(cedula)) {
            return new ResultadoBusqueda(actual, padre, null);
        }

        ResultadoBusqueda res = buscarNodoYPadre(actual, actual.getLigalista(), cedula);
        if (res != null) {
            return res;
        }

        ResultadoBusqueda resHermano = buscarNodoYPadre(padre, actual.getLiga(), cedula);
        if (resHermano != null) {
            if (resHermano.hermanoAnterior == null && actual.getLiga() == resHermano.nodoAEliminar) {
                resHermano.hermanoAnterior = actual;
            }
            return resHermano;
        }

        return null;
    }

    private void desconectarNodo(Nodo padre, Nodo hermanoAnterior, Nodo target) {
        if (padre != null && padre.getLigalista() == target) {
            padre.setLigalista(target.getLiga());
        } else if (hermanoAnterior != null) {
            hermanoAnterior.setLiga(target.getLiga());
        }
    }

    private void removerDeListaHermanos(Nodo padre, Nodo hijoAMover) {
        if (padre.getLigalista() == hijoAMover) {
            padre.setLigalista(hijoAMover.getLiga());
            return;
        }
        Nodo aux = padre.getLigalista();
        while (aux != null && aux.getLiga() != hijoAMover) {
            aux = aux.getLiga();
        }
        if (aux != null) {
            aux.setLiga(hijoAMover.getLiga());
        }
    }

    public void consultarRelaciones(Nodo raiz, String cedula) {

        Nodo persona = buscarNodo(raiz, cedula);

        if (persona == null) {
            System.out.println("La persona no existe.");
            return;
        }

        System.out.println("\n===== RELACIONES FAMILIARES =====");
        System.out.println("Persona consultada: "
                + persona.getInfo().getNombre());
        System.out.println("Cedula: "
                + persona.getInfo().getCedula());
        System.out.println("Fecha de nacimiento: "
                + persona.getInfo().getFechaNacimiento());

        // PADRE
        Nodo padre = buscarPadre(raiz, cedula);

        System.out.println("\nPADRE:");
        if (padre != null) {
            System.out.println(padre.getInfo().getNombre()
                    + " - " + padre.getInfo().getCedula());
        } else {
            System.out.println("No tiene padre.");
        }

        // HIJOS
        System.out.println("\nHIJOS:");
        mostrarHijos(persona);

        // HERMANOS
        System.out.println("\nHERMANOS:");
        if (padre != null) {
            mostrarHermanos(padre, cedula);
        } else {
            System.out.println("No tiene hermanos.");
        }

        /*
        // TIOS
        System.out.println("\nTIOS:");
        if (padre != null) {
            Nodo abuelo = buscarPadre(raiz, padre.getInfo().getCedula());

            if (abuelo != null) {
                mostrarHermanos(abuelo, padre.getInfo().getCedula());
            } else {
                System.out.println("No tiene tios.");
            }
        } else {
            System.out.println("No tiene tios.");
        }

        // SOBRINOS
        System.out.println("\nSOBRINOS:");
        mostrarSobrinos(raiz, persona, cedula);

        // PRIMOS
        System.out.println("\nPRIMOS:");
        if (padre != null) {
            Nodo abuelo = buscarPadre(raiz, padre.getInfo().getCedula());

            if (abuelo != null) {
                mostrarPrimos(abuelo, padre.getInfo().getCedula());
            } else {
                System.out.println("No tiene primos.");
            }
        } else {
            System.out.println("No tiene primos.");
        }

        // ANCESTROS
        System.out.println("\nANCESTROS:");
        mostrarAncestros(raiz, cedula);

        // DESCENDIENTES
        System.out.println("\nDESCENDIENTES:");
        mostrarDescendientes(persona);
         */
    }

    public Nodo buscarPadre(Nodo actual, String cedula) {
        if (actual == null) {
            return null;
        }

        // Revisamos si alguno de los hijos directos de 'actual' es la persona buscada
        Nodo hijo = actual.getLigalista();
        while (hijo != null) {
            if (!hijo.isSw() && hijo.getInfo().getCedula().equals(cedula)) {
                return actual; // ¡Encontramos al padre!
            }
            hijo = hijo.getLiga(); // Saltamos al hermano
        }

        // Si no está entre los hijos directos, buscamos recursivamente bajando por los hijos
        Nodo encontrado = buscarPadre(actual.getLigalista(), cedula);
        if (encontrado != null) {
            return encontrado;
        }

        // Si no, buscamos por los hermanos
        return buscarPadre(actual.getLiga(), cedula);
    }

    public void mostrarPadre(String cedula) {
        Nodo persona = buscarNodo(raiz, cedula);
        if (persona == null) {
            System.out.println("La persona no existe.");
            return;
        }

        Nodo padre = buscarPadre(raiz, cedula);

        System.out.println("\n--- PADRE DE " + persona.getInfo().getNombre() + " ---");
        if (padre != null) {
            System.out.println("Nombre: " + padre.getInfo().getNombre()
                    + " | Cédula: " + padre.getInfo().getCedula());
        } else {
            System.out.println("Es el ancestro principal (No tiene padre).");
        }
    }

    public boolean buscarEnSublista(Nodo actual, String cedula) {

        Nodo aux = actual;

        while (aux != null) {

            if (!aux.isSw()
                    && aux.getInfo().getCedula().equals(cedula)) {
                return true;
            }

            aux = aux.getLiga();
        }

        return false;
    }

    public void mostrarHijos(Nodo persona) {
        Nodo hijo = persona.getLigalista();
        if (hijo == null) {
            System.out.println("No tiene hijos.");
            return;
        }
        while (hijo != null) {
            if (!hijo.isSw()) {
                System.out.println("- " + hijo.getInfo().getNombre() + " (Cédula: " + hijo.getInfo().getCedula() + ")");
            }
            hijo = hijo.getLiga();
        }
    }

    public void mostrarHermanos(Nodo padre, String cedulaConsultada) {
        Nodo hermano = padre.getLigalista();
        boolean hay = false;
        while (hermano != null) {
            if (!hermano.isSw() && !hermano.getInfo().getCedula().equals(cedulaConsultada)) {
                System.out.println("- " + hermano.getInfo().getNombre() + " (Cédula: " + hermano.getInfo().getCedula() + ")");
                hay = true;
            }
            hermano = hermano.getLiga();
        }
        if (!hay) {
            System.out.println("No tiene hermanos.");
        }
    }

}
