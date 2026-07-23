package com.proteccioncivil.capacitaciones_pc.repository;

import com.proteccioncivil.capacitaciones_pc.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
}