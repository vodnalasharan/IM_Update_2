// Product.java (Model - No Change from last iteration)
package Stock_Inventory.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false, unique = true)
    private String name;
    private String description;
    private Double price;

    @Column(nullable = false)
    private Integer stockLevel;
}


// Update -2
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
//@Table(name = "product")
//public class Product {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long productId;
//
//    @Column(nullable = false, unique = true) // Ensures product names are unique
//    private String name;
//    private String description;
//    private Double price;
//}

// Method for Product and Stock only(Update 1)
//package Stock_Inventory.model;
//
//import jakarta.persistence.*;
//import com.fasterxml.jackson.annotation.JsonManagedReference;
//import lombok.*;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "product")
//public class Product {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long productId;
//
//    private String name;
//    private String description;
//    private Double price;
//    
//    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
//    @JsonManagedReference
//    private Stock stock;
//}
