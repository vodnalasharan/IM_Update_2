// StockUpdateRequest.java (DTO - No Change)
package Stock_Inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockUpdateRequest {
    private Integer quantity;
    private Integer reorderLevel;
}

//// StockUpdateRequest.java (DTO)
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
//public class StockUpdateRequest {
//    private Integer quantity; // This is the new absolute quantity for stock
//    private Integer reorderLevel; // Can be updated here too
//}