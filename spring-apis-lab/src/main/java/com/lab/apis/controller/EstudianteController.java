package com.lab.apis.controller;

import com.lab.apis.model.Estudiante;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    private List<Estudiante> estudiantes = new ArrayList<>(
        List.of(
            new Estudiante(1L, "Maria", "Gonzalez", "Ingenieria en Sistemas", 21),
            new Estudiante(2L, "Carlos", "Ramirez", "Administracion de Empresas", 23),
            new Estudiante(3L, "Ana", "Lopez", "Ingenieria Industrial", 20),
            new Estudiante(4L, "Jose", "Martinez", "Arquitectura", 22),
            new Estudiante(5L, "Lucia", "Perez", "Ingenieria en Sistemas", 24)
        )
    );

    @GetMapping
    public ResponseEntity<?> obtenerEstudiantes() {
        return ResponseEntity.ok(Map.of(
            "mensaje", "Estudiantes obtenidos correctamente",
            "total", estudiantes.size(),
            "datos", estudiantes
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerEstudiante(@PathVariable Long id) {
        for (Estudiante e : estudiantes) {
            if (e.getId().equals(id)) {
                return ResponseEntity.ok(Map.of("mensaje", "Estudiante encontrado", "datos", e));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Estudiante no encontrado", "codigo", 404));
    }

    @PostMapping
    public ResponseEntity<?> crearEstudiante(@RequestBody Estudiante estudiante) {
        if (estudiante.getNombre() == null || estudiante.getNombre().isBlank()
                || estudiante.getApellido() == null || estudiante.getApellido().isBlank()
                || estudiante.getCarrera() == null || estudiante.getCarrera().isBlank()
                || estudiante.getEdad() == null || estudiante.getEdad() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("mensaje", "Datos invalidos", "codigo", 400));
        }
        estudiante.setId((long) estudiantes.size() + 1);
        estudiantes.add(estudiante);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            Map.of("mensaje", "Estudiante creado correctamente", "codigo", 201, "datos", estudiante));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarEstudiante(@PathVariable Long id, @RequestBody Estudiante datos) {
        for (Estudiante e : estudiantes) {
            if (e.getId().equals(id)) {
                if (datos.getNombre() == null || datos.getNombre().isBlank()
                        || datos.getApellido() == null || datos.getApellido().isBlank()
                        || datos.getCarrera() == null || datos.getCarrera().isBlank()
                        || datos.getEdad() == null || datos.getEdad() <= 0) {
                    return ResponseEntity.badRequest().body(
                        Map.of("mensaje", "Datos invalidos", "codigo", 400));
                }
                e.setNombre(datos.getNombre());
                e.setApellido(datos.getApellido());
                e.setCarrera(datos.getCarrera());
                e.setEdad(datos.getEdad());
                return ResponseEntity.ok(Map.of("mensaje", "Estudiante actualizado correctamente", "codigo", 200, "datos", e));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Estudiante no encontrado", "codigo", 404));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> actualizarParcialmente(@PathVariable Long id, @RequestBody Estudiante datos) {
        for (Estudiante e : estudiantes) {
            if (e.getId().equals(id)) {
                if (datos.getNombre() != null) {
                    if (datos.getNombre().isBlank()) {
                        return ResponseEntity.badRequest().body(Map.of("mensaje", "El nombre no puede estar vacio", "codigo", 400));
                    }
                    e.setNombre(datos.getNombre());
                }
                if (datos.getApellido() != null) {
                    if (datos.getApellido().isBlank()) {
                        return ResponseEntity.badRequest().body(Map.of("mensaje", "El apellido no puede estar vacio", "codigo", 400));
                    }
                    e.setApellido(datos.getApellido());
                }
                if (datos.getCarrera() != null) {
                    if (datos.getCarrera().isBlank()) {
                        return ResponseEntity.badRequest().body(Map.of("mensaje", "La carrera no puede estar vacia", "codigo", 400));
                    }
                    e.setCarrera(datos.getCarrera());
                }
                if (datos.getEdad() != null) {
                    if (datos.getEdad() <= 0) {
                        return ResponseEntity.badRequest().body(Map.of("mensaje", "La edad debe ser mayor que cero", "codigo", 400));
                    }
                    e.setEdad(datos.getEdad());
                }
                return ResponseEntity.ok(Map.of("mensaje", "Estudiante actualizado parcialmente", "codigo", 200, "datos", e));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Estudiante no encontrado", "codigo", 404));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEstudiante(@PathVariable Long id) {
        for (Estudiante e : estudiantes) {
            if (e.getId().equals(id)) {
                estudiantes.remove(e);
                return ResponseEntity.ok(Map.of(
                    "mensaje", "Estudiante eliminado correctamente",
                    "codigo", 200
                ));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Estudiante no encontrado", "codigo", 404));
    }
}