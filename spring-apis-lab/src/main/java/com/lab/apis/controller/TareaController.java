package com.lab.apis.controller;

import com.lab.apis.model.Tarea;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    private List<Tarea> tareas = new ArrayList<>(
        List.of(
            new Tarea(1L, "Preparar informe mensual", "Consolidar los datos de ventas del mes", "Alta", false),
            new Tarea(2L, "Actualizar documentacion", "Revisar el README del proyecto", "Media", false),
            new Tarea(3L, "Corregir bug en login", "Revisar validacion de credenciales", "Alta", true),
            new Tarea(4L, "Reunion con el equipo", "Definir alcance del sprint", "Media", false),
            new Tarea(5L, "Backup de base de datos", "Ejecutar respaldo semanal", "Baja", true)
        )
    );

    @GetMapping
    public ResponseEntity<?> obtenerTareas() {
        return ResponseEntity.ok(Map.of(
            "mensaje", "Tareas obtenidas correctamente",
            "total", tareas.size(),
            "datos", tareas
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerTarea(@PathVariable Long id) {
        for (Tarea t : tareas) {
            if (t.getId().equals(id)) {
                return ResponseEntity.ok(Map.of("mensaje", "Tarea encontrada", "datos", t));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Tarea no encontrada", "codigo", 404));
    }

    @PostMapping
    public ResponseEntity<?> crearTarea(@RequestBody Tarea tarea) {
        if (tarea.getTitulo() == null || tarea.getTitulo().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("mensaje", "Datos invalidos", "codigo", 400));
        }
        tarea.setId((long) tareas.size() + 1);
        if (tarea.getCompletada() == null) {
            tarea.setCompletada(false);
        }
        tareas.add(tarea);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            Map.of("mensaje", "Tarea creada correctamente", "codigo", 201, "datos", tarea));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarTarea(@PathVariable Long id, @RequestBody Tarea datos) {
        for (Tarea t : tareas) {
            if (t.getId().equals(id)) {
                if (datos.getTitulo() == null || datos.getTitulo().isBlank()) {
                    return ResponseEntity.badRequest().body(
                        Map.of("mensaje", "Datos invalidos", "codigo", 400));
                }
                t.setTitulo(datos.getTitulo());
                t.setDescripcion(datos.getDescripcion());
                t.setPrioridad(datos.getPrioridad());
                t.setCompletada(datos.getCompletada() != null ? datos.getCompletada() : false);
                return ResponseEntity.ok(Map.of("mensaje", "Tarea actualizada correctamente", "codigo", 200, "datos", t));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Tarea no encontrada", "codigo", 404));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> actualizarParcialmente(@PathVariable Long id, @RequestBody Tarea datos) {
        for (Tarea t : tareas) {
            if (t.getId().equals(id)) {
                if (datos.getTitulo() != null) {
                    if (datos.getTitulo().isBlank()) {
                        return ResponseEntity.badRequest().body(Map.of("mensaje", "El titulo no puede estar vacio", "codigo", 400));
                    }
                    t.setTitulo(datos.getTitulo());
                }
                if (datos.getDescripcion() != null) {
                    t.setDescripcion(datos.getDescripcion());
                }
                if (datos.getPrioridad() != null) {
                    t.setPrioridad(datos.getPrioridad());
                }
                if (datos.getCompletada() != null) {
                    t.setCompletada(datos.getCompletada());
                }
                return ResponseEntity.ok(Map.of("mensaje", "Tarea actualizada parcialmente", "codigo", 200, "datos", t));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Tarea no encontrada", "codigo", 404));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarTarea(@PathVariable Long id) {
        for (Tarea t : tareas) {
            if (t.getId().equals(id)) {
                tareas.remove(t);
                return ResponseEntity.ok(Map.of("mensaje", "Tarea eliminada correctamente", "codigo", 200));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Tarea no encontrada", "codigo", 404));
    }
}