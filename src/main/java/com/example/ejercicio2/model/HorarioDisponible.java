package com.example.ejercicio2.model;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "horarios_disponibles")
public class HorarioDisponible {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalTime hora;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private boolean activo = true;

    @Column(nullable = false)
    private int orden = 0;

    public HorarioDisponible() {
    }

    public HorarioDisponible(LocalTime hora, String descripcion) {
        this.hora = hora;
        this.descripcion = descripcion;
        this.activo = true;
    }

    public HorarioDisponible(LocalTime hora, String descripcion, boolean activo, int orden) {
        this.hora = hora;
        this.descripcion = descripcion;
        this.activo = activo;
        this.orden = orden;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    @Override
    public String toString() {
        return "HorarioDisponible{" +
                "id=" + id +
                ", hora=" + hora +
                ", descripcion='" + descripcion + '\'' +
                ", activo=" + activo +
                ", orden=" + orden +
                '}';
    }
}
