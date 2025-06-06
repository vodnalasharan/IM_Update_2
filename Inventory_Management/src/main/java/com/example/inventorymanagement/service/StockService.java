package com.example.inventorymanagement.service;

import com.example.inventorymanagement.exception.ResourceNotFoundException;
import com.example.inventorymanagement.model.Stock;
import com.example.inventorymanagement.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    public Stock getStockByProductId(Long productId) {
        return stockRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock", "productId", productId));
    }

    public Stock updateStock(Stock stock) {
        // Ensure the stock for the given productId exists before updating
        stockRepository.findById(stock.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Stock", "productId", stock.getProductId()));
        return stockRepository.save(stock);
    }

    @Transactional
    public void updateStockAfterOrder(Long productId, Integer quantity) {
        Stock stock = stockRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock", "productId", productId));

        int newQuantity = stock.getQuantity() - quantity;
        if (newQuantity >= 0) {
            stock.setQuantity(newQuantity);
            stockRepository.save(stock);
            log.info("Stock for product ID {} decreased by {}. New quantity: {}", productId, quantity, newQuantity);
            if (newQuantity <= stock.getReorderLevel()) {
                log.warn("Low stock for product ID: {}. Current stock: {}", productId, newQuantity);
                // In a real application, you'd send an alert here (email, notification, etc.)
            }
        } else {
            log.error("Insufficient stock for product ID: {}. Current stock: {}, Ordered quantity: {}", productId, stock.getQuantity(), quantity);
            // You might want to throw a custom exception here, e.g., InsufficientStockException
            throw new RuntimeException("Insufficient stock for product ID: " + productId);
        }
    }

    @Transactional
    public void updateStockOnShipment(Long productId, Integer quantity) {
        Stock stock = stockRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock", "productId", productId));

        stock.setQuantity(stock.getQuantity() + quantity);
        stockRepository.save(stock);
        log.info("Stock for product ID {} increased by {}. New quantity: {}", productId, quantity, stock.getQuantity());
    }

    public Stock addStock(Stock stock) {
        // Check if stock for this product already exists to prevent duplicates if productId is primary key
        if (stock.getProductId() != null && stockRepository.existsById(stock.getProductId())) {
            log.warn("Stock entry for product ID {} already exists. Consider using updateStock instead.", stock.getProductId());
            // Optionally, throw an exception or update instead
        }
        return stockRepository.save(stock);
    }
}