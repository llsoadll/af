package com.universidad.gestion_estudiante.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class BackupCleanupService {
    private static final Logger log = LoggerFactory.getLogger(BackupCleanupService.class);
    @Value("${backup.directory}")
    private String backupDirectory;
    
    @Value("${backup.retention.days:30}")
    private int retentionDays;

    @Scheduled(cron = "0 0 2 * * ?") // Ejecutar a las 2am
    public void limpiarBackupsAntiguos() {
        File backupDir = new File(backupDirectory);
        if (!backupDir.exists()) return;

        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(retentionDays);
        
        File[] files = backupDir.listFiles((dir, name) -> name.startsWith("backup_"));
        if (files != null) {
            for (File file : files) {
                if (isFileOlderThan(file, cutoffDate)) {
                    if (file.delete()) {
                        log.info("Backup antiguo eliminado: {}", file.getName());
                    }
                }
            }
        }
    }

    private boolean isFileOlderThan(File file, LocalDateTime cutoffDate) {
        try {
            BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            LocalDateTime fileTime = LocalDateTime.ofInstant(
                attr.creationTime().toInstant(), 
                ZoneId.systemDefault()
            );
            return fileTime.isBefore(cutoffDate);
        } catch (Exception e) {
            log.error("Error al leer atributos del archivo: {}", file.getName(), e);
            return false;
        }
    }
}