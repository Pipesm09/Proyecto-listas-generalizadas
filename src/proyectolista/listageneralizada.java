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

            // Subcaso B: La raíz tiene hijos. El hijo mayor asciende a ser la nueva Raíz.
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

        // TIOS
        System.out.println("\nTIOS:");
        mostrarTios(raiz, cedula);

        // SOBRINOS
        System.out.println("\nSOBRINOS:");
        mostrarSobrinos(persona);
        /*
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

    public void mostrarHijosAux(Nodo persona) {
        Nodo hijo = persona.getLigalista();
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

    //Este metodo va a ser pa que esa consola no suelte mensajes del metodo mostrar hermanos :v
    public Nodo buscarHermanos(Nodo padre, String cedula) {

        if (padre == null) {
            return null;
        }

        Nodo sublista = padre.getLiga();

        if (sublista == null || !sublista.isSw()) {
            return null;
        }

        Nodo hermano = sublista.getLigalista();

        while (hermano != null) {

            if (!hermano.isSw()
                    && !hermano.getInfo().getCedula().equals(cedula)) {

                return hermano;
            }

            hermano = hermano.getLiga();
        }

        return null;
    }

    //Este metodo tiene toda la logica pa los tios, porque queda desorganizado poner todo esto en el principañ
    public void mostrarTios(Nodo raiz, String cedula) {

        Nodo padre = buscarPadre(raiz, cedula);

        if (padre == null) {
            System.out.println("No tiene tios.");
            return;
        }

        Nodo abuelo = buscarPadre(raiz, padre.getInfo().getCedula());

        if (abuelo == null) {
            System.out.println("No tiene tios.");
            return;
        }

        Nodo sublista = abuelo.getLiga();

        if (sublista == null || !sublista.isSw()) {
            System.out.println("No tiene tios.");
            return;
        }

        Nodo tio = sublista.getLigalista();
        boolean tieneTios = false;

        while (tio != null) {

            if (!tio.isSw()
                    && !tio.getInfo().getCedula().equals(padre.getInfo().getCedula())) {

                System.out.println(
                        "Nombre: " + tio.getInfo().getNombre()
                        + " | Cedula: " + tio.getInfo().getCedula()
                        + " | Fecha: " + tio.getInfo().getFechaNacimiento()
                );

                tieneTios = true;
            }

            tio = tio.getLiga();
        }

        if (!tieneTios) {
            System.out.println("No tiene tios.");
        }
    }

    //Esta vaina esta re mala ome
    public void mostrarSobrinos(Nodo persona) {

        Nodo padre = buscarPadre(raiz, persona.getInfo().getCedula());

        if (padre == null) {
            System.out.println("No tiene hermanos.");
            return;
        }

        Nodo sublista = padre.getLiga();

        if (sublista == null || !sublista.isSw()) {
            System.out.println("No tiene hermanos.");
            return;
        }

        Nodo hermano = sublista.getLigalista();

        while (hermano != null) {

            if (!hermano.isSw()
                    && !hermano.getInfo().getCedula().equals(persona.getInfo().getCedula())) {

                mostrarHijos(hermano);
            }

            hermano = hermano.getLiga();
        }
    }

    //altura del arbol
    public int obtenerAltura(Nodo actual) {
        //validacion para que desapile
        if (actual == null) {
            return 0;
        }
        int maxAlturaHijos = 0;
        Nodo hijo = actual.getLigalista();
        //recorrer la lista rama por rana
        while (hijo != null) {
            if (!hijo.isSw()) {//esto indica que si el hijo NO es una sublista entonces
                int alturahijo = obtenerAltura(hijo);
                if (alturahijo > maxAlturaHijos) {
                    maxAlturaHijos = alturahijo; //actualiza en caso que el siguiente hijo sea mayor, asi recursivamente
                }
            }
            hijo = hijo.getLiga(); //avanzar por los hermanos, verificando su altura
        }
        return 1 + maxAlturaHijos;
    }

    //nivel de un registro
    public int obtenerNivel(Nodo actual, String cedula, int nivelActual) {
        if (actual == null) {
            return -1; //para desapilar
        }
        if (!actual.isSw() && actual.getInfo().getCedula().equals(cedula)) {
            //si no es una sublista y la cedula de este nodo= a la cedula buscada
            return nivelActual;
        }
        int nivelEnHijos = obtenerNivel(actual.getLigalista(), cedula, nivelActual + 1);
        //baja por la sublista para ver su nivel
        if (nivelEnHijos != -1) {
            //si ya bajo y todavia hay hijos
            return nivelEnHijos;
        }
        //si ya bajo, deberia de ir por sus hermanos para ver si tienen mas nivel
        return obtenerNivel(actual.getLiga(), cedula, nivelActual);
    }

    //Familiar mas joven 
    public Nodo encontrarMasJoven(Nodo actual, Nodo masJovenActual) {
        if (actual == null) {
            return masJovenActual; //para desapilar
        }
        //toca ver entre TODO el arbol para ver quien es el que tiene menos edad, para eso el actual
        if (!actual.isSw()) {
            if (masJovenActual == null) {
                masJovenActual = actual;
            } else {
                //hace que se compare el actual sea MENOR que el masJovenActual
                if (actual.getInfo().getFechaNacimiento().isAfter(masJovenActual.getInfo().getFechaNacimiento())) {
                    masJovenActual = actual;
                }
            }
        }
        //buscar entre las sublistas
        masJovenActual = encontrarMasJoven(actual.getLigalista(), masJovenActual);
        return masJovenActual;
    }
    //Nodo con mayor grado, padre con mas hijos xd
    //variables globales, porque que pereza hacer otros metodos
    private Nodo nodoMaxGrado = null;
    private int mayorCantidadHijos = -1;

    private void calcularNodoMayorGrado(Nodo actual) {
        if (actual == null) {
            return;
        }
        if (!actual.isSw()) {
            //contar hijos directos
            int hijosDirectos = 0;
            Nodo h = actual.getLigalista(); // se para en el primer hijo
            while (h != null) {
                if (!h.isSw()) {
                    hijosDirectos++;
                    h = h.getLiga();//cuenta solo las que NO son sublista
                }
            }
            if (hijosDirectos > mayorCantidadHijos) {
                mayorCantidadHijos = hijosDirectos;
                nodoMaxGrado = actual;
            }
        }
        calcularNodoMayorGrado(actual.getLigalista());
        calcularNodoMayorGrado(actual.getLiga());
    }

    //metodo para ordenar eso
    public Nodo getNodoMayorGrado() {
        nodoMaxGrado = null;
        mayorCantidadHijos = -1;
        calcularNodoMayorGrado(raiz);
        return nodoMaxGrado;
    }

    //registros por nivel: imprime o recolecta a todas las personas de un nivel
    public void mostrarRegistroPorNivel(Nodo actual, int nivelBuscado, int nivelActual) {
        if (actual == null) {
            return;
        }
        if (!actual.isSw()) {
            if (nivelActual == nivelBuscado) {
                System.out.println("- " + actual.getInfo().getNombre() + " (Cedula: " + actual.getInfo().getCedula() + ")");
            }
        }
        //parametros para bajar hasta dar con el nivel
        if (nivelActual < nivelBuscado) {
            mostrarRegistroPorNivel(actual.getLigalista(), nivelBuscado, nivelActual + 1);
        }
        //busca ahora por sus hermanos
        mostrarRegistroPorNivel(actual.getLiga(), nivelBuscado, nivelActual);
    }
    //Nodo con mayor nivel
    //variables globles pa no tener que crear otros metodos
    private int maxProfundidadEncontrada = -1;
    private Nodo nodoMasProfundo = null;

    private void encontrarNodoMayorNivel(Nodo actual, int profundidadActual) {
        if (actual == null) {
            return;
        }

        if (!actual.isSw()) {
            if (profundidadActual > maxProfundidadEncontrada) {
                maxProfundidadEncontrada = profundidadActual;
                nodoMasProfundo = actual;
            }
        }
        //baja por las sublistas primero, luego por sus hermanos
        encontrarNodoMayorNivel(actual.getLigalista(), profundidadActual + 1);
        encontrarNodoMayorNivel(actual.getLiga(), profundidadActual);
    }

    public Nodo getNodoMayorNivel() {
        maxProfundidadEncontrada = -1;
        nodoMasProfundo = null;
        encontrarNodoMayorNivel(raiz, 1);
        return nodoMasProfundo;
    }

    public void trasladarRama(String cedulaA, String cedulaB) {
        //vadilaciones, para ver que no sean igual
        if (cedulaA.equals(cedulaB)) {
            JOptionPane.showMessageDialog(null, "La persona A y la persona B no pueden ser la misma.");
            return;
        }
        if (raiz != null && raiz.getInfo().getCedula().equals(cedulaA)) {
            JOptionPane.showMessageDialog(null, "No se puede trasladar al ancestro principal (Raíz) del arbol.");
            return;
        }
        //buscar si existe el nodo B
        Nodo nodoB = buscarNodo(raiz, cedulaB);
        if (nodoB == null) {
            JOptionPane.showMessageDialog(null, "No se encontró a la persona B (destino) con cédula: " + cedulaB);
            return;
        }
        //buscar a A, su padre y su hermano anterior para desconectarlos de A
        ResultadoBusqueda resultadoA = buscarNodoYPadre(null, raiz, cedulaA);
        if (resultadoA == null || resultadoA.nodoAEliminar == null) {
            JOptionPane.showMessageDialog(null, "No se encontro a la persona A (a trasladar) con cédula: " + cedulaA);
            return;
        }
        Nodo nodoA = resultadoA.nodoAEliminar;
        Nodo padreActualA = resultadoA.padreDirecto;
        Nodo hermanoAnteriorA = resultadoA.hermanoAnterior;

        //verificar que B no sea decendiente de A (ni el hijos, ni sobrinos, etc)
        if (esDescendiente(nodoA, cedulaB)) {
            JOptionPane.showMessageDialog(null, "Error: B es decendiente de A, por ende no se puede adoptar a A");
            return;
        }
        //se desconecta A de su padre y hermanos sin mover ligalista de A para que este no pierda su decendencia 
        if (padreActualA != null && padreActualA.getLigalista() == nodoA) {
            padreActualA.setLigalista(nodoA.getLiga());
        } else if (hermanoAnteriorA != null) {
            hermanoAnteriorA.setLiga(nodoA.getLiga());
        }
        //limpiamos a A para NO llevar a sus hermanos
        nodoA.setLiga(null);
        long cedulaANueva = Long.parseLong(nodoA.getInfo().getCedula());
        if (nodoB.getLigalista() == null) {
            nodoB.setLigalista(nodoA);
            JOptionPane.showMessageDialog(null, "¡Rama trasladada con éxito!");
            return;
        }
        //Ahora si todo salio bien xd, enlazar a A como hijo de B mediante su cedula y las cedulas de los hijos de B (si tiene)
        Nodo primerHijoB = nodoB.getLigalista();
        long cedulaPrimerHijoB = Long.parseLong(primerHijoB.getInfo().getCedula());
        if (cedulaANueva < cedulaPrimerHijoB) {
            nodoA.setLiga(primerHijoB);
            nodoB.setLigalista(nodoA);
            JOptionPane.showMessageDialog(null, "¡Rama trasladada con éxito!");
            return;
        }

        // Buscar la posición correcta entre los hijos de B ordenados por cédula
        Nodo anterior = primerHijoB;
        Nodo actual = anterior.getLiga();

        while (actual != null) {
            long cedulaActual = Long.parseLong(actual.getInfo().getCedula());
            if (cedulaANueva < cedulaActual) {
                break;
            }
            anterior = actual;
            actual = actual.getLiga();
        }

        nodoA.setLiga(actual);
        anterior.setLiga(nodoA);

        JOptionPane.showMessageDialog(null, "¡Rama trasladada con éxito!");
    }
            

    private boolean esDescendiente(Nodo actual, String cedulaBuscada) {
        if (actual == null) {
            return false;
        }

        Nodo hijo = actual.getLigalista();
        while (hijo != null) {
            if (!hijo.isSw()) {
                if (hijo.getInfo().getCedula().equals(cedulaBuscada)) {
                    return true;
                }
                // Revisar recursivamente en la descendencia de este hijo
                if (esDescendiente(hijo, cedulaBuscada)) {
                    return true;
                }
            }
            hijo = hijo.getLiga();
        }
        return false;
    }

}
