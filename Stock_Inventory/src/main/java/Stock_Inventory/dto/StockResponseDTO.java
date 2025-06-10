// src/main/java/Stock_Inventory/dto/StockResponseDTO.java
package Stock_Inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockResponseDTO {
    private Long stockId;
    private Long productId; // Include product ID
    private Integer quantity;
    private Integer reorderLevel;
}