package com.example.ejercicio2.controller;

import com.example.ejercicio2.model.Guardia;
import com.example.ejercicio2.model.HorarioDisponible;
import com.example.ejercicio2.repository.GuardiaRepository;
import com.example.ejercicio2.repository.HorarioDisponibleRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/guardias")
public class GuardiaController {

    private final GuardiaRepository guardiaRepository;
    private final HorarioDisponibleRepository horarioDisponibleRepository;

    public GuardiaController(GuardiaRepository guardiaRepository, HorarioDisponibleRepository horarioDisponibleRepository) {
        this.guardiaRepository = guardiaRepository;
        this.horarioDisponibleRepository = horarioDisponibleRepository;
    }

    @GetMapping
    public String listar(Authentication authentication, Model model) {
        model.addAttribute("username", authentication.getName());
        model.addAttribute("guardias", guardiaRepository.findAllByOrderByFechaDescHoraIngresoDesc());
        
        // Cargar horarios disponibles desde la base de datos
        List<HorarioDisponible> horariosDisponibles = horarioDisponibleRepository.findByActivoTrueOrderByOrdenAscHoraAsc();
        model.addAttribute("horariosDisponibles", horariosDisponibles);
        
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
    
    @PostMapping("/eliminar")
    @Transactional
    public String eliminarSeleccionados(@RequestParam(value = "guardiasSeleccionados", required = false) List<Long> ids,
                                       RedirectAttributes redirectAttributes) {
        try {
            if (ids != null && !ids.isEmpty()) {
                // Verificar que los guardias existen antes de eliminar
                List<Guardia> guardiasAEliminar = guardiaRepository.findAllById(ids);
                if (guardiasAEliminar.size() != ids.size()) {
                    redirectAttributes.addFlashAttribute("mensajeError", "Algunos guardias seleccionados no existen.");
                    return "redirect:/guardias";
                }
                
                guardiaRepository.deleteAllById(ids);
                int cantidad = ids.size();
                String mensaje = cantidad == 1 ? "1 guardia eliminado exitosamente." 
                                              : cantidad + " guardias eliminados exitosamente.";
                redirectAttributes.addFlashAttribute("mensaje", mensaje);
            } else {
                redirectAttributes.addFlashAttribute("mensajeError", "No se seleccionó ningún guardia para eliminar.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al eliminar los guardias: " + e.getMessage());
        }
        return "redirect:/guardias";
    }
}

