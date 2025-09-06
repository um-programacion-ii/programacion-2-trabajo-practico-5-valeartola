package com.example.sistema_gestion_empleados.controllers;

import com.example.sistema_gestion_empleados.exceptions.ProyectoNoEncontradoException;
import com.example.sistema_gestion_empleados.models.Proyecto;
import com.example.sistema_gestion_empleados.services.ProyectoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProyectoController.class)
class ProyectoControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private ProyectoService proyectoService;

    private Proyecto proyectoTest;

    @BeforeEach
    void setUp() {
        proyectoTest = new Proyecto(
                1L,
                "Proyecto Demo",
                "Proyecto demo p1",
                LocalDate.now(),
                LocalDate.now().plusMonths(18),
                Collections.emptySet()
        );
    }

    @Test
    void obtenerTodos() throws Exception {
        given(proyectoService.obtenerTodos()).willReturn(List.of(proyectoTest));

        mockMvc.perform(get("/api/proyectos"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre").value("Proyecto Demo"));
    }

    @Test
    void obtenerPorId() throws Exception {
        given(proyectoService.buscarPorId(1L)).willReturn(proyectoTest);

        mockMvc.perform(get("/api/proyectos/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Proyecto Demo"));
    }

    @Test
    void crearProyecto() throws Exception {
        given(proyectoService.guardar(any(Proyecto.class))).willReturn(proyectoTest);

        mockMvc.perform(post("/api/proyectos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(proyectoTest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Proyecto Demo")));
    }

    @Test
    void actualizarProyecto() throws Exception {
        Proyecto proyectoActualizado = new Proyecto(1L, "Proyecto Demo", "Proyecto demo p2",
                LocalDate.now(), LocalDate.now().plusMonths(12), Collections.emptySet());

        given(proyectoService.actualizar(anyLong(), any(Proyecto.class))).willReturn(proyectoActualizado);

        mockMvc.perform(put("/api/proyectos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(proyectoActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fechaFin", is(LocalDate.now().plusMonths(12).toString())));
    }

    @Test
    void actualizarProyectoNoExistente() throws Exception {
        Proyecto proyectoActualizado = new Proyecto(10L, "No Esxixtente", "No existe",
                LocalDate.now(), LocalDate.now().plusMonths(9), Collections.emptySet());

        willThrow(new ProyectoNoEncontradoException("Proyecto no existe"))
                .given(proyectoService).actualizar(anyLong(), any(Proyecto.class));

        mockMvc.perform(put("/api/proyectos/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(proyectoActualizado)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Recurso no encontrado"))
                .andExpect(jsonPath("$.message").value("Proyecto no existe"))
                .andExpect(jsonPath("$.path").value("/api/proyectos/10"));
    }

    @Test
    void eliminarProyecto() throws Exception {
        doNothing().when(proyectoService).eliminar(1L);

        mockMvc.perform(delete("/api/proyectos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void obtenerProyectosActivos() throws Exception {
        given(proyectoService.buscarPorProyectosActivos()).willReturn(List.of(proyectoTest));

        mockMvc.perform(get("/api/proyectos/activos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].nombre", is(proyectoTest.getNombre())));
    }
}
