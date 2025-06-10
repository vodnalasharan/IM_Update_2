// src/main/java/Stock_Inventory/repository/SupplierRepository.java
package Stock_Inventory.repository;

import Stock_Inventory.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}