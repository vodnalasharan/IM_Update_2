// Update -3
// Stock.java (Model - UPDATED: Showing only productId in JSON)
package Stock_Inventory.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stock")
// Use JsonIdentityInfo to avoid infinite recursion and specify how THIS Stock object is serialized
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary Key for the Stock entity itself

    // Use ManyToOne for the relationship.
    // @JoinColumn specifies the foreign key column in the 'stock' table.
    // 'unique = true' ensures that only one stock entry exists per product.
    // FetchType.LAZY is good for performance.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", unique = true, nullable = false)
    // IMPORTANT: @JsonIdentityInfo and @JsonIdentityReference on the 'product' field itself
    // instruct Jackson to serialize the 'product' reference as just its 'productId'.
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "productId")
    @JsonIdentityReference(alwaysAsId = true) // This is the magic line that makes it show only the ID
    private Product product; // Foreign Key to Product

    @Column(nullable = false)
    private Integer quantity; // Quantity of the product in stock (will be synchronized with Product.stockLevel)

    @Column(nullable = false)
    private Integer reorderLevel;
}


//package Stock_Inventory.model;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "stock")
//public class Stock {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id; // Primary key for the Stock entity itself
//
//    @Column(name = "product_id", unique = true, nullable = false) // Foreign key to Product, unique to ensure one-to-one mapping
//    private Long productId;
//
//    @Column(nullable = false)
//    private Integer quantity;
//
//    @Column(nullable = false)
//    private Integer reorderLevel;
//}

// Update -1
//package Stock_Inventory.model;
//
//import org.springframework.stereotype.Component;
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import jakarta.persistence.*;
//import lombok.*;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "stock")
//public class Stock {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;  
//
//    @OneToOne
//    @JoinColumn(name = "product_id", referencedColumnName = "productId")
//    @JsonBackReference
//    private Product product;
//
//    @Column(nullable = false)
//    private Integer quantity;
//
//    @Column(nullable = false)
//    private Integer reorderLevel;
//}