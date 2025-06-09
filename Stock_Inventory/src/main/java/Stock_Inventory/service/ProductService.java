// ProductService.java (Service - No Change from last iteration)
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
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

    private static final int DEFAULT_REORDER_LEVEL = 5;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    public Product addOrUpdateProductAndStock(Product product, Integer initialStockLevel, Integer reorderLevel) {
        Optional<Product> existingProductOptional = productRepository.findByName(product.getName());

        if (existingProductOptional.isPresent()) {
            Product existingProduct = existingProductOptional.get();
            Integer newStockLevel = existingProduct.getStockLevel() + (initialStockLevel != null ? initialStockLevel : 0);
            
            existingProduct.setStockLevel(newStockLevel);
            Product savedProduct = productRepository.save(existingProduct);

            Optional<Stock> existingStockEntry = stockRepository.findByProduct(existingProduct);
            if (existingStockEntry.isPresent()) {
                Stock stockToUpdate = existingStockEntry.get();
                stockToUpdate.setQuantity(newStockLevel);
                if (reorderLevel != null) {
                    stockToUpdate.setReorderLevel(reorderLevel);
                }
                stockRepository.save(stockToUpdate);
                log.info("Updated stock level for existing product '{}' (ID: {}). New stockLevel: {}, New Stock.quantity: {}",
                         existingProduct.getName(), existingProduct.getProductId(), existingProduct.getStockLevel(), stockToUpdate.getQuantity());
            } else {
                Stock newStock = new Stock(null, savedProduct, newStockLevel, reorderLevel != null ? reorderLevel : DEFAULT_REORDER_LEVEL);
                stockRepository.save(newStock);
                log.warn("Existing product '{}' (ID: {}) found without a stock entry. Created new stock entry for it.", existingProduct.getName(), existingProduct.getProductId());
            }

            return savedProduct;
        } else {
            product.setStockLevel(initialStockLevel != null ? initialStockLevel : 0);
            Product savedProduct = productRepository.save(product);

            Stock newStock = new Stock(null, savedProduct, savedProduct.getStockLevel(), reorderLevel != null ? reorderLevel : DEFAULT_REORDER_LEVEL);
            stockRepository.save(newStock);

            log.info("Added new product '{}' (ID: {}) with initial stockLevel: {}. Created associated Stock entry with quantity: {}",
                     savedProduct.getName(), savedProduct.getProductId(), savedProduct.getStockLevel(), newStock.getQuantity());
            return savedProduct;
        }
    }

    @Transactional
    public Product updateProductDetails(Long id, Product productDetails) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(productDetails.getName());
                    existingProduct.setDescription(productDetails.getDescription());
                    existingProduct.setPrice(productDetails.getPrice());

                    if (productDetails.getStockLevel() != null) {
                        existingProduct.setStockLevel(productDetails.getStockLevel());
                        stockRepository.findByProduct(existingProduct).ifPresent(stock -> {
                            stock.setQuantity(productDetails.getStockLevel());
                            stockRepository.save(stock);
                            log.info("Synchronized Stock.quantity for product ID {} during product details update.", id);
                        });
                    }
                    log.info("Updated product details for ID: {}", id);
                    return productRepository.save(existingProduct);
                })
                .orElse(null);
    }

    @Transactional
    public Product updateProductAndStockQuantities(Long productId, Integer quantityChange) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            log.warn("Product with ID {} not found for quantity update.", productId);
            return null;
        }

        Product product = productOptional.get();
        Optional<Stock> stockOptional = stockRepository.findByProduct(product);
        if (stockOptional.isEmpty()) {
            log.warn("Stock entry not found for product ID {}. Cannot update quantity.", productId);
            return null;
        }
        Stock stock = stockOptional.get();

        Integer newStockLevel = product.getStockLevel() + quantityChange;
        if (newStockLevel < 0) {
            log.warn("Attempted to set negative stock level for product ID {}. Current: {}, Change: {}", productId, product.getStockLevel(), quantityChange);
            return null;
        }

        product.setStockLevel(newStockLevel);
        productRepository.save(product);

        stock.setQuantity(newStockLevel);
        stockRepository.save(stock);

        log.info("Updated stock level for product ID {}. Product.stockLevel: {}, Stock.quantity: {}",
                 productId, product.getStockLevel(), stock.getQuantity());
        return product;
    }

    @Transactional
    public void deleteProduct(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            stockRepository.findByProduct(product).ifPresent(stockRepository::delete);
            productRepository.delete(product);
            log.info("Deleted product with ID: {} and its associated stock entry.", id);
        } else {
            log.warn("Attempted to delete non-existent product with ID: {}", id);
        }
    }

	public Product updateProductQuantity(Long productId, int i) {
		// TODO Auto-generated method stub
		return null;
	}
}

// Update -2
// ProductService.java (Service)
//package Stock_Inventory.service;
//
//import Stock_Inventory.model.Product;
//import Stock_Inventory.model.Stock;
//import Stock_Inventory.repository.ProductRepository;
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
//public class ProductService {
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private StockRepository stockRepository;
//
//    private static final int DEFAULT_REORDER_LEVEL = 5;
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
//    public Product addOrUpdateProductAndStock(Product product, Integer initialQuantity, Integer reorderLevel) {
//        Optional<Product> existingProductOptional = productRepository.findByName(product.getName());
//
//        if (existingProductOptional.isPresent()) {
//            // Product already exists, update its stock quantity, do NOT create a new product
//            Product existingProduct = existingProductOptional.get();
//            Optional<Stock> existingStockOptional = stockRepository.findByProductId(existingProduct.getProductId());
//
//            if (existingStockOptional.isPresent()) {
//                Stock existingStock = existingStockOptional.get();
//                // Add to existing quantity for duplicate product name
//                existingStock.setQuantity(existingStock.getQuantity() + (initialQuantity != null ? initialQuantity : 0));
//                stockRepository.save(existingStock);
//                log.info("Updated quantity for existing product '{}' (ID: {}). New quantity: {}", existingProduct.getName(), existingProduct.getProductId(), existingStock.getQuantity());
//            } else {
//                // Edge case: product exists but has no stock. Create new stock for it.
//                Stock newStock = new Stock();
//                newStock.setProductId(existingProduct.getProductId());
//                newStock.setQuantity(initialQuantity != null ? initialQuantity : 0);
//                newStock.setReorderLevel(reorderLevel != null ? reorderLevel : DEFAULT_REORDER_LEVEL);
//                stockRepository.save(newStock);
//                log.warn("Existing product '{}' (ID: {}) found without stock. Created new stock for it.", existingProduct.getName(), existingProduct.getProductId());
//            }
//            return existingProduct; // Return the existing product
//        } else {
//            // Product does not exist, create a new product and its associated stock
//            Product savedProduct = productRepository.save(product); // Save product first to get its ID
//
//            Stock newStock = new Stock();
//            newStock.setProductId(savedProduct.getProductId()); // Link stock to the newly saved product
//            newStock.setQuantity(initialQuantity != null ? initialQuantity : 0);
//            newStock.setReorderLevel(reorderLevel != null ? reorderLevel : DEFAULT_REORDER_LEVEL);
//            stockRepository.save(newStock); // Save the new stock
//
//            log.info("Added new product '{}' (ID: {}) with initial quantity: {}", savedProduct.getName(), savedProduct.getProductId(), newStock.getQuantity());
//            return savedProduct;
//        }
//    }
//
//    @Transactional
//    public Product updateProduct(Long id, Product product) {
//        return productRepository.findById(id)
//                .map(existingProduct -> {
//                    // Update only product details (name, description, price)
//                    existingProduct.setName(product.getName());
//                    existingProduct.setDescription(product.getDescription());
//                    existingProduct.setPrice(product.getPrice());
//                    log.info("Updated product details for ID: {}", id);
//                    return productRepository.save(existingProduct);
//                })
//                .orElse(null); // Product not found
//    }
//
//    // This method handles quantity changes from various sources (orders, supplier receipts, manual adjustments)
//    @Transactional
//    public Product updateProductQuantity(Long productId, Integer quantityChange) {
//        Optional<Product> productOptional = productRepository.findById(productId);
//        if (productOptional.isPresent()) {
//            Product product = productOptional.get();
//            Optional<Stock> stockOptional = stockRepository.findByProductId(productId);
//
//            if (stockOptional.isPresent()) {
//                Stock stock = stockOptional.get();
//                // Check if decrement would result in negative stock
//                if (stock.getQuantity() + quantityChange < 0) {
//                    log.warn("Attempted to set negative quantity for product ID {}. Current: {}, Change: {}", productId, stock.getQuantity(), quantityChange);
//                    // Instead of null, you might throw a custom exception like InsufficientStockException
//                    return null;
//                }
//                stock.setQuantity(stock.getQuantity() + quantityChange); // Apply the change
//                stockRepository.save(stock);
//                log.info("Updated quantity for product ID {}. New quantity: {}", productId, stock.getQuantity());
//                return product; // Return the product whose stock was updated
//            } else {
//                log.warn("Product ID {} found without stock. Cannot update quantity.", productId);
//                return null; // Or throw a custom exception if stock is expected
//            }
//        }
//        return null; // Product not found
//    }
//
//    @Transactional
//    public void deleteProduct(Long id) {
//        // Find product to get its ID to delete associated stock
//        Optional<Product> productOptional = productRepository.findById(id);
//        if (productOptional.isPresent()) {
//            Product product = productOptional.get();
//            // Delete associated stock first
//            stockRepository.findByProductId(product.getProductId()).ifPresent(stockRepository::delete);
//            // Then delete the product
//            productRepository.deleteById(id);
//            log.info("Deleted product with ID: {} and its associated stock.", id);
//        } else {
//            log.warn("Attempted to delete non-existent product with ID: {}", id);
//        }
//    }
//}



//package Stock_Inventory.service;
//
//import Stock_Inventory.model.Product;
//import Stock_Inventory.model.Stock;
//import Stock_Inventory.repository.ProductRepository;
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
//public class ProductService {
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private StockRepository stockRepository;
//
//    public List<Product> getAllProducts() {
//        return productRepository.findAll();
//    }
//    public Optional<Product> getProductById(Long id) {
//        return productRepository.findById(id);
//    }
//
//    @Transactional
//    public Product addOrUpdateProductAndStock(Product product, Integer quantity, Integer reorderLevel) {
//        Optional<Product> existingProductOptional = productRepository.findByName(product.getName());
//
//        if (existingProductOptional.isPresent()) {
//            // Product already exists, update its quantity
//            Product existingProduct = existingProductOptional.get();
//            Stock existingStock = existingProduct.getStock();
//
//            if (existingStock != null) {
//                // Update quantity if stock exists
//                existingStock.setQuantity(existingStock.getQuantity() + quantity); // Add to existing quantity
//                stockRepository.save(existingStock); // Save the updated stock explicitly
//                log.info("Updated quantity for existing product '{}'. New quantity: {}", existingProduct.getName(), existingStock.getQuantity());
//                // No need to call productRepository.save(existingProduct) here, as stock is cascaded and saved.
//                // The transaction will commit the changes to existingStock.
//            } else {
//                // This scenario means an existing product was found without an associated stock.
//                // This is less ideal, but we handle it by creating a new Stock and linking it.
//                Stock newStock = new Stock();
//                newStock.setProduct(existingProduct); // Link the new stock to the existing product
//                newStock.setQuantity(quantity != null ? quantity : 0);
//                newStock.setReorderLevel(reorderLevel != null ? reorderLevel : 5);
//                existingProduct.setStock(newStock); // Set the new stock on the existing product
//                // No need to call stockRepository.save(newStock) explicitly here because cascade will handle it
//                // when productRepository.save(existingProduct) is called below.
//                log.warn("Existing product '{}' found without stock. Created new stock for it.", existingProduct.getName());
//            }
//            // Always save the existing product to ensure all changes (including cascaded stock updates/creations) are persisted.
//            return productRepository.save(existingProduct); // Save the updated product (and its potentially new/updated stock)
//
//        } else {
//            // Product does not exist, create a new product and stock
//            if (product.getStock() == null) {
//                // If stock object is not provided in the incoming product, create a new one
//                Stock stock = new Stock();
//                stock.setProduct(product);
//                stock.setQuantity(quantity != null ? quantity : 0);
//                stock.setReorderLevel(reorderLevel != null ? reorderLevel : 5);
//                product.setStock(stock);
//            } else {
//                // If stock is already provided in the product object from the request
//                product.getStock().setProduct(product); // Ensure the bidirectional link is set
//
//                // Set quantity and reorderLevel if they were null in the incoming stock object
//                if (product.getStock().getQuantity() == null) {
//                    product.getStock().setQuantity(quantity != null ? quantity : 0);
//                }
//                if (product.getStock().getReorderLevel() == null) {
//                    product.getStock().setReorderLevel(reorderLevel != null ? reorderLevel : 5);
//                }
//
//                // IMPORTANT: For new product, ensure the incoming stock ID is null to prevent 'detached entity' error
//                if (product.getStock().getId() != null && product.getStock().getId() > 0) {
//                     log.warn("Received stock with ID for new product. Setting stock ID to null for persistence.");
//                     product.getStock().setId(null); // Explicitly nullify the ID for new creation
//                }
//            }
//            log.info("Adding new product '{}' with initial quantity: {}", product.getName(), product.getStock().getQuantity());
//            return productRepository.save(product); // This will cascade persist the new Product and its associated Stock
//        }
//    }
//
//    @Transactional
//    public Product updateProduct(Long id, Product product) {
//        return productRepository.findById(id)
//                .map(existingProduct -> {
//                    existingProduct.setName(product.getName());
//                    existingProduct.setDescription(product.getDescription());
//                    existingProduct.setPrice(product.getPrice());
//
//                    // Update stock details if provided in the product object
//                    if (product.getStock() != null) {
//                        Stock existingStock = existingProduct.getStock();
//                        if (existingStock != null) {
//                            if (product.getStock().getQuantity() != null) {
//                                existingStock.setQuantity(product.getStock().getQuantity());
//                            }
//                            if (product.getStock().getReorderLevel() != null) {
//                                existingStock.setReorderLevel(product.getStock().getReorderLevel());
//                            }
//                            stockRepository.save(existingStock); // Save the updated stock
//                        } else {
//                            // If product had no stock previously, create a new one
//                            Stock newStock = new Stock();
//                            newStock.setProduct(existingProduct);
//                            newStock.setQuantity(product.getStock().getQuantity() != null ? product.getStock().getQuantity() : 0);
//                            newStock.setReorderLevel(product.getStock().getReorderLevel() != null ? product.getStock().getReorderLevel() : 5);
//                            existingProduct.setStock(newStock);
//                            stockRepository.save(newStock);
//                        }
//                    }
//
//                    return productRepository.save(existingProduct);
//                })
//                .orElse(null);
//    }
//
//    // New method to update only the quantity of an existing product
//    @Transactional
//    public Product updateProductQuantity(Long productId, Integer quantityToAdd) {
//        Optional<Product> productOptional = productRepository.findById(productId);
//        if (productOptional.isPresent()) {
//            Product product = productOptional.get();
//            Stock stock = product.getStock();
//            if (stock != null) {
//                stock.setQuantity(stock.getQuantity() + quantityToAdd);
//                stockRepository.save(stock);
//                log.info("Updated quantity for product ID {}. New quantity: {}", productId, stock.getQuantity());
//                return product;
//            } else {
//                log.warn("Product ID {} found without stock. Cannot update quantity.", productId);
//                return null; // Or throw an exception
//            }
//        }
//        return null; // Product not found
//    }
//
//    public void deleteProduct(Long id) {
//        productRepository.deleteById(id);
//    }
//}