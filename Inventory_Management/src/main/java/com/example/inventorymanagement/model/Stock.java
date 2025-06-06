package com.example.inventorymanagement.model;



import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Stock {

    @Id
    private Long productId;

    @OneToOne
    @JoinColumn(name = "productId", referencedColumnName = "productId") // Explicitly define referencedColumnName
    private Product product;

    private Integer quantity;
    private Integer reorderLevel;

    // Constructor to easily create a Stock object from a Product
    public Stock(Product product, Integer quantity, Integer reorderLevel) {
        this.productId = product.getProductId();
        this.product = product;
        this.quantity = quantity;
        this.reorderLevel = reorderLevel;
    }
}


//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import com.fasterxml.jackson.annotation.JsonBackReference;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//public class Stock {
//
//    @Id
//    private Long productId; // Primary key is also the foreign key to Product
//
//    @OneToOne(fetch = FetchType.LAZY) // One-to-one relationship with Product
//    @JoinColumn(name = "productId", referencedColumnName = "productId") // Link to Product's primary key
//    @MapsId // Ensures that productId is both primary key and foreign key
//    @JsonBackReference // Essential for bidirectional JSON serialization
//    private Product product;
//
//    private Integer quantity;
//    private Integer reorderLevel;
//
//    // Optional: Convenience constructor
//    public Stock(Product product, Integer quantity, Integer reorderLevel) {
//        this.product = product;
//        this.productId = product.getProductId(); // Set productId explicitly
//        this.quantity = quantity;
//        this.reorderLevel = reorderLevel;
//    }
//}
