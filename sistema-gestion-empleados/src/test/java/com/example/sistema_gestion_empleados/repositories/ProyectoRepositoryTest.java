package com.example.sistema_gestion_empleados.repositories;

import com.example.sistema_gestion_empleados.models.Proyecto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ProyectoRepositoryTest {

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        proyectoRepository.deleteAll();
    }

    @Test
    void buscarProyectoActivo() {
        // Arrange
        Proyecto proyecto1 = new Proyecto();
        proyecto1.setNombre("Proyecto Demo p1");
        proyecto1.setDescripcion("Proycto de prueba p1.");
        proyecto1.setFechaInicio(LocalDate.now());
        proyecto1.setFechaFin(LocalDate.now().plusMonths(1));


        Proyecto proyecto2 = new Proyecto();
        proyecto2.setNombre("Proyecto Demo p2");
        proyecto2.setDescripcion("Proycto de prueba p2.");
        proyecto2.setFechaInicio(LocalDate.now());
        proyecto2.setFechaFin(LocalDate.now().plusMonths(10));


        Proyecto proyecto3 = new Proyecto();
        proyecto3.setNombre("Proyecto Demo p3");
        proyecto3.setDescripcion("Proycto de prueba p3.");
        proyecto3.setFechaInicio(LocalDate.now());
        proyecto3.setFechaFin(LocalDate.now().minusMonths(1));

        entityManager.persist(proyecto1);
        entityManager.persist(proyecto2);
        entityManager.persist(proyecto3);

        // Act
        List<Proyecto> proyectosActivos = proyectoRepository.findByFechaFinAfter(LocalDate.now());

        // Assert
        assertNotNull(proyectosActivos);
        assertEquals(2, proyectosActivos.size());
        assertTrue(proyectosActivos.stream().anyMatch(p -> "Proyecto Demo p1".equals(p.getNombre())));
        assertTrue(proyectosActivos.stream().anyMatch(p -> "Proyecto Demo p2".equals(p.getNombre())));
        assertFalse(proyectosActivos.stream().anyMatch(p -> "Proyecto Demo p3".equals(p.getNombre())));
    }
}
