package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.model.Product;
import com.example.inventorymanagement.service.ProductService;
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

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id); // Service now throws exception
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product savedProduct = productService.addProduct(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(id, product); // Service now throws exception
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id); // Service now throws exception if not found
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
//import com.example.inventorymanagement.exception.ResourceNotFoundException;
//import com.example.inventorymanagement.model.Product;
//import com.example.inventorymanagement.service.ProductService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import java.util.List;
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
//        Product product = productService.getProductById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
//        return new ResponseEntity<>(product, HttpStatus.OK);
//    }
//
//    @PostMapping
//    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
//        Product savedProduct = productService.addProduct(product);
//        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
//        Product updatedProduct = productService.updateProduct(id, product);
//        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
//        productService.deleteProduct(id);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//}
