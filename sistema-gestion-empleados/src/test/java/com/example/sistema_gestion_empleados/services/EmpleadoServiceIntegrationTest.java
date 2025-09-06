package com.example.sistema_gestion_empleados.services;

import com.example.sistema_gestion_empleados.exceptions.EmailDuplicadoException;
import com.example.sistema_gestion_empleados.exceptions.EmpleadoNoEncontradoException;
import com.example.sistema_gestion_empleados.models.Departamento;
import com.example.sistema_gestion_empleados.models.Empleado;
import com.example.sistema_gestion_empleados.repositories.DepartamentoRepository;
import com.example.sistema_gestion_empleados.repositories.EmpleadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.transaction.Transactional;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class EmpleadoServiceIntegrationTest {

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    private Departamento departamentoTest;

    @BeforeEach
    void setUp() {

        empleadoRepository.deleteAll();
        departamentoRepository.deleteAll();

        departamentoTest = new Departamento();
        departamentoTest.setNombre("RRHH");
        departamentoTest.setDescripcion("Recursos Humanos");
        departamentoTest = departamentoRepository.save(departamentoTest);

    }

    @Test
    void guardaEmpleado() {

        Empleado empleado = new Empleado();
        empleado.setNombre("Juan");
        empleado.setApellido("Pérez");
        empleado.setEmail("juan.perez@empresa.com");
        empleado.setFechaContratacion(LocalDate.now());
        empleado.setSalario(new BigDecimal("50000.00"));
        empleado.setDepartamento(departamentoTest);

        Empleado empleadoGuardado = empleadoService.guardar(empleado);

        assertNotNull(empleadoGuardado.getId());
        assertEquals("juan.perez@empresa.com", empleadoGuardado.getEmail());
        assertTrue(empleadoRepository.existsById(empleadoGuardado.getId()));

    }

    @Test
    void obtenerTodos() {

        Empleado empleado1 = new Empleado();
        empleado1.setNombre("Carlos");
        empleado1.setApellido("López");
        empleado1.setEmail("carlos.lopez@empresa.com");
        empleado1.setFechaContratacion(LocalDate.now());
        empleado1.setSalario(new BigDecimal("60000.00"));
        empleado1.setDepartamento(departamentoTest);
        empleadoRepository.save(empleado1);

        Empleado empleado2 = new Empleado();
        empleado2.setNombre("Ana");
        empleado2.setApellido("García");
        empleado2.setEmail("ana.garcia@empresa.com");
        empleado2.setFechaContratacion(LocalDate.now());
        empleado2.setSalario(new BigDecimal("55000.00"));
        empleado2.setDepartamento(departamentoTest);
        empleadoRepository.save(empleado2);

        List<Empleado> empleadosEncontrados = empleadoService.obtenerTodos();

        assertNotNull(empleadosEncontrados);
        assertEquals(2, empleadosEncontrados.size());
        assertTrue(empleadosEncontrados.stream()
                .anyMatch(e -> "carlos.lopez@empresa.com".equals(e.getEmail())));
        assertTrue(empleadosEncontrados.stream()
                .anyMatch(e -> "ana.garcia@empresa.com".equals(e.getEmail())));

    }

    @Test
    void buscarPorId() {

        Empleado empleado = new Empleado();
        empleado.setNombre("Mauro");
        empleado.setApellido("Codina");
        empleado.setEmail("maurocodina@empresa.com");
        empleado.setFechaContratacion(LocalDate.now());
        empleado.setSalario(new BigDecimal("55000.00"));
        empleado.setDepartamento(departamentoTest);

        Empleado empleadoGuardado = empleadoRepository.save(empleado);

        Empleado resultado = empleadoService.buscarPorId(empleadoGuardado.getId());

        assertNotNull(resultado);
        assertEquals(empleadoGuardado.getId(), resultado.getId());
        assertEquals("maurocodina@empresa.com", resultado.getEmail());

    }


    @Test
    void buscarPorIdNoExistente() {

        Long idInexistente = 999L;

        assertThrows(EmpleadoNoEncontradoException.class, () -> {
            empleadoService.buscarPorId(idInexistente);
        });

    }

    @Test
    void actualizarEmpleado() {

        Empleado empleadoOriginal = new Empleado();
        empleadoOriginal.setNombre("Maria");
        empleadoOriginal.setApellido("Gomez");
        empleadoOriginal.setEmail("mariagomez@empresa.com");
        empleadoOriginal.setFechaContratacion(LocalDate.now());
        empleadoOriginal.setSalario(new BigDecimal("60000.00"));
        empleadoOriginal.setDepartamento(departamentoTest);
        Empleado empleadoGuardado = empleadoRepository.save(empleadoOriginal);

        Empleado empleadoActualizado = new Empleado();
        empleadoActualizado.setNombre("Maria Jose");
        empleadoActualizado.setApellido("Gomez Jota");
        empleadoActualizado.setEmail("mariajjo.gomez@empresa.com");
        empleadoActualizado.setSalario(new BigDecimal("70000.00"));
        empleadoActualizado.setDepartamento(departamentoTest);

        Empleado resultado = empleadoService.actualizar(empleadoGuardado.getId(), empleadoActualizado);

        assertEquals(empleadoGuardado.getId(), resultado.getId()); // El ID debe ser el mismo
        assertEquals("Maria Jose", resultado.getNombre());
        assertEquals("Gomez Jota", resultado.getApellido());
        assertEquals("mariajjo.gomez@empresa.com", resultado.getEmail());
        assertEquals(new BigDecimal("70000.00"), resultado.getSalario());

    }

    @Test
    void empleadoConEmailDuplicado() {

        Empleado empleado1 = new Empleado();
        empleado1.setNombre("marta");
        empleado1.setApellido("Lopez");
        empleado1.setEmail("m.lopez@empresa.com");
        empleado1.setFechaContratacion(LocalDate.now());
        empleado1.setSalario(new BigDecimal("45000.00"));
        empleado1.setDepartamento(departamentoTest);
        empleadoService.guardar(empleado1);

        Empleado empleado2 = new Empleado();
        empleado2.setNombre("Maria");
        empleado2.setApellido("Gimenez");
        empleado2.setEmail("m.lopez@empresa.com"); // Email duplicado
        empleado2.setFechaContratacion(LocalDate.now());
        empleado2.setSalario(new BigDecimal("50000.00"));
        empleado2.setDepartamento(departamentoTest);

        assertThrows(EmailDuplicadoException.class, () -> {
            empleadoService.guardar(empleado2);
        });

    }

    @Test
    void eliminarEmpleado() {

        Empleado empleado = new Empleado();
        empleado.setNombre("Maria");
        empleado.setApellido("Gomez");
        empleado.setEmail("mariagomez@empresa.com");
        empleado.setFechaContratacion(LocalDate.now());
        empleado.setSalario(new BigDecimal("55000.00"));
        empleado.setDepartamento(departamentoTest);
        Empleado empleadoGuardado = empleadoRepository.save(empleado);

        empleadoService.eliminar(empleadoGuardado.getId());

        assertFalse(empleadoRepository.existsById(empleadoGuardado.getId()));

    }

    @Test
    void elimianrEmpleadoNoExistente() {

        Long idNoExistente = 999L;

        assertThrows(RuntimeException.class, () -> empleadoService.eliminar(idNoExistente));

    }
}