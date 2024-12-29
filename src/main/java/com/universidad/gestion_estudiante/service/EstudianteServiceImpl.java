package com.universidad.gestion_estudiante.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.universidad.gestion_estudiante.model.Estudiante;
import com.universidad.gestion_estudiante.model.Usuario;
import com.universidad.gestion_estudiante.repository.EstudianteRepository;

import jakarta.servlet.http.HttpServletRequest;

import com.universidad.gestion_estudiante.dto.EstadisticasDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import com.universidad.gestion_estudiante.model.Auditoria;
import com.universidad.gestion_estudiante.repository.AuditoriaRepository;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class EstudianteServiceImpl implements EstudianteService {
    
    private static final Logger logger = LoggerFactory.getLogger(EstudianteServiceImpl.class);

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private HttpServletRequest request;


    @Override
    public List<Estudiante> buscarEstudiantes(String searchTerm) {
        return estudianteRepository.findByNombreApellidoContainingIgnoreCase(searchTerm);
    }


    @Override
    public EstadisticasDTO obtenerEstadisticas(Integer anio, String cuatrimestre) {
        logger.info("Buscando estadísticas para año: {} y cuatrimestre: {}", anio, cuatrimestre);
        
        List<Estudiante> estudiantes = estudianteRepository.findByAnioAndCuatrimestre(anio, cuatrimestre);
        
        // Imprimir todas las condiciones para debug
        logger.info("Condiciones encontradas:");
        estudiantes.forEach(e -> logger.info("Estudiante: {}, Condición: '{}'", 
            e.getNombreApellido(), e.getCondicion()));

        EstadisticasDTO estadisticas = new EstadisticasDTO();
        estadisticas.setTotalEstudiantes(estudiantes.size());

        long cantidadRegulares = estudiantes.stream()
            .filter(e -> "regular".equalsIgnoreCase(e.getCondicion()))
            .count();
        
        // Modificar el filtro para incluir variaciones de "promocion"
        long cantidadPromocionados = estudiantes.stream()
            .filter(e -> e.getCondicion() != null && 
                        (e.getCondicion().equalsIgnoreCase("promocion") ||
                         e.getCondicion().equalsIgnoreCase("promoción") ||
                         e.getCondicion().equalsIgnoreCase("PROMOCION") ||
                         e.getCondicion().equalsIgnoreCase("PROMOCIÓN")))
            .count();

        logger.info("Cantidad de promocionados encontrados: {}", cantidadPromocionados);

        double promedioNotas = estudiantes.stream()
            .filter(e -> e.getNotaFinal() != null)
            .mapToDouble(Estudiante::getNotaFinal)
            .average()
            .orElse(0.0);

        estadisticas.setCantidadRegulares((int) cantidadRegulares);
        estadisticas.setCantidadPromocionados((int) cantidadPromocionados);
        estadisticas.setPromedioNotas(promedioNotas);

        // Inicializar array de distribución
        int[] distribucion = new int[5];
        
        // Calcular distribución de notas
        estudiantes.stream()
            .filter(e -> e.getNotaFinal() != null)
            .forEach(e -> {
                double nota = e.getNotaFinal();
                if (nota >= 1 && nota <= 2) distribucion[0]++;
                else if (nota > 2 && nota <= 4) distribucion[1]++;
                else if (nota > 4 && nota <= 6) distribucion[2]++;
                else if (nota > 6 && nota <= 8) distribucion[3]++;
                else if (nota > 8 && nota <= 10) distribucion[4]++;
            });
        
        estadisticas.setDistribucionNotas(distribucion);

        logger.info("Estadísticas calculadas: regulares={}, promocionados={}, promedio={}", 
            cantidadRegulares, cantidadPromocionados, promedioNotas);

        return estadisticas;
    }

    @Override
public EstadisticasDTO obtenerEstadisticasGenerales() {
    List<Estudiante> todosLosEstudiantes = estudianteRepository.findAll();
    EstadisticasDTO estadisticas = new EstadisticasDTO();
    
    // Calcular estadísticas totales
    estadisticas.setTotalEstudiantes(todosLosEstudiantes.size());
    
    // Contar promocionados y regulares
    long cantidadPromocionados = todosLosEstudiantes.stream()
        .filter(e -> "promocion".equalsIgnoreCase(e.getCondicion()) || 
                     "promoción".equalsIgnoreCase(e.getCondicion()))
        .count();
    
    long cantidadRegulares = todosLosEstudiantes.stream()
        .filter(e -> "regular".equalsIgnoreCase(e.getCondicion()))
        .count();
    
    // Calcular promedio general
    double promedioGeneral = todosLosEstudiantes.stream()
        .filter(e -> e.getNotaFinal() != null)
        .mapToDouble(Estudiante::getNotaFinal)
        .average()
        .orElse(0.0);
    
    // Calcular distribución de notas
    int[] distribucion = new int[5];
    todosLosEstudiantes.stream()
        .filter(e -> e.getNotaFinal() != null)
        .forEach(e -> {
            double nota = e.getNotaFinal();
            if (nota >= 1 && nota <= 2) distribucion[0]++;
            else if (nota > 2 && nota <= 4) distribucion[1]++;
            else if (nota > 4 && nota <= 6) distribucion[2]++;
            else if (nota > 6 && nota <= 8) distribucion[3]++;
            else if (nota > 8 && nota <= 10) distribucion[4]++;
        });
    
    estadisticas.setCantidadRegulares((int) cantidadRegulares);
    estadisticas.setCantidadPromocionados((int) cantidadPromocionados);
    estadisticas.setPromedioNotas(promedioGeneral);
    estadisticas.setDistribucionNotas(distribucion);
    
    return estadisticas;
}

    @Override
    public Double obtenerPromedioPorCuatrimestre(String cuatrimestre) {
        return estudianteRepository.findByCuatrimestre(cuatrimestre).stream()
            .filter(e -> e.getNotaFinal() != null)
            .mapToDouble(Estudiante::getNotaFinal)
            .average()
            .orElse(0.0);
    }

    @Override
    public List<Estudiante> listarTodos() {
        return estudianteRepository.findAll();
    }

    @Override
    public void guardar(Estudiante estudiante) {
        estudianteRepository.save(estudiante);
        registrarAuditoria("Guardar", "Estudiante", estudiante.getId());
    }

    @Override
    public Estudiante obtenerPorId(Long id) {
        return estudianteRepository.findById(id).orElse(null);
    }

    @Override
    public void eliminar(Long id) {
        estudianteRepository.deleteById(id);
        registrarAuditoria("Eliminar", "Estudiante", id);
    }

    private void registrarAuditoria(String accion, String entidad, Long entidadId) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    Usuario usuario = usuarioService.findByUsername(username);
    
    Auditoria auditoria = new Auditoria();
    auditoria.setUsuario(username);
    auditoria.setNombreCompleto(usuario != null ? usuario.getNombreCompleto() : username);
    auditoria.setAccion(accion);
    auditoria.setEntidad(entidad);
    auditoria.setEntidadId(entidadId);
    auditoria.setDetalles("Acción " + accion + " en " + entidad + " ID: " + entidadId);
    auditoria.setIp(request.getRemoteAddr());
    auditoria.setNavegador(request.getHeader("User-Agent"));
    auditoria.setFecha(LocalDateTime.now());
    
    auditoriaRepository.save(auditoria);
}

    
    @Override
    public void cargarCsv(MultipartFile file) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            
            String line;
            boolean firstLine = true;
            
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                String[] datos = line.split(",");
                Estudiante estudiante = new Estudiante();
                estudiante.setNombreApellido(datos[0].trim());
                estudiante.setLegajo(datos[1].trim());
                estudiante.setCondicion(datos[2].trim());
                estudiante.setNotaFinal(datos[3].trim().isEmpty() ? null : 
                    Double.parseDouble(datos[3].trim()));
                estudiante.setCuatrimestre(datos[4].trim());
                estudiante.setAnio(Integer.parseInt(datos[5].trim()));
                
                estudianteRepository.save(estudiante);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar archivo CSV: " + e.getMessage());
        }
    }

    @Override
    public List<Estudiante> buscarPorLegajo(String legajo) {
        return estudianteRepository.findByLegajoContainingIgnoreCase(legajo);
    }
}