// OrderItem.java (Model - No Change)
package Stock_Inventory.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private Order order;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Integer quantity;

    private Double priceAtOrder;
}

// Update -2
//package Stock_Inventory.model;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import jakarta.persistence.*;
//import lombok.*;
//
//// This entity serves as the join table for orders and products,
//// allowing one order to have multiple products with their quantities.
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "order_item")
//public class OrderItem {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id; // Primary key for the order_item record itself
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "order_id", nullable = false)
//    @JsonBackReference // Prevents infinite recursion in JSON serialization
//    private Order order;
//
//    @Column(name = "product_id", nullable = false)
//    private Long productId; // Foreign key to the Product entity
//
//    @Column(nullable = false)
//    private Integer quantity; // Quantity of this specific product in the order
//
//    private Double priceAtOrder; // Price of the product at the time of order
//}