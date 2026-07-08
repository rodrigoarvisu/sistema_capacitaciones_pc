package com.proteccioncivil.capacitaciones_pc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.proteccioncivil.capacitaciones_pc.entity.Capacitacion;
import com.proteccioncivil.capacitaciones_pc.repository.*;
import com.proteccioncivil.capacitaciones_pc.dto.DashboardFiltroDTO;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class DashboardController {

    private final CapacitacionRepository capacitacionRepository;
    private final TipoInmuebleRepository tipoInmuebleRepository;
    private final TipoCapacitacionRepository tipoCapacitacionRepository;
    private final EstatusRepository estatusRepository;
    private final InstructorRepository instructorRepository;

    public DashboardController(CapacitacionRepository capacitacionRepository,
                               TipoInmuebleRepository tipoInmuebleRepository,
                               TipoCapacitacionRepository tipoCapacitacionRepository,
                               EstatusRepository estatusRepository,
                               InstructorRepository instructorRepository) {
        this.capacitacionRepository     = capacitacionRepository;
        this.tipoInmuebleRepository     = tipoInmuebleRepository;
        this.tipoCapacitacionRepository = tipoCapacitacionRepository;
        this.estatusRepository          = estatusRepository;
        this.instructorRepository       = instructorRepository;
    }

    @GetMapping
    public String dashboard(@ModelAttribute DashboardFiltroDTO filtro, Model model)
            throws JsonProcessingException {

        // --- Defaults de fecha: mes actual ---
        if (filtro.getFechaInicio() == null) {
            filtro.setFechaInicio(LocalDate.now().withDayOfMonth(1));
        }
        if (filtro.getFechaFin() == null) {
            filtro.setFechaFin(LocalDate.now());
        }

        // --- Obtener todas las capacitaciones y filtrar en memoria ---
        List<Capacitacion> todas = capacitacionRepository.findAll();
        List<Capacitacion> filtradas = todas.stream()
                .filter(c -> !c.getFecha().isBefore(filtro.getFechaInicio()))
                .filter(c -> !c.getFecha().isAfter(filtro.getFechaFin()))
                .filter(c -> filtro.getTipoInmuebleId() == null ||
                        c.getTipoInmueble().getId().equals(filtro.getTipoInmuebleId()))
                .filter(c -> filtro.getTipoCapacitacionId() == null ||
                        c.getTipoCapacitacion().getId().equals(filtro.getTipoCapacitacionId()))
                .filter(c -> filtro.getEstatusId() == null ||
                        c.getEstatus().getId().equals(filtro.getEstatusId()))
                .filter(c -> filtro.getInstructorId() == null ||
                        (c.getInstructor() != null &&
                                c.getInstructor().getId().equals(filtro.getInstructorId())))
                .collect(Collectors.toList());

        // --- KPIs generales ---
        long total       = filtradas.size();
        long realizadas  = filtradas.stream()
                .filter(c -> c.getEstatus().getNombre().equalsIgnoreCase("Realizada")).count();
        long programadas = filtradas.stream()
                .filter(c -> c.getEstatus().getNombre().equalsIgnoreCase("Confirmada") ||
                        c.getEstatus().getNombre().equalsIgnoreCase("Pendiente")).count();
        long canceladas  = filtradas.stream()
                .filter(c -> c.getEstatus().getNombre().equalsIgnoreCase("Cancelada")).count();

        // Horas impartidas (diferencia horaFin - horaInicio en minutos → horas)
        long minutos = filtradas.stream()
                .filter(c -> c.getHoraInicio() != null && c.getHoraFin() != null)
                .mapToLong(c -> java.time.Duration.between(c.getHoraInicio(), c.getHoraFin()).toMinutes())
                .sum();
        long horas = minutos / 60;

        // Beneficiarios
        long totalHombres = filtradas.stream()
                .mapToLong(c -> c.getHombres() != null ? c.getHombres() : 0).sum();
        long totalMujeres = filtradas.stream()
                .mapToLong(c -> c.getMujeres() != null ? c.getMujeres() : 0).sum();
        long totalNinos   = filtradas.stream()
                .mapToLong(c -> c.getNinos() != null ? c.getNinos() : 0).sum();
        long totalBeneficiarios = totalHombres + totalMujeres + totalNinos;

        // --- Gráfica 1: Por tipo de inmueble ---
        Map<String, Long> porInmueble = filtradas.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getTipoInmueble().getNombre(), Collectors.counting()));

        // Colores dinámicos del catálogo
        Map<String, String> coloresInmueble = tipoInmuebleRepository.findAll().stream()
                .collect(Collectors.toMap(t -> t.getNombre(), t -> t.getColor()));

        List<String> inmuebleLabels = new ArrayList<>(porInmueble.keySet());
        List<Long>   inmuebleData   = inmuebleLabels.stream()
                .map(porInmueble::get).collect(Collectors.toList());
        List<String> inmuebleColores = inmuebleLabels.stream()
                .map(l -> coloresInmueble.getOrDefault(l, "#9CA3AF")).collect(Collectors.toList());

        // --- Gráfica 2: Por tipo de capacitación ---
        Map<String, Long> porCapacitacion = filtradas.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getTipoCapacitacion().getNombre(), Collectors.counting()));
        List<String> capLabels = new ArrayList<>(porCapacitacion.keySet());
        capLabels.sort((a, b) -> Long.compare(porCapacitacion.get(b), porCapacitacion.get(a)));
        List<Long> capData = capLabels.stream()
                .map(porCapacitacion::get).collect(Collectors.toList());

        // --- Gráfica 3: Por semana (últimas 8 semanas) ---
        WeekFields wf = WeekFields.of(Locale.getDefault());
        Map<String, Long> porSemana = new LinkedHashMap<>();
        LocalDate inicio = filtro.getFechaInicio();
        LocalDate fin    = filtro.getFechaFin();
        // Agrupa por semana ISO
        filtradas.stream()
                .sorted(Comparator.comparing(Capacitacion::getFecha))
                .forEach(c -> {
                    int semana = c.getFecha().get(wf.weekOfWeekBasedYear());
                    int año    = c.getFecha().getYear();
                    // Primer día de esa semana
                    LocalDate lunesSemana = c.getFecha()
                            .with(wf.dayOfWeek(), 1);
                    String key = lunesSemana.getDayOfMonth() + "/" +
                            lunesSemana.getMonthValue();
                    porSemana.merge(key, 1L, Long::sum);
                });
        List<String> semanaLabels = new ArrayList<>(porSemana.keySet());
        List<Long>   semanaData   = new ArrayList<>(porSemana.values());

        // --- Gráfica 4: Por estatus ---
        Map<String, Long> porEstatus = filtradas.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getEstatus().getNombre(), Collectors.counting()));
        List<String> estatusLabels  = new ArrayList<>(porEstatus.keySet());
        List<Long>   estatusData    = estatusLabels.stream()
                .map(porEstatus::get).collect(Collectors.toList());

        // Colores fijos por estatus
        Map<String, String> coloresEstatus = Map.of(
                "Realizada",  "#16A34A",
                "Confirmada", "#2563EB",
                "Pendiente",  "#EAB308",
                "Cancelada",  "#DC2626"
        );
        List<String> estatusColores = estatusLabels.stream()
                .map(l -> coloresEstatus.getOrDefault(l, "#9CA3AF"))
                .collect(Collectors.toList());

        // --- Próximas capacitaciones ---
        List<Capacitacion> proximas = todas.stream()
                .filter(c -> !c.getFecha().isBefore(LocalDate.now()))
                .filter(c -> c.getEstatus().getNombre().equalsIgnoreCase("Confirmada") ||
                        c.getEstatus().getNombre().equalsIgnoreCase("Pendiente"))
                .sorted(Comparator.comparing(Capacitacion::getFecha))
                .limit(5)
                .collect(Collectors.toList());

        // --- Serializar a JSON para las gráficas ---
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Construir objeto de datos para gráficas
        Map<String, Object> graficas = new LinkedHashMap<>();
        graficas.put("inmuebleLabels",  inmuebleLabels);
        graficas.put("inmuebleData",    inmuebleData);
        graficas.put("inmuebleColores", inmuebleColores);
        graficas.put("capLabels",       capLabels);
        graficas.put("capData",         capData);
        graficas.put("semanaLabels",    semanaLabels);
        graficas.put("semanaData",      semanaData);
        graficas.put("estatusLabels",   estatusLabels);
        graficas.put("estatusData",     estatusData);
        graficas.put("estatusColores",  estatusColores);

        // --- Pasar todo al modelo ---
        model.addAttribute("filtro",             filtro);
        model.addAttribute("graficasJson",        mapper.writeValueAsString(graficas));
        model.addAttribute("proximasJson",        mapper.writeValueAsString(proximas));

        // KPIs
        model.addAttribute("total",              total);
        model.addAttribute("realizadas",         realizadas);
        model.addAttribute("programadas",        programadas);
        model.addAttribute("canceladas",         canceladas);
        model.addAttribute("horas",              horas);
        model.addAttribute("totalHombres",       totalHombres);
        model.addAttribute("totalMujeres",       totalMujeres);
        model.addAttribute("totalNinos",         totalNinos);
        model.addAttribute("totalBeneficiarios", totalBeneficiarios);

        // Catálogos para los selects
        model.addAttribute("tiposInmueble",     tipoInmuebleRepository.findAll());
        model.addAttribute("tiposCapacitacion", tipoCapacitacionRepository.findAll());
        model.addAttribute("estatusLista",      estatusRepository.findAll());
        model.addAttribute("instructores",      instructorRepository.findByActivoTrue());

        model.addAttribute("paginaActiva", "dashboard");
        return "dashboard";
    }
}