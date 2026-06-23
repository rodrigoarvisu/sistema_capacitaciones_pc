package com.proteccioncivil.capacitaciones_pc.controller;

import com.proteccioncivil.capacitaciones_pc.entity.Capacitacion;
import com.proteccioncivil.capacitaciones_pc.entity.Instructor;
import com.proteccioncivil.capacitaciones_pc.repository.CapacitacionRepository;
import com.proteccioncivil.capacitaciones_pc.service.InstructorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/instructores")
public class InstructorController {

    private final InstructorService instructorService;
    private final CapacitacionRepository capacitacionRepository;

    public InstructorController(InstructorService instructorService,
                                CapacitacionRepository capacitacionRepository) {
        this.instructorService = instructorService;
        this.capacitacionRepository = capacitacionRepository;
    }

    // Muestra la vista con el catálogo de instructores
    @GetMapping
    public String listarInstructores(Model model) {
        model.addAttribute("instructores", instructorService.obtenerTodos());
        model.addAttribute("instructor", new Instructor());
        model.addAttribute("paginaActiva", "instructores");
        return "instructores";
    }

    // Guarda un instructor nuevo o edita uno existente
    @PostMapping("/guardar")
    public String guardarInstructor(@ModelAttribute Instructor instructor) {
        // Si activo viene null (checkbox no marcado), lo forzamos a false
        if (instructor.getActivo() == null) {
            instructor.setActivo(false);
        }
        instructorService.guardar(instructor);
        return "redirect:/instructores?guardado=true";
    }

    // Cambia el estatus activo/inactivo de un instructor
    @PostMapping("/inactivar/{id}")
    public String toggleEstatus(@PathVariable Long id) {
        Instructor instructor = instructorService.obtenerPorId(id);
        if (instructor != null) {
            instructor.setActivo(!instructor.getActivo());
            instructorService.guardar(instructor);
        }
        return "redirect:/instructores";
    }

    @GetMapping("/resumen/{id}")
    @ResponseBody
    public Map<String, Object> resumen(
            @PathVariable Long id) {

        Map<String, Object> datos = new HashMap<>();

        long proximas =
                capacitacionRepository
                        .countByInstructorIdAndFechaGreaterThanEqual(
                                id,
                                LocalDate.now()
                        );

        long totales =
                capacitacionRepository
                        .countByInstructorId(id);

        LocalDate inicioMes =
                LocalDate.now().withDayOfMonth(1);

        long realizadasMes =
                capacitacionRepository
                        .countByInstructorIdAndFechaBetween(
                                id,
                                inicioMes,
                                LocalDate.now()
                        );

        Capacitacion proximaCap =
                capacitacionRepository
                        .findFirstByInstructorIdAndFechaGreaterThanEqualOrderByFechaAsc(
                                id,
                                LocalDate.now()
                        );

        List<Capacitacion> proximasCapacitaciones =
                capacitacionRepository
                        .findByInstructorIdAndFechaGreaterThanEqualOrderByFechaAsc(
                                id,
                                LocalDate.now()
                        );
        List<Capacitacion> historial =
                capacitacionRepository
                        .findByInstructorIdAndFechaLessThanOrderByFechaDesc(
                                id,
                                LocalDate.now()
                        );

        datos.put("proximas", proximas);
        datos.put("totales", totales);
        datos.put("realizadasMes", realizadasMes);

        DateTimeFormatter fechaFormatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy");

        DateTimeFormatter horaFormatter =
                DateTimeFormatter.ofPattern("HH:mm");

        if (proximaCap != null) {
            datos.put(
                    "proximaCapacitacion",
                    proximaCap.getFecha().format(fechaFormatter)
                            + " "
                            + proximaCap.getHoraInicio().format(horaFormatter)
            );
        }

        datos.put("proximasCapacitaciones", proximasCapacitaciones);
        datos.put("historial", historial);
        return datos;
    }
}