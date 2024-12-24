package com.universidad.gestion_estudiante.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


    @Service
    public class BackupService {
    private static final Logger log = LoggerFactory.getLogger(BackupService.class);

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${backup.directory:/backup}")
    private String backupDirectory;
    
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private EmailService emailService;

    private String getPassword() {
        return dbPassword;
    }

    private String getDatabaseName() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            return conn.getCatalog();
        }
    }

    @Scheduled(cron = "0 0 1 * * ?") 
    public void realizarBackupAutomatico() throws IOException, InterruptedException, SQLException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = "backup_" + timestamp + ".sql";
        String filePath = backupDirectory + "/" + fileName;

        try {
            File backupDir = new File(backupDirectory);
            if (!backupDir.exists()) {
                backupDir.mkdirs();
            }

            ProcessBuilder pb = new ProcessBuilder(
                "mysqldump",
                "-u" + dataSource.getConnection().getMetaData().getUserName(),
                "-p" + getPassword(),
                getDatabaseName(),
                "--result-file=" + filePath
            );

            Process process = pb.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                log.info("Backup completado exitosamente: {}", fileName);
                emailService.enviarNotificacionBackup("Backup completado exitosamente", fileName);
            } else {
                log.error("Error en backup. Exit code: {}", exitCode);
            }

        } catch (Exception e) {
            log.error("Error realizando backup", e);
            throw e;
        }
    }
}