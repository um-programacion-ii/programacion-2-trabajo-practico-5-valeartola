package com.example.sistema_gestion_empleados.services;

import com.example.sistema_gestion_empleados.exceptions.DepartamentoNoEncontradoException;
import com.example.sistema_gestion_empleados.models.Departamento;
import com.example.sistema_gestion_empleados.models.Empleado;
import com.example.sistema_gestion_empleados.models.Proyecto;
import com.example.sistema_gestion_empleados.repositories.DepartamentoRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class DepartamentoServiceIntegrationTest {

    @Autowired
    private DepartamentoService departamentoService;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @BeforeEach
    void setUp() {
        departamentoRepository.deleteAll();

    }

    @Test
    void guardarDepartamento() {

        Departamento departamento = new Departamento();
        departamento.setNombre("RRHH");
        departamento.setDescripcion("Recursos Humanos");

        Departamento departamentoGuardado = departamentoService.guardar(departamento);

        assertNotNull(departamentoGuardado.getId());
        assertEquals("RRHH", departamentoGuardado.getNombre());
        assertTrue(departamentoRepository.existsById(departamentoGuardado.getId()));
    }

    @Test
    void actualizarDepartamento() {

        Departamento departamento1 = new Departamento();
        departamento1.setNombre("RRHH");
        departamento1.setDescripcion("Recursos Humanos");
        Departamento departamentoGuardado = departamentoRepository.save(departamento1);

        Departamento departamento2 = new Departamento();
        departamento2.setNombre("IT");
        departamento2.setDescripcion("Informatica");

        Departamento resultado = departamentoService.actualizar(departamentoGuardado.getId(), departamento2);

        assertEquals(departamento1.getId(), resultado.getId());
        assertEquals("IT", resultado.getNombre());
    }

    @Test
    void obtenerTodos() {

        Departamento departamento1 = new Departamento();
        departamento1.setNombre("RRHH");
        departamento1.setDescripcion("Recursos Humanos");
        departamentoRepository.save(departamento1);

        Departamento departamento2 = new Departamento();
        departamento2.setNombre("IT");
        departamento2.setDescripcion("Informatica");
        departamentoRepository.save(departamento2);

        List<Departamento> departamentoEncontrados = departamentoService.obtenerTodos();

        assertNotNull(departamentoEncontrados);
        assertEquals(2, departamentoEncontrados.size());
        assertTrue(departamentoEncontrados.stream()
                .anyMatch(e -> "RRHH".equals(e.getNombre())));
        assertTrue(departamentoEncontrados.stream()
                .anyMatch(e -> "IT".equals(e.getNombre())));

    }

    @Test
    void buscarPorID() {

        Departamento departamento = new Departamento();
        departamento.setNombre("RRHH");
        departamento.setDescripcion("Recursos Humanos");

        Departamento departamentoGuardado = departamentoRepository.save(departamento);

        Departamento resultado = departamentoService.buscarPorId(departamentoGuardado.getId());

        assertNotNull(resultado);
        assertEquals(departamentoGuardado.getId(), resultado.getId());
        assertEquals("RRHH", resultado.getNombre());

    }

    @Test
    void buscarPorIDNoExistente() {

        Long idInexistente = 999L;

        assertThrows(DepartamentoNoEncontradoException.class, () -> {
            departamentoService.buscarPorId(idInexistente);
        });
    }

    @Test
    void eliminarDepartamento() {

        Departamento departamento = new Departamento();
        departamento.setNombre("RRHH");
        departamento.setDescripcion("Recursos Humanos");
        Departamento departamentoGuardado = departamentoRepository.save(departamento);

        departamentoService.eliminar(departamentoGuardado.getId());

        assertFalse(departamentoRepository.existsById(departamentoGuardado.getId()));
    }

    @Test
    void eliminarDepartamentoNoExistente() {

        Long idInexistente = 999L;

        assertThrows(DepartamentoNoEncontradoException.class, () -> {
            departamentoService.eliminar(idInexistente);
        });

    }

}
