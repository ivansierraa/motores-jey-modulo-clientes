package com.yiesoftware.motoresjey;
 
import com.yiesoftware.motoresjey.vista.MenuClientes;
 
/**
 * Punto de entrada del módulo Gestión de Clientes de Motores JEY.
 *
 * @author Iván Sierra
 */
public class Principal {
 
    /**
     * Inicia el menú de consola.
     *
     * @param args no se usan
     */
    public static void main(String[] args) {
        new MenuClientes().mostrar();
    }
}
 