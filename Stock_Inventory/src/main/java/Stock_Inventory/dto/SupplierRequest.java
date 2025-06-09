// SupplierRequest.java (DTO - No Change)
package Stock_Inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplierRequest {
    private String name;
    private String contactInfo;
    private String productsSupplied;
}


//// SupplierRequest.java (DTO)
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
//public class SupplierRequest {
//    private String name;
//    private String contactInfo;
//    private String productsSupplied; // As a string/TEXT field
//}