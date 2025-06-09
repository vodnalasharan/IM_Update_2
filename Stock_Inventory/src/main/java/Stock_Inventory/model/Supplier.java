// Supplier.java (Model - No Change)
package Stock_Inventory.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "supplier")
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supplierId;

    @Column(nullable = false, unique = true)
    private String name;

    private String contactInfo;

    @Column(columnDefinition = "TEXT")
    private String productsSupplied;
}



// Update-2
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
//@Table(name = "supplier")
//public class Supplier {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long supplierId;
//
//    @Column(nullable = false, unique = true)
//    private String name;
//
//    private String contactInfo;
//
//    // LLD specified "ProductsSupplied", implementing as a TEXT column.
//    // This could store a comma-separated string or a JSON array string.
//    @Column(columnDefinition = "TEXT")
//    private String productsSupplied;
//}