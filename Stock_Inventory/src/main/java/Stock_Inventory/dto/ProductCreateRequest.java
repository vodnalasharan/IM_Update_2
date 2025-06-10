// src/main/java/Stock_Inventory/dto/ProductCreateRequest.java
package Stock_Inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequest {
    @NotBlank(message = "Product name cannot be blank")
    private String name;

    private String description;

    @NotNull(message = "Product price cannot be null")
    @Positive(message = "Product price must be positive")
    private Double price;

    @NotNull(message = "Initial stock level is required")
    @PositiveOrZero(message = "Stock level cannot be negative")
    private Integer stockLevel;
}



//// src/main/java/Stock_Inventory/dto/ProductCreateRequest.java
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
//public class ProductCreateRequest {
//    @NotBlank(message = "Product name cannot be blank")
//    private String name;
//
//    private String description;
//
//    @NotNull(message = "Product price cannot be null")
//    @Min(value = 0, message = "Product price must be non-negative")
//    private Double price;
//}