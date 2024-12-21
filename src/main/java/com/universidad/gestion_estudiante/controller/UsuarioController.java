package com.universidad.gestion_estudiante.controller;

import com.universidad.gestion_estudiante.model.Usuario;
import com.universidad.gestion_estudiante.service.UsuarioService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario) {
        usuarioService.guardar(usuario);
        return "redirect:/usuarios/lista";
    }

    @GetMapping("/lista")
    public String listarUsuarios(Model model) {
        model.addAttribute("listaUsuarios", usuarioService.listarTodos());
        return "usuarios/lista";
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
public String registrarUsuario(@ModelAttribute @Valid Usuario usuario, 
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
    if (result.hasErrors()) {
        return "registro";
    }
    
    usuario.setRol("ROLE_ADMIN");
    usuarioService.guardar(usuario);
    redirectAttributes.addFlashAttribute("registroExitoso", true);
    return "redirect:/login";
}


 @GetMapping("/nuevo-admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String mostrarFormularioAdmin(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuarios/crear_admin";
    }

    @PostMapping("/guardar-admin")
    public String guardarAdmin(@ModelAttribute Usuario usuario) {
        usuario.setRol("ROLE_ADMIN");
        usuarioService.guardar(usuario);
        return "redirect:/usuarios/lista";
    }
    // Otros métodos como listar usuarios, editar, etc.
}