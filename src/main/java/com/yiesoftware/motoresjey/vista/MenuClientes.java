package com.yiesoftware.motoresjey.vista;
 
import com.yiesoftware.motoresjey.dao.ClienteDAO;
import com.yiesoftware.motoresjey.dao.ClienteDAOImpl;
import com.yiesoftware.motoresjey.modelo.Cliente;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
 
/**
 * Menú de consola del módulo Gestión de Clientes.
 * Pide los datos al administrador y usa ClienteDAO para guardarlos.
 *
 * @author Iván Sierra
 */
public class MenuClientes {
 
    private static final List<String> TIPOS_DOCUMENTO = List.of("CC", "CE", "TI", "PAS", "NIT");
    private static final List<String> ESTADOS = List.of("ACTIVO", "INACTIVO");
 
    private final Scanner scanner = new Scanner(System.in);
    private final ClienteDAO clienteDAO;
 
    /**
     * Crea el menú usando la implementación JDBC del DAO.
     */
    public MenuClientes() {
        this(new ClienteDAOImpl());
    }
 
    /**
     * Crea el menú con el DAO que se le entregue (útil para pruebas).
     *
     * @param clienteDAO objeto que accede a los datos de los clientes
     */
    public MenuClientes(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }
 
    /**
     * Muestra el menú y atiende opciones hasta que el usuario elija salir.
     */
    public void mostrar() {
        String opcion;
        do {
            imprimirOpciones();
            opcion = leerTexto("Seleccione una opción: ");
            try {
                switch (opcion) {
                    case "1" -> registrarCliente();
                    case "2" -> buscarCliente();
                    case "3" -> listarClientes();
                    case "4" -> actualizarCliente();
                    case "5" -> eliminarCliente();
                    case "0" -> System.out.println("Hasta luego.");
                    default -> System.out.println("Opción no válida. Intente de nuevo.");
                }
            } catch (SQLException e) {
                System.out.println("Error de base de datos: " + e.getMessage());
                System.out.println("Revise que MySQL esté encendido y los datos de db.properties.");
            }
        } while (!opcion.equals("0"));
    }
 
    private void imprimirOpciones() {
        System.out.println();
        System.out.println("===== MOTORES JEY · GESTIÓN DE CLIENTES =====");
        System.out.println("1. Registrar cliente");
        System.out.println("2. Buscar cliente por id");
        System.out.println("3. Listar clientes");
        System.out.println("4. Actualizar cliente");
        System.out.println("5. Eliminar cliente");
        System.out.println("0. Salir");
    }
 
    private void registrarCliente() throws SQLException {
        System.out.println("--- Registrar cliente ---");
        String tipoDocumento = leerTipoDocumento();
        String numeroDocumento = leerObligatorio("Número de documento: ");
        String nombre = leerObligatorio("Nombre: ");
        String apellido = leerOpcional("Apellido (opcional): ");
        String telefono = leerOpcional("Teléfono (opcional): ");
        String email = leerEmail("Email (opcional): ", null);
        String direccion = leerOpcional("Dirección (opcional): ");
 
        Cliente cliente = new Cliente(tipoDocumento, numeroDocumento, nombre,
                apellido, telefono, email, direccion);
        try {
            int idGenerado = clienteDAO.insertar(cliente);
            System.out.println("Cliente registrado con id " + idGenerado + ".");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("No se registró: ya existe una persona con ese documento o ese email.");
        }
    }
 
    private void buscarCliente() throws SQLException {
        System.out.println("--- Buscar cliente ---");
        int idPersona = leerEntero("Id del cliente: ");
        Optional<Cliente> resultado = clienteDAO.buscarPorId(idPersona);
        if (resultado.isPresent()) {
            System.out.println(resultado.get());
        } else {
            System.out.println("No existe un cliente con el id " + idPersona + ".");
        }
    }
 
    private void listarClientes() throws SQLException {
        System.out.println("--- Lista de clientes ---");
        List<Cliente> clientes = clienteDAO.listarTodos();
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }
        for (Cliente cliente : clientes) {
            System.out.println(cliente);
        }
        System.out.println("Total: " + clientes.size() + " cliente(s).");
    }
 
    private void actualizarCliente() throws SQLException {
        System.out.println("--- Actualizar cliente ---");
        int idPersona = leerEntero("Id del cliente: ");
        Optional<Cliente> resultado = clienteDAO.buscarPorId(idPersona);
        if (resultado.isEmpty()) {
            System.out.println("No existe un cliente con el id " + idPersona + ".");
            return;
        }
        Cliente cliente = resultado.get();
        System.out.println("Datos actuales: " + cliente);
        System.out.println("Escriba el dato nuevo o presione Enter para conservar el actual.");
 
        cliente.setNombre(leerConValorActual("Nombre", cliente.getNombre()));
        cliente.setApellido(leerConValorActual("Apellido", cliente.getApellido()));
        cliente.setTelefono(leerConValorActual("Teléfono", cliente.getTelefono()));
        cliente.setEmail(leerEmail("Email [" + mostrarValor(cliente.getEmail()) + "]: ", cliente.getEmail()));
        cliente.setDireccion(leerConValorActual("Dirección", cliente.getDireccion()));
        cliente.setEstado(leerEstado(cliente.getEstado()));
 
        try {
            if (clienteDAO.actualizar(cliente)) {
                System.out.println("Cliente actualizado.");
            } else {
                System.out.println("No se pudo actualizar el cliente.");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("No se actualizó: ese email ya lo tiene otra persona.");
        }
    }
 
    private void eliminarCliente() throws SQLException {
        System.out.println("--- Eliminar cliente ---");
        int idPersona = leerEntero("Id del cliente: ");
        Optional<Cliente> resultado = clienteDAO.buscarPorId(idPersona);
        if (resultado.isEmpty()) {
            System.out.println("No existe un cliente con el id " + idPersona + ".");
            return;
        }
        System.out.println(resultado.get());
        String confirmacion = leerTexto("¿Seguro que desea eliminarlo? (S/N): ");
        if (!confirmacion.equalsIgnoreCase("S")) {
            System.out.println("Operación cancelada.");
            return;
        }
        try {
            if (clienteDAO.eliminar(idPersona)) {
                System.out.println("Cliente eliminado.");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("No se puede eliminar: el cliente tiene motos u otros registros asociados.");
            System.out.println("Puede cambiar su estado a INACTIVO con la opción 4.");
        }
    }
 
    // ---------- Métodos de ayuda para leer datos ----------
 
    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }
 
    private String leerObligatorio(String mensaje) {
        String valor = leerTexto(mensaje);
        while (valor.isEmpty()) {
            System.out.println("Este dato es obligatorio.");
            valor = leerTexto(mensaje);
        }
        return valor;
    }
 
    private String leerOpcional(String mensaje) {
        String valor = leerTexto(mensaje);
        return valor.isEmpty() ? null : valor;
    }
 
    private String leerConValorActual(String etiqueta, String valorActual) {
        String valor = leerTexto(etiqueta + " [" + mostrarValor(valorActual) + "]: ");
        return valor.isEmpty() ? valorActual : valor;
    }
 
    private String mostrarValor(String valor) {
        return valor == null ? "vacío" : valor;
    }
 
    private int leerEntero(String mensaje) {
        while (true) {
            String valor = leerTexto(mensaje);
            try {
                return Integer.parseInt(valor);
            } catch (NumberFormatException e) {
                System.out.println("Debe escribir un número entero.");
            }
        }
    }
 
    private String leerTipoDocumento() {
        String mensaje = "Tipo de documento " + TIPOS_DOCUMENTO + ": ";
        String valor = leerTexto(mensaje).toUpperCase();
        while (!TIPOS_DOCUMENTO.contains(valor)) {
            System.out.println("Tipo no válido. Opciones: " + TIPOS_DOCUMENTO);
            valor = leerTexto(mensaje).toUpperCase();
        }
        return valor;
    }
 
    private String leerEmail(String mensaje, String valorActual) {
        while (true) {
            String valor = leerTexto(mensaje);
            if (valor.isEmpty()) {
                return valorActual;
            }
            if (valor.contains("@") && valor.contains(".")) {
                return valor;
            }
            System.out.println("Email no válido. Ejemplo: nombre@correo.com");
        }
    }
 
    private String leerEstado(String valorActual) {
        String mensaje = "Estado " + ESTADOS + " [" + valorActual + "]: ";
        while (true) {
            String valor = leerTexto(mensaje).toUpperCase();
            if (valor.isEmpty()) {
                return valorActual;
            }
            if (ESTADOS.contains(valor)) {
                return valor;
            }
            System.out.println("Estado no válido. Opciones: " + ESTADOS);
        }
    }
}
 