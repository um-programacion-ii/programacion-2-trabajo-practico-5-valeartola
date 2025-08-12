package com.example.sistema_gestion_empleados.service;

import com.example.sistema_gestion_empleados.exceptions.ProyectoNoEncontradoException;
import com.example.sistema_gestion_empleados.models.Proyecto;

import java.util.List;

public interface ProyectoService {
    Proyecto guardar(Proyecto proyecto);
    Proyecto buscarPorId(Long id);
    List<Proyecto> obtenerTodos();
    Proyecto actualizar(Long id, Proyecto proyecto) throws ProyectoNoEncontradoException;
    List<Proyecto> buscarPorProyectosActivos();
    void eliminar(Long id);
}
