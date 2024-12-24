package com.universidad.gestion_estudiante.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.universidad.gestion_estudiante.model.Usuario;
import com.universidad.gestion_estudiante.service.UsuarioService;
import com.universidad.gestion_estudiante.service.EmailService;
import java.util.UUID;

@Controller
public class PasswordController {
    
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private EmailService emailService;
    
    @GetMapping("/forgot-password")
    public String mostrarFormularioOlvido() {
        return "forgot-password";
    }
    
    @PostMapping("/forgot-password")
    public String procesarSolicitudReset(@RequestParam("email") String email, 
                                       RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioService.findByEmail(email);
        if (usuario != null) {
            String token = UUID.randomUUID().toString();
            usuarioService.crearTokenResetPassword(usuario, token);
            emailService.enviarEmailRecuperacion(email, token);
            redirectAttributes.addFlashAttribute("mensaje", 
                "Se ha enviado un email con instrucciones para resetear tu contraseña");
        }
        return "redirect:/login";
    }


    @GetMapping("/reset-password")
public String mostrarFormularioReset(@RequestParam("token") String token, Model model) {
    Usuario usuario = usuarioService.findByResetPasswordToken(token);
    if (usuario == null || usuarioService.isTokenExpirado(usuario)) {
        return "redirect:/login?error=token-invalido";
    }
    model.addAttribute("token", token);
    return "reset-password";
}

@PostMapping("/reset-password")
public String procesarResetPassword(@RequestParam("token") String token, 
                                  @RequestParam("password") String password,
                                  RedirectAttributes redirectAttributes) {
    if (password.length() < 6) {
        redirectAttributes.addFlashAttribute("error", 
            "La contraseña debe tener al menos 6 caracteres");
        return "redirect:/reset-password?token=" + token;
    }

    Usuario usuario = usuarioService.findByResetPasswordToken(token);
    if (usuario == null || usuarioService.isTokenExpirado(usuario)) {
        return "redirect:/login?error=token-invalido";
    }
    
    usuarioService.actualizarPassword(usuario, password);
    redirectAttributes.addFlashAttribute("mensaje", 
        "Contraseña actualizada exitosamente");
    return "redirect:/login";
}
}