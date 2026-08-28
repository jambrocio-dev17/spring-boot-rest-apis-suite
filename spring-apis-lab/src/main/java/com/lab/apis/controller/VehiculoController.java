package com.lab.apis.controller;

import com.lab.apis.model.Vehiculo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    private List<Vehiculo> vehiculos = new ArrayList<>(
        List.of(
            new Vehiculo(1L, "Toyota", "Corolla", 2022, 145000.00),
            new Vehiculo(2L, "Honda", "Civic", 2021, 150000.00),
            new Vehiculo(3L, "Mazda", "CX-5", 2023, 210000.00),
            new Vehiculo(4L, "Nissan", "Sentra", 2020, 120000.00),
            new Vehiculo(5L, "Ford", "Ranger", 2022, 260000.00)
        )
    );

    @GetMapping
    public ResponseEntity<?> obtenerVehiculos() {
        return ResponseEntity.ok(Map.of(
            "mensaje", "Vehiculos obtenidos correctamente",
            "total", vehiculos.size(),
            "datos", vehiculos
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerVehiculo(@PathVariable Long id) {
        for (Vehiculo v : vehiculos) {
            if (v.getId().equals(id)) {
                return ResponseEntity.ok(Map.of("mensaje", "Vehiculo encontrado", "datos", v));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Vehiculo no encontrado", "codigo", 404));
    }

    @PostMapping
    public ResponseEntity<?> crearVehiculo(@RequestBody Vehiculo vehiculo) {
        if (vehiculo.getMarca() == null || vehiculo.getMarca().isBlank()
                || vehiculo.getModelo() == null || vehiculo.getModelo().isBlank()
                || vehiculo.getAnio() == null || vehiculo.getAnio() <= 0
                || vehiculo.getPrecio() == null || vehiculo.getPrecio() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("mensaje", "Datos invalidos", "codigo", 400));
        }
        vehiculo.setId((long) vehiculos.size() + 1);
        vehiculos.add(vehiculo);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            Map.of("mensaje", "Vehiculo creado correctamente", "codigo", 201, "datos", vehiculo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarVehiculo(@PathVariable Long id, @RequestBody Vehiculo datos) {
        for (Vehiculo v : vehiculos) {
            if (v.getId().equals(id)) {
                if (datos.getMarca() == null || datos.getMarca().isBlank()
                        || datos.getModelo() == null || datos.getModelo().isBlank()
                        || datos.getAnio() == null || datos.getAnio() <= 0
                        || datos.getPrecio() == null || datos.getPrecio() <= 0) {
                    return ResponseEntity.badRequest().body(
                        Map.of("mensaje", "Datos invalidos", "codigo", 400));
                }
                v.setMarca(datos.getMarca());
                v.setModelo(datos.getModelo());
                v.setAnio(datos.getAnio());
                v.setPrecio(datos.getPrecio());
                return ResponseEntity.ok(Map.of("mensaje", "Vehiculo actualizado correctamente", "codigo", 200, "datos", v));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Vehiculo no encontrado", "codigo", 404));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> actualizarParcialmente(@PathVariable Long id, @RequestBody Vehiculo datos) {
        for (Vehiculo v : vehiculos) {
            if (v.getId().equals(id)) {
                if (datos.getMarca() != null) {
                    if (datos.getMarca().isBlank()) {
                        return ResponseEntity.badRequest().body(Map.of("mensaje", "La marca no puede estar vacia", "codigo", 400));
                    }
                    v.setMarca(datos.getMarca());
                }
                if (datos.getModelo() != null) {
                    if (datos.getModelo().isBlank()) {
                        return ResponseEntity.badRequest().body(Map.of("mensaje", "El modelo no puede estar vacio", "codigo", 400));
                    }
                    v.setModelo(datos.getModelo());
                }
                if (datos.getAnio() != null) {
                    if (datos.getAnio() <= 0) {
                        return ResponseEntity.badRequest().body(Map.of("mensaje", "El anio debe ser mayor que cero", "codigo", 400));
                    }
                    v.setAnio(datos.getAnio());
                }
                if (datos.getPrecio() != null) {
                    if (datos.getPrecio() <= 0) {
                        return ResponseEntity.badRequest().body(Map.of("mensaje", "El precio debe ser mayor que cero", "codigo", 400));
                    }
                    v.setPrecio(datos.getPrecio());
                }
                return ResponseEntity.ok(Map.of("mensaje", "Vehiculo actualizado parcialmente", "codigo", 200, "datos", v));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Vehiculo no encontrado", "codigo", 404));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarVehiculo(@PathVariable Long id) {
        for (Vehiculo v : vehiculos) {
            if (v.getId().equals(id)) {
                vehiculos.remove(v);
                return ResponseEntity.ok(Map.of("mensaje", "Vehiculo eliminado correctamente", "codigo", 200));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Vehiculo no encontrado", "codigo", 404));
    }
}