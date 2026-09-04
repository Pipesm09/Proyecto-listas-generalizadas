package proyectolista;

import java.time.format.DateTimeParseException;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class ProyectoLista {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Nodo raiz = null;
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
                    if (arbol.getRaiz() == null) { //Valida si el arbol ya tiene raiz
                        JOptionPane.showMessageDialog(null, "No se encontraron registros guardados."
                                + " Introduce los datos de la primera persona para iniciar el árbol: ");
                        nombre = JOptionPane.showInputDialog("Introduzca el nombre de la persona: "); //Registra datos de la persona
                        cedula = JOptionPane.showInputDialog("Introduzca la cedula de la persona: "); //Registra datos de la persona

                        while (true) { //Este while es para que si el usuario pone mal la fecha, no tenga que volver a poner todos los datos de la persona :v
                            fechaNacimiento = JOptionPane.showInputDialog("Ingrese la fecha de nacimiento en formato (dd/MM/yyyy):"); //Registra datos de la persona
                            try { //Aquí hacemos try-catch en caso de que el usuario ingrese mal el formato de la fecha
                                arbol.registrarRaiz(cedula, fechaNacimiento, nombre);
                                JOptionPane.showMessageDialog(null, "Persona registrada correctamente.");
                                break;
                            } catch (DateTimeParseException e) {
                                JOptionPane.showMessageDialog(null, "Fecha inválida. Use el formato dd/MM/yyyy."
                                );
                            }
                        }
                    } else {
                        nombre = JOptionPane.showInputDialog("Introduzca el nombre de la persona: ");
                        cedula = JOptionPane.showInputDialog("Introduzca la cedula de la persona: ");
                        cedulaPadre = JOptionPane.showInputDialog("Introduzca la cedula del padre de la persona: ");

                        while (true) { //Este while es para que si el usuario pone mal la fecha, no tenga que volver a poner todos los datos de la persona :v
                            fechaNacimiento = JOptionPane.showInputDialog("Ingrese la fecha de nacimiento en formato (dd/MM/yyyy):"); //Registra datos de la persona
                            try { //Aquí hacemos try-catch en caso de que el usuario ingrese mal el formato de la fecha
                                arbol.registrarRaiz(cedula, fechaNacimiento, nombre);
                                JOptionPane.showMessageDialog(null, "Persona registrada correctamente.");
                                break;
                            } catch (DateTimeParseException e) {
                                JOptionPane.showMessageDialog(null, "Fecha inválida. Use el formato dd/MM/yyyy."
                                );
                            }
                        }
                    }
                    break;

                case 2:
                    //Esta chimbada esta mala todavia yo creo, no he pillado :v
                    System.out.println("\n--- REPRESENTACIÓN COMO LISTA GENERALIZADA ---");
                    if (raiz == null) {
                        System.out.println("No hay ningún árbol cargado en memoria.");
                    } else {
                        System.out.print("Estructura: ");
                        listageneralizada.imprimirComoLista(raiz);
                        System.out.println(); // Salto de línea al final
                    }
                    break;

                case 3:
                    //esta otra chimbada demas que tampoco sirve todavia :v
                    System.out.println("\n--- VISUALIZACIÓN JERÁRQUICA DEL ÁRBOL ---");
                    if (raiz == null) {
                        System.out.println("No hay ningún árbol cargado en memoria.");
                    } else {
                        listageneralizada.visualizarArbol(raiz, 0);
                    }
                    break;

                case 4:
                    System.out.println("\nSaliendo del programa...");
                    break;

                default:
                    System.out.println("\nOpción no válida. Intenta de nuevo.");
            }
        } while (opc != 0);

    }

    public static int Menu() {
        int opc = Integer.parseInt(JOptionPane.showInputDialog("      MENÚ ÁRBOL GENEALÓGICO (N-ARIO)        \n"
                + "1. Registrar miembros en el arbol \n"
                + "2. Mostrar representación como Lista Generalizada \n"
                + "3. Visualizar Árbol jerárquico \n"
                + "0. Salir\n"
                + "Selecciona una opción: "));

        return opc;
    }
}
