package Stock_Inventory.dto;

import Stock_Inventory.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private Long orderId;
    private Long customerId; // Only customerId
    private List<OrderItemResponseDTO> orderItems; // List of simplified OrderItem DTOs
    private LocalDateTime orderDate;
    private OrderStatus status;
    private Double totalAmount;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemResponseDTO {
        private Long id; // Corresponds to orderItemId
        private Long productId; // Only productId
        private Integer quantity;
        private Double priceAtOrder;
    }
}