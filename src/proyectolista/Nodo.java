/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectolista;

/**
 *
 * @author ASUS
 */
public class Nodo {
    private int dato;
    private Nodo liga;

    public Nodo(int dato) {
        this.dato = dato;
        liga = null;
    }

    public int getDato() {
        return dato;
    }

    public void setDato(int dato) {
        this.dato = dato;
    }

    public Nodo getliga() {
        return liga;
    }

    public void setliga(Nodo liga) {
        this.liga = liga;
    }
            
}