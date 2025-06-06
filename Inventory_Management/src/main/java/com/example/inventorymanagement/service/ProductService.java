package com.example.inventorymanagement.service;

import com.example.inventorymanagement.exception.ResourceNotFoundException;
import com.example.inventorymanagement.model.Product;
import com.example.inventorymanagement.model.Stock; // Import Stock
import com.example.inventorymanagement.repository.ProductRepository;
import com.example.inventorymanagement.repository.StockRepository; // Import StockRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import Transactional
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository; // Inject StockRepository

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }

    @Transactional // Ensures both save operations are atomic
    public Product addProduct(Product product) {
        // Save the product first to get the generated ID
        Product savedProduct = productRepository.save(product);

        // Create a corresponding Stock entry for the new product
        // Set initial stock quantity from product's stockLevel and a default reorder level
        Stock stock = new Stock(savedProduct, savedProduct.getStockLevel(), 10); // Default reorder level of 10
        stockRepository.save(stock);

        return savedProduct;
    }

    @Transactional
    public Product updateProduct(Long id, Product productDetails) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        existingProduct.setName(productDetails.getName());
        existingProduct.setDescription(productDetails.getDescription());
        existingProduct.setPrice(productDetails.getPrice());
        existingProduct.setStockLevel(productDetails.getStockLevel()); // Update stockLevel in Product

        // Also update the stock quantity in the Stock table if stockLevel changes
        stockRepository.findById(id).ifPresent(stock -> {
            stock.setQuantity(productDetails.getStockLevel());
            stockRepository.save(stock);
        });

        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product", "id", id);
        }
        productRepository.deleteById(id);
        // Optionally, delete the corresponding stock entry if product is deleted
        stockRepository.deleteById(id);
    }
}

//
//import com.example.inventorymanagement.exception.ResourceNotFoundException;
//import com.example.inventorymanagement.model.Product;
//import com.example.inventorymanagement.model.Stock;
//import com.example.inventorymanagement.repository.ProductRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional; // For transactional operations
//import java.util.List;
//import java.util.Optional;
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@Slf4j
//public class ProductService {
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private StockService stockService; // Inject StockService
//
//    public List<Product> getAllProducts() {
//        return productRepository.findAll();
//    }
//
//    public Optional<Product> getProductById(Long id) {
//        return productRepository.findById(id);
//    }
//
//    @Transactional
//    public Product addProduct(Product product) {
//        // Save the product first to get an ID if it's new
//        Product savedProduct = productRepository.save(product);
//
//        // Check if a stock entry already exists for this product ID
//        Optional<Stock> existingStock = stockService.getStockByProductId(savedProduct.getProductId());
//
//        if (existingStock.isPresent()) {
//            // If stock exists, update its quantity by adding the product's initialStockLevel
//            Stock stockToUpdate = existingStock.get();
//            stockToUpdate.setQuantity(stockToUpdate.getQuantity() + product.getInitialStockLevel());
//            stockService.updateStock(stockToUpdate);
//            log.info("Updated stock for existing product: {}", savedProduct.getProductId());
//        } else {
//            // If no stock entry, create a new one
//            Stock newStock = new Stock();
//            newStock.setProduct(savedProduct); // Link the product
//            newStock.setQuantity(product.getInitialStockLevel());
//            newStock.setReorderLevel(10); // Default reorder level, adjust as needed
//            stockService.addStock(newStock); // Add the new stock (which will also save it)
//            log.info("Created new stock entry for product: {}", savedProduct.getProductId());
//        }
//
//        // Set the stock object back to the product for the response
//        // Fetching it again ensures the product object has the latest stock state
//        savedProduct.setStock(stockService.getStockByProductId(savedProduct.getProductId()).orElse(null));
//        return savedProduct;
//    }
//
//
//    @Transactional
//    public Product updateProduct(Long id, Product productDetails) {
//        Product existingProduct = productRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
//
//        // Update product basic details
//        existingProduct.setName(productDetails.getName());
//        existingProduct.setDescription(productDetails.getDescription());
//        existingProduct.setPrice(productDetails.getPrice());
//
//        // Handle initialStockLevel change, which implies a stock adjustment
//        if (productDetails.getInitialStockLevel() != null) {
//            // This assumes initialStockLevel in PUT request means "set stock to this value"
//            // or "add this amount to stock". Given the context, we'll assume it's an adjustment.
//            // If you want to set the absolute stock, adjust this logic.
//            Integer oldStockLevel = existingProduct.getInitialStockLevel(); // This was the initial quantity for the product
//            Integer newProvidedStockLevel = productDetails.getInitialStockLevel(); // This is the new amount provided in the update request
//
//            // Save the product details first
//            existingProduct.setInitialStockLevel(newProvidedStockLevel); // Update the product's own stock level field
//            Product updatedProduct = productRepository.save(existingProduct);
//
//            // Now update the associated Stock entry
//            Optional<Stock> optionalStock = stockService.getStockByProductId(updatedProduct.getProductId());
//            if (optionalStock.isPresent()) {
//                Stock stock = optionalStock.get();
//                // We're adjusting the stock quantity by the difference between the new provided value
//                // and what was previously associated with the product's "initial stock".
//                // A more robust system might have separate API calls for "adjust stock" vs "update product details".
//                // For this interim, we'll just set the stock quantity to the new initialStockLevel from the product.
//                stock.setQuantity(newProvidedStockLevel); // Setting the stock quantity directly
//                stockService.updateStock(stock);
//                log.info("Updated stock quantity for product {} to {}. Original product initial stock: {}", id, newProvidedStockLevel, oldStockLevel);
//            } else {
//                // This scenario should ideally not happen if addProduct works correctly
//                log.warn("No stock entry found for product ID: {}. Creating a new one with the updated stock level.", updatedProduct.getProductId());
//                Stock newStock = new Stock();
//                newStock.setProduct(updatedProduct);
//                newStock.setQuantity(updatedProduct.getInitialStockLevel());
//                newStock.setReorderLevel(10); // Default reorder level
//                stockService.addStock(newStock);
//            }
//            updatedProduct.setStock(stockService.getStockByProductId(updatedProduct.getProductId()).orElse(null));
//            return updatedProduct;
//
//        } else {
//            // If initialStockLevel is not provided in the update, just save product details
//            Product updatedProduct = productRepository.save(existingProduct);
//            updatedProduct.setStock(stockService.getStockByProductId(updatedProduct.getProductId()).orElse(null));
//            return updatedProduct;
//        }
//    }
//
//    @Transactional
//    public void deleteProduct(Long id) {
//        productRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
//        // Due to CascadeType.ALL and orphanRemoval=true on Product's @OneToOne mapping,
//        // deleting the Product will automatically delete the associated Stock entry.
//        productRepository.deleteById(id);
//        log.info("Deleted product and its associated stock entry for product ID: {}", id);
//    }
//}