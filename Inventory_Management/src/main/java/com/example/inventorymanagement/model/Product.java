package com.example.inventorymanagement.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String name;
    private String description;
    private Double price;
    private Integer stockLevel; // This initial stock level will be used for the Stock entry

}

//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import com.fasterxml.jackson.annotation.JsonManagedReference;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//public class Product {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long productId;
//    private String name;
//    private String description;
//    private Double price;
//    private Integer initialStockLevel; // Use this for initial stock when product is created
//
//    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference // Essential for bidirectional JSON serialization
//    private Stock stock;
//
//    // Optional constructor for convenience
//    public Product(String name, String description, Double price, Integer initialStockLevel) {
//        this.name = name;
//        this.description = description;
//        this.price = price;
//        this.initialStockLevel = initialStockLevel;
//    }
//}
