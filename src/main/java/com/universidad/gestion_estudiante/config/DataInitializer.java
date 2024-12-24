package com.universidad.gestion_estudiante.config;

import com.universidad.gestion_estudiante.model.Usuario;
import com.universidad.gestion_estudiante.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("!prod") 
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        try {
            // Create admin user if not exists
            if (usuarioRepository.findByUsername("admin") == null) {
                Usuario admin = new Usuario();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setEmail("admin@example.com");
                admin.setNombreCompleto("Administrador");
                admin.setRol("ROLE_ADMIN");
                usuarioRepository.save(admin);
                System.out.println("Admin user created successfully");
            }

            // Create guest user if not exists
            if (usuarioRepository.findByUsername("invitado") == null) {
                Usuario invitado = new Usuario();
                invitado.setUsername("invitado");
                invitado.setPassword(passwordEncoder.encode("invitado123"));
                invitado.setEmail("invitado@example.com");
                invitado.setNombreCompleto("Usuario Invitado");
                invitado.setRol("ROLE_INVITADO");
                usuarioRepository.save(invitado);
                System.out.println("Guest user created successfully");
            }
        } catch (Exception e) {
            System.err.println("Error initializing data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}