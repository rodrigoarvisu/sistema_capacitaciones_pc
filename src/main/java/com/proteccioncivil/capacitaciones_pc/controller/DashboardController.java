package com.proteccioncivil.capacitaciones_pc.controller;

import com.proteccioncivil.capacitaciones_pc.repository.CapacitacionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final CapacitacionRepository capacitacionRepository;

    public DashboardController(CapacitacionRepository capacitacionRepository) {
        this.capacitacionRepository = capacitacionRepository;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("titulo", "Sistema de Capacitaciones");
        model.addAttribute("capacitaciones", capacitacionRepository.findAll());
        model.addAttribute("paginaActiva", "dashboard");
        return "dashboard";
    }
}