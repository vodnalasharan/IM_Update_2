// OrderCreationRequest.java (DTO - No Change)
package Stock_Inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreationRequest {
    private Long customerId;
    private List<OrderItemRequest> orderItems;
}
//// OrderCreationRequest.java (DTO)
//package Stock_Inventory.dto;
//
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
//public class OrderCreationRequest {
//    private Long customerId;
//    private List<OrderItemRequest> orderItems; // Represents the multiple products in an order
//}