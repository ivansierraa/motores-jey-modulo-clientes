package com.yiesoftware.motoresjey.conexion;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Entrega conexiones JDBC a la base de datos motores_jey.
 * Lee la URL, el usuario y la clave desde db.properties.
 *
 * @author Iván Sierra
 */
public final class ConexionBD {

    private static final String ARCHIVO_CONFIGURACION = "/db.properties";
    private static final Properties PROPIEDADES = cargarPropiedades();

    private ConexionBD() {
        // Clase de utilidad: no se crean objetos de ella.
    }

    /**
     * Abre una conexión nueva. Quien la pide debe cerrarla (try-with-resources).
     *
     * @return una conexión abierta a la base de datos
     * @throws SQLException si no se puede conectar
     */
    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(
                PROPIEDADES.getProperty("db.url"),
                PROPIEDADES.getProperty("db.usuario"),
                PROPIEDADES.getProperty("db.clave"));
    }

    private static Properties cargarPropiedades() {
        Properties propiedades = new Properties();
        try (InputStream entrada = ConexionBD.class.getResourceAsStream(ARCHIVO_CONFIGURACION)) {
            if (entrada == null) {
                throw new IllegalStateException("No se encontró " + ARCHIVO_CONFIGURACION);
            }
            propiedades.load(entrada);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer la configuración", e);
        }
        return propiedades;
    }
}