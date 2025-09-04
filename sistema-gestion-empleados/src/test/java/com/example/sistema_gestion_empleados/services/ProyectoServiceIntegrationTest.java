package com.example.sistema_gestion_empleados.services;

import com.example.sistema_gestion_empleados.exceptions.ProyectoNoEncontradoException;
import com.example.sistema_gestion_empleados.models.Proyecto;
import com.example.sistema_gestion_empleados.repositories.ProyectoRepository;
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
public class ProyectoServiceIntegrationTest {

    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private ProyectoRepository proyectoRepository;

    @BeforeEach
    void setUp() {
        proyectoRepository.deleteAll();
    }

    @Test
    void guardarProyecto() {

        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Proyecto Demo");
        proyecto.setDescripcion("Proycto de prueba p1.");
        proyecto.setFechaInicio(LocalDate.now());
        proyecto.setFechaFin(LocalDate.now().plusMonths(6));

        Proyecto proyectoGuardado = proyectoService.guardar(proyecto);

        assertNotNull(proyectoGuardado.getId());
        assertEquals("Proyecto Demo", proyectoGuardado.getNombre());
        assertTrue(proyectoRepository.existsById(proyectoGuardado.getId()));

    }

    @Test
    void buscarPorId() {

        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Proyecto Demo");
        proyecto.setDescripcion("Proycto de prueba p1.");
        proyecto.setFechaInicio(LocalDate.now());
        proyecto.setFechaFin(LocalDate.now().plusMonths(6));
        Proyecto proyectoGuardado = proyectoRepository.save(proyecto);

        Proyecto resultado = proyectoService.buscarPorId(proyectoGuardado.getId());

        assertNotNull(resultado);
        assertEquals(proyectoGuardado.getId(), resultado.getId());
        assertEquals("Proyecto Demo", resultado.getNombre());

    }

    @Test
    void obtenerTodos() {

        Proyecto proyecto1 = new Proyecto();
        proyecto1.setNombre("Proyecto Demo p1");
        proyecto1.setDescripcion("Proycto de prueba p1.");
        proyecto1.setFechaInicio(LocalDate.now());
        proyecto1.setFechaFin(LocalDate.now().plusMonths(6));
        proyectoRepository.save(proyecto1);

        Proyecto proyecto2 = new Proyecto();
        proyecto2.setNombre("Proyecto Demo p2");
        proyecto2.setDescripcion("Proycto de prueba p2.");
        proyecto2.setFechaInicio(LocalDate.now());
        proyecto2.setFechaFin(LocalDate.now().plusMonths(6));
        proyectoRepository.save(proyecto2);

        List<Proyecto> proyectosEncontrados = proyectoService.obtenerTodos();

        assertNotNull(proyectosEncontrados);
        assertEquals(2, proyectosEncontrados.size());
        assertTrue(proyectosEncontrados.stream()
                .anyMatch(e -> "Proyecto Demo p1".equals(e.getNombre())));
        assertTrue(proyectosEncontrados.stream()
                .anyMatch(e -> "Proyecto Demo p1".equals(e.getNombre())));

    }

    @Test
    void actualizarProyecto() {

        Proyecto proyectoOriginal = new Proyecto();
        proyectoOriginal.setNombre("Proyecto Demo p1");
        proyectoOriginal.setDescripcion("Proycto de prueba p1.");
        proyectoOriginal.setFechaInicio(LocalDate.now());
        proyectoOriginal.setFechaFin(LocalDate.now().plusMonths(6));
        Proyecto proyectoGuardado = proyectoRepository.save(proyectoOriginal);

        Proyecto proyecto2 = new Proyecto();
        proyecto2.setNombre("Proyecto Demo p2");
        proyecto2.setDescripcion("Proycto de prueba p2.");
        proyecto2.setFechaInicio(LocalDate.now());
        proyecto2.setFechaFin(LocalDate.now().plusMonths(6));

        Proyecto resultado = proyectoService.actualizar(proyectoGuardado.getId(), proyecto2);

        assertEquals(proyectoOriginal.getId(), resultado.getId());
        assertEquals("Proyecto Demo p2", resultado.getNombre());

    }

    @Test
    void actualizarProyectoNoExistente() {

        Long idInexistente = 999L;
        Proyecto proyecto2 = new Proyecto();
        proyecto2.setNombre("Proyecto Demo p2");
        proyecto2.setDescripcion("Proycto de prueba p2.");
        proyecto2.setFechaInicio(LocalDate.now());
        proyecto2.setFechaFin(LocalDate.now().plusMonths(6));

        assertThrows(ProyectoNoEncontradoException.class, () -> {
            proyectoService.actualizar(idInexistente, proyecto2);
        });

    }

    @Test
    void buscarProyectoActivo() {

        Proyecto proyecto1 = new Proyecto();
        proyecto1.setNombre("Proyecto Demo p1");
        proyecto1.setDescripcion("Proycto de prueba p1.");
        proyecto1.setFechaInicio(LocalDate.now());
        proyecto1.setFechaFin(LocalDate.now().plusMonths(1));
        proyectoRepository.save(proyecto1);

        Proyecto proyecto2 = new Proyecto();
        proyecto2.setNombre("Proyecto Demo p2");
        proyecto2.setDescripcion("Proycto de prueba p2.");
        proyecto2.setFechaInicio(LocalDate.now());
        proyecto2.setFechaFin(LocalDate.now().plusMonths(2));
        proyectoRepository.save(proyecto2);

        Proyecto proyecto3 = new Proyecto();
        proyecto3.setNombre("Proyecto Demo p3");
        proyecto3.setDescripcion("Proycto de prueba p3.");
        proyecto3.setFechaInicio(LocalDate.now());
        proyecto3.setFechaFin(LocalDate.now().minusMonths(1));
        proyectoRepository.save(proyecto3);

        List<Proyecto> proyectosActivos = proyectoService.buscarPorProyectosActivos();

        assertNotNull(proyectosActivos);
        assertEquals(2, proyectosActivos.size());
        assertTrue(proyectosActivos.stream().anyMatch(p -> "Proyecto Demo p1".equals(p.getNombre())));
        assertTrue(proyectosActivos.stream().anyMatch(p -> "Proyecto Demo p2".equals(p.getNombre())));
        assertFalse(proyectosActivos.stream().anyMatch(p -> "Proyecto Demo p3".equals(p.getNombre())));

    }

    @Test
    void eliminarProyecto() {

        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Proyecto Demo p1");
        proyecto.setDescripcion("Proycto de prueba p1.");
        proyecto.setFechaInicio(LocalDate.now());
        proyecto.setFechaFin(LocalDate.now().plusMonths(6));
        Proyecto proyectoGuardado = proyectoRepository.save(proyecto);

        proyectoService.eliminar(proyectoGuardado.getId());

        assertFalse(proyectoRepository.existsById(proyectoGuardado.getId()));

    }

    @Test
    void eliminarProyectoNoExistente() {

        Long idInexistente = 999L;

        assertThrows(ProyectoNoEncontradoException.class, () -> {
            proyectoService.eliminar(idInexistente);
        });
        
    }
}
