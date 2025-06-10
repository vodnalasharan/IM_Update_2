// src/main/java/Stock_Inventory/service/SupplierService.java
package Stock_Inventory.service;

import Stock_Inventory.dto.SupplierRequest;
import Stock_Inventory.model.Supplier;
import Stock_Inventory.repository.SupplierRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Transactional
    public Supplier createSupplier(SupplierRequest request) {
        Supplier supplier = new Supplier();
        supplier.setName(request.getName());
        supplier.setContactInfo(request.getContactInfo());
        supplier.setProductsSupplied(request.getProductsSupplied());
        return supplierRepository.save(supplier);
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Optional<Supplier> getSupplierById(Long id) {
        return supplierRepository.findById(id);
    }

    @Transactional
    public Supplier updateSupplier(Long id, SupplierRequest request) {
        return supplierRepository.findById(id).map(supplier -> {
            supplier.setName(request.getName());
            supplier.setContactInfo(request.getContactInfo());
            supplier.setProductsSupplied(request.getProductsSupplied());
            return supplierRepository.save(supplier);
        }).orElseThrow(() -> new RuntimeException("Supplier not found with id " + id));
    }

    @Transactional
    public void deleteSupplier(Long id) {
        supplierRepository.deleteById(id);
    }
}