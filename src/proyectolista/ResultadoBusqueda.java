/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectolista;

/**
 *
 * @author andre
 */
class ResultadoBusqueda {
    Nodo nodoAEliminar;
    Nodo padreDirecto;    // Si el nodo era el primer hijo (apuntado por ligalista)
    Nodo hermanoAnterior; // Si el nodo era un hermano lateral (apuntado por liga)

    public ResultadoBusqueda(Nodo nodoAEliminar, Nodo padreDirecto, Nodo hermanoAnterior) {
        this.nodoAEliminar = nodoAEliminar;
        this.padreDirecto = padreDirecto;
        this.hermanoAnterior = hermanoAnterior;
    }
}
