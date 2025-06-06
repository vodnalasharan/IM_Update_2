package com.example.inventorymanagement.repository;

import com.example.inventorymanagement.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    // Custom query methods (e.g., findByReportTypeAndStartDateBetween)
}