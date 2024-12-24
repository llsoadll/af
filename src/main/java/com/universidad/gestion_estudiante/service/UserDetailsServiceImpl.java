package com.universidad.gestion_estudiante.service;

import com.universidad.gestion_estudiante.model.Usuario;
import com.universidad.gestion_estudiante.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username);
        return new User(usuario.getUsername(),
                       usuario.getPassword(),
                       Collections.singletonList(new SimpleGrantedAuthority(usuario.getRol())));
    }
}