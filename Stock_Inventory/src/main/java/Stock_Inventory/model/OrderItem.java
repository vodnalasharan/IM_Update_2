package Stock_Inventory.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonBackReference; // Important for bidirectional relationships

@Entity
@Table(name = "order_item") // Good practice to explicitly name table
@Getter // Generates all getters
@Setter // Generates all setters
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId; // This will be the 'id' in your DTO

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; // <--- This field is crucial for getProduct()/setProduct()

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference // Prevents infinite recursion when serializing Order -> OrderItems -> Order
    private Order order; // The parent Order object

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double priceAtOrder; // Price of the product at the time the order was placed
}


//// src/main/java/Stock_Inventory/model/OrderItem.java
//package Stock_Inventory.model;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import com.fasterxml.jackson.annotation.JsonIdentityInfo;
//import com.fasterxml.jackson.annotation.ObjectIdGenerators;
//
//@Entity
//@Table(name = "order_item")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "orderItemId")
//public class OrderItem {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long orderItemId;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "order_id", nullable = false)
//    @JsonBackReference // Prevents infinite recursion in JSON serialization
//    private Order order;
//
//    // As per LLD, Order entity has ProductID, not Product object directly in OrderItem
//    @Column(name = "product_id", nullable = false)
//    private Long productId;
//
//    @Column(nullable = false)
//    private Integer quantity;
//
//    @Column(nullable = false)
//    private Double priceAtOrder; // Price at the time of order
//}