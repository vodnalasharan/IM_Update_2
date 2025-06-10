package Stock_Inventory.service;

import Stock_Inventory.dto.OrderCreateRequest;
import Stock_Inventory.dto.OrderResponseDTO;
import Stock_Inventory.model.Customer;
import Stock_Inventory.model.Order;
import Stock_Inventory.model.OrderItem;
import Stock_Inventory.model.Product;
import Stock_Inventory.model.Stock;
import Stock_Inventory.model.OrderStatus; // <--- IMPORTANT: Reverted to top-level import
import Stock_Inventory.repository.CustomerRepository;
import Stock_Inventory.repository.OrderRepository;
import Stock_Inventory.repository.ProductRepository;
import Stock_Inventory.repository.StockRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

    private OrderResponseDTO convertToDto(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(order.getOrderId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.calculateTotalAmount());

        if (order.getCustomer() != null) {
            dto.setCustomerId(order.getCustomer().getCustomerId());
        }

        if (order.getOrderItems() != null) {
            List<OrderResponseDTO.OrderItemResponseDTO> itemDtos = order.getOrderItems().stream()
                    .map(item -> {
                        OrderResponseDTO.OrderItemResponseDTO itemDto = new OrderResponseDTO.OrderItemResponseDTO();
                        itemDto.setId(item.getOrderItemId());
                        itemDto.setQuantity(item.getQuantity());
                        itemDto.setPriceAtOrder(item.getPriceAtOrder());
                        if (item.getProduct() != null) {
                            itemDto.setProductId(item.getProduct().getProductId());
                        }
                        return itemDto;
                    })
                    .collect(Collectors.toList());
            dto.setOrderItems(itemDtos);
        }
        return dto;
    }

    public OrderResponseDTO createOrder(OrderCreateRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + request.getCustomerId()));

        Order order = new Order();
        order.setCustomer(customer);
        // Default status and date are handled by @PrePersist in Order.java

        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderCreateRequest.OrderItemRequest itemRequest : request.getOrderItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + itemRequest.getProductId()));

            Stock stock = stockRepository.findByProduct_ProductId(itemRequest.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Stock entry not found for product ID: " + itemRequest.getProductId()));

            if (stock.getQuantity() < itemRequest.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for product: " + product.getName() + ". Available: " + stock.getQuantity() + ", Requested: " + itemRequest.getQuantity());
            }

            stock.setQuantity(stock.getQuantity() - itemRequest.getQuantity());
            stockRepository.save(stock);

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPriceAtOrder(product.getPrice());
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        return convertToDto(savedOrder);
    }

    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<OrderResponseDTO> getOrderById(Long id) {
        return orderRepository.findById(id).map(this::convertToDto);
    }

    public OrderResponseDTO updateOrderStatus(Long id, OrderStatus newStatus) { // <--- Now uses the top-level enum
        return orderRepository.findById(id).map(order -> {
            order.setStatus(newStatus);
            Order updatedOrder = orderRepository.save(order);
            return convertToDto(updatedOrder);
        }).orElseThrow(() -> new EntityNotFoundException("Order not found with id " + id));
    }

    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new EntityNotFoundException("Order not found with id " + id);
        }
        orderRepository.deleteById(id);
    }
}

//// src/main/java/Stock_Inventory/service/OrderService.java
//package Stock_Inventory.service;
//
//import Stock_Inventory.dto.OrderCreateRequest;
//import Stock_Inventory.dto.OrderItemRequest;
//import Stock_Inventory.dto.OrderStatusUpdateRequest;
//import Stock_Inventory.model.Order;
//import Stock_Inventory.model.OrderItem;
//import Stock_Inventory.model.Product;
//import Stock_Inventory.model.Stock;
//import Stock_Inventory.repository.CustomerRepository;
//import Stock_Inventory.repository.OrderRepository;
//import Stock_Inventory.repository.ProductRepository;
//import Stock_Inventory.repository.StockRepository;
//import jakarta.transaction.Transactional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//public class OrderService {
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Autowired
//    private CustomerRepository customerRepository;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private StockRepository stockRepository;
//
//    @Transactional
//    public Order createOrder(OrderCreateRequest request) {
//        Order order = new Order();
//        order.setCustomer(customerRepository.findById(request.getCustomerId())
//                .orElseThrow(() -> new RuntimeException("Customer not found with id " + request.getCustomerId())));
//
//        // Process order items and update stock
//        List<OrderItem> orderItems = request.getOrderItems().stream().map(itemRequest -> {
//            Product product = productRepository.findById(itemRequest.getProductId())
//                    .orElseThrow(() -> new RuntimeException("Product not found with id " + itemRequest.getProductId()));
//
//            Stock stock = stockRepository.findByProduct_ProductId(product.getProductId())
//                    .orElseThrow(() -> new RuntimeException("Stock entry not found for product id " + product.getProductId()));
//
//            if (stock.getQuantity() < itemRequest.getQuantity()) {
//                throw new IllegalArgumentException("Insufficient stock for product: " + product.getName() +
//                        ". Available: " + stock.getQuantity() + ", Requested: " + itemRequest.getQuantity());
//            }
//
//            // Deduct stock
//            stock.setQuantity(stock.getQuantity() - itemRequest.getQuantity());
//            stockRepository.save(stock); // Save updated stock
//
//            OrderItem orderItem = new OrderItem();
//            orderItem.setOrder(order); // Set the parent order
//            orderItem.setProductId(itemRequest.getProductId()); // Only store ProductID as per LLD
//            orderItem.setQuantity(itemRequest.getQuantity());
//            orderItem.setPriceAtOrder(itemRequest.getPriceAtOrder()); // Store price at order time
//            return orderItem;
//        }).collect(Collectors.toList());
//
//        order.setOrderItems(orderItems);
//        order.setStatus(Order.OrderStatus.PENDING); // Default status
//        Order savedOrder = orderRepository.save(order);
//
//        // Ensure total amount is calculated for immediate response if needed
//        savedOrder.setTotalAmount(savedOrder.calculateTotalAmount());
//
//        return savedOrder;
//    }
//
//    public List<Order> getAllOrders() {
//        List<Order> orders = orderRepository.findAll();
//        // Calculate total amount for each order when fetching
//        orders.forEach(order -> order.setTotalAmount(order.calculateTotalAmount()));
//        return orders;
//    }
//
//    public Optional<Order> getOrderById(Long id) {
//        Optional<Order> order = orderRepository.findById(id);
//        order.ifPresent(o -> o.setTotalAmount(o.calculateTotalAmount()));
//        return order;
//    }
//
//    @Transactional
//    public Order updateOrderStatus(Long id, OrderStatusUpdateRequest request) {
//        return orderRepository.findById(id).map(order -> {
//            // Handle stock reversal if order is cancelled from a non-pending state
//            if (order.getStatus() != Order.OrderStatus.CANCELLED && request.getStatus() == Order.OrderStatus.CANCELLED) {
//                order.getOrderItems().forEach(orderItem -> {
//                    stockRepository.findByProduct_ProductId(orderItem.getProductId()).ifPresent(stock -> {
//                        stock.setQuantity(stock.getQuantity() + orderItem.getQuantity());
//                        stockRepository.save(stock);
//                    });
//                });
//            }
//            order.setStatus(request.getStatus());
//            Order updatedOrder = orderRepository.save(order);
//            updatedOrder.setTotalAmount(updatedOrder.calculateTotalAmount()); // Recalculate
//            return updatedOrder;
//        }).orElseThrow(() -> new RuntimeException("Order not found with id " + id));
//    }
//
//    @Transactional
//    public void deleteOrder(Long id) {
//        // Before deleting, consider if stock needs to be returned (e.g., if order was PENDING)
//        orderRepository.findById(id).ifPresent(order -> {
//            if (order.getStatus() == Order.OrderStatus.PENDING) {
//                order.getOrderItems().forEach(orderItem -> {
//                    stockRepository.findByProduct_ProductId(orderItem.getProductId()).ifPresent(stock -> {
//                        stock.setQuantity(stock.getQuantity() + orderItem.getQuantity());
//                        stockRepository.save(stock);
//                    });
//                });
//            }
//            orderRepository.delete(order);
//        });
//    }
//}