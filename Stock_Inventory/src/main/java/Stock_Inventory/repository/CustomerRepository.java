// src/main/java/Stock_Inventory/repository/CustomerRepository.java
package Stock_Inventory.repository;

import Stock_Inventory.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}