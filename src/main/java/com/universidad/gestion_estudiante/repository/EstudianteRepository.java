package com.universidad.gestion_estudiante.repository;

import com.universidad.gestion_estudiante.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    
    // Método con query explícito para debug
    @Query("SELECT e FROM Estudiante e WHERE e.anio = ?1 AND LOWER(e.cuatrimestre) = LOWER(?2)")
    List<Estudiante> findByAnioAndCuatrimestre(Integer anio, String cuatrimestre);
    
    // Método adicional para verificar datos
    @Query("SELECT COUNT(e) FROM Estudiante e")
    Long countAll();

    List<Estudiante> findByCuatrimestre(String cuatrimestre); // Agregar este método

    @Query("SELECT e FROM Estudiante e WHERE LOWER(TRANSLATE(e.nombreApellido, 'áéíóúÁÉÍÓÚ', 'aeiouAEIOU')) LIKE LOWER(TRANSLATE(CONCAT('%', :searchTerm, '%'), 'áéíóúÁÉÍÓÚ', 'aeiouAEIOU'))")
    List<Estudiante> findByNombreApellidoContainingIgnoreCase(@Param("searchTerm") String searchTerm);

    List<Estudiante> findByLegajoContainingIgnoreCase(String legajo);
}