package com.universidad.gestion_estudiante.service;

import com.universidad.gestion_estudiante.model.Usuario;
import com.universidad.gestion_estudiante.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void guardar(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioRepository.save(usuario);
    }

    // **Nuevo método para listar usuarios**
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }


    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public void crearTokenResetPassword(Usuario usuario, String token) {
        usuario.setResetPasswordToken(token);
        usuario.setResetPasswordTokenExpiry(LocalDateTime.now().plusHours(24));
        usuarioRepository.save(usuario);
    }
    

    public Usuario findByResetPasswordToken(String token) {
        return usuarioRepository.findByResetPasswordToken(token);
    }
    
    public boolean isTokenExpirado(Usuario usuario) {
        return LocalDateTime.now().isAfter(usuario.getResetPasswordTokenExpiry());
    }
    
    public void actualizarPassword(Usuario usuario, String newPassword) {
        usuario.setPassword(passwordEncoder.encode(newPassword));
        usuario.setResetPasswordToken(null);
        usuario.setResetPasswordTokenExpiry(null);
        usuarioRepository.save(usuario);
    }
    // Otros métodos como listar, eliminar, etc.
}