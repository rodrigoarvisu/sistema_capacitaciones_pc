package com.proteccioncivil.capacitaciones_pc.controller;

import com.proteccioncivil.capacitaciones_pc.entity.Capacitacion;
import com.proteccioncivil.capacitaciones_pc.repository.EstatusRepository;
import com.proteccioncivil.capacitaciones_pc.repository.TipoCapacitacionRepository;
import com.proteccioncivil.capacitaciones_pc.repository.TipoInmuebleRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CapacitacionController {

    private final TipoInmuebleRepository tipoInmuebleRepository;
    private final TipoCapacitacionRepository tipoCapacitacionRepository;
    private final EstatusRepository estatusRepository;

    public CapacitacionController(TipoInmuebleRepository tipoInmuebleRepository,
                                  TipoCapacitacionRepository tipoCapacitacionRepository,
                                  EstatusRepository estatusRepository) {
        this.tipoInmuebleRepository = tipoInmuebleRepository;
        this.tipoCapacitacionRepository = tipoCapacitacionRepository;
        this.estatusRepository = estatusRepository;
    }

    @GetMapping("/")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/capacitaciones")
    public String listarCapacitaciones(Model model) {

        model.addAttribute("titulo", "Sistema de Capacitaciones");
        model.addAttribute("capacitacion", new Capacitacion());
        model.addAttribute("tiposInmueble", tipoInmuebleRepository.findAll());
        model.addAttribute("tiposCapacitacion", tipoCapacitacionRepository.findAll());
        model.addAttribute("estatusLista", estatusRepository.findAll());

        return "capacitaciones";
    }
}