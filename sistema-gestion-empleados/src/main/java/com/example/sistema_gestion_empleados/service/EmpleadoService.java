package com.example.sistema_gestion_empleados.service;

import com.example.sistema_gestion_empleados.models.Empleado;

import java.math.BigDecimal;
import java.util.List;

public interface EmpleadoService {
    Empleado guardar(Empleado empleado);
    Empleado buscarPorId(Long id);
    List<Empleado> buscarPorDepartamento(String nombreDepartamento);
    List<Empleado> buscarPorRangoSalario(BigDecimal salarioMin, BigDecimal salarioMax);
    BigDecimal obtenerSalarioPromedioPorDepartamento(Long departamentoId);
    List<Empleado> obtenerTodos();
    Empleado actualizar(Long id, Empleado empleado);
    void eliminar(Long id);
}