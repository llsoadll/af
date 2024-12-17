package com.universidad.gestion_estudiante.service;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import com.universidad.gestion_estudiante.model.Estudiante;
import com.universidad.gestion_estudiante.dto.EstadisticasDTO;

public interface EstudianteService {
    List<Estudiante> listarTodos();
    void guardar(Estudiante estudiante);
    Estudiante obtenerPorId(Long id);
    void eliminar(Long id);
    EstadisticasDTO obtenerEstadisticas(Integer anio, String cuatrimestre);
    void cargarCsv(MultipartFile file); // Agregar este método
    EstadisticasDTO obtenerEstadisticasGenerales();
    Double obtenerPromedioPorCuatrimestre(String cuatrimestre);
    List<Estudiante> buscarEstudiantes(String searchTerm);
    List<Estudiante> buscarPorLegajo(String legajo);
}