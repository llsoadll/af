package com.universidad.gestion_estudiante.controller;

import com.universidad.gestion_estudiante.repository.AuditoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;


@Controller
public class AuditoriaController {

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    @PreAuthorize("authentication.name == 'llsoadll'")
    @GetMapping("/auditoria")
    public String verAuditoria(Model model) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!"llsoadll".equals(currentUser)) {
            throw new AccessDeniedException("No autorizado");
        }
        model.addAttribute("registros", auditoriaRepository.findAll());
        return "auditoria/lista";
    }
}