package Stock_Inventory.controller;

import Stock_Inventory.dto.OrderCreateRequest;
import Stock_Inventory.dto.OrderResponseDTO;
import Stock_Inventory.model.OrderStatus; // <--- IMPORTANT: Reverted to top-level import
import Stock_Inventory.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        try {
            OrderResponseDTO orderDTO = orderService.createOrder(request);
            return new ResponseEntity<>(orderDTO, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(order -> new ResponseEntity<>(order, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) { // <--- Now uses the top-level enum
        try {
            OrderResponseDTO updatedOrder = orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok(updatedOrder);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}


//package Stock_Inventory.controller;
//
//import Stock_Inventory.dto.OrderCreateRequest;
//import Stock_Inventory.dto.OrderResponseDTO;
//import Stock_Inventory.model.OrderStatus; // Make sure OrderStatus is imported
//import Stock_Inventory.service.OrderService;
//import jakarta.persistence.EntityNotFoundException;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/orders")
//public class OrderController {
//
//    @Autowired
//    private OrderService orderService;
//
//    @PostMapping
//    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderCreateRequest request) {
//        try {
//            OrderResponseDTO orderDTO = orderService.createOrder(request);
//            return new ResponseEntity<>(orderDTO, HttpStatus.CREATED);
//        } catch (EntityNotFoundException e) {
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
//        }
//    }
//
//    @GetMapping
//    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
//        List<OrderResponseDTO> orders = orderService.getAllOrders();
//        return new ResponseEntity<>(orders, HttpStatus.OK);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
//        return orderService.getOrderById(id)
//                .map(order -> new ResponseEntity<>(order, HttpStatus.OK))
//                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }
//
//    @PutMapping("/{id}/status")
//    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
//            @PathVariable Long id,
//            @RequestParam OrderStatus status) { // Uses OrderStatus enum
//        try {
//            OrderResponseDTO updatedOrder = orderService.updateOrderStatus(id, status);
//            return ResponseEntity.ok(updatedOrder);
//        } catch (EntityNotFoundException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
//        try {
//            orderService.deleteOrder(id);
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        } catch (EntityNotFoundException e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//}


//package Stock_Inventory.controller;
//
//import Stock_Inventory.dto.OrderCreateRequest;
//import Stock_Inventory.dto.OrderResponseDTO; // Import the new DTO
//import Stock_Inventory.model.OrderStatus;
//import Stock_Inventory.service.OrderService;
//import jakarta.persistence.EntityNotFoundException;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/orders")
//public class OrderController {
//
//    @Autowired
//    private OrderService orderService;
//
//    /**
//     * Creates a new order.
//     * @param request DTO containing customerId and order items.
//     * @return ResponseEntity with the created OrderResponseDTO and HTTP status 201.
//     */
//    @PostMapping
//    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderCreateRequest request) {
//        try {
//            OrderResponseDTO orderDTO = orderService.createOrder(request);
//            return new ResponseEntity<>(orderDTO, HttpStatus.CREATED);
//        } catch (EntityNotFoundException e) {
//            // Log the exception details for debugging
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // Customer or Product not found
//        } catch (IllegalArgumentException e) {
//            // Log the exception details for debugging
//            return new ResponseEntity<>(null, HttpStatus.CONFLICT); // Insufficient stock
//        }
//    }
//
//    /**
//     * Retrieves a list of all orders.
//     * @return ResponseEntity with a list of OrderResponseDTOs and HTTP status 200.
//     */
//    @GetMapping
//    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
//        List<OrderResponseDTO> orders = orderService.getAllOrders();
//        return new ResponseEntity<>(orders, HttpStatus.OK);
//    }
//
//    /**
//     * Retrieves an order by its unique order ID.
//     * @param id The ID of the order.
//     * @return ResponseEntity with the OrderResponseDTO and HTTP status 200, or 404 if not found.
//     */
//    @GetMapping("/{id}")
//    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
//        return orderService.getOrderById(id)
//                .map(order -> new ResponseEntity<>(order, HttpStatus.OK))
//                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }
//
//    /**
//     * Updates the status of an existing order.
//     * @param id The ID of the order to update.
//     * @param status The new status for the order.
//     * @return ResponseEntity with the updated OrderResponseDTO and HTTP status 200, or 404 if not found.
//     */
//    @PutMapping("/{id}/status")
//    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
//            @PathVariable Long id,
//            @RequestParam OrderStatus status) {
//        try {
//            OrderResponseDTO updatedOrder = orderService.updateOrderStatus(id, status);
//            return ResponseEntity.ok(updatedOrder);
//        } catch (EntityNotFoundException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    /**
//     * Deletes an order by its ID.
//     * @param id The ID of the order to delete.
//     * @return ResponseEntity with HTTP status 204 (No Content) on success, or 404 if not found.
//     */
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
//        try {
//            orderService.deleteOrder(id);
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        } catch (EntityNotFoundException e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//}


//// src/main/java/Stock_Inventory/controller/OrderController.java
//package Stock_Inventory.controller;
//
//import Stock_Inventory.dto.OrderCreateRequest;
//import Stock_Inventory.dto.OrderStatusUpdateRequest;
//import Stock_Inventory.model.Order;
//import Stock_Inventory.service.OrderService;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/orders")
//public class OrderController {
//
//    @Autowired
//    private OrderService orderService;
//
//    @PostMapping
//    public ResponseEntity<Order> createOrder(@Valid @RequestBody OrderCreateRequest request) {
//        try {
//            Order order = orderService.createOrder(request);
//            return new ResponseEntity<>(order, HttpStatus.CREATED);
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); // Insufficient stock, etc.
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // Customer/Product not found
//        }
//    }
//
//    @GetMapping
//    public ResponseEntity<List<Order>> getAllOrders() {
//        List<Order> orders = orderService.getAllOrders();
//        return new ResponseEntity<>(orders, HttpStatus.OK);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
//        return orderService.getOrderById(id)
//                .map(order -> new ResponseEntity<>(order, HttpStatus.OK))
//                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }
//
//    @PatchMapping("/{id}/status")
//    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id, @Valid @RequestBody OrderStatusUpdateRequest request) {
//        try {
//            Order updatedOrder = orderService.updateOrderStatus(id, request);
//            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
//        try {
//            orderService.deleteOrder(id);
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//}