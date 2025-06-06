package com.example.inventorymanagement.controller;


import com.example.inventorymanagement.model.Stock;
import com.example.inventorymanagement.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/{productId}")
    public ResponseEntity<Stock> getStockByProductId(@PathVariable Long productId) {
        Stock stock = stockService.getStockByProductId(productId); // Service now throws exception
        return new ResponseEntity<>(stock, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Stock> updateStock(@RequestBody Stock stock) {
        Stock updatedStock = stockService.updateStock(stock); // Service now throws exception if not found
        return new ResponseEntity<>(updatedStock, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Stock> addStock(@RequestBody Stock stock) {
        Stock savedStock = stockService.addStock(stock);
        return new ResponseEntity<>(savedStock, HttpStatus.CREATED);
    }
}

//import com.example.inventorymanagement.exception.ResourceNotFoundException;
//import com.example.inventorymanagement.model.Stock;
//import com.example.inventorymanagement.service.StockService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/stock")
//public class StockController {
//
//    @Autowired
//    private StockService stockService;
//
//    @GetMapping("/{productId}")
//    public ResponseEntity<Stock> getStockByProductId(@PathVariable Long productId) {
//        Stock stock = stockService.getStockByProductId(productId)
//                .orElseThrow(() -> new ResourceNotFoundException("Stock not found for product ID: " + productId));
//        return new ResponseEntity<>(stock, HttpStatus.OK);
//    }
//
//    @PostMapping // Use this to directly add/increase stock for an existing product
//    public ResponseEntity<Stock> addStock(@RequestBody Stock stock) {
//        Stock savedStock = stockService.addStock(stock);
//        return new ResponseEntity<>(savedStock, HttpStatus.CREATED);
//    }
//
//    @PutMapping // Use this to directly update a stock entry (e.g., set exact quantity or reorder level)
//    public ResponseEntity<Stock> updateStock(@RequestBody Stock stock) {
//        Stock updatedStock = stockService.updateStock(stock);
//        return new ResponseEntity<>(updatedStock, HttpStatus.OK);
//    }
//}