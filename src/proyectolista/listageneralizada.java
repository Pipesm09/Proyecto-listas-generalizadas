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

        // Buscamos al nodo padre en todo el árbol
        Nodo padre = buscarNodo(raiz, cedulaPadre);

        if (padre == null) {
            JOptionPane.showMessageDialog(null,
                    "No se encontró al padre con cédula: " + cedulaPadre);
            return;
        }

        // 1. Si el padre todavía NO tiene hijos (su ligalista está vacío)
        if (padre.getLigalista() == null) {
            padre.setLigalista(nuevo); // El nuevo nodo es el primer hijo directo
            return;
        }

        // 2. Si ya tiene hijos, el primer hijo está directamente en la ligalista del padre
        Nodo primerHijo = padre.getLigalista();

        long cedulaNueva = Long.parseLong(cedula);
        long cedulaPrimerHijo = Long.parseLong(primerHijo.getInfo().getCedula());

        // 3. Insertar como el nuevo primer hijo (si su cédula es menor numéricamente)
        if (cedulaNueva < cedulaPrimerHijo) {
            nuevo.setLiga(primerHijo);
            padre.setLigalista(nuevo);
            return;
        }

        // 4. Buscar la posición correcta entre los hermanos usando la 'liga'
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

        // Insertamos en medio o al final de la lista de hermanos
        nuevo.setLiga(actual);
        anterior.setLiga(nuevo);
    }

    public Nodo buscarNodo(Nodo actual, String cedula) {
        if (actual == null || cedula == null) {
            return null;
        }

        // 1. ¿Es el nodo actual?
        if (actual.getInfo() != null && actual.getInfo().getCedula().trim().equals(cedula.trim())) {
            return actual;
        }

        // 2. Buscar abajo en los hijos
        if (actual.getLigalista() != null) {
            Nodo encontrado = buscarNodo(actual.getLigalista(), cedula);
            if (encontrado != null) {
                return encontrado; // Lo halló en la descendencia
            }
        }

        // 3. Si no estuvo en sus hijos, buscar a la derecha en los hermanos
        return buscarNodo(actual.getLiga(), cedula);
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
        System.out.println("Persona consultada: " + persona.getInfo().getNombre());
        System.out.println("Cédula: " + persona.getInfo().getCedula());
        System.out.println("Fecha de nacimiento: " + persona.getInfo().getFechaNacimiento());

        // PADRE
        Nodo padre = buscarPadre(raiz, cedula);

        System.out.println("\nPADRE:");
        if (padre != null) {
            System.out.println("- " + padre.getInfo().getNombre() + " (Cédula: " + padre.getInfo().getCedula() + ")");
        } else {
            System.out.println("No tiene padre registrado (Es el ancestro principal).");
        }

        // HIJOS
        System.out.println("\nHIJOS:");
        mostrarHijos(persona);

        // HERMANOS
        System.out.println("\nHERMANOS:");
        if (padre != null) {
            mostrarHermanos(padre, cedula);
        } else {
            System.out.println("No tiene hermanos (no hay padre registrado).");
        }

        // TIOS
        System.out.println("\nTIOS:");
        mostrarTios(raiz, cedula);

        // SOBRINOS
        System.out.println("\nSOBRINOS:");
        mostrarSobrinos(persona);

        // PRIMOS
        System.out.println("\nPRIMOS:");
        if (padre != null) {
            Nodo abuelo = buscarPadre(raiz, padre.getInfo().getCedula());
            if (abuelo != null) {
                mostrarPrimos(abuelo, padre.getInfo().getCedula());
            } else {
                System.out.println("No tiene primos registrados (no hay abuelo).");
            }
        } else {
            System.out.println("No tiene primos registrados (no hay padre).");
        }

        // ANCESTROS
        System.out.println("\nANCESTROS:");
        mostrarAncestros(raiz, cedula);

        // DESCENDIENTES
        System.out.println("\nDESCENDIENTES:");
        mostrarDescendientes(persona);
    }

    public Nodo buscarPadre(Nodo actual, String cedula) {
        if (actual == null) {
            return null;
        }

        Nodo aux = actual;

        // Acá vamos a buscar entre todos los hermanos del nivel
        while (aux != null) {

            // Si encontramos un nodo con sw = true, buscamos entre sus hijos
            if (aux.isSw()) {
                Nodo hijo = aux.getLigalista();

                // Buscamos si uno de sus hijos es al que le estamos buscando el señor padre
                while (hijo != null) {
                    if (hijo.getInfo().getCedula().equals(cedula)) {
                        return aux; // Si encuentra al papa devuelve el Nodo donde esta parado el puntero
                    }
                    hijo = hijo.getLiga(); // Sino pues sigue revisando los otros hijos de la misma sublista
                }

                // Si no está en los hijos directos, toca buscar en los nietos y ubica los punteros en el siguiente nivel
                Nodo encontrado = buscarPadre(aux.getLigalista(), cedula);
                if (encontrado != null) {
                    return encontrado;
                }
            }

            // Si no estaba por esa rama, nos vamos al siguiente hermano del nivel a verle los hijos
            aux = aux.getLiga();
        }

        return null;
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
            System.out.println("Es el ancestro principal (No tiene padre registrado).");
        }
    }

    public boolean buscarEnSublista(Nodo actual, String cedula) {
        Nodo aux = actual;
        while (aux != null) {
            // Ya no se niega el SW, cualquier nodo en la lista es una persona válida
            if (aux.getInfo().getCedula().equals(cedula)) {
                return true;
            }
            aux = aux.getLiga();
        }
        return false;
    }

    public void mostrarHijos(Nodo persona) {
        // Verificamos que la persona tenga hijos con sw o ligalista
        if (!persona.isSw() || persona.getLigalista() == null) {
            System.out.println("No tiene hijos.");
            return;
        }

        Nodo hijo = persona.getLigalista();
        // Pasamos por todos los nodos de la sublista y los mostramos
        while (hijo != null) {
            System.out.println("- " + hijo.getInfo().getNombre() + " (Cédula: " + hijo.getInfo().getCedula() + ")");
            hijo = hijo.getLiga();
        }
    }

    public void mostrarHijosAux(Nodo persona) {
        if (persona.isSw()) {
            Nodo hijo = persona.getLigalista();
            while (hijo != null) {
                System.out.println("- " + hijo.getInfo().getNombre() + " (Cédula: " + hijo.getInfo().getCedula() + ")");
                hijo = hijo.getLiga();
            }
        }
    }

    public void mostrarHermanos(Nodo padre, String cedulaConsultada) {
        if (padre == null || !padre.isSw()) {
            System.out.println("No tiene hermanos.");
            return;
        }

        // Los hermanos de la persona son los hijos de su padre
        Nodo hermano = padre.getLigalista();
        boolean hay = false;

        while (hermano != null) {
            // Se muestra a todos excepto a la persona consultada
            if (!hermano.getInfo().getCedula().equals(cedulaConsultada)) {
                System.out.println("- " + hermano.getInfo().getNombre() + " (Cédula: " + hermano.getInfo().getCedula() + ")");
                hay = true;
            }
            hermano = hermano.getLiga();
        }

        if (!hay) {
            System.out.println("No tiene hermanos.");
        }
    }

    public Nodo buscarHermanos(Nodo padre, String cedula) {
        if (padre == null || !padre.isSw()) {
            return null;
        }

        Nodo hermano = padre.getLigalista();

        while (hermano != null) {
            if (!hermano.getInfo().getCedula().equals(cedula)) {
                return hermano;
            }
            hermano = hermano.getLiga();
        }

        return null;
    }

    public void mostrarTios(Nodo raiz, String cedula) {
        Nodo padre = buscarPadre(raiz, cedula);
        if (padre == null) {
            System.out.println("No tiene tios registrados (no se halló el padre).");
            return;
        }

        Nodo abuelo = buscarPadre(raiz, padre.getInfo().getCedula());
        if (abuelo == null || !abuelo.isSw()) {
            System.out.println("No tiene tios registrados (no se halló el abuelo).");
            return;
        }

        // Los tios son los hijos del abuelo (excluyendo al padre)
        Nodo tio = abuelo.getLigalista();
        boolean tieneTios = false;

        while (tio != null) {
            if (!tio.getInfo().getCedula().equals(padre.getInfo().getCedula())) {
                System.out.println("- Nombre: " + tio.getInfo().getNombre()
                        + " | Cedula: " + tio.getInfo().getCedula()
                        + " | Fecha: " + tio.getInfo().getFechaNacimiento());
                tieneTios = true;
            }
            tio = tio.getLiga();
        }

        if (!tieneTios) {
            System.out.println("No tiene tios.");
        }
    }

    public void mostrarSobrinos(Nodo persona) {
        // Asumiendo que 'raiz' es un atributo global de la clase
        Nodo padre = buscarPadre(raiz, persona.getInfo().getCedula());

        if (padre == null || !padre.isSw()) {
            System.out.println("No tiene sobrinos (no tiene hermanos registrados).");
            return;
        }

        Nodo hermano = padre.getLigalista();
        boolean tieneSobrinos = false;

        while (hermano != null) {
            // Solo miramos a los hermanos (excluimos a la propia persona)
            if (!hermano.getInfo().getCedula().equals(persona.getInfo().getCedula())) {

                // Si el hermano tiene hijos (switch en true), iteramos sus hijos
                if (hermano.isSw()) {
                    Nodo sobrino = hermano.getLigalista();
                    while (sobrino != null) {
                        System.out.println("- " + sobrino.getInfo().getNombre()
                                + " (Cédula: " + sobrino.getInfo().getCedula() + ") "
                                + "[Hijo(a) de " + hermano.getInfo().getNombre() + "]");
                        tieneSobrinos = true;
                        sobrino = sobrino.getLiga();
                    }
                }
            }
            hermano = hermano.getLiga();
        }

        if (!tieneSobrinos) {
            System.out.println("No tiene sobrinos.");
        }
    }

    // ====================================================
    // MÉTODOS ADICIONALES (Primos, Ancestros, Descendientes)
    // ====================================================
    public void mostrarPrimos(Nodo abuelo, String cedulaPadre) {
        if (abuelo == null || !abuelo.isSw()) {
            return;
        }

        Nodo tio = abuelo.getLigalista();
        boolean hayPrimos = false;

        while (tio != null) {
            // Buscamos hijos en los tíos (omitimos al padre de la persona)
            if (!tio.getInfo().getCedula().equals(cedulaPadre)) {
                if (tio.isSw()) {
                    Nodo primo = tio.getLigalista();
                    while (primo != null) {
                        System.out.println("- " + primo.getInfo().getNombre() + " (Cédula: " + primo.getInfo().getCedula() + ")");
                        hayPrimos = true;
                        primo = primo.getLiga();
                    }
                }
            }
            tio = tio.getLiga();
        }

        if (!hayPrimos) {
            System.out.println("No tiene primos.");
        }
    }

    public void mostrarAncestros(Nodo raiz, String cedula) {
        Nodo actual = buscarPadre(raiz, cedula);
        if (actual == null) {
            System.out.println("No hay ancestros registrados para esta persona.");
            return;
        }

        while (actual != null) {
            System.out.println("- " + actual.getInfo().getNombre() + " (Cédula: " + actual.getInfo().getCedula() + ")");
            actual = buscarPadre(raiz, actual.getInfo().getCedula());
        }
    }

    public void mostrarDescendientes(Nodo persona) {
        if (!persona.isSw() || persona.getLigalista() == null) {
            System.out.println("No tiene descendientes.");
            return;
        }
        mostrarDescendientesRecursivo(persona.getLigalista(), 1);
    }

    private void mostrarDescendientesRecursivo(Nodo actual, int nivel) {
        while (actual != null) {
            // Creamos un espaciado para representar el árbol visualmente
            String espacio = "";
            for (int i = 0; i < nivel; i++) {
                espacio += "  ";
            }

            System.out.println(espacio + "- " + actual.getInfo().getNombre() + " (Cédula: " + actual.getInfo().getCedula() + ")");

            // Si tiene hijos, llamamos recursivamente aumentando el nivel visual
            if (actual.isSw()) {
                mostrarDescendientesRecursivo(actual.getLigalista(), nivel + 1);
            }
            actual = actual.getLiga();
        }
    }

    //altura del arbol
    public int obtenerAltura(Nodo actual) {
        if (actual == null) {
            return 0;
        }

        int maxSubArbolAltura = 0;

        // Recorremos todos los hijos directos de este nodo.
        // El primer hijo se obtiene con 'ligalista', y los demás hermanos con 'liga'.
        Nodo hijo = actual.getLigalista();
        while (hijo != null) {
            if (!hijo.isSw()) {
                // Calculamos recursivamente la altura de la rama de este hijo
                int alturaRamaHijo = obtenerAltura(hijo);

                // Nos quedamos con la rama más larga de entre todos los hijos
                if (alturaRamaHijo > maxSubArbolAltura) {
                    maxSubArbolAltura = alturaRamaHijo;
                }
            }
            hijo = hijo.getLiga(); // Pasamos al siguiente hermano (siguiente hijo del mismo padre)
        }

        // La altura total desde este nodo es 1 (él mismo) + la altura de su rama de hijos más profunda
        return 1 + maxSubArbolAltura;
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

    public void ancestroComunMasCercano(Nodo raiz, String cedula1, String cedula2) {
        Nodo persona1 = buscarNodo(raiz, cedula1);
        Nodo persona2 = buscarNodo(raiz, cedula2);

        // Verificamos que las 2 personas si existan :v
        if (persona1 == null || persona2 == null) {
            System.out.println("Error: Una o ambas cédulas no existen en el árbol genealógico.");
            return;
        }

        Nodo ancestro1 = persona1;

        // Ciclo externo: Sube por el linaje de la persona 1
        while (ancestro1 != null) {

            // Para cada nivel de la persona 1, evaluamos a la persona 2 desde abajo hacia arriba
            Nodo ancestro2 = persona2;

            // Ciclo interno: Sube por el linaje de la persona 2
            while (ancestro2 != null) {

                // Si en algún momento son la misma persona, ¡encontramos la intersección!
                if (ancestro1.getInfo().getCedula().equals(ancestro2.getInfo().getCedula())) {
                    System.out.println("\nEl ancestro común más cercano entre " + persona1.getInfo().getNombre()
                            + " y " + persona2.getInfo().getNombre() + " es:");
                    System.out.println("-> " + ancestro1.getInfo().getNombre() + " (Cédula: " + ancestro1.getInfo().getCedula() + ")");
                    return; // Terminamos el método aquí porque ya hallamos el más cercano
                }

                // Subimos un nivel en la familia de la persona 2 (buscamos a su padre)
                ancestro2 = buscarPadre(raiz, ancestro2.getInfo().getCedula());
            }

            // Subimos un nivel en la familia de la persona 1 (buscamos a su padre)
            ancestro1 = buscarPadre(raiz, ancestro1.getInfo().getCedula());
        }

        // Si los ciclos terminan y no hubo coincidencias (raro si todos vienen de la misma raíz)
        System.out.println("No tienen ningún ancestro en común.");
    }

    //eliminar nivel, enlazar los nietos a sus abuelos
    public void eliminarNivel(int nivelAEliminar) {
        if (raiz == null) {
            JOptionPane.showMessageDialog(null, "El árbol está vacío.");
            return;
        }

        if (nivelAEliminar <= 1) {
            JOptionPane.showMessageDialog(null, "No se puede eliminar el nivel 1 (La Raíz principal del árbol).");
            return;
        }

        // Validamos que el nivel exista antes de intentar borrar
        int alturaMaxima = obtenerAltura(raiz);
        if (nivelAEliminar > alturaMaxima) {
            JOptionPane.showMessageDialog(null, "El nivel ingresado no existe en el árbol. La altura máxima es: " + alturaMaxima);
            return;
        }

        // Ejecutamos la lógica de puenteo en el nivel (nivelAEliminar - 1)
        procesarEliminacionNivel(raiz, 1, nivelAEliminar);
        JOptionPane.showMessageDialog(null, "Nivel " + nivelAEliminar + " eliminado con éxito. Los descendientes han ascendido.");
    }

    /**
     * Método recursivo que se detiene en los padres (nivel - 1) para conectar a
     * los abuelos con los nietos.
     */
    private void procesarEliminacionNivel(Nodo actual, int nivelActual, int nivelBuscado) {
        if (actual == null) {
            return;
        }

        // Si estamos exactamente en el nivel ANTERIOR al que se va a eliminar (los padres)
        if (nivelActual == nivelBuscado - 1) {
            Nodo hijoDirecto = actual.getLigalista(); // Estos son los nodos del nivel a eliminar
            Nodo nuevaListaHijos = null;
            Nodo ultimoHijoAgregado = null;

            // Iteramos sobre los hijos del nodo actual (los que van a desaparecer)
            while (hijoDirecto != null) {
                // Para cada nodo que se va a eliminar, rescatamos a SUS propios hijos (los nietos)
                Nodo nietosDelInfortunado = hijoDirecto.getLigalista();

                while (nietosDelInfortunado != null) {
                    Nodo siguienteNieto = nietosDelInfortunado.getLiga();
                    nietosDelInfortunado.setLiga(null); // Desconectamos al nieto temporalmente

                    // Los unimos en una nueva lista de hijos adoptados para el abuelo (actual)
                    if (nuevaListaHijos == null) {
                        nuevaListaHijos = nietosDelInfortunado;
                        ultimoHijoAgregado = nietosDelInfortunado;
                    } else {
                        ultimoHijoAgregado.setLiga(nietosDelInfortunado);
                        ultimoHijoAgregado = nietosDelInfortunado; // Avanzamos el puntero del último
                    }

                    nietosDelInfortunado = siguienteNieto;
                }

                hijoDirecto = hijoDirecto.getLiga(); // Pasamos al siguiente hijo a eliminar
            }

            // El abuelo ('actual') adopta directamente a los nietos, puenteando a los padres del medio
            if (nuevaListaHijos != null) {
                actual.setLigalista(nuevaListaHijos);
            } else {
                // Si los nodos a eliminar no tenían hijos (eran hojas de ese nivel), la ligalista del abuelo pasa a ser null
                actual.setLigalista(null);
            }
        }

        // Continuamos barriendo el árbol recursivamente hacia abajo (hijos) y hacia los lados (hermanos)
        if (nivelActual < nivelBuscado - 1) {
            procesarEliminacionNivel(actual.getLigalista(), nivelActual + 1, nivelBuscado);
        }
        procesarEliminacionNivel(actual.getLiga(), nivelActual, nivelBuscado);
    }

}
