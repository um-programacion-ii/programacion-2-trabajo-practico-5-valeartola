package com.example.sistema_gestion_empleados.exceptions;

public class DepartamentoNoEncontradoException extends RuntimeException {
    public DepartamentoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
