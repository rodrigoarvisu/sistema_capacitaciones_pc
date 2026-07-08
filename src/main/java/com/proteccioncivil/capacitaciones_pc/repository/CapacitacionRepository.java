package com.proteccioncivil.capacitaciones_pc.repository;

import com.proteccioncivil.capacitaciones_pc.entity.Capacitacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

public interface CapacitacionRepository
        extends JpaRepository<Capacitacion, Long>, JpaSpecificationExecutor<Capacitacion> {

    long countByInstructorIdAndFechaGreaterThanEqual(
            Long instructorId,
            LocalDate fecha
    );

    List<Capacitacion>
    findByInstructorIdAndFechaGreaterThanEqualOrderByFechaAsc(
            Long instructorId,
            LocalDate fecha
    );

    List<Capacitacion>
    findByInstructorIdAndFechaLessThanOrderByFechaDesc(
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