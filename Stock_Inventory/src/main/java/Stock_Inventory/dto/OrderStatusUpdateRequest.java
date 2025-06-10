package Stock_Inventory.dto;

// Import the top-level OrderStatus enum
import Stock_Inventory.model.OrderStatus; // <--- IMPORTANT: Changed this import

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusUpdateRequest {
    @NotNull(message = "Order status cannot be null")
    private OrderStatus status; // <--- Changed this to reference the top-level enum
}


//// src/main/java/Stock_Inventory/dto/OrderStatusUpdateRequest.java
//package Stock_Inventory.dto;
//
//import Stock_Inventory.model.Order;
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
//public class OrderStatusUpdateRequest {
//    @NotNull(message = "Order status cannot be null")
//    private Order.OrderStatus status;
//}