package com.example.sistema_gestion_empleados.repositories;

import com.example.sistema_gestion_empleados.models.Departamento;
import com.example.sistema_gestion_empleados.models.Empleado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class EmpleadoRepositoryTest {
    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private TestEntityManager entityManager; // Para gestionar el estado de la base de datos de prueba

    private Departamento departamentoTest;

    @BeforeEach
    void setup() {

        empleadoRepository.deleteAll();
        departamentoTest = new Departamento();
        departamentoTest.setNombre("IT");
        departamentoTest.setDescripcion("Departamento de Tecnología");
        entityManager.persist(departamentoTest);

    }

    @Test
    void buscarPorEmail() {

        Empleado empleado = new Empleado();
        empleado.setNombre("Maria");
        empleado.setApellido("Gimenez");
        empleado.setEmail("mariagimenez@empresa.com");
        empleado.setDepartamento(departamentoTest);
        empleado.setProyectos(Collections.emptySet());
        empleado.setFechaContratacion(LocalDate.now());
        empleado.setSalario(new BigDecimal("40000"));
        entityManager.persist(empleado);

        Optional<Empleado> resultado = empleadoRepository.findByEmail("mariagimenez@empresa.com");

        assertTrue(resultado.isPresent());
        assertEquals("Maria", resultado.get().getNombre());

    }

    @Test
    void buscarPorEmailNoExistente() {

        Optional<Empleado> resultado = empleadoRepository.findByEmail("noexiste@empresa.com");

        assertFalse(resultado.isPresent());

    }

    @Test
    void buscarPorSalario() {

        Empleado empleado1 = new Empleado();
        empleado1.setNombre("Ana");
        empleado1.setApellido("Lopez");
        empleado1.setEmail("analopez@empresa.com");
        empleado1.setFechaContratacion(LocalDate.now());
        empleado1.setSalario(new BigDecimal("45000.00"));
        empleado1.setDepartamento(departamentoTest);
        entityManager.persist(empleado1);

        Empleado empleado2 = new Empleado();
        empleado2.setNombre("Maria");
        empleado2.setApellido("Gimenez");
        empleado2.setEmail("mariagimenez@empresa.com");
        empleado2.setFechaContratacion(LocalDate.now());
        empleado2.setSalario(new BigDecimal("65000.00"));
        empleado2.setDepartamento(departamentoTest);
        entityManager.persist(empleado2);

        Empleado empleado3 = new Empleado();
        empleado3.setNombre("Luis");
        empleado3.setApellido("Gimenez");
        empleado3.setEmail("luisgimenez@empresa.com");
        empleado3.setFechaContratacion(LocalDate.now());
        empleado3.setSalario(new BigDecimal("55000.00"));
        empleado3.setDepartamento(departamentoTest);
        entityManager.persist(empleado3);

        List<Empleado> resultado = empleadoRepository.findBySalarioBetween(new BigDecimal("40000.00"), new BigDecimal("60000.00"));

        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().anyMatch(e -> "Ana".equals(e.getNombre())));
        assertTrue(resultado.stream().anyMatch(e -> "Luis".equals(e.getNombre())));
        assertFalse(resultado.stream().anyMatch(e -> "Maria".equals(e.getNombre())));

    }

    @Test
    void buscarPorDepartamento() {

        Empleado empleado1 = new Empleado();
        empleado1.setNombre("Ana");
        empleado1.setApellido("Lopez");
        empleado1.setEmail("analopez@empresa.com");
        empleado1.setFechaContratacion(LocalDate.now());
        empleado1.setSalario(new BigDecimal("45000.00"));
        empleado1.setDepartamento(departamentoTest);
        entityManager.persist(empleado1);

        Empleado empleado2 = new Empleado();
        empleado2.setNombre("Maria");
        empleado2.setApellido("Gimenez");
        empleado2.setEmail("mariagimenez@empresa.com");
        empleado2.setFechaContratacion(LocalDate.now());
        empleado2.setSalario(new BigDecimal("65000.00"));
        empleado2.setDepartamento(departamentoTest);
        entityManager.persist(empleado2);

        List<Empleado> resultado = empleadoRepository.findByNombreDepartamento("IT");

        assertEquals(2, resultado.size());

    }

    @Test
    void cuandoFindAverageSalarioByDepartamento_entoncesCalculaPromedioCorrectamente() {

        Empleado empleado1 = new Empleado();
        empleado1.setNombre("Ana");
        empleado1.setApellido("Lopez");
        empleado1.setEmail("analopez@empresa.com");
        empleado1.setFechaContratacion(LocalDate.now());
        empleado1.setSalario(new BigDecimal("100.00"));
        empleado1.setDepartamento(departamentoTest);
        entityManager.persist(empleado1);

        Empleado empleado2 = new Empleado();
        empleado2.setNombre("Maria");
        empleado2.setApellido("Gimenez");
        empleado2.setEmail("mariagimenez@empresa.com");
        empleado2.setFechaContratacion(LocalDate.now());
        empleado2.setSalario(new BigDecimal("200.00"));
        empleado2.setDepartamento(departamentoTest);
        entityManager.persist(empleado2);

        Empleado empleado3 = new Empleado();
        empleado3.setNombre("Luis");
        empleado3.setApellido("Gimenez");
        empleado3.setEmail("luisgimenez@empresa.com");
        empleado3.setFechaContratacion(LocalDate.now());
        empleado3.setSalario(new BigDecimal("300.00"));
        empleado3.setDepartamento(departamentoTest);
        entityManager.persist(empleado3);

        Optional<BigDecimal> promedio = empleadoRepository.findAverageSalarioByDepartamento(departamentoTest.getId());

        assertTrue(promedio.isPresent());
        assertEquals(new BigDecimal("200.00").stripTrailingZeros(), promedio.get().stripTrailingZeros());

    }

}
