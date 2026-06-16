package com.proteccioncivil.capacitaciones_pc.repository;

import com.proteccioncivil.capacitaciones_pc.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
}
