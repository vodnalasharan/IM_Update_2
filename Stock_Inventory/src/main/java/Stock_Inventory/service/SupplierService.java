// SupplierService.java (Service - No Change)
package Stock_Inventory.service;

import Stock_Inventory.model.Supplier;
import Stock_Inventory.repository.SupplierRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Optional<Supplier> getSupplierById(Long id) {
        return supplierRepository.findById(id);
    }

    @Transactional
    public Supplier addSupplier(Supplier supplier) {
        Optional<Supplier> existingSupplier = supplierRepository.findByName(supplier.getName());
        if (existingSupplier.isPresent()) {
            log.warn("Supplier with name '{}' already exists.", supplier.getName());
            return null;
        }
        log.info("Adding new supplier: {}", supplier.getName());
        return supplierRepository.save(supplier);
    }

    @Transactional
    public Supplier updateSupplier(Long id, Supplier supplierDetails) {
        return supplierRepository.findById(id)
                .map(existingSupplier -> {
                    existingSupplier.setName(supplierDetails.getName());
                    existingSupplier.setContactInfo(supplierDetails.getContactInfo());
                    existingSupplier.setProductsSupplied(supplierDetails.getProductsSupplied());
                    log.info("Updated supplier with ID: {}", id);
                    return supplierRepository.save(existingSupplier);
                })
                .orElse(null);
    }

    @Transactional
    public void deleteSupplier(Long id) {
        if (supplierRepository.existsById(id)) {
            supplierRepository.deleteById(id);
            log.info("Deleted supplier with ID: {}", id);
        } else {
            log.warn("Attempted to delete non-existent supplier with ID: {}", id);
        }
    }
}

//// SupplierService.java (Service)
//package Stock_Inventory.service;
//
//import Stock_Inventory.model.Supplier;
//import Stock_Inventory.repository.SupplierRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@Slf4j
//public class SupplierService {
//
//    @Autowired
//    private SupplierRepository supplierRepository;
//
//    public List<Supplier> getAllSuppliers() {
//        return supplierRepository.findAll();
//    }
//
//    public Optional<Supplier> getSupplierById(Long id) {
//        return supplierRepository.findById(id);
//    }
//
//    @Transactional
//    public Supplier addSupplier(Supplier supplier) {
//        Optional<Supplier> existingSupplier = supplierRepository.findByName(supplier.getName());
//        if (existingSupplier.isPresent()) {
//            log.warn("Supplier with name '{}' already exists.", supplier.getName());
//            return null; // Indicate conflict
//        }
//        log.info("Adding new supplier: {}", supplier.getName());
//        return supplierRepository.save(supplier);
//    }
//
//    @Transactional
//    public Supplier updateSupplier(Long id, Supplier supplierDetails) {
//        return supplierRepository.findById(id)
//                .map(existingSupplier -> {
//                    existingSupplier.setName(supplierDetails.getName());
//                    existingSupplier.setContactInfo(supplierDetails.getContactInfo());
//                    existingSupplier.setProductsSupplied(supplierDetails.getProductsSupplied());
//                    log.info("Updated supplier with ID: {}", id);
//                    return supplierRepository.save(existingSupplier);
//                })
//                .orElse(null); // Supplier not found
//    }
//
//    @Transactional
//    public void deleteSupplier(Long id) {
//        if (supplierRepository.existsById(id)) {
//            supplierRepository.deleteById(id);
//            log.info("Deleted supplier with ID: {}", id);
//        } else {
//            log.warn("Attempted to delete non-existent supplier with ID: {}", id);
//        }
//    }
//}