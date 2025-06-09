// OrderService.java (Service - No Change from last iteration)
package Stock_Inventory.service;

import Stock_Inventory.model.Order;
import Stock_Inventory.model.OrderItem;
import Stock_Inventory.model.Product;
import Stock_Inventory.model.Stock;
import Stock_Inventory.repository.OrderItemRepository;
import Stock_Inventory.repository.OrderRepository;
import Stock_Inventory.repository.ProductRepository;
import Stock_Inventory.repository.StockRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

    public List<Order> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        orders.forEach(this::calculateOrderTotal);
        return orders;
    }

    public Optional<Order> getOrderById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        order.ifPresent(this::calculateOrderTotal);
        return order;
    }

    private void calculateOrderTotal(Order order) {
        double total = 0.0;
        if (order.getOrderItems() != null) {
            for (OrderItem item : order.getOrderItems()) {
                total += item.getPriceAtOrder() * item.getQuantity();
            }
        }
        order.setTotalAmount(total);
    }

    @Transactional
    public Order createOrder(Order order) {
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.OrderStatus.PENDING);

        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            log.warn("Attempted to create an order with no items.");
            return null;
        }

        for (OrderItem item : order.getOrderItems()) {
            Optional<Product> productOptional = productRepository.findById(item.getProductId());
            if (productOptional.isEmpty()) {
                log.error("Product with ID {} not found for order item.", item.getProductId());
                throw new IllegalArgumentException("Product not found: " + item.getProductId());
            }
            Product product = productOptional.get();

            Optional<Stock> stockOptional = stockRepository.findByProduct(product);
            if (stockOptional.isEmpty()) {
                log.error("Stock entry not found for product ID {}. Cannot process order.", item.getProductId());
                throw new IllegalStateException("Stock details missing for product: " + product.getName());
            }
            Stock stock = stockOptional.get();

            item.setPriceAtOrder(product.getPrice());

            if (stock.getQuantity() < item.getQuantity()) {
                log.error("Insufficient stock for product ID {}. Available: {}, Requested: {}", item.getProductId(),
                        stock.getQuantity(), item.getQuantity());
                throw new IllegalStateException("Insufficient stock for product: " + product.getName());
            }

            item.setOrder(order);
        }

        Order savedOrder = orderRepository.save(order);
        
        for (OrderItem item : savedOrder.getOrderItems()) {
            Product product = productRepository.findById(item.getProductId())
                                .orElseThrow(() -> new RuntimeException("Product not found after order creation: " + item.getProductId()));
            Stock stock = stockRepository.findByProduct(product)
                                .orElseThrow(() -> new RuntimeException("Stock not found after order creation for product: " + item.getProductId()));

            product.setStockLevel(product.getStockLevel() - item.getQuantity());
            productRepository.save(product);

            stock.setQuantity(stock.getQuantity() - item.getQuantity());
            stockRepository.save(stock);

            log.info("Decremented stock for product ID {}. New Product.stockLevel: {}, New Stock.quantity: {}",
                     item.getProductId(), product.getStockLevel(), stock.getQuantity());
        }

        log.info("Created new order with ID: {}", savedOrder.getOrderId());
        calculateOrderTotal(savedOrder);
        return savedOrder;
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, Order.OrderStatus newStatus) {
        return orderRepository.findById(orderId)
                .map(existingOrder -> {
                    existingOrder.setStatus(newStatus);
                    log.info("Updated status for order ID {} to {}", orderId, newStatus);
                    Order updatedOrder = orderRepository.save(existingOrder);
                    calculateOrderTotal(updatedOrder);
                    return updatedOrder;
                })
                .orElse(null);
    }

    @Transactional
    public void deleteOrder(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            log.info("Deleted order with ID: {}", id);
        } else {
            log.warn("Attempted to delete non-existent order with ID: {}", id);
        }
    }
}



//// OrderService.java (Service - No Change from last iteration)
//package Stock_Inventory.service;
//
//import Stock_Inventory.model.Order;
//import Stock_Inventory.model.OrderItem;
//import Stock_Inventory.model.Product;
//import Stock_Inventory.model.Stock;
//import Stock_Inventory.repository.OrderItemRepository;
//import Stock_Inventory.repository.OrderRepository;
//import Stock_Inventory.repository.ProductRepository;
//import Stock_Inventory.repository.StockRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@Slf4j
//public class OrderService {
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Autowired
//    private OrderItemRepository orderItemRepository;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private StockRepository stockRepository;
//
//    public List<Order> getAllOrders() {
//        List<Order> orders = orderRepository.findAll();
//        orders.forEach(this::calculateOrderTotal);
//        return orders;
//    }
//
//    public Optional<Order> getOrderById(Long id) {
//        Optional<Order> order = orderRepository.findById(id);
//        order.ifPresent(this::calculateOrderTotal);
//        return order;
//    }
//
//    private void calculateOrderTotal(Order order) {
//        double total = 0.0;
//        if (order.getOrderItems() != null) {
//            for (OrderItem item : order.getOrderItems()) {
//                total += item.getPriceAtOrder() * item.getQuantity();
//            }
//        }
//        order.setTotalAmount(total);
//    }
//
//    @Transactional
//    public Order createOrder(Order order) {
//        order.setOrderDate(LocalDateTime.now());
//        order.setStatus(Order.OrderStatus.PENDING);
//
//        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
//            log.warn("Attempted to create an order with no items.");
//            return null;
//        }
//
//        for (OrderItem item : order.getOrderItems()) {
//            Optional<Product> productOptional = productRepository.findById(item.getProductId());
//            if (productOptional.isEmpty()) {
//                log.error("Product with ID {} not found for order item.", item.getProductId());
//                throw new IllegalArgumentException("Product not found: " + item.getProductId());
//            }
//            Product product = productOptional.get();
//
//            Optional<Stock> stockOptional = stockRepository.findByProductId(product);
//            if (stockOptional.isEmpty()) {
//                log.error("Stock entry not found for product ID {}. Cannot process order.", item.getProductId());
//                throw new IllegalStateException("Stock details missing for product: " + product.getName());
//            }
//            Stock stock = stockOptional.get();
//
//            item.setPriceAtOrder(product.getPrice());
//
//            if (stock.getQuantity() < item.getQuantity()) {
//                log.error("Insufficient stock for product ID {}. Available: {}, Requested: {}", item.getProductId(),
//                        stock.getQuantity(), item.getQuantity());
//                throw new IllegalStateException("Insufficient stock for product: " + product.getName());
//            }
//
//            item.setOrder(order);
//        }
//
//        Order savedOrder = orderRepository.save(order);
//        
//        for (OrderItem item : savedOrder.getOrderItems()) {
//            Product product = productRepository.findById(item.getProductId())
//                                .orElseThrow(() -> new RuntimeException("Product not found after order creation: " + item.getProductId()));
//            Stock stock = stockRepository.findByProductId(product)
//                                .orElseThrow(() -> new RuntimeException("Stock not found after order creation for product: " + item.getProductId()));
//
//            product.setStockLevel(product.getStockLevel() - item.getQuantity());
//            productRepository.save(product);
//
//            stock.setQuantity(stock.getQuantity() - item.getQuantity());
//            stockRepository.save(stock);
//
//            log.info("Decremented stock for product ID {}. New Product.stockLevel: {}, New Stock.quantity: {}",
//                     item.getProductId(), product.getStockLevel(), stock.getQuantity());
//        }
//
//        log.info("Created new order with ID: {}", savedOrder.getOrderId());
//        calculateOrderTotal(savedOrder);
//        return savedOrder;
//    }
//
//    @Transactional
//    public Order updateOrderStatus(Long orderId, Order.OrderStatus newStatus) {
//        return orderRepository.findById(orderId)
//                .map(existingOrder -> {
//                    existingOrder.setStatus(newStatus);
//                    log.info("Updated status for order ID {} to {}", orderId, newStatus);
//                    Order updatedOrder = orderRepository.save(existingOrder);
//                    calculateOrderTotal(updatedOrder);
//                    return updatedOrder;
//                })
//                .orElse(null);
//    }
//
//    @Transactional
//    public void deleteOrder(Long id) {
//        if (orderRepository.existsById(id)) {
//            orderRepository.deleteById(id);
//            log.info("Deleted order with ID: {}", id);
//        } else {
//            log.warn("Attempted to delete non-existent order with ID: {}", id);
//        }
//    }
//}