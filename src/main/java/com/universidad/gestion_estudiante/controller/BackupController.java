package com.universidad.gestion_estudiante.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.universidad.gestion_estudiante.service.BackupService;

@RestController
public class BackupController {
    
    @Autowired
    private BackupService backupService;
    
    @GetMapping("/api/backup/test")
    public ResponseEntity<String> testBackup() {
        try {
            backupService.realizarBackupAutomatico();
            return ResponseEntity.ok("Backup iniciado manualmente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}