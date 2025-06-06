package com.example.inventorymanagement.repository;


import com.example.inventorymanagement.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    // Custom query methods if needed
}