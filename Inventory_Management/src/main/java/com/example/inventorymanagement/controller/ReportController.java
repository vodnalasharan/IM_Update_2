package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.model.Report;
import com.example.inventorymanagement.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        return new ResponseEntity<>(reportService.getAllReports(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable Long id) {
        Report report = reportService.getReportById(id); // Service now throws exception
        return new ResponseEntity<>(report, HttpStatus.OK);
    }

    @PostMapping("/inventory")
    public ResponseEntity<Report> generateInventoryReport(
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate) {
        Report report = reportService.generateInventoryReport(startDate, endDate);
        return new ResponseEntity<>(report, HttpStatus.OK);
    }

    @PostMapping("/order")
    public ResponseEntity<Report> generateOrderReport(
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate) {
        Report report = reportService.generateOrderReport(startDate, endDate);
        return new ResponseEntity<>(report, HttpStatus.OK);
    }

    @PostMapping("/supplier")
    public ResponseEntity<Report> generateSupplierReport(
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate) {
        Report report = reportService.generateSupplierReport(startDate, endDate);
        return new ResponseEntity<>(report, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Report> saveReport(@RequestBody Report report) {
        Report savedReport = reportService.saveReport(report);
        return new ResponseEntity<>(savedReport, HttpStatus.CREATED);
    }
}