package com.universidad.gestion_estudiante.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        .authorizeHttpRequests(auth -> auth
        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
        // 1) Primero permitimos /registro y /usuarios/registro
        .requestMatchers("/registro", "/usuarios/registro").permitAll()
        .requestMatchers("/usuarios/registro/**").permitAll()  // Add this line
        // 2) Después especificamos las rutas que requieren rol
        .requestMatchers("/estudiantes").hasAnyRole("ADMIN", "INVITADO", "USER")
        .requestMatchers("/dashboard").hasAnyRole("ADMIN", "INVITADO", "USER")
        .requestMatchers("/editar/**", "/eliminar/**", "/nuevo", "/guardar", "/cargar-csv").hasRole("ADMIN")
        // 3) Finalmente /usuarios/** requiere rol ADMIN, excepto /usuarios/registro que ya se permitió arriba
        .requestMatchers("/usuarios/**").hasRole("ADMIN")
        .requestMatchers("/usuarios/nuevo-admin").hasRole("ADMIN")
        .requestMatchers("/usuarios/guardar-admin").hasRole("ADMIN")
        .requestMatchers("/auditoria").hasRole("ADMIN")
        .requestMatchers("/forgot-password", "/reset-password").permitAll() 
        // 4) Las demás rutas exigen autenticación
        .anyRequest().authenticated()
    )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)  // Cambiado a "/" con true para forzar redirección
                .permitAll()
            )
            .logout(logout -> logout
                .permitAll()
            );
        
        return http.build();
    }

    
}