package com.proteccioncivil.capacitaciones_pc.controller;

import com.proteccioncivil.capacitaciones_pc.dto.ConsultaFiltroDTO;
import com.proteccioncivil.capacitaciones_pc.entity.Capacitacion;
import com.proteccioncivil.capacitaciones_pc.repository.CapacitacionRepository;
import com.proteccioncivil.capacitaciones_pc.repository.CapacitacionSpecification;
import com.proteccioncivil.capacitaciones_pc.repository.EstatusRepository;
import com.proteccioncivil.capacitaciones_pc.repository.InstructorRepository;
import com.proteccioncivil.capacitaciones_pc.repository.TipoCapacitacionRepository;
import com.proteccioncivil.capacitaciones_pc.repository.TipoInmuebleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/consulta-historica")
public class ConsultaHistoricaController {

    private final CapacitacionRepository capacitacionRepository;
    private final TipoInmuebleRepository tipoInmuebleRepository;
    private final TipoCapacitacionRepository tipoCapacitacionRepository;
    private final EstatusRepository estatusRepository;
    private final InstructorRepository instructorRepository;

    public ConsultaHistoricaController(CapacitacionRepository capacitacionRepository,
                                       TipoInmuebleRepository tipoInmuebleRepository,
                                       TipoCapacitacionRepository tipoCapacitacionRepository,
                                       EstatusRepository estatusRepository,
                                       InstructorRepository instructorRepository) {
        this.capacitacionRepository    = capacitacionRepository;
        this.tipoInmuebleRepository    = tipoInmuebleRepository;
        this.tipoCapacitacionRepository = tipoCapacitacionRepository;
        this.estatusRepository         = estatusRepository;
        this.instructorRepository      = instructorRepository;
    }

    @GetMapping
    public String consulta(
            @ModelAttribute ConsultaFiltroDTO filtro,
            @RequestParam(defaultValue = "0")  int pagina,
            @RequestParam(defaultValue = "10") int tamano,
            Model model) {

        Pageable pageable = PageRequest.of(pagina, tamano,
                Sort.by(Sort.Direction.DESC, "fecha"));

        Page<Capacitacion> resultado = capacitacionRepository.findAll(
                CapacitacionSpecification.conFiltros(filtro), pageable);

        // Datos para la vista
        model.addAttribute("filtro",            filtro);
        model.addAttribute("resultado",         resultado);
        model.addAttribute("capacitaciones",    resultado.getContent());
        model.addAttribute("totalResultados",   resultado.getTotalElements());
        model.addAttribute("totalPaginas",      resultado.getTotalPages());
        model.addAttribute("paginaActual",      pagina);
        model.addAttribute("tamano",            tamano);

        // Catálogos para los selects
        model.addAttribute("tiposInmueble",    tipoInmuebleRepository.findAll());
        model.addAttribute("tiposCapacitacion", tipoCapacitacionRepository.findAll());
        model.addAttribute("estatusLista",     estatusRepository.findAll());
        model.addAttribute("instructores",     instructorRepository.findByActivoTrue());

        model.addAttribute("paginaActiva", "consultaHistorica");
        return "consulta-historica";
    }
}