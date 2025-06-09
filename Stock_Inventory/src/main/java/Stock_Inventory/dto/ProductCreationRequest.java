// ProductCreationRequest.java (DTO - No Change)
package Stock_Inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreationRequest {
    private String name;
    private String description;
    private Double price;
    private Integer initialStockLevel;
    private Integer reorderLevel;
}

//// ProductCreationRequest.java (DTO)
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
//public class ProductCreationRequest {
//    private String name;
//    private String description;
//    private Double price;
//    private Integer initialQuantity; // Used to initialize stock for a new product
//    private Integer reorderLevel;
//}