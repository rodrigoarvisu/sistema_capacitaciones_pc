package com.proteccioncivil.capacitaciones_pc.controller;

import com.proteccioncivil.capacitaciones_pc.entity.Capacitacion;
import com.proteccioncivil.capacitaciones_pc.repository.EstatusRepository;
import com.proteccioncivil.capacitaciones_pc.repository.InstructorRepository;
import com.proteccioncivil.capacitaciones_pc.repository.TipoCapacitacionRepository;
import com.proteccioncivil.capacitaciones_pc.repository.TipoInmuebleRepository;
import com.proteccioncivil.capacitaciones_pc.service.ArchivoService;
import com.proteccioncivil.capacitaciones_pc.service.CapacitacionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/capacitaciones")
public class CapacitacionController {

    private final TipoInmuebleRepository tipoInmuebleRepository;
    private final TipoCapacitacionRepository tipoCapacitacionRepository;
    private final EstatusRepository estatusRepository;
    private final CapacitacionService capacitacionService;
    private final InstructorRepository instructorRepository;
    private final ArchivoService archivoService;

    public CapacitacionController(TipoInmuebleRepository tipoInmuebleRepository,
                                  TipoCapacitacionRepository tipoCapacitacionRepository,
                                  EstatusRepository estatusRepository,
                                  CapacitacionService capacitacionService,
                                  InstructorRepository instructorRepository,
                                  ArchivoService archivoService) {
        this.tipoInmuebleRepository = tipoInmuebleRepository;
        this.tipoCapacitacionRepository = tipoCapacitacionRepository;
        this.estatusRepository = estatusRepository;
        this.capacitacionService = capacitacionService;
        this.instructorRepository = instructorRepository;
        this.archivoService = archivoService;
    }

    @GetMapping
    public String listarCapacitaciones(Model model) {

        model.addAttribute("titulo", "Sistema de Capacitaciones");
        model.addAttribute("capacitacion", new Capacitacion());
        model.addAttribute("tiposInmueble", tipoInmuebleRepository.findAll());
        model.addAttribute("tiposCapacitacion", tipoCapacitacionRepository.findAll());
        model.addAttribute("estatusLista", estatusRepository.findAll());
        model.addAttribute("paginaActiva", "capacitaciones");
        model.addAttribute("instructor", instructorRepository.findByActivoTrue());

        return "capacitaciones";
    }

    @PostMapping("/guardar")
    public String guardarCapacitaciones(
            @ModelAttribute Capacitacion capacitacion,
            @RequestParam(value = "archivo", required = false)
            MultipartFile archivo) {
        try {
            Capacitacion guardada = capacitacionService.guardar(capacitacion);

            if (archivo != null && !archivo.isEmpty()) {
                System.out.println("Archivo recibido: " + archivo.getOriginalFilename());
                System.out.println("Content-Type: " + archivo.getContentType());
                System.out.println("Tamaño: " + archivo.getSize());

                String rutaArchivo = archivoService.guardarLista(archivo, guardada.getId());
                guardada.setArchivoLista(rutaArchivo);
                capacitacionService.guardar(guardada);

                System.out.println("Ruta guardada en BD: " + guardada.getArchivoLista());
            } else {
                System.out.println("El archivo llegó null o vacío");
            }

            return "redirect:/capacitaciones?guardado=true";

        } catch (Exception e) {   // <-- capturamos TODO, no solo IOException
            e.printStackTrace();
            return "redirect:/capacitaciones?errorArchivo=true";
        }
    }

    @GetMapping("/editar/{id}")
    public String editarCapacitacion(@PathVariable Long id, Model model) {

        Capacitacion capacitacion = capacitacionService.obtenerPorId(id);

        model.addAttribute("titulo", "Editar capacitación");
        model.addAttribute("capacitacion", capacitacion);
        model.addAttribute("tiposInmueble", tipoInmuebleRepository.findAll());
        model.addAttribute("tiposCapacitacion", tipoCapacitacionRepository.findAll());
        model.addAttribute("estatusLista", estatusRepository.findAll());
        model.addAttribute("instructor", instructorRepository.findAll());
        model.addAttribute("paginaActiva", "capacitaciones");

        return "capacitaciones";
    }
}