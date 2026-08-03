package com.proteccioncivil.capacitaciones_pc.controller;

import com.proteccioncivil.capacitaciones_pc.entity.Usuario;
import com.proteccioncivil.capacitaciones_pc.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public PerfilController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private Usuario obtenerUsuarioActual() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByUsername(username).orElseThrow();
    }

    // Redirige de vuelta a la página donde estaba el usuario (Referer),
    // agregando un query param — mismo patrón que ya usas con ?guardado=true
    private String redirigir(HttpServletRequest request, String param) {
        String referer = request.getHeader("Referer");
        String base = (referer != null) ? referer.split("\\?")[0] : "/";
        return "redirect:" + base + "?" + param;
    }

    @PostMapping("/actualizar")
    public String actualizarPerfil(@RequestParam String nombre,
                                   @RequestParam String username,
                                   HttpServletRequest request) {

        Usuario usuario = obtenerUsuarioActual();

        // Si cambia el username, verificar que no lo tenga ya otro usuario
        boolean usernameCambio = !usuario.getUsername().equals(username);
        if (usernameCambio && usuarioRepository.findByUsername(username).isPresent()) {
            return redirigir(request, "errorPerfil=usuario_existe");
        }

        usuario.setNombre(nombre);
        usuario.setUsername(username);
        usuarioRepository.save(usuario);

        // Si cambió el username, la sesión de Spring Security queda con el
        // username viejo — forzamos logout para que vuelva a entrar limpio.
        if (usernameCambio) {
            SecurityContextHolder.clearContext();
            return "redirect:/login?perfilActualizado=true";
        }

        return redirigir(request, "perfilActualizado=true");
    }

    @PostMapping("/cambiar-password")
    public String cambiarPassword(@RequestParam String passwordActual,
                                  @RequestParam String passwordNueva,
                                  @RequestParam String passwordConfirmar,
                                  HttpServletRequest request) {

        Usuario usuario = obtenerUsuarioActual();

        if (!passwordEncoder.matches(passwordActual, usuario.getPassword())) {
            return redirigir(request, "errorPassword=actual_incorrecta");
        }

        if (!passwordNueva.equals(passwordConfirmar)) {
            return redirigir(request, "errorPassword=no_coincide");
        }

        usuario.setPassword(passwordEncoder.encode(passwordNueva));
        usuarioRepository.save(usuario);

        return redirigir(request, "passwordActualizada=true");
    }
}