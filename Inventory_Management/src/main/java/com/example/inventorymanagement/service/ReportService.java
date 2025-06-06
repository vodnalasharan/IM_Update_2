package com.example.inventorymanagement.service;

import com.example.inventorymanagement.exception.ResourceNotFoundException;
import com.example.inventorymanagement.model.Report;
import com.example.inventorymanagement.model.Product;
import com.example.inventorymanagement.model.Order;
import com.example.inventorymanagement.model.Stock;
import com.example.inventorymanagement.model.Supplier;
import com.example.inventorymanagement.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private StockService stockService;
    @Autowired
    private SupplierService supplierService;

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public Report getReportById(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report", "id", id));
    }

    public Report generateInventoryReport(LocalDate startDate, LocalDate endDate) {
        List<Product> products = productService.getAllProducts();
        StringBuilder reportData = new StringBuilder("Inventory Report (" + startDate + " - " + endDate + "):\n\n");
        reportData.append(String.format("%-10s %-30s %-15s %-15s %-15s\n", "ID", "Name", "Price", "Stock Level", "Reorder Level"));
        reportData.append("----------------------------------------------------------------------------------\n");

        for (Product product : products) {
            try {
                Stock stock = stockService.getStockByProductId(product.getProductId());
                reportData.append(String.format("%-10d %-30s %-15.2f %-15d %-15d\n",
                        product.getProductId(),
                        product.getName(),
                        product.getPrice(),
                        stock.getQuantity(),
                        stock.getReorderLevel()));
            } catch (ResourceNotFoundException e) {
                // Handle cases where a product might not have a corresponding stock entry (shouldn't happen with updated addProduct)
                log.warn("No stock entry found for product ID: {}", product.getProductId());
                reportData.append(String.format("%-10d %-30s %-15.2f %-15s %-15s (No Stock Data)\n",
                        product.getProductId(),
                        product.getName(),
                        product.getPrice(),
                        "N/A", "N/A"));
            }
        }

        Report report = new Report();
        report.setReportType("Inventory");
        report.setStartDate(startDate);
        report.setEndDate(endDate);
        report.setData(reportData.toString());
        return reportRepository.save(report);
    }

    public Report generateOrderReport(LocalDate startDate, LocalDate endDate) {
        List<Order> orders = orderService.getAllOrders(); // Ideally, filter by date range
        StringBuilder reportData = new StringBuilder("Order Report (" + startDate + " - " + endDate + "):\n\n");
        reportData.append(String.format("%-10s %-15s %-15s %-10s %-15s %-15s\n", "Order ID", "Customer ID", "Product ID", "Quantity", "Order Date", "Status"));
        reportData.append("------------------------------------------------------------------------------------------\n");

        orders.stream()
              .filter(order -> !order.getOrderDate().isBefore(startDate) && !order.getOrderDate().isAfter(endDate))
              .forEach(order -> reportData.append(String.format("%-10d %-15d %-15d %-10d %-15s %-15s\n",
                      order.getOrderId(),
                      order.getCustomerId(),
                      order.getProductId(),
                      order.getQuantity(),
                      order.getOrderDate(),
                      order.getStatus())));

        Report report = new Report();
        report.setReportType("Order");
        report.setStartDate(startDate);
        report.setEndDate(endDate);
        report.setData(reportData.toString());
        return reportRepository.save(report);
    }

    public Report generateSupplierReport(LocalDate startDate, LocalDate endDate) {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        StringBuilder reportData = new StringBuilder("Supplier Report (" + startDate + " - " + endDate + "):\n\n");
        reportData.append(String.format("%-12s %-30s %-40s %-30s\n", "Supplier ID", "Name", "Contact Info", "Products Supplied"));
        reportData.append("----------------------------------------------------------------------------------------------------------------------------------\n");

        suppliers.forEach(supplier -> reportData.append(String.format("%-12d %-30s %-40s %-30s\n",
                supplier.getSupplierId(),
                supplier.getName(),
                supplier.getContactInfo(),
                supplier.getProductsSupplied())));

        Report report = new Report();
        report.setReportType("Supplier");
        report.setStartDate(startDate);
        report.setEndDate(endDate);
        report.setData(reportData.toString());
        return reportRepository.save(report);
    }

    public Report saveReport(Report report) {
        return reportRepository.save(report);
    }
}