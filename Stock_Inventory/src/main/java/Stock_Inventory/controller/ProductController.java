// src/main/java/Stock_Inventory/controller/ProductController.java
package Stock_Inventory.controller;

import Stock_Inventory.dto.ProductCreateRequest;
import Stock_Inventory.dto.ProductUpdateRequest;
import Stock_Inventory.dto.ProductQuantityUpdateRequest;
import Stock_Inventory.model.Product;
import Stock_Inventory.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * Creates a new product and initializes its stock entry.
     * @param request DTO containing product details and initial stock level.
     * @return ResponseEntity with the created Product and HTTP status 201.
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductCreateRequest request) {
        Product product = productService.createProduct(request);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    /**
     * Retrieves a list of all products.
     * @return ResponseEntity with a list of Products and HTTP status 200.
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * Retrieves a product by its ID.
     * @param id The ID of the product to retrieve.
     * @return ResponseEntity with the Product and HTTP status 200, or 404 if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Updates an existing product's details.
     * @param id The ID of the product to update.
     * @param request DTO containing updated product details.
     * @return ResponseEntity with the updated Product and HTTP status 200, or 404 if not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductUpdateRequest request) {
        try {
            Product updatedProduct = productService.updateProduct(id, request);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Updates the quantity (stock level) of a product.
     * @param id The ID of the product to update.
     * @param request DTO containing the quantity change.
     * @return ResponseEntity with the updated Product and HTTP status 200, 400 for bad request, or 404 if not found.
     */
    @PatchMapping("/{id}/quantity")
    public ResponseEntity<Product> updateProductQuantity(@PathVariable Long id, @Valid @RequestBody ProductQuantityUpdateRequest request) {
        try {
            Product updatedProduct = productService.updateProductQuantity(id, request);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            // e.g., insufficient stock leading to negative quantity
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Deletes a product by its ID.
     * @param id The ID of the product to delete.
     * @return ResponseEntity with HTTP status 204 (No Content) on success, or 404 if not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}


//// src/main/java/Stock_Inventory/controller/ProductController.java
//package Stock_Inventory.controller;
//
//import Stock_Inventory.dto.ProductCreateRequest;
//import Stock_Inventory.dto.ProductUpdateRequest;
//import Stock_Inventory.dto.ProductQuantityUpdateRequest;
//import Stock_Inventory.model.Product;
//import Stock_Inventory.service.ProductService;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/products")
//public class ProductController {
//
//    @Autowired
//    private ProductService productService;
//
//    @PostMapping
//    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductCreateRequest request) {
//        Product product = productService.createProduct(request);
//        return new ResponseEntity<>(product, HttpStatus.CREATED);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<Product>> getAllProducts() {
//        List<Product> products = productService.getAllProducts();
//        return new ResponseEntity<>(products, HttpStatus.OK);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
//        return productService.getProductById(id)
//                .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
//                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductUpdateRequest request) {
//        try {
//            Product updatedProduct = productService.updateProduct(id, request);
//            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//
//    @PatchMapping("/{id}/quantity")
//    public ResponseEntity<Product> updateProductQuantity(@PathVariable Long id, @Valid @RequestBody ProductQuantityUpdateRequest request) {
//        try {
//            Product updatedProduct = productService.updateProductQuantity(id, request);
//            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
//        try {
//            productService.deleteProduct(id);
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//}