package com.universidad.gestion_estudiante.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.universidad.gestion_estudiante.service.ExportService;

@Controller
public class ExportController {

    @Autowired
    private ExportService exportService;
    
    @GetMapping("/exportar")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void exportarExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=DatosAF.xlsx");
        
        XSSFWorkbook workbook = exportService.exportarDatos();
        workbook.write(response.getOutputStream());
    }
}