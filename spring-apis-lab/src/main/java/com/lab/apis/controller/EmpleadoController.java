package com.lab.apis.controller;

import com.lab.apis.model.Empleado;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    private List<Empleado> empleados = new ArrayList<>(
        List.of(
            new Empleado(1L, "Pedro Sanchez", "Desarrollador Backend", 8500.00, "Tecnologia"),
            new Empleado(2L, "Laura Diaz", "Analista de Recursos Humanos", 6200.00, "Recursos Humanos"),
            new Empleado(3L, "Miguel Torres", "Contador", 7100.00, "Finanzas"),
            new Empleado(4L, "Sofia Castillo", "Disenadora UX/UI", 7500.00, "Tecnologia"),
            new Empleado(5L, "Diego Morales", "Gerente de Ventas", 9800.00, "Ventas")
        )
    );

    @GetMapping
    public ResponseEntity<?> obtenerEmpleados() {
        return ResponseEntity.ok(Map.of(
            "mensaje", "Empleados obtenidos correctamente",
            "total", empleados.size(),
            "datos", empleados
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerEmpleado(@PathVariable Long id) {
        for (Empleado e : empleados) {
            if (e.getId().equals(id)) {
                return ResponseEntity.ok(Map.of("mensaje", "Empleado encontrado", "datos", e));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Empleado no encontrado", "codigo", 404));
    }

    @PostMapping
    public ResponseEntity<?> crearEmpleado(@RequestBody Empleado empleado) {
        if (empleado.getNombre() == null || empleado.getNombre().isBlank()
                || empleado.getPuesto() == null || empleado.getPuesto().isBlank()
                || empleado.getSalario() == null || empleado.getSalario() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("mensaje", "Datos invalidos", "codigo", 400));
        }
        empleado.setId((long) empleados.size() + 1);
        empleados.add(empleado);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            Map.of("mensaje", "Empleado creado correctamente", "codigo", 201, "datos", empleado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarEmpleado(@PathVariable Long id, @RequestBody Empleado datos) {
        for (Empleado e : empleados) {
            if (e.getId().equals(id)) {
                if (datos.getNombre() == null || datos.getNombre().isBlank()
                        || datos.getPuesto() == null || datos.getPuesto().isBlank()
                        || datos.getSalario() == null || datos.getSalario() <= 0) {
                    return ResponseEntity.badRequest().body(
                        Map.of("mensaje", "Datos invalidos", "codigo", 400));
                }
                e.setNombre(datos.getNombre());
                e.setPuesto(datos.getPuesto());
                e.setSalario(datos.getSalario());
                e.setDepartamento(datos.getDepartamento());
                return ResponseEntity.ok(Map.of("mensaje", "Empleado actualizado correctamente", "codigo", 200, "datos", e));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Empleado no encontrado", "codigo", 404));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> actualizarParcialmente(@PathVariable Long id, @RequestBody Empleado datos) {
        for (Empleado e : empleados) {
            if (e.getId().equals(id)) {
                if (datos.getNombre() != null) {
                    if (datos.getNombre().isBlank()) {
                        return ResponseEntity.badRequest().body(Map.of("mensaje", "El nombre no puede estar vacio", "codigo", 400));
                    }
                    e.setNombre(datos.getNombre());
                }
                if (datos.getPuesto() != null) {
                    if (datos.getPuesto().isBlank()) {
                        return ResponseEntity.badRequest().body(Map.of("mensaje", "El puesto no puede estar vacio", "codigo", 400));
                    }
                    e.setPuesto(datos.getPuesto());
                }
                if (datos.getSalario() != null) {
                    if (datos.getSalario() <= 0) {
                        return ResponseEntity.badRequest().body(Map.of("mensaje", "El salario debe ser mayor que cero", "codigo", 400));
                    }
                    e.setSalario(datos.getSalario());
                }
                if (datos.getDepartamento() != null) {
                    e.setDepartamento(datos.getDepartamento());
                }
                return ResponseEntity.ok(Map.of("mensaje", "Empleado actualizado parcialmente", "codigo", 200, "datos", e));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Empleado no encontrado", "codigo", 404));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEmpleado(@PathVariable Long id) {
        for (Empleado e : empleados) {
            if (e.getId().equals(id)) {
                empleados.remove(e);
                return ResponseEntity.ok(Map.of("mensaje", "Empleado eliminado correctamente", "codigo", 200));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Empleado no encontrado", "codigo", 404));
    }
}