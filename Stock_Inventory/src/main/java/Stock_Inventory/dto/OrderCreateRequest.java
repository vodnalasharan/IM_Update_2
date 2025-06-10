package Stock_Inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data; // Provides getters and setters
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List; // For the list of order items

@Data // This Lombok annotation generates getters, setters, toString, equals, hashCode
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequest {
    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Order must have at least one item")
    @Size(min = 1, message = "Order must have at least one item")
    private List<OrderItemRequest> orderItems; // <--- This needs to be a List, and @Data will generate getOrderItems()

    @Data // Nested DTO for individual items
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemRequest {
        @NotNull(message = "Product ID is required for order item")
        private Long productId;

        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;
    }
}


//// src/main/java/Stock_Inventory/dto/OrderCreateRequest.java
//package Stock_Inventory.dto;
//
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.Valid;
//import jakarta.validation.constraints.Size;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import java.util.List;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class OrderCreateRequest {
//    @NotNull(message = "Customer ID cannot be null")
//    private Long customerId;
//
//    @Valid
//    @NotNull(message = "Order items cannot be null")
//    @Size(min = 1, message = "Order must contain at least one item")
//    private List<OrderItemRequest> orderItems;
//}