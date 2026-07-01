package com.proteccioncivil.capacitaciones_pc.service;

import com.proteccioncivil.capacitaciones_pc.entity.Capacitacion;
import com.proteccioncivil.capacitaciones_pc.repository.CapacitacionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CapacitacionService {
    private final CapacitacionRepository capacitacionRepository;

    public CapacitacionService(CapacitacionRepository capacitacionRepository) {
        this.capacitacionRepository = capacitacionRepository;
    }

    public List<Capacitacion> obtenerTodas() {
        return capacitacionRepository.findAll();
    }

    public Capacitacion guardar(Capacitacion capacitacion) {
        return capacitacionRepository.save(capacitacion);
    }

    public Capacitacion obtenerPorId(Long id) {
        return capacitacionRepository.findById(id).orElseThrow();
    }

    public List<Capacitacion> obtenerProximasPorInstructor(Long instructorId) {
        return capacitacionRepository
                .findByInstructorIdAndFechaGreaterThanEqualOrderByFechaAsc(
                        instructorId, LocalDate.now());
    }

    public List<Capacitacion> obtenerHistorialPorInstructor(Long instructorId) {
        return capacitacionRepository
                .findByInstructorIdAndFechaLessThanOrderByFechaDesc(
                        instructorId, LocalDate.now());
    }
}
