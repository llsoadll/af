package com.universidad.gestion_estudiante.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.universidad.gestion_estudiante.model.Estudiante;
import com.universidad.gestion_estudiante.repository.EstudianteRepository;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Service
public class CsvService {
    
    @Autowired
    private EstudianteRepository estudianteRepository;

    public void cargarCsv(MultipartFile archivo) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(archivo.getInputStream(), StandardCharsets.UTF_8))) {
            
            String line;
            boolean firstLine = true;
            int contador = 0; // Agrega un contador

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] datos = line.split(",");

                // Verificar que la línea tenga el número correcto de campos
                if (datos.length < 6) {
                    throw new IllegalArgumentException("Formato incorrecto en línea: " + line);
                }

                Estudiante estudiante = new Estudiante();
                estudiante.setNombreApellido(datos[0].trim());
                estudiante.setLegajo(datos[1].trim());
                estudiante.setCondicion(datos[2].trim());
                estudiante.setNotaFinal(datos[3].trim().isEmpty() ? null : Double.parseDouble(datos[3].trim()));
                estudiante.setCuatrimestre(datos[4].trim());
                estudiante.setAnio(Integer.parseInt(datos[5].trim()));
                
                estudianteRepository.save(estudiante);

                contador++;
                // Verifica si hay una condición que limite a 1000
                // if (contador >= 1000) {
                //     break; // Esto detendría la carga después de 1000 registros
                // }
            }
            System.out.println("Total de registros procesados: " + contador);
        } catch (Exception e) {
            // Imprimir el stack trace para depuración
            e.printStackTrace();
            // Lanzar una excepción para que el controlador la maneje
            throw new RuntimeException("Error al procesar CSV: " + e.getMessage(), e);
        }
    }
}
