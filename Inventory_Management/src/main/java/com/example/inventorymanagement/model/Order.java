package com.example.inventorymanagement.model;


import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders") // "order" is a reserved keyword in some databases
@Getter
@Setter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private Long customerId; // Assuming you'll handle customer details separately
    private Long productId;
    private Integer quantity;
    private LocalDate orderDate;
    private String status; // Pending, Shipped, Delivered
}