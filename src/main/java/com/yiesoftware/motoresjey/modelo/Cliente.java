package com.yiesoftware.motoresjey.modelo;

import java.time.LocalDateTime;

/**
 * Representa a un cliente del taller Motores Jey.
 * Une los datos de las tablas personas y clientes.
 *
 * @author Iván Sierra
 */
public class Cliente {

    private int idPersona;
    private String tipoDocumento;
    private String numeroDocumento;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private String direccion;
    private LocalDateTime fechaRegistro;
    private String estado;

    /**
     * Crea un cliente vacío. Lo usa el DAO al leer datos de la base de datos.
     */
    public Cliente() {
    }

    /**
     * Crea un cliente con los datos del formulario de registro.
     * El id, la fecha de registro y el estado los asigna la base de datos.
     *
     * @param tipoDocumento   tipo de documento (CC, CE, TI, PAS, NIT)
     * @param numeroDocumento número del documento
     * @param nombre          nombre del cliente
     * @param apellido        apellido del cliente
     * @param telefono        teléfono de contacto
     * @param email           correo electrónico
     * @param direccion       dirección de residencia
     */
    public Cliente(String tipoDocumento, String numeroDocumento, String nombre,
            String apellido, String telefono, String email, String direccion) {
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
    }

    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Devuelve la ficha del cliente en una sola línea, para mostrarla en consola.
     *
     * @return texto con los datos principales del cliente
     */
    @Override
    public String toString() {
        return "#" + idPersona + " | " + tipoDocumento + " " + numeroDocumento
                + " | " + nombre + " " + (apellido == null ? "" : apellido)
                + " | " + telefono + " | " + email + " | " + estado;
    }
}
