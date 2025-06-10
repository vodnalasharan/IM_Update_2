// src/main/java/Stock_Inventory/repository/OrderRepository.java
package Stock_Inventory.repository;

import Stock_Inventory.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}