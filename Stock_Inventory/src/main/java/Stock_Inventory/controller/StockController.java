// StockController.java (Controller - No Change)
package Stock_Inventory.controller;

import Stock_Inventory.model.Stock;
import Stock_Inventory.service.StockService;
import Stock_Inventory.dto.StockUpdateRequest;
import Stock_Inventory.dto.StockCreationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/{productId}")
    public ResponseEntity<Stock> getStockByProductId(@PathVariable Long productId) {
        Optional<Stock> stock = stockService.getStockByProductId(productId);
        return stock.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Stock>> getAllStockEntries() {
        return ResponseEntity.ok(stockService.getAllStockEntries());
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Stock> updateStock(@PathVariable Long productId, @RequestBody StockUpdateRequest request) {
        Stock updatedStock = stockService.updateStockEntry(productId, request.getQuantity(), request.getReorderLevel());
        return updatedStock != null ? ResponseEntity.ok(updatedStock) : ResponseEntity.notFound().build();
    }

    @PostMapping("/{productId}")
    public ResponseEntity<Stock> addStockForProduct(@PathVariable Long productId, @RequestBody StockCreationRequest request) {
        if (request.getQuantity() == null || request.getQuantity() < 0 || request.getReorderLevel() == null || request.getReorderLevel() < 0) {
            return ResponseEntity.badRequest().build();
        }
        Stock newStock = stockService.addStockEntry(productId, request.getQuantity(), request.getReorderLevel());
        if (newStock == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return new ResponseEntity<>(newStock, HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long productId) {
        Optional<Stock> stock = stockService.getStockByProductId(productId);
        if (stock.isPresent()) {
            stockService.deleteStockByProductId(productId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}


// Update -2
//// StockController.java (Controller - Using DTOs from new package)
//package Stock_Inventory.controller;
//
//import Stock_Inventory.model.Stock;
//import Stock_Inventory.service.StockService;
//import Stock_Inventory.dto.StockUpdateRequest;
//import Stock_Inventory.dto.StockCreationRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
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
//        Optional<Stock> stock = stockService.getStockByProductId(productId);
//        return stock.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    @GetMapping
//    public ResponseEntity<List<Stock>> getAllStockEntries() {
//        return ResponseEntity.ok(stockService.getAllStockEntries());
//    }
//
//    // PUT endpoint to update the absolute stock quantity and reorder level
//    @PutMapping("/{productId}")
//    public ResponseEntity<Stock> updateStock(@PathVariable Long productId, @RequestBody StockUpdateRequest request) {
//        if (request.getQuantity() == null || request.getQuantity() < 0) {
//            return ResponseEntity.badRequest().build();
//        }
//        // This updates the quantity to a specific new level.
//        Stock updatedStock = stockService.updateStockQuantity(productId, request.getQuantity());
//        // If you also want to update reorder level via this endpoint, you'd add logic here
//        // stockService.updateReorderLevel(productId, request.getReorderLevel());
//        return updatedStock != null ? ResponseEntity.ok(updatedStock) : ResponseEntity.notFound().build();
//    }
//
//    // POST endpoint to add a *new* stock entry for a product (should typically only happen once per product)
//    @PostMapping("/{productId}")
//    public ResponseEntity<Stock> addStockForProduct(@PathVariable Long productId, @RequestBody StockCreationRequest request) {
//        if (request.getQuantity() == null || request.getQuantity() < 0) {
//            return ResponseEntity.badRequest().build();
//        }
//        if (request.getReorderLevel() == null || request.getReorderLevel() < 0) {
//            request.setReorderLevel(5); // Default reorder level
//        }
//
//        Stock newStock = stockService.addStock(productId, request.getQuantity(), request.getReorderLevel());
//        if (newStock == null) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // Indicates stock already exists for this product
//        }
//        return new ResponseEntity<>(newStock, HttpStatus.CREATED);
//    }
//
//    @DeleteMapping("/{productId}")
//    public ResponseEntity<Void> deleteStock(@PathVariable Long productId) {
//        Optional<Stock> stock = stockService.getStockByProductId(productId);
//        if (stock.isPresent()) {
//            stockService.deleteStockByProductId(productId);
//            return ResponseEntity.noContent().build();
//        }
//        return ResponseEntity.notFound().build();
//    }
//}


//package Stock_Inventory.controller;
//
//import Stock_Inventory.model.Stock;
//import Stock_Inventory.model.Product;
//import Stock_Inventory.service.StockService;
//import Stock_Inventory.service.ProductService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/stock")
//public class StockController {
//
//    @Autowired
//    private StockService stockService;
//
//    @Autowired
//    private ProductService productService;
//
//    @GetMapping("/{productId}")
//    public ResponseEntity<Stock> getStockByProductId(@PathVariable Long productId) {
//        Optional<Stock> stock = stockService.getStockByProductId(productId);
//        return stock.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    @GetMapping
//    public ResponseEntity<List<Stock>> getAllStockEntries() {
//        return ResponseEntity.ok(stockService.getAllStockEntries());
//    }
//
//    @PutMapping("/{productId}")
//    public ResponseEntity<Stock> updateStock(@PathVariable Long productId, @RequestBody Stock stock) {
//        Optional<Product> product = productService.getProductById(productId);
//        return product.map(p -> ResponseEntity.ok(stockService.updateStockByProduct(p, stock.getQuantity())))
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    @PostMapping("/{productId}")
//    public ResponseEntity<Stock> addStock(@PathVariable Long productId, @RequestBody Stock stock) {
//        Optional<Product> product = productService.getProductById(productId);
//        return product.map(p -> ResponseEntity.ok(stockService.addStock(p, stock.getQuantity(), stock.getReorderLevel())))
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//}