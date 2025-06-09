// StockService.java (Service - No Change from last iteration)
package Stock_Inventory.service;

import Stock_Inventory.model.Product;
import Stock_Inventory.model.Stock;
import Stock_Inventory.repository.ProductRepository;
import Stock_Inventory.repository.StockRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class StockService {

    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private ProductRepository productRepository;

    public Optional<Stock> getStockByProductId(Long productId) {
        return productRepository.findById(productId)
                .flatMap(product -> stockRepository.findByProduct(product));
    }

    public List<Stock> getAllStockEntries() {
        return stockRepository.findAll();
    }

    @Transactional
    public Stock updateStockEntry(Long productId, Integer newQuantity, Integer newReorderLevel) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            log.warn("Product with ID {} not found for stock update.", productId);
            return null;
        }
        Product product = productOptional.get();

        Optional<Stock> stockOpt = stockRepository.findByProduct(product);
        if (stockOpt.isPresent()) {
            Stock stock = stockOpt.get();
            
            if (newQuantity != null) {
                if (newQuantity < 0) {
                    log.warn("Attempted to set negative quantity for product ID {}. New quantity: {}", productId, newQuantity);
                    return null;
                }
                stock.setQuantity(newQuantity);
                product.setStockLevel(newQuantity);
                productRepository.save(product);
                log.info("Synchronized Product.stockLevel for product ID {} during Stock.quantity update.", productId);
            }
            
            if (newReorderLevel != null) {
                if (newReorderLevel < 0) {
                    log.warn("Attempted to set negative reorder level for product ID {}. New level: {}", productId, newReorderLevel);
                    return null;
                }
                stock.setReorderLevel(newReorderLevel);
            }

            log.info("Updated stock entry for product ID {}. New quantity: {}, New reorder level: {}", productId, stock.getQuantity(), stock.getReorderLevel());
            return stockRepository.save(stock);
        }
        log.warn("Stock entry not found for product ID {}. Cannot update.", productId);
        return null;
    }

    @Transactional
    public Stock addStockEntry(Long productId, Integer quantity, Integer reorderLevel) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            log.warn("Product with ID {} not found for creating new stock entry.", productId);
            return null;
        }
        Product product = productOptional.get();

        Optional<Stock> existingStock = stockRepository.findByProduct(product);
        if (existingStock.isPresent()) {
            log.warn("Stock entry already exists for product ID {}. Use updateStockEntry instead.", productId);
            return null;
        }
        
        if (quantity == null || quantity < 0 || reorderLevel == null || reorderLevel < 0) {
             return null;
        }

        Stock stock = new Stock(null, product, quantity, reorderLevel);
        product.setStockLevel(quantity);
        productRepository.save(product);

        log.info("Added new stock entry for product ID {} with quantity: {} and reorder level: {}", productId, quantity, reorderLevel);
        return stockRepository.save(stock);
    }

    @Transactional
    public void deleteStockByProductId(Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            stockRepository.findByProduct(product).ifPresent(stockRepository::delete);
            log.info("Deleted stock entry for product ID: {}", productId);
        } else {
            log.warn("Attempted to delete stock entry for non-existent product with ID: {}", productId);
        }
    }
}

// Update -2
//// StockService.java (Service)
//package Stock_Inventory.service;
//
//import Stock_Inventory.model.Stock;
//import Stock_Inventory.repository.StockRepository;
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
//public class StockService {
//
//    @Autowired
//    private StockRepository stockRepository;
//
//    // Retrieve stock by product ID
//    public Optional<Stock> getStockByProductId(Long productId) {
//        return stockRepository.findByProductId(productId);
//    }
//
//    // Retrieve all stock entries
//    public List<Stock> getAllStockEntries() {
//        return stockRepository.findAll();
//    }
//
//    // Update the absolute stock quantity for a product
//    @Transactional
//    public Stock updateStockQuantity(Long productId, Integer newStockLevel) {
//        Optional<Stock> stockOpt = stockRepository.findByProductId(productId);
//        if (stockOpt.isPresent()) {
//            Stock stock = stockOpt.get();
//            if (newStockLevel < 0) {
//                log.warn("Attempted to set negative stock level for product ID {}. New level: {}", productId, newStockLevel);
//                return null; // Or throw an exception like InvalidQuantityException
//            }
//            stock.setQuantity(newStockLevel);
//            log.info("Updated stock quantity for product ID {}. New quantity: {}", productId, newStockLevel);
//            return stockRepository.save(stock);
//        }
//        log.warn("Stock not found for product ID {}. Cannot update quantity.", productId);
//        return null; // Stock not found
//    }
//
//    // Add a new stock entry for a product. This should primarily be used during initial product creation
//    // or if stock was somehow missing for an existing product. Not for daily adjustments.
//    @Transactional
//    public Stock addStock(Long productId, Integer quantity, Integer reorderLevel) {
//        Optional<Stock> existingStock = stockRepository.findByProductId(productId);
//        if (existingStock.isPresent()) {
//            log.warn("Stock already exists for product ID {}. Use updateProductQuantity in ProductService for changes.", productId);
//            return null; // Don't create a new stock entry if one already exists
//        }
//        Stock stock = new Stock();
//        stock.setProductId(productId);
//        stock.setQuantity(quantity);
//        stock.setReorderLevel(reorderLevel);
//        log.info("Added new stock for product ID {} with quantity: {}", productId, quantity);
//        return stockRepository.save(stock);
//    }
//
//    // Delete stock entry for a product
//    @Transactional
//    public void deleteStockByProductId(Long productId) {
//        Optional<Stock> stockOptional = stockRepository.findByProductId(productId);
//        if (stockOptional.isPresent()) {
//            stockRepository.delete(stockOptional.get());
//            log.info("Deleted stock for product ID: {}", productId);
//        } else {
//            log.warn("Attempted to delete non-existent stock for product ID: {}", productId);
//        }
//    }
//}


//package Stock_Inventory.service;
//
//import Stock_Inventory.model.Stock;
//import Stock_Inventory.model.Product;
//import Stock_Inventory.repository.StockRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@Slf4j
//public class StockService {
//
//    @Autowired
//    private StockRepository stockRepository;
//
//    public Optional<Stock> getStockByProductId(Long productId) {
//        return stockRepository.findById(productId);
//    }
//
//    public List<Stock> getAllStockEntries() {
//        return stockRepository.findAll();
//    }
//
//    public Stock updateStockByProduct(Product product, Integer newStockLevel) {
//        Optional<Stock> stockOpt = stockRepository.findByProduct(product);
//        if (stockOpt.isPresent()) {
//            Stock stock = stockOpt.get();
//            stock.setQuantity(newStockLevel);
//            return stockRepository.save(stock);
//        }
//        return null;
//    }
//
//    public Stock addStock(Product product, Integer quantity, Integer reorderLevel) {
//        Stock stock = new Stock();
//        stock.setProduct(product);
//        stock.setQuantity(quantity);
//        stock.setReorderLevel(reorderLevel);
//        return stockRepository.save(stock);
//    }
//}