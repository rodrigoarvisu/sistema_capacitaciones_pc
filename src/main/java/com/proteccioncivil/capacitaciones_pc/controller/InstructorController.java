package com.proteccioncivil.capacitaciones_pc.controller;

import com.proteccioncivil.capacitaciones_pc.entity.Instructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InstructorController {

    @GetMapping("/instructores")
    public String instructores(Model model) {

        model.addAttribute("instructor", new Instructor());
        model.addAttribute("paginaActiva", "instructores");

        return "instructores";
    }
}
