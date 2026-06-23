package com.proteccioncivil.capacitaciones_pc.service;

import com.proteccioncivil.capacitaciones_pc.entity.Capacitacion;
import com.proteccioncivil.capacitaciones_pc.entity.Instructor;
import com.proteccioncivil.capacitaciones_pc.repository.CapacitacionRepository;
import com.proteccioncivil.capacitaciones_pc.repository.InstructorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstructorService {
    private final InstructorRepository instructorRepository;

    public InstructorService(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    public List<Instructor> obtenerTodos() {
        return instructorRepository.findAll();
    }

    public Instructor guardar(Instructor instructor) {
        return instructorRepository.save(instructor);
    }

    public Instructor obtenerPorId(Long id) { return instructorRepository.findById(id).orElse(null); }
}
