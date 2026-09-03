package proyectolista;

public class listageneralizada {
    
    private Nodo raiz;

    public Nodo getRaiz() {
        return raiz;
    }

    public void setRaiz(Nodo raiz) {
        this.raiz = null;
    }
    
    public static Nodo construirLista(String s){
        int[] index = {0}; 
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
                Nodo sublista = construirLista(s, index); 
                
                nuevo = new Nodo(null);       
                nuevo.setSw(true);            
                nuevo.setLigalista(sublista); 
            }
            else {
                nuevo = new Nodo(actual);     
                nuevo.setSw(false);           
                index[0]++;
            }
            
            if(cabeza == null){
                cabeza = nuevo;
                ultimo = nuevo;
            }
            else{
                ultimo.setLiga(nuevo); 
                ultimo = nuevo;
            }
        }
        
        if(index[0] < s.length() && s.charAt(index[0]) == ')'){
            index[0]++;
        }
        
        return cabeza;
    }
  public static void visualizarArbol(Nodo x, int nivel) {
        if (x == null) return;
                if (!x.isSw()) {
            Persona p = (Persona) x.getInfo();
            imprimirSangria(nivel);
            System.out.println("└── " + p.getNombre() + " (ID: " + p.getCedula() + ")");
        }
        
        Nodo aux = x.getLiga();
        while (aux != null) {
            if (!aux.isSw()) {
                Persona p = (Persona) aux.getInfo();
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