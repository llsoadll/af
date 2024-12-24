package com.universidad.gestion_estudiante.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {
    @Value("${app.base-url}")
    private String baseUrl;
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Autowired
    private JavaMailSender mailSender;
    
    public void enviarEmailRecuperacion(String to, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject("Recuperación de contraseña");
            message.setText("Para resetear tu contraseña, haz click aquí: "
                    + baseUrl + "/reset-password?token=" + token);
            mailSender.send(message);
            logger.info("Email enviado exitosamente a: {}", to);
        } catch (Exception e) {
            logger.error("Error enviando email a {}: {}", to, e.getMessage());
            throw new RuntimeException("Error enviando email", e);
    }
}


public void enviarNotificacionBackup(String mensaje, String fileName) {
    try {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo("admin@ejemplo.com"); // Configura el email del admin
        message.setSubject("Notificación de Backup");
        message.setText(mensaje + "\nArchivo: " + fileName);
        mailSender.send(message);
        logger.info("Notificación de backup enviada");
    } catch (Exception e) {
        logger.error("Error enviando notificación de backup", e);
    }
}
}