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
                    System.out.println("\n--- REPRESENTACION COMO LISTA GENERALIZADA ---");
                    // CORREGIDO: Evaluamos arbol.getRaiz() en lugar de la variable local raiz
                    if (arbol.getRaiz() == null) {
                        System.out.println("No hay ningun arbol cargado en memoria.");
                    } else {
                        System.out.print("Estructura: ");
                        listageneralizada.imprimirComoLista(arbol.getRaiz());
                        System.out.println(); 
                    }
                    break;

                case 3:
                    System.out.println("\n--- VISUALIZACION JERARQUICA DEL ARBOL ---");
                    // CORREGIDO: Evaluamos arbol.getRaiz()
                    if (arbol.getRaiz() == null) {
                        System.out.println("No hay ningun arbol cargado en memoria.");
                    } else {
                        listageneralizada.visualizarArbol(arbol.getRaiz(), 0);
                    }
                    break;

                case 4:
                    if (arbol.getRaiz() == null) {
                        JOptionPane.showMessageDialog(null, "El arbol esta vacio.");
                    } else {
                        cedula = JOptionPane.showInputDialog("Digite la cedula de la persona:");
                        arbol.consultarRelaciones(arbol.getRaiz(), cedula);
                    }
                    break;

                case 5:
                    if (arbol.getRaiz() == null) {
                        JOptionPane.showMessageDialog(null, "El arbol esta vacio.");
                    } else {
                        cedula = JOptionPane.showInputDialog("Digite la cedula de la persona que desea eliminar:");
                        arbol.eliminarPersona(cedula);
                    }
                    break;

                // =========================================================
                // NUEVOS CASOS: Los que yo CLARAMENTE hice
                // =========================================================
                case 6: // Altura del Árbol
                    if (arbol.getRaiz() == null) {
                        JOptionPane.showMessageDialog(null, "El arbol esta vacío.");
                    } else {
                        int altura = arbol.obtenerAltura(arbol.getRaiz());
                        JOptionPane.showMessageDialog(null, "La altura total del arbol (cantidad de generaciones) es: " + altura);
                    }
                    break;

                case 7: // Nivel de un Registro
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

                case 8: // Familiar más joven
                    if (arbol.getRaiz() == null) {
                        JOptionPane.showMessageDialog(null, "El arbol esta vacio.");
                    } else {
                        Nodo joven = arbol.encontrarFamiliarMasJoven(arbol.getRaiz(), null);
                        if (joven != null) {
                            JOptionPane.showMessageDialog(null, "Familiar mas joven:\n" +
                                    "Nombre: " + joven.getInfo().getNombre() + "\n" +
                                    "Cedula: " + joven.getInfo().getCedula() + "\n" +
                                    "Fecha de Nacimiento: " + joven.getInfo().getFechaNacimiento());
                        }
                    }
                    break;

                case 9: // Nodo con Mayor Grado
                    if (arbol.getRaiz() == null) {
                        JOptionPane.showMessageDialog(null, "El arbol esta vacio.");
                    } else {
                        Nodo mayorGrado = arbol.getNodoMayorGrado();
                        if (mayorGrado != null) {
                            JOptionPane.showMessageDialog(null, "Persona con mayor numero de hijos directos:\n" +
                                    "Nombre: " + mayorGrado.getInfo().getNombre() + "\n" +
                                    "Cedula: " + mayorGrado.getInfo().getCedula());
                        } else {
                            JOptionPane.showMessageDialog(null, "No se pudo determinar.");
                        }
                    }
                    break;

                case 10: // Registros por Nivel (Se imprime en la consola)
                    if (arbol.getRaiz() == null) {
                        JOptionPane.showMessageDialog(null, "El arbol esta vacio.");
                    } else {
                        String nivelStr = JOptionPane.showInputDialog("Ingrese el numero de nivel (generacion) que desea consultar:");
                        int nBuscado = Integer.parseInt(nivelStr);
                        System.out.println("\n--- PERSONAS EN EL NIVEL " + nBuscado + " ---");
                        arbol.mostrarRegistrosPorNivel(arbol.getRaiz(), nBuscado, 1);
                        System.out.println("----------------------------------------");
                        JOptionPane.showMessageDialog(null, "Consulta de registros por nivel impresa en la consola");
                    }
                    break;

                case 11: // Nodo con Mayor Nivel (Más profundo)
                    if (arbol.getRaiz() == null) {
                        JOptionPane.showMessageDialog(null, "El arbol esta vacio.");
                    } else {
                        Nodo masProfundo = arbol.getNodoMayorNivel();
                        if (masProfundo != null) {
                            JOptionPane.showMessageDialog(null, "Persona que se encuentra mas profunda en el arbol:\n" +
                                    "Nombre: " + masProfundo.getInfo().getNombre() + "\n" +
                                    "Cedula: " + masProfundo.getInfo().getCedula());
                        }
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
                + "2. Mostrar representación como Lista Generalizada \n"
                + "3. Visualizar Árbol jerárquico \n"
                + "4. Consultar Relaciones Familiares \n"
                + "5. Eliminar persona (Conservando linaje)\n"
                + "6. Consultar Altura del Árbol\n"
                + "7. Consultar Nivel de un Registro\n"
                + "8. Consultar Familiar más joven\n"
                + "9. Consultar Nodo con Mayor Grado (más hijos)\n"
                + "10. Consultar Registros por Nivel\n"
                + "11. Consultar Nodo con Mayor Nivel (más profundo)\n"
                + "0. Salir\n"
                + "Selecciona una opción: "));

        return opc;
    }
}