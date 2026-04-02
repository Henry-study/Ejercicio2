package com.example.ejercicio2.config;

import com.example.ejercicio2.model.HorarioDisponible;
import com.example.ejercicio2.model.User;
import com.example.ejercicio2.repository.HorarioDisponibleRepository;
import com.example.ejercicio2.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final HorarioDisponibleRepository horarioDisponibleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, HorarioDisponibleRepository horarioDisponibleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.horarioDisponibleRepository = horarioDisponibleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Crear usuario admin si no existe
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User(
                    "admin",
                    passwordEncoder.encode("admin"),
                    "ROLE_ADMIN"
            );
            userRepository.save(admin);
            System.out.println(">>> Usuario admin creado (password: admin)");
        }

        // Crear usuario user si no existe
        if (userRepository.findByUsername("user").isEmpty()) {
            User user = new User(
                    "user",
                    passwordEncoder.encode("password"),
                    "ROLE_USER"
            );
            userRepository.save(user);
            System.out.println(">>> Usuario user creado (password: password)");
        }

        // Crear horarios predefinidos si no existen
        if (horarioDisponibleRepository.count() == 0) {
            crearHorariosPredefinidos();
            System.out.println(">>> Horarios predefinidos creados");
        }
    }

    private void crearHorariosPredefinidos() {
        HorarioDisponible[] horarios = {
            new HorarioDisponible(LocalTime.of(6, 0), "06:00 - Turno Madrugada", true, 1),
            new HorarioDisponible(LocalTime.of(7, 0), "07:00 - Turno Mañana Temprano", true, 2),
            new HorarioDisponible(LocalTime.of(8, 0), "08:00 - Turno Mañana", true, 3),
            new HorarioDisponible(LocalTime.of(9, 0), "09:00 - Turno Mañana Tardío", true, 4),
            new HorarioDisponible(LocalTime.of(14, 0), "14:00 - Turno Tarde", true, 5),
            new HorarioDisponible(LocalTime.of(15, 0), "15:00 - Turno Tarde Medio", true, 6),
            new HorarioDisponible(LocalTime.of(18, 0), "18:00 - Turno Tarde-Noche", true, 7),
            new HorarioDisponible(LocalTime.of(20, 0), "20:00 - Turno Noche Temprano", true, 8),
            new HorarioDisponible(LocalTime.of(22, 0), "22:00 - Turno Noche", true, 9),
            new HorarioDisponible(LocalTime.of(0, 0), "00:00 - Turno Medianoche", true, 10)
        };

        for (HorarioDisponible horario : horarios) {
            horarioDisponibleRepository.save(horario);
        }
    }
}

