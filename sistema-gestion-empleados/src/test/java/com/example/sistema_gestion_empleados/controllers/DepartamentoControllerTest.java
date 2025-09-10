package com.example.sistema_gestion_empleados.controllers;

import com.example.sistema_gestion_empleados.exceptions.DepartamentoNoEncontradoException;
import com.example.sistema_gestion_empleados.models.Departamento;
import com.example.sistema_gestion_empleados.services.DepartamentoService;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(DepartamentoController.class)
public class DepartamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean
    private DepartamentoService departamentoService;

    private Departamento departamentoTest;

    @BeforeEach
    void setUp() {
        departamentoTest = new Departamento();
        departamentoTest.setId(1L);
        departamentoTest.setNombre("IT");
        departamentoTest.setDescripcion("Departamento de Tecnología");
    }
    @Test
    void obtenerTodos() throws Exception {
        given(departamentoService.obtenerTodos()).willReturn(List.of(departamentoTest));
        mockMvc.perform(get("/api/departamentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("IT"));
    }

    @Test
    void obtenerPorId() throws Exception {
        given(departamentoService.buscarPorId(1L)).willReturn(departamentoTest);
        mockMvc.perform(get("/api/departamentos/{id}",1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("IT"));
    }

    @Test
    void obtenerPorIdNoExistente() throws Exception {
        given(departamentoService.buscarPorId(anyLong())).willThrow(new DepartamentoNoEncontradoException("No se encontró el departamento ingresado"));
        mockMvc.perform(get("/api/departamentos/{id}", 20L))
                .andExpect(status().isNotFound());
    }

    @Test
    void crearDepartmento() throws Exception {
        given(departamentoService.guardar(departamentoTest)).willReturn(departamentoTest);
        mockMvc.perform(post("/api/departamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departamentoTest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("IT"));
    }

    @Test
    void actualizarDepartamento() throws Exception {
        Departamento departamentoActualizado = new Departamento(1L, "Informatica","Departamento de Informatica", null );
        given(departamentoService.actualizar(anyLong(), any(Departamento.class))).willReturn(departamentoActualizado);
        mockMvc.perform(put("/api/departamentos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departamentoActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Informatica"));
    }

    @Test
    void actualizarDepartamentoNoExistente() throws Exception {
        given(departamentoService.actualizar(anyLong(), any(Departamento.class)))
                .willThrow(new DepartamentoNoEncontradoException("No se encontró el departamento ingresado"));
        mockMvc.perform(put("/api/departamentos/{id}", 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departamentoTest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarDepartamento() throws Exception {
        doNothing().when(departamentoService).eliminar(1L);

        mockMvc.perform(delete("/api/departamentos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminarDepartamentoNoExistente() throws Exception {
        willThrow(new DepartamentoNoEncontradoException("No se encontró el departamento ingresado"))
                .given(departamentoService).eliminar(10L);

        mockMvc.perform(delete("/api/departamentos/10"))
                .andExpect(status().isNotFound());
    }
}
