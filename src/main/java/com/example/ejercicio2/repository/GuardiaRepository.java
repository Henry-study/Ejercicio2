package com.example.ejercicio2.repository;

import com.example.ejercicio2.model.Guardia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuardiaRepository extends JpaRepository<Guardia, Long> {
    List<Guardia> findAllByOrderByFechaDescHoraIngresoDesc();
}

