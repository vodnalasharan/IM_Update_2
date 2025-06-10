// src/main/java/Stock_Inventory/model/Product.java
package Stock_Inventory.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product")
@Data // Includes @Getter, @Setter, @RequiredArgsConstructor, @ToString, @EqualsAndHashCode
@NoArgsConstructor // Adds a no-argument constructor
@AllArgsConstructor // Adds a constructor with all fields
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotBlank(message = "Product name is required")
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;

    // This field is for convenience in DTOs/responses and is not a persistent column in the DB.
    // The actual stock quantity is stored in the Stock entity.
    @Transient
    private Integer stockLevel;
}



//// src/main/java/Stock_Inventory/model/Product.java
//package Stock_Inventory.model;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import com.fasterxml.jackson.annotation.JsonIdentityInfo;
//import com.fasterxml.jackson.annotation.ObjectIdGenerators;
//
//@Entity
//@Table(name = "product")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "productId")
//public class Product {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long productId;
//
//    @Column(nullable = false)
//    private String name;
//
//    @Column(columnDefinition = "TEXT")
//    private String description;
//
//    @Column(nullable = false)
//    private Double price;
//
//    @Transient // Stock level is managed by the Stock entity but displayed with Product
//    private Integer stockLevel;
//}