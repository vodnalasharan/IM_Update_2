package com.example.inventorymanagement.service;

import com.example.inventorymanagement.exception.ResourceNotFoundException;
import com.example.inventorymanagement.model.Supplier;
import com.example.inventorymanagement.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", id));
    }

    public Supplier addSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    public Supplier updateSupplier(Long id, Supplier supplierDetails) {
        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", id));

        existingSupplier.setName(supplierDetails.getName());
        existingSupplier.setContactInfo(supplierDetails.getContactInfo());
        existingSupplier.setProductsSupplied(supplierDetails.getProductsSupplied());
        return supplierRepository.save(existingSupplier);
    }

    public void deleteSupplier(Long id) {
        if (!supplierRepository.existsById(id)) {
            throw new ResourceNotFoundException("Supplier", "id", id);
        }
        supplierRepository.deleteById(id);
    }
}