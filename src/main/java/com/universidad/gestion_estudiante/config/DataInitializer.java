package com.universidad.gestion_estudiante.config;

import com.universidad.gestion_estudiante.model.Usuario;
import com.universidad.gestion_estudiante.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Crear usuario admin si no existe
        if (usuarioRepository.findByUsername("admin") == null) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRol("ROLE_ADMIN");
            usuarioRepository.save(admin);
        }

        // Crear usuario invitado si no existe
        if (usuarioRepository.findByUsername("invitado") == null) {
            Usuario invitado = new Usuario();
            invitado.setUsername("invitado");
            invitado.setPassword(passwordEncoder.encode("invitado123"));
            invitado.setRol("ROLE_INVITADO");
            usuarioRepository.save(invitado);
        }
    }
}