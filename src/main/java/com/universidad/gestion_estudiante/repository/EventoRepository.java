package com.universidad.gestion_estudiante.repository;

import java.util.List;  
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.universidad.gestion_estudiante.model.Evento;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findByStartBetween(String start, String end);
}
