// StockRepository.java
package Stock_Inventory.repository;

import Stock_Inventory.model.Product;
import Stock_Inventory.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    // THIS IS THE CORRECT METHOD for finding Stock by a Product object.
    // Spring Data JPA correctly maps this to the 'product' field in Stock and uses Product's ID.
    Optional<Stock> findByProduct(Product product);
    // DO NOT ADD: Optional<Stock> findByProductId(Product product); // This causes the error!
}


//package Stock_Inventory.repository;
//
//import Stock_Inventory.model.Stock;
//import Stock_Inventory.model.Product;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//@Repository
//public interface StockRepository extends JpaRepository<Stock, Long> {
//    Optional<Stock> findByProduct(Product product);
//}