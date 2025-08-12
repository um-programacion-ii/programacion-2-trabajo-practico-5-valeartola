package com.example.sistema_gestion_empleados.exceptions;

public class ProyectoNoEncontradoException extends RuntimeException {
    public ProyectoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
