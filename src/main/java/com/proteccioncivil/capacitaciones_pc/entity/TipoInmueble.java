package com.proteccioncivil.capacitaciones_pc.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tipos_inmueble")
@Data
public class TipoInmueble {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String color;
}