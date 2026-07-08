package com.proteccioncivil.capacitaciones_pc.dto;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

public class DashboardFiltroDTO {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaInicio;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaFin;

    private Long tipoInmuebleId;
    private Long tipoCapacitacionId;
    private Long estatusId;
    private Long instructorId;

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate v) { this.fechaInicio = v; }
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate v) { this.fechaFin = v; }
    public Long getTipoInmuebleId() { return tipoInmuebleId; }
    public void setTipoInmuebleId(Long v) { this.tipoInmuebleId = v; }
    public Long getTipoCapacitacionId() { return tipoCapacitacionId; }
    public void setTipoCapacitacionId(Long v) { this.tipoCapacitacionId = v; }
    public Long getEstatusId() { return estatusId; }
    public void setEstatusId(Long v) { this.estatusId = v; }
    public Long getInstructorId() { return instructorId; }
    public void setInstructorId(Long v) { this.instructorId = v; }
}