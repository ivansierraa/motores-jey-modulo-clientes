package com.yiesoftware.motoresjey.dao;

import com.yiesoftware.motoresjey.modelo.Cliente;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Operaciones de persistencia del módulo Gestión de Clientes.
 * Define qué se puede hacer con los clientes en la base de datos;
 * la forma de hacerlo está en ClienteDAOImpl.
 *
 * @author Iván Sierra
 */
public interface ClienteDAO {

    /**
     * Registra la persona y el cliente.
     *
     * @param cliente datos del cliente a registrar
     * @return el id generado por la base de datos
     * @throws SQLException si ocurre un error en la base de datos
     */
    int insertar(Cliente cliente) throws SQLException;

    /**
     * Busca un cliente por su id.
     *
     * @param idPersona id del cliente
     * @return el cliente encontrado, o vacío si no existe
     * @throws SQLException si ocurre un error en la base de datos
     */
    Optional<Cliente> buscarPorId(int idPersona) throws SQLException;

    /**
     * Devuelve todos los clientes ordenados por apellido y nombre.
     *
     * @return lista de clientes (vacía si no hay ninguno)
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<Cliente> listarTodos() throws SQLException;

    /**
     * Actualiza los datos de contacto y el estado de un cliente.
     *
     * @param cliente cliente con los datos nuevos (debe tener idPersona)
     * @return true si encontró y actualizó el cliente
     * @throws SQLException si ocurre un error en la base de datos
     */
    boolean actualizar(Cliente cliente) throws SQLException;

    /**
     * Elimina el cliente y su persona.
     *
     * @param idPersona id del cliente a eliminar
     * @return true si el cliente existía y se eliminó
     * @throws SQLException si ocurre un error en la base de datos
     *                      (por ejemplo, si tiene motos registradas)
     */
    boolean eliminar(int idPersona) throws SQLException;
}