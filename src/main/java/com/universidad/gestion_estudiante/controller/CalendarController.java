package com.universidad.gestion_estudiante.controller;

import java.util.List; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.universidad.gestion_estudiante.model.Evento;
import com.universidad.gestion_estudiante.repository.EventoRepository;

@Controller
@RequestMapping("/calendario")
public class CalendarController {
    
    @Autowired
    private EventoRepository eventoRepository;
    
    @GetMapping
    public String mostrarCalendario() {
        return "calendario";
    }
    
    @GetMapping("/eventos")
    @ResponseBody
    public List<Evento> obtenerEventos() {
        return eventoRepository.findAll();
    }
    
    @PostMapping("/eventos")
    @ResponseBody
    public Evento crearEvento(@RequestBody Evento evento) {
        return eventoRepository.save(evento);
    }
    
    @DeleteMapping("/eventos/{id}")
    @ResponseBody
    public void eliminarEvento(@PathVariable Long id) {
        eventoRepository.deleteById(id);
    }
}