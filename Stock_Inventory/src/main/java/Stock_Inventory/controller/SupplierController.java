// SupplierController.java (Controller - No Change)
package Stock_Inventory.controller;

import Stock_Inventory.model.Supplier;
import Stock_Inventory.service.SupplierService;
import Stock_Inventory.dto.SupplierRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping
    public ResponseEntity<List<Supplier>> getAllSuppliers() {
        return ResponseEntity.ok(supplierService.getAllSuppliers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Supplier> getSupplierById(@PathVariable Long id) {
        Optional<Supplier> supplier = supplierService.getSupplierById(id);
        return supplier.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Supplier> addSupplier(@RequestBody SupplierRequest request) {
        Supplier supplier = new Supplier();
        supplier.setName(request.getName());
        supplier.setContactInfo(request.getContactInfo());
        supplier.setProductsSupplied(request.getProductsSupplied());

        Supplier addedSupplier = supplierService.addSupplier(supplier);
        if (addedSupplier == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return new ResponseEntity<>(addedSupplier, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Supplier> updateSupplier(@PathVariable Long id, @RequestBody SupplierRequest request) {
        Supplier supplierDetails = new Supplier();
        supplierDetails.setName(request.getName());
        supplierDetails.setContactInfo(request.getContactInfo());
        supplierDetails.setProductsSupplied(request.getProductsSupplied());

        Supplier updatedSupplier = supplierService.updateSupplier(id, supplierDetails);
        return updatedSupplier != null ? ResponseEntity.ok(updatedSupplier) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        Optional<Supplier> supplier = supplierService.getSupplierById(id);
        if (supplier.isPresent()) {
            supplierService.deleteSupplier(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

//// SupplierController.java (Controller - Using DTOs from new package)
//package Stock_Inventory.controller;
//
//import Stock_Inventory.model.Supplier;
//import Stock_Inventory.service.SupplierService;
//import Stock_Inventory.dto.SupplierRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/suppliers")
//public class SupplierController {
//
//    @Autowired
//    private SupplierService supplierService;
//
//    @GetMapping
//    public ResponseEntity<List<Supplier>> getAllSuppliers() {
//        return ResponseEntity.ok(supplierService.getAllSuppliers());
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Supplier> getSupplierById(@PathVariable Long id) {
//        Optional<Supplier> supplier = supplierService.getSupplierById(id);
//        return supplier.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    @PostMapping
//    public ResponseEntity<Supplier> addSupplier(@RequestBody SupplierRequest request) {
//        Supplier supplier = new Supplier();
//        supplier.setName(request.getName());
//        supplier.setContactInfo(request.getContactInfo());
//        supplier.setProductsSupplied(request.getProductsSupplied());
//
//        Supplier addedSupplier = supplierService.addSupplier(supplier);
//        if (addedSupplier == null) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // Supplier with name already exists
//        }
//        return new ResponseEntity<>(addedSupplier, HttpStatus.CREATED);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Supplier> updateSupplier(@PathVariable Long id, @RequestBody SupplierRequest request) {
//        Supplier supplierDetails = new Supplier();
//        supplierDetails.setName(request.getName());
//        supplierDetails.setContactInfo(request.getContactInfo());
//        supplierDetails.setProductsSupplied(request.getProductsSupplied());
//
//        Supplier updatedSupplier = supplierService.updateSupplier(id, supplierDetails);
//        return updatedSupplier != null ? ResponseEntity.ok(updatedSupplier) : ResponseEntity.notFound().build();
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
//        Optional<Supplier> supplier = supplierService.getSupplierById(id);
//        if (supplier.isPresent()) {
//            supplierService.deleteSupplier(id);
//            return ResponseEntity.noContent().build();
//        }
//        return ResponseEntity.notFound().build();
//    }
//}