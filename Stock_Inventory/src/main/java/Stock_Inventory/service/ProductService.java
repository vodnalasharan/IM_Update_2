// src/main/java/Stock_Inventory/service/ProductService.java
package Stock_Inventory.service;

import Stock_Inventory.dto.ProductCreateRequest;
import Stock_Inventory.dto.ProductUpdateRequest;
import Stock_Inventory.dto.ProductQuantityUpdateRequest;
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

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

    public Product createProduct(ProductCreateRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());

        Product savedProduct = productRepository.save(product);

        Stock stock = new Stock();
        stock.setProduct(savedProduct);
        stock.setQuantity(request.getStockLevel());
        stock.setReorderLevel(5); // Set default reorder level to 5 when product is created
        stockRepository.save(stock);

        savedProduct.setStockLevel(stock.getQuantity());
        return savedProduct;
    }

    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        products.forEach(product -> stockRepository.findByProduct_ProductId(product.getProductId())
                .ifPresent(stock -> product.setStockLevel(stock.getQuantity())));
        return products;
    }

    public Optional<Product> getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        product.ifPresent(p -> stockRepository.findByProduct_ProductId(p.getProductId())
                .ifPresent(stock -> p.setStockLevel(stock.getQuantity())));
        return product;
    }

    public Product updateProduct(Long id, ProductUpdateRequest request) {
        return productRepository.findById(id).map(product -> {
            product.setName(request.getName());
            product.setDescription(request.getDescription());
            product.setPrice(request.getPrice());

            Product updatedProduct = productRepository.save(product);
            stockRepository.findByProduct_ProductId(updatedProduct.getProductId())
                    .ifPresent(stock -> updatedProduct.setStockLevel(stock.getQuantity()));
            return updatedProduct;
        }).orElseThrow(() -> new EntityNotFoundException("Product not found with id " + id));
    }

    public Product updateProductQuantity(Long productId, ProductQuantityUpdateRequest request) {
        return productRepository.findById(productId).map(product -> {
            Stock stock = stockRepository.findByProduct_ProductId(productId)
                    .orElseThrow(() -> new EntityNotFoundException("Stock entry not found for product id " + productId));

            int newQuantity = stock.getQuantity() + request.getQuantityChange();
            if (newQuantity < 0) {
                throw new IllegalArgumentException("Stock quantity cannot be negative for product: " + product.getName() + ". Attempted change: " + request.getQuantityChange() + ", Current stock: " + stock.getQuantity());
            }
            stock.setQuantity(newQuantity);
            stockRepository.save(stock);

            product.setStockLevel(newQuantity);
            return product;
        }).orElseThrow(() -> new EntityNotFoundException("Product not found with id " + productId));
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Product not found with id " + id);
        }
        stockRepository.findByProduct_ProductId(id).ifPresent(stockRepository::delete);
        productRepository.deleteById(id);
    }
}


//// src/main/java/Stock_Inventory/service/ProductService.java
//package Stock_Inventory.service;
//
//import Stock_Inventory.dto.ProductCreateRequest;
//import Stock_Inventory.dto.ProductUpdateRequest;
//import Stock_Inventory.dto.ProductQuantityUpdateRequest;
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
//public class ProductService {
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private StockRepository stockRepository;
//
//    /**
//     * Creates a new product and automatically initializes its stock entry.
//     *
//     * @param request The DTO containing product details and initial stock level.
//     * @return The newly created Product, with its transient stockLevel set for response.
//     */
//    public Product createProduct(ProductCreateRequest request) {
//        Product product = new Product();
//        product.setName(request.getName());
//        product.setDescription(request.getDescription());
//        product.setPrice(request.getPrice());
//
//        // Save Product first to get the generated productId
//        Product savedProduct = productRepository.save(product);
//
//        // Automatically create and save the initial Stock entry for this new product
//        Stock stock = new Stock();
//        stock.setProduct(savedProduct); // Link the stock to the newly saved product
//        stock.setQuantity(request.getStockLevel()); // Set initial quantity from request
//        stock.setReorderLevel(0); // Default reorder level to 0, can be updated later via StockController
//        stockRepository.save(stock);
//
//        // Set the transient stockLevel on the returned Product object for immediate client feedback
//        savedProduct.setStockLevel(stock.getQuantity());
//        return savedProduct;
//    }
//
//    /**
//     * Retrieves all products, populating their transient stockLevel from the Stock entity.
//     *
//     * @return A list of all products.
//     */
//    public List<Product> getAllProducts() {
//        List<Product> products = productRepository.findAll();
//        // For each product, fetch its stock and set the transient stockLevel
//        products.forEach(product -> stockRepository.findByProduct_ProductId(product.getProductId())
//                .ifPresent(stock -> product.setStockLevel(stock.getQuantity())));
//        return products;
//    }
//
//    /**
//     * Retrieves a single product by its ID, populating its transient stockLevel.
//     *
//     * @param id The ID of the product to retrieve.
//     * @return An Optional containing the Product if found, empty otherwise.
//     */
//    public Optional<Product> getProductById(Long id) {
//        Optional<Product> product = productRepository.findById(id);
//        // If product is found, fetch its stock and set the transient stockLevel
//        product.ifPresent(p -> stockRepository.findByProduct_ProductId(p.getProductId())
//                .ifPresent(stock -> p.setStockLevel(stock.getQuantity())));
//        return product;
//    }
//
//    /**
//     * Updates an existing product's details (name, description, price).
//     * Stock level and reorder level are managed via separate Stock endpoints.
//     *
//     * @param id The ID of the product to update.
//     * @param request The DTO containing updated product details.
//     * @return The updated Product entity.
//     * @throws EntityNotFoundException if the product is not found.
//     */
//    public Product updateProduct(Long id, ProductUpdateRequest request) {
//        return productRepository.findById(id).map(product -> {
//            product.setName(request.getName());
//            product.setDescription(request.getDescription());
//            product.setPrice(request.getPrice());
//
//            Product updatedProduct = productRepository.save(product);
//            // Ensure transient stockLevel is updated for the response object
//            stockRepository.findByProduct_ProductId(updatedProduct.getProductId())
//                    .ifPresent(stock -> updatedProduct.setStockLevel(stock.getQuantity()));
//            return updatedProduct;
//        }).orElseThrow(() -> new EntityNotFoundException("Product not found with id " + id));
//    }
//
//    /**
//     * Updates the quantity (stock level) of a product. This is a PATCH operation
//     * for quantity changes (e.g., sales, receipts).
//     *
//     * @param productId The ID of the product whose quantity is to be updated.
//     * @param request The DTO containing the quantity change (+/-).
//     * @return The updated Product entity with its new transient stockLevel.
//     * @throws IllegalArgumentException if the resulting stock quantity would be negative.
//     * @throws EntityNotFoundException if the product or its stock entry is not found.
//     */
//    public Product updateProductQuantity(Long productId, ProductQuantityUpdateRequest request) {
//        return productRepository.findById(productId).map(product -> {
//            Stock stock = stockRepository.findByProduct_ProductId(productId)
//                    .orElseThrow(() -> new EntityNotFoundException("Stock entry not found for product id " + productId));
//
//            int newQuantity = stock.getQuantity() + request.getQuantityChange();
//            if (newQuantity < 0) {
//                throw new IllegalArgumentException("Stock quantity cannot be negative for product: " + product.getName() + ". Attempted change: " + request.getQuantityChange() + ", Current stock: " + stock.getQuantity());
//            }
//            stock.setQuantity(newQuantity);
//            stockRepository.save(stock);
//
//            // Update the transient stockLevel of the product for the response
//            product.setStockLevel(newQuantity);
//            return product;
//        }).orElseThrow(() -> new EntityNotFoundException("Product not found with id " + productId));
//    }
//
//    /**
//     * Deletes a product by its ID. Also deletes the associated stock entry.
//     *
//     * @param id The ID of the product to delete.
//     * @throws EntityNotFoundException if the product is not found.
//     */
//    public void deleteProduct(Long id) {
//        if (!productRepository.existsById(id)) {
//            throw new EntityNotFoundException("Product not found with id " + id);
//        }
//        // First, delete the associated stock entry to maintain referential integrity
//        stockRepository.findByProduct_ProductId(id).ifPresent(stockRepository::delete);
//        // Then, delete the product itself
//        productRepository.deleteById(id);
//    }
//}



//// src/main/java/Stock_Inventory/service/ProductService.java
//package Stock_Inventory.service;
//
//import Stock_Inventory.dto.ProductCreateRequest;
//import Stock_Inventory.dto.ProductUpdateRequest;
//import Stock_Inventory.dto.ProductQuantityUpdateRequest;
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
//public class ProductService {
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private StockRepository stockRepository;
//
//    @Transactional
//    public Product createProduct(ProductCreateRequest request) {
//        Product product = new Product();
//        product.setName(request.getName());
//        product.setDescription(request.getDescription());
//        product.setPrice(request.getPrice());
//
//        Product savedProduct = productRepository.save(product);
//
//        // Initialize stock for the new product
//        Stock stock = new Stock();
//        stock.setProduct(savedProduct);
//        stock.setQuantity(0); // New products start with 0 stock
//        stock.setReorderLevel(0); // Default reorder level
//        stockRepository.save(stock);
//
//        savedProduct.setStockLevel(stock.getQuantity()); // Set initial stock level in product DTO
//        return savedProduct;
//    }
//
//    public List<Product> getAllProducts() {
//        List<Product> products = productRepository.findAll();
//        products.forEach(product -> stockRepository.findByProduct_ProductId(product.getProductId())
//                .ifPresent(stock -> product.setStockLevel(stock.getQuantity())));
//        return products;
//    }
//
//    public Optional<Product> getProductById(Long id) {
//        Optional<Product> product = productRepository.findById(id);
//        product.ifPresent(p -> stockRepository.findByProduct_ProductId(p.getProductId())
//                .ifPresent(stock -> p.setStockLevel(stock.getQuantity())));
//        return product;
//    }
//
//    @Transactional
//    public Product updateProduct(Long id, ProductUpdateRequest request) {
//        return productRepository.findById(id).map(product -> {
//            product.setName(request.getName());
//            product.setDescription(request.getDescription());
//            product.setPrice(request.getPrice());
//
//            Product updatedProduct = productRepository.save(product);
//            stockRepository.findByProduct_ProductId(updatedProduct.getProductId())
//                    .ifPresent(stock -> updatedProduct.setStockLevel(stock.getQuantity()));
//            return updatedProduct;
//        }).orElseThrow(() -> new RuntimeException("Product not found with id " + id));
//    }
//
//    @Transactional
//    public Product updateProductQuantity(Long productId, ProductQuantityUpdateRequest request) {
//        return productRepository.findById(productId).map(product -> {
//            Stock stock = stockRepository.findByProduct_ProductId(productId)
//                    .orElseThrow(() -> new RuntimeException("Stock entry not found for product id " + productId));
//
//            int newQuantity = stock.getQuantity() + request.getQuantityChange();
//            if (newQuantity < 0) {
//                throw new IllegalArgumentException("Stock quantity cannot be negative for product " + product.getName());
//            }
//            stock.setQuantity(newQuantity);
//            stockRepository.save(stock);
//
//            product.setStockLevel(newQuantity);
//            return product; // Return product with updated stock level
//        }).orElseThrow(() -> new RuntimeException("Product not found with id " + productId));
//    }
//
//    @Transactional
//    public void deleteProduct(Long id) {
//        // Before deleting a product, delete its associated stock entry
//        stockRepository.findByProduct_ProductId(id).ifPresent(stockRepository::delete);
//        productRepository.deleteById(id);
//    }
//}