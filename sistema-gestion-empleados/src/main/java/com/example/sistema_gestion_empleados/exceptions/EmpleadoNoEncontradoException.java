package com.example.sistema_gestion_empleados.exceptions;

public class EmpleadoNoEncontradoException extends RuntimeException {
    public EmpleadoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
