// src/main/java/Stock_Inventory/repository/ProductRepository.java
package Stock_Inventory.repository;

import Stock_Inventory.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}