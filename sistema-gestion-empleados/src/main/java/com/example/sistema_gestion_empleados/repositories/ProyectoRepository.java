package com.example.sistema_gestion_empleados.repositories;

import com.example.sistema_gestion_empleados.models.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
    List<Proyecto> findByFechaFinAfter(LocalDate fechaFin);

}