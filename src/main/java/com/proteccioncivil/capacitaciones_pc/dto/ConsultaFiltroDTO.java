package com.proteccioncivil.capacitaciones_pc.dto;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

/**
 * DTO que recibe los parámetros del formulario de Consulta Histórica.
 * Los campos son opcionales — null significa "sin filtro".
 */
public class ConsultaFiltroDTO {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaInicio;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaFin;

    private String solicitante;       // búsqueda parcial por nombreSolicitante
    private Long tipoInmuebleId;
    private Long tipoCapacitacionId;
    private Long estatusId;
    private Long instructorId;
    private String pc;                // búsqueda parcial
    private String op;                // búsqueda parcial

    // Getters y setters
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public String getSolicitante() { return solicitante; }
    public void setSolicitante(String solicitante) { this.solicitante = solicitante; }

    public Long getTipoInmuebleId() { return tipoInmuebleId; }
    public void setTipoInmuebleId(Long tipoInmuebleId) { this.tipoInmuebleId = tipoInmuebleId; }

    public Long getTipoCapacitacionId() { return tipoCapacitacionId; }
    public void setTipoCapacitacionId(Long tipoCapacitacionId) { this.tipoCapacitacionId = tipoCapacitacionId; }

    public Long getEstatusId() { return estatusId; }
    public void setEstatusId(Long estatusId) { this.estatusId = estatusId; }

    public Long getInstructorId() { return instructorId; }
    public void setInstructorId(Long instructorId) { this.instructorId = instructorId; }

    public String getPc() { return pc; }
    public void setPc(String pc) { this.pc = pc; }

    public String getOp() { return op; }
    public void setOp(String op) { this.op = op; }
}