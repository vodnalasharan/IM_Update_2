// src/main/java/Stock_Inventory/model/Stock.java
package Stock_Inventory.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stock")
@Data // Includes @Getter, @Setter, @RequiredArgsConstructor, @ToString, @EqualsAndHashCode
@NoArgsConstructor // Adds a no-argument constructor
@AllArgsConstructor // Adds a constructor with all fields
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", unique = true, nullable = false)
    @NotNull(message = "Product is required for stock")
    private Product product;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    @NotNull(message = "Reorder level cannot be null")
    @Min(value = 0, message = "Reorder level cannot be negative")
    private Integer reorderLevel;
}


//// src/main/java/Stock_Inventory/model/Stock.java
//package Stock_Inventory.model;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import com.fasterxml.jackson.annotation.JsonIdentityInfo;
//import com.fasterxml.jackson.annotation.ObjectIdGenerators;
//import com.fasterxml.jackson.annotation.JsonIdentityReference;
//
//@Entity
//@Table(name = "stock")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "stockId")
//public class Stock {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long stockId; // Added a separate ID for Stock entity
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "product_id", unique = true, nullable = false) // Ensures one stock entry per product
//    @JsonIdentityReference(alwaysAsId = true) // Serialize Product as ID to avoid recursion
//    private Product product;
//
//    @Column(nullable = false)
//    private Integer quantity;
//
//    @Column(nullable = false)
//    private Integer reorderLevel;
//}