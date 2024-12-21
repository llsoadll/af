package com.universidad.gestion_estudiante.service;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.universidad.gestion_estudiante.model.Auditoria;
import com.universidad.gestion_estudiante.model.Usuario;
import com.universidad.gestion_estudiante.repository.AuditoriaRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuditoriaService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuditoriaService.class);
    
    @Autowired
    private AuditoriaRepository auditoriaRepository;
    
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UsuarioService usuarioService;
    
    public void registrarAccion(String usuario, String accion, String entidad, String detalles) {
        Auditoria auditoria = new Auditoria();
        auditoria.setUsuario(usuario);
        Usuario usuarioObj = usuarioService.findByUsername(usuario);
        auditoria.setNombreCompleto(usuarioObj != null ? usuarioObj.getNombreCompleto() : usuario);
        auditoria.setAccion(accion);
        auditoria.setEntidad(entidad);
        auditoria.setFecha(LocalDateTime.now());
        auditoria.setDetalles(detalles);
        auditoria.setIp(request.getRemoteAddr());
        auditoria.setNavegador(request.getHeader("User-Agent"));
        
        logger.debug("Auditoria: usuario={}, nombreCompleto={}, accion={}, entidad={}, detalles={}, ip={}, navegador={}",
            usuario, auditoria.getNombreCompleto(), accion, entidad, detalles, auditoria.getIp(), auditoria.getNavegador());
        
        auditoriaRepository.save(auditoria);
    }
}