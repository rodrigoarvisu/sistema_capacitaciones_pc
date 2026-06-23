package com.proteccioncivil.capacitaciones_pc.controller;

import com.proteccioncivil.capacitaciones_pc.entity.Capacitacion;
import com.proteccioncivil.capacitaciones_pc.repository.EstatusRepository;
import com.proteccioncivil.capacitaciones_pc.repository.InstructorRepository;
import com.proteccioncivil.capacitaciones_pc.repository.TipoCapacitacionRepository;
import com.proteccioncivil.capacitaciones_pc.repository.TipoInmuebleRepository;
import com.proteccioncivil.capacitaciones_pc.service.CapacitacionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/capacitaciones")
public class CapacitacionController {

    private final TipoInmuebleRepository tipoInmuebleRepository;
    private final TipoCapacitacionRepository tipoCapacitacionRepository;
    private final EstatusRepository estatusRepository;
    private final CapacitacionService capacitacionService;
    private final InstructorRepository instructorRepository;

    public CapacitacionController(TipoInmuebleRepository tipoInmuebleRepository,
                                  TipoCapacitacionRepository tipoCapacitacionRepository,
                                  EstatusRepository estatusRepository,
                                  CapacitacionService capacitacionService,
                                  InstructorRepository instructorRepository) {
        this.tipoInmuebleRepository = tipoInmuebleRepository;
        this.tipoCapacitacionRepository = tipoCapacitacionRepository;
        this.estatusRepository = estatusRepository;
        this.capacitacionService = capacitacionService;
        this.instructorRepository = instructorRepository;
    }

    @GetMapping
    public String listarCapacitaciones(Model model) {

        model.addAttribute("titulo", "Sistema de Capacitaciones");
        model.addAttribute("capacitacion", new Capacitacion());
        model.addAttribute("tiposInmueble", tipoInmuebleRepository.findAll());
        model.addAttribute("tiposCapacitacion", tipoCapacitacionRepository.findAll());
        model.addAttribute("estatusLista", estatusRepository.findAll());
        model.addAttribute("paginaActiva", "capacitaciones");
        model.addAttribute("instructor", instructorRepository.findAll());

        return "capacitaciones";
    }

    @PostMapping("/guardar")
    public String guardarCapacitaciones(
            @ModelAttribute Capacitacion capacitacion) {
        capacitacionService.guardar(capacitacion);
        return "redirect:/capacitaciones?guardado=true";
    }
}