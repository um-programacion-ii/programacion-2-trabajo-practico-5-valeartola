package com.example.sistema_gestion_empleados.service;

import com.example.sistema_gestion_empleados.exceptions.EmpleadoNoEncontradoException;
import com.example.sistema_gestion_empleados.exceptions.ProyectoNoEncontradoException;
import com.example.sistema_gestion_empleados.models.Proyecto;
import com.example.sistema_gestion_empleados.repositories.ProyectoRepository;

import java.time.LocalDate;
import java.util.List;

public class ProyectoServiceImpl implements ProyectoService {
    private ProyectoRepository proyectoRepository;

    public ProyectoServiceImpl(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    @Override
    public Proyecto guardar(Proyecto proyecto) {
        return proyectoRepository.save(proyecto);
    }

    @Override
    public Proyecto buscarPorId(Long id) {
        return proyectoRepository.findById(id)
                .orElseThrow(() -> new EmpleadoNoEncontradoException("Proyecto no encontrado con ID: " + id));
    }

    @Override
    public List<Proyecto> obtenerTodos() {
        return proyectoRepository.findAll();
    }

    @Override
    public Proyecto actualizar(Long id, Proyecto proyecto) {
        if (!proyectoRepository.existsById(id)) {
            throw new ProyectoNoEncontradoException("Proyecto no encontrado con ID: " + id);
        }
        proyecto.setId(id);
        return proyectoRepository.save(proyecto);
    }

    @Override
    public void eliminar(Long id) {
        if (!proyectoRepository.existsById(id)) {
            throw new EmpleadoNoEncontradoException("Proyecto no encontrado con ID: " + id);
        }
        proyectoRepository.deleteById(id);
    }

    @Override
    public List<Proyecto> buscarPorProyectosActivos(){
        return proyectoRepository.findByFechaFinAfter(LocalDate.now());
    }
}
