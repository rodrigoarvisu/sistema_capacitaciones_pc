package com.proteccioncivil.capacitaciones_pc.repository;

import com.proteccioncivil.capacitaciones_pc.entity.Capacitacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CapacitacionRepository
        extends JpaRepository<Capacitacion, Long> {

    long countByInstructorIdAndFechaGreaterThanEqual(
            Long instructorId,
            LocalDate fecha
    );

    List<Capacitacion>
    findByInstructorIdAndFechaGreaterThanEqualOrderByFechaAsc(
            Long instructorId,
            LocalDate fecha
    );

    long countByInstructorIdAndFechaBetween(
            Long instructorId,
            LocalDate inicio,
            LocalDate fin
    );

    long countByInstructorId(
            Long instructorID
    );

    Capacitacion findFirstByInstructorIdAndFechaGreaterThanEqualOrderByFechaAsc(
            Long instructorId,
            LocalDate fecha
    );

}