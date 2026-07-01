package com.proteccioncivil.capacitaciones_pc.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "instructores")
@Data
public class Instructor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String telefono;

    @Column(nullable = false)
    private Boolean activo = true;
}
