package com.lab.apis.controller;

import com.lab.apis.model.Cliente;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private List<Cliente> clientes = new ArrayList<>(
        List.of(
            new Cliente(1L, "Andrea", "Fuentes", "andrea.fuentes@correo.com", "50212345678"),
            new Cliente(2L, "Roberto", "Chavez", "roberto.chavez@correo.com", "50223456789"),
            new Cliente(3L, "Patricia", "Reyes", "patricia.reyes@correo.com", "50234567890"),
            new Cliente(4L, "Fernando", "Ortiz", "fernando.ortiz@correo.com", "50245678901"),
            new Cliente(5L, "Gabriela", "Mendez", "gabriela.mendez@correo.com", "50256789012")
        )
    );

    @GetMapping
    public ResponseEntity<?> obtenerClientes() {
        return ResponseEntity.ok(Map.of(
            "mensaje", "Clientes obtenidos correctamente",
            "total", clientes.size(),
            "datos", clientes
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerCliente(@PathVariable Long id) {
        for (Cliente c : clientes) {
            if (c.getId().equals(id)) {
                return ResponseEntity.ok(Map.of("mensaje", "Cliente encontrado", "datos", c));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Cliente no encontrado", "codigo", 404));
    }

    @PostMapping
    public ResponseEntity<?> crearCliente(@RequestBody Cliente cliente) {
        if (cliente.getNombre() == null || cliente.getNombre().isBlank()
                || cliente.getApellido() == null || cliente.getApellido().isBlank()
                || cliente.getCorreo() == null || cliente.getCorreo().isBlank()
                || !cliente.getCorreo().contains("@")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("mensaje", "Datos invalidos", "codigo", 400));
        }
        for (Cliente item : clientes) {
            if (item.getCorreo().equalsIgnoreCase(cliente.getCorreo())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("mensaje", "Ya existe un cliente con ese correo", "codigo", 409));
            }
        }
        cliente.setId((long) clientes.size() + 1);
        clientes.add(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            Map.of("mensaje", "Cliente creado correctamente", "codigo", 201, "datos", cliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCliente(@PathVariable Long id, @RequestBody Cliente datos) {
        for (Cliente c : clientes) {
            if (c.getId().equals(id)) {
                if (datos.getNombre() == null || datos.getNombre().isBlank()
                        || datos.getApellido() == null || datos.getApellido().isBlank()
                        || datos.getCorreo() == null || datos.getCorreo().isBlank()
                        || !datos.getCorreo().contains("@")) {
                    return ResponseEntity.badRequest().body(
                        Map.of("mensaje", "Datos invalidos", "codigo", 400));
                }
                c.setNombre(datos.getNombre());
                c.setApellido(datos.getApellido());
                c.setCorreo(datos.getCorreo());
                c.setTelefono(datos.getTelefono());
                return ResponseEntity.ok(Map.of("mensaje", "Cliente actualizado correctamente", "codigo", 200, "datos", c));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Cliente no encontrado", "codigo", 404));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> actualizarParcialmente(@PathVariable Long id, @RequestBody Cliente datos) {
        for (Cliente c : clientes) {
            if (c.getId().equals(id)) {
                if (datos.getNombre() != null) {
                    if (datos.getNombre().isBlank()) {
                        return ResponseEntity.badRequest().body(Map.of("mensaje", "El nombre no puede estar vacio", "codigo", 400));
                    }
                    c.setNombre(datos.getNombre());
                }
                if (datos.getApellido() != null) {
                    if (datos.getApellido().isBlank()) {
                        return ResponseEntity.badRequest().body(Map.of("mensaje", "El apellido no puede estar vacio", "codigo", 400));
                    }
                    c.setApellido(datos.getApellido());
                }
                if (datos.getCorreo() != null) {
                    if (datos.getCorreo().isBlank() || !datos.getCorreo().contains("@")) {
                        return ResponseEntity.badRequest().body(Map.of("mensaje", "El correo no es valido", "codigo", 400));
                    }
                    c.setCorreo(datos.getCorreo());
                }
                if (datos.getTelefono() != null) {
                    c.setTelefono(datos.getTelefono());
                }
                return ResponseEntity.ok(Map.of("mensaje", "Cliente actualizado parcialmente", "codigo", 200, "datos", c));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Cliente no encontrado", "codigo", 404));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCliente(@PathVariable Long id) {
        for (Cliente c : clientes) {
            if (c.getId().equals(id)) {
                clientes.remove(c);
                return ResponseEntity.ok(Map.of("mensaje", "Cliente eliminado correctamente", "codigo", 200));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Cliente no encontrado", "codigo", 404));
    }
}