package com.example.ejercicio2.controller;

import com.example.ejercicio2.model.Guardia;
import com.example.ejercicio2.repository.GuardiaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@RequestMapping("/guardias")
public class GuardiaController {

    private final GuardiaRepository guardiaRepository;

    public GuardiaController(GuardiaRepository guardiaRepository) {
        this.guardiaRepository = guardiaRepository;
    }

    @GetMapping
    public String listar(Authentication authentication, Model model) {
        model.addAttribute("username", authentication.getName());
        model.addAttribute("guardias", guardiaRepository.findAllByOrderByFechaDescHoraIngresoDesc());
        return "guardias";
    }

    @PostMapping
    public String registrar(@RequestParam String nombre,
                            @RequestParam LocalDate fecha,
                            @RequestParam LocalTime horaIngreso,
                            RedirectAttributes redirectAttributes) {
        Guardia guardia = new Guardia(nombre, fecha, horaIngreso);
        guardiaRepository.save(guardia);
        redirectAttributes.addFlashAttribute("mensaje", "Guardia registrado exitosamente.");
        return "redirect:/guardias";
    }
}

