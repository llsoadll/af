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
        .requestMatchers("/css/**", "/js/**", "/images/**", "/manifest.json", "/service-worker.js").permitAll()
        .requestMatchers("/login", "/forgot-password", "/reset-password").permitAll()
        .requestMatchers("/ws/**").permitAll()
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
        .requestMatchers("/calendario/**").authenticated() // Permite acceso a usuarios autenticados
        .requestMatchers("/calendario/eventos/**").authenticated()
        .anyRequest().authenticated()
    )

// ... resto de la configuración ...
.csrf(csrf -> csrf
.ignoringRequestMatchers("/calendario/eventos/**") // Ignora CSRF para endpoints del calendario
)

.formLogin(form -> form
.loginPage("/login")
.defaultSuccessUrl("/dashboard", true)  // Cambiar esto
.permitAll()
)
.sessionManagement(session -> session
.invalidSessionUrl("/login")
.maximumSessions(1)
.maxSessionsPreventsLogin(false)
);

return http.build();
    }

    
}