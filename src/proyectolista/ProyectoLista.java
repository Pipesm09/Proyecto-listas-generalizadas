
package proyectolista;

import javax.swing.JOptionPane;

public class ProyectoLista {


    public static void main(String[] args) {
        int opc = 0;
        
        do{
            opc = Menu();
            switch (opc){
                case 1: 
                    break;
                case 2: 
                    break;
                case 3:
                    break;
                case 4: 
                    break;
                case 0:
                    System.out.println("Saliendo ...");
                    break;
                default:
                    System.out.println("Opcion incorrecta");
            }
        } while(opc != 0);
    }
    public static int Menu() {
        int opc = Integer.parseInt(JOptionPane.showInputDialog("****** Menu Principal ******\n"
                + "1. Recorrido InOrden.\n"
                + "2. Recorrido PreOrden\n"
                + "3. Recorrido PosOrden\n"
                + "4. Mostrar arbol completo.\n"
                + "0. Salir.\n"
                + "Ingrese una opcion\n"));

        return opc;
    }
}
