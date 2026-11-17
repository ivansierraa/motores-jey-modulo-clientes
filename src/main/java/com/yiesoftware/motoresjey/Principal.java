package com.yiesoftware.motoresjey;

import com.yiesoftware.motoresjey.conexion.ConexionBD;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Punto de entrada del módulo Gestión de Clientes.
 *
 * @author Iván Sierra
 */
public class Principal {

    public static void main(String[] args) {
        try (Connection conexion = ConexionBD.obtenerConexion()) {
            System.out.println("Conectado a: " + conexion.getCatalog());
        } catch (SQLException e) {
            System.out.println("No se pudo conectar: " + e.getMessage());
        }
    }
}