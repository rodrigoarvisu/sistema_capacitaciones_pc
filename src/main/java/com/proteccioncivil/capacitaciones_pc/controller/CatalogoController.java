package com.proteccioncivil.capacitaciones_pc.controller;

import com.proteccioncivil.capacitaciones_pc.entity.Estatus;
import com.proteccioncivil.capacitaciones_pc.entity.TipoInmueble;
import com.proteccioncivil.capacitaciones_pc.entity.TipoCapacitacion;
import com.proteccioncivil.capacitaciones_pc.repository.EstatusRepository;
import com.proteccioncivil.capacitaciones_pc.repository.TipoCapacitacionRepository;
import com.proteccioncivil.capacitaciones_pc.repository.TipoInmuebleRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/catalogos")
public class CatalogoController {
    private final TipoInmuebleRepository tipoInmuebleRepository;
    private final TipoCapacitacionRepository tipoCapacitacionRepository;
    private final EstatusRepository estatusRepository;

    public CatalogoController(TipoInmuebleRepository tipoInmuebleRepository,
                              TipoCapacitacionRepository tipoCapacitacionRepository,
                              EstatusRepository estatusRepository) {
        this.tipoInmuebleRepository = tipoInmuebleRepository;
        this.tipoCapacitacionRepository = tipoCapacitacionRepository;
        this.estatusRepository = estatusRepository;
    }

    @GetMapping
    public String catalogos(Model model) {
        model.addAttribute("tiposInmueble", tipoInmuebleRepository.findAll());
        model.addAttribute("tiposCapacitacion", tipoCapacitacionRepository.findAll());
        model.addAttribute("estatusLista", estatusRepository.findAll());
        model.addAttribute("paginaActiva", "catalogos");
        return "catalogos";
    }

        @PostMapping("/tipoInmueble/guardar")
        public String guardarTipoInmueble(@ModelAttribute TipoInmueble tipoInmueble) {
            tipoInmuebleRepository.save(tipoInmueble);
            return "redirect:/catalogos?guardado=true";
        }

        @PostMapping("/tipoInmueble/eliminar/{id}")
        public String eliminarTipoInmueble(@PathVariable Long id) {
            tipoInmuebleRepository.deleteById(id);
            return "redirect:/catalogos?eliminado=true";
        }

        @PostMapping("/tipoCapacitacion/guardar")
        public String guardarTipoCapacitacion(@ModelAttribute TipoCapacitacion tipoCapacitacion) {
            tipoCapacitacionRepository.save(tipoCapacitacion);
            return "redirect:/catalogos?guardado=true";
        }

        @PostMapping("/tipoCapacitacion/eliminar/{id}")
        public String eliminarTipoCapacitacion(@PathVariable Long id) {
            tipoCapacitacionRepository.deleteById(id);
            return "redirect:/catalogos?eliminado=true";
        }

        @PostMapping("/estatus/guardar")
        public String guardarEstatus(@ModelAttribute Estatus estatus) {
             estatusRepository.save(estatus);
             return "redirect:/catalogos?guardado=true";
        }

        @PostMapping("/estatus/eliminar/{id}")
        public String eliminarEstatus(@PathVariable Long id) {
              estatusRepository.deleteById(id);
              return "redirect:/catalogos?eliminado=true";
        }
}
