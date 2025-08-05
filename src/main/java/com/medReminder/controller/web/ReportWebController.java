package com.medReminder.controller.web;

import com.medReminder.entity.Patient;
import com.medReminder.entity.Report;
import com.medReminder.service.PatientService;
import com.medReminder.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/reports")
@RequiredArgsConstructor
@Slf4j
public class ReportWebController {

    private final ReportService reportService;
    private final PatientService patientService;

    @GetMapping
    public String showReportsList(Model model) {
        List<Patient> patients = patientService.getPatientsForCurrentUser();
        model.addAttribute("patients", patients);
        return "report/list";
    }

    @GetMapping("/patient/{patientId}")
    public String showReportsForPatient(@PathVariable Long patientId, Model model) {
        List<Report> reports = reportService.getReportsByPatientId(patientId);
        Patient patient = patientService.getPatientById(patientId);
        
        model.addAttribute("reports", reports);
        model.addAttribute("patient", patient);
        return "report/list";
    }

    @GetMapping("/{id}")
    public String showReportDetails(@PathVariable Long id, Model model) {
        Report report = reportService.getReportById(id);
        model.addAttribute("report", report);
        return "report/details";
    }

    @GetMapping("/generate")
    public String showGenerateReportForm(Model model) {
        List<Patient> patients = patientService.getPatientsForCurrentUser();
        model.addAttribute("patients", patients);
        return "report/generate";
    }
} 