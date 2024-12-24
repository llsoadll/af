package com.universidad.gestion_estudiante.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.ss.usermodel.Row;
import java.util.List;
import com.universidad.gestion_estudiante.model.Estudiante;


@Service
public class ExportService {
    
    @Autowired
    private EstudianteService estudianteService;
    
    public XSSFWorkbook exportarDatos() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        
        // Hoja de Estudiantes
        XSSFSheet estudiantesSheet = workbook.createSheet("Estudiantes");
        List<Estudiante> estudiantes = estudianteService.listarTodos();
        
        // Crear encabezados
        Row headerRow = estudiantesSheet.createRow(0);
        headerRow.createCell(0).setCellValue("Nombre y Apellido");
        headerRow.createCell(1).setCellValue("Legajo");
        headerRow.createCell(2).setCellValue("Condición");
        headerRow.createCell(3).setCellValue("Nota Final");
        headerRow.createCell(4).setCellValue("Cuatrimestre");
        headerRow.createCell(5).setCellValue("Año");
        
        // Llenar datos
        int rowNum = 1;
        for(Estudiante estudiante : estudiantes) {
            Row row = estudiantesSheet.createRow(rowNum++);
            row.createCell(0).setCellValue(estudiante.getNombreApellido() != null ? estudiante.getNombreApellido() : "");
            row.createCell(1).setCellValue(estudiante.getLegajo() != null ? estudiante.getLegajo() : "");
            row.createCell(2).setCellValue(estudiante.getCondicion() != null ? estudiante.getCondicion() : "");
            row.createCell(3).setCellValue(estudiante.getNotaFinal() != null ? estudiante.getNotaFinal() : 0.0);
            row.createCell(4).setCellValue(estudiante.getCuatrimestre() != null ? estudiante.getCuatrimestre() : "");
            row.createCell(5).setCellValue(estudiante.getAnio() != null ? estudiante.getAnio().doubleValue() : 0.0);
        }
        
        return workbook;
    }
}
