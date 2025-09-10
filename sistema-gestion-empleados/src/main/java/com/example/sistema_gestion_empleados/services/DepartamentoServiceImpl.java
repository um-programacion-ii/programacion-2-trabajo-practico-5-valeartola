package com.example.sistema_gestion_empleados.services;

import com.example.sistema_gestion_empleados.exceptions.DepartamentoNoEncontradoException;
import com.example.sistema_gestion_empleados.models.Departamento;
import com.example.sistema_gestion_empleados.repositories.DepartamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class DepartamentoServiceImpl implements DepartamentoService {
    private final DepartamentoRepository departamentoRepository;
    public DepartamentoServiceImpl(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }
    @Override
    public Departamento guardar(Departamento departamento) {
        return departamentoRepository.save(departamento);
    }
    @Override
    public Departamento buscarPorId(Long id) {
        return departamentoRepository.findById(id)
                .orElseThrow(() -> new DepartamentoNoEncontradoException("Departamento no encontrado con ID: " + id));
    }
    @Override
    public List<Departamento> obtenerTodos() {
        return departamentoRepository.findAll();
    }
    @Override
    public Departamento actualizar(Long id, Departamento departamento) {
        if (!departamentoRepository.existsById(id)) {
            throw new DepartamentoNoEncontradoException("Departamento no encontrado con ID: " + id);
        }
        departamento.setId(id);
        return departamentoRepository.save(departamento);
    }

    @Override
    public void eliminar(Long id) {
        if (!departamentoRepository.existsById(id)) {
            throw new DepartamentoNoEncontradoException("Departamento no encontrado con ID: " + id);
        }
        departamentoRepository.deleteById(id);
    }
}
