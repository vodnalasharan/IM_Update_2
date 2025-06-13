// src/main/java/Stock_Inventory/repository/OrderItemRepository.java
package Stock_Inventory.repository;

import Stock_Inventory.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}