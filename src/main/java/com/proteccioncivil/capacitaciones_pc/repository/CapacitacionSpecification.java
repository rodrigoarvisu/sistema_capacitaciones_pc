package com.proteccioncivil.capacitaciones_pc.repository;

import com.proteccioncivil.capacitaciones_pc.dto.ConsultaFiltroDTO;
import com.proteccioncivil.capacitaciones_pc.entity.Capacitacion;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Construye dinámicamente los predicados JPA según los filtros activos.
 * Solo agrega condiciones para campos que no son null/vacíos.
 */
public class CapacitacionSpecification {

    public static Specification<Capacitacion> conFiltros(ConsultaFiltroDTO f) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Fecha inicio
            if (f.getFechaInicio() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("fecha"), f.getFechaInicio()));
            }

            // Fecha fin
            if (f.getFechaFin() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("fecha"), f.getFechaFin()));
            }

            // Solicitante (búsqueda parcial, case-insensitive)
            if (f.getSolicitante() != null && !f.getSolicitante().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("nombreSolicitante")),
                        "%" + f.getSolicitante().toLowerCase() + "%"
                ));
            }

            // Tipo de inmueble
            if (f.getTipoInmuebleId() != null) {
                predicates.add(cb.equal(root.get("tipoInmueble").get("id"), f.getTipoInmuebleId()));
            }

            // Tipo de capacitación
            if (f.getTipoCapacitacionId() != null) {
                predicates.add(cb.equal(root.get("tipoCapacitacion").get("id"), f.getTipoCapacitacionId()));
            }

            // Estatus
            if (f.getEstatusId() != null) {
                predicates.add(cb.equal(root.get("estatus").get("id"), f.getEstatusId()));
            }

            // Instructor
            if (f.getInstructorId() != null) {
                predicates.add(cb.equal(root.get("instructor").get("id"), f.getInstructorId()));
            }

            // PC (búsqueda parcial)
            if (f.getPc() != null && !f.getPc().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("pc")),
                        "%" + f.getPc().toLowerCase() + "%"
                ));
            }

            // OP (búsqueda parcial)
            if (f.getOp() != null && !f.getOp().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("op")),
                        "%" + f.getOp().toLowerCase() + "%"
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}