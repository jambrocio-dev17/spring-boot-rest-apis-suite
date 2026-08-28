package com.lab.apis.controller;

import com.lab.apis.model.Pedido;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private List<Pedido> pedidos = new ArrayList<>(
        List.of(
            new Pedido(1L, "Andrea Fuentes", "Laptop Lenovo ThinkPad", 1, 6500.00, "PENDIENTE"),
            new Pedido(2L, "Roberto Chavez", "Mouse Inalambrico Logitech", 2, 241.00, "ENVIADO"),
            new Pedido(3L, "Patricia Reyes", "Silla Ergonomica", 1, 780.00, "ENTREGADO"),
            new Pedido(4L, "Fernando Ortiz", "Cafetera Oster", 3, 1050.00, "PENDIENTE"),
            new Pedido(5L, "Gabriela Mendez", "Escritorio de Oficina", 1, 950.00, "CANCELADO")
        )
    );

    @GetMapping
    public ResponseEntity<?> obtenerPedidos() {
        return ResponseEntity.ok(Map.of(
            "mensaje", "Pedidos obtenidos correctamente",
            "total", pedidos.size(),
            "datos", pedidos
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPedido(@PathVariable Long id) {
        for (Pedido p : pedidos) {
            if (p.getId().equals(id)) {
                return ResponseEntity.ok(Map.of("mensaje", "Pedido encontrado", "datos", p));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Pedido no encontrado", "codigo", 404));
    }

    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestBody Pedido pedido) {
        if (pedido.getCliente() == null || pedido.getCliente().isBlank()
                || pedido.getProducto() == null || pedido.getProducto().isBlank()
                || pedido.getCantidad() == null || pedido.getCantidad() <= 0
                || pedido.getTotal() == null || pedido.getTotal() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("mensaje", "Datos invalidos", "codigo", 400));
        }
        pedido.setId((long) pedidos.size() + 1);
        if (pedido.getEstado() == null || pedido.getEstado().isBlank()) {
            pedido.setEstado("PENDIENTE");
        }
        pedidos.add(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            Map.of("mensaje", "Pedido creado correctamente", "codigo", 201, "datos", pedido));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPedido(@PathVariable Long id, @RequestBody Pedido datos) {
        for (Pedido p : pedidos) {
            if (p.getId().equals(id)) {
                if (datos.getCliente() == null || datos.getCliente().isBlank()
                        || datos.getProducto() == null || datos.getProducto().isBlank()
                        || datos.getCantidad() == null || datos.getCantidad() <= 0
                        || datos.getTotal() == null || datos.getTotal() <= 0) {
                    return ResponseEntity.badRequest().body(
                        Map.of("mensaje", "Datos invalidos", "codigo", 400));
                }
                p.setCliente(datos.getCliente());
                p.setProducto(datos.getProducto());
                p.setCantidad(datos.getCantidad());
                p.setTotal(datos.getTotal());
                p.setEstado(datos.getEstado() != null ? datos.getEstado() : p.getEstado());
                return ResponseEntity.ok(Map.of("mensaje", "Pedido actualizado correctamente", "codigo", 200, "datos", p));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Pedido no encontrado", "codigo", 404));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> actualizarParcialmente(@PathVariable Long id, @RequestBody Pedido datos) {
        for (Pedido p : pedidos) {
            if (p.getId().equals(id)) {
                if (datos.getCliente() != null) {
                    if (datos.getCliente().isBlank()) {
                        return ResponseEntity.badRequest().body(Map.of("mensaje", "El cliente no puede estar vacio", "codigo", 400));
                    }
                    p.setCliente(datos.getCliente());
                }
                if (datos.getProducto() != null) {
                    if (datos.getProducto().isBlank()) {
                        return ResponseEntity.badRequest().body(Map.of("mensaje", "El producto no puede estar vacio", "codigo", 400));
                    }
                    p.setProducto(datos.getProducto());
                }
                if (datos.getCantidad() != null) {
                    if (datos.getCantidad() <= 0) {
                        return ResponseEntity.badRequest().body(Map.of("mensaje", "La cantidad debe ser mayor que cero", "codigo", 400));
                    }
                    p.setCantidad(datos.getCantidad());
                }
                if (datos.getTotal() != null) {
                    if (datos.getTotal() <= 0) {
                        return ResponseEntity.badRequest().body(Map.of("mensaje", "El total debe ser mayor que cero", "codigo", 400));
                    }
                    p.setTotal(datos.getTotal());
                }
                if (datos.getEstado() != null) {
                    p.setEstado(datos.getEstado());
                }
                return ResponseEntity.ok(Map.of("mensaje", "Pedido actualizado parcialmente", "codigo", 200, "datos", p));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Pedido no encontrado", "codigo", 404));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPedido(@PathVariable Long id) {
        for (Pedido p : pedidos) {
            if (p.getId().equals(id)) {
                pedidos.remove(p);
                return ResponseEntity.ok(Map.of("mensaje", "Pedido eliminado correctamente", "codigo", 200));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("mensaje", "Pedido no encontrado", "codigo", 404));
    }
}