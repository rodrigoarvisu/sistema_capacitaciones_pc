package com.proteccioncivil.capacitaciones_pc.config;

import com.proteccioncivil.capacitaciones_pc.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class UsuarioGlobalAdvice {

    private final UsuarioRepository usuarioRepository;

    public UsuarioGlobalAdvice(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Se ejecuta antes de CUALQUIER controller — agrega "usuarioActual"
    // al modelo si hay alguien logueado, sin tener que repetirlo en cada método.
    @ModelAttribute
    public void agregarUsuarioActual(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            usuarioRepository.findByUsername(auth.getName())
                    .ifPresent(u -> model.addAttribute("usuarioActual", u));
        }
    }
}