package com.lab.apis.controller;

import com.lab.apis.model.Libro;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private List<Libro> libros = new ArrayList<>(
        List.of(
            new Libro(1L, "Cien Anios de Soledad", "Gabriel Garcia Marquez", "Realismo Magico", 180.00),
            new Libro(2L, "El Senor de los Anillos", "J.R.R. Tolkien", "Fantasia", 250.00),
            new Libro(3L, "1984", "George Orwell", "Distopia", 150.00),
            new Libro(4L, "Clean Code", "Robert C. Martin", "Tecnico", 320.00),
            new Libro(5L, "El Principito", "Antoine de Saint-Exupery", "Infantil", 100.00)
        )
    );

    @GetMapping
    public ResponseEntity<?> obtenerLibros() {
        return ResponseEntity.ok(Map.of(
            "mensaje", "Libros obtenidos correctamente",
            "total", libros.size(),
            "datos", libros
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerLibro(@PathVariable Long id) {
        for (Libro l : libros) {
            if (l.getId().equals(id)) {
                return ResponseEntity.ok(Map.of("mensaje", "Libro encontrado", "datos", l));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Libro no encontrado", "codigo", 404));
    }

    @PostMapping
    public ResponseEntity<?> crearLibro(@RequestBody Libro libro) {
        if (libro.getTitulo() == null || libro.getTitulo().isBlank()
                || libro.getAutor() == null || libro.getAutor().isBlank()
                || libro.getPrecio() == null || libro.getPrecio() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("mensaje", "Datos invalidos", "codigo", 400));
        }
        for (Libro item : libros) {
            if (item.getTitulo().equalsIgnoreCase(libro.getTitulo())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("mensaje", "Ya existe un libro con ese titulo", "codigo", 409));
            }
        }
        libro.setId((long) libros.size() + 1);
        libros.add(libro);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            Map.of("mensaje", "Libro creado correctamente", "codigo", 201, "datos", libro));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarLibro(@PathVariable Long id, @RequestBody Libro datos) {
        for (Libro l : libros) {
            if (l.getId().equals(id)) {
                if (datos.getTitulo() == null || datos.getTitulo().isBlank()
                        || datos.getAutor() == null || datos.getAutor().isBlank()
                        || datos.getPrecio() == null || datos.getPrecio() <= 0) {
                    return ResponseEntity.badRequest().body(
                        Map.of("mensaje", "Datos invalidos", "codigo", 400));
                }
                l.setTitulo(datos.getTitulo());
                l.setAutor(datos.getAutor());
                l.setGenero(datos.getGenero());
                l.setPrecio(datos.getPrecio());
                return ResponseEntity.ok(Map.of("mensaje", "Libro actualizado correctamente", "codigo", 200, "datos", l));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Libro no encontrado", "codigo", 404));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> actualizarParcialmente(@PathVariable Long id, @RequestBody Libro datos) {
        for (Libro l : libros) {
            if (l.getId().equals(id)) {
                if (datos.getTitulo() != null) {
                    if (datos.getTitulo().isBlank()) {
                        return ResponseEntity.badRequest().body(Map.of("mensaje", "El titulo no puede estar vacio", "codigo", 400));
                    }
                    l.setTitulo(datos.getTitulo());
                }
                if (datos.getAutor() != null) {
                    if (datos.getAutor().isBlank()) {
                        return ResponseEntity.badRequest().body(Map.of("mensaje", "El autor no puede estar vacio", "codigo", 400));
                    }
                    l.setAutor(datos.getAutor());
                }
                if (datos.getGenero() != null) {
                    l.setGenero(datos.getGenero());
                }
                if (datos.getPrecio() != null) {
                    if (datos.getPrecio() <= 0) {
                        return ResponseEntity.badRequest().body(Map.of("mensaje", "El precio debe ser mayor que cero", "codigo", 400));
                    }
                    l.setPrecio(datos.getPrecio());
                }
                return ResponseEntity.ok(Map.of("mensaje", "Libro actualizado parcialmente", "codigo", 200, "datos", l));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Libro no encontrado", "codigo", 404));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarLibro(@PathVariable Long id) {
        for (Libro l : libros) {
            if (l.getId().equals(id)) {
                libros.remove(l);
                return ResponseEntity.ok(Map.of("mensaje", "Libro eliminado correctamente", "codigo", 200));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Libro no encontrado", "codigo", 404));
    }
}