package com.proteccioncivil.capacitaciones_pc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CapacitacionController {

    @GetMapping("/")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/capacitaciones")
    public String listarCapacitaciones(Model model) {

        model.addAttribute("titulo", "Sistema de Capacitaciones");

        return "capacitaciones";
    }
}