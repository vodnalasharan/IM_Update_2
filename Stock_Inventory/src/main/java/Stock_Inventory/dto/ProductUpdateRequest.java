// src/main/java/Stock_Inventory/dto/ProductUpdateRequest.java
package Stock_Inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min; // Note: For price, it's @Positive. @Min applies to stock.
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateRequest {
    @NotBlank(message = "Product name cannot be blank")
    private String name;

    private String description;

    @NotNull(message = "Product price cannot be null")
    @Min(value = 0, message = "Product price must be non-negative") // Changed to @Min(0) to match Product's price validation if needed
    private Double price;
}


//// src/main/java/Stock_Inventory/dto/ProductUpdateRequest.java
//package Stock_Inventory.dto;
//
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Min;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class ProductUpdateRequest {
//    @NotBlank(message = "Product name cannot be blank")
//    private String name;
//
//    private String description;
//
//    @NotNull(message = "Product price cannot be null")
//    @Min(value = 0, message = "Product price must be non-negative")
//    private Double price;
//}