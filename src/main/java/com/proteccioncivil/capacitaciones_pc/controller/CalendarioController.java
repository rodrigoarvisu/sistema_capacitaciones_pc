package com.proteccioncivil.capacitaciones_pc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.proteccioncivil.capacitaciones_pc.entity.Capacitacion;
import com.proteccioncivil.capacitaciones_pc.entity.TipoInmueble;
import com.proteccioncivil.capacitaciones_pc.repository.TipoInmuebleRepository;
import com.proteccioncivil.capacitaciones_pc.service.CapacitacionService;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/calendario")
public class CalendarioController {

    private final CapacitacionService capacitacionService;
    private final TipoInmuebleRepository tipoInmuebleRepository;

    public CalendarioController(CapacitacionService capacitacionService,
                                TipoInmuebleRepository tipoInmuebleRepository){
        this.capacitacionService = capacitacionService;
        this.tipoInmuebleRepository = tipoInmuebleRepository;
    }

    @GetMapping
    public String calendario(Model model) throws JsonProcessingException {
        List<Capacitacion> caps = capacitacionService.obtenerTodas();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        model.addAttribute("capacitacionesJson", mapper.writeValueAsString(caps));

        List<TipoInmueble> tipos = tipoInmuebleRepository.findAll();
        Map<String, String> coloresPorTipo = tipos.stream()
                        .collect(Collectors.toMap(TipoInmueble::getNombre, TipoInmueble::getColor));
        model.addAttribute("coloresTipoInmuebleJson", mapper.writeValueAsString(coloresPorTipo));
        model.addAttribute("tiposInmueble", tipos);

        model.addAttribute("paginaActiva", "calendario");
        return "calendario";
    }
}