package com.proteccioncivil.capacitaciones_pc.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "capacitaciones")
@Data
public class Capacitacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pc;

    private String op;

    @Column(length = 100)
    private String nombreSolicitante;

    private LocalDate fecha;

    private LocalTime horaInicio;

    private LocalTime horaFin;

    @Column(length = 200)
    private String direccion;

    @Column(length = 100)
    private String contacto;

    @Column(length = 40)
    private String telefono;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @PrePersist
    public void prePersist() {
        fechaRegistro = LocalDateTime.now();
    }

    @ManyToOne
    @JoinColumn(name = "tipo_inmueble_id")
    private TipoInmueble tipoInmueble;

    @ManyToOne
    @JoinColumn(name = "tipo_capacitacion_id")
    private TipoCapacitacion tipoCapacitacion;

    @ManyToOne
    @JoinColumn(name = "estatus_id")
    private Estatus estatus;
}