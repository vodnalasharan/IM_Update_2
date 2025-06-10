// src/main/java/Stock_Inventory/dto/ProductQuantityUpdateRequest.java
package Stock_Inventory.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductQuantityUpdateRequest {
    @NotNull(message = "Quantity change cannot be null")
    private Integer quantityChange; // Can be positive (add) or negative (sell)
}



//// src/main/java/Stock_Inventory/dto/ProductQuantityUpdateRequest.java
//package Stock_Inventory.dto;
//
//import jakarta.validation.constraints.NotNull;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class ProductQuantityUpdateRequest {
//    @NotNull(message = "Quantity change cannot be null")
//    private Integer quantityChange; // Can be positive (add) or negative (sell)
//}