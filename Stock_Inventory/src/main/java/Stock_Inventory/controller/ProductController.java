// ProductController.java (Controller - No Change)
package Stock_Inventory.controller;

import Stock_Inventory.model.Product;
import Stock_Inventory.service.ProductService;
import Stock_Inventory.dto.ProductCreationRequest;
import Stock_Inventory.dto.QuantityUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody ProductCreationRequest request) {
        if (request.getInitialStockLevel() == null || request.getInitialStockLevel() < 0) {
            return ResponseEntity.badRequest().body(null);
        }
        if (request.getReorderLevel() == null || request.getReorderLevel() < 0) {
            request.setReorderLevel(5);
        }

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());

        Product savedProduct = productService.addOrUpdateProductAndStock(product, request.getInitialStockLevel(), request.getReorderLevel());
        if (savedProduct == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @PatchMapping("/{productId}/quantity")
    public ResponseEntity<Product> updateProductQuantity(@PathVariable Long productId, @RequestBody QuantityUpdateRequest request) {
        if (request.getQuantity() == null) {
            return ResponseEntity.badRequest().build();
        }
        Product updatedProduct = productService.updateProductAndStockQuantities(productId, request.getQuantity());
        return updatedProduct != null ? ResponseEntity.ok(updatedProduct) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product updatedProduct = productService.updateProductDetails(id, product);
        return updatedProduct != null ? ResponseEntity.ok(updatedProduct) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}



// Update -2
//// ProductController.java (Controller - Using DTOs from new package)
//package Stock_Inventory.controller;
//
//import Stock_Inventory.model.Product;
//import Stock_Inventory.service.ProductService;
//import Stock_Inventory.dto.ProductCreationRequest;
//import Stock_Inventory.dto.QuantityUpdateRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/products")
//public class ProductController {
//
//    @Autowired
//    private ProductService productService;
//
//    @GetMapping
//    public ResponseEntity<List<Product>> getAllProducts() {
//        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
//        Optional<Product> product = productService.getProductById(id);
//        return product.map(ResponseEntity::ok)
//                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }
//
//    @PostMapping
//    public ResponseEntity<Product> addProduct(@RequestBody ProductCreationRequest request) {
//        if (request.getInitialQuantity() == null || request.getInitialQuantity() < 0) {
//            return ResponseEntity.badRequest().body(null);
//        }
//        if (request.getReorderLevel() == null || request.getReorderLevel() < 0) {
//            request.setReorderLevel(5); // Default reorder level
//        }
//
//        Product product = new Product();
//        product.setName(request.getName());
//        product.setDescription(request.getDescription());
//        product.setPrice(request.getPrice());
//
//        Product savedProduct = productService.addOrUpdateProductAndStock(product, request.getInitialQuantity(), request.getReorderLevel());
//        if (savedProduct == null) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
//    }
//
//    // PATCH endpoint to update product quantity (e.g., from supplier receipts or manual adjustments)
//    @PatchMapping("/{productId}/quantity")
//    public ResponseEntity<Product> updateProductQuantity(@PathVariable Long productId, @RequestBody QuantityUpdateRequest request) {
//        if (request.getQuantity() == null) {
//            return ResponseEntity.badRequest().build();
//        }
//        // The 'quantity' in the request body is the *change* in quantity (positive for add, negative for subtract).
//        Product updatedProduct = productService.updateProductQuantity(productId, request.getQuantity());
//        return updatedProduct != null ? ResponseEntity.ok(updatedProduct) : ResponseEntity.notFound().build();
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
//        // This PUT endpoint only updates product details, not stock.
//        Product updatedProduct = productService.updateProduct(id, product);
//        return updatedProduct != null ? ResponseEntity.ok(updatedProduct) : ResponseEntity.notFound().build();
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
//        Optional<Product> product = productService.getProductById(id);
//        if (product.isPresent()) {
//            productService.deleteProduct(id);
//            return ResponseEntity.noContent().build();
//        }
//        return ResponseEntity.notFound().build();
//    }
//}



//package Stock_Inventory.controller;
//
//import Stock_Inventory.model.Product;
//import Stock_Inventory.service.ProductService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/products")
//public class ProductController {
//
//    @Autowired
//    private ProductService productService;
//
//    @GetMapping
//    public ResponseEntity<List<Product>> getAllProducts() {
//        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
//        Optional<Product> product = productService.getProductById(id);
//        return product.map(ResponseEntity::ok)
//                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }
//
//    @PostMapping
//    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
//        // Validate stock details if present
//        if (product.getStock() != null &&
//                (product.getStock().getQuantity() == null || product.getStock().getReorderLevel() == null)) {
//            return ResponseEntity.badRequest().body(null);
//        }
//
//        Integer quantity = (product.getStock() != null) ? product.getStock().getQuantity() : null;
//        Integer reorderLevel = (product.getStock() != null) ? product.getStock().getReorderLevel() : null;
//
//        Product savedProduct = productService.addOrUpdateProductAndStock(product, quantity, reorderLevel);
//        if (savedProduct == null) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Should not happen with current logic
//        }
//        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
//    }
//
//    // New endpoint to update only the quantity of an existing product
//    @PatchMapping("/{productId}/quantity")
//    public ResponseEntity<Product> updateProductQuantity(@PathVariable Long productId, @RequestBody QuantityUpdate request) {
//        if (request.getQuantity() == null) {
//            return ResponseEntity.badRequest().build();
//        }
//        Product updatedProduct = productService.updateProductQuantity(productId, request.getQuantity());
//        return updatedProduct != null ? ResponseEntity.ok(updatedProduct) : ResponseEntity.notFound().build();
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
//        Product updatedProduct = productService.updateProduct(id, product);
//        return updatedProduct != null ? ResponseEntity.ok(updatedProduct) : ResponseEntity.notFound().build();
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
//        Optional<Product> product = productService.getProductById(id);
//        if (product.isPresent()) {
//            productService.deleteProduct(id);
//            return ResponseEntity.noContent().build();
//        }
//        return ResponseEntity.notFound().build();
//    }
//
//    // Helper class for updating quantity
//    static class QuantityUpdate {
//        private Integer quantity;
//
//        public Integer getQuantity() {
//            return quantity;
//        }
//
//        public void setQuantity(Integer quantity) {
//            this.quantity = quantity;
//        }
//    }
//}
