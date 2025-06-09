// StockCreationRequest.java (DTO - No Change)
package Stock_Inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockCreationRequest {
    private Long productId;
    private Integer quantity;
    private Integer reorderLevel;
}

//// StockCreationRequest.java (DTO)
//package Stock_Inventory.dto;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class StockCreationRequest {
//    private Integer quantity;
//    private Integer reorderLevel;
//}