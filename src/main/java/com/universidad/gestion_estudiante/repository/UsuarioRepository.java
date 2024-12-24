package com.universidad.gestion_estudiante.repository;

import com.universidad.gestion_estudiante.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByUsername(String username);
    Usuario findByEmail(String email);
    Usuario findByResetPasswordToken(String token);
}