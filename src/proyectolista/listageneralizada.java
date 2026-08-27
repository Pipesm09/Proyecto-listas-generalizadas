
package proyectolista;


public class listageneralizada {
    
    
    public static Nodo construirLista (String s){
        int [] index={0}; //ek qye recorre para poder hacer el recursivo (puntero que puntea xdxdxddx)
        return construirLista (s, index);
    }
    private static Nodo construirLista(String s, int[] index){
        if(index[0]>=s.length() || s.charAt(index[0])!='('){
            return null;
        }
        index[0]++;
        Nodo cabeza=null;
        Nodo ultimo=null;
        while(index[0]<s.length() && s.charAt(index[0])!=')'){
            char actual=s.charAt(index[0]);
            if(actual==','){
                index[0]++;
                continue;
            }
            Nodo nuevo;
            if(actual=='('){
                nuevo= new Nodo (1, sublista);
            }
            else {
                nuevo=new Nodo(0,actual);
                index[0]++;
            }
            if(cabeza==null){
                cabeza=nuevo;
                ultimo=nuevo;
            }
            else{
                ultimo.setliga(nuevo);
                ultimo=nuevo;
            }
            if(index[0]<s.length() && s.charAt(index[0])==')'){
                index[0]++;
            }
            return cabeza;
        }
        
    }
}
