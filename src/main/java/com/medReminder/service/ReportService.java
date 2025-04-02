package com.medReminder.service;

import com.medReminder.entity.Report;
import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    Report generateReport(Report report);
    Report getReportById(Long id);
    List<Report> getReportsByPatientId(Long patientId);
    List<Report> getReportsByDateRange(Long patientId, LocalDate startDate, LocalDate endDate);
    void deleteReport(Long id);
} 