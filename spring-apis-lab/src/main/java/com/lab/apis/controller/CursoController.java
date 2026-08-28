package com.lab.apis.controller;

import com.lab.apis.model.Curso;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    private List<Curso> cursos = new ArrayList<>(
        List.of(
            new Curso(1L, "Programacion I", "Fundamentos de programacion estructurada", 4, "Presencial"),
            new Curso(2L, "Bases de Datos", "Modelado y consultas SQL", 5, "Semipresencial"),
            new Curso(3L, "Desarrollo Web", "Frontend y backend con frameworks modernos", 5, "Virtual"),
            new Curso(4L, "Redes de Computadoras", "Fundamentos de redes y protocolos", 4, "Presencial"),
            new Curso(5L, "Ingenieria de Software", "Metodologias agiles y ciclo de vida del software", 5, "Virtual")
        )
    );

    @GetMapping
    public ResponseEntity<?> obtenerCursos() {
        return ResponseEntity.ok(Map.of(
            "mensaje", "Cursos obtenidos correctamente",
            "total", cursos.size(),
            "datos", cursos
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerCurso(@PathVariable Long id) {
        for (Curso c : cursos) {
            if (c.getId().equals(id)) {
                return ResponseEntity.ok(Map.of("mensaje", "Curso encontrado", "datos", c));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Curso no encontrado", "codigo", 404));
    }

    @PostMapping
    public ResponseEntity<?> crearCurso(@RequestBody Curso curso) {
        if (curso.getNombre() == null || curso.getNombre().isBlank()
                || curso.getCreditos() == null || curso.getCreditos() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("mensaje", "Datos invalidos", "codigo", 400));
        }
        for (Curso item : cursos) {
            if (item.getNombre().equalsIgnoreCase(curso.getNombre())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("mensaje", "Ya existe un curso con ese nombre", "codigo", 409));
            }
        }
        curso.setId((long) cursos.size() + 1);
        cursos.add(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            Map.of("mensaje", "Curso creado correctamente", "codigo", 201, "datos", curso));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCurso(@PathVariable Long id, @RequestBody Curso datos) {
        for (Curso c : cursos) {
            if (c.getId().equals(id)) {
                if (datos.getNombre() == null || datos.getNombre().isBlank()
                        || datos.getCreditos() == null || datos.getCreditos() <= 0) {
                    return ResponseEntity.badRequest().body(
                        Map.of("mensaje", "Datos invalidos", "codigo", 400));
                }
                c.setNombre(datos.getNombre());
                c.setDescripcion(datos.getDescripcion());
                c.setCreditos(datos.getCreditos());
                c.setModalidad(datos.getModalidad());
                return ResponseEntity.ok(Map.of("mensaje", "Curso actualizado correctamente", "codigo", 200, "datos", c));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Curso no encontrado", "codigo", 404));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> actualizarParcialmente(@PathVariable Long id, @RequestBody Curso datos) {
        for (Curso c : cursos) {
            if (c.getId().equals(id)) {
                if (datos.getNombre() != null) {
                    if (datos.getNombre().isBlank()) {
                        return ResponseEntity.badRequest().body(Map.of("mensaje", "El nombre no puede estar vacio", "codigo", 400));
                    }
                    c.setNombre(datos.getNombre());
                }
                if (datos.getDescripcion() != null) {
                    c.setDescripcion(datos.getDescripcion());
                }
                if (datos.getCreditos() != null) {
                    if (datos.getCreditos() <= 0) {
                        return ResponseEntity.badRequest().body(Map.of("mensaje", "Los creditos deben ser mayor que cero", "codigo", 400));
                    }
                    c.setCreditos(datos.getCreditos());
                }
                if (datos.getModalidad() != null) {
                    c.setModalidad(datos.getModalidad());
                }
                return ResponseEntity.ok(Map.of("mensaje", "Curso actualizado parcialmente", "codigo", 200, "datos", c));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Curso no encontrado", "codigo", 404));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCurso(@PathVariable Long id) {
        for (Curso c : cursos) {
            if (c.getId().equals(id)) {
                cursos.remove(c);
                return ResponseEntity.ok(Map.of("mensaje", "Curso eliminado correctamente", "codigo", 200));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Curso no encontrado", "codigo", 404));
    }
}