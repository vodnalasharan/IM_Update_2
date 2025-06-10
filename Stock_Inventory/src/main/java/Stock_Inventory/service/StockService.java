// src/main/java/Stock_Inventory/service/StockService.java
package Stock_Inventory.service;

import Stock_Inventory.dto.StockAddRequest;
import Stock_Inventory.dto.StockUpdateRequest;
import Stock_Inventory.dto.StockResponseDTO; // Ensure this is imported
import Stock_Inventory.model.Product;
import Stock_Inventory.model.Stock;
import Stock_Inventory.repository.ProductRepository;
import Stock_Inventory.repository.StockRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductRepository productRepository;

    // Helper method to map Stock entity to StockResponseDTO
    private StockResponseDTO convertToDto(Stock stock) {
        StockResponseDTO dto = new StockResponseDTO();
        dto.setStockId(stock.getStockId());
        dto.setQuantity(stock.getQuantity());
        dto.setReorderLevel(stock.getReorderLevel());
        if (stock.getProduct() != null) {
            dto.setProductId(stock.getProduct().getProductId());
        }
        return dto;
    }

    // Now returns StockResponseDTO
    public StockResponseDTO createStock(StockAddRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + request.getProductId()));

        if (stockRepository.findByProduct_ProductId(request.getProductId()).isPresent()) {
            throw new IllegalArgumentException("Stock entry already exists for product ID: " + request.getProductId() + ". Use update operations instead.");
        }

        Stock stock = new Stock();
        stock.setProduct(product);
        stock.setQuantity(request.getQuantity());
        stock.setReorderLevel(request.getReorderLevel());
        Stock savedStock = stockRepository.save(stock); // Save the entity
        return convertToDto(savedStock); // Convert to DTO before returning
    }

    public List<StockResponseDTO> getAllStocks() {
        return stockRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<StockResponseDTO> getStockById(Long id) {
        return stockRepository.findById(id).map(this::convertToDto);
    }

    public Optional<StockResponseDTO> getStockByProductId(Long productId) {
        return stockRepository.findByProduct_ProductId(productId).map(this::convertToDto);
    }

    // Now returns StockResponseDTO
    public StockResponseDTO updateStock(Long id, StockUpdateRequest request) {
        return stockRepository.findById(id).map(stock -> {
            if (!stock.getProduct().getProductId().equals(request.getProductId())) {
                throw new IllegalArgumentException("Product ID in request (" + request.getProductId() + ") does not match product ID of stock entry (" + stock.getProduct().getProductId() + ").");
            }
            stock.setQuantity(request.getQuantity());
            stock.setReorderLevel(request.getReorderLevel());
            Stock updatedStock = stockRepository.save(stock); // Save the entity
            return convertToDto(updatedStock); // Convert to DTO before returning
        }).orElseThrow(() -> new EntityNotFoundException("Stock entry not found with id " + id));
    }

    // Now returns StockResponseDTO
    @Transactional
    public StockResponseDTO updateStockQuantity(Long productId, Integer newQuantity) {
        Stock stock = stockRepository.findByProduct_ProductId(productId)
                .orElseThrow(() -> new EntityNotFoundException("Stock not found for product ID: " + productId));
        stock.setQuantity(newQuantity);
        Stock updatedStock = stockRepository.save(stock); // Save the entity
        return convertToDto(updatedStock); // Convert to DTO before returning
    }

    // Now returns StockResponseDTO
    @Transactional
    public StockResponseDTO updateReorderLevel(Long productId, Integer newReorderLevel) {
        Stock stock = stockRepository.findByProduct_ProductId(productId)
                .orElseThrow(() -> new EntityNotFoundException("Stock not found for product ID: " + productId));
        stock.setReorderLevel(newReorderLevel);
        Stock updatedStock = stockRepository.save(stock); // Save the entity
        return convertToDto(updatedStock); // Convert to DTO before returning
    }

    @Transactional
    public void deleteStock(Long id) {
        if (!stockRepository.existsById(id)) {
            throw new EntityNotFoundException("Stock entry not found with id " + id);
        }
        stockRepository.deleteById(id);
    }
}




//// src/main/java/Stock_Inventory/service/StockService.java
//package Stock_Inventory.service;
//
//import Stock_Inventory.dto.StockAddRequest;
//import Stock_Inventory.dto.StockUpdateRequest;
//import Stock_Inventory.model.Product;
//import Stock_Inventory.model.Stock;
//import Stock_Inventory.repository.ProductRepository;
//import Stock_Inventory.repository.StockRepository;
//import jakarta.persistence.EntityNotFoundException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@Transactional // Apply @Transactional at the class level for all public methods
//public class StockService {
//
//    @Autowired
//    private StockRepository stockRepository;
//
//    @Autowired
//    private ProductRepository productRepository; // Needed to link stock to product
//
//    /**
//     * Creates a new stock entry for a given product.
//     * This is intended for explicit stock creation, if a product exists without a stock entry.
//     * Note: Initial stock for a brand new product is typically handled by ProductService.createProduct.
//     *
//     * @param request DTO containing productId, quantity, and reorderLevel.
//     * @return The newly created Stock entity.
//     * @throws EntityNotFoundException if the product specified by productId does not exist.
//     * @throws IllegalArgumentException if a stock entry for the product already exists.
//     */
//    public Stock createStock(StockAddRequest request) {
//        Product product = productRepository.findById(request.getProductId())
//                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + request.getProductId()));
//
//        // Check if stock entry already exists for this product to prevent duplicates
//        if (stockRepository.findByProduct_ProductId(request.getProductId()).isPresent()) {
//            throw new IllegalArgumentException("Stock entry already exists for product ID: " + request.getProductId() + ". Use update operations instead.");
//        }
//
//        Stock stock = new Stock();
//        stock.setProduct(product);
//        stock.setQuantity(request.getQuantity());
//        stock.setReorderLevel(request.getReorderLevel());
//        return stockRepository.save(stock);
//    }
//
//    /**
//     * Retrieves all stock entries.
//     * @return A list of all Stock entities.
//     */
//    public List<Stock> getAllStocks() {
//        return stockRepository.findAll();
//    }
//
//    /**
//     * Retrieves a stock entry by its unique stock ID.
//     * @param id The ID of the stock entry.
//     * @return An Optional containing the Stock if found, empty otherwise.
//     */
//    public Optional<Stock> getStockById(Long id) {
//        return stockRepository.findById(id);
//    }
//
//    /**
//     * Retrieves a stock entry by the associated product's ID.
//     * @param productId The ID of the product.
//     * @return An Optional containing the Stock if found, empty otherwise.
//     */
//    public Optional<Stock> getStockByProductId(Long productId) {
//        return stockRepository.findByProduct_ProductId(productId);
//    }
//
//    /**
//     * Updates an existing stock entry by its stock ID.
//     * This is typically used for full replacement of stock details.
//     *
//     * @param id The ID of the stock entry to update.
//     * @param request DTO containing updated quantity and reorder level.
//     * @return The updated Stock entity.
//     * @throws EntityNotFoundException if the stock entry is not found.
//     * @throws IllegalArgumentException if the product ID in the request does not match the existing stock entry's product.
//     */
//    public Stock updateStock(Long id, StockUpdateRequest request) {
//        return stockRepository.findById(id).map(stock -> {
//            // Ensure the product ID being updated matches the existing stock entry's product
//            if (!stock.getProduct().getProductId().equals(request.getProductId())) {
//                throw new IllegalArgumentException("Product ID in request (" + request.getProductId() + ") does not match product ID of stock entry (" + stock.getProduct().getProductId() + ").");
//            }
//            stock.setQuantity(request.getQuantity());
//            stock.setReorderLevel(request.getReorderLevel());
//            return stockRepository.save(stock);
//        }).orElseThrow(() -> new EntityNotFoundException("Stock entry not found with id " + id));
//    }
//
//    /**
//     * Updates only the quantity of a stock entry for a given product.
//     *
//     * @param productId The ID of the product whose stock quantity is to be updated.
//     * @param newQuantity The new quantity for the stock.
//     * @return The updated Stock entity.
//     * @throws EntityNotFoundException if the stock entry for the product is not found.
//     */
//    @Transactional
//    public Stock updateStockQuantity(Long productId, Integer newQuantity) {
//        Stock stock = stockRepository.findByProduct_ProductId(productId)
//                .orElseThrow(() -> new EntityNotFoundException("Stock not found for product ID: " + productId));
//        stock.setQuantity(newQuantity);
//        return stockRepository.save(stock);
//    }
//
//    /**
//     * Updates only the reorder level of a stock entry for a given product.
//     *
//     * @param productId The ID of the product whose reorder level is to be updated.
//     * @param newReorderLevel The new reorder level.
//     * @return The updated Stock entity.
//     * @throws EntityNotFoundException if the stock entry for the product is not found.
//     */
//    @Transactional
//    public Stock updateReorderLevel(Long productId, Integer newReorderLevel) {
//        Stock stock = stockRepository.findByProduct_ProductId(productId)
//                .orElseThrow(() -> new EntityNotFoundException("Stock not found for product ID: " + productId));
//        stock.setReorderLevel(newReorderLevel);
//        return stockRepository.save(stock);
//    }
//
//    /**
//     * Deletes a stock entry by its ID.
//     * @param id The ID of the stock entry to delete.
//     * @throws EntityNotFoundException if the stock entry is not found.
//     */
//    @Transactional
//    public void deleteStock(Long id) {
//        if (!stockRepository.existsById(id)) {
//            throw new EntityNotFoundException("Stock entry not found with id " + id);
//        }
//        stockRepository.deleteById(id);
//    }
//}




//// src/main/java/Stock_Inventory/service/StockService.java
//package Stock_Inventory.service;
//
//import Stock_Inventory.dto.StockAddRequest;
//import Stock_Inventory.dto.StockUpdateRequest;
//import Stock_Inventory.model.Product;
//import Stock_Inventory.model.Stock;
//import Stock_Inventory.repository.ProductRepository;
//import Stock_Inventory.repository.StockRepository;
//import jakarta.transaction.Transactional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class StockService {
//
//    @Autowired
//    private StockRepository stockRepository;
//
//    @Autowired
//    private ProductRepository productRepository; // Needed to link stock to product
//
//    @Transactional
//    public Stock createStock(StockAddRequest request) {
//        Product product = productRepository.findById(request.getProductId())
//                .orElseThrow(() -> new RuntimeException("Product not found with id: " + request.getProductId()));
//
//        // Check if stock entry already exists for this product
//        if (stockRepository.findByProduct_ProductId(request.getProductId()).isPresent()) {
//            throw new IllegalArgumentException("Stock entry already exists for product ID: " + request.getProductId() + ". Use update instead.");
//        }
//
//        Stock stock = new Stock();
//        stock.setProduct(product);
//        stock.setQuantity(request.getQuantity());
//        stock.setReorderLevel(request.getReorderLevel());
//        return stockRepository.save(stock);
//    }
//
//    public List<Stock> getAllStocks() {
//        return stockRepository.findAll();
//    }
//
//    public Optional<Stock> getStockById(Long id) {
//        return stockRepository.findById(id);
//    }
//
//    @Transactional
//    public Stock updateStock(Long id, StockUpdateRequest request) {
//        return stockRepository.findById(id).map(stock -> {
//            // Ensure the product ID being updated matches
//            if (!stock.getProduct().getProductId().equals(request.getProductId())) {
//                throw new IllegalArgumentException("Product ID in request does not match product ID of stock entry.");
//            }
//            stock.setQuantity(request.getQuantity());
//            stock.setReorderLevel(request.getReorderLevel());
//            return stockRepository.save(stock);
//        }).orElseThrow(() -> new RuntimeException("Stock entry not found with id " + id));
//    }
//
//    @Transactional
//    public void deleteStock(Long id) {
//        stockRepository.deleteById(id);
//    }
//}