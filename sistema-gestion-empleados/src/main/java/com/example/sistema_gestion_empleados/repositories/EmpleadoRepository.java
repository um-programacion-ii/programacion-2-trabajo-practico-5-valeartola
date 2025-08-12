package com.example.sistema_gestion_empleados.repositories;

import com.example.sistema_gestion_empleados.entidades.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

}