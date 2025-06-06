package com.example.inventorymanagement.service;


import com.example.inventorymanagement.exception.ResourceNotFoundException;
import com.example.inventorymanagement.model.Order;
import com.example.inventorymanagement.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private StockService stockService;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
    }

    @Transactional // Ensures order creation and stock update are atomic
    public Order createOrder(Order order) {
        order.setOrderDate(LocalDate.now());
        Order savedOrder = orderRepository.save(order);
        // This is the key part: decrease stock upon order creation
        stockService.updateStockAfterOrder(order.getProductId(), order.getQuantity());
        return savedOrder;
    }

    public Order updateOrder(Long id, Order orderDetails) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));

        existingOrder.setCustomerId(orderDetails.getCustomerId());
        existingOrder.setProductId(orderDetails.getProductId());
        existingOrder.setQuantity(orderDetails.getQuantity());
        existingOrder.setOrderDate(orderDetails.getOrderDate());
        existingOrder.setStatus(orderDetails.getStatus());
        return orderRepository.save(existingOrder);
    }

    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order", "id", id);
        }
        orderRepository.deleteById(id);
    }
}