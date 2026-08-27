package proyectolista;

public class listageneralizada {
    
    public static Nodo construirLista(String s){
        int[] index = {0}; // Cursor de lectura
        return construirLista(s, index);
    }
    
    private static Nodo construirLista(String s, int[] index){
        if(index[0] >= s.length() || s.charAt(index[0]) != '('){
            return null;
        }
        index[0]++; // Saltar '('
        
        Nodo cabeza = null;
        Nodo ultimo = null;
        
        while(index[0] < s.length() && s.charAt(index[0]) != ')'){
            char actual = s.charAt(index[0]);
            if(actual == ','){
                index[0]++;
                continue;
            }
            
            Nodo nuevo;
            if(actual == '('){
                // 1. Construir la sublista recursivamente
                Nodo sublista = construirLista(s, index); 
                
                // 2. Crear el nodo para la sublista usando tu constructor
                nuevo = new Nodo(null);       // info se queda vacío (null)
                nuevo.setSw(true);            // true = representa una sublista
                nuevo.setLigalista(sublista); // Apuntamos al inicio de la sublista
            }
            else {
                // 1. Crear el nodo para el átomo usando tu constructor
                nuevo = new Nodo(actual);     // Guardamos el carácter en "info"
                nuevo.setSw(false);           // false = representa un átomo
                index[0]++;
            }
            
            // Enlazar horizontalmente los nodos de este nivel
            if(cabeza == null){
                cabeza = nuevo;
                ultimo = nuevo;
            }
            else{
                // CORRECCIÓN CLAVE: Usamos setLiga con 'L' mayúscula, tal como está en tu Nodo.java
                ultimo.setLiga(nuevo); 
                ultimo = nuevo;
            }
        }
        
        // Saltar el paréntesis de cierre del nivel actual
        if(index[0] < s.length() && s.charAt(index[0]) == ')'){
            index[0]++;
        }
        
        return cabeza;
    }
    public static void imprimir(Nodo x) {
    System.out.print("(");
    Nodo aux = x;
    while (aux != null) {
        if (aux.isSw() == false) { 
            // Es un átomo, imprimimos su información
            System.out.print(aux.getInfo());
        } else {
            // Es una sublista, llamamos recursivamente pasando "ligalista"
            imprimir(aux.getLigalista()); 
        }
        aux = aux.getLiga(); // Avanzar horizontalmente
        if (aux != null) {
            System.out.print(",");
        }
    }
    System.out.print(")");
}
}