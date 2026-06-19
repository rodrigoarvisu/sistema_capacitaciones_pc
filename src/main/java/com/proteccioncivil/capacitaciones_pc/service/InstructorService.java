package com.proteccioncivil.capacitaciones_pc.service;

import com.proteccioncivil.capacitaciones_pc.entity.Capacitacion;
import com.proteccioncivil.capacitaciones_pc.entity.Instructor;
import com.proteccioncivil.capacitaciones_pc.repository.CapacitacionRepository;
import com.proteccioncivil.capacitaciones_pc.repository.InstructorRepository;

import java.util.List;

public class InstructorService {
    private final InstructorRepository instructorRepository;

    public InstructorService(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    public List<Instructor> obtenerTodas() {
        return instructorRepository.findAll();
    }

    public Instructor guardar(Instructor instructor) {
        return instructorRepository.save(instructor);
    }
}
