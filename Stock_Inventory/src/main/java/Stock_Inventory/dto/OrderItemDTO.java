// src/main/java/Stock_Inventory/dto/OrderItemDTO.java
package Stock_Inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    @NotNull(message = "Product ID is required for an order item")
    @Min(value = 1, message = "Product ID must be positive")
    private Long productId;

    @NotNull(message = "Quantity is required for an order item")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}