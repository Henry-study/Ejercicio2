package com.example.ejercicio2.controller;

import com.example.ejercicio2.model.HorarioDisponible;
import com.example.ejercicio2.repository.HorarioDisponibleRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/admin/horarios")
public class HorarioController {

    private final HorarioDisponibleRepository horarioDisponibleRepository;

    public HorarioController(HorarioDisponibleRepository horarioDisponibleRepository) {
        this.horarioDisponibleRepository = horarioDisponibleRepository;
    }

    @GetMapping
    public String listarHorarios(Authentication authentication, Model model) {
        model.addAttribute("username", authentication.getName());
        List<HorarioDisponible> horarios = horarioDisponibleRepository.findAllByOrderByOrdenAscHoraAsc();
        model.addAttribute("horarios", horarios);
        return "admin/horarios";
    }

    @PostMapping("/agregar")
    public String agregarHorario(@RequestParam LocalTime hora,
                                @RequestParam String descripcion,
                                @RequestParam(defaultValue = "true") boolean activo,
                                @RequestParam(defaultValue = "0") int orden,
                                RedirectAttributes redirectAttributes) {
        try {
            if (horarioDisponibleRepository.existsByHora(hora)) {
                redirectAttributes.addFlashAttribute("mensajeError", "Ya existe un horario para la hora " + hora);
                return "redirect:/admin/horarios";
            }

            HorarioDisponible horario = new HorarioDisponible(hora, descripcion, activo, orden);
            horarioDisponibleRepository.save(horario);
            redirectAttributes.addFlashAttribute("mensaje", "Horario agregado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al agregar horario: " + e.getMessage());
        }
        return "redirect:/admin/horarios";
    }

    @PostMapping("/editar/{id}")
    public String editarHorario(@PathVariable Long id,
                               @RequestParam LocalTime hora,
                               @RequestParam String descripcion,
                               @RequestParam(defaultValue = "true") boolean activo,
                               @RequestParam(defaultValue = "0") int orden,
                               RedirectAttributes redirectAttributes) {
        try {
            HorarioDisponible horario = horarioDisponibleRepository.findById(id).orElse(null);
            if (horario == null) {
                redirectAttributes.addFlashAttribute("mensajeError", "Horario no encontrado.");
                return "redirect:/admin/horarios";
            }

            // Verificar si ya existe otro horario con la misma hora (excluyendo el actual)
            if (!horario.getHora().equals(hora) && horarioDisponibleRepository.existsByHora(hora)) {
                redirectAttributes.addFlashAttribute("mensajeError", "Ya existe un horario para la hora " + hora);
                return "redirect:/admin/horarios";
            }

            horario.setHora(hora);
            horario.setDescripcion(descripcion);
            horario.setActivo(activo);
            horario.setOrden(orden);
            
            horarioDisponibleRepository.save(horario);
            redirectAttributes.addFlashAttribute("mensaje", "Horario actualizado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al actualizar horario: " + e.getMessage());
        }
        return "redirect:/admin/horarios";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarHorario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            if (horarioDisponibleRepository.existsById(id)) {
                horarioDisponibleRepository.deleteById(id);
                redirectAttributes.addFlashAttribute("mensaje", "Horario eliminado exitosamente.");
            } else {
                redirectAttributes.addFlashAttribute("mensajeError", "Horario no encontrado.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al eliminar horario: " + e.getMessage());
        }
        return "redirect:/admin/horarios";
    }

    @PostMapping("/toggle/{id}")
    public String toggleActivo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            HorarioDisponible horario = horarioDisponibleRepository.findById(id).orElse(null);
            if (horario != null) {
                horario.setActivo(!horario.isActivo());
                horarioDisponibleRepository.save(horario);
                redirectAttributes.addFlashAttribute("mensaje", 
                    "Horario " + (horario.isActivo() ? "activado" : "desactivado") + " exitosamente.");
            } else {
                redirectAttributes.addFlashAttribute("mensajeError", "Horario no encontrado.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al cambiar estado: " + e.getMessage());
        }
        return "redirect:/admin/horarios";
    }
}
