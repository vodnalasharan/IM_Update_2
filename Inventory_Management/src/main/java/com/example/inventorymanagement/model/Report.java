package com.example.inventorymanagement.model;


import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;
    private String reportType; // Inventory, Order, Supplier
    private LocalDate startDate;
    private LocalDate endDate;

    @Lob // To store potentially large report data
    private String data;

}
