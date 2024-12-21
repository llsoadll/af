package com.universidad.gestion_estudiante.repository;

import com.universidad.gestion_estudiante.model.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
    // ...existing code...
}