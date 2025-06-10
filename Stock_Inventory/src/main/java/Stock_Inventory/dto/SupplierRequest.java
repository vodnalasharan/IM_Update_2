// src/main/java/Stock_Inventory/dto/SupplierRequest.java
package Stock_Inventory.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplierRequest {
    @NotBlank(message = "Supplier name cannot be blank")
    private String name;

    private String contactInfo; // Can be blank

    private String productsSupplied; // Can be blank, text field
}