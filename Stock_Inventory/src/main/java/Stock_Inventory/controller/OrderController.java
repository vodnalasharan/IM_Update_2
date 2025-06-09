// OrderController.java (Controller - No Change)
package Stock_Inventory.controller;

import Stock_Inventory.model.Order;
import Stock_Inventory.model.OrderItem;
import Stock_Inventory.service.OrderService;
import Stock_Inventory.dto.OrderCreationRequest;
import Stock_Inventory.dto.OrderItemRequest;
import Stock_Inventory.dto.OrderStatusUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Optional<Order> order = orderService.getOrderById(id);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderCreationRequest request) {
        if (request.getCustomerId() == null || request.getOrderItems() == null || request.getOrderItems().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        Order order = new Order();
        order.setCustomerId(request.getCustomerId());

        List<OrderItem> orderItems = request.getOrderItems().stream().map(itemReq -> {
            OrderItem item = new OrderItem();
            item.setProductId(itemReq.getProductId());
            item.setQuantity(itemReq.getQuantity());
            return item;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);

        try {
            Order createdOrder = orderService.createOrder(order);
            if (createdOrder == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id, @RequestBody OrderStatusUpdateRequest request) {
        if (request.getStatus() == null || request.getStatus().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Order.OrderStatus newStatus = Order.OrderStatus.valueOf(request.getStatus().toUpperCase());
            Order updatedOrder = orderService.updateOrderStatus(id, newStatus);
            return updatedOrder != null ? ResponseEntity.ok(updatedOrder) : ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        Optional<Order> order = orderService.getOrderById(id);
        if (order.isPresent()) {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

// Update -2
//// OrderController.java (Controller - Using DTOs from new package)
//package Stock_Inventory.controller;
//
//import Stock_Inventory.model.Order;
//import Stock_Inventory.model.OrderItem;
//import Stock_Inventory.service.OrderService;
//import Stock_Inventory.dto.OrderCreationRequest;
//import Stock_Inventory.dto.OrderItemRequest;
//import Stock_Inventory.dto.OrderStatusUpdateRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/api/orders")
//public class OrderController {
//
//    @Autowired
//    private OrderService orderService;
//
//    @GetMapping
//    public ResponseEntity<List<Order>> getAllOrders() {
//        return ResponseEntity.ok(orderService.getAllOrders());
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
//        Optional<Order> order = orderService.getOrderById(id);
//        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    @PostMapping
//    public ResponseEntity<Order> createOrder(@RequestBody OrderCreationRequest request) {
//        if (request.getCustomerId() == null || request.getOrderItems() == null || request.getOrderItems().isEmpty()) {
//            return ResponseEntity.badRequest().body(null);
//        }
//
//        Order order = new Order();
//        order.setCustomerId(request.getCustomerId());
//
//        // Convert DTO OrderItemRequest list to Model OrderItem list
//        List<OrderItem> orderItems = request.getOrderItems().stream().map(itemReq -> {
//            OrderItem item = new OrderItem();
//            item.setProductId(itemReq.getProductId());
//            item.setQuantity(itemReq.getQuantity());
//            return item;
//        }).collect(Collectors.toList());
//
//        order.setOrderItems(orderItems);
//
//        try {
//            Order createdOrder = orderService.createOrder(order);
//            if (createdOrder == null) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//            }
//            return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
//        } catch (IllegalArgumentException e) {
//            // Catches errors like "Product not found"
//            return ResponseEntity.badRequest().body(null);
//        } catch (IllegalStateException e) {
//            // Catches errors like "Insufficient stock"
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
//        } catch (Exception e) {
//            // General catch for unexpected server errors
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
//    @PutMapping("/{id}/status")
//    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id, @RequestBody OrderStatusUpdateRequest request) {
//        if (request.getStatus() == null || request.getStatus().isBlank()) {
//            return ResponseEntity.badRequest().build();
//        }
//        try {
//            Order.OrderStatus newStatus = Order.OrderStatus.valueOf(request.getStatus().toUpperCase());
//            Order updatedOrder = orderService.updateOrderStatus(id, newStatus);
//            return updatedOrder != null ? ResponseEntity.ok(updatedOrder) : ResponseEntity.notFound().build();
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(null); // Invalid status enum string
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
//        Optional<Order> order = orderService.getOrderById(id);
//        if (order.isPresent()) {
//            orderService.deleteOrder(id);
//            return ResponseEntity.noContent().build();
//        }
//        return ResponseEntity.notFound().build();
//    }
//}