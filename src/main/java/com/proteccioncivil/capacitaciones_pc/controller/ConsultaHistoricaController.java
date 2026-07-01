package com.proteccioncivil.capacitaciones_pc.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/consulta")
public class ConsultaHistoricaController {

    @GetMapping
    public String consulta(Model model) {
        model.addAttribute("titulo", "Consulta Historica");
        model.addAttribute("paginaActiva", "consulta");
        return "consulta";
    }
}
