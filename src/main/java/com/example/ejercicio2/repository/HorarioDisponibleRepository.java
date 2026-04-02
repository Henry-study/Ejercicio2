package com.example.ejercicio2.repository;

import com.example.ejercicio2.model.HorarioDisponible;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HorarioDisponibleRepository extends JpaRepository<HorarioDisponible, Long> {
    
    // Obtener horarios activos ordenados por campo orden y luego por hora
    List<HorarioDisponible> findByActivoTrueOrderByOrdenAscHoraAsc();
    
    // Obtener todos los horarios ordenados
    List<HorarioDisponible> findAllByOrderByOrdenAscHoraAsc();
    
    // Verificar si existe un horario específico
    boolean existsByHora(java.time.LocalTime hora);
}
