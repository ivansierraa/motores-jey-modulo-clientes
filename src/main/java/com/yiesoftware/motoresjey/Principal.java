package com.yiesoftware.motoresjey;

import com.yiesoftware.motoresjey.dao.ClienteDAO;
import com.yiesoftware.motoresjey.dao.ClienteDAOImpl;
import com.yiesoftware.motoresjey.modelo.Cliente;
import java.sql.SQLException;

public class Principal {

    public static void main(String[] args) {
        ClienteDAO clienteDAO = new ClienteDAOImpl();
        try {
            // 1. Insertar
            Cliente nuevo = new Cliente("CC", "1000000001", "Prueba", "JDBC",
                    "3000000000", "prueba.jdbc@correo.com", "Calle Falsa 123");
            int id = clienteDAO.insertar(nuevo);
            System.out.println("Insertado con id: " + id);

            // 2. Buscar por id
            System.out.println("Buscado: " + clienteDAO.buscarPorId(id).orElse(null));

            // 3. Actualizar
            nuevo.setTelefono("3111111111");
            nuevo.setEstado("INACTIVO");
            System.out.println("¿Actualizado? " + clienteDAO.actualizar(nuevo));

            // 4. Listar todos
            System.out.println("Todos los clientes:");
            for (Cliente cliente : clienteDAO.listarTodos()) {
                System.out.println("  " + cliente);
            }

            // 5. Eliminar
            System.out.println("¿Eliminado? " + clienteDAO.eliminar(id));
            System.out.println("Buscado otra vez: " + clienteDAO.buscarPorId(id).orElse(null));
        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }
}