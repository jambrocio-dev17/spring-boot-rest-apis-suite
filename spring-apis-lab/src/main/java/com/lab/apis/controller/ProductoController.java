package com.lab.apis.controller;

import com.lab.apis.model.Producto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private List<Producto> productos = new ArrayList<>(
        List.of(
            new Producto(1L, "Laptop Lenovo ThinkPad", 6500.00, "Tecnologia"),
            new Producto(2L, "Mouse Inalambrico Logitech", 120.50, "Tecnologia"),
            new Producto(3L, "Escritorio de Oficina", 950.00, "Muebles"),
            new Producto(4L, "Silla Ergonomica", 780.00, "Muebles"),
            new Producto(5L, "Cafetera Oster", 350.00, "Electrodomesticos")
        )
    );

    @GetMapping
    public ResponseEntity<?> obtenerProductos() {
        return ResponseEntity.ok(Map.of(
            "mensaje", "Productos obtenidos correctamente",
            "total", productos.size(),
            "datos", productos
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerProducto(@PathVariable Long id) {
        for (Producto p : productos) {
            if (p.getId().equals(id)) {
                return ResponseEntity.ok(Map.of("mensaje", "Producto encontrado", "datos", p));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Producto no encontrado", "codigo", 404));
    }

    @PostMapping
    public ResponseEntity<?> crearProducto(@RequestBody Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().isBlank()
                || producto.getPrecio() == null || producto.getPrecio() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("mensaje", "Datos invalidos", "codigo", 400));
        }
        for (Producto item : productos) {
            if (item.getNombre().equalsIgnoreCase(producto.getNombre())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("mensaje", "Ya existe un producto con ese nombre", "codigo", 409));
            }
        }
        producto.setId((long) productos.size() + 1);
        productos.add(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            Map.of("mensaje", "Producto creado correctamente", "codigo", 201, "datos", producto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id, @RequestBody Producto datos) {
        for (Producto p : productos) {
            if (p.getId().equals(id)) {
                if (datos.getNombre() == null || datos.getNombre().isBlank()
                        || datos.getPrecio() == null || datos.getPrecio() <= 0) {
                    return ResponseEntity.badRequest().body(
                        Map.of("mensaje", "Datos invalidos", "codigo", 400));
                }
                p.setNombre(datos.getNombre());
                p.setPrecio(datos.getPrecio());
                p.setCategoria(datos.getCategoria());
                return ResponseEntity.ok(Map.of("mensaje", "Producto actualizado correctamente", "codigo", 200, "datos", p));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Producto no encontrado", "codigo", 404));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> actualizarParcialmente(@PathVariable Long id, @RequestBody Producto datos) {
        for (Producto p : productos) {
            if (p.getId().equals(id)) {
                if (datos.getNombre() != null) {
                    if (datos.getNombre().isBlank()) {
                        return ResponseEntity.badRequest().body(Map.of("mensaje", "El nombre no puede estar vacio", "codigo", 400));
                    }
                    p.setNombre(datos.getNombre());
                }
                if (datos.getPrecio() != null) {
                    if (datos.getPrecio() <= 0) {
                        return ResponseEntity.badRequest().body(Map.of("mensaje", "El precio debe ser mayor que cero", "codigo", 400));
                    }
                    p.setPrecio(datos.getPrecio());
                }
                if (datos.getCategoria() != null) {
                    p.setCategoria(datos.getCategoria());
                }
                return ResponseEntity.ok(Map.of("mensaje", "Producto actualizado parcialmente", "codigo", 200, "datos", p));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Producto no encontrado", "codigo", 404));
    }

    @DeleteMapping("/{id}")
        public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
            for (Producto p : productos) {
                if (p.getId().equals(id)) {
                    productos.remove(p);
                    return ResponseEntity.ok(Map.of(
                        "mensaje", "Producto eliminado correctamente",
                        "codigo", 200
                    ));
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("mensaje", "Producto no encontrado", "codigo", 404));
        }
}