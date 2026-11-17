package com.yiesoftware.motoresjey.dao;
 
import com.yiesoftware.motoresjey.conexion.ConexionBD;
import com.yiesoftware.motoresjey.modelo.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
 
/**
 * Implementación JDBC de ClienteDAO para MySQL.
 * Un cliente se guarda en dos tablas: personas y clientes.
 *
 * @author Iván Sierra
 */
public class ClienteDAOImpl implements ClienteDAO {
 
    private static final String SQL_INSERTAR_PERSONA =
            "INSERT INTO personas (tipo_documento, numero_documento, nombre, apellido, "
            + "telefono, email, direccion) VALUES (?, ?, ?, ?, ?, ?, ?)";
 
    private static final String SQL_INSERTAR_CLIENTE =
            "INSERT INTO clientes (id_persona) VALUES (?)";
 
    private static final String SQL_SELECCIONAR_BASE =
            "SELECT p.id_persona, p.tipo_documento, p.numero_documento, p.nombre, p.apellido, "
            + "p.telefono, p.email, p.direccion, c.fecha_registro, c.estado "
            + "FROM clientes c INNER JOIN personas p ON p.id_persona = c.id_persona";
 
    private static final String SQL_BUSCAR_POR_ID = SQL_SELECCIONAR_BASE + " WHERE c.id_persona = ?";
 
    private static final String SQL_LISTAR_TODOS = SQL_SELECCIONAR_BASE + " ORDER BY p.apellido, p.nombre";
 
    private static final String SQL_ACTUALIZAR =
            "UPDATE personas p INNER JOIN clientes c ON c.id_persona = p.id_persona "
            + "SET p.nombre = ?, p.apellido = ?, p.telefono = ?, p.email = ?, p.direccion = ?, c.estado = ? "
            + "WHERE p.id_persona = ?";
 
    private static final String SQL_ELIMINAR_CLIENTE = "DELETE FROM clientes WHERE id_persona = ?";
 
    private static final String SQL_ELIMINAR_PERSONA = "DELETE FROM personas WHERE id_persona = ?";
 
    @Override
    public int insertar(Cliente cliente) throws SQLException {
        try (Connection conexion = ConexionBD.obtenerConexion()) {
            conexion.setAutoCommit(false);
            try (PreparedStatement sentenciaPersona = conexion.prepareStatement(
                         SQL_INSERTAR_PERSONA, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement sentenciaCliente = conexion.prepareStatement(SQL_INSERTAR_CLIENTE)) {
 
                sentenciaPersona.setString(1, cliente.getTipoDocumento());
                sentenciaPersona.setString(2, cliente.getNumeroDocumento());
                sentenciaPersona.setString(3, cliente.getNombre());
                sentenciaPersona.setString(4, cliente.getApellido());
                sentenciaPersona.setString(5, cliente.getTelefono());
                sentenciaPersona.setString(6, cliente.getEmail());
                sentenciaPersona.setString(7, cliente.getDireccion());
                sentenciaPersona.executeUpdate();
 
                int idGenerado;
                try (ResultSet llavesGeneradas = sentenciaPersona.getGeneratedKeys()) {
                    llavesGeneradas.next();
                    idGenerado = llavesGeneradas.getInt(1);
                }
 
                sentenciaCliente.setInt(1, idGenerado);
                sentenciaCliente.executeUpdate();
 
                conexion.commit();
                cliente.setIdPersona(idGenerado);
                return idGenerado;
            } catch (SQLException e) {
                conexion.rollback();
                throw e;
            }
        }
    }
 
    @Override
    public Optional<Cliente> buscarPorId(int idPersona) throws SQLException {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_BUSCAR_POR_ID)) {
            sentencia.setInt(1, idPersona);
            try (ResultSet fila = sentencia.executeQuery()) {
                if (fila.next()) {
                    return Optional.of(mapearCliente(fila));
                }
                return Optional.empty();
            }
        }
    }
 
    @Override
    public List<Cliente> listarTodos() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_LISTAR_TODOS);
             ResultSet fila = sentencia.executeQuery()) {
            while (fila.next()) {
                clientes.add(mapearCliente(fila));
            }
        }
        return clientes;
    }
 
    @Override
    public boolean actualizar(Cliente cliente) throws SQLException {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_ACTUALIZAR)) {
            sentencia.setString(1, cliente.getNombre());
            sentencia.setString(2, cliente.getApellido());
            sentencia.setString(3, cliente.getTelefono());
            sentencia.setString(4, cliente.getEmail());
            sentencia.setString(5, cliente.getDireccion());
            sentencia.setString(6, cliente.getEstado());
            sentencia.setInt(7, cliente.getIdPersona());
            int filasAfectadas = sentencia.executeUpdate();
            return filasAfectadas > 0;
        }
    }
 
    @Override
    public boolean eliminar(int idPersona) throws SQLException {
        try (Connection conexion = ConexionBD.obtenerConexion()) {
            conexion.setAutoCommit(false);
            try (PreparedStatement sentenciaCliente = conexion.prepareStatement(SQL_ELIMINAR_CLIENTE);
                 PreparedStatement sentenciaPersona = conexion.prepareStatement(SQL_ELIMINAR_PERSONA)) {
 
                sentenciaCliente.setInt(1, idPersona);
                int filasAfectadas = sentenciaCliente.executeUpdate();
 
                if (filasAfectadas > 0) {
                    sentenciaPersona.setInt(1, idPersona);
                    sentenciaPersona.executeUpdate();
                }
 
                conexion.commit();
                return filasAfectadas > 0;
            } catch (SQLException e) {
                conexion.rollback();
                throw e;
            }
        }
    }
 
    private Cliente mapearCliente(ResultSet fila) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setIdPersona(fila.getInt("id_persona"));
        cliente.setTipoDocumento(fila.getString("tipo_documento"));
        cliente.setNumeroDocumento(fila.getString("numero_documento"));
        cliente.setNombre(fila.getString("nombre"));
        cliente.setApellido(fila.getString("apellido"));
        cliente.setTelefono(fila.getString("telefono"));
        cliente.setEmail(fila.getString("email"));
        cliente.setDireccion(fila.getString("direccion"));
        cliente.setFechaRegistro(fila.getObject("fecha_registro", LocalDateTime.class));
        cliente.setEstado(fila.getString("estado"));
        return cliente;
    }
}
 