// Order.java (Model - No Change)
package Stock_Inventory.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private Long customerId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OrderItem> orderItems;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Transient
    private Double totalAmount;

    public enum OrderStatus {
        PENDING, SHIPPED, DELIVERED, CANCELLED
    }
}


// Update -2
//package Stock_Inventory.model;
//
//import jakarta.persistence.*;
//import lombok.*;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "customer_order") // Renamed to avoid 'order' keyword conflict in some databases
//public class Order {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long orderId;
//
//    private Long customerId; // As per LLD
//
//    // Represents the "ProductID" and "Quantity" from the LLD for multiple products
//    // This establishes the one-to-many relationship from Order to OrderItem
//    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//    private List<OrderItem> orderItems;
//
//    private LocalDateTime orderDate;
//
//    @Enumerated(EnumType.STRING)
//    private OrderStatus status; // Enum for status (PENDING, SHIPPED, DELIVERED, CANCELLED)
//
//    // Derived field, not directly stored in DB, calculated for convenience in API response
//    @Transient
//    private Double totalAmount;
//
//    public enum OrderStatus {
//        PENDING, SHIPPED, DELIVERED, CANCELLED
//    }
//}