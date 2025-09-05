package com.example.sistema_gestion_empleados.repositories;

import com.example.sistema_gestion_empleados.models.Departamento;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class DepartamentoRepositoryTest {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Test
    void buscarPorId() {

        Departamento departamento = new Departamento();
        departamento.setNombre("IT");
        departamento.setDescripcion("Informatica");

        Departamento departamentoGuardado = departamentoRepository.save(departamento);
        Optional<Departamento> resultado = departamentoRepository.findById(departamentoGuardado.getId());

        assertTrue(resultado.isPresent());
        assertEquals("IT", resultado.get().getNombre());
        
    }
}
