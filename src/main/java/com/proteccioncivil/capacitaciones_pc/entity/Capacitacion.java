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

    @Column(length = 1000)
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "tipo_inmueble_id")
    private TipoInmueble tipoInmueble;

    @ManyToOne
    @JoinColumn(name = "tipo_capacitacion_id")
    private TipoCapacitacion tipoCapacitacion;

    @ManyToOne
    @JoinColumn(name = "estatus_id")
    private Estatus estatus;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    @Column(name = "instructores_apoyo", length = 255)
    private String instructoresApoyo;

    @Column(nullable = false)
    private Integer hombres = 0;

    @Column(nullable = false)
    private Integer mujeres = 0;

    @Column(nullable = false)
    private Integer ninos = 0;

    @Column(name = "archivo_lista", length = 255)
    private String archivoLista;

    @Column(name = "registrado_por", length = 100, updatable = false)
    private String registradoPor;

    @Column(name = "modificado_por", length = 100)
    private String modificadoPor;

    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @PrePersist
    public void PrePersist() {
        registradoPor = obtenerUsuarioActual();
        fechaRegistro = LocalDateTime.now();
        modificadoPor = registradoPor;
        fechaModificacion = fechaRegistro;
    }

    @PreUpdate
    public void PreUpdate() {
        modificadoPor = obtenerUsuarioActual();
        fechaModificacion = LocalDateTime.now();
    }

    @Transient
    private String obtenerUsuarioActual() {
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return auth.getName();
        }
        return "sistema";
    }
}