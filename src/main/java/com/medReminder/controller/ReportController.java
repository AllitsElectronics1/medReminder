package com.medReminder.controller;

import com.medReminder.entity.Report;
import com.medReminder.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<Report> generateReport(@RequestBody Report report) {
        return ResponseEntity.ok(reportService.generateReport(report));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.getReportById(id));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Report>> getReportsByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(reportService.getReportsByPatientId(patientId));
    }

    @GetMapping("/patient/{patientId}/date-range")
    public ResponseEntity<List<Report>> getReportsByDateRange(
            @PathVariable Long patientId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(reportService.getReportsByDateRange(patientId, startDate, endDate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.ok().build();
    }
} 