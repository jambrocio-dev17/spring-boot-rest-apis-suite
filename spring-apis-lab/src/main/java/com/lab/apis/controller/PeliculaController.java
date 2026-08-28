package com.lab.apis.controller;

import com.lab.apis.model.Pelicula;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/peliculas")
public class PeliculaController {

    private List<Pelicula> peliculas = new ArrayList<>(
        List.of(
            new Pelicula(1L, "El Padrino", "Francis Ford Coppola", "Drama", 1972),
            new Pelicula(2L, "Inception", "Christopher Nolan", "Ciencia Ficcion", 2010),
            new Pelicula(3L, "Pulp Fiction", "Quentin Tarantino", "Crimen", 1994),
            new Pelicula(4L, "Coco", "Lee Unkrich", "Animacion", 2017),
            new Pelicula(5L, "Interstellar", "Christopher Nolan", "Ciencia Ficcion", 2014)
        )
    );

    @GetMapping
    public ResponseEntity<?> obtenerPeliculas() {
        return ResponseEntity.ok(Map.of(
            "mensaje", "Peliculas obtenidas correctamente",
            "total", peliculas.size(),
            "datos", peliculas
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPelicula(@PathVariable Long id) {
        for (Pelicula p : peliculas) {
            if (p.getId().equals(id)) {
                return ResponseEntity.ok(Map.of("mensaje", "Pelicula encontrada", "datos", p));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Pelicula no encontrada", "codigo", 404));
    }

    @PostMapping
    public ResponseEntity<?> crearPelicula(@RequestBody Pelicula pelicula) {
        if (pelicula.getTitulo() == null || pelicula.getTitulo().isBlank()
                || pelicula.getDirector() == null || pelicula.getDirector().isBlank()
                || pelicula.getAnio() == null || pelicula.getAnio() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("mensaje", "Datos invalidos", "codigo", 400));
        }
        for (Pelicula item : peliculas) {
            if (item.getTitulo().equalsIgnoreCase(pelicula.getTitulo())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("mensaje", "Ya existe una pelicula con ese titulo", "codigo", 409));
            }
        }
        pelicula.setId((long) peliculas.size() + 1);
        peliculas.add(pelicula);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            Map.of("mensaje", "Pelicula creada correctamente", "codigo", 201, "datos", pelicula));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPelicula(@PathVariable Long id, @RequestBody Pelicula datos) {
        for (Pelicula p : peliculas) {
            if (p.getId().equals(id)) {
                if (datos.getTitulo() == null || datos.getTitulo().isBlank()
                        || datos.getDirector() == null || datos.getDirector().isBlank()
                        || datos.getAnio() == null || datos.getAnio() <= 0) {
                    return ResponseEntity.badRequest().body(
                        Map.of("mensaje", "Datos invalidos", "codigo", 400));
                }
                p.setTitulo(datos.getTitulo());
                p.setDirector(datos.getDirector());
                p.setGenero(datos.getGenero());
                p.setAnio(datos.getAnio());
                return ResponseEntity.ok(Map.of("mensaje", "Pelicula actualizada correctamente", "codigo", 200, "datos", p));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Pelicula no encontrada", "codigo", 404));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> actualizarParcialmente(@PathVariable Long id, @RequestBody Pelicula datos) {
        for (Pelicula p : peliculas) {
            if (p.getId().equals(id)) {
                if (datos.getTitulo() != null) {
                    if (datos.getTitulo().isBlank()) {
                        return ResponseEntity.badRequest().body(Map.of("mensaje", "El titulo no puede estar vacio", "codigo", 400));
                    }
                    p.setTitulo(datos.getTitulo());
                }
                if (datos.getDirector() != null) {
                    if (datos.getDirector().isBlank()) {
                        return ResponseEntity.badRequest().body(Map.of("mensaje", "El director no puede estar vacio", "codigo", 400));
                    }
                    p.setDirector(datos.getDirector());
                }
                if (datos.getGenero() != null) {
                    p.setGenero(datos.getGenero());
                }
                if (datos.getAnio() != null) {
                    if (datos.getAnio() <= 0) {
                        return ResponseEntity.badRequest().body(Map.of("mensaje", "El anio debe ser mayor que cero", "codigo", 400));
                    }
                    p.setAnio(datos.getAnio());
                }
                return ResponseEntity.ok(Map.of("mensaje", "Pelicula actualizada parcialmente", "codigo", 200, "datos", p));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Pelicula no encontrada", "codigo", 404));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPelicula(@PathVariable Long id) {
        for (Pelicula p : peliculas) {
            if (p.getId().equals(id)) {
                peliculas.remove(p);
                return ResponseEntity.ok(Map.of("mensaje", "Pelicula eliminada correctamente", "codigo", 200));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Pelicula no encontrada", "codigo", 404));
    }
}