package com.example.inventorymanagement.repository;

import com.example.inventorymanagement.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Custom query methods (e.g., findByCustomerId, findByStatus)
}