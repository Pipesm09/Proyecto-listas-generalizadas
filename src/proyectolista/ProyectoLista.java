
package proyectolista;

import java.util.Scanner;

public class ProyectoLista {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Nodo raiz = null;
        int opcion = 0;
        
        do {
            System.out.println("\n=============================================");
            System.out.println("      MENÚ ÁRBOL GENEALÓGICO (N-ARIO)        ");
            System.out.println("=============================================");
            System.out.println("1. Ingresar/Cargar árbol genealógico");
            System.out.println("2. Mostrar representación como Lista Generalizada (paréntesis)");
            System.out.println("3. Visualizar Árbol jerárquico (sangría de generaciones)");
            System.out.println("4. Salir");
            System.out.print("Selecciona una opción: ");
            
            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("\nError: Por favor ingresa un número válido.");
                continue;
            }
            
            switch (opcion) {
                case 1:
                    System.out.println("\n--- INGRESAR ÁRBOL ---");
                    System.out.println("Usa el formato: (Padre, (Hijo1, Nieto))");
                    System.out.print("Entrada: ");
                    String entrada = scanner.nextLine();
                    
                    raiz = listageneralizada.construirLista(entrada);
                    if (raiz == null) {
                        System.out.println("\n[ERROR] No se pudo construir el árbol. Verifica los paréntesis.");
                    } else {
                        System.out.println("\n[ÉXITO] Árbol genealógico cargado correctamente.");
                    }
                    break;
                    
                case 2:
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
            
        } while (opcion != 4);
        
        scanner.close();
    }
}



