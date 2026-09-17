package proyectolista;

import java.time.format.DateTimeParseException;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class ProyectoLista {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opc = 0;
        listageneralizada arbol = new listageneralizada();
        String nombre;
        String cedula;
        String fechaNacimiento;
        String cedulaPadre;

        do {
            opc = Menu();
            switch (opc) {
                case 1:
                    if (arbol.getRaiz() == null) { // Valida si el árbol ya tiene raíz
                        JOptionPane.showMessageDialog(null, "No se encontraron registros guardados.\nIntroduce los datos de la primera persona para iniciar el arbol:");
                        nombre = JOptionPane.showInputDialog("Introduzca el nombre de la persona: ");
                        cedula = JOptionPane.showInputDialog("Introduzca la cedula de la persona: ");

                        while (true) {
                            fechaNacimiento = JOptionPane.showInputDialog("Ingrese la fecha de nacimiento en formato (dd/MM/yyyy):");
                            try {
                                arbol.registrarRaiz(cedula, fechaNacimiento, nombre);
                                JOptionPane.showMessageDialog(null, "Persona registrada correctamente.");
                                break;
                            } catch (DateTimeParseException e) {
                                JOptionPane.showMessageDialog(null, "Fecha invalida. Use el formato dd/MM/yyyy.");
                            }
                        }
                    } else {
                        nombre = JOptionPane.showInputDialog("Introduzca el nombre de la persona: ");
                        cedula = JOptionPane.showInputDialog("Introduzca la cedula de la persona: ");
                        cedulaPadre = JOptionPane.showInputDialog("Introduzca la cedula del padre de la persona: ");

                        while (true) {
                            fechaNacimiento = JOptionPane.showInputDialog("Ingrese la fecha de nacimiento en formato (dd/MM/yyyy):");
                            try {
                                arbol.registrarPersona(cedula, fechaNacimiento, nombre, cedulaPadre);
                                JOptionPane.showMessageDialog(null, "Persona registrada correctamente.");
                                break;
                            } catch (DateTimeParseException e) {
                                JOptionPane.showMessageDialog(null, "Fecha invalida. Use el formato dd/MM/yyyy.");
                            }
                        }
                    }
                    break;
                case 2:
                    if (arbol.getRaiz() == null) {
                        JOptionPane.showMessageDialog(null, "El arbol esta vacio.");
                    } else {
                        cedula = JOptionPane.showInputDialog("Digite la cedula de la persona:");
                        arbol.consultarRelaciones(arbol.getRaiz(), cedula);
                    }
                    break;

                case 3:
                    if (arbol.getRaiz() == null) {
                        JOptionPane.showMessageDialog(null, "El arbol esta vacio.");
                    } else {
                        cedula = JOptionPane.showInputDialog("Digite la cedula de la persona que desea eliminar:");
                        arbol.eliminarPersona(cedula);
                    }
                    break;

                // NUEVOS CASOS: Los que yo CLARAMENTE hice
                case 4: // Altura del Árbol
                    if (arbol.getRaiz() == null) {
                        JOptionPane.showMessageDialog(null, "El arbol esta vacío.");
                    } else {
                        int altura = arbol.obtenerAltura(arbol.getRaiz());
                        JOptionPane.showMessageDialog(null, "La altura total del arbol (cantidad de generaciones) es: " + altura);
                    }
                    break;

                case 5: // Nivel de un Registro
                    if (arbol.getRaiz() == null) {
                        JOptionPane.showMessageDialog(null, "El arbol esta vacio.");
                    } else {
                        cedula = JOptionPane.showInputDialog("Digite la cedula de la persona para conocer su nivel:");
                        int nivel = arbol.obtenerNivel(arbol.getRaiz(), cedula, 1);
                        if (nivel != -1) {
                            JOptionPane.showMessageDialog(null, "La persona se encuentra en el nivel (generacion): " + nivel);
                        } else {
                            JOptionPane.showMessageDialog(null, "No se encontro a ninguna persona con esa cedula.");
                        }
                    }
                    break;

                case 6: // Familiar más joven
                    if (arbol.getRaiz() == null) {
                        JOptionPane.showMessageDialog(null, "El árbol está vacío.");
                    } else {
                        Nodo joven = arbol.getNodoMasJoven(); // Llamamos al método seguro
                        if (joven != null) {
                            JOptionPane.showMessageDialog(null, "Familiar más joven:\n"
                                    + "Nombre: " + joven.getInfo().getNombre() + "\n"
                                    + "Cédula: " + joven.getInfo().getCedula() + "\n"
                                    + "Fecha de Nacimiento: " + joven.getInfo().getFechaNacimiento());
                        }
                    }
                    break;

                case 7: // Nodo con Mayor Grado
                    if (arbol.getRaiz() == null) {
                        JOptionPane.showMessageDialog(null, "El arbol esta vacio.");
                    } else {
                        Nodo mayorGrado = arbol.getNodoMayorGrado();
                        if (mayorGrado != null) {
                            JOptionPane.showMessageDialog(null, "Persona con mayor numero de hijos directos:\n"
                                    + "Nombre: " + mayorGrado.getInfo().getNombre() + "\n"
                                    + "Cedula: " + mayorGrado.getInfo().getCedula());
                        } else {
                            JOptionPane.showMessageDialog(null, "No se pudo determinar.");
                        }
                    }
                    break;

                case 8: // Registros por Nivel (Se imprime en la consola)
                    if (arbol.getRaiz() == null) {
                        JOptionPane.showMessageDialog(null, "El arbol esta vacio.");
                    } else {
                        String nivelStr = JOptionPane.showInputDialog("Ingrese el numero de nivel (generacion) que desea consultar:");
                        int nBuscado = Integer.parseInt(nivelStr);
                        System.out.println("\n--- PERSONAS EN EL NIVEL " + nBuscado + " ---");
                        arbol.mostrarRegistroPorNivel(arbol.getRaiz(), nBuscado, 1);
                        System.out.println("----------------------------------------");
                        JOptionPane.showMessageDialog(null, "Consulta de registros por nivel impresa en la consola");
                    }
                    break;

                case 9: // Nodo con Mayor Nivel (Más profundo)
                    if (arbol.getRaiz() == null) {
                        JOptionPane.showMessageDialog(null, "El arbol esta vacio.");
                    } else {
                        Nodo masProfundo = arbol.getNodoMayorNivel();
                        if (masProfundo != null) {
                            JOptionPane.showMessageDialog(null, "Persona que se encuentra mas profunda en el arbol:\n"
                                    + "Nombre: " + masProfundo.getInfo().getNombre() + "\n"
                                    + "Cedula: " + masProfundo.getInfo().getCedula());
                        }
                    }
                    break;
                case 10: // Trasladar Rama (Adopción)
                    if (arbol.getRaiz() == null) {
                        JOptionPane.showMessageDialog(null, "El árbol está vacío.");
                    } else {
                        String cedulaA = JOptionPane.showInputDialog("Digite la cédula de la persona (A) que será trasladada con su descendencia:");
                        String cedulaB = JOptionPane.showInputDialog("Digite la cédula de la persona (B) que será el nuevo padre adoptivo:");

                        arbol.trasladarRama(cedulaA, cedulaB);
                    }
                    break;
                case 11: //Ancestro comun mas cercano

                    String cedula1 = JOptionPane.showInputDialog(null,
                            "Ingrese la cédula de la PRIMERA persona:",
                            "Buscar Ancestro Común",
                            JOptionPane.QUESTION_MESSAGE);

                    // Validar si el usuario presionó 'Cancelar' o dejó en blanco
                    if (cedula1 == null || cedula1.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Operación cancelada o cédula vacía.");
                        break;
                    }

                    // Pedir la segunda cédula
                    String cedula2 = JOptionPane.showInputDialog(null,
                            "Ingrese la cédula de la SEGUNDA persona:",
                            "Buscar Ancestro Común",
                            JOptionPane.QUESTION_MESSAGE);

                    // Validar la segunda entrada
                    if (cedula2 == null || cedula2.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Operación cancelada o cédula vacía.");
                        break;
                    }
                    System.out.println("\n--- ANCESTRO COMÚN MÁS CERCANO ---");
                    arbol.ancestroComunMasCercano(arbol.getRaiz(), cedula1.trim(), cedula2.trim());

                    break;
                case 12: // Eliminar Nivel completo
                    if (arbol.getRaiz() == null) {
                        JOptionPane.showMessageDialog(null, "El árbol está vacío.");
                    } else {
                        String nivelStr = JOptionPane.showInputDialog("Digite el número de nivel que desea eliminar (Ej. 2, 3...):");
                        try {
                            int nivelAEliminar = Integer.parseInt(nivelStr);
                            arbol.eliminarNivel(nivelAEliminar);
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Por favor, ingrese un número válido.");
                        }
                    }
                    break;
                case 13:
                    // Validamos si la raíz es nula antes de intentar graficar
                    if (arbol.getRaiz() == null) {
                        JOptionPane.showMessageDialog(null,
                                "El árbol genealógico está vacío. No hay datos para mostrar.",
                                "Árbol Vacío",
                                JOptionPane.WARNING_MESSAGE);
                    } else {
                        VisualizadorArbol ventana = new VisualizadorArbol(arbol.getRaiz());
                        ventana.setVisible(true);
                        ventana.toFront();

                        // Pausamos la repetición del menú hasta que el usuario decida cerrarla o continuar
                        JOptionPane.showMessageDialog(null,
                                "El árbol está desplegado en pantalla.\nPresiona ACEPTAR cuando quieras volver al menú principal.",
                                "Visualizando Árbol",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                    break;
                case 0:
                    System.out.println("Saliendo del programa...");
                    break;

                default:
                    System.out.println("\nOpcion no válida. Intenta de nuevo.");
            }
        } while (opc != 0);
    }

    public static int Menu() {
        int opc = Integer.parseInt(JOptionPane.showInputDialog("      MENÚ ÁRBOL GENEALÓGICO (N-ARIO)        \n"
                + "1. Registrar miembros en el árbol \n"
                + "2. Consultar Relaciones Familiares \n"
                + "3. Eliminar persona (Conservando linaje)\n"
                + "4. Consultar Altura del Árbol\n"
                + "5. Consultar Nivel de un Registro\n"
                + "6. Consultar Familiar más joven\n"
                + "7. Consultar Nodo con Mayor Grado (más hijos)\n"
                + "8. Consultar Registros por Nivel\n"
                + "9. Consultar Nodo con Mayor Nivel (más profundo)\n"
                + "10. Trasladar rama \n"
                + "11. Consultar Ancestro común más cercano entre 2 personas\n"
                + "12. Eliminar nivel (Conservando linaje)\n"
                + "13. Visualizar el arbol de forma gráfica\n"
                + "0. Salir\n"
                + "Selecciona una opción: "));

        return opc;
    }
}
