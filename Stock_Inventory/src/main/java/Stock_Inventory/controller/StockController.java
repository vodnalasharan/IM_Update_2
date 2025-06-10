// src/main/java/Stock_Inventory/controller/StockController.java
package Stock_Inventory.controller;

import Stock_Inventory.dto.StockAddRequest;
import Stock_Inventory.dto.StockUpdateRequest;
import Stock_Inventory.dto.StockResponseDTO; // Ensure this is imported
import Stock_Inventory.service.StockService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@Validated
public class StockController {

    @Autowired
    private StockService stockService;

    // Now returns StockResponseDTO
    @PostMapping
    public ResponseEntity<StockResponseDTO> createStock(@Valid @RequestBody StockAddRequest request) {
        try {
            StockResponseDTO stockDTO = stockService.createStock(request);
            return new ResponseEntity<>(stockDTO, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<StockResponseDTO>> getAllStocks() {
        List<StockResponseDTO> stocks = stockService.getAllStocks();
        return new ResponseEntity<>(stocks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockResponseDTO> getStockById(@PathVariable Long id) {
        return stockService.getStockById(id)
                .map(stock -> new ResponseEntity<>(stock, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<StockResponseDTO> getStockByProductId(@PathVariable Long productId) {
        return stockService.getStockByProductId(productId)
                .map(stock -> new ResponseEntity<>(stock, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Now returns StockResponseDTO
//    @PutMapping("/{id}")
//    public ResponseEntity<StockResponseDTO> updateStock(@PathVariable Long id, @Valid @RequestBody StockUpdateRequest request) {
//        try {
//            StockResponseDTO updatedStockDTO = stockService.updateStock(id, request);
//            return new ResponseEntity<>(updatedStockDTO, HttpStatus.OK);
//        } catch (IllegalArgumentException e) {
//            // This is the line that's returning 400 BAD REQUEST
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        } catch (jakarta.persistence.EntityNotFoundException e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//    @PutMapping("/{id}")
//    public ResponseEntity<StockResponseDTO> updateStock(@PathVariable Long id, @Valid @RequestBody StockUpdateRequest request) {
//        try {
//            StockResponseDTO updatedStockDTO = stockService.updateStock(id, request);
//            return new ResponseEntity<>(updatedStockDTO, HttpStatus.OK);
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        } catch (jakarta.persistence.EntityNotFoundException e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }

    // Now returns StockResponseDTO
    @PutMapping("/product/{productId}/quantity")
    public ResponseEntity<StockResponseDTO> updateStockQuantity(
            @PathVariable Long productId,
            @RequestParam @Min(value = 0, message = "Quantity cannot be negative") Integer newQuantity) {
        try {
            StockResponseDTO updatedStockDTO = stockService.updateStockQuantity(productId, newQuantity);
            return ResponseEntity.ok(updatedStockDTO);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Now returns StockResponseDTO
    @PutMapping("/product/{productId}/reorder-level")
    public ResponseEntity<StockResponseDTO> updateReorderLevel(
            @PathVariable Long productId,
            @RequestParam @Min(value = 0, message = "Reorder level cannot be negative") Integer newReorderLevel) {
        try {
            StockResponseDTO updatedStockDTO = stockService.updateReorderLevel(productId, newReorderLevel);
            return ResponseEntity.ok(updatedStockDTO);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        try {
            stockService.deleteStock(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}


//// src/main/java/Stock_Inventory/controller/StockController.java
//package Stock_Inventory.controller;
//
//import Stock_Inventory.dto.StockAddRequest;
//import Stock_Inventory.dto.StockUpdateRequest;
//import Stock_Inventory.model.Stock;
//import Stock_Inventory.service.StockService;
//import jakarta.persistence.EntityNotFoundException;
//import jakarta.validation.Valid;
//import jakarta.validation.constraints.Min;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated; // Needed for @RequestParam validation
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/stocks")
//@Validated // Enable validation for request parameters (like @Min on newReorderLevel)
//public class StockController {
//
//    @Autowired
//    private StockService stockService;
//
//    /**
//     * Creates a new stock entry. This is for explicit stock creation if a product exists but lacks a stock entry.
//     * Typically, initial stock is handled via product creation.
//     * @param request DTO containing productId, quantity, reorderLevel.
//     * @return ResponseEntity with the created Stock and HTTP status 201.
//     */
//    @PostMapping
//    public ResponseEntity<Stock> createStock(@Valid @RequestBody StockAddRequest request) {
//        try {
//            Stock stock = stockService.createStock(request);
//            return new ResponseEntity<>(stock, HttpStatus.CREATED);
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>(null, HttpStatus.CONFLICT); // Stock entry already exists
//        } catch (jakarta.persistence.EntityNotFoundException e) {
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // Product not found
//        }
//    }
//
//    /**
//     * Retrieves a list of all stock entries.
//     * @return ResponseEntity with a list of Stocks and HTTP status 200.
//     */
//    @GetMapping
//    public ResponseEntity<List<Stock>> getAllStocks() {
//        List<Stock> stocks = stockService.getAllStocks();
//        return new ResponseEntity<>(stocks, HttpStatus.OK);
//    }
//
//    /**
//     * Retrieves a stock entry by its unique stock ID.
//     * @param id The ID of the stock entry.
//     * @return ResponseEntity with the Stock and HTTP status 200, or 404 if not found.
//     */
//    @GetMapping("/{id}")
//    public ResponseEntity<Stock> getStockById(@PathVariable Long id) {
//        return stockService.getStockById(id)
//                .map(stock -> new ResponseEntity<>(stock, HttpStatus.OK))
//                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }
//
//    /**
//     * Retrieves a stock entry by the associated product's ID.
//     * @param productId The ID of the product.
//     * @return ResponseEntity with the Stock and HTTP status 200, or 404 if not found.
//     */
//    @GetMapping("/product/{productId}")
//    public ResponseEntity<Stock> getStockByProductId(@PathVariable Long productId) {
//        return stockService.getStockByProductId(productId)
//                .map(stock -> new ResponseEntity<>(stock, HttpStatus.OK))
//                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }
//
//    /**
//     * Updates an existing stock entry by its stock ID.
//     * @param id The ID of the stock entry to update.
//     * @param request DTO containing updated quantity and reorder level.
//     * @return ResponseEntity with the updated Stock and HTTP status 200, 400 for bad request, or 404 if not found.
//     */
//    @PutMapping("/{id}")
//    public ResponseEntity<Stock> updateStock(@PathVariable Long id, @Valid @RequestBody StockUpdateRequest request) {
//        try {
//            Stock updatedStock = stockService.updateStock(id, request);
//            return new ResponseEntity<>(updatedStock, HttpStatus.OK);
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Product ID mismatch
//        } catch (jakarta.persistence.EntityNotFoundException e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//
//    /**
//     * Updates only the quantity of stock for a given product.
//     * @param productId The ID of the product.
//     * @param newQuantity The new quantity for the stock.
//     * @return ResponseEntity with the updated Stock and HTTP status 200, or 404 if not found.
//     */
//    @PutMapping("/product/{productId}/quantity")
//    public ResponseEntity<Stock> updateStockQuantity(
//            @PathVariable Long productId,
//            @RequestParam @Min(value = 0, message = "Quantity cannot be negative") Integer newQuantity) {
//        try {
//            Stock updatedStock = stockService.updateStockQuantity(productId, newQuantity);
//            return ResponseEntity.ok(updatedStock);
//        } catch (jakarta.persistence.EntityNotFoundException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    /**
//     * Updates only the reorder level for a given product.
//     * @param productId The ID of the product.
//     * @param newReorderLevel The new reorder level.
//     * @return ResponseEntity with the updated Stock and HTTP status 200, or 404 if not found.
//     */
//    @PutMapping("/product/{productId}/reorder-level")
//    public ResponseEntity<Stock> updateReorderLevel(
//            @PathVariable Long productId,
//            @RequestParam @Min(value = 0, message = "Reorder level cannot be negative") Integer newReorderLevel) {
//        try {
//            Stock updatedStock = stockService.updateReorderLevel(productId, newReorderLevel);
//            return ResponseEntity.ok(updatedStock);
//        } catch (jakarta.persistence.EntityNotFoundException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    /**
//     * Deletes a stock entry by its ID.
//     * @param id The ID of the stock entry to delete.
//     * @return ResponseEntity with HTTP status 204 (No Content) on success, or 404 if not found.
//     */
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
//        try {
//            stockService.deleteStock(id);
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        } catch (jakarta.persistence.EntityNotFoundException e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//}


//// src/main/java/Stock_Inventory/controller/StockController.java
//package Stock_Inventory.controller;
//
//import Stock_Inventory.dto.StockAddRequest;
//import Stock_Inventory.dto.StockUpdateRequest;
//import Stock_Inventory.model.Stock;
//import Stock_Inventory.service.StockService;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/stock")
//public class StockController {
//
//    @Autowired
//    private StockService stockService;
//
//    @PostMapping
//    public ResponseEntity<Stock> createStock(@Valid @RequestBody StockAddRequest request) {
//        try {
//            Stock stock = stockService.createStock(request);
//            return new ResponseEntity<>(stock, HttpStatus.CREATED);
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>(null, HttpStatus.CONFLICT); // Stock entry already exists
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // Product not found
//        }
//    }
//
//    @GetMapping
//    public ResponseEntity<List<Stock>> getAllStocks() {
//        return (ResponseEntity<List<Stock>>) stockService.getAllStocks();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Stock> getStockById(@PathVariable Long id) {
//        return stockService.getStockById(id)
//                .map(stock -> new ResponseEntity<>(stock, HttpStatus.OK))
//                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Stock> updateStock(@PathVariable Long id, @Valid @RequestBody StockUpdateRequest request) {
//        try {
//            Stock updatedStock = stockService.updateStock(id, request);
//            return new ResponseEntity<>(updatedStock, HttpStatus.OK);
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Product ID mismatch
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
//        try {
//            stockService.deleteStock(id);
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//}