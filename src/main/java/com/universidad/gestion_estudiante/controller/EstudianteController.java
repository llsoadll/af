package com.universidad.gestion_estudiante.controller;

import java.time.Year;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;
import com.universidad.gestion_estudiante.model.Estudiante;
import com.universidad.gestion_estudiante.service.EstudianteService;
import com.universidad.gestion_estudiante.dto.EstadisticasDTO;

@Controller
public class EstudianteController {

    @Autowired
    private EstudianteService service;

    @GetMapping("/")
    public String index() {
        return "redirect:/dashboard";
    }

    @GetMapping("/estudiantes")
    public String listarEstudiantes(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String legajoSearch,
            Model model) {
        List<Estudiante> estudiantes;
        
        if (legajoSearch != null && !legajoSearch.isEmpty()) {
            estudiantes = service.buscarPorLegajo(legajoSearch);
            model.addAttribute("legajoSearch", legajoSearch);
        } else if (searchTerm != null && !searchTerm.isEmpty()) {
            estudiantes = service.buscarEstudiantes(searchTerm);
            model.addAttribute("searchTerm", searchTerm);
        } else {
            estudiantes = service.listarTodos();
        }
        model.addAttribute("estudiantes", estudiantes);
        return "estudiantes/lista";
    }

    @GetMapping("/nuevo")
    public String nuevoEstudiante(Model model) {
        model.addAttribute("estudiante", new Estudiante());
        return "formulario";
    }

    @PostMapping("/guardar")
    public String guardarEstudiante(@ModelAttribute Estudiante estudiante) {
        service.guardar(estudiante);
        return "redirect:/";
    }

    @GetMapping("/editar/{id}")
    public String editarEstudiante(@PathVariable Long id, Model model) {
        Estudiante estudiante = service.obtenerPorId(id);
        model.addAttribute("estudiante", estudiante);
        return "formulario";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarEstudiante(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            service.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Estudiante eliminado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el estudiante.");
        }
        return "redirect:/estudiantes";
    }

    @GetMapping("/estadisticas")
    public String mostrarEstadisticas(@RequestParam(required = false) Integer anio,
                                      @RequestParam(required = false) String cuatrimestre,
                                      Model model) {
        if (anio != null && cuatrimestre != null) {
            EstadisticasDTO estadisticas = service.obtenerEstadisticas(anio, cuatrimestre);
            model.addAttribute("estadisticas", estadisticas);
            model.addAttribute("anio", anio);
            model.addAttribute("cuatrimestre", cuatrimestre);
        }
        return "estadisticas";
    }

    @GetMapping("/cargar-csv")
public String mostrarCargarCsv() {
    return "cargar-csv";
}

@PostMapping("/cargar-csv")
public String cargarCsv(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
    try {
        service.cargarCsv(file);
        redirectAttributes.addFlashAttribute("mensaje", "Archivo CSV cargado exitosamente");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Error al cargar el archivo: " + e.getMessage());
    }
    return "redirect:/estudiantes";
}

    @GetMapping("/dashboard")
public String mostrarDashboard(
        @RequestParam(required = false) Integer anio,
        @RequestParam(required = false) String cuatrimestre,
        Model model) {

    if (anio == null) {
        anio = Year.now().getValue(); // Año actual
    }
    if (cuatrimestre == null) {
        cuatrimestre = "segundo"; // O "primer", según corresponda
    }

    // Obtener estadísticas del período actual
    EstadisticasDTO estadisticas = service.obtenerEstadisticas(anio, cuatrimestre);
    model.addAttribute("estadisticas", estadisticas);
    model.addAttribute("selectedAnio", anio);
    model.addAttribute("selectedCuatrimestre", cuatrimestre);

    // Datos para el gráfico de barras
    String[] labels = {anio + " - " + cuatrimestre};
    double[] promedios = {estadisticas.getPromedioNotas()};
    model.addAttribute("labels", labels);
    model.addAttribute("promedios", promedios);

    // Datos para comparativa entre cuatrimestres
    String cuatrimestreAnterior = cuatrimestre.equals("primer") ? "segundo" : "primer";
    Integer anioAnterior = cuatrimestre.equals("primer") ? anio - 1 : anio;
    EstadisticasDTO estadisticasAnteriores = service.obtenerEstadisticas(anioAnterior, cuatrimestreAnterior);

    model.addAttribute("comparativaLabels", new String[]{
        anioAnterior + " - " + cuatrimestreAnterior,
        anio + " - " + cuatrimestre
    });

    model.addAttribute("comparativaPromedios", new double[]{
        estadisticasAnteriores != null ? estadisticasAnteriores.getPromedioNotas() : 0.0,
        estadisticas.getPromedioNotas()
    });

    // Distribución de notas
    model.addAttribute("distribucionNotas", estadisticas.getDistribucionNotas());

    // Tendencias y estadísticas generales
    model.addAttribute("porcentajePromocion", estadisticas.getPorcentajePromocionados());
    model.addAttribute("notaModa", estadisticas.getNotaModa());
    model.addAttribute("totalEstudiantes", estadisticas.getTotalEstudiantes());

    return "dashboard";
}

}