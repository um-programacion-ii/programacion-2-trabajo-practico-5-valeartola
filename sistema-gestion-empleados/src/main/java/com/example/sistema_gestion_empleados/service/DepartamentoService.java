package com.example.sistema_gestion_empleados.service;

import com.example.sistema_gestion_empleados.exceptions.DepartamentoNoEncontradoException;
import com.example.sistema_gestion_empleados.models.Departamento;

import java.util.List;

public interface DepartamentoService {
    Departamento guardar(Departamento departamento);
    Departamento buscarPorId(Long id);
    List<Departamento> obtenerTodos();
    Departamento actualizar(Long id, Departamento departamento) throws DepartamentoNoEncontradoException;
    void eliminar(Long id);
}
