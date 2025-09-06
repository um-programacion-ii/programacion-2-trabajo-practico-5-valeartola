package com.example.sistema_gestion_empleados.controllers;



import com.example.sistema_gestion_empleados.exceptions.EmpleadoNoEncontradoException;
import com.example.sistema_gestion_empleados.models.Departamento;
import com.example.sistema_gestion_empleados.models.Empleado;
import com.example.sistema_gestion_empleados.services.EmpleadoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@WebMvcTest(EmpleadoController.class)
public class EmpleadoControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean
    private EmpleadoService empleadoService;

    private Empleado empleadoTest;
    private Departamento departamentoTest;


    @BeforeEach
    void setUp() {
        departamentoTest = new Departamento();
        departamentoTest.setId(1L);
        departamentoTest.setNombre("IT");
        departamentoTest.setDescripcion("Departamento de Tecnología");


        empleadoTest = new Empleado();
        empleadoTest.setId(1L);
        empleadoTest.setNombre("Maria");
        empleadoTest.setApellido("Gimenez");
        empleadoTest.setEmail("mariagimenez@empresa.com");
        empleadoTest.setDepartamento(departamentoTest);
        empleadoTest.setProyectos(Collections.emptySet());
        empleadoTest.setFechaContratacion(LocalDate.now());
        empleadoTest.setSalario(new BigDecimal("40000"));
    }

    @Test
    void obtenerTodos() throws Exception {
        given(empleadoService.obtenerTodos()).willReturn(List.of(empleadoTest));

        mockMvc.perform(get("/api/empleados"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].nombre").value("Maria"))
                .andExpect(jsonPath("$[0].departamento.nombre").value("IT"));
    }

    @Test
    void obtenerTodosPorId() throws Exception {
        given(empleadoService.buscarPorId(1L)).willReturn(empleadoTest);

        mockMvc.perform(get("/api/empleados/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Maria"));
    }

    @Test
    void buscarPorRangoSalario() throws Exception {
        given(empleadoService.buscarPorRangoSalario(new BigDecimal(20000), new BigDecimal(60000))).willReturn(List.of(empleadoTest));

        mockMvc.perform(get("/api/empleados/salario")
                        .param("min", "20000")
                        .param("max", "60000"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nombre").value("Maria"))
                .andExpect(jsonPath("$[0].salario").value(40000));
    }

    @Test
    void obtenerPorDepartamento() throws Exception {
        given(empleadoService.buscarPorDepartamento("IT")).willReturn(List.of(empleadoTest));

        mockMvc.perform(get("/api/empleados/departamento/IT"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nombre").value("Maria"));
    }

    @Test
    void crearEmpleado() throws Exception {
        given(empleadoService.guardar(any(Empleado.class))).willReturn(empleadoTest);

        mockMvc.perform(post("/api/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(empleadoTest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Maria")));
    }

    @Test
    void actualizarEmpleado() throws Exception {
        Empleado empleadoActualizado = new Empleado( 1L, "Maria", "Gimenez", "mariagimenez@empresa.com", LocalDate.now(), new BigDecimal(60000), departamentoTest, Collections.emptySet() );

        given(empleadoService.actualizar(anyLong(), any(Empleado.class))).willReturn(empleadoActualizado);

        mockMvc.perform(put("/api/empleados/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(empleadoActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.salario", is(60000)));
    }

    @Test
    void actualizarEmpleadoNoExistente() throws Exception {

        given(empleadoService.actualizar(eq(50L) , any(Empleado.class)))
                .willThrow(new EmpleadoNoEncontradoException("Empleado no encontrado"));

        mockMvc.perform(put("/api/empleados/{id}", 50L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(empleadoTest)))

                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Recurso no encontrado"))
                .andExpect(jsonPath("$.message").value("Empleado no encontrado"))
                .andExpect(jsonPath("$.path").value("/api/empleados/50"));
    }

    @Test
    void eliminarEmpleado() throws Exception {
        doNothing().when(empleadoService).eliminar(1L);

        mockMvc.perform(delete("/api/empleados/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminarEmpleadoNoExistente() throws Exception {
        willThrow(new EmpleadoNoEncontradoException("Empleado no encontrado"))
                .given(empleadoService).eliminar(77L);

        mockMvc.perform(delete("/api/empleados/{id}", 77L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Recurso no encontrado"))
                .andExpect(jsonPath("$.message").value("Empleado no encontrado"))
                .andExpect(jsonPath("$.path").value("/api/empleados/77"));
    }

    
}
